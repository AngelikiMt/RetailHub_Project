import javax.swing.*;
import java.awt.*;

public class StoreFrame extends JFrame {
    private JPanel contentPanel;

    public StoreFrame() {
        setTitle("Store Management");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Πλήρης οθόνη

        // === Φόντο ===
        BackgroundPanel backgroundPanel = new BackgroundPanel("RetailHub.png");
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        // === Κουμπιά στα αριστερά ===
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

        // === Κεντρικό Panel ===
        contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new GridBagLayout());

        backgroundPanel.add(sidePanel, BorderLayout.WEST);
        backgroundPanel.add(contentPanel, BorderLayout.CENTER);

        // === Κουμπιά Δράσεις ===
        createBtn.addActionListener(e -> showCreatePanel());
        updateBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Update logic here."));
        deactivateBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Deactivate logic here."));
        showStoreBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Show store logic here."));
        showAllBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Show all logic here."));
        jsonBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "JSON logic here."));

        setVisible(true);
    }

    private void showCreatePanel() {
        contentPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField name = new JTextField();
        name.setPreferredSize(new Dimension(200, 25));
        JTextField address = new JTextField();
        address.setPreferredSize(new Dimension(200, 25));
        JTextField country = new JTextField();
        country.setPreferredSize(new Dimension(200, 25));
        JTextField phone = new JTextField();
        phone.setPreferredSize(new Dimension(200, 25));
        JButton submit = new JButton("Create");

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

        submit.addActionListener(e -> {
            try {
                Store store = StoreService.createStore(
                    name.getText(), address.getText(), country.getText(), phone.getText()
                );
                JOptionPane.showMessageDialog(this, "Store Created:\n" + store);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // === Εσωτερική Κλάση για Φόντο ===
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
