import pandas as pd
from database.db_utils import create_connection


def get_sales_by_product(product_id: int) -> dict:
    """
    Επιστρέφει συνολικές πωλήσεις και έσοδα για το δοσμένο productId.
    """
    query = """
    SELECT
        p.productId,
        p.description,
        s.address   AS store,
        i.soldQuantity,
        p.price
    FROM includes i
    JOIN products    p ON i.productId     = p.productId
    JOIN transaction t ON i.transactionId = t.transactionId
    JOIN Store       s ON t.storeId       = s.storeId
    WHERE p.productId = %s
    """

    try:
        conn = create_connection()
        with conn.cursor() as cur:
            cur.execute(query, (product_id,))
            rows = cur.fetchall()
        conn.close()

        if not rows:
            return {"error": f"No sales found for productId {product_id}"}

        # -------------------- σε DataFrame --------------------
        df = pd.DataFrame(rows)
        df["soldQuantity"] = pd.to_numeric(df["soldQuantity"])
        df["price"]        = pd.to_numeric(df["price"])

        total_units   = int(df["soldQuantity"].sum())
        total_revenue = round(float((df["soldQuantity"] * df["price"]).sum()), 2)
        description   = df["description"].iat[0]

        per_store = (
            df.groupby("store")["soldQuantity"]
              .sum()
              .reset_index()
              .rename(columns={"soldQuantity": "units"})
              .to_dict(orient="records")
        )

        return {
            "product_id": product_id,
            "description": description,
            "total_units_sold": total_units,
            "total_revenue": total_revenue,
            "sales_per_store": per_store
        }

    except Exception as e:
        return {"error": str(e)}
