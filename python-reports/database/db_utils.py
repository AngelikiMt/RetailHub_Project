import pymysql

def create_connection():
    try:
        connection = pymysql.connect(
            host='localhost',
            user='root',         # or your MySQL username
            password='password', # update with your actual password
            database='retailhub_db',
            cursorclass=pymysql.cursors.DictCursor
        )
        print("✅ Connected using PyMySQL!")
        return connection
    except Exception as e:
        print(f"❌ Connection error: {e}")
        return None
