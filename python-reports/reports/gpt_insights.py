import os
import json
from openai import OpenAI
from reports.generate_prompt_reports import generate_reports_for_gpt
from dotenv import load_dotenv

def get_gpt_insights() -> dict:
    generate_reports_for_gpt()  # Δημιουργεί τα αρχεία .json για το prompt

    REPORTS_TO_LOAD = [
        "category_performance.json",
        "stock_vs_sales.json",
        "monthly_sales_trends.json",
        "most_profitable_products.json",
        "store_ranking.json"
    ]

    # 🔄 Φόρτωμα όλων των reports σε dict
    report_data = {}
    for filename in REPORTS_TO_LOAD:
        path = os.path.join("io", filename)
        if os.path.exists(path):
            with open(path, encoding="utf-8") as f:
                try:
                    report_data[filename.replace(".json", "")] = json.load(f)
                except json.JSONDecodeError:
                    report_data[filename.replace(".json", "")] = {"error": "invalid JSON"}

    # 🧠 Prompt για GPT
    full_prompt = (
    "Ακολουθούν δεδομένα από το σύστημα RetailHub.\n"
    "Παρακαλώ γράψε καθαρή ανάλυση και προτάσεις με απλό κείμενο.\n"
    "Απέφυγε bullets, σύμβολα, markdown ή ειδικούς χαρακτήρες.\n"
    "Χώρισε τις παραγράφους μόνο με Enter και κράτα το ύφος κατανοητό για παρουσίαση σε GUI εφαρμογή.\n\n"
)


    for key, value in report_data.items():
        full_prompt += f"\n[{key.upper()}]\n{json.dumps(value, indent=2, ensure_ascii=False)}\n"

    # 🤖 Κλήση GPT
    load_dotenv()
    client = OpenAI(api_key=os.getenv("OPENAI_API_KEY"))
    response = client.chat.completions.create(
        model="gpt-4o",
        messages=[
            {"role": "system", "content": "Είσαι ένας data analyst που εξηγεί τι δείχνουν τα KPIs και προτείνει ενέργειες."},
            {"role": "user", "content": full_prompt}
        ],
        max_tokens=500
    )

    gpt_reply = response.choices[0].message.content

    return {
        "report": "gpt_insights",
        "summary": "Ανάλυση βασισμένη στα πιο πρόσφατα KPIs της RetailHub.",
        "suggestions": gpt_reply
    }
