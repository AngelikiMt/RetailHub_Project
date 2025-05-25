import pandas as pd
from database.db_utils import create_connection

def get_profit_by_product(product_id: int) -> dict:
    """
    Επιστρέφει για το δοσμένο productId:
      • συνολικές πωλήσεις (units)
      • συνολικά έσοδα (revenue)
      • συνολικό κόστος
      • κέρδος και περιθώριο κέρδους
    """
    query = """
    SELECT
      p.productId,
      p.description,
      SUM(i.soldQuantity)              AS total_units,
      SUM(i.soldQuantity * p.price)    AS total_revenue,
      SUM(i.soldQuantity * p.cost)     AS total_cost
    FROM includes i
    JOIN products p ON i.productId = p.productId
    WHERE p.productId = %s
    GROUP BY p.productId, p.description
    """

    try:
        conn = create_connection()
        with conn.cursor() as cur:
            cur.execute(query, (product_id,))
            rows = cur.fetchall()
        conn.close()

        if not rows:
            return {"error": f"No sales found for productId {product_id}"}

        # σε DataFrame
        df = pd.DataFrame(rows)
        # εξασφάλιση αριθμητικών τύπων
        df["total_units"]     = pd.to_numeric(df["total_units"])
        df["total_revenue"]   = pd.to_numeric(df["total_revenue"])
        df["total_cost"]      = pd.to_numeric(df["total_cost"])

        # υπολογισμοί κέρδους
        total_units   = int(df.at[0, "total_units"])
        total_revenue = float(df.at[0, "total_revenue"])
        total_cost    = float(df.at[0, "total_cost"])
        total_profit  = round(total_revenue - total_cost, 2)
        profit_margin = round(total_profit / total_revenue, 4) if total_revenue else None

        return {
            "product_id":     product_id,
            "description":    df.at[0, "description"],
            "total_units":    total_units,
            "total_revenue":  round(total_revenue, 2),
            "total_cost":     round(total_cost, 2),
            "total_profit":   total_profit,
            "profit_margin":  profit_margin
        }

    except Exception as e:
        return {"error": str(e)}
