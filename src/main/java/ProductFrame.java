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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

public class ProductFrame extends JFrame {
    private JPanel backgroundPanel;
    private JPanel currentContentPanel; // To hold the active sub-panel
    private ProductService productService;

    public ProductFrame() {
        setTitle("Products Menu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Closes the frame
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen

        productService = new ProductService();

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

        // Initial content will be the main product menu buttons
        showProductMainMenu();

        setContentPane(backgroundPanel);
        setVisible(true);
    }

    private void showProductMainMenu() {
        if (currentContentPanel != null) {
            backgroundPanel.remove(currentContentPanel);
        }

        // === BUTTONS ===
        JButton createBtn = new JButton("Create Product");
        JButton updateBtn = new JButton("Update Product");
        JButton deleteBtn = new JButton("Delete Product");
        JButton toggleBtn = new JButton("Activate/Deactivate");
        JButton jsonBtn = new JButton("Show Product JSON");
        JButton viewAllBtn = new JButton("View All Products");
        JButton backToMainBtn = new JButton("Back to Main Menu");

        Font buttonFont = new Font("Arial", Font.BOLD, 20);
        Dimension buttonSize = new Dimension(250, 60);

        JButton[] buttons = {createBtn, updateBtn, deleteBtn, toggleBtn, jsonBtn, viewAllBtn, backToMainBtn};
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

        backgroundPanel.add(centerPanel, BorderLayout.CENTER); // Add to the center
        currentContentPanel = centerPanel;
        backgroundPanel.revalidate();
        backgroundPanel.repaint();

        // === ACTIONS ===
        createBtn.addActionListener(e -> showPanel(new CreateProductPanel()));
        updateBtn.addActionListener(e -> showPanel(new UpdateProductPanel()));
        deleteBtn.addActionListener(e -> showPanel(new DeleteProductPanel()));
        toggleBtn.addActionListener(e -> showPanel(new ToggleProductStatusPanel()));
        jsonBtn.addActionListener(e -> showPanel(new ShowProductJsonPanel()));
        viewAllBtn.addActionListener(e -> showPanel(new ViewAllProductsPanel()));
        backToMainBtn.addActionListener(e -> {
            dispose(); // Closes ProductFrame
        });
    }

    private void showPanel(JPanel panel) {
        if (currentContentPanel != null) {
            backgroundPanel.remove(currentContentPanel);
        }
        backgroundPanel.add(panel, BorderLayout.CENTER);
        currentContentPanel = panel;
        backgroundPanel.revalidate();
        backgroundPanel.repaint();
    }

    // --- Sub-Panels for Product Operations ---

    // Base Panel for common elements like 'Back' button
    private abstract class ProductOperationPanel extends JPanel {
        protected JTextField idField; // Common for update, delete, toggle, json
        protected ProductService productService;
        protected JButton backButton;
        protected JPanel controlPanel; // Holds the back button

        public ProductOperationPanel() {
            this.productService = new ProductService();
            setLayout(new BorderLayout());
            setOpaque(false); // Transparent background

            controlPanel = new JPanel(new BorderLayout());
            controlPanel.setOpaque(false);

            backButton = new JButton("Back to Product Menu");
            backButton.setFont(new Font("Arial", Font.BOLD, 18));
            backButton.addActionListener(e -> showProductMainMenu());

            JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            backButtonPanel.setOpaque(false);
            backButtonPanel.add(backButton);
            controlPanel.add(backButtonPanel, BorderLayout.NORTH);

            add(controlPanel, BorderLayout.NORTH); // Adds the instance's controlPanel
        }

        protected void showError(String message) {
            JOptionPane.showMessageDialog(this, "Error: " + message, "Error", JOptionPane.ERROR_MESSAGE);
        }

        protected void showSuccess(String message) {
            JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Create Product Panel
    private class CreateProductPanel extends ProductOperationPanel {
        private JTextField descField, priceField, costField;
        private JComboBox<String> categoryBox;

        public CreateProductPanel() {
            super();
            JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
            formPanel.setOpaque(false);
            formPanel.setBorder(BorderFactory.createEmptyBorder(50, 200, 50, 200)); // Padding

            descField = new JTextField(20);
            categoryBox = new JComboBox<>(new String[]{"clothing", "beauty", "electronics", "home goods", "kitchen appliances"});
            priceField = new JTextField(10);
            costField = new JTextField(10);

            Font labelFont = new Font("Arial", Font.PLAIN, 16);
            Font fieldFont = new Font("Arial", Font.PLAIN, 16);

            JLabel descLabel = new JLabel("Description:");
            descLabel.setFont(labelFont);
            JLabel categoryLabel = new JLabel("Category:");
            categoryLabel.setFont(labelFont);
            JLabel priceLabel = new JLabel("Price:");
            priceLabel.setFont(labelFont);
            JLabel costLabel = new JLabel("Cost:");
            costLabel.setFont(labelFont);

            descField.setFont(fieldFont);
            categoryBox.setFont(fieldFont);
            priceField.setFont(fieldFont);
            costField.setFont(fieldFont);

            formPanel.add(descLabel);
            formPanel.add(descField);
            formPanel.add(categoryLabel);
            formPanel.add(categoryBox);
            formPanel.add(priceLabel);
            formPanel.add(priceField);
            formPanel.add(costLabel);
            formPanel.add(costField);

            JButton createBtn = new JButton("Create Product");
            createBtn.setFont(new Font("Arial", Font.BOLD, 18));
            createBtn.setPreferredSize(new Dimension(180, 50));
            createBtn.addActionListener(e -> createProduct());

            JPanel buttonHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonHolder.setOpaque(false);
            buttonHolder.add(createBtn);
            formPanel.add(new JLabel()); // Empty cell for alignment
            formPanel.add(buttonHolder);


            add(formPanel, BorderLayout.CENTER);
        }

        private void createProduct() {
            try {
                var p = productService.createProduct(
                        descField.getText(),
                        categoryBox.getSelectedItem().toString().toLowerCase(),
                        Double.parseDouble(priceField.getText()),
                        Double.parseDouble(costField.getText())
                );
                if (p != null) {
                    showSuccess("Product created with ID: " + p.getProductId());
                    descField.setText("");
                    priceField.setText("");
                    costField.setText("");
                    categoryBox.setSelectedIndex(0);
                }
            } catch (NumberFormatException ex) {
                showError("Invalid number format for Price or Cost.");
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        }
    }

    // Update Product Panel
    private class UpdateProductPanel extends ProductOperationPanel {
        private JTextField descField, priceField, costField;
        private JComboBox<String> categoryBox;

        public UpdateProductPanel() {
            super();
            JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
            formPanel.setOpaque(false);
            formPanel.setBorder(BorderFactory.createEmptyBorder(50, 200, 50, 200));

            idField = new JTextField(10); // ID field is common
            descField = new JTextField(20);
            categoryBox = new JComboBox<>(new String[]{"clothing", "beauty", "electronics", "home goods", "kitchen appliances"});
            priceField = new JTextField(10);
            costField = new JTextField(10);

            Font labelFont = new Font("Arial", Font.PLAIN, 16);
            Font fieldFont = new Font("Arial", Font.PLAIN, 16);

            JLabel idLabel = new JLabel("Product ID to Update:");
            idLabel.setFont(labelFont);
            JLabel descLabel = new JLabel("New Description (optional):");
            descLabel.setFont(labelFont);
            JLabel categoryLabel = new JLabel("New Category (optional):");
            categoryLabel.setFont(labelFont);
            JLabel priceLabel = new JLabel("New Price (optional):");
            priceLabel.setFont(labelFont);
            JLabel costLabel = new JLabel("New Cost (optional):");
            costLabel.setFont(labelFont);

            idField.setFont(fieldFont);
            descField.setFont(fieldFont);
            categoryBox.setFont(fieldFont);
            priceField.setFont(fieldFont);
            costField.setFont(fieldFont);

            formPanel.add(idLabel);
            formPanel.add(idField);
            formPanel.add(descLabel);
            formPanel.add(descField);
            formPanel.add(categoryLabel);
            formPanel.add(categoryBox);
            formPanel.add(priceLabel);
            formPanel.add(priceField);
            formPanel.add(costLabel);
            formPanel.add(costField);

            JButton updateBtn = new JButton("Update Product");
            updateBtn.setFont(new Font("Arial", Font.BOLD, 18));
            updateBtn.setPreferredSize(new Dimension(180, 50));
            updateBtn.addActionListener(e -> updateProduct());

            JPanel buttonHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonHolder.setOpaque(false);
            buttonHolder.add(updateBtn);
            formPanel.add(new JLabel());
            formPanel.add(buttonHolder);

            add(formPanel, BorderLayout.CENTER);
        }

        private void updateProduct() {
            try {
                if (idField.getText().trim().isEmpty()) {
                    showError("Please enter a Product ID to update.");
                    return;
                }
                long id = Long.parseLong(idField.getText());

                String desc = descField.getText();
                String category = categoryBox.getSelectedItem().toString().toLowerCase();
                String priceText = priceField.getText();
                String costText = costField.getText();

                double price = priceText.isEmpty() ? -1 : Double.parseDouble(priceText);
                double cost = costText.isEmpty() ? -1 : Double.parseDouble(costText);

                productService.updateProduct(id,
                        desc.isEmpty() ? null : desc,
                        category.isEmpty() ? null : category,
                        price,
                        cost
                );
                showSuccess("Product ID " + id + " updated successfully.");
                idField.setText("");
                descField.setText("");
                priceField.setText("");
                costField.setText("");
                categoryBox.setSelectedIndex(0);
            } catch (NumberFormatException ex) {
                showError("Invalid number format for Product ID, Price or Cost.");
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        }
    }

    // Delete Product Panel
    private class DeleteProductPanel extends ProductOperationPanel {
        public DeleteProductPanel() {
            super();
            JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
            formPanel.setOpaque(false);
            formPanel.setBorder(BorderFactory.createEmptyBorder(100, 200, 100, 200));

            idField = new JTextField(10);
            idField.setFont(new Font("Arial", Font.PLAIN, 16));

            JLabel idLabel = new JLabel("Product ID to Delete:");
            idLabel.setFont(new Font("Arial", Font.PLAIN, 16));

            formPanel.add(idLabel);
            formPanel.add(idField);

            JButton deleteBtn = new JButton("Delete Product");
            deleteBtn.setFont(new Font("Arial", Font.BOLD, 18));
            deleteBtn.setPreferredSize(new Dimension(180, 50));
            deleteBtn.addActionListener(e -> deleteProduct());

            JPanel buttonHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonHolder.setOpaque(false);
            buttonHolder.add(deleteBtn);
            formPanel.add(new JLabel());
            formPanel.add(buttonHolder);

            add(formPanel, BorderLayout.CENTER);
        }

        private void deleteProduct() {
            try {
                if (idField.getText().trim().isEmpty()) {
                    showError("Please enter a Product ID to delete.");
                    return;
                }
                long id = Long.parseLong(idField.getText());
                productService.deleteProduct(id);
                showSuccess("Product ID " + id + " deleted successfully.");
                idField.setText("");
            } catch (NumberFormatException ex) {
                showError("Invalid Product ID.");
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        }
    }

    // Active/Inactive Product Status Panel
    private class ToggleProductStatusPanel extends ProductOperationPanel {
        private JButton toggleButton;

        public ToggleProductStatusPanel() {
            super();
            JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
            formPanel.setOpaque(false);
            formPanel.setBorder(BorderFactory.createEmptyBorder(100, 200, 100, 200));

            idField = new JTextField(10);
            idField.setFont(new Font("Arial", Font.PLAIN, 16));

            JLabel idLabel = new JLabel("Product ID to Active/Inactive Status:");
            idLabel.setFont(new Font("Arial", Font.PLAIN, 16));

            formPanel.add(idLabel);
            formPanel.add(idField);

            toggleButton = new JButton("Active/Inactive Status");
            toggleButton.setFont(new Font("Arial", Font.BOLD, 18));
            toggleButton.setPreferredSize(new Dimension(180, 50));
            toggleButton.addActionListener(e -> toggleProductStatus());

            JPanel buttonHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonHolder.setOpaque(false);
            buttonHolder.add(toggleButton);
            formPanel.add(new JLabel());
            formPanel.add(buttonHolder);

            add(formPanel, BorderLayout.CENTER);
        }

        private void toggleProductStatus() {
            try {
                if (idField.getText().trim().isEmpty()) {
                    showError("Please enter a Product ID to toggle status.");
                    return;
                }
                long id = Long.parseLong(idField.getText());
                var p = productService.findProductById(id);
                if (p != null) {
                    boolean newState = !p.getActive();
                    productService.setProductActiveStatus(id, newState);
                    showSuccess("Product ID " + id + " status changed to: " + (newState ? "Active" : "Inactive"));

                    // Temporary text change for feedback
                    toggleButton.setText(newState ? "Deactivated" : "Activated");
                    Timer timer = new Timer(1500, evt -> toggleButton.setText("Toggle Status"));
                    timer.setRepeats(false);
                    timer.start();
                    idField.setText("");
                } else {
                    showError("Product not found with ID: " + id);
                }
            } catch (NumberFormatException ex) {
                showError("Invalid Product ID.");
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        }
    }

    // Show Product JSON Panel
    private class ShowProductJsonPanel extends ProductOperationPanel {
        private JTextArea jsonDisplayArea;

        public ShowProductJsonPanel() {
            super();
            JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
            inputPanel.setOpaque(false);
            inputPanel.setBorder(BorderFactory.createEmptyBorder(50, 200, 10, 200));

            idField = new JTextField(10);
            idField.setFont(new Font("Arial", Font.PLAIN, 16));

            JLabel idLabel = new JLabel("Product ID to View as JSON:");
            idLabel.setFont(new Font("Arial", Font.PLAIN, 16));

            inputPanel.add(idLabel);
            inputPanel.add(idField);

            JButton showJsonBtn = new JButton("Show JSON");
            showJsonBtn.setFont(new Font("Arial", Font.BOLD, 18));
            showJsonBtn.setPreferredSize(new Dimension(150, 50));
            showJsonBtn.addActionListener(e -> showProductJson());

            JPanel buttonHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonHolder.setOpaque(false);
            buttonHolder.add(showJsonBtn);

            JPanel topSection = new JPanel(new BorderLayout());
            topSection.setOpaque(false);
            topSection.add(inputPanel, BorderLayout.NORTH);
            topSection.add(buttonHolder, BorderLayout.CENTER);

            jsonDisplayArea = new JTextArea(15, 40);
            jsonDisplayArea.setEditable(false);
            jsonDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
            JScrollPane scrollPane = new JScrollPane(jsonDisplayArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 50)); // Padding

            add(topSection, BorderLayout.CENTER);
            add(scrollPane, BorderLayout.SOUTH);
            add(controlPanel, BorderLayout.NORTH); // Adds the common control panel with the back button

        }

        private void showProductJson() {
            try {
                if (idField.getText().trim().isEmpty()) {
                    showError("Please enter a Product ID to view JSON.");
                    jsonDisplayArea.setText("");
                    return;
                }
                long id = Long.parseLong(idField.getText());
                String json = ProductService.getProductAsJson(id);
                if (json != null && !json.contains("Product not found")) {
                    jsonDisplayArea.setText(json);
                } else {
                    jsonDisplayArea.setText("Product not found with ID: " + id);
                    showError("Product not found with ID: " + id);
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

    // View All Products Panel
    private class ViewAllProductsPanel extends ProductOperationPanel {
        private JTable table;
        private DefaultTableModel model;
        private long lastId = 0; // Keeps track of highest ID for refresh

        public ViewAllProductsPanel() {
            super();
            setLayout(new BorderLayout());
            JPanel tablePanel = new JPanel(new BorderLayout());
            tablePanel.setOpaque(false);
            tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));

            model = new DefaultTableModel(new String[]{"ID", "Description", "Category", "Price", "Cost", "Active"}, 0) {
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

            refreshAll(); // Populates table on creation
        }

        private void refreshAll() {
            model.setRowCount(0);
            List<Product> allProducts = productService.getAllProducts();
            for (Product p : allProducts) {
                model.addRow(new Object[]{
                        p.getProductId(), p.getDescription(), p.getCategory(),
                        String.format("%.2f", p.getPrice()), String.format("%.2f", p.getCost()), p.getActive()
                });
                if (p.getProductId() > lastId) {
                    lastId = p.getProductId(); // Update lastId for subsequent refreshes
                }
            }
        }
    }
}