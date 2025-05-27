#report_router.py

import json
from reports.sales_by_product import get_sales_by_product
from reports.profit_by_product import get_profit_by_product
from reports.most_profitable_products import get_most_profitable_products
from reports.sales_by_store import get_sales_by_store
from reports.profit_by_store import get_profit_by_store
from reports.client_behavior import get_client_behavior
from reports.category_performance import get_category_performance
from reports.stock_vs_sales import get_stock_vs_sales
from reports.monthly_sales_trends import get_monthly_sales_trends
from reports.store_ranking import get_store_ranking
# from reports.gpt_insights import get_gpt_insights



INPUT_PATH  = "io/input.json"
OUTPUT_PATH = "io/output.json"


def load_input() -> dict:
    with open(INPUT_PATH, encoding="utf-8") as f:
        return json.load(f)


def save_output(data: dict) -> None:
    with open(OUTPUT_PATH, "w", encoding="utf-8") as f:
        json.dump(data, f, indent=4, ensure_ascii=False)

        


def main() -> None:
    try:
        data = load_input()
        report_type = data.get("report_type")

        if report_type == "sales_by_product":
            pid = int(data["product_id"])
            result = get_sales_by_product(pid)

        elif report_type == "profit_by_product":
            pid = int(data["product_id"])
            result = get_profit_by_product(pid)
            
        elif report_type == "sales_by_store":
            store_id = int(data["store_id"])
            result = get_sales_by_store(store_id)

        elif report_type == "profit_by_store":
            store_id = int(data["store_id"])
            result = get_profit_by_store(store_id)

        elif report_type == "most_profitable_products":
            result = get_most_profitable_products()
        
        elif report_type == "client_behavior":
            client_id = int(data["client_id"])
            result = get_client_behavior(client_id)

        elif report_type == "category_performance":
            result = get_category_performance()

        elif report_type == "stock_vs_sales":
           result = get_stock_vs_sales()

        elif report_type == "monthly_sales_trends":
           result = get_monthly_sales_trends()

        elif report_type == "store_ranking":
           result = get_store_ranking()

#       elif report_type == "gpt_insights":
#            result = get_gpt_insights()

        else:
            result = {"error": f"Unsupported report_type: {report_type}"}

        save_output(result)

    except Exception as e:
        save_output({"error": str(e)})



if __name__ == "__main__":
    main()
