import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class ProductFrame extends JFrame {

    private JTextField descField, categoryField, priceField, costField;
    private JTable table;
    private DefaultTableModel model;

    public ProductFrame() {
     setTitle("Products");
     setExtendedState(JFrame.MAXIMIZED_BOTH); // Πλήρης οθόνη
     setDefaultCloseOperation(EXIT_ON_CLOSE);
     setLayout(new BorderLayout());


        // === Πάνω μέρος - Φόρμα ===
        JPanel form = new JPanel(new GridLayout(5, 2));
        descField = new JTextField();
        categoryField = new JTextField();
        priceField = new JTextField();
        costField = new JTextField();

        form.add(new JLabel("Description:")); form.add(descField);
        form.add(new JLabel("Category:")); form.add(categoryField);
        form.add(new JLabel("Price:")); form.add(priceField);
        form.add(new JLabel("Cost:")); form.add(costField);

        JButton addBtn = new JButton("Add");
        form.add(addBtn);

        JButton updateBtn = new JButton("Update");
        form.add(updateBtn);

        add(form, BorderLayout.NORTH);

        // === Πίνακας ===
        model = new DefaultTableModel(new String[]{"Description", "Category", "Price", "Cost", "Status"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // === Κάτω μέρος - Κουμπιά ===
        JPanel buttons = new JPanel();
        JButton deleteBtn = new JButton("Delete");
        JButton toggleBtn = new JButton("Active");
        JButton jsonBtn = new JButton("Show as JSON");

        buttons.add(deleteBtn);
        buttons.add(toggleBtn);
        buttons.add(jsonBtn);
        add(buttons, BorderLayout.SOUTH);

        // === Λειτουργίες ===
        addBtn.addActionListener(e -> {
            try {
                String desc = descField.getText();
                String cat = categoryField.getText();
                double price = Double.parseDouble(priceField.getText());
                double cost = Double.parseDouble(costField.getText());

                if (!desc.isEmpty() && !cat.isEmpty()) {
                    model.addRow(new Object[]{desc, cat, price, cost, "Active"});
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Συμπλήρωσε όλα τα πεδία.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Τιμή και Κόστος πρέπει να είναι αριθμοί.");
            }
        });

        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                try {
                    model.setValueAt(descField.getText(), row, 0);
                    model.setValueAt(categoryField.getText(), row, 1);
                    model.setValueAt(Double.parseDouble(priceField.getText()), row, 2);
                    model.setValueAt(Double.parseDouble(costField.getText()), row, 3);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Λάθος στα δεδομένα.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Επίλεξε γραμμή για ενημέρωση.");
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) model.removeRow(row);
        });

        toggleBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String status = (String) model.getValueAt(row, 4);
                model.setValueAt(status.equals("Active") ? "Inactive" : "Active", row, 4);
            }
        });

        jsonBtn.addActionListener(e -> {
            StringBuilder json = new StringBuilder("[\n");
            for (int i = 0; i < model.getRowCount(); i++) {
                json.append("  {\n");
                json.append("    \"description\": \"").append(model.getValueAt(i, 0)).append("\",\n");
                json.append("    \"category\": \"").append(model.getValueAt(i, 1)).append("\",\n");
                json.append("    \"price\": ").append(model.getValueAt(i, 2)).append(",\n");
                json.append("    \"cost\": ").append(model.getValueAt(i, 3)).append(",\n");
                json.append("    \"status\": \"").append(model.getValueAt(i, 4)).append("\"\n");
                json.append("  }");
                if (i < model.getRowCount() - 1) json.append(",");
                json.append("\n");
            }
            json.append("]");

            JTextArea area = new JTextArea(json.toString());
            area.setEditable(false);
            JScrollPane pane = new JScrollPane(area);
            pane.setPreferredSize(new Dimension(600, 300));
            JOptionPane.showMessageDialog(this, pane, "JSON Προβολή", JOptionPane.INFORMATION_MESSAGE);
        });

        // Όταν κάνεις κλικ σε γραμμή, να γεμίζει η φόρμα για edit
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    descField.setText(model.getValueAt(row, 0).toString());
                    categoryField.setText(model.getValueAt(row, 1).toString());
                    priceField.setText(model.getValueAt(row, 2).toString());
                    costField.setText(model.getValueAt(row, 3).toString());
                }
            }
        });

        setVisible(true);
    }

    private void clearFields() {
        descField.setText("");
        categoryField.setText("");
        priceField.setText("");
        costField.setText("");
    }

    public static void main(String[] args) {
        new ProductFrame();
    }
}
