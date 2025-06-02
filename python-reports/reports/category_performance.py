import pandas as pd
from database.db_utils import create_connection
import matplotlib.pyplot as plt

def get_category_performance() -> dict:
    """
    Επιστρέφει KPI ανά κατηγορία προϊόντος:
      • συνολικές μονάδες
      • συνολικά έσοδα
      • συνολικό κόστος
      • καθαρό κέρδος
      • περιθώριο κέρδους
    """
    query = """
    SELECT
      p.category,
      SUM(i.soldQuantity)              AS units,
      SUM(i.soldQuantity * p.price)    AS revenue,
      SUM(i.soldQuantity * p.cost)     AS cost
    FROM includes i
    JOIN products p ON i.productId = p.productId
    GROUP BY p.category
    """

    try:
        conn = create_connection()
        with conn.cursor() as cur:
            cur.execute(query)
            rows = cur.fetchall()
        conn.close()

        df = pd.DataFrame(rows)
        if df.empty:
            return {"categories": []}

        # Μετατροπές & υπολογισμοί
        df["units"] = pd.to_numeric(df["units"])
        df["revenue"] = pd.to_numeric(df["revenue"])
        df["cost"] = pd.to_numeric(df["cost"])
        df["profit"] = df["revenue"] - df["cost"]
        df["margin"] = df.apply(
            lambda row: row["profit"] / row["revenue"] if row["revenue"] else None,
            axis=1
        )

        # Round για καθαρό output
        df["units"] = df["units"].astype(int)
        df["revenue"] = df["revenue"].round(2)
        df["cost"] = df["cost"].round(2)
        df["profit"] = df["profit"].round(2)
        df["margin"] = df["margin"].round(4)

        return {
            "report_type": "category_performance",
            "categories": df.to_dict(orient="records")
        }

    except Exception as e:
        return {"error": str(e)}




def plot_category_performance(df: pd.DataFrame, path="io/report_chart.png"):
    if df.empty:
        print("No data to plot.")
        return

    df[["category", "revenue", "cost", "profit"]].set_index("category").plot(
        kind="bar",
        figsize=(10, 6),
        title="Revenue, Cost, and Profit per Category"
    )
    plt.ylabel("€")
    plt.xlabel("Product Category")
    plt.xticks(rotation=45)
    plt.tight_layout()
    plt.savefig(path, dpi=150)
    plt.close()
    print(f"Chart saved as '{path}'")
