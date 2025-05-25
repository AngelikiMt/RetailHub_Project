import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ReportService {

    public static String getByProductResults(long productId, String reportName) {
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
                output.append("\nΣφάλμα στην εκτέλεση του Python script (exit code: ").append(exitCode).append(")\n");
            }

            // 3. Ανάγνωση output.json
            File outputFile = new File("python-reports/io/output.json");
            if (outputFile.exists()) {
                Map<?, ?> outputData = mapper.readValue(outputFile, Map.class);
                output.append("\nΑποτελέσματα:\n");
                outputData.forEach((k, v) -> output.append(k).append(": ").append(v).append("\n"));
            } else {
                output.append("\nΔεν βρέθηκε το output.json\n");
            }

            return output.toString();

        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        }
    }
}
