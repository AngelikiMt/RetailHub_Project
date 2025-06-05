import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class MainMenu extends JFrame {
    private static final String ADMIN_PIN = "12345";
    private String path;

    public static ImageIcon getIcon (String path){
        ImageIcon icon = new ImageIcon(MainMenu.class.getResource(path));
        Image image = icon.getImage().getScaledInstance(285, 195, Image.SCALE_SMOOTH); // π.χ. 64x64
        icon = new ImageIcon(image);
        return icon ;
    }

    public MainMenu() {
        setTitle("Retail Hub Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen

        JPanel topPanel = new JPanel(new BorderLayout()) ;
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setPreferredSize(new Dimension(400,350));
        topPanel.setBackground(new Color(239, 247, 255));
        
        // Left side (welcome text + description)
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.setBorder(BorderFactory.createEmptyBorder(40, 5, 5, 10));

        JLabel welcomeLabel = new JLabel("Welcome to RetailHub");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 35));
        welcomeLabel.setForeground(new Color(0,0,205));
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        //welcomeLabel.setHorizontalAlignment(SwingConstants.LEFT);
        //welcomeLabel.setVerticalAlignment(SwingConstants.NORTH);
        //welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10)); // padding

        JTextArea textArea = new JTextArea("Welcome to RetailHub! Your modern destination for smart, high-quality shopping. Explore our products, manage your transactions, and track your store's progress—all in one place. Select an option to get started!");
        textArea.setFont(new Font("SansSerif", Font.ITALIC, 14));
        textArea.setEditable(false); // αν δεν θες να γράφει ο χρήστης
        textArea.setFocusable(false);  
        textArea.setOpaque(false); // αφαιρεί το φόντο αν θες να φαίνεται "σαν Label"
        textArea.setBorder(null);     // αφαιρεί περίγραμμα
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setAlignmentX(Component.LEFT_ALIGNMENT);
       // textArea.setAlignmentX(Component.BOTTOM_ALIGNMENT);
       
        textPanel.add(welcomeLabel);
        textPanel.add(Box.createVerticalStrut(10)); // spacing
        textPanel.add(textArea);

        // Right: Company logo or image
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("retaillogo.png")); // Replace with your image path
        Image scaledImage = logoIcon.getImage().getScaledInstance(500, 400, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        imageLabel.setVerticalAlignment(SwingConstants.TOP);
        //imageLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10)); // padding

        topPanel.add(textPanel, BorderLayout.CENTER);
        topPanel.add(imageLabel, BorderLayout.EAST);
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50)); // Αυτό δημιουργεί το κενό
        wrapper.setBackground(Color.WHITE); // Για να φαίνεται το περίγραμμα
        wrapper.add(topPanel, BorderLayout.CENTER);

        // === BUTTONS ===
        JButton clientBtn = new JButton();
        path = "clienticon.png";
        clientBtn.setIcon(getIcon(path));

        JButton productBtn = new JButton();
            path = "/producticon.png";
            productBtn.setIcon(getIcon(path));

        JButton storeBtn = new JButton("STORE (PIN)");
            path = "/storeicon.png";
            storeBtn.setIcon(getIcon(path));

        JButton stockBtn = new JButton();
            path = "/stockicon.png";
            stockBtn.setIcon(getIcon(path));

        JButton transactionBtn = new JButton();
            path = "/transactionicon.png";
            transactionBtn.setIcon(getIcon(path));
           
        JButton reportsBtn = new JButton("Reports");
            path = "/reportsicon.png";
            reportsBtn.setIcon(getIcon(path));

        //JButton exitBtn = new JButton("EXIT");

        Dimension buttonSize = new Dimension(200, 120);

        JButton[] buttons = {clientBtn, productBtn, storeBtn, stockBtn, transactionBtn, reportsBtn};
              
         for (JButton btn : buttons) {
            btn.setPreferredSize(buttonSize);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setFocusPainted(false);
            btn.setOpaque(false);
         }

        // === PANEL WITH 3 COLUMNS × 2 LINES ===
        JPanel buttonGrid = new JPanel(new GridLayout(2, 3, 20, 20));
        buttonGrid.setOpaque(false); // Transparent so the background is visible
        for (JButton btn : buttons) {
            buttonGrid.add(btn);
        }

        // === CENTERING BUTTONS BOTTOM ===
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 40));
        bottomPanel.setOpaque(false);
        bottomPanel.add(buttonGrid);

        //backgroundPanel.add(bottomPanel, BorderLayout.SOUTH);

        // === ACTIONS ===
        clientBtn.addActionListener(e -> new ClientFrame());
        productBtn.addActionListener(e -> new ProductFrame());
        storeBtn.addActionListener(e -> new StoreLoginFrame());
        stockBtn.addActionListener(e -> new StockFrame());
        transactionBtn.addActionListener(e -> new TransactionFrame());
        reportsBtn.addActionListener(e -> new ReportsFrame());

        //exitBtn.addActionListener(e -> {
        //     dispose();
        //     new LoginFrame();
        // });

        this.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
        this.getContentPane().add(wrapper,BorderLayout.NORTH);
        getContentPane().setBackground(Color.WHITE);    
        //setContentPane(backgroundPanel);
        setVisible(true);
    }
}


