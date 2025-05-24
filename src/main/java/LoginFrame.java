import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private static final String USERNAME = "";
    private static final String PASSWORD = "";

    public LoginFrame() {
        setTitle("Login - Retail Hub");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Πλήρης οθόνη

        // === Custom JPanel με background εικόνα ===
        JPanel backgroundPanel = new JPanel() {
            Image background = new ImageIcon(getClass().getResource("/RetailHub.png")).getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new GridBagLayout()); // Κεντράρει το login panel

        // === Panel σύνδεσης ===
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setOpaque(false); // Να φαίνεται το background πίσω του

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(15);

        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);

        JCheckBox showPass = new JCheckBox("Show Password");
        showPass.setOpaque(false); // Για να είναι διάφανο
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
                    dispose();
                    new MainMenu();
                });
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // === Προσθήκη στοιχείων ===
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

        // === Τοποθέτηση loginPanel στο κέντρο ===
        backgroundPanel.add(loginPanel, new GridBagConstraints());

        setContentPane(backgroundPanel);
        setVisible(true);
    }

}
