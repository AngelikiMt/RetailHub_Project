import javax.swing.*;
import java.awt.*;

import javax.swing.*;
import java.awt.*;

public class ReportsFrame extends JFrame {

    private JTextArea outputArea;

    public ReportsFrame() {
        setTitle("Generate Report");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // === Φόρμα εισαγωγής ===
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel productIdLabel = new JLabel("Product ID:");
        JTextField productIdField = new JTextField();

        JLabel reportTypeLabel = new JLabel("Report Type:");
        String[] reportOptions = {"sales_by_product"};
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

            if (productIdText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a Product ID.");
                return;
            }

            try {
                long productId = Long.parseLong(productIdText);
                outputArea.setText("Running report...\n");

                // Εκτέλεση και εμφάνιση αποτελέσματος
                // Αν θέλεις μπορείς να τρέξεις σε νέο thread για να μην παγώνει το UI
                new Thread(() -> {
                    String result = ReportService.getSalesByProductResults(productId, reportType);
                    SwingUtilities.invokeLater(() -> outputArea.setText(result));
                }).start();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Product ID.");
            }
        });

        setVisible(true);
    }
}

