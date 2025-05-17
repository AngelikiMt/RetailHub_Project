import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {
    private static final String ADMIN_PIN = "12345";

    public MainMenu() {
        setTitle("Retail Hub Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen

        // === PANEL ΜΕ ΦΟΝΤΟ ===
        JPanel backgroundPanel = new JPanel() {
            Image bg = new ImageIcon("RetailHub.png").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        // === ΚΟΥΜΠΙΑ ===
        JButton clientBtn = new JButton("CLIENT");
        JButton productBtn = new JButton("PRODUCT");
        JButton storeBtn = new JButton("STORE (PIN)");
        JButton stockBtn = new JButton("STOCK");
        JButton transactionBtn = new JButton("TRANSACTION");
        JButton exitBtn = new JButton("EXIT");

        Font buttonFont = new Font("Arial", Font.BOLD, 20);
        Dimension buttonSize = new Dimension(200, 50);

        JButton[] buttons = {clientBtn, productBtn, storeBtn, stockBtn, transactionBtn, exitBtn};
        for (JButton btn : buttons) {
            btn.setFont(buttonFont);
            btn.setPreferredSize(buttonSize);
        }

        // === PANEL ΜΕ 3 ΣΤΗΛΕΣ × 2 ΓΡΑΜΜΕΣ ===
        JPanel buttonGrid = new JPanel(new GridLayout(2, 3, 30, 30));
        buttonGrid.setOpaque(false); // διαφανές για να φαίνεται το background
        for (JButton btn : buttons) {
            buttonGrid.add(btn);
        }

        // === ΚΕΝΤΡΑΡΙΣΜΑ ΚΟΥΜΠΙΩΝ ΚΑΤΩ ===
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 40));
        bottomPanel.setOpaque(false);
        bottomPanel.add(buttonGrid);

        backgroundPanel.add(bottomPanel, BorderLayout.SOUTH);

        // === ΔΡΑΣΕΙΣ ===
        clientBtn.addActionListener(e -> new ClientFrame());
        productBtn.addActionListener(e -> new ProductFrame());
        storeBtn.addActionListener(e -> {
            String pin = JOptionPane.showInputDialog(this, "Enter PIN:");
            if (ADMIN_PIN.equals(pin)) {
                new StoreFrame();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid PIN.");
            }
        });
        stockBtn.addActionListener(e -> new StockFrame());
        transactionBtn.addActionListener(e -> new TransactionFrame());
        exitBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        setContentPane(backgroundPanel);
        setVisible(true);
    }


}


