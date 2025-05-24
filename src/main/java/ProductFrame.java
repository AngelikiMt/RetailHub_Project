import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class ProductFrame extends JFrame {
    private JTextField descField, priceField, costField, idField;
    private JComboBox<String> categoryBox;
    private JTable table;
    private DefaultTableModel model;
    private ProductService productService;
    private long lastId = 0;

    public ProductFrame() {
        setTitle("Products");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        productService = new ProductService();

        JPanel inputPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        descField = new JTextField();
        categoryBox = new JComboBox<>(new String[]{"clothing", "beauty", "electronics"});
        priceField = new JTextField();
        costField = new JTextField();
        idField = new JTextField();

        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryBox);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Cost:"));
        inputPanel.add(costField);
        inputPanel.add(new JLabel("ID (for update/delete/json):"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel(""));
        inputPanel.add(new JLabel(""));

        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        JButton createBtn = new JButton("Create");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton toggleBtn = new JButton("Active/Deactivate");
        JButton jsonBtn = new JButton("Show JSON");
        JButton backBtn = new JButton("Back");

        buttonPanel.add(createBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(toggleBtn);
        buttonPanel.add(jsonBtn);
        buttonPanel.add(backBtn);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Description", "Category", "Price", "Cost", "Active"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        createBtn.addActionListener(e -> {
            try {
                var p = productService.createProduct(
                        descField.getText(),
                        categoryBox.getSelectedItem().toString().toLowerCase(),
                        Double.parseDouble(priceField.getText()),
                        Double.parseDouble(costField.getText())
                );
                if (p != null) {
                    lastId = Math.max(lastId, p.getProductId());
                    refreshAll();
                }
            } catch (Exception ex) {
                showError(ex);
            }
        });

        updateBtn.addActionListener(e -> {
            try {
                if (idField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter a Product ID to update.");
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
                refreshAll();
            } catch (Exception ex) {
                showError(ex);
            }
        });

        deleteBtn.addActionListener(e -> {
            try {
                long id = Long.parseLong(idField.getText());
                productService.deleteProduct(id);
                refreshAll();
            } catch (Exception ex) {
                showError(ex);
            }
        });

        toggleBtn.addActionListener(e -> {
            try {
                long id = Long.parseLong(idField.getText());
                var p = productService.findProductById(id);
                if (p != null) {
                    boolean newState = !p.getActive();
                    productService.deactivateProduct(id, newState);
                    refreshAll();
                    toggleBtn.setText(newState ? "Deactivated" : "Activated");
                    Timer timer = new Timer(1500, evt -> toggleBtn.setText("Active/Deactivate"));
                    timer.setRepeats(false);
                    timer.start();
                }
            } catch (Exception ex) {
                showError(ex);
            }
        });

        jsonBtn.addActionListener(e -> {
            try {
                if (idField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter a Product ID to view JSON.");
                    return;
                }
                long id = Long.parseLong(idField.getText());
                var p = productService.findProductById(id);
                if (p != null)
                    JOptionPane.showMessageDialog(this, ProductService.getProductAsJson(p));
                else
                    JOptionPane.showMessageDialog(this, "Product not found");
            } catch (Exception ex) {
                showError(ex);
            }
        });

        backBtn.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void refreshAll() {
        model.setRowCount(0);
        for (long i = 1; i <= lastId; i++) {
            var p = productService.findProductById(i);
            if (p != null) {
                model.addRow(new Object[]{
                        p.getProductId(), p.getDescription(), p.getCategory(),
                        p.getPrice(), p.getCost(), p.getActive()
                });
            }
        }
    }

    private void showError(Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
}