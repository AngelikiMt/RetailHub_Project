import javax.swing.*;
import java.awt.*;

public class StockFrame extends JFrame {
    public StockFrame() {
        setTitle("Stock Management");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 1, 5, 5));

        JButton addBtn = new JButton("Add Stock");
        JButton getBtn = new JButton("Get Stock for Product & Store");
        JButton updateBtn = new JButton("Update Stock");
        JButton lowStockBtn = new JButton("Get Low Stock (<3)");
        JButton jsonBtn = new JButton("Get Stock as JSON");
        JButton closeBtn = new JButton("Close");

        addBtn.addActionListener(e -> {
            JTextField productId = new JTextField();
            JTextField storeId = new JTextField();
            JTextField quantity = new JTextField();

            Object[] message = {
                "Product ID:", productId,
                "Store ID:", storeId,
                "Quantity:", quantity
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Add Stock", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    StockService.addStock(
                        Integer.parseInt(storeId.getText()),
                        Long.parseLong(productId.getText()),
                        Integer.parseInt(quantity.getText())
                    );
                    JOptionPane.showMessageDialog(this, "Stock Added");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        getBtn.addActionListener(e -> {
            String p = JOptionPane.showInputDialog("Product ID:");
            String s = JOptionPane.showInputDialog("Store ID:");
            var result = StockService.getStock(Long.parseLong(p), Integer.parseInt(s));
            if (result.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No stock found.");
            } else {
                JOptionPane.showMessageDialog(this, "Stock: " + result.get(0).getStockQuantity());
            }
        });

        updateBtn.addActionListener(e -> {
            JTextField productId = new JTextField();
            JTextField storeId = new JTextField();
            JTextField quantity = new JTextField();

            Object[] message = {
                "Product ID:", productId,
                "Store ID:", storeId,
                "New Quantity:", quantity
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Update Stock", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                StockService.updateStock(
                    Long.parseLong(productId.getText()),
                    Integer.parseInt(storeId.getText()),
                    Integer.parseInt(quantity.getText())
                );
                JOptionPane.showMessageDialog(this, "Stock Updated");
            }
        });

        lowStockBtn.addActionListener(e -> {
            StockService.getLowStockProducts(); // Assuming it shows via console or modifies something else
            JOptionPane.showMessageDialog(this, "Check console for low stock items.");
        });

        jsonBtn.addActionListener(e -> {
            String p = JOptionPane.showInputDialog("Enter product ID:");
            String json = StockService.getStockAsJson(Long.parseLong(p));
            JOptionPane.showMessageDialog(this, json);
        });

        closeBtn.addActionListener(e -> dispose());

        add(addBtn);
        add(getBtn);
        add(updateBtn);
        add(lowStockBtn);
        add(jsonBtn);
        add(closeBtn);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

