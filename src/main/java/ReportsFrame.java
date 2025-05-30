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
    private JLabel chartLabel;

    public ReportsFrame() {
        setTitle("RetailHub Reports");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
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
            new ReportItem("Profit by Store ID", "profit_by_store"),
            new ReportItem("Sales by Store", "sales_by_store"),
            new ReportItem("Client Behavior", "client_behavior"),
            new ReportItem("Store Ranking", "store_ranking"),
            new ReportItem("Stock vs Sales", "stock_vs_sales"),
            new ReportItem("Monthly Sales Trends", "monthly_sales_trends"),
            new ReportItem("Category Performance", "category_performance"),
            new ReportItem("GPT Suggestions", "gpt_insights")
        };
        JComboBox<ReportItem> reportTypeCombo = new JComboBox<>(reportOptions);
        reportTypeCombo.addActionListener(e -> {
            productIdLabel.setVisible(false);
            productIdField.setVisible(false);
            storeIdLabel.setVisible(false);
            storeIdField.setVisible(false);
            clientIdLabel.setVisible(false);
            clientIdField.setVisible(false);

            String selected = ((ReportItem) reportTypeCombo.getSelectedItem()).getValue();

            productIdLabel.setVisible(selected.equals("sales_by_product") || selected.equals("profit_by_product"));
            productIdField.setVisible(selected.equals("sales_by_product") || selected.equals("profit_by_product"));

            storeIdLabel.setVisible(selected.equals("sales_by_store") || selected.equals("profit_by_store"));
            storeIdField.setVisible(selected.equals("sales_by_store") || selected.equals("profit_by_store"));

            clientIdLabel.setVisible(selected.equals("client_behavior"));
            clientIdField.setVisible(selected.equals("client_behavior"));

            formPanel.revalidate();
            formPanel.repaint();
//=============================================sos allagh gia grafima !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            // Καθαρισμός γραφήματος αν αλλάζει επιλογή σε αναφορά που δεν έχει γράφημα
           if (!(selected.equals("monthly_sales_trends") || selected.equals("most_profitable_products"))) {
             chartLabel.setIcon(null);
            }

        });

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
//======== πλαισιο κειμενου αναφορας ==========================
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 14));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        JScrollPane textScrollPane = new JScrollPane(outputArea);
        textScrollPane.setBorder(BorderFactory.createTitledBorder("Report Output"));
// ================== πλαισιο γραφηματων αναφορας =====================
        chartLabel = new JLabel();
        chartLabel.setHorizontalAlignment(JLabel.CENTER);
        chartLabel.setVerticalAlignment(JLabel.CENTER);
        JScrollPane imageScrollPane = new JScrollPane(chartLabel);
        imageScrollPane.setBorder(BorderFactory.createTitledBorder("Report Chart"));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, textScrollPane, imageScrollPane);
        splitPane.setDividerLocation(0.5);
        splitPane.setResizeWeight(0.5);

        add(splitPane, BorderLayout.CENTER);
//=============== ενεργοποιηση λειτουργιας κουμπιου για εμφανιση καταλληλησ αναφορας ===================
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

                    case "profit_by_store":
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
                    case "store_ranking":
                    case "stock_vs_sales":
                    case "monthly_sales_trends":
                    case "category_performance":
                    case "gpt_insights":
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

            if (reportType.equals("most_profitable_products")) { // !!!!!!!!!!!!!!!!!!!!!!sos allagh ======================
                new Thread(() -> {
                    String result = ReportService.getReportResults(inputData, reportType);
                    SwingUtilities.invokeLater(() -> {
                        outputArea.setText(result);

                        //  η εικόνα αποθηκεύεται εδώ
                        String chartPath = "python-reports/io/report_chart.png";
                        loadChartImage(chartPath);
                    });
                }).start();
            } else {
                chartLabel.setIcon(null); // Καθαρισμός γραφήματος
                new Thread(() -> {
                    String result = ReportService.getReportResults(inputData, reportType);
                    SwingUtilities.invokeLater(() -> outputArea.setText(result));
                }).start();
            }
        });

        exportPdfBtn.addActionListener(e -> exportTextAsPDF(outputArea.getText()));

        setVisible(true);
    }
//============================================= image ====================================================
    private void loadChartImage(String imagePath) {
        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            ImageIcon icon = new ImageIcon(imgFile.getAbsolutePath());

            // Παίρνουμε το Image από το ImageIcon
            Image img = icon.getImage();

            // Παίρνουμε το μέγεθος του JLabel (chartLabel)
            int labelWidth = chartLabel.getWidth();
            int labelHeight = chartLabel.getHeight();

            // Αν το μέγεθος είναι 0 (πριν το layout), βάζουμε default
            if (labelWidth <= 0 || labelHeight <= 0) {
                labelWidth = 400;
                labelHeight = 300;
            }

            // Κάνουμε scale την εικόνα να ταιριάζει στο JLabel με ομαλή κλιμάκωση
            Image scaledImage = img.getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);

            // Δημιουργούμε νέο ImageIcon με το scaled image
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            // Ορίζουμε το icon στο JLabel
            chartLabel.setIcon(scaledIcon);
        } else {
            chartLabel.setIcon(null);
            JOptionPane.showMessageDialog(this, "Chart image not found:\n" + imgFile.getAbsolutePath());
        }
    }

//================================== αποθηκευση pdf ===================================================
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

                // --- Τίτλος ---
                com.lowagie.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
                Paragraph title = new Paragraph("RetailHub Report\n\n", titleFont);
                title.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(title);

                // --- Περιεχόμενο κειμένου ---
                com.lowagie.text.Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
                Paragraph reportContent = new Paragraph(content + "\n\n", textFont);
                document.add(reportContent);

                // --- Προσθήκη εικόνας, αν υπάρχει ---
                String chartPath = "python-reports/io/report_chart.png";
                File imageFile = new File(chartPath);

                if (imageFile.exists()) {
                    com.lowagie.text.Image chartImage = com.lowagie.text.Image.getInstance(imageFile.getAbsolutePath());
                    chartImage.scaleToFit(500, 400); // προσαρμογή μεγέθους
                    chartImage.setAlignment(com.lowagie.text.Image.ALIGN_CENTER);
                    document.add(chartImage);
                }

                document.close();

                JOptionPane.showMessageDialog(this, "Exported successfully to " + file.getAbsolutePath());
            } catch (Exception ex) {
                ex.printStackTrace();
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
            return label;
        }
    }
}
