import pandas as pd
import matplotlib.pyplot as plt
from database.db_utils import create_connection

def plot_unique_clients_per_month(path="io/report_chart.png"):
    query = """
    SELECT
        DATE_FORMAT(dateTime, '%Y-%m-01') AS month,
        COUNT(DISTINCT clientId) AS unique_clients
    FROM transaction
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
            print("No data to plot.")
            return

        df["month"] = pd.to_datetime(df["month"])

        # Plot
        fig, ax = plt.subplots(figsize=(10, 6))
        ax.plot(df["month"], df["unique_clients"], marker='o', linewidth=3, color='green')
        for i, row in df.iterrows():
            ax.text(row["month"], row["unique_clients"] + 2, str(int(row["unique_clients"])), ha='center', fontsize=9)
        ax.set_title("Customers per Month")
        ax.set_xlabel("Month")
        ax.set_ylabel("Distinct Clients")
        ax.grid(True)
        plt.xticks(rotation=45)
        plt.tight_layout()
        plt.savefig(path, dpi=150)
        plt.close()

    except Exception as e:
        print(f"Plot error: {e}")
