import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.*;


// Create Table like json, with the right colums for every report
public class ReportTable {

    
    public static DefaultTableModel getTableModel(String output) {
        JSONObject json = new JSONObject(new JSONTokener(output));
        String reportType = json.optString("report_type", null);

        if(reportType==null ){
            return buildGenericTable(json);
        }
        

        switch (reportType) {

            case "sales_by_product":
                
                return buildSalesByProductTable(json);

            case "profit_by_product":
                return buildProfitByProductTable(json);

             case "most_profitable_products":
                 return buildMostProfitableProductsTable(json);
            
             case "sales_by_store":
                 return buildSalesByStoreTable(json);     

             case "profit_by_store":
                 return buildProfitByStoreTable(json);

             case "store_ranking":
                 return buildStoreRankingTable(json);

             case "client_behavior":
                 return buildClientBehaviorTable(json);

             case "category_performance":
                 return buildCategoryPerformanceTable(json);

             case "monthly_sales_trends":
                 return buildMonthlySalesTrendsTable(json);

             case "stock_vs_sales":
                 return buildStockVsSalesTable(json);

            case "profit_by_category_per_month" :
                return null;  

            case "spending_by_age_and_category"  :
                return null;

            case "uniqye_clients_per_month" :
                return null ;
            
            case "predict_category_sales":
                return null; 
            case "predict_monthly_prodits":
                return null;       

            // case "gpt_insights": It is String, we don't create Table.
                
            default:
                return buildGenericTable(json); // fallback για άγνωστες αναφορές
        }
    }


    private static DefaultTableModel buildGenericTable(JSONObject json) {
        List<String> columnsInOrder = new ArrayList<>();
        Map<String, Object> row = new LinkedHashMap<>(); 

        JSONArray names = json.names();
        for (int i = 0; i < names.length(); i++) {
            String key = names.getString(i);
            Object val = json.get(key);

            if (val instanceof JSONArray) {
                JSONArray arr = (JSONArray) val;
                boolean isStringArray = true;
                for (int j = 0; j < arr.length(); j++) {
                    if (!(arr.get(j) instanceof String)) {
                        isStringArray = false;
                        break;
                    }
                }
                if (isStringArray) {
                    StringBuilder sb = new StringBuilder();
                    for (int j = 0; j < arr.length(); j++) {
                        sb.append(arr.getString(j));
                        if (j < arr.length() - 1) sb.append(", ");
                    }
                    row.put(key, sb.toString());
                    columnsInOrder.add(key);
                }
            } else if (!(val instanceof JSONObject)) {
                row.put(key, val);
                columnsInOrder.add(key);
            }
        }

        String[] columns = columnsInOrder.toArray(new String[0]);
        Object[][] data = { row.values().toArray(new Object[0]) };

        return new DefaultTableModel(data, columns);
    }
//======================================= product ==============================================================
    
    public static DefaultTableModel buildSalesByProductTable(JSONObject json) {
        String[] columns = {
            "Product Id", "Description", "Total Units Sold", "Total Revenue", "Store", "Units"
        };

        JSONArray salesArr = json.optJSONArray("sales_per_store");
        int storeRows = (salesArr != null) ? salesArr.length() : 0;
        int totalRows = storeRows + 1;

        Object[][] data = new Object[totalRows][columns.length];

        // Πρώτη γραμμή - συνολική πληροφορία προϊόντος
        data[0][0] = json.opt("product_id");
        data[0][1] = json.opt("description");
        data[0][2] = json.opt("total_units_sold");
        data[0][3] = json.opt("total_revenue");
        data[0][4] = "";
        data[0][5] = "";

        // Λίστα καταστημάτων
        for (int i = 0; i < storeRows; i++) {
            JSONObject store = salesArr.getJSONObject(i);
            data[i + 1][0] = "";
            data[i + 1][1] = "";
            data[i + 1][2] = "";
            data[i + 1][3] = "";
            data[i + 1][4] = store.optString("store");
            data[i + 1][5] = store.optInt("units");
        }
        
        return new DefaultTableModel(data, columns);
    }

    

    private static DefaultTableModel buildProfitByProductTable(JSONObject json) {
        String[] columns = {
            "Product Id", "Description", "Total Units",
            "Total Revenue", "Total Cost", "Total Profit", "Profit Margin"
        };

        String[] jsonKeys = {
            "product_id", "description", "total_units",
            "total_revenue", "total_cost", "total_profit", "profit_margin"
        };

        Object[][] data = new Object[1][columns.length];
        for (int i = 0; i < columns.length; i++) {
            Object value = json.opt(jsonKeys[i]);
            data[0][i] = (value != null) ? value.toString() : "";
        }

        return new DefaultTableModel(data, columns);
    }



    private static DefaultTableModel buildMostProfitableProductsTable(JSONObject json) {
        String[] columns = {
            "Product Id", "Description", "Category", "Total Units",
            "Total Revenue", "Total Cost", "Total Profit", "Profit Margin"
        };

        String[] keys = {
            "productId", "description", "category", "total_units",
            "total_revenue", "total_cost", "total_profit", "profit_margin"
        };

        JSONArray arr = json.optJSONArray("top_profitable_products");
        if (arr == null || arr.length() == 0) {
            return new DefaultTableModel(new Object[][] {{"No data", "", "", "", "", "", "", ""}}, columns);
        }

        Object[][] data = new Object[arr.length()][columns.length];

        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            for (int j = 0; j < keys.length; j++) {
                Object value = obj.opt(keys[j]);
                if ("profit_margin".equals(keys[j]) && value instanceof Number) {
                    value = String.format("%.2f%%", ((Number) value).doubleValue() * 100);
                }
                data[i][j] = value;
            }
        }

        return new DefaultTableModel(data, columns);
    }


//============================================== store ==================================================


    private static DefaultTableModel buildSalesByStoreTable(JSONObject json) {
        // Στήλες πίνακα
        String[] columns = {
            "Store Id", "Address", "Product", "Units Sold", "Total Units Sold", "Total Revenue"
        };

        JSONArray topProducts = json.optJSONArray("top_products");
        int productRows = (topProducts != null) ? topProducts.length() : 0;
        int totalRows = productRows + 1; // μία για το header

        Object[][] data = new Object[totalRows][columns.length];

        // Πρώτη γραμμή - συνολική πληροφορία καταστήματος
        data[0][0] = json.opt("store_id");
        data[0][1] = json.opt("address");
        data[0][2] = "—";
        data[0][3] = "—";
        data[0][4] = json.opt("total_units_sold");
        data[0][5] = json.opt("total_revenue");

        // Λεπτομέρειες προϊόντων
        for (int i = 0; i < productRows; i++) {
            JSONObject product = topProducts.getJSONObject(i);
            data[i + 1][0] = "";
            data[i + 1][1] = "";
            data[i + 1][2] = product.optString("description");
            data[i + 1][3] = product.optInt("units");
            data[i + 1][4] = "";
            data[i + 1][5] = "";
        }

        return new DefaultTableModel(data, columns);
    }

    private static DefaultTableModel buildProfitByStoreTable(JSONObject json) {
        String[] columns = {
            "Store Id", "Address", "Product", "Units", "Revenue", "Cost", "Profit",
            "Total Units Sold", "Total Revenue", "Total Cost", "Total Profit"
        };

        JSONArray products = json.optJSONArray("product_profit_analysis");
        int productRows = (products != null) ? products.length() : 0;
        int totalRows = productRows + 1;  // 1 για τη γραμμή των συνολικών

        Object[][] data = new Object[totalRows][columns.length];

        // Συνολικά στοιχεία - πρώτη γραμμή
        data[0][0] = json.opt("store_id");
        data[0][1] = json.opt("address");
        data[0][2] = "";
        data[0][3] = "";
        data[0][4] = "";
        data[0][5] = "";
        data[0][6] = "";
        data[0][7] = json.opt("total_units_sold");
        data[0][8] = json.opt("total_revenue");
        data[0][9] = json.opt("total_cost");
        data[0][10] = json.opt("total_profit");

        if (products != null && products.length() > 0) {
            for (int i = 0; i < products.length(); i++) {
                JSONObject product = products.getJSONObject(i);
                data[i + 1][0] = ""; // Αφήνουμε κενό το store_id
                data[i + 1][1] = ""; // Κενό και το address
                data[i + 1][2] = product.optString("description");
                data[i + 1][3] = product.optInt("units");
                data[i + 1][4] = product.optDouble("revenue");
                data[i + 1][5] = product.optDouble("cost");
                data[i + 1][6] = product.optDouble("profit");
                data[i + 1][7] = "";
                data[i + 1][8] = "";
                data[i + 1][9] = "";
                data[i + 1][10] = "";
            }
        } else {
            
            data[1][0] = "";
            data[1][1] = "";
            data[1][2] = "—";
            data[1][3] = 0;
            data[1][4] = 0.0;
            data[1][5] = 0.0;
            data[1][6] = 0.0;
            data[1][7] = "";
            data[1][8] = "";
            data[1][9] = "";
            data[1][10] = "";
        }

        return new DefaultTableModel(data, columns);
    }

    private static DefaultTableModel buildStoreRankingTable(JSONObject json) {
        String[] columns = {
            "Store Id", "Address", "Total Revenue", "Total Cost", "Total Profit", "Profit Margin"
        };

        JSONArray stores = json.optJSONArray("stores");
        int rowCount = (stores != null) ? stores.length() : 1;

        Object[][] data = new Object[rowCount][columns.length];

        if (stores != null && stores.length() > 0) {
            for (int i = 0; i < stores.length(); i++) {
                JSONObject store = stores.getJSONObject(i);
                data[i][0] = store.optInt("storeId");
                data[i][1] = store.optString("address");
                data[i][2] = store.optDouble("total_revenue");
                data[i][3] = store.optDouble("total_cost");
                data[i][4] = store.optDouble("total_profit");
                // Προσοχή στο margin: αν είναι null, βάζουμε "-"
                double margin = store.optDouble("profit_margin", -1);
                data[i][5] = (margin >= 0) ? margin : "-";
            }
        } else {
            // Αν δεν υπάρχουν δεδομένα
            data[0][0] = "No data";
            for (int j = 1; j < columns.length; j++) {
                data[0][j] = "";
            }
        }

        return new DefaultTableModel(data, columns);
    }

//============================================== client ===========================================

   private static DefaultTableModel buildClientBehaviorTable(JSONObject json) {
        String[] columns = {
            "Client id", "Full Name", "Total Transactions", "Total Spent", "Stores", "Discount"
        };

        JSONArray storesArr = json.optJSONArray("stores_visited");
        int storeRows = (storesArr != null) ? storesArr.length() : 0;
        int totalRows = storeRows + 1;

        Object[][] data = new Object[totalRows][columns.length];

       
        data[0][0] = json.opt("client_id");
        data[0][1] = json.opt("full_name");
        data[0][2] = json.opt("total_transactions");
        data[0][3] = json.opt("total_spent");
        data[0][4] = "";  // stores_visited 
        data[0][5] = json.optBoolean("has_discount");

        // Λίστα καταστημάτων ανά γραμμή στη συνέχεια
        if (storesArr != null && storesArr.length() > 0) {
            for (int i = 0; i < storesArr.length(); i++) {
                data[i + 1][0] = "";
                data[i + 1][1] = "";
                data[i + 1][2] = "";
                data[i + 1][3] = "";
                data[i + 1][4] = storesArr.optString(i);
                data[i + 1][5] = "";
            }
        } else {
            
            data[1][0] = "";
            data[1][1] = "";
            data[1][2] = "";
            data[1][3] = "";
            data[1][4] = "—";
            data[1][5] = "";
        }

        return new DefaultTableModel(data, columns);
    }

//============================================== category ================================================

    private static DefaultTableModel buildCategoryPerformanceTable(JSONObject json) {
        String[] columns = {
            "Category", "Units", "Revenue", "Cost", "Profit", "Margin"
        };

        JSONArray categories = json.optJSONArray("categories");
        int categoryRows = (categories != null) ? categories.length() : 0;
        int totalRows = categoryRows + 1;

        Object[][] data = new Object[totalRows][columns.length];


        if (categories != null && categories.length() > 0) {
            for (int i = 0; i < categories.length(); i++) {
                JSONObject cat = categories.getJSONObject(i);
                data[i][0] = cat.optString("category");
                data[i][1] = cat.optInt("units");
                data[i][2] = cat.optDouble("revenue");
                data[i][3] = cat.optDouble("cost");
                data[i][4] = cat.optDouble("profit");
                data[i][5] = cat.optDouble("margin");
            }
        }

        return new DefaultTableModel(data, columns);
    }

//=========================================== monthly sales ==================================================
    private static DefaultTableModel buildMonthlySalesTrendsTable(JSONObject json) {
        String[] columns = {
            "Month", "Units", "Revenue"
        };

        JSONArray monthlyStats = json.optJSONArray("monthly_stats");
        int monthlyRows = (monthlyStats != null) ? monthlyStats.length() : 0;

        
        int totalUnits = 0;
        double totalRevenue = 0.0;
        if (monthlyStats != null) {
            for (int i = 0; i < monthlyRows; i++) {
                JSONObject monthData = monthlyStats.getJSONObject(i);
                totalUnits += monthData.optInt("units", 0);
                totalRevenue += monthData.optDouble("revenue", 0.0);
            }
        }

        
        boolean totalsAreZero = (totalUnits == 0) && (totalRevenue == 0.0);

        int totalRows = totalsAreZero ? monthlyRows : monthlyRows + 1;
        Object[][] data = new Object[totalRows][columns.length];

        int startIndex = 0;
        if (!totalsAreZero) {
            data[0][0] = "Total";
            data[0][1] = totalUnits;
            data[0][2] = Math.round(totalRevenue * 100.0) / 100.0;
            startIndex = 1;
        }

        if (monthlyStats != null && monthlyRows > 0) {
            for (int i = 0; i < monthlyRows; i++) {
                JSONObject monthData = monthlyStats.getJSONObject(i);
                data[i + startIndex][0] = monthData.optString("month");
                data[i + startIndex][1] = monthData.optInt("units");
                data[i + startIndex][2] = monthData.optDouble("revenue");
            }
        } else if (totalRows == 0) {
           
            data = new Object[1][columns.length];
            data[0][0] = "—";
            data[0][1] = 0;
            data[0][2] = 0.0;
        }

        return new DefaultTableModel(data, columns);
    }
//==============================================stock vs sales =============================================

    private static DefaultTableModel buildStockVsSalesTable(JSONObject json) {
        String[] columns = {
            "Store", "Product Id", "Description",
            "Total Stock", "Units Sold", "Remaining", "Unsold Ratio"
        };

        JSONArray analysis = json.optJSONArray("store_product_analysis");
        int rows = (analysis != null) ? analysis.length() : 0;

        Object[][] data = new Object[rows][columns.length];

        for (int i = 0; i < rows; i++) {
            JSONObject row = analysis.getJSONObject(i);
            data[i][0] = row.optString("store", "—");
            data[i][1] = row.optInt("product_id", 0);
            data[i][2] = row.optString("description", "—");
            data[i][3] = row.optInt("total_stock", 0);
            data[i][4] = row.optInt("units_sold", 0);
            data[i][5] = row.optInt("remaining", 0);
            data[i][6] = String.format("%.0f%%", row.optDouble("unsold_ratio", 0.0) * 100); 
        }

        return new DefaultTableModel(data, columns);
    }


}


 
