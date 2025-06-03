import pandas as pd
import matplotlib.pyplot as plt
from database.db_utils import create_connection

def plot_profit_by_category_per_month(path="io/report_chart.png"):
    query = """
    SELECT
        DATE_FORMAT(t.dateTime, '%Y-%m-01') AS month,
        p.category,
        SUM(i.soldQuantity * p.price) AS revenue,
        SUM(i.soldQuantity * p.cost)  AS cost
    FROM transaction t
    JOIN includes i ON t.transactionId = i.transactionId
    JOIN products p ON i.productId = p.productId
    GROUP BY month, p.category
    ORDER BY month ASC
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
        df["profit"] = df["revenue"] - df["cost"]
        df = df[df["category"].isin(["clothing", "beauty", "electronics"])]

        pivot_df = df.pivot(index="month", columns="category", values="profit").fillna(0)

        # Plot
        # Plot (taller aspect ratio for clarity)
        pivot_df.plot(kind="line", figsize=(10, 8), marker='o')  # width=10, height=8

        plt.title("Monthly Profit by Product Category")
        plt.xlabel("Month")
        plt.ylabel("Profit (€)")
        plt.grid(True)
        plt.xticks(rotation=45)
        plt.tight_layout()
        plt.savefig(path, dpi=150)
        plt.close()

    except Exception as e:
        print(f"Plot error: {e}")


def get_profit_by_category_per_month() -> dict:
    query = """
    SELECT
        DATE_FORMAT(t.dateTime, '%Y-%m-01') AS month,
        p.category,
        SUM(i.soldQuantity * p.price) AS revenue,
        SUM(i.soldQuantity * p.cost)  AS cost
    FROM transaction t
    JOIN includes i ON t.transactionId = i.transactionId
    JOIN products p ON i.productId = p.productId
    GROUP BY month, p.category
    ORDER BY month ASC
    """

    try:
        conn = create_connection()
        with conn.cursor() as cur:
            cur.execute(query)
            rows = cur.fetchall()
        conn.close()

        df = pd.DataFrame(rows)
        if df.empty:
            return {"monthly_profit_by_category": []}

        df["month"] = pd.to_datetime(df["month"]).dt.strftime("%Y-%m")
        df["profit"] = df["revenue"] - df["cost"]

        grouped = df.groupby(["month", "category"])["profit"].sum().reset_index()
        result = grouped.pivot(index="month", columns="category", values="profit").fillna(0)

        return {
            "monthly_profit_by_category": result.reset_index().to_dict(orient="records")
        }

    except Exception as e:
        return {"error": str(e)}
