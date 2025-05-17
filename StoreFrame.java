import javax.swing.*;
import java.awt.*;

public class StoreFrame extends JFrame {
    public StoreFrame() {
        setTitle("Store Management");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(7, 1, 5, 5));

        JButton createBtn = new JButton("Create Store");
        JButton updateBtn = new JButton("Update Store");
        JButton deactivateBtn = new JButton("Deactivate Store");
        JButton showStoreBtn = new JButton("Show Specific Store");
        JButton showAllBtn = new JButton("Show All Stores");
        JButton jsonBtn = new JButton("Get Store as JSON");
        JButton backBtn = new JButton("Close");

        createBtn.addActionListener(e -> {
            JTextField name = new JTextField();
            JTextField address = new JTextField();
            JTextField country = new JTextField();
            JTextField phone = new JTextField();

            Object[] message = {
                "Name:", name,
                "Address:", address,
                "Country:", country,
                "Phone:", phone
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Create Store", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    Store store = StoreService.createStore(name.getText(), address.getText(), country.getText(), phone.getText());
                    JOptionPane.showMessageDialog(this, "Store Created:\n" + store);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        updateBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Enter store ID to update:");
            if (input != null) {
                int id = Integer.parseInt(input);
                if (StoreService.validateId(id)) {
                    JTextField name = new JTextField();
                    JTextField address = new JTextField();
                    JTextField country = new JTextField();
                    JTextField phone = new JTextField();

                    Object[] message = {
                        "New Name:", name,
                        "New Address:", address,
                        "New Country:", country,
                        "New Phone:", phone
                    };

                    int option = JOptionPane.showConfirmDialog(this, message, "Update Store", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        StoreService.updateStore(id, name.getText(), address.getText(), country.getText(), phone.getText());
                        JOptionPane.showMessageDialog(this, "Updated:\n" + StoreService.getStoreById(id));
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid ID");
                }
            }
        });

        deactivateBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Enter store ID to deactivate:");
            if (input != null) {
                int id = Integer.parseInt(input);
                if (StoreService.validateId(id)) {
                    StoreService.getStoreById(id).setActive(false);
                    JOptionPane.showMessageDialog(this, "Store " + id + " deactivated.");
                }
            }
        });

        showStoreBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Enter store ID:");
            if (input != null) {
                int id = Integer.parseInt(input);
                if (StoreService.validateId(id)) {
                    JOptionPane.showMessageDialog(this, StoreService.getStoreById(id));
                }
            }
        });

        showAllBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, StoreService.getStores().toString());
        });

        jsonBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Enter store ID for JSON:");
            if (input != null) {
                int id = Integer.parseInt(input);
                JOptionPane.showMessageDialog(this, StoreService.getStoreAsJson(StoreService.getStoreById(id)));
            }
        });

        backBtn.addActionListener(e -> dispose());

        add(createBtn);
        add(updateBtn);
        add(deactivateBtn);
        add(showStoreBtn);
        add(showAllBtn);
        add(jsonBtn);
        add(backBtn);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
