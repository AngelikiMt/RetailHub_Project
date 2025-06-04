# 🗃️ Database Initialization & Population — RetailHub Project

This module (`populate.py`) generates realistic demo data for the RetailHub project's backend database.  
It ensures the schema is fully populated with clean, consistent, and relationally accurate data — suitable for testing, analysis, and report generation.

---

## ✅ What `populate.py` Does

1. **Clients**  
   - Creates `n=100` random clients (with first/last name, gender, birthdate, email, phone)  
   - Randomly sets their `activeStatus`

2. **Stores**  
   - Creates 5 fake store records (with address, country, phone, store name)

3. **Products**  
   - Creates 30 fake products across 3 categories: `'clothing'`, `'electronics'`, `'beauty'`  
   - Each product has price, cost, category, and active status

4. **Stock Entries**  
   - Links each store to ~10 products with random `stockQuantity`  
   - Avoids duplicate (store, product) combinations

5. **Transactions + Includes**  
   - Generates 300 transactions by active clients in random stores  
   - Each transaction has:
     - Timestamp
     - Payment method (`cash`, `card`, `paypal`)
     - 1–3 products from that store (only if in stock)
     - `includes` rows linking product + quantity sold  
   - Stock is **decremented** accordingly

6. **Client Summary Update**  
   - After all transactions, each client has their:
     - `clientSumTotal` (total amount spent)
     - `lastPurchaseDate` (most recent transaction)  
   - Updated based on actual transaction data

---

## 📂 Other Files in This Module

### `db_utils.py`
- Provides a reusable function to connect to the MySQL database using PyMySQL.
- Used by all other scripts in the folder.
- Modify if DB credentials or host change.


---

### `test_connection.py`

Simple script that:

- Connects to the database  
- Queries all rows from the `Client` table  
- Prints them to the console  

Useful for testing DB connectivity after setup.

---

## 💡 Notes

- Data is fully relational and respects all foreign key constraints  
- Quantity updates on `stock` are enforced to reflect actual sales  
- All fields follow your final DB schema structure  
- Categories, dates, discounts, and clients are varied for realism
