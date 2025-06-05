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
           
            return new String(Files.readAllBytes(Paths.get("python-reports/io/output.json")));

        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        }

       
    }


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

}

