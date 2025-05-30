import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class StockFrame extends JFrame {
    private JPanel backgroundPanel;
    private JPanel currentContentPanel; // Holds the active sub-panel
    
    //private static StockService stockService;
    //public StockService getStockService() {
    // return stockService;
    //} 

    public StockFrame() {
        setTitle("Stock Management Menu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Closes the frame
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen

        //stockService = new StockService();

        // === PANEL WITH BACKGROUND ===
        backgroundPanel = new JPanel() {
            Image bg = new ImageIcon(getClass().getResource("RetailHub.png")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        // Initial content will be the main stock menu buttons
        showStockMainMenu();

        setContentPane(backgroundPanel);
        setVisible(true);
    }

    private void showStockMainMenu() {
        if (currentContentPanel != null) {
            backgroundPanel.remove(currentContentPanel);
        }

        // === BUTTONS ===
        JButton addBtn = new JButton("Set Initial Stock");
        JButton getBtn = new JButton("Get Stock Quantity");
        JButton updateBtn = new JButton("Add Stock");
        JButton lowStockBtn = new JButton("View Low Stock");
        JButton jsonBtn = new JButton("Get Stock as JSON");
        JButton searchOtherStoresBtn = new JButton("Search Other Stores");
        JButton backToMainBtn = new JButton("Back to Main Menu");

        Font buttonFont = new Font("Arial", Font.BOLD, 20);
        Dimension buttonSize = new Dimension(250, 60);

        JButton[] buttons = {addBtn, getBtn, updateBtn, lowStockBtn, jsonBtn, searchOtherStoresBtn, backToMainBtn};
        for (JButton btn : buttons) {
            btn.setFont(buttonFont);
            btn.setPreferredSize(buttonSize);
        }

        // === PANEL WITH GRID LAYOUT ===
        JPanel buttonGrid = new JPanel(new GridLayout(3, 3, 30, 30));
        buttonGrid.setOpaque(false); // Transparent so the background is visible
        for (JButton btn : buttons) {
            buttonGrid.add(btn);
        }

        // === CENTERING BUTTONS ===
        JPanel centerPanel = new JPanel(new GridBagLayout()); // Using GridBagLayout for true centering
        centerPanel.setOpaque(false);
        centerPanel.add(buttonGrid);

        backgroundPanel.add(centerPanel, BorderLayout.SOUTH); // Add to the center
        currentContentPanel = centerPanel;
        backgroundPanel.revalidate();
        backgroundPanel.repaint();

        // === ACTIONS ===
        addBtn.addActionListener(e -> showPanel(new AddStockPanel()));
        getBtn.addActionListener(e -> showPanel(new GetStockPanel()));
        updateBtn.addActionListener(e -> showPanel(new UpdateStockPanel()));
        lowStockBtn.addActionListener(e -> showPanel(new LowStockPanel()));
        jsonBtn.addActionListener(e -> showPanel(new GetStockJsonPanel()));
        searchOtherStoresBtn.addActionListener(e -> showPanel(new SearchOtherStoresPanel()));
        backToMainBtn.addActionListener(e -> {
            dispose(); // Closes StockFrame
        });
    }

    // Method to switch between content panels
    private void showPanel(JPanel panel) {
        if (currentContentPanel != null) {
            backgroundPanel.remove(currentContentPanel);
        }
        backgroundPanel.add(panel, BorderLayout.SOUTH);
        currentContentPanel = panel;
        backgroundPanel.revalidate();
        backgroundPanel.repaint();
    }

    // --- Abstract Base Panel for common elements like 'Back' button ---
    private abstract class StockOperationPanel extends JPanel {
        protected StockService stockService;
        protected JButton backButton;
        protected JPanel controlPanel; // Holds the back button

        public StockOperationPanel() {
            this.stockService = new StockService();
            setLayout(new BorderLayout());
            setOpaque(false); // Transparent background

            controlPanel = new JPanel(new BorderLayout());
            controlPanel.setOpaque(false);

            backButton = new JButton("Back to Stock Menu");
            backButton.setFont(new Font("Arial", Font.BOLD, 18));
            backButton.addActionListener(e -> showStockMainMenu());

            JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            backButtonPanel.setOpaque(false);
            backButtonPanel.add(backButton);
            controlPanel.add(backButtonPanel, BorderLayout.NORTH);

            add(controlPanel, BorderLayout.NORTH); // Adds the instance's controlPanel to the top
        }

        protected void showError(String message) {
            JOptionPane.showMessageDialog(this, "Error: " + message, "Error", JOptionPane.ERROR_MESSAGE);
        }

        protected void showSuccess(String message) {
            JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Add Stock Panel
    private class AddStockPanel extends StockOperationPanel {
        private JTextField productIdField, storeIdField, quantityField;

        public AddStockPanel() {
            super();
            JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
            formPanel.setOpaque(false);
            formPanel.setBorder(BorderFactory.createEmptyBorder(50, 200, 50, 200));

            productIdField = new JTextField(10);
            storeIdField = new JTextField(10);
            quantityField = new JTextField(10);

            Font labelFont = new Font("Arial", Font.PLAIN, 16);
            Font fieldFont = new Font("Arial", Font.PLAIN, 16);

            JLabel productIdLabel = new JLabel("Product ID:");
            productIdLabel.setFont(labelFont);
            JLabel storeIdLabel = new JLabel("Store ID:");
            storeIdLabel.setFont(labelFont);
            JLabel quantityLabel = new JLabel("Quantity to Add:");
            quantityLabel.setFont(labelFont);

            productIdField.setFont(fieldFont);
            storeIdField.setFont(fieldFont);
            quantityField.setFont(fieldFont);

            formPanel.add(productIdLabel);
            formPanel.add(productIdField);
            formPanel.add(storeIdLabel);
            formPanel.add(storeIdField);
            formPanel.add(quantityLabel);
            formPanel.add(quantityField);

            JButton submitBtn = new JButton("Add Stock");
            submitBtn.setFont(new Font("Arial", Font.BOLD, 18));
            submitBtn.setPreferredSize(new Dimension(180, 50));
            submitBtn.addActionListener(e -> addStock());

            JPanel buttonHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonHolder.setOpaque(false);
            buttonHolder.add(submitBtn);
            formPanel.add(new JLabel());
            formPanel.add(buttonHolder);

            add(formPanel, BorderLayout.CENTER);
        }

        private void addStock() {
            try {
                int storeId = Integer.parseInt(storeIdField.getText());
                long productId = Long.parseLong(productIdField.getText());
                int quantity = Integer.parseInt(quantityField.getText());

                StockService.addStock(storeId, productId, quantity);
                showSuccess("Stock added successfully.");
                productIdField.setText("");
                storeIdField.setText("");
                quantityField.setText("");
            } catch (NumberFormatException ex) {
                showError("Invalid number format for ID or Quantity.");
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        }
    }

    // Get Stock Panel (for a specific product in a specific store)
    private class GetStockPanel extends StockOperationPanel {
        private JTextField productIdField, storeIdField;
        private JLabel resultLabel; // To display the quantity

        public GetStockPanel() {
            super();
            JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
            formPanel.setOpaque(false);
            formPanel.setBorder(BorderFactory.createEmptyBorder(50, 200, 50, 200));

            productIdField = new JTextField(10);
            storeIdField = new JTextField(10);
            resultLabel = new JLabel("Quantity: ");
            resultLabel.setFont(new Font("Arial", Font.BOLD, 18));

            Font labelFont = new Font("Arial", Font.PLAIN, 16);
            Font fieldFont = new Font("Arial", Font.PLAIN, 16);

            JLabel productIdLabel = new JLabel("Product ID:");
            productIdLabel.setFont(labelFont);
            JLabel storeIdLabel = new JLabel("Store ID:");
            storeIdLabel.setFont(labelFont);

            productIdField.setFont(fieldFont);
            storeIdField.setFont(fieldFont);

            formPanel.add(productIdLabel);
            formPanel.add(productIdField);
            formPanel.add(storeIdLabel);
            formPanel.add(storeIdField);
            formPanel.add(new JLabel()); // Empty cell
            formPanel.add(resultLabel);

            JButton getBtn = new JButton("Get Stock");
            getBtn.setFont(new Font("Arial", Font.BOLD, 18));
            getBtn.setPreferredSize(new Dimension(180, 50));
            getBtn.addActionListener(e -> getStock());

            JPanel buttonHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonHolder.setOpaque(false);
            buttonHolder.add(getBtn);
            formPanel.add(new JLabel());
            formPanel.add(buttonHolder);

            add(formPanel, BorderLayout.CENTER);
        }

        private void getStock() {
            try {
                long productId = Long.parseLong(productIdField.getText());
                int storeId = Integer.parseInt(storeIdField.getText());

                Stock stock = StockService.getStock(productId, storeId);
                if (stock != null) {
                    resultLabel.setText("Quantity: " + stock.getStockQuantity());
                    showSuccess("Stock found.");
                } else {
                    resultLabel.setText("Quantity: N/A");
                    showError("Stock not found for Product ID " + productId + " in Store ID " + storeId + ".");
                }
            } catch (NumberFormatException ex) {
                showError("Invalid number format for ID.");
                resultLabel.setText("Quantity: N/A");
            } catch (Exception ex) {
                showError(ex.getMessage());
                resultLabel.setText("Quantity: N/A");
            }
        }
    }

    // Update Stock Panel
    private class UpdateStockPanel extends StockOperationPanel {
        private JTextField productIdField, storeIdField, quantityField;

        public UpdateStockPanel() {
            super();
            JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
            formPanel.setOpaque(false);
            formPanel.setBorder(BorderFactory.createEmptyBorder(50, 200, 50, 200));

            productIdField = new JTextField(10);
            storeIdField = new JTextField(10);
            quantityField = new JTextField(10);

            Font labelFont = new Font("Arial", Font.PLAIN, 16);
            Font fieldFont = new Font("Arial", Font.PLAIN, 16);

            JLabel productIdLabel = new JLabel("Product ID:");
            productIdLabel.setFont(labelFont);
            JLabel storeIdLabel = new JLabel("Store ID:");
            storeIdLabel.setFont(labelFont);
            JLabel quantityLabel = new JLabel("New Quantity:");
            quantityLabel.setFont(labelFont);

            productIdField.setFont(fieldFont);
            storeIdField.setFont(fieldFont);
            quantityField.setFont(fieldFont);

            formPanel.add(productIdLabel);
            formPanel.add(productIdField);
            formPanel.add(storeIdLabel);
            formPanel.add(storeIdField);
            formPanel.add(quantityLabel);
            formPanel.add(quantityField);

            JButton updateBtn = new JButton("Update Stock");
            updateBtn.setFont(new Font("Arial", Font.BOLD, 18));
            updateBtn.setPreferredSize(new Dimension(180, 50));
            updateBtn.addActionListener(e -> updateStock());

            JPanel buttonHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonHolder.setOpaque(false);
            buttonHolder.add(updateBtn);
            formPanel.add(new JLabel());
            formPanel.add(buttonHolder);

            add(formPanel, BorderLayout.CENTER);
        }

        private void updateStock() {
            try {
                long productId = Long.parseLong(productIdField.getText());
                int storeId = Integer.parseInt(storeIdField.getText());
                int quantity = Integer.parseInt(quantityField.getText());

                StockService.updateStock(productId, storeId, quantity);
                showSuccess("Stock updated successfully.");
                productIdField.setText("");
                storeIdField.setText("");
                quantityField.setText("");
            } catch (NumberFormatException ex) {
                showError("Invalid number format for ID or Quantity.");
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        }
    }

    // Low Stock Panel
    private class LowStockPanel extends StockOperationPanel {
        private JTable table;
        private DefaultTableModel model;

        public LowStockPanel() {
            super();
            setLayout(new BorderLayout()); // Set layout for this panel
            JPanel tablePanel = new JPanel(new BorderLayout());
            tablePanel.setOpaque(false);
            tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));

            model = new DefaultTableModel(new String[]{"Product ID", "Store ID", "Quantity"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Makes table non-editable
                }
            };
            table = new JTable(model);
            table.setFont(new Font("Arial", Font.PLAIN, 14));
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
            table.setRowHeight(25);

            JScrollPane scrollPane = new JScrollPane(table);
            tablePanel.add(scrollPane, BorderLayout.CENTER);

            // Add back button to the top of this panel
            JPanel topControl = new JPanel(new FlowLayout(FlowLayout.LEFT));
            topControl.setOpaque(false);
            topControl.add(backButton); // Uses the back button from the superclass
            add(topControl, BorderLayout.NORTH);

            add(tablePanel, BorderLayout.CENTER);

            refreshLowStock(); // Populate table on creation
        }

        private void refreshLowStock() {
            model.setRowCount(0); // Clearing existing data
            List<Stock> lowStocks = StockService.getLowStockProducts();
            if (lowStocks.isEmpty()) {
                showError("No low stock products found.");
            } else {
                for (Stock stock : lowStocks) {
                    model.addRow(new Object[]{
                            stock.getProductId(), stock.getStoreId(), stock.getStockQuantity()
                    });
                }
            }
        }
    }

    // Get Stock as JSON Panel
    private class GetStockJsonPanel extends StockOperationPanel {
        private JTextField productIdField;
        private JTextArea jsonDisplayArea;

        public GetStockJsonPanel() {
            super();
            JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
            inputPanel.setOpaque(false);
            inputPanel.setBorder(BorderFactory.createEmptyBorder(50, 200, 10, 200));

            productIdField = new JTextField(10);
            productIdField.setFont(new Font("Arial", Font.PLAIN, 16));

            JLabel productIdLabel = new JLabel("Product ID to View as JSON:");
            productIdLabel.setFont(new Font("Arial", Font.PLAIN, 16));

            inputPanel.add(productIdLabel);
            inputPanel.add(productIdField);

            JButton showJsonBtn = new JButton("Show JSON");
            showJsonBtn.setFont(new Font("Arial", Font.BOLD, 18));
            showJsonBtn.setPreferredSize(new Dimension(150, 50));
            showJsonBtn.addActionListener(e -> showStockJson());

            JPanel buttonHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonHolder.setOpaque(false);
            buttonHolder.add(showJsonBtn);

            JPanel topSection = new JPanel(new BorderLayout());
            topSection.setOpaque(false);
            topSection.add(inputPanel, BorderLayout.NORTH);
            topSection.add(buttonHolder, BorderLayout.CENTER);

            jsonDisplayArea = new JTextArea(15, 50);
            jsonDisplayArea.setEditable(false);
            jsonDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
            JScrollPane scrollPane = new JScrollPane(jsonDisplayArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 50));

            add(topSection, BorderLayout.CENTER);
            add(scrollPane, BorderLayout.SOUTH);
            add(controlPanel, BorderLayout.NORTH); // Adds the common control panel with the back button
        }

        private void showStockJson() {
            try {
                if (productIdField.getText().trim().isEmpty()) {
                    showError("Please enter a Product ID to view JSON.");
                    jsonDisplayArea.setText("");
                    return;
                }
                long productId = Long.parseLong(productIdField.getText());
                String json = StockService.getStockAsJson(productId);
                if (json != null && !json.contains("Stock not found")) {
                    jsonDisplayArea.setText(json);
                } else {
                    jsonDisplayArea.setText("Stock not found for Product ID: " + productId);
                    showError("Stock not found for Product ID: " + productId);
                }
            } catch (NumberFormatException ex) {
                showError("Invalid Product ID.");
                jsonDisplayArea.setText("");
            } catch (Exception ex) {
                showError(ex.getMessage());
                jsonDisplayArea.setText("");
            }
        }
    }

    // Searchs Other Stores Panel
    private class SearchOtherStoresPanel extends StockOperationPanel {
        private JTextField productIdField, excludedStoreIdField;
        private JTable table;
        private DefaultTableModel model;

        public SearchOtherStoresPanel() {
            // The main content area of this panel
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setOpaque(false); // Keep it transparent

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 200, 10, 200));

        productIdField = new JTextField(10);
        excludedStoreIdField = new JTextField(10);

        Font labelFont = new Font("Arial", Font.PLAIN, 16);
        Font fieldFont = new Font("Arial", Font.PLAIN, 16);

        JLabel productIdLabel = new JLabel("Product ID:");
        productIdLabel.setFont(labelFont);
        JLabel excludedStoreIdLabel = new JLabel("Excluded Store ID:");
        excludedStoreIdLabel.setFont(labelFont);

        productIdField.setFont(fieldFont);
        excludedStoreIdField.setFont(fieldFont);

        formPanel.add(productIdLabel);
        formPanel.add(productIdField);
        formPanel.add(excludedStoreIdLabel);
        formPanel.add(excludedStoreIdField);

        JButton searchBtn = new JButton("Search Other Stores");
        searchBtn.setFont(new Font("Arial", Font.BOLD, 18));
        searchBtn.setPreferredSize(new Dimension(220, 50));
        searchBtn.addActionListener(e -> searchOtherStores());

        JPanel buttonHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonHolder.setOpaque(false);
        buttonHolder.add(searchBtn);
        formPanel.add(new JLabel()); // Empty cell for layout
        formPanel.add(buttonHolder);

        // Table to display results
        model = new DefaultTableModel(new String[]{"Store ID", "Quantity"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Makes table non-editable
            }
        };
        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 50));

        // Adds formPanel and scrollPane to the main content panel
        mainContentPanel.add(formPanel, BorderLayout.NORTH);
        mainContentPanel.add(scrollPane, BorderLayout.CENTER);

        // Adds the mainContentPanel to the SearchOtherStoresPanel to the CENTER, as the superclass already added controlPanel to NORTH.
        add(mainContentPanel, BorderLayout.CENTER);
        }

        private void searchOtherStores() {
            model.setRowCount(0); // Clears previous results
            try {
                long productId = Long.parseLong(productIdField.getText());
                int excludedStoreId = Integer.parseInt(excludedStoreIdField.getText());

                List<Stock> stocks = StockService.searchProductInOtherStores(productId, excludedStoreId);

                if (stocks.isEmpty()) {
                    showSuccess("No stock found in other stores for Product ID " + productId + ".");
                } else {
                    for (Stock s : stocks) {
                        model.addRow(new Object[]{s.getStoreId(), s.getStockQuantity()});
                    }
                }
            } catch (NumberFormatException ex) {
                showError("Invalid number format for ID.");
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        }
    }
}