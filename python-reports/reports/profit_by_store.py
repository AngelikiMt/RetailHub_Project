import pandas as pd
from database.db_utils import create_connection


def get_profit_by_store(store_id: int) -> dict:
    """
    Επιστρέφει για ένα κατάστημα:
      • συνολικές μονάδες, έσοδα, κόστος και καθαρό κέρδος
      • ανάλυση ανά προϊόν
    """
    query = """
    SELECT
        s.address,
        p.description,
        SUM(i.soldQuantity)                  AS units,
        SUM(i.soldQuantity * p.price)        AS revenue,
        SUM(i.soldQuantity * p.cost)         AS cost
    FROM includes i
    JOIN products    p ON i.productId     = p.productId
    JOIN transaction t ON i.transactionId = t.transactionId
    JOIN Store       s ON t.storeId       = s.storeId
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
        df["units"]   = pd.to_numeric(df["units"])
        df["revenue"] = pd.to_numeric(df["revenue"])
        df["cost"]    = pd.to_numeric(df["cost"])
        df["profit"]  = df["revenue"] - df["cost"]

        address         = df["address"].iat[0]
        total_units     = int(df["units"].sum())
        total_revenue   = round(float(df["revenue"].sum()), 2)
        total_cost      = round(float(df["cost"].sum()), 2)
        total_profit    = round(float(df["profit"].sum()), 2)

        product_profit_analysis = [
            {
                "description": row["description"],
                "units": int(row["units"]),
                "revenue": round(float(row["revenue"]), 2),
                "cost": round(float(row["cost"]), 2),
                "profit": round(float(row["profit"]), 2)
            }
            for _, row in df.iterrows()
        ]

        return {
            "report_type": "profit_by_store",
            "store_id": store_id,
            "address": address,
            "total_units_sold": total_units,
            "total_revenue": total_revenue,
            "total_cost": total_cost,
            "total_profit": total_profit,
            "product_profit_analysis": product_profit_analysis
        }

    except Exception as e:
        return {"error": str(e)}
