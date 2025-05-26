import pandas as pd
from database.db_utils import create_connection

def get_most_profitable_products(limit=10):
    """
    Επιστρέφει τα top 10 προϊόντα με το υψηλότερο περιθώριο κέρδους.

    """
    query = """
    SELECT
      p.productId,
      p.description,
      p.category,
      SUM(i.soldQuantity)              AS total_units,
      SUM(i.soldQuantity * p.price)    AS total_revenue,
      SUM(i.soldQuantity * p.cost)     AS total_cost
    FROM includes i
    JOIN products p ON i.productId = p.productId
    GROUP BY p.productId, p.description, p.category
    """

    try:
        conn = create_connection()
        with conn.cursor() as cur:
            cur.execute(query)
            rows = cur.fetchall()
        conn.close()

        df = pd.DataFrame(rows)
        if df.empty:
            return {"top_products": []}

        df["total_units"]    = pd.to_numeric(df["total_units"])
        df["total_revenue"]  = pd.to_numeric(df["total_revenue"])
        df["total_cost"]     = pd.to_numeric(df["total_cost"])
        df["total_profit"]   = df["total_revenue"] - df["total_cost"]

        df["profit_margin"] = df.apply(
            lambda row: row["total_profit"] / row["total_revenue"]
            if row["total_revenue"] else None,
            axis=1
        )

        df["total_revenue"] = df["total_revenue"].round(2)
        df["total_cost"]    = df["total_cost"].round(2)
        df["total_profit"]  = df["total_profit"].round(2)
        df["profit_margin"] = df["profit_margin"].round(4)
        df["total_units"]   = df["total_units"].astype(int)

        df = df.sort_values(by="profit_margin", ascending=False)

        return {
            "report": "most_profitable_products",
            "top_profitable_products": df.head(limit).to_dict(orient="records")
        }

    except Exception as e:
        return {"error": str(e)}
