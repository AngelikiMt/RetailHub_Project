import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private static final String USERNAME = "RETAILHUB";
    private static final String PASSWORD = "RetailHub2025";

    public LoginFrame() {
        setTitle("Login - Retail Hub");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Πλήρης οθόνη

        // === Κύριο Panel ===
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // === Εικόνα ===
        try {
            ImageIcon imageIcon = new ImageIcon("RetailHub.png");
            JLabel imageLabel = new JLabel(imageIcon);
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            mainPanel.add(Box.createVerticalStrut(30));
            mainPanel.add(imageLabel);
        } catch (Exception e) {
            System.err.println("Image not found: " + e.getMessage());
        }

        // === Panel σύνδεσης ===
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();

        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();

        JCheckBox showPass = new JCheckBox("Show Password");
        showPass.addActionListener(e -> {
            passField.setEchoChar(showPass.isSelected() ? (char) 0 : '•');
        });

        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Arial", Font.BOLD, 16));

        loginBtn.addActionListener(e -> {
            String enteredUser = userField.getText();
            String enteredPass = new String(passField.getPassword());

            if (USERNAME.equals(enteredUser) && PASSWORD.equals(enteredPass)) {
                SwingUtilities.invokeLater(() -> {
                    dispose();       // Κλείνει το LoginFrame
                    new MainMenu();  // Ανοίγει το MainMenu
                });
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // === Προσθήκη στο loginPanel ===
        gbc.gridx = 0; gbc.gridy = 0;
        loginPanel.add(userLabel, gbc);
        gbc.gridx = 1;
        loginPanel.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        loginPanel.add(passLabel, gbc);
        gbc.gridx = 1;
        loginPanel.add(passField, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        loginPanel.add(showPass, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        loginPanel.add(loginBtn, gbc);

        mainPanel.add(Box.createVerticalStrut(50));
        mainPanel.add(loginPanel);

        setContentPane(mainPanel);
        setVisible(true);
    }

}
