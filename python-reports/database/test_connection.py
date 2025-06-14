from db_utils import create_connection

def fetch_first_client():
    conn = create_connection()
    if not conn:
        print("❌ Database connection failed.")
        return

    try:
        with conn.cursor() as cursor:
            cursor.execute("SELECT * FROM client LIMIT 1")
            row = cursor.fetchone()

            print("✅ Connection to database successful.")
            if row:
                print("🧾 Example client in the DB:")
                print(f" {row['firstName']} {row['lastName']} ({row['email']}) - Active: {row['activeStatus']}")
            else:
                print("ℹ️ No clients found in the database.")
                print("   The DB is likely empty. Use `populate.py` or insert data manually.")

    except Exception as e:
        print(f"❌ Query error: {e}")
    finally:
        conn.close()
        print("🔒 Connection closed.")

if __name__ == "__main__":
    fetch_first_client()
