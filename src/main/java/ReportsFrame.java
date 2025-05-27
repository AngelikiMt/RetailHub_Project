import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.FontFactory;


public class ReportsFrame extends JFrame {

    private JTextArea outputArea;

    public ReportsFrame() {

        setTitle("RetailHub Reports");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- Φόρμα εισαγωγής ---
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel productIdLabel = new JLabel("Product ID:");
        JTextField productIdField = new JTextField();

        JLabel clientIdLabel = new JLabel("Client ID:");
        JTextField clientIdField = new JTextField();

        JLabel storeIdLabel = new JLabel("Store ID:");
        JTextField storeIdField = new JTextField();

        
        JLabel reportTypeLabel = new JLabel("Report Type:");
        ReportItem[] reportOptions = {
            new ReportItem("Sales by Product", "sales_by_product"),
            new ReportItem("Profit by Product", "profit_by_product"),
            new ReportItem("Most Profitable Products", "most_profitable_products"),
            new ReportItem("Profit by Store ID", "profit_by_store_id"),
            new ReportItem("Sales by Store", "sales_by_store"),
            new ReportItem("Client Behavior", "client_behavior")
        };
        JComboBox<ReportItem> reportTypeCombo = new JComboBox<>(reportOptions);


        JButton generateBtn = new JButton("Generate Report");
        JButton exportPdfBtn = new JButton("Export to PDF");

        formPanel.add(productIdLabel);
        formPanel.add(productIdField);
        formPanel.add(clientIdLabel);
        formPanel.add(clientIdField);
        formPanel.add(storeIdLabel);
        formPanel.add(storeIdField);
        formPanel.add(reportTypeLabel);
        formPanel.add(reportTypeCombo);
        formPanel.add(generateBtn);
        formPanel.add(exportPdfBtn);

        add(formPanel, BorderLayout.NORTH);

        // --- Περιοχή εμφάνισης αποτελεσμάτων ---
       generateBtn.addActionListener(e -> {
            ReportItem selectedItem = (ReportItem) reportTypeCombo.getSelectedItem();
            if (selectedItem == null) return;

            String reportType = selectedItem.getValue();
            String productIdText = productIdField.getText().trim();
            String clientIdText = clientIdField.getText().trim();
            String storeIdText = storeIdField.getText().trim();

            Map<String, Object> inputData = new HashMap<>();
            inputData.put("report_type", reportType);

            try {
                switch (reportType) {
                    case "sales_by_product":
                    case "profit_by_product":
                        if (productIdText.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Please enter a Product ID.");
                            return;
                        }
                        inputData.put("product_id", Long.parseLong(productIdText));
                        break;

                    case "profit_by_store_id":
                    case "sales_by_store":
                        if (storeIdText.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Please enter a Store ID.");
                            return;
                        }
                        inputData.put("store_id", Long.parseLong(storeIdText));
                        break;

                    case "client_behavior":
                        if (clientIdText.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Please enter a Client ID.");
                            return;
                        }
                        inputData.put("client_id", Long.parseLong(clientIdText));
                        break;

                    case "most_profitable_products":
                        // No ID required
                        break;

                    default:
                        JOptionPane.showMessageDialog(this, "Unsupported report type.");
                        return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. IDs must be numeric.");
                return;
            }

            outputArea.setText("Running report...\n");

            new Thread(() -> {
                String result = ReportService.getReportResults(inputData, reportType);
                SwingUtilities.invokeLater(() -> outputArea.setText(result));
            }).start();
        });



        // --- Ενέργεια κουμπιού Export to PDF ---
        exportPdfBtn.addActionListener(e -> exportTextAsPDF(outputArea.getText()));

        // --- Περιοχή εμφάνισης αποτελεσμάτων ---
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Report Output"));

        add(scrollPane, BorderLayout.CENTER); // << ΕΔΩ προσθέτεις στο layout

        setVisible(true);
    }

    private void exportTextAsPDF(String content) {
        if (content == null || content.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No report content to export.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Report as PDF");

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String path = file.getAbsolutePath();

            if (!path.toLowerCase().endsWith(".pdf")) {
                file = new File(path + ".pdf");
            }

            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();

                Font font = FontFactory.getFont(FontFactory.COURIER, 10);
                document.add(new Paragraph(content, font));

                document.close();

                JOptionPane.showMessageDialog(this, "Exported successfully to " + file.getAbsolutePath());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error exporting PDF: " + ex.getMessage());
            }
        }
    }

        public class ReportItem {
            private final String label;
            private final String value;

            public ReportItem(String label, String value) {
                this.label = label;
                this.value = value;
            }

            public String getLabel() {
                return label;
            }

            public String getValue() {
                return value;
            }

            @Override
            public String toString() {
                return label; // Εμφανίζεται στο ComboBox
            }
        }


}

