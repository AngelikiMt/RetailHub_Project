/* UI (User Interface) for interacting with stock management */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class StockFrame extends JFrame {
    private JPanel contentPanel;

    public StockFrame() {
        setTitle("Stock Management");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen

        StoreFrame.BackgroundPanel backgroundPanel = new StoreFrame.BackgroundPanel("RETAIL1.png");
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        JPanel sidePanel = new JPanel(new GridLayout(7, 1, 10, 10));
        sidePanel.setOpaque(false);
        sidePanel.setPreferredSize(new Dimension(250, 0));

        JButton addBtn = new JButton("Add Stock");
        JButton getBtn = new JButton("Get Stock");
        JButton updateBtn = new JButton("Update Stock");
        JButton lowStockBtn = new JButton("Low Stock (<3)");
        JButton jsonBtn = new JButton("Get Stock as JSON");
        JButton searchOtherStoresBtn = new JButton("Search Other Stores");
        JButton exitBtn = new JButton("Back to Main Menu");

        for (JButton btn : new JButton[]{addBtn, getBtn, updateBtn, lowStockBtn, jsonBtn, searchOtherStoresBtn, exitBtn}) {
            btn.setFont(new Font("Arial", Font.BOLD, 16));
            sidePanel.add(btn);
        }

        contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new GridBagLayout());

        backgroundPanel.add(sidePanel, BorderLayout.WEST);
        backgroundPanel.add(contentPanel, BorderLayout.CENTER);

        addBtn.addActionListener(e -> showAddStockPanel());
        getBtn.addActionListener(e -> showGetStockPanel());
        updateBtn.addActionListener(e -> showUpdateStockPanel());
        lowStockBtn.addActionListener(e -> {
            StockService.getLowStockProducts();
            JOptionPane.showMessageDialog(this, "Check console for low stock items.");
        });
        jsonBtn.addActionListener(e -> showJsonPanel());
        searchOtherStoresBtn.addActionListener(e -> showSearchOtherStoresPanel()); // Adds listener for the new panel
        exitBtn.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void showAddStockPanel() {
        contentPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField productId = new JTextField(10);
        JTextField storeId = new JTextField(10);
        JTextField quantity = new JTextField(10);
        JButton submit = new JButton("Add");

        int y = 0;
        gbc.gridx = 0; gbc.gridy = y; contentPanel.add(new JLabel("Product ID:"), gbc);
        gbc.gridx = 1; contentPanel.add(productId, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; contentPanel.add(new JLabel("Store ID:"), gbc);
        gbc.gridx = 1; contentPanel.add(storeId, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; contentPanel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1; contentPanel.add(quantity, gbc);

        gbc.gridx = 1; gbc.gridy = ++y; contentPanel.add(submit, gbc);

        submit.addActionListener(e -> {
            try {
                StockService.addStock(
                        Integer.parseInt(storeId.getText()),
                        Long.parseLong(productId.getText()),
                        Integer.parseInt(quantity.getText())
                );
                JOptionPane.showMessageDialog(this, "Stock Added");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showGetStockPanel() {
        contentPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField productId = new JTextField(10);
        JTextField storeId = new JTextField(10);
        JButton getBtn = new JButton("Get");

        int y = 0;
        gbc.gridx = 0; gbc.gridy = y; contentPanel.add(new JLabel("Product ID:"), gbc);
        gbc.gridx = 1; contentPanel.add(productId, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; contentPanel.add(new JLabel("Store ID:"), gbc);
        gbc.gridx = 1; contentPanel.add(storeId, gbc);

        gbc.gridx = 1; gbc.gridy = ++y; contentPanel.add(getBtn, gbc);

        getBtn.addActionListener(e -> {
            try {
               Stock result = StockService.getStock(
                    Long.parseLong(productId.getText()),
                    Integer.parseInt(storeId.getText())
                );
                if (result == null) { // Check for null, not empty list
                    JOptionPane.showMessageDialog(this, "No stock found.");
                } else {
                    JOptionPane.showMessageDialog(this, "Stock quantity: " + result.getStockQuantity());
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showUpdateStockPanel() {
        contentPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField productId = new JTextField(10);
        JTextField storeId = new JTextField(10);
        JTextField quantity = new JTextField(10);
        JButton updateBtn = new JButton("Update");

        int y = 0;
        gbc.gridx = 0; gbc.gridy = y; contentPanel.add(new JLabel("Product ID:"), gbc);
        gbc.gridx = 1; contentPanel.add(productId, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; contentPanel.add(new JLabel("Store ID:"), gbc);
        gbc.gridx = 1; contentPanel.add(storeId, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; contentPanel.add(new JLabel("New Quantity:"), gbc);
        gbc.gridx = 1; contentPanel.add(quantity, gbc);

        gbc.gridx = 1; gbc.gridy = ++y; contentPanel.add(updateBtn, gbc);

        updateBtn.addActionListener(e -> {
            try {
                StockService.updateStock(
                        Long.parseLong(productId.getText()),
                        Integer.parseInt(storeId.getText()),
                        Integer.parseInt(quantity.getText())
                );
                JOptionPane.showMessageDialog(this, "Stock updated");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showJsonPanel() {
        contentPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField productId = new JTextField(10);
        JButton jsonBtn = new JButton("Get JSON");

        int y = 0;
        gbc.gridx = 0; gbc.gridy = y; contentPanel.add(new JLabel("Product ID:"), gbc);
        gbc.gridx = 1; contentPanel.add(productId, gbc);

        gbc.gridx = 1; gbc.gridy = ++y; contentPanel.add(jsonBtn, gbc);

        jsonBtn.addActionListener(e -> {
            try {
                String json = StockService.getStockAsJson(Long.parseLong(productId.getText()));
                JOptionPane.showMessageDialog(this, json);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showSearchOtherStoresPanel() {
        contentPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField productIdField = new JTextField(10);
        JTextField excludedStoreIdField = new JTextField(10);
        JButton searchBtn = new JButton("Search");

        JTextArea resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        int y = 0;
        gbc.gridx = 0; gbc.gridy = y; contentPanel.add(new JLabel("Product ID:"), gbc);
        gbc.gridx = 1; contentPanel.add(productIdField, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; contentPanel.add(new JLabel("Excluded Store ID:"), gbc);
        gbc.gridx = 1; contentPanel.add(excludedStoreIdField, gbc);

        gbc.gridx = 1; gbc.gridy = ++y; contentPanel.add(searchBtn, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; gbc.gridwidth = 2; contentPanel.add(scrollPane, gbc);

        searchBtn.addActionListener(e -> {
            try {
                long productId = Long.parseLong(productIdField.getText());
                int excludedStoreId = Integer.parseInt(excludedStoreIdField.getText());

                var stocks = StockService.searchProductInOtherStores(productId, excludedStoreId);

                if (stocks.isEmpty()) {
                    resultArea.setText("No stock found in other stores.");
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (var s : stocks) {
                        sb.append("Store ID: ").append(s.getStoreId())
                          .append(", Quantity: ").append(s.getStockQuantity())
                          .append("\n");
                    }
                    resultArea.setText(sb.toString());
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
