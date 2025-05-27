import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportService {

    public static String getProductResults(long productId, String reportName) {
        Map<String, Object> inputData = new HashMap<>();
        inputData.put("report_type", reportName);

        // Χρήση product_id μόνο όταν απαιτείται
        if (!"most_profitable_products".equals(reportName)) {
            inputData.put("product_id", productId);
        }

        return getReportResults(inputData, reportName);
    }

    public static String getReportResults(Map<String, Object> inputData, String reportName) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            // 1. Δημιουργία input.json
            File inputFile = new File("python-reports/io/input.json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(inputFile, inputData);

            // 2. Εκτέλεση Python script
            ProcessBuilder pb = new ProcessBuilder("python", "report_router.py");
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
            File outputFile = new File("python-reports/io/output.json");
            if (outputFile.exists()) {
                Map<?, ?> outputData = mapper.readValue(outputFile, Map.class);
                output.append("\nResults:\n");

                switch (reportName) {
                    case "most_profitable_products" -> output.append(formatMostProfitableProducts(outputData));
                    case "profit_by_store_id" -> output.append(formatProfitByStore(outputData));
                    case "client_behavior" -> output.append(formatClientBehavior(outputData));
                    case "sales_by_product" -> output.append(formatSalesByProduct(outputData));
                    case "profit_by_product" -> output.append(formatProfitByProduct(outputData));
                    case "sales_by_store" -> output.append(formatSalesByStore(outputData));
                    default -> outputData.forEach((k, v) -> output.append(k).append(": ").append(v).append("\n"));
                }

            } else {
                output.append("\nError: output.json not found\n");
            }

            return output.toString();

        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        }
    }

    private static String formatMostProfitableProducts(Map<?, ?> data) {
        StringBuilder sb = new StringBuilder();
        Object topProductsObj = data.get("top_profitable_products");

        if (topProductsObj instanceof List<?> topList) {
            int index = 1;
            for (Object item : topList) {
                if (item instanceof Map<?, ?> product) {
                    sb.append(" Product #").append(index++).append("\n");
                    sb.append(" ID: ").append(product.get("productId")).append("\n");
                    sb.append(" Description: ").append(product.get("description")).append("\n");
                    sb.append(" Category: ").append(product.get("category")).append("\n");
                    sb.append(" Sales: ").append(product.get("total_units")).append("\n");
                    sb.append(" Revenue: ").append(product.get("total_revenue")).append("\n");
                    sb.append(" Cost: ").append(product.get("total_cost")).append("\n");
                    sb.append(" Profit: ").append(product.get("total_profit")).append("\n");
                    sb.append(" Margin: ").append(product.get("profit_margin")).append("\n");
                    sb.append("--------------------------------------------------\n");
                }
            }
        } else {
            sb.append("No results found.\n");
        }

        return sb.toString();
    }

    private static String formatProfitByStore(Map<?, ?> data) {
        StringBuilder sb = new StringBuilder();

        sb.append("Store: ").append(data.get("address")).append(" (ID: ").append(data.get("store_id")).append(")\n");
        sb.append("Total Units Sold: ").append(data.get("total_units_sold")).append("\n");
        sb.append("Total Revenue: ").append(data.get("total_revenue")).append("\n");
        sb.append("Total Cost: ").append(data.get("total_cost")).append("\n");
        sb.append("Total Profit: ").append(data.get("total_profit")).append("\n\n");
        sb.append("=== Product Profit Breakdown ===\n");

        Object analysisObj = data.get("product_profit_analysis");
        if (analysisObj instanceof List<?> productList) {
            for (Object item : productList) {
                if (item instanceof Map<?, ?> product) {
                    sb.append("- ").append(product.get("description")).append("\n");
                    sb.append("  Units: ").append(product.get("units")).append("\n");
                    sb.append("  Revenue: ").append(product.get("revenue")).append("\n");
                    sb.append("  Cost: ").append(product.get("cost")).append("\n");
                    sb.append("  Profit: ").append(product.get("profit")).append("\n");
                    sb.append("---------------------------------------\n");
                }
            }
        }

        return sb.toString();
    }

    private static String formatClientBehavior(Map<?, ?> data) {
        // Έλεγχος για error πεδίο
        if (data.containsKey("error")) {
            return "Error: " + data.get("error") + "\n";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Client: ").append(data.get("full_name")).append(" (ID: ").append(data.get("client_id")).append(")\n");
        sb.append("Total Transactions: ").append(data.get("total_transactions")).append("\n");
        sb.append("Total Spent: ").append(data.get("total_spent")).append("\n");
        sb.append("Has Received Discount: ").append(data.get("has_discount")).append("\n");
        sb.append("Stores Visited:\n");

        Object storesObj = data.get("stores_visited");
        if (storesObj instanceof List<?> storeList) {
            for (Object store : storeList) {
                sb.append("  • ").append(store).append("\n");
            }
        }

        return sb.toString();
    }


    private static String formatSalesByProduct(Map<?, ?> data) {
        StringBuilder sb = new StringBuilder();

        sb.append("Product: ").append(data.get("description")).append(" (ID: ").append(data.get("product_id")).append(")\n");
        sb.append("Total Units Sold: ").append(data.get("total_units_sold")).append("\n");
        sb.append("Total Revenue: ").append(data.get("total_revenue")).append("\n");

        Object salesPerStoreObj = data.get("sales_per_store");
        if (salesPerStoreObj instanceof List<?> salesPerStoreList) {
            sb.append("\nSales per Store:\n");
            for (Object item : salesPerStoreList) {
                if (item instanceof Map<?, ?> store) {
                    sb.append("- ").append(store.get("store")).append(": ").append(store.get("units")).append(" units\n");
                }
            }
        }

        return sb.toString();
    }


    private static String formatProfitByProduct(Map<?, ?> data) {
        StringBuilder sb = new StringBuilder();

        sb.append("Product: ").append(data.get("description")).append(" (ID: ").append(data.get("product_id")).append(")\n");
        sb.append("Total Units Sold: ").append(data.get("total_units")).append("\n");
        sb.append("Total Revenue: ").append(data.get("total_revenue")).append("\n");
        sb.append("Total Cost: ").append(data.get("total_cost")).append("\n");
        sb.append("Total Profit: ").append(data.get("total_profit")).append("\n");
        sb.append("Profit Margin: ").append(data.get("profit_margin")).append("\n");

        return sb.toString();
    }


    private static String formatSalesByStore(Map<?, ?> data) {
        StringBuilder sb = new StringBuilder();

        sb.append("Store: ").append(data.get("address")).append(" (ID: ").append(data.get("store_id")).append(")\n");
        sb.append("Total Units Sold: ").append(data.get("total_units_sold")).append("\n");
        sb.append("Total Revenue: ").append(data.get("total_revenue")).append("\n");

        // Αν θέλεις να εμφανίσεις τα top_products (λίστα προϊόντων με πωλήσεις)
        Object topProductsObj = data.get("top_products");
        if (topProductsObj instanceof List<?> topProducts) {
            sb.append("\nTop Products:\n");
            for (Object item : topProducts) {
                if (item instanceof Map<?, ?> product) {
                    sb.append("- ").append(product.get("description")).append(" (Units Sold: ").append(product.get("units")).append(")\n");
                }
            }
        }

        return sb.toString();
    }


    public static String getAdvancedReportResults(String reportType, long productId, long storeId, long clientId) {
        Map<String, Object> inputData = new HashMap<>();
        inputData.put("report_type", reportType);

        switch (reportType) {
            case "sales by product":
            case "profit by product":
                inputData.put("product_id", productId);
                break;

            case "sales by store":
            case "profit by store id":
                inputData.put("store_id", storeId);
                break;

            case "client behavior":
                inputData.put("client_id", clientId);
                break;

            case "most profitable products":
                // No extra input needed
                break;

            default:
                return "Unsupported report type: " + reportType;
        }

        return getReportResults(inputData, reportType);
    }

}

