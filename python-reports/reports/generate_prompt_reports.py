import json
import os
from reports.category_performance import get_category_performance
from reports.stock_vs_sales import get_stock_vs_sales
from reports.monthly_sales_trends import get_monthly_sales_trends
from reports.most_profitable_products import get_most_profitable_products
from reports.store_ranking import get_store_ranking
from reports.profit_by_category_per_month import get_profit_by_category_per_month

OUTPUT_DIR = "io"

def save_json(filename: str, data: dict):
    path = os.path.join(OUTPUT_DIR, filename)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(data, f, indent=4, ensure_ascii=False)

def generate_reports_for_gpt():
    save_json("category_performance.json", get_category_performance())
    save_json("stock_vs_sales.json", get_stock_vs_sales())
    save_json("monthly_sales_trends.json", get_monthly_sales_trends())
    save_json("most_profitable_products.json", get_most_profitable_products())
    save_json("store_ranking.json", get_store_ranking())
    save_json("get_profit_by_category_per_month.json", get_profit_by_category_per_month())
