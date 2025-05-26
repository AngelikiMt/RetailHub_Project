import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ReportService {

    public static String getProductResults(long productId, String reportName) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            // 1. Δημιουργία input.json
            Map<String, Object> inputData = new HashMap<>();
            inputData.put("product_id", productId);
            inputData.put("report_type", reportName);

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

            // 3. Ανάγνωση output.json
            File outputFile = new File("python-reports/io/output.json");
            if (outputFile.exists()) {
                Map<?, ?> outputData = mapper.readValue(outputFile, Map.class);
                output.append("\nResults:\n");
                
                if ("most_profitable_products".equals(reportName)) {
                    Object topProductsObj = outputData.get("top_profitable_products");
                    if (topProductsObj instanceof java.util.List<?> topList) {
                        int index = 1;
                        for (Object item : topList) {
                            if (item instanceof Map<?, ?> product) {
                                output.append("📦 Προϊόν #").append(String.valueOf(index++)).append("\n");
                                output.append("🆔 ID: ").append(product.get("productId").toString()).append("\n");
                                output.append("📝 Περιγραφή: ").append(product.get("description").toString()).append("\n");
                                output.append("📂 Κατηγορία: ").append(product.get("category").toString()).append("\n");
                                output.append("📊 Πωλήσεις: ").append(product.get("total_units").toString()).append("\n");
                                output.append("💰 Έσοδα: ").append(product.get("total_revenue").toString()).append("\n");
                                output.append("💸 Κόστος: ").append(product.get("total_cost").toString()).append("\n");
                                output.append("📈 Κέρδος: ").append(product.get("total_profit").toString()).append("\n");
                                output.append("📉 Περιθώριο: ").append(product.get("profit_margin").toString()).append("\n");
                                output.append("--------------------------------------------------\n");
                            }
                        }
                    } else {
                        output.append("No result found.\n");
                    }
                } else {
                    // Προεπιλεγμένη απεικόνιση για άλλα reports
                    outputData.forEach((k, v) -> output.append(k).append(": ").append(v).append("\n"));
                }

            } else {
                output.append("\nError: \"output.json not found\n");
            }

            return output.toString();

        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        }
    }
}
