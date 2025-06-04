import pandas as pd
import matplotlib.pyplot as plt
from database.db_utils import create_connection
from statsmodels.tsa.holtwinters import ExponentialSmoothing
import warnings

warnings.filterwarnings("ignore", category=UserWarning)

def predict_monthly_profits(path="io/report_chart.png"):
    query = """
        SELECT
            DATE_FORMAT(t.dateTime, '%Y-%m') AS month,
            SUM(i.soldQuantity * (p.price - p.cost)) AS profit
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
            print("No data to plot or predict.")
            return {"monthly_profit_forecast": []}

        df["profit"] = pd.to_numeric(df["profit"])
        df["month"] = pd.to_datetime(df["month"])
        df.set_index("month", inplace=True)
        df.sort_index(inplace=True)

        model = ExponentialSmoothing(df["profit"], trend="add", seasonal=None)
        fit = model.fit()
        forecast = fit.forecast(12)

        future_months = pd.date_range(start=df.index[-1] + pd.DateOffset(months=1), periods=12, freq="MS")

        forecast_df = pd.DataFrame({
            "month": future_months,
            "predicted_profit": forecast.round(2).values
        })

        # 🔧 Plotting
        fig, ax = plt.subplots(figsize=(10, 6))
        ax.plot(forecast_df["month"], forecast_df["predicted_profit"], marker='o', linewidth=3, color='blue')
        for i, row in forecast_df.iterrows():
            ax.text(row["month"], row["predicted_profit"] + 200, f"{row['predicted_profit']:.0f}", ha='center', fontsize=8)
        ax.set_title("Predicted Monthly Profits (Next 12 Months)")
        ax.set_xlabel("Month")
        ax.set_ylabel("Profit (€)")
        ax.grid(True)
        plt.xticks(rotation=45)
        plt.tight_layout()
        plt.savefig(path, dpi=150)
        plt.close()

        return {
            "report": "predict_monthly_profits",
            "note": "Predicted profits for the next 12 months using Exponential Smoothing.",
            "monthly_profit_forecast": forecast_df.assign(month=forecast_df["month"].dt.strftime("%Y-%m")).to_dict(orient="records")
        }

    except Exception as e:
        return {"error": str(e)}
