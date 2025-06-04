import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.FontFactory;
import java.util.List;

public class ReportsFrame extends JFrame {

    private JTextArea outputArea;
    private JLabel chartLabel;
    private static final List<String> reportsWithoutChart = new ArrayList<>(List.of("sales_by_product","profit_by_product","profit_by_store","sales_by_store","client_behavior","gpt_insights"));
    private static final List<String> reportsWithoutTable = new ArrayList<>(List.of("profit_by_category_per_month","spending_by_age_and_category","unique_clients_per_month"));
    private Boolean chartAvailable = false;
    private JTextArea descriptionArea;
    private JComponent reportContentComponent;  // π.χ. JTable ή JTextArea

    private static final Map<String, String> reportDescriptions = new HashMap<>() {{
        put("client_behavior", "Summarizes a client's purchase behavior — total spent, number of transactions, and store visits. Useful for targeting loyal or high-value customers.");
        put("sales_by_product", "Displays total sales and revenue for a specific product, with breakdown per store. Helps track where the product sells best.");
        put("sales_by_store", "Lists the top-selling products for a specific store based on revenue and units sold. Useful for understanding local demand and planning inventory.");
        put("profit_by_product", "Shows profit analysis for a single product, including sales, cost, and margin. Useful for evaluating how well that product performs financially.");
        put("profit_by_store", "Breaks down a store’s total sales and profit by product. Helps understand which items drive profit and which may be hurting performance.");
        put("store_ranking", "Ranks all stores based on total profit and margin. Helps identify top-performing and underperforming store locations.");
        put("monthly_sales_trends", "Shows monthly sales trends in units and revenue over time. Helps track business growth and spot seasonal patterns.");
        put("stock_vs_sales", "Highlights the 20 products with the most unsold stock across all stores. Useful for spotting overstock issues and adjusting inventory.");
        put("gpt_insights", "Analyzes key reports using GPT to generate strategic suggestions. Helps executives make smarter decisions based on trends and customer behavior.");
        put("category_performance", "Shows total sales, cost, and profit for each product category. Helps compare which categories perform best overall.");
        put("most_profitable_products", "Shows the top 10 products with the highest profit margins, based on total sales, cost, and revenue. Helps RetailHub see which items bring the most profit.");
        put("profit_by_category_per_month", "Shows total profit by product category per month. Helps identify the best and worst performing product categories, as well as seasonal trends.");
        put("spending_by_age_and_category", "Displays total profit per product category by age group. Helps gauge the popularity of each product category by age group and allows for  targeted marketing toward the right age group for each product category.");
        put("unique_clients_per_month","This chart shows the number of unique customers who made purchases each month. It helps track overall client activity and highlights peaks, drops, and seasonal engagement patterns.");
    }};


    public ReportsFrame() {
        setTitle("RetailHub Reports");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

       // --- Νέα Φόρμα Εισαγωγής ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Report Input"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // --- Report Type Label ---
        JLabel reportTypeTitle = new JLabel("Select Report Type:");
        reportTypeTitle.setFont(new java.awt.Font("SansSerif", Font.BOLD, 14));
        formPanel.add(reportTypeTitle, gbc);

        // --- Report Type ComboBox ---
        gbc.gridx = 1;
        ReportItem[] reportOptions = {
            new ReportItem("Sales by Product", "sales_by_product"),
            new ReportItem("Profit by Product", "profit_by_product"),
            new ReportItem("Most Profitable Products", "most_profitable_products"),
            new ReportItem("Profit by Store ID", "profit_by_store"),
            new ReportItem("Sales by Store", "sales_by_store"),
            new ReportItem("Store Ranking", "store_ranking"),
            new ReportItem("Client Behavior", "client_behavior"),
            new ReportItem("Clients Per Month", "unique_clients_per_month"),
            new ReportItem("Profit By Age & Category", "spending_by_age_and_category"), 
            new ReportItem("Stock vs Sales", "stock_vs_sales"),
            new ReportItem("Monthly Sales Trends", "monthly_sales_trends"),
            new ReportItem("Profit Category Per Month", "profit_by_category_per_month"),
            new ReportItem("Category Performance", "category_performance"),
            new ReportItem("GPT Suggestions", "gpt_insights")
        };

        JComboBox<ReportItem> reportTypeCombo = new JComboBox<>(reportOptions);
        formPanel.add(reportTypeCombo, gbc);

        // --- Product ID ---
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel productIdLabel = new JLabel("Product ID:");
        formPanel.add(productIdLabel, gbc);

        gbc.gridx = 1;
        JTextField productIdField = new JTextField(15);
        formPanel.add(productIdField, gbc);

        // --- Store ID ---
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel storeIdLabel = new JLabel("Store ID:");
        formPanel.add(storeIdLabel, gbc);

        gbc.gridx = 1;
        JTextField storeIdField = new JTextField(15);
        formPanel.add(storeIdField, gbc);

        // --- Client ID ---
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel clientIdLabel = new JLabel("Client ID:");
        formPanel.add(clientIdLabel, gbc);

        gbc.gridx = 1;
        JTextField clientIdField = new JTextField(15);
        formPanel.add(clientIdField, gbc);

        //--περιγραφη--
        this.descriptionArea = new JTextArea(3, 30);
        this.descriptionArea.setWrapStyleWord(true);
        this.descriptionArea.setLineWrap(true);
        this.descriptionArea.setEditable(false);
        this.descriptionArea.setFont(new java.awt.Font("Arial", Font.ITALIC, 12));
        this.descriptionArea.setBorder(BorderFactory.createTitledBorder("Description"));
        formPanel.add(this.descriptionArea, gbc);

        // --- Buttons ---
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        JButton generateBtn = new JButton("Generate Report");
        formPanel.add(generateBtn, gbc);

        gbc.gridx = 1;
        JButton exportPdfBtn = new JButton("Export to PDF");
        formPanel.add(exportPdfBtn, gbc);

        // --- Αρχική Απόκρυψη ---
        productIdLabel.setVisible(false);
        productIdField.setVisible(false);
        storeIdLabel.setVisible(false);
        storeIdField.setVisible(false);
        clientIdLabel.setVisible(false);
        clientIdField.setVisible(false);

        // --- Listener επιλογής ---
        reportTypeCombo.addActionListener(e -> {
            String selected = ((ReportItem) reportTypeCombo.getSelectedItem()).getValue();

            productIdLabel.setVisible(selected.equals("sales_by_product") || selected.equals("profit_by_product"));
            productIdField.setVisible(selected.equals("sales_by_product") || selected.equals("profit_by_product"));

            storeIdLabel.setVisible(selected.equals("sales_by_store") || selected.equals("profit_by_store"));
            storeIdField.setVisible(selected.equals("sales_by_store") || selected.equals("profit_by_store"));

            clientIdLabel.setVisible(selected.equals("client_behavior"));
            clientIdField.setVisible(selected.equals("client_behavior"));

            String description = reportDescriptions.getOrDefault(selected, "No description available.");
            descriptionArea.setText(description);

            formPanel.revalidate();
            formPanel.repaint();
        });

        // --- Προσθήκη φόρμας στο πάνω μέρος ---
        add(formPanel, BorderLayout.NORTH);

        formPanel.add(productIdLabel);
        formPanel.add(productIdField);
        formPanel.add(clientIdLabel);
        formPanel.add(clientIdField);
        formPanel.add(storeIdLabel);
        formPanel.add(storeIdField);
        formPanel.add(reportTypeTitle);
        formPanel.add(reportTypeCombo);
        formPanel.add(generateBtn);
        formPanel.add(exportPdfBtn);

        add(formPanel, BorderLayout.NORTH);
//======== πλαισιο κειμενου αναφορας -> πινακας  ==========================
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        JScrollPane textScrollPane = new JScrollPane(outputArea);
        textScrollPane.setBorder(BorderFactory.createTitledBorder("Report Output"));
//=================== Πλαίσιο περιγραφής===================================
        descriptionArea.setEditable(false);
        descriptionArea.setFont(new java.awt.Font("Arial", java.awt.Font.ITALIC, 13));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(BorderFactory.createTitledBorder("Description"));

        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);

        // Ομαδοποίηση πίνακα + περιγραφή σε κάθετο panel
        JPanel textPanel = new JPanel(new BorderLayout(20, 20));
        textPanel.add(textScrollPane, BorderLayout.CENTER);
        textPanel.add(descriptionScrollPane, BorderLayout.NORTH);
// ================== πλαισιο γραφηματων αναφορας =====================
        chartLabel = new JLabel();
        chartLabel.setHorizontalAlignment(JLabel.CENTER);
        chartLabel.setVerticalAlignment(JLabel.CENTER);
        JScrollPane imageScrollPane = new JScrollPane(chartLabel);
        imageScrollPane.setBorder(BorderFactory.createTitledBorder("Report Chart"));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, textPanel, imageScrollPane);

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
                    case "spending_by_age_and_category":
                    case "unique_clients_per_month":
                    case "profit_by_category_per_month":
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
            //ανανεωση παραθυρου
            textPanel.add(textScrollPane, BorderLayout.CENTER);
            textPanel.add(descriptionScrollPane, BorderLayout.NORTH);
            splitPane.setLeftComponent(textPanel);
            splitPane.setRightComponent(imageScrollPane);
            splitPane.setDividerLocation(0.5);

            //εμφανιζει το chart μονο σε αυτα που εχουν 
            chartAvailable = !reportsWithoutChart.contains(reportType);

            textScrollPane.setViewportView(outputArea);
            outputArea.setText("Running report...\n");

            new Thread(() -> {
                String result = ReportService.getReportResults(inputData, reportType);
                SwingUtilities.invokeLater(() -> {
                    if(reportType.equals("gpt_insights") ){
                        JTextArea textArea = new JTextArea(ReportService.formatGptInsights2(result));
                        textArea.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 13));
                        textArea.setWrapStyleWord(true);
                        textArea.setLineWrap(true);
                        textArea.setEditable(false);
                        reportContentComponent = textArea;
                        textScrollPane.setViewportView(reportContentComponent);
                        textScrollPane.setVisible(true);
                    }
                    else if(reportsWithoutTable.contains(reportType)) {
                        imageScrollPane.setViewportView(chartLabel);
                        // Δημιουργία panel με περιγραφή πάνω και γράφημα κάτω
                        JPanel chartOnlyPanel = new JPanel(new BorderLayout(10, 10));
                        chartOnlyPanel.add(descriptionScrollPane, BorderLayout.NORTH);
                        chartOnlyPanel.add(imageScrollPane, BorderLayout.CENTER);

                        // Τοποθέτηση στο splitPane (αριστερό) και άδειασμα δεξιού
                        splitPane.setLeftComponent(chartOnlyPanel);
                        splitPane.setRightComponent(null);  
                                        
                    }
                    else{
                        JTable table = new JTable(ReportTable.getTableModel(result));
                        table.setAutoCreateRowSorter(true); // ταξινομηση 
                        reportContentComponent = table;
                        textScrollPane.setViewportView(reportContentComponent);
                        textScrollPane.setVisible(true);
                        
                    }
                    

                    if(chartAvailable){
                        splitPane.setDividerLocation(0.5);
                        splitPane.setResizeWeight(0.5);
                        if(!reportsWithoutTable.contains(reportType)){
                                splitPane.setRightComponent(chartLabel);
                            }
                        
                        //  η εικόνα αποθηκεύεται εδώ
                        String chartPath = "python-reports/io/report_chart.png";
                        //wait for layout resizing
                        SwingUtilities.invokeLater(() -> {
                            loadChartImage(chartPath);
                        });
                        
                    }
                    else{
                        splitPane.setDividerLocation(1.0); //εξαφανιζουμε το chart 
                    }
                    
                });
            }).start();
            
            chartLabel.setIcon(null); // Καθαρισμός γραφήματος
                  
        });

        exportPdfBtn.addActionListener(e -> exportTextAsPDF(chartAvailable));
       



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
        //imageScrollPane.setVisible(false);
    }

//================================== αποθηκευση pdf ===================================================
    
    private void exportTextAsPDF(Boolean chartAvailable) {
        if (reportContentComponent == null && !chartAvailable) {
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
                ReportItem selectedItem = (ReportItem) ((JComboBox<?>) ((JPanel) getContentPane().getComponent(0)).getComponent(7)).getSelectedItem();
                String reportTitle = selectedItem != null ? selectedItem.getLabel() : "RetailHub Report";
                Paragraph title = new Paragraph(reportTitle + "\n\n", titleFont);
                title.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(title);

                // --- Περιγραφή ---
                com.lowagie.text.Font descFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 12);
                String descriptionText = descriptionArea.getText();
                Paragraph descParagraph = new Paragraph(descriptionText + "\n\n", descFont);
                document.add(descParagraph);

                // --- Περιεχόμενο Αναφοράς ---
                if (reportContentComponent instanceof JTextArea textArea) {
                    com.lowagie.text.Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
                    Paragraph textParagraph = new Paragraph(textArea.getText() + "\n\n", textFont);
                    document.add(textParagraph);

                } else if (reportContentComponent instanceof JTable table) {
                    PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
                    pdfTable.setWidthPercentage(100);

                    // Κεφαλίδες
                    for (int i = 0; i < table.getColumnCount(); i++) {
                        pdfTable.addCell(new PdfPCell(new Phrase(table.getColumnName(i))));
                    }

                    // Δεδομένα
                    for (int row = 0; row < table.getRowCount(); row++) {
                        for (int col = 0; col < table.getColumnCount(); col++) {
                            Object value = table.getValueAt(row, col);
                            pdfTable.addCell(new PdfPCell(new Phrase(value != null ? value.toString() : "")));
                        }
                    }

                    document.add(pdfTable);
                    document.add(new Paragraph("\n"));
                }

                // --- Εικόνα / Διάγραμμα ---
                String chartPath = "python-reports/io/report_chart.png";
                File imageFile = new File(chartPath);
                if (imageFile.exists() && chartAvailable) {
                    com.lowagie.text.Image chartImage = com.lowagie.text.Image.getInstance(imageFile.getAbsolutePath());
                    chartImage.scaleToFit(500, 400);
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
