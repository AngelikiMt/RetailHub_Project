from faker import Faker
import random
from db_utils import create_connection

# ----- simulation sizes -----
N_CLIENTS            = 300
N_STORES             = 30
N_PRODUCTS           = 100
PRODUCTS_PER_STORE   = 20
N_TRANSACTIONS       = 2000
MAX_PRODUCTS_IN_TRX  = 3      

fake = Faker()
conn = create_connection()

def create_clients(n=N_CLIENTS):
    with conn.cursor() as cursor:
        for _ in range(n):
            cursor.execute("""
                INSERT INTO Client (firstName, lastName, birthDate, phoneNumber, email, gender, activeStatus)
                VALUES (%s, %s, %s, %s, %s, %s, %s)
            """, (
                fake.first_name(),
                fake.last_name(),
                fake.date_of_birth(minimum_age=18, maximum_age=75),
                fake.unique.numerify(text="##########"),
                fake.unique.email(),
                random.choice(['male', 'female', 'other']),
                random.choice([1, 0])
            ))
    conn.commit()
    print("Clients inserted")

def create_stores(n=N_STORES):
    with conn.cursor() as cursor:
        for _ in range(n):
            cursor.execute("""
                INSERT INTO Store (phone, address, country, storeName)
                VALUES (%s, %s, %s, %s)
            """, (
                fake.unique.numerify(text="##########"),
                fake.address(),
                fake.country(),
                fake.company()
            ))
    conn.commit()
    print("Stores inserted")

def create_products(n=N_PRODUCTS):
    with conn.cursor() as cursor:
        for _ in range(n):
            cursor.execute("""
                INSERT INTO products (description, price, cost, category)
                VALUES (%s, %s, %s, %s)
            """, (
                fake.word(),
                round(random.uniform(5, 200), 2),
                round(random.uniform(2, 100), 2),
                random.choice(['clothing', 'electronics', 'beauty'])
            ))
    conn.commit()
    print("Products inserted")

def create_stock_entries(n=PRODUCTS_PER_STORE):
    with conn.cursor() as cursor:
        cursor.execute("SELECT storeId FROM Store")
        store_ids = [row['storeId'] for row in cursor.fetchall()]

        cursor.execute("SELECT productId FROM products")
        product_ids = [row['productId'] for row in cursor.fetchall()]

        for store_id in store_ids:
            chosen_products = random.sample(product_ids, k=min(n, len(product_ids)))
            stock_rows = [(store_id, pid, random.randint(5, 100)) for pid in chosen_products]
            cursor.executemany(
                "INSERT INTO stock (storeId, productId, stockQuantity) VALUES (%s, %s, %s)",
                stock_rows
            )
    conn.commit()
    print("Stock entries inserted")



from datetime import datetime, timedelta

from datetime import datetime, timedelta

def create_transactions(n=N_TRANSACTIONS):
    with conn.cursor() as cursor:
        # Fetch clients and stores
        cursor.execute("SELECT clientId FROM Client WHERE activeStatus = 1")
        clients = [row['clientId'] for row in cursor.fetchall()]

        cursor.execute("SELECT storeId FROM Store")
        stores = [row['storeId'] for row in cursor.fetchall()]

        # Fetch stock data
        cursor.execute("SELECT storeId, productId, stockQuantity FROM stock WHERE stockQuantity > 0")
        stock_data = cursor.fetchall()

        stock_lookup = {}
        for row in stock_data:
            key = (row['storeId'], row['productId'])
            stock_lookup[key] = row['stockQuantity']

        # Track client spending since last discount
        client_spent = {cid: 0 for cid in clients}

        for _ in range(n):
            client_id = random.choice(clients)
            store_id = random.choice(stores)
            date = fake.date_time_between(start_date='-1y', end_date='now')
            payment_method = random.choice(['cash', 'card', 'paypal'])
            product_choices = [key for key in stock_lookup if key[0] == store_id]
            selected_products = random.sample(product_choices, k=min(3, len(product_choices)))

            if not selected_products:
                continue

            total = 0
            includes_entries = []

            for sp in selected_products:
                max_qty = stock_lookup[sp]
                if max_qty <= 0:
                    continue

                quantity = random.randint(1, min(3, max_qty))

                cursor.execute("SELECT price FROM products WHERE productId = %s", (sp[1],))
                price = cursor.fetchone()['price']
                total += price * quantity
                includes_entries.append((sp[1], quantity))

                # Reduce stock
                stock_lookup[sp] -= quantity
                cursor.execute("UPDATE stock SET stockQuantity = %s WHERE storeId = %s AND productId = %s",
                               (stock_lookup[sp], sp[0], sp[1]))

            if not includes_entries:
                continue

            # Check for discount eligibility
            discount = 0
            if client_spent[client_id] >= 400:
                discount = 20
                client_spent[client_id] = 0  # reset after giving discount
            else:
                client_spent[client_id] += total

            final_total = round(total * (1 - discount / 100), 2)

            # Insert transaction
            cursor.execute("""
                INSERT INTO transaction (clientId, storeId, dateTime, paymentMethod, sumTotal, discount)
                VALUES (%s, %s, %s, %s, %s, %s)
            """, (client_id, store_id, date, payment_method, final_total, discount))
            transaction_id = cursor.lastrowid

            # Insert includes
            for product_id, qty in includes_entries:
                cursor.execute("""
                    INSERT INTO includes (transactionId, productId, soldQuantity)
                    VALUES (%s, %s, %s)
                """, (transaction_id, product_id, qty))

    conn.commit()
    print("Transactions and includes inserted")



def update_client_totals():
    with conn.cursor() as cursor:
        cursor.execute("""
            SELECT clientId, SUM(sumTotal) AS total, MAX(dateTime) AS lastDate
            FROM transaction
            GROUP BY clientId
        """)
        client_summaries = cursor.fetchall()

        for row in client_summaries:
            cursor.execute("""
                UPDATE Client
                SET clientSumTotal = %s,
                    lastPurchaseDate = %s
                WHERE clientId = %s
            """, (round(row['total'], 2), row['lastDate'], row['clientId']))

    conn.commit()
    print("Updated clientSumTotal and lastPurchaseDate")



def main():
    create_clients()
    create_stores()
    create_products()
    create_stock_entries()
    create_transactions()
    update_client_totals()

    conn.close()
    print("Database population complete.")

if __name__ == "__main__":
    main()
