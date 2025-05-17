import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {
    private static final String ADMIN_PIN = "12345";

    public MainMenu() {
        setTitle("Retail Hub Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen

        // === ΚΥΡΙΟ PANEL ===
        JPanel mainPanel = new JPanel(new BorderLayout());

        // === ΕΙΚΟΝΑ ΣΤΟ ΠΑΝΩ ΜΕΡΟΣ ===
        ImageIcon imageIcon = new ImageIcon("RetailHub.png"); // Αντικατέστησέ το με την εικόνα σου
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(imageLabel, BorderLayout.NORTH);

        // === PANEL ΚΟΥΜΠΙΩΝ ΜΕ 2 ΣΤΗΛΕΣ ===
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 40, 40)); // 3 γραμμές, 2 στήλες, με κενά

        JButton clientBtn = new JButton("CLIENT");
        JButton productBtn = new JButton("PRODUCT");
        JButton storeBtn = new JButton("STORE (PIN)");
        JButton stockBtn = new JButton("STOCK");
        JButton transactionBtn = new JButton("TRANSACTION");
        JButton exitBtn = new JButton("EXIT");

        // Στυλ για κουμπιά
        Font buttonFont = new Font("Arial", Font.BOLD, 22);
        for (JButton btn : new JButton[]{clientBtn, productBtn, storeBtn, stockBtn, transactionBtn, exitBtn}) {
            btn.setFont(buttonFont);
            buttonPanel.add(btn);
        }

        // === ΔΡΑΣΕΙΣ ΚΟΥΜΠΙΩΝ ===
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
        exitBtn.addActionListener(e ->{dispose();       
                    new LoginFrame();});

        // Κέντρο: κουμπιά κάτω από την εικόνα
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 50));
        centerPanel.add(buttonPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
        setVisible(true);
    }

}

