import pandas as pd
from database.db_utils import create_connection


def get_sales_by_store(store_id: int) -> dict:
    """
    Επιστρέφει συγκεντρωτικές πωλήσεις για το συγκεκριμένο κατάστημα:
      • συνολικές μονάδες
      • συνολικά έσοδα
      • ανάλυση προϊόντων (κατά φθίνουσα σειρά εσόδων)
    """
    query = """
    SELECT
        s.address,
        p.description,
        SUM(i.soldQuantity)              AS units,
        SUM(i.soldQuantity * p.price)    AS revenue
    FROM includes i
    JOIN products p    ON i.productId     = p.productId
    JOIN transaction t ON i.transactionId = t.transactionId
    JOIN Store s       ON t.storeId       = s.storeId
    WHERE s.storeId = %s
    GROUP BY s.address, p.description
    ORDER BY revenue DESC
    """

    try:
        conn = create_connection()
        with conn.cursor() as cur:
            cur.execute(query, (store_id,))
            rows = cur.fetchall()
        conn.close()

        if not rows:
            return {"error": f"No sales found for storeId {store_id}"}

        df = pd.DataFrame(rows)
        df["units"] = pd.to_numeric(df["units"])
        df["revenue"] = pd.to_numeric(df["revenue"])

        address = df["address"].iat[0]
        total_units = int(df["units"].sum())
        total_revenue = round(float(df["revenue"].sum()), 2)

        top_products = [
            {
                "description": row["description"],
                "units": int(row["units"]),
                "revenue": round(float(row["revenue"]), 2)
            }
            for _, row in df.iterrows()
        ]


        return {
            "store_id": store_id,
            "address": address,
            "total_units_sold": total_units,
            "total_revenue": total_revenue,
            "top_products": top_products
        }

    except Exception as e:
        return {"error": str(e)}
