import pandas as pd
from database.db_utils import create_connection

def get_monthly_sales_trends() -> dict:
    """
    Επιστρέφει συνολικές πωλήσεις ανά μήνα:
      • μονάδες
      • έσοδα
    """
    query = """
    SELECT
      DATE_FORMAT(t.dateTime, '%Y-%m') AS month,
      SUM(i.soldQuantity)             AS units,
      SUM(i.soldQuantity * p.price)   AS revenue
    FROM transaction t
    JOIN includes i ON t.transactionId = i.transactionId
    JOIN products p ON i.productId = p.productId
    GROUP BY month
    ORDER BY month
    """

    try:
        conn = create_connection()
        with conn.cursor() as cur:
            cur.execute(query)
            rows = cur.fetchall()
        conn.close()

        df = pd.DataFrame(rows)
        if df.empty:
            return {"monthly_stats": []}

        df["units"] = pd.to_numeric(df["units"]).astype(int)
        df["revenue"] = pd.to_numeric(df["revenue"]).round(2)

        return {
            "report": "monthly_sales_trends",
            "note": "Total monthly sales (units & revenue) across all of RetailHub.",
            "monthly_stats": df.to_dict(orient="records")
        }

    except Exception as e:
        return {"error": str(e)}
