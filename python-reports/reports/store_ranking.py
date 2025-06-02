import matplotlib.pyplot as plt
import os
import pandas as pd
from database.db_utils import create_connection

def get_store_ranking() -> dict:
    """
    Επιστρέφει κατάταξη καταστημάτων με βάση:
      • συνολικά έσοδα
      • κόστος
      • καθαρό κέρδος
      • περιθώριο κέρδους
    """
    query = """
    SELECT
      s.storeId,
      s.address,
      SUM(i.soldQuantity * p.price) AS total_revenue,
      SUM(i.soldQuantity * p.cost)  AS total_cost
    FROM transaction t
    JOIN includes i ON t.transactionId = i.transactionId
    JOIN products p ON i.productId = p.productId
    JOIN Store s ON t.storeId = s.storeId
    WHERE s.active = 1
    GROUP BY s.storeId, s.address
    """

    try:
        conn = create_connection()
        with conn.cursor() as cur:
            cur.execute(query)
            rows = cur.fetchall()
        conn.close()

        df = pd.DataFrame(rows)
        if df.empty:
            return {"stores": []}

        # Μετατροπή & υπολογισμοί
        df["total_revenue"] = pd.to_numeric(df["total_revenue"])
        df["total_cost"] = pd.to_numeric(df["total_cost"])
        df["total_profit"] = df["total_revenue"] - df["total_cost"]
        df["profit_margin"] = df.apply(
            lambda row: row["total_profit"] / row["total_revenue"] if row["total_revenue"] else None,
            axis=1
        )

        # Round
        df["total_revenue"] = df["total_revenue"].round(2)
        df["total_cost"] = df["total_cost"].round(2)
        df["total_profit"] = df["total_profit"].round(2)
        df["profit_margin"] = df["profit_margin"].round(4)

        # Sort κατά κέρδος
        df = df.sort_values(by="total_profit", ascending=False)

        plot_store_ranking(df)

        return {
            "report_type": "store_ranking",
            "stores": df.to_dict(orient="records")
        }

    except Exception as e:
        return {"error": str(e)}


def plot_store_ranking(df: pd.DataFrame, path="io/report_chart.png"):
    if df.empty:
        print("No data to plot.")
        return

    os.makedirs("io", exist_ok=True)

    top_n = df.head(10)  # Προαιρετικά, δείξε τα 10 κορυφαία καταστήματα

    fig, ax = plt.subplots(figsize=(10, 6))
    bars = ax.barh(top_n["address"], top_n["total_profit"], color="dodgerblue")

    ax.set_xlabel("Total Profit (€)")
    ax.set_title("Top Stores by Total Profit")

    for bar in bars:
        width = bar.get_width()
        ax.text(width + 0.01, bar.get_y() + bar.get_height() / 2, f"{width:.2f}", va="center")

    plt.gca().invert_yaxis()
    plt.tight_layout()
    plt.savefig(path, dpi=150)
    plt.close()
    print(f"Chart saved as '{path}'")