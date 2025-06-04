import pandas as pd
from database.db_utils import create_connection

def get_client_behavior(client_id: int) -> dict:
    """
    Επιστρέφει βασικά στατιστικά για τον πελάτη:
      • πλήρες όνομα
      • αριθμός αγορών
      • συνολικό ποσό που έχει ξοδέψει
      • λίστα καταστημάτων που έχει επισκεφθεί
      • αν έχει λάβει ποτέ έκπτωση
    """
    query = """
    SELECT
      c.firstName,
      c.lastName,
      COUNT(t.transactionId)               AS total_transactions,
      SUM(t.sumTotal)                      AS total_spent,
      MAX(t.discount)                      AS max_discount,
      GROUP_CONCAT(DISTINCT s.address SEPARATOR '|||') AS stores
    FROM Client c
    JOIN transaction t ON t.clientId = c.clientId
    JOIN Store s       ON t.storeId  = s.storeId
    WHERE c.clientId = %s
    GROUP BY c.clientId
    """

    try:
        conn = create_connection()
        with conn.cursor() as cur:
            cur.execute(query, (client_id,))
            rows = cur.fetchall()
        conn.close()

        if not rows:
            return {"error": f"No transactions found for clientId {client_id}. Client {client_id} may be inactive."}

        row = rows[0]
        full_name = f"{row['firstName']} {row['lastName']}"
        total_transactions = int(row["total_transactions"])
        total_spent = round(float(row["total_spent"]), 2)
        has_discount = float(row["max_discount"] or 0) > 0
        stores_visited = row["stores"].split("|||") if row["stores"] else []

        return {
            "report_type": "client_behavior",
            "client_id": client_id,
            "full_name": full_name,
            "total_transactions": total_transactions,
            "total_spent": total_spent,
            "stores_visited": stores_visited,
            "has_discount": has_discount
        }

    except Exception as e:
        return {"error": str(e)}
