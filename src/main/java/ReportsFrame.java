import javax.swing.*;
import java.awt.*;

import javax.swing.*;
import java.awt.*;

public class ReportsFrame extends JFrame {

    private JTextArea outputArea;

    public ReportsFrame() {
        setTitle("RetailHub Reports");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // === Φόρμα εισαγωγής ===
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel productIdLabel = new JLabel("Product ID:");
        JTextField productIdField = new JTextField();

        JLabel reportTypeLabel = new JLabel("Report Type:");
        String[] reportOptions = {"sales_by_product", "profit_by_product","most_profitable_products"};
        JComboBox<String> reportTypeCombo = new JComboBox<>(reportOptions);

        JButton generateBtn = new JButton("Generate Report");

        formPanel.add(productIdLabel);
        formPanel.add(productIdField);
        formPanel.add(reportTypeLabel);
        formPanel.add(reportTypeCombo);
        formPanel.add(new JLabel()); // κενό κελί
        formPanel.add(generateBtn);

        add(formPanel, BorderLayout.NORTH);

        // === Περιοχή εμφάνισης αποτελεσμάτων ===
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Report Output"));

        add(scrollPane, BorderLayout.CENTER);

        // === Δράση κουμπιού ===
        generateBtn.addActionListener(e -> {
            String productIdText = productIdField.getText();
            String reportType = (String) reportTypeCombo.getSelectedItem();

            if (!"most_profitable_products".equals(reportType) && productIdText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a Product ID.");
                return;
            }

            try {
                long productId = ("most_profitable_products".equals(reportType) || productIdText.isEmpty())
                        ? 0L
                        : Long.parseLong(productIdText);

                outputArea.setText("Running report...\n");

                new Thread(() -> {
                    String result = ReportService.getProductResults(productId, reportType);
                    SwingUtilities.invokeLater(() -> outputArea.setText(result));
                }).start();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Product ID.");
            }
        });
         setVisible(true);
    }
}

