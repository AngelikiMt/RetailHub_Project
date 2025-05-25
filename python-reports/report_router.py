#report_router.py

import json
from reports.sales_by_product import get_sales_by_product

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
        data        = load_input()
        report_type = data.get("report_type")

        if report_type == "sales_by_product":
            product_id = int(data["product_id"])         # πάντοτε int
            result     = get_sales_by_product(product_id)
            save_output(result)
        else:
            save_output({"error": f"Unsupported report_type: {report_type}"})

    except Exception as e:
        save_output({"error": str(e)})


if __name__ == "__main__":
    main()
