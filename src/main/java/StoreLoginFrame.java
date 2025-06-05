import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class StoreLoginFrame extends JFrame {
    private static final String PASSWORD = "12345";


    public StoreLoginFrame() {
        setTitle("Login - Store");
        setFont(new Font("MinionPro", Font.BOLD, 20));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
               
        ImageIcon icon = new ImageIcon(getClass().getResource("storeicon.png"));
        // Get the scaled instance. This returns an 'Image' object.
        Image scaledImage = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH); // Made it 150x150 for better visibility as a logo
        // Create a new ImageIcon from the scaled Image
        ImageIcon finalScaledIcon = new ImageIcon(scaledImage);        
        JLabel logoLabel = new JLabel(finalScaledIcon);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // === Connection Panel ===
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(1, 10, 1, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("MinionPro", Font.BOLD, 20));
        JPasswordField passField = new JPasswordField(15);

        JCheckBox showPass = new JCheckBox("Show Password");
        showPass.setFont(new Font("MinionPro", Font.BOLD, 17));
        showPass.setOpaque(false);
        showPass.addActionListener(e -> {
            passField.setEchoChar(showPass.isSelected() ? (char) 0 : '•');
        });

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(128, 0, 128)); // Purple background
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("MinionPro", Font.PLAIN, 20));
        loginBtn.setPreferredSize(new Dimension(120, 30));

        loginBtn.addActionListener(e -> {
            String enteredPass = new String(passField.getPassword());

            if (PASSWORD.trim().equals(enteredPass)) {
                SwingUtilities.invokeLater(() -> {
                    dispose();
                    new StoreFrame();
                });
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // === Add items ===
        gbc.gridx = 0; gbc.gridy = 0;
        loginPanel.add(passLabel, gbc);
        gbc.gridx = 1;
        loginPanel.add(passField, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        loginPanel.add(showPass, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        loginPanel.add(loginBtn, gbc);

        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new GridBagLayout());

        // === Placing loginPanel in the center ===
        GridBagConstraints panelGbc = new GridBagConstraints();
        panelGbc.gridx = 0; panelGbc.gridy = 0;
        mainPanel.add(logoLabel,panelGbc);

        panelGbc.gridx = 0; panelGbc.gridy = 1;
        mainPanel.add(loginPanel,panelGbc);

        this.getContentPane().add(mainPanel);
        getContentPane().setBackground(Color.WHITE);
        pack();
        this.setSize(370,350);
        this.setLocationRelativeTo(null);
        setVisible(true);
    }
}
