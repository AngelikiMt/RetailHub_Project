import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class TransactionFrame extends JFrame {
    private final JPanel contentPanel;
    private final TransactionService transactionService;
    private final ClientService clientService;
    Font customFont = new Font("MinionPro", Font.PLAIN, 20);

    public TransactionFrame() {
        setTitle("Transaction Menu");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel background = new JPanel();
        background.setOpaque(false);
        this.add(background);
        getContentPane().setBackground(Color.WHITE);

        // Top navigation bar
        JPanel topBar = new JPanel();
        topBar.setLayout(new BoxLayout(topBar, BoxLayout.X_AXIS));
        topBar.setBackground(new Color(239, 247, 255));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Navigation items
        String[] navItems = {"Create", "View All", "Export JSON"};

        JLabel logoLabel = new JLabel();
        ImageIcon logoIcon = null;
        logoIcon = new ImageIcon(getClass().getResource("/croppedLogo.png"));
        if (logoIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                Image image = logoIcon.getImage();
                Image scaledImage = image.getScaledInstance(-1, 60, Image.SCALE_SMOOTH);
                logoIcon = new ImageIcon(scaledImage);
        } else {
            System.err.println("Warning: Could not load logo.png, or it's not a valid image.");
            // Fallback to text if image fails to load
            logoLabel.setText("TransactionMenu");
            logoLabel.setFont(new Font("MinionPro", Font.BOLD, 25));
            logoLabel.setForeground(Color.BLACK);
        }

        if (logoIcon != null) {
        logoLabel.setIcon(logoIcon);
        }
        topBar.add(logoLabel);        
        topBar.add(Box.createHorizontalStrut(30)); // space before nav items
        topBar.add(Box.createHorizontalGlue()); // Pushes elements to the right

        Font navFont = new Font("MinionPro", Font.BOLD, 20);

        for (String item : navItems) {
            JButton navButton = new JButton(item);
            navButton.setFont(navFont);
            navButton.setFocusPainted(false);
            navButton.setForeground(new Color(0,0,205));
            navButton.setBackground(new Color(239, 247, 255));
            navButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            navButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            navButton.setOpaque(true);
            navButton.setBorderPainted(false);
            navButton.setAlignmentY(Component.CENTER_ALIGNMENT);
            navButton.setMaximumSize(new Dimension(250, 30));

            topBar.add(navButton);
            topBar.add(Box.createHorizontalStrut(10));

            // Adds action listeners based on button text
            switch (item) {
                case "Create" -> navButton.addActionListener(e -> menuCreateTransaction());
                case "View All" -> navButton.addActionListener(e -> menuViewAllTransactions());
                case "Export JSON" -> navButton.addActionListener(e -> menuGetTransactionAsJson());
            }
        }

        JButton backButton = new JButton();
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("left-arrow.png"));
        ImageIcon scaledIcon = null;

        if (originalIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            Image image = originalIcon.getImage();
            Image newImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            scaledIcon = new ImageIcon(newImage);
            backButton.setIcon(scaledIcon);
        } else {
            System.err.println("Warning: Could not load left-arrow.png, or it's not a valid image.");
            backButton.setText("Back");
        }

        backButton.setPreferredSize(new Dimension(50, 50));
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> dispose());

        // Adds to the right side of the top bar
        topBar.add(Box.createHorizontalStrut(10));
        background.setLayout(new BorderLayout());
        background.add(topBar, BorderLayout.NORTH);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        background.add(contentPanel, BorderLayout.CENTER);

        // Bottom panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        bottomPanel.add(backButton);
        background.add(bottomPanel, BorderLayout.SOUTH);

        transactionService = new TransactionService(); // Initialize TransactionService
        clientService = new ClientService(); // Initialize ClientService

        menuCreateTransaction(); // Shows create form by default
        setVisible(true);
    }

    private void menuCreateTransaction() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        JPanel borderedContentPanel = new JPanel(new BorderLayout());
        borderedContentPanel.setOpaque(false);

        TitledBorder titled = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY, 2),
            "",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("MinionPro", Font.BOLD, 20),
            Color.DARK_GRAY
        );
        borderedContentPanel.setBorder(BorderFactory.createCompoundBorder(
            titled,
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        JLabel iconLabel = new JLabel();
        ImageIcon addIcon = null;
        try {
            addIcon = new ImageIcon(getClass().getResource("/add-store.png"));
            if (addIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                Image image = addIcon.getImage();
                Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                addIcon = new ImageIcon(scaledImage);
            } else {
                System.err.println("Warning: Could not load add-store.png");
                iconLabel.setText("Create Transaction");
                iconLabel.setFont(new Font("MinionPro", Font.BOLD, 22));
                iconLabel.setForeground(Color.DARK_GRAY);
            }
        } catch (Exception e) {
            System.err.println("Error loading add-store.png: " + e.getMessage());
            iconLabel.setText("Create Transaction");
            iconLabel.setFont(new Font("MinionPro", Font.BOLD, 22));
            iconLabel.setForeground(Color.DARK_GRAY);
        }

        if (addIcon != null) {
            iconLabel.setIcon(addIcon);
        }
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        borderedContentPanel.add(iconLabel, BorderLayout.NORTH);

        JPanel fieldsAndButtonPanel = new JPanel(new BorderLayout());
        fieldsAndButtonPanel.setOpaque(false);
        fieldsAndButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JPanel formFieldsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formFieldsPanel.setOpaque(false);

        JTextField clientInputField = new JTextField(15);
        JTextField storeIdField = new JTextField(15);
        JTextField productIdField = new JTextField(15);
        JTextField quantityField = new JTextField(15);

        JTextField[] fields = {clientInputField, storeIdField, productIdField, quantityField};
        for (JTextField field : fields) {
            field.setFont(customFont);
            field.setPreferredSize(new Dimension(200, 40));
            field.setBorder(BorderFactory.createCompoundBorder(
                field.getBorder(),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
        }

        formFieldsPanel.add(new JLabel("Client's Email or Phone:")).setFont(new Font("MinionPro", Font.BOLD, 20));
        formFieldsPanel.add(clientInputField);

        formFieldsPanel.add(new JLabel("Store ID:")).setFont(new Font("MinionPro", Font.BOLD, 20));
        formFieldsPanel.add(storeIdField);

        formFieldsPanel.add(new JLabel("Product ID:")).setFont(new Font("MinionPro", Font.BOLD, 20));
        formFieldsPanel.add(productIdField);

        formFieldsPanel.add(new JLabel("Quantity:")).setFont(new Font("MinionPro", Font.BOLD, 20));
        formFieldsPanel.add(quantityField);

        fieldsAndButtonPanel.add(formFieldsPanel, BorderLayout.CENTER);

        JButton addProductBtn = new JButton("Add Product");
        JButton submitTransactionBtn = new JButton("Submit Transaction");

        JButton[] buttons = {addProductBtn, submitTransactionBtn};
        for (JButton button : buttons) {
            button.setBackground(new Color(128, 0, 128));
            button.setForeground(Color.WHITE);
            button.setFont(new Font("MinionPro", Font.BOLD, 20));
            button.setPreferredSize(new Dimension(200, 40));
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonPanel.add(addProductBtn);
        buttonPanel.add(submitTransactionBtn);

        fieldsAndButtonPanel.add(buttonPanel, BorderLayout.SOUTH);
        borderedContentPanel.add(fieldsAndButtonPanel, BorderLayout.CENTER);
        centerWrapper.add(borderedContentPanel);
        contentPanel.add(centerWrapper, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();

        List<Long> productIds = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();

        addProductBtn.addActionListener(e -> {
            try {
                long productId = Long.parseLong(productIdField.getText().trim());
                int quantity = Integer.parseInt(quantityField.getText().trim());
                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(this, "Quantity must be greater than zero.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                productIds.add(productId);
                quantities.add(quantity);                
                JOptionPane.showMessageDialog(this, "Product added to transaction.");
                productIdField.setText("");
                quantityField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid product ID or quantity. Please enter numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        submitTransactionBtn.addActionListener(e -> {
            try {
                if (productIds.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please add products before submitting the transaction.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String clientInput = clientInputField.getText().trim();
                Long clientId = clientService.authenticateClient1(clientInput);                
                if (clientId == null) {
                    JOptionPane.showMessageDialog(this, "Client not found. Please ensure the email or phone is correct.", "Client Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int storeId = Integer.parseInt(storeIdField.getText().trim());
                TransactionService.ShowTotalResult totalResult = TransactionService.calculateTotal(productIds, quantities, storeId, clientId);
                double sumTotal = totalResult.getSumTotal();
                double discount = totalResult.getDiscount();
                double finalAmount = sumTotal - discount;

                String[] paymentMethods = {"card", "cash", "credit"};
                String selectedPaymentMethod = (String) JOptionPane.showInputDialog(
                        this,
                        "Choose a payment method:",
                        "Payment Selection",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        paymentMethods,
                        paymentMethods[0]
                );

                if (selectedPaymentMethod == null) {
                    JOptionPane.showMessageDialog(this, "Transaction cancelled. No payment method selected.", "Transaction Cancelled", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                Object[] options = {"Confirm", "Cancel"};
                String message = String.format("Do you want to proceed with the transaction?\n" +
                               "Subtotal: %.2f\n" +
                               "Discount: %.2f\n" +
                               "Final Amount: %.2f",
                        sumTotal, discount, finalAmount);

                int result = JOptionPane.showOptionDialog(
                        this,
                        message,
                        "Confirm Transaction",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]
                );

                if (result == JOptionPane.YES_OPTION) {
                    Transaction t = transactionService.createTransaction(productIds, quantities, storeId, clientId, selectedPaymentMethod);
                    JOptionPane.showMessageDialog(this, "Transaction created successfully:\n" + t.toString(), "Transaction Success", JOptionPane.INFORMATION_MESSAGE);
                    // Clear fields and lists for next transaction
                    clientInputField.setText("");
                    storeIdField.setText("");
                    productIdField.setText("");
                    quantityField.setText("");
                    productIds.clear();
                    quantities.clear();
                } else {
                    JOptionPane.showMessageDialog(this, "Transaction cancelled.", "Transaction Cancelled", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input for Store ID. Please enter a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private DefaultTableModel transactionTableModel;
    private JTable transactionTable;

    private void menuViewAllTransactions() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        JPanel tableDisplayPanel = new JPanel(new BorderLayout());
        tableDisplayPanel.setOpaque(false);
        tableDisplayPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        transactionTableModel = new DefaultTableModel(new String[]{"ID", "Client ID", "Store ID", "Date", "Total Amount", "Discount", "Payment Method", "Products"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Makes table non-editable
            }
        };
        transactionTable = new JTable(transactionTableModel);
        transactionTable.setFont(new Font("MinionPro", Font.PLAIN, 18));
        transactionTable.getTableHeader().setFont(new Font("MinionPro", Font.BOLD, 20));
        transactionTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(transactionTable);
        tableDisplayPanel.add(scrollPane, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("");
        titleLabel.setFont(new Font("MinionPro", Font.BOLD, 20));
        titleLabel.setForeground(Color.DARK_GRAY);

        TitledBorder titled = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "", // no text
                TitledBorder.LEFT,
                TitledBorder.TOP
        );

        JPanel titledPanel = new JPanel(new BorderLayout());
        titledPanel.setOpaque(false);
        titledPanel.setBorder(BorderFactory.createCompoundBorder(
                titled,
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        titledPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel overallPanel = new JPanel(new BorderLayout());
        overallPanel.setOpaque(false);
        overallPanel.add(titledPanel, BorderLayout.NORTH);
        overallPanel.add(tableDisplayPanel, BorderLayout.CENTER);

        contentPanel.add(overallPanel, BorderLayout.CENTER);

        refreshTransactionTable(); // Populate table on display

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void refreshTransactionTable() {
        transactionTableModel.setRowCount(0); // Clears existing data
        List<Transaction> allTransactions = transactionService.getAllTransactions();
        List<Includes> allIncludes = transactionService.getAllIncludes();

        // Creates a map from transactionId to a list of its Includes
        Map<Long, List<Includes>> transactionIncludesMap = new HashMap<>();
        for (Includes include : allIncludes) {
            transactionIncludesMap.computeIfAbsent(include.getTransactionId(), k -> new ArrayList<>()).add(include);
        }

        for (Transaction t : allTransactions) {
            StringBuilder productsDisplay = new StringBuilder();
            List<Includes> includesForTransaction = transactionIncludesMap.getOrDefault(t.getTransactionId(), new ArrayList<>());

            if (!includesForTransaction.isEmpty()) {
                for (Includes inc : includesForTransaction) {
                    productsDisplay.append(inc.getProductId()).append(" (x").append(inc.getSoldQuantity()).append(") ");
                }
            } else {
                productsDisplay.append("N/A");
            }

            transactionTableModel.addRow(new Object[]{
                    t.getTransactionId(),
                    t.getClientId(),
                    t.getStoreId(),
                    t.getDateTime(),
                    String.format("%.2f", t.getSumTotal()),
                    String.format("%.2f", t.getDiscount()),
                    t.getPaymentMethod(),
                    productsDisplay.toString()
            });
        }
    }

    private void menuGetTransactionAsJson() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel enterTextLabel = new JLabel("Enter Transaction ID:");
        enterTextLabel.setFont(new Font("MinionPro", Font.BOLD, 20));

        JTextField idField = new JTextField(15);
        idField.setFont(customFont);
        idField.setPreferredSize(new Dimension(200, 30));
        idField.setBorder(BorderFactory.createCompoundBorder(
                idField.getBorder(),
                BorderFactory.createEmptyBorder(5, 10, 1, 10)
        ));

        JButton showJsonBtn = new JButton("Show JSON");
        showJsonBtn.setBackground(new Color(128, 0, 128));
        showJsonBtn.setForeground(Color.WHITE);
        showJsonBtn.setFont(customFont);

        form.add(enterTextLabel);
        form.add(idField);
        form.add(showJsonBtn);

        // === DISPLAY PANEL ===
        JTextArea jsonDisplayArea = new JTextArea(10, 50);
        jsonDisplayArea.setEditable(false);
        jsonDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 20));
        jsonDisplayArea.setLineWrap(true);
        jsonDisplayArea.setWrapStyleWord(true);
        jsonDisplayArea.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        jsonDisplayArea.setBackground(Color.WHITE);
        jsonDisplayArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(jsonDisplayArea);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "", TitledBorder.LEFT, TitledBorder.TOP
        ));

        JLabel titleLabel = new JLabel("");
        titleLabel.setFont(new Font("MinionPro", Font.BOLD, 20));
        titleLabel.setForeground(Color.DARK_GRAY);

        // Box layout to stack vertically
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));
        centerPanel.add(form);
        centerPanel.add(scrollPane);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // === CENTER WRAPPER FOR GRID ALIGNMENT ===
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(centerPanel);

        contentPanel.add(centerWrapper, BorderLayout.CENTER);

        showJsonBtn.addActionListener(e -> {
            try {
                if (idField.getText().trim().isEmpty()) {
                    jsonDisplayArea.setText("Please enter a Transaction ID to view JSON.");
                    return;
                }
                long id = Long.parseLong(idField.getText());
                String json = transactionService.getTransactionAsJson(id);
                if (json != null && !json.contains("Transaction not found")) {
                    jsonDisplayArea.setText(json);
                } else {
                    jsonDisplayArea.setText("Transaction not found with ID: " + id);
                }
            } catch (NumberFormatException ex) {
                jsonDisplayArea.setText("Invalid Transaction ID. Please enter a number.");
            } catch (Exception ex) {
                jsonDisplayArea.setText("An error occurred: " + ex.getMessage());
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }
}