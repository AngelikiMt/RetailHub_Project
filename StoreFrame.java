
import javax.swing.*;
import java.awt.*;

public class StoreFrame extends JFrame {
    private JPanel contentPanel;

    public StoreFrame() {
        setTitle("Store Management");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        BackgroundPanel backgroundPanel = new BackgroundPanel("RETAIL1.png");
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        JPanel sidePanel = new JPanel(new GridLayout(6, 1, 10, 10));
        sidePanel.setOpaque(false);
        sidePanel.setPreferredSize(new Dimension(250, 0));

        JButton createBtn = new JButton("Create Store");
        JButton updateBtn = new JButton("Update Store");
        JButton deactivateBtn = new JButton("Deactivate Store");
        JButton showStoreBtn = new JButton("Show Store");
        JButton showAllBtn = new JButton("Show All Stores");
        JButton jsonBtn = new JButton("Get Store as JSON");

        for (JButton btn : new JButton[]{createBtn, updateBtn, deactivateBtn, showStoreBtn, showAllBtn, jsonBtn}) {
            btn.setFont(new Font("Arial", Font.BOLD, 16));
            sidePanel.add(btn);
        }

        contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new GridBagLayout());

        backgroundPanel.add(sidePanel, BorderLayout.WEST);
        backgroundPanel.add(contentPanel, BorderLayout.CENTER);

        createBtn.addActionListener(e -> showCreatePanel());
        updateBtn.addActionListener(e -> showUpdatePanel());
        deactivateBtn.addActionListener(e -> showDeactivatePanel());
        showStoreBtn.addActionListener(e -> showSpecificStorePanel());
        showAllBtn.addActionListener(e -> showAllStoresPanel());
        jsonBtn.addActionListener(e -> showStoreAsJsonPanel());

        setVisible(true);
    }

    private void showCreatePanel() {
        contentPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField name = new JTextField();
        JTextField address = new JTextField();
        JTextField country = new JTextField();
        JTextField phone = new JTextField();
        JButton submit = new JButton("Create");
        JTextArea output = new JTextArea(5, 30);
        output.setEditable(false);

        int y = 0;
        gbc.gridx = 0; gbc.gridy = y; contentPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; contentPanel.add(name, gbc);
        gbc.gridx = 0; gbc.gridy = ++y; contentPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1; contentPanel.add(address, gbc);
        gbc.gridx = 0; gbc.gridy = ++y; contentPanel.add(new JLabel("Country:"), gbc);
        gbc.gridx = 1; contentPanel.add(country, gbc);
        gbc.gridx = 0; gbc.gridy = ++y; contentPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1; contentPanel.add(phone, gbc);
        gbc.gridx = 1; gbc.gridy = ++y; contentPanel.add(submit, gbc);
        gbc.gridx = 0; gbc.gridy = ++y; gbc.gridwidth = 2; contentPanel.add(new JScrollPane(output), gbc);

        submit.addActionListener(e -> {
            try {
                Store store = StoreService.createStore(
                        name.getText(), address.getText(), country.getText(), phone.getText());
                output.setText("Store Created:\n" + store);
            } catch (IllegalArgumentException ex) {
                output.setText(ex.getMessage());
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showUpdatePanel() {
        contentPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField countryField = new JTextField();
        JTextField phoneField = new JTextField();
        JButton submit = new JButton("Update");
        JTextArea output = new JTextArea(5, 30);
        output.setEditable(false);

        int y = 0;
        gbc.gridx = 0; gbc.gridy = y; contentPanel.add(new JLabel("Store ID:"), gbc);
        gbc.gridx = 1; contentPanel.add(idField, gbc);
        gbc.gridx = 0; gbc.gridy = ++y; contentPanel.add(new JLabel("New Name:"), gbc);
        gbc.gridx = 1; contentPanel.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = ++y; contentPanel.add(new JLabel("New Address:"), gbc);
        gbc.gridx = 1; contentPanel.add(addressField, gbc);
        gbc.gridx = 0; gbc.gridy = ++y; contentPanel.add(new JLabel("New Country:"), gbc);
        gbc.gridx = 1; contentPanel.add(countryField, gbc);
        gbc.gridx = 0; gbc.gridy = ++y; contentPanel.add(new JLabel("New Phone:"), gbc);
        gbc.gridx = 1; contentPanel.add(phoneField, gbc);
        gbc.gridx = 1; gbc.gridy = ++y; contentPanel.add(submit, gbc);
        gbc.gridx = 0; gbc.gridy = ++y; gbc.gridwidth = 2; contentPanel.add(new JScrollPane(output), gbc);

        submit.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                if (!StoreService.validateId(id)) {
                    output.setText("Invalid Store ID");
                    return;
                }
                StoreService.updateStore(id,
                        nameField.getText(), addressField.getText(), countryField.getText(), phoneField.getText());
                output.setText("Store updated:\n" + StoreService.getStoreById(id));
            } catch (Exception ex) {
                output.setText("Error: " + ex.getMessage());
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showDeactivatePanel() {
        contentPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField idField = new JTextField();
        JButton submit = new JButton("Deactivate");
        JTextArea output = new JTextArea(3, 30);
        output.setEditable(false);

        gbc.gridx = 0; gbc.gridy = 0; contentPanel.add(new JLabel("Store ID to deactivate:"), gbc);
        gbc.gridx = 1; contentPanel.add(idField, gbc);
        gbc.gridx = 1; gbc.gridy = 1; contentPanel.add(submit, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; contentPanel.add(new JScrollPane(output), gbc);

        submit.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                if (StoreService.validateId(id)) {
                    StoreService.getStoreById(id).setActive(false);
                    output.setText("Store with ID " + id + " deactivated.");
                } else {
                    output.setText("Invalid Store ID.");
                }
            } catch (Exception ex) {
                output.setText("Error: " + ex.getMessage());
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showSpecificStorePanel() {
        contentPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField idField = new JTextField();
        JButton submit = new JButton("Show");
        JTextArea output = new JTextArea(5, 30);
        output.setEditable(false);

        gbc.gridx = 0; gbc.gridy = 0; contentPanel.add(new JLabel("Store ID:"), gbc);
        gbc.gridx = 1; contentPanel.add(idField, gbc);
        gbc.gridx = 1; gbc.gridy = 1; contentPanel.add(submit, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; contentPanel.add(new JScrollPane(output), gbc);

        submit.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                if (StoreService.validateId(id)) {
                    output.setText(StoreService.getStoreById(id).toString());
                } else {
                    output.setText("Invalid Store ID");
                }
            } catch (Exception ex) {
                output.setText("Error: " + ex.getMessage());
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showAllStoresPanel() {
        contentPanel.removeAll();
        JTextArea output = new JTextArea(20, 40);
        output.setEditable(false);
        output.setText(StoreService.getStores().toString());
        contentPanel.add(new JScrollPane(output));
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showStoreAsJsonPanel() {
        contentPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField idField = new JTextField();
        JButton submit = new JButton("Get JSON");
        JTextArea output = new JTextArea(10, 30);
        output.setEditable(false);

        gbc.gridx = 0; gbc.gridy = 0; contentPanel.add(new JLabel("Store ID:"), gbc);
        gbc.gridx = 1; contentPanel.add(idField, gbc);
        gbc.gridx = 1; gbc.gridy = 1; contentPanel.add(submit, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; contentPanel.add(new JScrollPane(output), gbc);

        submit.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                if (StoreService.validateId(id)) {
                    String json = StoreService.getStoreAsJson(StoreService.getStoreById(id));
                    output.setText(json);
                } else {
                    output.setText("Invalid Store ID");
                }
            } catch (Exception ex) {
                output.setText("Error: " + ex.getMessage());
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    static class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            backgroundImage = new ImageIcon(imagePath).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}
