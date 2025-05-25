import pandas as pd
from database.db_utils import create_connection

def get_most_profitable_products(limit=10):
    """
    Επιστρέφει τα top προϊόντα με το υψηλότερο περιθώριο κέρδους.
    Περιλαμβάνει:
    - περιγραφή προϊόντος
    -κατηγορία προιόντος 
    - μονάδες που πουλήθηκαν
    - έσοδα
    - κόστος
    - καθαρό κέρδος
    - περιθώριο κέρδους
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
        df["total_units"]    = pd.to_numeric(df["total_units"])
        df["total_revenue"]  = pd.to_numeric(df["total_revenue"])
        df["total_cost"]     = pd.to_numeric(df["total_cost"])
        df["total_profit"]   = df["total_revenue"] - df["total_cost"]
        df["profit_margin"]  = df["total_profit"] / df["total_revenue"]
        df = df.sort_values(by="profit_margin", ascending=False)

        return {
            "top_profitable_products": df.head(limit).to_dict(orient="records")
        }

    except Exception as e:
        return {"error": str(e)}
