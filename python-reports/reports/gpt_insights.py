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
        "store_ranking.json",
        "profit_by_category_per_month.json"
    ]

    # Φόρτωμα όλων των reports σε dict
    report_data = {}
    for filename in REPORTS_TO_LOAD:
        path = os.path.join("io", filename)
        if os.path.exists(path):
            with open(path, encoding="utf-8") as f:
                try:
                    report_data[filename.replace(".json", "")] = json.load(f)
                except json.JSONDecodeError:
                    report_data[filename.replace(".json", "")] = {"error": "invalid JSON"}



    # # should update prompt, it should cut to the chase, going to analysis and suggestions, not refrazing the question.
    # # also maybe now special characters are available?
    
    # full_prompt = (
    # "The following data comes from the RetailHub system.\n"
    # "Please write a clear analysis and recommendations in plain text.\n"
    # "Avoid using bullets, symbols, markdown, or special characters.\n"
    # "Separate paragraphs only with line breaks and keep the tone simple and suitable for a GUI application.\n\n"
    # )
    full_prompt = (
    "You are an experienced business analyst reviewing operational reports from RetailHub.\n"
    "Analyze the data below and provide actionable insights and strategic recommendations.\n"
    "Focus on what matters — highlight performance trends, profit opportunities, and problem areas.\n"
    "Use a clear and concise tone. You may use bullet points, symbols, and markdown for clarity.\n"
    "No need to explain what the data is — go straight to conclusions and suggestions.\n\n"
)



    for key, value in report_data.items():
        full_prompt += f"\n[{key.upper()}]\n{json.dumps(value, indent=2, ensure_ascii=False)}\n"

    # Κλήση GPT
    load_dotenv()
    client = OpenAI(api_key=os.getenv("OPENAI_API_KEY"))
    response = client.chat.completions.create(
        model="gpt-4o",
        messages=[
            {"role": "system", "content": "You are a data analyst responsible for interpreting KPIs and providing actionable recommendations."},
            {"role": "user", "content": full_prompt}
        ],
        max_tokens=500
    )

    gpt_reply = response.choices[0].message.content

    return {
        "report": "gpt_insights",
        "summary": "An analysis based on RetailHub's most recent KPIs.",
        "suggestions": gpt_reply
    }
