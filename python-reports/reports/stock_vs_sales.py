import pandas as pd
from database.db_utils import create_connection

TOP_N = 20

def get_stock_vs_sales() -> dict:
    """
    Επιστρέφει τα TOP_N προϊόντα με το υψηλότερο ποσοστό απούλητου αποθέματος (unsold ratio)
    σε όλα τα καταστήματα.
    """
    query = """
    SELECT
      s.address      AS store,
      p.productId    AS product_id,
      p.description  AS description,
      st.stockQuantity AS total_stock,
      COALESCE(SUM(i.soldQuantity), 0) AS units_sold
    FROM stock st
    JOIN Store s    ON st.storeId = s.storeId
    JOIN products p ON st.productId = p.productId
    LEFT JOIN includes i
      ON st.storeId = (
          SELECT t.storeId FROM transaction t WHERE t.transactionId = i.transactionId LIMIT 1
      )
      AND st.productId = i.productId
    GROUP BY s.address, p.productId, p.description, st.stockQuantity
    """

    try:
        conn = create_connection()
        with conn.cursor() as cur:
            cur.execute(query)
            rows = cur.fetchall()
        conn.close()

        df = pd.DataFrame(rows)
        if df.empty:
            return {"store_product_analysis": []}

        df["total_stock"] = pd.to_numeric(df["total_stock"])
        df["units_sold"] = pd.to_numeric(df["units_sold"])
        df["remaining"] = df["total_stock"] - df["units_sold"]
        df["unsold_ratio"] = df.apply(
            lambda row: round((row["remaining"] / row["total_stock"]), 2) if row["total_stock"] > 0 else None,
            axis=1
        )

        df["total_stock"] = df["total_stock"].astype(int)
        df["units_sold"] = df["units_sold"].astype(int)
        df["remaining"] = df["remaining"].astype(int)

        # Φιλτράρει μόνο τα προϊόντα με έγκυρο, μη-αρνητικό ποσοστό
        valid_df = df[df["unsold_ratio"].notnull() & (df["unsold_ratio"] > 0)]

        # Επιστρέφει έως TOP_N προϊόντα με το υψηλότερο unsold ratio
        top_unsold = valid_df.sort_values(by="unsold_ratio", ascending=False).head(TOP_N)

        return {
            "report": "stock_vs_sales",
            "top_n": min(TOP_N, len(top_unsold)),  # αν έχει π.χ. μόνο 17 έγκυρα προϊόντα, να δείξει 17
            "note": f"The {len(top_unsold)} products with the highest unsold stock percentage across all stores.",
            "store_product_analysis": top_unsold.to_dict(orient="records")
        }

        

    except Exception as e:
        return {"error": str(e)}
