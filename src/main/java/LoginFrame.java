import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class LoginFrame extends JFrame {
    private static final String USERNAME = "";
    private static final String PASSWORD = "";

    public LoginFrame() {
        setTitle("Login - Retail Hub");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
               
        ImageIcon icon = new ImageIcon(getClass().getResource("loginicon_120.png"));
        JLabel logoLabel = new JLabel(icon);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // // === Custom JPanel with background image ===
        // JPanel backgroundPanel = new JPanel() {
        //     Image background = new ImageIcon(getClass().getResource("loginicon.png")).getImage();
        //     @Override
        //     protected void paintComponent(Graphics g) {
        //         super.paintComponent(g);
        //         g.drawImage(background, 10, 10, getWidth(), getHeight(), this);
        //     }
        // };
        // backgroundPanel.setLayout(new GridBagLayout()); // Centers the login panel

        // === Connection Panel ===
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setOpaque(false); // Show the background behind it
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(15);

        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);

        JCheckBox showPass = new JCheckBox("Show Password");
        showPass.setOpaque(false); // To be transparent
        showPass.addActionListener(e -> {
            passField.setEchoChar(showPass.isSelected() ? (char) 0 : '•');
        });

        //ImageIcon login = new ImageIcon(getClass().getResource("login.png"));
        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(128, 0, 128)); // Purple background
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("MinionPro", Font.PLAIN, 20));
        loginBtn.setPreferredSize(new Dimension(120, 30));

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

        // === Add items ===
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

        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new GridBagLayout());

        // === Placing loginPanel in the center ===
        GridBagConstraints panelGbc = new GridBagConstraints();
        panelGbc.gridx = 0; panelGbc.gridy = 0;
        mainPanel.add(logoLabel,panelGbc);

        panelGbc.gridx = 0; panelGbc.gridy = 1;
        mainPanel.add(loginPanel,panelGbc);

        //panelGbc.weighty = 1.0;
        //panelGbc.anchor = GridBagConstraints.PAGE_END;
        //panelGbc.insets = new Insets(0, 0, 10, 0);
        //backgroundPanel.add(loginPanel, panelGbc);
        //setContentPane(backgroundPanel);
        this.getContentPane().add(mainPanel);
        getContentPane().setBackground(Color.WHITE);
        pack();
        this.setSize(350,350);
        this.setLocationRelativeTo(null);
        setVisible(true);
    }
}
