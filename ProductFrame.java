import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class ProductFrame extends JFrame {
    public ProductFrame() {
        setTitle("Product Management");
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        JTextField descField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField costField = new JTextField();

        formPanel.add(new JLabel("Description:"));
        formPanel.add(descField);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryField);
        formPanel.add(new JLabel("Price:"));
        formPanel.add(priceField);
        formPanel.add(new JLabel("Cost:"));
        formPanel.add(costField);

        JButton createBtn = new JButton("Create Product");
        createBtn.addActionListener(e -> {
            String desc = descField.getText();
            String category = categoryField.getText();
            double price = Double.parseDouble(priceField.getText());
            double cost = Double.parseDouble(costField.getText());
            productService.createProduct(desc, category, price, cost);
            JOptionPane.showMessageDialog(this, "Product created successfully.");
        });

        add(formPanel, BorderLayout.CENTER);
        add(createBtn, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

