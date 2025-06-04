import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class ReportService {

    public static String getReportResults(Map<String, Object> inputData, String reportName) {

        
        ObjectMapper mapper = new ObjectMapper();

        try {
            // 1. Δημιουργία input.json
            File inputFile = new File("python-reports/io/input.json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(inputFile, inputData);

            // 2. Εκτέλεση Python script
            ProcessBuilder pb = new ProcessBuilder("python-reports\\venv\\Scripts\\python.exe", "report_router.py");
            pb.directory(new File("python-reports"));
            pb.redirectErrorStream(true);

            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                output.append("\nError in the Python script (exit code: ").append(exitCode).append(")\n");
            }

            // 3. Ανάγνωση αποτελέσματος από output.json
            //File outputFile = new File("python-reports/io/output.json");
            // if (outputFile.exists()) {
            //     Map<?, ?> outputData = mapper.readValue(outputFile, Map.class);
            //     output.append("\nResults:\n");

            //     switch (reportName) {
            //         case "most_profitable_products" -> output.append(formatMostProfitableProducts(outputData));
            //         case "profit_by_store" -> output.append(formatProfitByStore(outputData));
            //         case "client_behavior" -> output.append(formatClientBehavior(outputData));
            //         case "sales_by_product" -> output.append(formatSalesByProduct(outputData));
            //         case "profit_by_product" -> output.append(formatProfitByProduct(outputData));
            //         case "sales_by_store" -> output.append(formatSalesByStore(outputData));
            //         case "category_performance" -> output.append(formatCategoryPerformance(outputData));
            //         case "monthly_sales_trends" -> output.append(formatMonthlySalesTrends(outputData));
            //         case "stock_vs_sales" -> output.append(formatStockVsSales(outputData));
            //         case "store_ranking" -> output.append(formatStoreRanking(outputData));
            //         case "gpt_insights" -> output.append((formatGptInsights(outputData)));
            //         default -> outputData.forEach((k, v) -> output.append(k).append(": ").append(v).append("\n"));
            //     }

            // } else {
            //     output.append("\nError: output.json not found\n");
            // }

            //return output.toString();
            return new String(Files.readAllBytes(Paths.get("python-reports/io/output.json")));

        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        }

       
    }


// ========================== format for output.json ============================================= 
 
//============================ store =============================================== 

//     private static String formatSalesByStore(Map<?, ?> data) {
//         StringBuilder sb = new StringBuilder();

//         sb.append("Store: ").append(data.get("address"))
//         .append(" (ID: ").append(data.get("store_id")).append(")\n");
//         sb.append("Total Units Sold : ").append(data.get("total_units_sold")).append("\n");
//         sb.append("Total Revenue    : ").append(data.get("total_revenue")).append("\n");

//         Object topProductsObj = data.get("top_products");
//         if (topProductsObj instanceof List<?> topProducts && !topProducts.isEmpty()) {
//             sb.append("\nTop Products:\n\n");

//             // Headers
//             String headerFormat = "%-30s %-12s\n";
//             String rowFormat =    "%-30s %-12s\n";

//             sb.append(String.format(headerFormat, "Description", "Units Sold"));
//             sb.append(String.format(headerFormat, "------------------------------", "------------"));

//             for (Object item : topProducts) {
//                 if (item instanceof Map<?, ?> product) {
//                     sb.append(String.format(rowFormat,
//                             String.valueOf(product.get("description")),
//                             String.valueOf(product.get("units"))
//                     ));
//                 }
//             }
//         }

//         return sb.toString();
//     }



//    private static String formatProfitByStore(Map<?, ?> data) {
//         StringBuilder sb = new StringBuilder();

//         sb.append("Store: ").append(data.get("address"))
//         .append(" (ID: ").append(data.get("store_id")).append(")\n");
//         sb.append("Total Units Sold : ").append(data.get("total_units_sold")).append("\n");
//         sb.append("Total Revenue    : ").append(data.get("total_revenue")).append("\n");
//         sb.append("Total Cost       : ").append(data.get("total_cost")).append("\n");
//         sb.append("Total Profit     : ").append(data.get("total_profit")).append("\n");

//         Object analysisObj = data.get("product_profit_analysis");
//         if (analysisObj instanceof List<?> productList && !productList.isEmpty()) {
//             sb.append("\nProduct Profit Breakdown:\n\n");

//             String headerFormat = "%-30s %-6s %-10s %-10s %-10s\n";
//             String rowFormat =    "%-30s %-6s %-10s %-10s %-10s\n";

//             sb.append(String.format(headerFormat, "Description", "Units", "Revenue", "Cost", "Profit"));
//             sb.append(String.format(headerFormat, "------------------------------", "------", "----------", "----------", "----------"));

//             for (Object item : productList) {
//                 if (item instanceof Map<?, ?> product) {
//                     sb.append(String.format(rowFormat,
//                             String.valueOf(product.get("description")),
//                             String.valueOf(product.get("units")),
//                             String.valueOf(product.get("revenue")),
//                             String.valueOf(product.get("cost")),
//                             String.valueOf(product.get("profit"))
//                     ));
//                 }
//             }
//         }

//         return sb.toString();
//     }

//====================================== client ================================================================

    // private static String formatClientBehavior(Map<?, ?> data) {
    //     if (data.containsKey("error")) {
    //         return "Error: " + data.get("error") + "\n";
    //     }

    //     StringBuilder sb = new StringBuilder();
    //     sb.append("Client Behavior Report\n");
    //     sb.append("=======================\n\n");

    //     sb.append(String.format("Client ID           : %s\n", data.get("client_id")));
    //     sb.append(String.format("Full Name           : %s\n", data.get("full_name")));
    //     sb.append(String.format("Total Transactions  : %s\n", data.get("total_transactions")));
    //     sb.append(String.format("Total Spent         : %.2f\n", data.get("total_spent")));
    //     sb.append(String.format("Has Discount        : %s\n\n", data.get("has_discount")));

    //     sb.append("Stores Visited:\n");
    //     sb.append("------------------------\n");

    //     Object storesObj = data.get("stores_visited");
    //     if (storesObj instanceof List<?> storeList && !storeList.isEmpty()) {
    //         for (Object store : storeList) {
    //             sb.append(String.format(" • %s\n", store));
    //         }
    //     } else {
    //         sb.append(" (No stores recorded)\n");
    //     }

    //     return sb.toString();
    // }

// =========================  product ==========================================
//     private static String formatSalesByProduct(Map<?, ?> data) {
//         StringBuilder sb = new StringBuilder();

//         sb.append("Product Sales Report\n");
//         sb.append("====================\n\n");

//         sb.append(String.format("Product ID   : %s\n", data.get("product_id")));
//         sb.append(String.format("Description  : %s\n", data.get("description")));
//         sb.append(String.format("Units Sold   : %s\n", data.get("total_units_sold")));
//         sb.append(String.format("Total Revenue: %.2f\n", data.get("total_revenue")));

//         Object salesPerStoreObj = data.get("sales_per_store");
//         if (salesPerStoreObj instanceof List<?> salesPerStoreList) {
//             sb.append("\nSales per Store\n");
//             sb.append("----------------------------\n");
//             sb.append(String.format("%-25s %s\n", "Store", "Units Sold"));
//             sb.append(String.format("%-25s %s\n", "-------------------------", "----------"));

//             for (Object item : salesPerStoreList) {
//                 if (item instanceof Map<?, ?> store) {
//                     sb.append(String.format("%-25s %s\n", store.get("store"), store.get("units")));
//                 }
//             }
//         }

//         return sb.toString();
//     }


//     private static String formatProfitByProduct(Map<?, ?> data) {
//         StringBuilder sb = new StringBuilder();

//         sb.append("Product Profit Report\n");
//         sb.append("=====================\n\n");

//         sb.append(String.format("Product ID    : %s\n", data.get("product_id")));
//         sb.append(String.format("Description   : %s\n", data.get("description")));
//         sb.append(String.format("Units Sold    : %s\n", data.get("total_units")));
//         sb.append(String.format("Total Revenue : %.2f\n", data.get("total_revenue")));
//         sb.append(String.format("Total Cost    : %.2f\n", data.get("total_cost")));
//         sb.append(String.format("Total Profit  : %.2f\n", data.get("total_profit")));
//         sb.append(String.format("Profit Margin : %.2f%%\n", data.get("profit_margin")));

//         return sb.toString();
//     }


//    private static String formatMostProfitableProducts(Map<?, ?> data) {
//         StringBuilder sb = new StringBuilder();
//         Object topProductsObj = data.get("top_profitable_products");

//         if (topProductsObj instanceof List<?> topList && !topList.isEmpty()) {
//             sb.append("Most Profitable Products\n");
//             sb.append("========================\n\n");

//             // Table header
//             sb.append(String.format("%-4s %-10s %-25s %-15s %-6s %-10s %-10s %-10s %-8s\n",
//                     "#", "ID", "Description", "Category", "Units", "Revenue", "Cost", "Profit", "Margin"));
//             sb.append(String.format("%s\n", "-".repeat(108)));

//             int index = 1;
//             for (Object item : topList) {
//                 if (item instanceof Map<?, ?> product) {
//                     sb.append(String.format("%-4d %-10s %-25s %-15s %-6s %-10.2f %-10.2f %-10.2f %-7.2f%%\n",
//                             index++,
//                             product.get("productId"),
//                             product.get("description"),
//                             product.get("category"),
//                             product.get("total_units"),
//                             product.get("total_revenue"),
//                             product.get("total_cost"),
//                             product.get("total_profit"),
//                             product.get("profit_margin")));
//                 }
//             }
//         } else {
//             sb.append("No results found.\n");
//         }

//         return sb.toString();
//     }


//========================= category ===========================================

    // private static String formatCategoryPerformance(Map<?, ?> data) {
    //     StringBuilder sb = new StringBuilder();
    //     sb.append("=== Category Performance ===\n\n");

    //     Object categoriesObj = data.get("categories");
    //     if (categoriesObj instanceof List<?> categoryList && !categoryList.isEmpty()) {
    //         // Header πίνακα
    //         sb.append(String.format("%-20s %10s %12s %12s %12s %12s\n",
    //                 "Category", "Units Sold", "Revenue", "Cost", "Profit", "Margin (%)"));
    //         sb.append(String.format("%s\n", "-".repeat(80)));

    //         for (Object item : categoryList) {
    //             if (item instanceof Map<?, ?> category) {
    //                 String categoryName = (String) category.get("category");
    //                 int units = ((Number) category.get("units")).intValue();
    //                 double revenue = ((Number) category.get("revenue")).doubleValue();
    //                 double cost = ((Number) category.get("cost")).doubleValue();
    //                 double profit = ((Number) category.get("profit")).doubleValue();
    //                 double margin = ((Number) category.get("margin")).doubleValue();

    //                 sb.append(String.format("%-20s %10d %12.2f %12.2f %12.2f %12.2f\n",
    //                         categoryName, units, revenue, cost, profit, margin * 100));
    //             }
    //         }
    //     } else {
    //         sb.append("No category data available.\n");
    //     }

    //     return sb.toString();
    // }

//============================================ monthly sales =======================================
//    private static String formatMonthlySalesTrends(Map<?, ?> data) {
//         StringBuilder sb = new StringBuilder();
//         sb.append("=== Monthly Sales Trends ===\n");

//         Object noteObj = data.get("note");
//         sb.append(noteObj != null ? noteObj.toString() : "").append("\n\n");

//         Object statsObj = data.get("monthly_stats");
//         if (statsObj instanceof List<?> monthList && !monthList.isEmpty()) {
//             sb.append(String.format("%-10s %12s %15s\n", "Month", "Units Sold", "Total Revenue"));
//             sb.append(String.format("%s\n", "-".repeat(40)));

//             for (Object item : monthList) {
//                 if (item instanceof Map<?, ?> month) {
//                     String monthName = (String) month.get("month");
//                     int units = ((Number) month.get("units")).intValue();
//                     double revenue = ((Number) month.get("revenue")).doubleValue();

//                     sb.append(String.format("%-10s %12d %15.2f\n", monthName, units, revenue));
//                 }
//             }
//         } else {
//             sb.append("No monthly sales data available.\n");
//         }

//         return sb.toString();
//     }


//========================================= Stock (unsold ratio) ===================================
//    private static String formatStockVsSales(Map<?, ?> data) {
//         StringBuilder sb = new StringBuilder();

//         sb.append("Top ").append(data.get("top_n")).append(" Products with Highest Unsold Stock Percentage\n");
//         sb.append("Note: ").append(data.get("note")).append("\n\n");

//         Object analysisObj = data.get("store_product_analysis");
//         if (analysisObj instanceof List<?> productList && !productList.isEmpty()) {
//             // Κεφαλίδες πίνακα
//             sb.append(String.format("%-25s %-8s %-12s %-10s %-14s %-20s\n",
//                     "Description", "ID", "Total Stock", "Units Sold", "Remaining", "Unsold Ratio (%)", "Store"));
//             sb.append(String.format("%s\n", "-".repeat(95)));

//             for (Object item : productList) {
//                 if (item instanceof Map<?, ?> product) {
//                     String description = (String) product.get("description");
//                     String productId = String.valueOf(product.get("product_id"));
//                     int totalStock = ((Number) product.get("total_stock")).intValue();
//                     int unitsSold = ((Number) product.get("units_sold")).intValue();
//                     int remaining = ((Number) product.get("remaining")).intValue();
//                     double unsoldRatio = ((Number) product.get("unsold_ratio")).doubleValue() * 100;
//                     String store = String.valueOf(product.get("store"));

//                     sb.append(String.format("%-25s %-8s %-12d %-10d %-14d %-14.2f %-20s\n",
//                             description, productId, totalStock, unitsSold, remaining, unsoldRatio, store));
//                 }
//             }
//         } else {
//             sb.append("No product analysis data available.\n");
//         }

//         // return sb.toString();
//     }

//============================================ store ranking =====================================
//    private static String formatStoreRanking(Map<?, ?> data) {
//         StringBuilder sb = new StringBuilder();

//         sb.append("Store Ranking by Profitability:\n");
//         sb.append("====================================\n\n");

//         Object storesObj = data.get("stores");
//         if (storesObj instanceof List<?> storeList && !storeList.isEmpty()) {
//             // Κεφαλίδες πίνακα
//             sb.append(String.format("%-8s | %-30s | %-12s | %-12s | %-12s | %-14s\n",
//                     "StoreID", "Address", "Total Profit", "Total Revenue", "Total Cost", "Profit Margin"));
//             sb.append(String.format("%s\n", "-".repeat(95)));

//             for (Object item : storeList) {
//                 if (item instanceof Map<?, ?> store) {
//                     String storeId = String.valueOf(store.get("storeId"));
//                     String address = String.valueOf(store.get("address")).replace("\n", ", ").replace("\r", "");
//                     double totalProfit = ((Number) store.get("total_profit")).doubleValue();
//                     double totalRevenue = ((Number) store.get("total_revenue")).doubleValue();
//                     double totalCost = ((Number) store.get("total_cost")).doubleValue();
//                     double profitMargin = ((Number) store.get("profit_margin")).doubleValue();

//                     sb.append(String.format("%-8s | %-30s | %12.2f | %12.2f | %12.2f | %14.4f\n",
//                             storeId, address, totalProfit, totalRevenue, totalCost, profitMargin));
//                 }
//             }
//         } else {
//             sb.append("No store ranking data available.\n");
//         }

//         return sb.toString();
//     }




 //===================================== GPT insights ===========================================

    // private static String formatGptInsights(Map<?, ?> data) {
    //     StringBuilder sb = new StringBuilder();

    //     Object summaryObj = data.get("summary");
    //     String summary = (summaryObj instanceof String) ? (String) summaryObj : "No summary available";
    //     sb.append(summary).append("\n\n");

    //     Object suggestions = data.get("suggestions");
    //     if (suggestions != null) {
    //         sb.append(suggestions.toString());
    //     } else {
    //         sb.append("No suggestions available.");
    //     }

    //     return sb.toString();
    // }

    public static String formatGptInsights2(String jsonString) {
        String result = null;
        try {
                JSONObject json = new JSONObject(jsonString);

                if (json.has("suggestions")) {
                    Object suggestion = json.get("suggestions");

                    if (suggestion instanceof String) {
                        result = (String) suggestion;
                    }
                } else {
                    result = "No suggestions found.";
                }
            } catch (Exception e) {
               
            }
        return result;
    }


// υποχτεωρτικα πεδια για id σε συγκεκριμενες αναφορες 
    // public static String getAdvancedReportResults(String reportType, long productId, long storeId, long clientId) {
    //     Map<String, Object> inputData = new HashMap<>();
    //     inputData.put("report_type", reportType);

    //     switch (reportType) {
    //         case "sales_by_product":
    //         case "profit_by_product":
    //             inputData.put("product_id", productId);
    //             break;

    //         case "sales_by_store":
    //         case "profit_by_store":
    //             inputData.put("store_id", storeId);
    //             break;

    //         case "client_behavior":
    //             inputData.put("client_id", clientId);
    //             break;

    //         case "most_profitable_products":
    //         case "store_ranking":
    //         case "stock_vs_sales":
    //         case "monthly_sales_trends":
    //         case "category_performance":
    //         case "gpt_insights":
    //             // No extra input needed
    //             break;

    //         default:
    //             return "Unsupported report type: " + reportType;
    //     }

    //     return getReportResults(inputData, reportType);
    // }


}

