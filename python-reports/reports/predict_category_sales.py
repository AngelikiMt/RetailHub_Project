# reports/predict_category_sales.py
import pandas as pd
import matplotlib.pyplot as plt
from database.db_utils import create_connection
from statsmodels.tsa.holtwinters import ExponentialSmoothing
import warnings

warnings.filterwarnings("ignore", category=UserWarning)

TARGET_CATEGORIES = ["beauty", "electronics", "clothing"]   # ← only these

def predict_category_sales(path="io/report_chart.png"):
    query = """
        SELECT
            DATE_FORMAT(t.dateTime, '%Y-%m') AS month,
            p.category,
            SUM(i.soldQuantity) AS units_sold
        FROM transaction t
        JOIN includes i ON t.transactionId = i.transactionId
        JOIN products p ON i.productId = p.productId
        WHERE p.category IN ('beauty','electronics','clothing')
        GROUP BY category, month
        ORDER BY category, month
    """

    try:
        conn = create_connection()
        with conn.cursor() as cur:
            cur.execute(query)
            rows = cur.fetchall()
        conn.close()

        df = pd.DataFrame(rows)
        if df.empty:
            print("No data to plot or predict.")
            return {"category_sales_forecast": []}

        df["units_sold"] = pd.to_numeric(df["units_sold"])
        df["month"] = pd.to_datetime(df["month"])

        forecasts_json = {}
        plot_df_list   = []          # collect for chart

        for category in TARGET_CATEGORIES:
            cat_df = df[df["category"] == category].copy()
            if cat_df.empty:
                continue

            cat_df.set_index("month", inplace=True)
            cat_df.sort_index(inplace=True)

            # Holt-Winters (trend only)
            model = ExponentialSmoothing(cat_df["units_sold"],
                                         trend="add", seasonal=None)
            fit       = model.fit()
            forecast  = fit.forecast(12)

            future_ix = pd.date_range(start=cat_df.index[-1] + pd.DateOffset(months=1),
                                      periods=12, freq="MS")

            # ---------- JSON ----------
            forecasts_json[category] = pd.DataFrame({
                "month"         : future_ix.strftime("%Y-%m"),
                "predicted_units": forecast.round().astype(int).values
            }).to_dict(orient="records")

            # ---------- collect for plotting ----------
            tmp_plot = pd.DataFrame({
                "month"         : future_ix,
                "predicted_units": forecast,
                "category"      : category.capitalize()   # nicer label
            })
            plot_df_list.append(tmp_plot)

        # ---------- CHART ----------
        if plot_df_list:
            plot_df = pd.concat(plot_df_list)
            pivot   = plot_df.pivot(index="month",
                                    columns="category",
                                    values="predicted_units")
            pivot.plot(kind="line", figsize=(10, 6), marker='o')
            plt.title("Predicted Unit Sales per Category (Next 12 Months)")
            plt.xlabel("Month")
            plt.ylabel("Units Sold")
            plt.grid(True)
            plt.xticks(rotation=45)
            plt.tight_layout()
            plt.savefig(path, dpi=150)
            plt.close()

        return {
            "report"                 : "predict_category_sales",
            "note"                   : "Predicted units sold per category for the next 12 months.",
            "category_sales_forecast": forecasts_json
        }

    except Exception as e:
        return {"error": str(e)}
