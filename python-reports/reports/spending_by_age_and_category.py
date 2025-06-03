import pandas as pd
import matplotlib.pyplot as plt
from database.db_utils import create_connection   

def plot_total_spending_by_age_and_category(
        path="io/report_chart.png"):
    """
    Builds the grouped-bar chart:
    Total € spending per product category (beauty, clothing, electronics)
    broken down by age group.
    """
    query = """
    SELECT
        c.birthDate,
        p.category,
        i.soldQuantity * p.price AS amount
    FROM transaction t
    JOIN client c      ON t.clientId      = c.clientId
    JOIN includes i    ON t.transactionId = i.transactionId
    JOIN products p    ON i.productId     = p.productId
    WHERE p.category IN ('clothing','beauty','electronics')
          AND c.birthDate IS NOT NULL
    """

    # —— fetch data
    conn = create_connection()
    with conn.cursor() as cur:
        cur.execute(query)
        rows = cur.fetchall()
    conn.close()

    df = pd.DataFrame(rows)
    if df.empty:
        print("No data to plot.")
        return

    # —— derive age & age-group
    today = pd.Timestamp("today")
    df["age"] = ((today - pd.to_datetime(df["birthDate"])).dt.days // 365)
    bins = [18, 25, 35, 45, 60, 150]
    labels = ["18-25", "26-35", "36-45", "46-60", "60+"]
    df["age_group"] = pd.cut(df["age"], bins=bins, labels=labels, right=True)
    df = df.dropna(subset=["age_group"])

    # —— aggregate
    grouped = (df
               .groupby(["age_group", "category"], observed=True)["amount"]
               .sum()
               .reset_index())

    pivot_df = (grouped
                .pivot(index="age_group",
                       columns="category",
                       values="amount")
                .reindex(labels)        # keep age order
                .fillna(0))

    # —— plot
    pivot_df.plot(kind="bar", figsize=(10, 6))
    plt.title("Total Spending per Product Category by Age Group")
    plt.xlabel("Age Group")
    plt.ylabel("Total Amount (€)")
    plt.xticks(rotation=0)
    plt.tight_layout()
    plt.savefig(path, dpi=150)
    plt.close()
