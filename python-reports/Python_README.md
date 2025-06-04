# Python-Reports Module

This folder implements the “Java ↔ Python” report pipeline for RetailHub. When the user clicks “Generate Report” in the Java GUI, we:

1. **Write** a small JSON request → `io/input.json`  
   • Created by **Java**, based on user input (e.g. product ID)

2. **Run** the Python router script → `report_router.py`  
   • Java triggers this using `ProcessBuilder`, running the file inside `python-reports/`

3. **Produce** a JSON response → `io/output.json`  
   • Python reads the input, calls the correct report function (e.g. `get_sales_by_product()`), and writes results here

4. **Read** the output in Java and update the GUI  
   • Java reads the JSON and displays values (units, revenue, per-store breakdown)

---

## 📂 Directory Layout

├── python-reports/            # Python reporting backend
│   ├── database/              # σύνδεση & αρχικοποίηση DB
│   │   ├── DB_README.md       # οδηγίες για την MySQL
│   │   ├── db_utils.py        # create_connection()
│   │   ├── populate.py        # faker data loader
│   │   └── test_connection.py # τσεκάρει σύνδεση DB
│   │
│   ├── io/                    # input/output JSON
│   │   ├── input.json
│   │   └── output.json
│   │
│   ├── reports/               # κάθε report ως function
│   │   └── sales_by_product.py
│   │
│   ├── report_router.py       # κεντρικός “Main” για όλα τα reports
│   ├── test_reports.py        # CLI tester για report_router
│   └── venv/                  # virtualenv (εγκατεστημένες βιβλιοθήκες)