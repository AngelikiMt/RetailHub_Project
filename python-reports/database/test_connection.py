from db_utils import create_connection

def fetch_clients():
    conn = create_connection()
    if not conn:
        return

    try:
        with conn.cursor() as cursor:
            cursor.execute("SELECT * FROM Client")  # Capital 'C'
            rows = cursor.fetchall()

            print("📋 Retrieved clients:")
            for row in rows:
                print(f"🧑 {row['firstName']} {row['lastName']} ({row['email']}) - Active: {row['activeStatus']}")

    except Exception as e:
        print(f"❌ Query error: {e}")
    finally:
        conn.close()
        print("🔌 Connection closed.")

if __name__ == "__main__":
    fetch_clients()
