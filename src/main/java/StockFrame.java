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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class StockFrame extends JFrame {
    private final JPanel contentPanel;
    Font customFont = new Font("MinionPro", Font.PLAIN, 20);
    // Table model and table for "View All Clients" to be accessible for refresh
    private DefaultTableModel model;
    private JTable table;
    private JTextField prodField, excludedStField;
    private JScrollPane searchTableScrollPane;

    public StockFrame() {
        setTitle("Stock Menu");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(800, 600));

        JPanel background = new JPanel();
        background.setOpaque(false);
        this.add(background);
        getContentPane().setBackground(Color.WHITE);

        // clientTableModel = new DefaultTableModel(new String[]{
        //     "ID", "First Name", "Last Name", "Email", "Phone", "Gender",
        //     "Birth Date", "Active", "Date Joined", "Last Purchase", "Sum"
        // }, 0) {
        //     @Override
        //     public boolean isCellEditable(int row, int column) {
        //         return false; // Makes table non-editable
        //     }
        // };
        // clientTable = new JTable(clientTableModel);
        // clientTable.setFont(new Font("MinionPro", Font.PLAIN, 15));
        // clientTable.getTableHeader().setFont(new Font("MinionPro", Font.BOLD, 17));
        // clientTable.setRowHeight(30);

        // Top navigation bar
        JPanel topBar = new JPanel();
        topBar.setLayout(new BoxLayout(topBar, BoxLayout.X_AXIS));
        topBar.setBackground(new Color(239, 247, 255));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Navigation items
        String[] navItems  = {
            "Set stock", "Get stock", "Add stock", "View low stock","Search other stores", "Export JSON"};

        // Icon Logo
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
            logoLabel.setText("ClientMenu");
            logoLabel.setFont(new Font("MinionPro", Font.BOLD, 25));
            logoLabel.setForeground(Color.BLACK);
        }

        if (logoIcon != null) {
        logoLabel.setIcon(logoIcon);
        }
        topBar.add(logoLabel);
        topBar.add(Box.createHorizontalStrut(15)); // space before nav items
        topBar.add(Box.createHorizontalGlue());

        Font navFont = new Font("MinionPro", Font.BOLD, 20);

        for (String item : navItems) {
            JButton navButton = new JButton(item);
            navButton.setFont(navFont);
            navButton.setFocusPainted(false);
            navButton.setForeground(new Color(0,0,205)); // Light blue background
            navButton.setBackground(new Color(239, 247, 255)); // Blue text
            navButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            navButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            navButton.setOpaque(true);
            navButton.setBorderPainted(false);
            navButton.setAlignmentY(Component.CENTER_ALIGNMENT);
            navButton.setMaximumSize(new Dimension(250, 30));
            
            topBar.add(navButton);
            // Adds spacing between buttons
            topBar.add(Box.createHorizontalStrut(5));

            // Adds action listeners based on button text
            switch (item) {
                case "Set stock" -> navButton.addActionListener(e -> menuSetStock());
                case "Get stock" -> navButton.addActionListener(e -> menuGetStock());
                case "Add stock" -> navButton.addActionListener(e -> menuAddStock());
                case "View low stock" -> navButton.addActionListener(e -> menuViewLowStock());
                case "Search other stores" -> navButton.addActionListener(e -> menuSearchOtherStores());
                case "Export JSON" -> navButton.addActionListener(e -> menuGetJson());
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
            backButton.setText("Back"); // Fallback
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

        menuSetStock(); // Shows create form by default
        setVisible(true); // Shows window
    }

          protected void showError(String message) {
            JOptionPane.showMessageDialog(this, "Error: " + message, "Error", JOptionPane.ERROR_MESSAGE);
        }

        protected void showSuccess(String message) {
            JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    
    private void menuSetStock() {
        contentPanel.removeAll(); // Clears previous content
        contentPanel.setLayout(new BorderLayout());

        // Main panel to hold the form, centered on the screen
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false); 

        // This is the panel that will have the border and contain the icon and the form fields
        JPanel borderedContentPanel = new JPanel(new BorderLayout());
        borderedContentPanel.setOpaque(false);

        // Set the border for this panel
        TitledBorder titled = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY, 2),
            "", // No title text here, the icon will be visually above
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
            addIcon = new ImageIcon(getClass().getResource("/setstock.png"));

            if (addIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                Image image = addIcon.getImage();
                Image scaledImage = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                addIcon = new ImageIcon(scaledImage);
            } else {
                System.err.println("Warning: Could not load add.png, or it's not a valid image.");
                // Fallback to text if icon fails to load
                iconLabel.setText("Set Initial Stock");
                iconLabel.setFont(new Font("MinionPro", Font.BOLD, 22));
                iconLabel.setForeground(Color.DARK_GRAY);
            }
        } catch (Exception e) {
            System.err.println("Error loading add.png: " + e.getMessage());
            // Fallback to text if an exception occurs
            iconLabel.setText("Set initial stock");
            iconLabel.setFont(new Font("MinionPro", Font.BOLD, 22));
            iconLabel.setForeground(Color.DARK_GRAY);
        }
        if (addIcon != null) {
            iconLabel.setIcon(addIcon);
        }

        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        borderedContentPanel.add(iconLabel, BorderLayout.NORTH);

        // Creates a panel to hold the form fields and button
        JPanel fieldsAndButtonPanel = new JPanel(new BorderLayout());
        fieldsAndButtonPanel.setOpaque(false);
        fieldsAndButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // Padding below icon

        // Form layout
        JPanel formFieldsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formFieldsPanel.setOpaque(false);

        JTextField productField = new JTextField(15);
        productField.setFont(customFont);
        productField.setPreferredSize(new Dimension(200, 10));
        productField.setBorder(BorderFactory.createCompoundBorder(
            productField.getBorder(),
            BorderFactory.createEmptyBorder(1, 5, 1, 5)
        ));

        JTextField storeField = new JTextField(15);
        storeField.setFont(customFont);
        storeField.setPreferredSize(new Dimension(200, 10));
        storeField.setBorder(BorderFactory.createCompoundBorder(
            storeField.getBorder(),
            BorderFactory.createEmptyBorder(1, 5, 1, 5)
        ));

        JTextField quantityField = new JTextField(15);
        quantityField.setFont(customFont);
        quantityField.setPreferredSize(new Dimension(200, 10));
        quantityField.setBorder(BorderFactory.createCompoundBorder(
            quantityField.getBorder(),
            BorderFactory.createEmptyBorder(1, 5, 1, 5)
        ));

        // Add fields to formFieldsPanel
        JLabel label = new JLabel("Product ID:");
        label.setFont(new Font("MinionPro", Font.BOLD, 20));
        formFieldsPanel.add(label); formFieldsPanel.add(productField);

        JLabel lLabel = new JLabel("Store ID");
        lLabel.setFont(new Font("MinionPro", Font.BOLD, 20));
        formFieldsPanel.add(lLabel); formFieldsPanel.add(storeField);

        JLabel pLabel = new JLabel("Quantity to Add:");
        pLabel.setFont(new Font("MinionPro", Font.BOLD, 20));
        formFieldsPanel.add(pLabel); formFieldsPanel.add(quantityField);

        // Adds formFieldsPanel to the center of the fieldsAndButtonPanel
        fieldsAndButtonPanel.add(formFieldsPanel, BorderLayout.CENTER);

        // Submit button
        JButton submit = new JButton("Set Stock");
        submit.setBackground(new Color(128, 0, 128)); // Purple background
        submit.setForeground(Color.WHITE);
        submit.setFont(new Font("MinionPro", Font.BOLD, 20));
        submit.setPreferredSize(new Dimension(200, 40));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonPanel.add(submit);

        fieldsAndButtonPanel.add(buttonPanel, BorderLayout.SOUTH);
        borderedContentPanel.add(fieldsAndButtonPanel, BorderLayout.CENTER);

        centerWrapper.add(borderedContentPanel);
        contentPanel.add(centerWrapper, BorderLayout.CENTER);

        // Create client on submit
        submit.addActionListener(e -> {
            try {
                int storeId = Integer.parseInt(storeField.getText());
                long productId = Long.parseLong(productField.getText());
                int quantity = Integer.parseInt(quantityField.getText());

                StockService.addStock(storeId, productId, quantity);
                // Clear fields after successful submission
                productField.setText("");
                storeField.setText("");
                quantityField.setText("");
            // } catch (NumberFormatException ex) {
            //     showError("Invalid number format for ID or Quantity.");
            // } catch (Exception ex) {
            //     showError(ex.getMessage());
            // }
                //refreshTable(); // Update table
                JOptionPane.showMessageDialog(contentPanel, "Stock" + productId +" added successfully to store" + storeId);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(contentPanel, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace(); // Print stack trace for debugging
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void menuGetStock() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(80, 100, 80, 100));

        JTextField productIdField = new JTextField(10);
        JTextField storeIdField = new JTextField(10);
      
        JTextField[] fields = {productIdField, storeIdField};
        for (JTextField field : fields) {
            field.setFont(customFont);
            field.setPreferredSize(new Dimension(200, 35));
            field.setBorder(BorderFactory.createCompoundBorder(
                field.getBorder(),
                BorderFactory.createEmptyBorder(0, 8, 0, 8)
        ));
        }

    JButton search = new JButton("Search");
    search.setBackground(new Color(128, 0, 128));
    search.setForeground(Color.WHITE);
    search.setFont(customFont);

    JLabel storeLabel = new JLabel("Store ID");
    storeLabel.setFont(new Font("MinionPro", Font.BOLD, 20));
    formPanel.add(storeLabel); formPanel.add(storeIdField);

    JLabel productLabel = new JLabel("Product ID");
    productLabel.setFont(new Font("MinionPro", Font.BOLD, 20));
    formPanel.add(productLabel); formPanel.add(productIdField);
    
    formPanel.add(new JLabel()); formPanel.add(search);
    
    JLabel resultLabel = new JLabel("Quantity: ");
    resultLabel.setFont(new Font("MinionPro", Font.BOLD, 20));
    formPanel.add(new JLabel()); formPanel.add(resultLabel);
    
    //formPanel.add(new JLabel()); // Empty cell


    JLabel titleLabel = new JLabel(""); 
    titleLabel.setFont(new Font("MinionPro", Font.BOLD, 20));
    titleLabel.setForeground(Color.DARK_GRAY);

    TitledBorder titled = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            "",
            TitledBorder.LEFT,
            TitledBorder.TOP
    );

    JPanel titledPanel = new JPanel(new BorderLayout());
    titledPanel.setOpaque(false);
    //titledPanel.setPreferredSize(new Dimension(400,600));
    titledPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
    titledPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
    
    titledPanel.add(titleLabel, BorderLayout.NORTH);
    titledPanel.add(formPanel, BorderLayout.CENTER);

    JPanel wrapper = new JPanel(new BorderLayout(10, 10));
    wrapper.setOpaque(false);
    wrapper.setBorder(BorderFactory.createEmptyBorder(80, 80, 80, 80));
    wrapper.add(titledPanel, BorderLayout.NORTH);
    //wrapper.add(scroll, BorderLayout.CENTER);

    JPanel centerWrapper = new JPanel(new GridBagLayout());
    centerWrapper.setOpaque(false);
    centerWrapper.add(wrapper);

    contentPanel.add(centerWrapper, BorderLayout.CENTER);

    // Add fields to form

    // Title with icon
    // ImageIcon rawIcon = new ImageIcon(getClass().getResource("/refresh.png"));
    // Image scaledImage = rawIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
    // ImageIcon icon = new ImageIcon(scaledImage);    
    // JLabel titleLabel = new JLabel("", icon, JLabel.CENTER);
    // titleLabel.setFont(new Font("MinionPro", Font.BOLD, 20));
    // titleLabel.setForeground(Color.DARK_GRAY);
    // titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 30, 40, 30));


    // Action
    search.addActionListener(e -> {
        try {
                long productId = Long.parseLong(productIdField.getText());
                int storeId = Integer.parseInt(storeIdField.getText());

                Stock stock = StockService.getStock(productId, storeId);
                if (stock != null) {
                    resultLabel.setText("Quantity: " + stock.getStockQuantity());
                    showSuccess("Stock found.");
                } else {
                    resultLabel.setText("Quantity: N/A");
                    showError("Stock not found for Product ID " + productId + " in Store ID " + storeId + ".");
                }
            } catch (NumberFormatException ex) {
                showError("Invalid number format for ID.");
                resultLabel.setText("Quantity: N/A");
            } catch (Exception ex) {
                showError(ex.getMessage());
                resultLabel.setText("Quantity: N/A");
            }
    });
    contentPanel.revalidate();
    contentPanel.repaint();
    }

    private void menuAddStock() {

    contentPanel.removeAll();
    contentPanel.setLayout(new BorderLayout());

    JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false); 

        // This is the panel that will have the border and contain the icon and the form fields
        JPanel borderedContentPanel = new JPanel(new BorderLayout());
        borderedContentPanel.setOpaque(false);

        // Set the border for this panel
        TitledBorder titled = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY, 2),
            "", // No title text here, the icon will be visually above
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("MinionPro", Font.BOLD, 20),
            Color.DARK_GRAY
        );
        borderedContentPanel.setBorder(BorderFactory.createCompoundBorder(
            titled,
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

     JPanel fieldsAndButtonPanel = new JPanel(new BorderLayout());
        fieldsAndButtonPanel.setOpaque(false);
        fieldsAndButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // Padding below icon

        // Form layout
        JPanel formFieldsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formFieldsPanel.setOpaque(false);

        JTextField productField = new JTextField(15);
        productField.setFont(customFont);
        productField.setPreferredSize(new Dimension(200, 10));
        productField.setBorder(BorderFactory.createCompoundBorder(
            productField.getBorder(),
            BorderFactory.createEmptyBorder(1, 5, 1, 5)
        ));

        JTextField storeField = new JTextField(15);
        storeField.setFont(customFont);
        storeField.setPreferredSize(new Dimension(200, 10));
        storeField.setBorder(BorderFactory.createCompoundBorder(
            storeField.getBorder(),
            BorderFactory.createEmptyBorder(1, 5, 1, 5)
        ));

        JTextField quantityField = new JTextField(15);
        quantityField.setFont(customFont);
        quantityField.setPreferredSize(new Dimension(200, 10));
        quantityField.setBorder(BorderFactory.createCompoundBorder(
            quantityField.getBorder(),
            BorderFactory.createEmptyBorder(1, 5, 1, 5)
        ));

        // Add fields to formFieldsPanel
        JLabel label = new JLabel("Product ID:");
        label.setFont(new Font("MinionPro", Font.BOLD, 20));
        formFieldsPanel.add(label); formFieldsPanel.add(productField);

        JLabel lLabel = new JLabel("Store ID");
        lLabel.setFont(new Font("MinionPro", Font.BOLD, 20));
        formFieldsPanel.add(lLabel); formFieldsPanel.add(storeField);

        JLabel pLabel = new JLabel("Quantity to Add:");
        pLabel.setFont(new Font("MinionPro", Font.BOLD, 20));
        formFieldsPanel.add(pLabel); formFieldsPanel.add(quantityField);

        // Adds formFieldsPanel to the center of the fieldsAndButtonPanel
        fieldsAndButtonPanel.add(formFieldsPanel, BorderLayout.CENTER);

        // Submit button
        JButton add = new JButton("Add Stock");
        add.setBackground(new Color(128, 0, 128)); // Purple background
        add.setForeground(Color.WHITE);
        add.setFont(new Font("MinionPro", Font.BOLD, 20));
        add.setPreferredSize(new Dimension(200, 40));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonPanel.add(add);

        fieldsAndButtonPanel.add(buttonPanel, BorderLayout.SOUTH);
        borderedContentPanel.add(fieldsAndButtonPanel, BorderLayout.CENTER);

        centerWrapper.add(borderedContentPanel);
        contentPanel.add(centerWrapper, BorderLayout.CENTER);


    add.addActionListener(e -> {
        try {
             long productId = Long.parseLong(productField.getText());
                int storeId = Integer.parseInt(storeField.getText());
                int quantity = Integer.parseInt(quantityField.getText());

                StockService.updateStock(productId, storeId, quantity);
                showSuccess("Stock updated successfully.");
                productField.setText("");
                storeField.setText("");
                quantityField.setText("");
            } catch (NumberFormatException ex) {
                showError("Invalid number format for ID or Quantity.");
            } catch (Exception ex) {
                showError(ex.getMessage());  
            }
    });

    contentPanel.revalidate();
    contentPanel.repaint();
    }

    private void menuViewLowStock() {

        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        JPanel tableDisplayPanel = new JPanel(new BorderLayout());
        tableDisplayPanel.setOpaque(false);
        tableDisplayPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        model = new DefaultTableModel(new String[]{"Product ID", "Store ID", "Quantity"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Makes table non-editable
                }
            };
            table = new JTable(model);
            table.setFont(new Font("Arial", Font.PLAIN, 14));
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
            table.setRowHeight(25);

            JScrollPane scrollPane = new JScrollPane(table);
            tableDisplayPanel.add(scrollPane, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("Stores with low stock");
        titleLabel.setFont(new Font("MinionPro", Font.BOLD, 20));
        titleLabel.setForeground(Color.DARK_GRAY);

        TitledBorder titled = BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(Color.GRAY, 2),"",
        TitledBorder.LEFT,
        TitledBorder.TOP,
        new Font("MinionPro", Font.BOLD, 20),
        Color.DARK_GRAY
        );

        JPanel titledPanel = new JPanel(new BorderLayout());
        titledPanel.setOpaque(false);
        titledPanel.setBorder(BorderFactory.createCompoundBorder(
           titled,
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        titledPanel.add(titleLabel, BorderLayout.NORTH);

        // Overall panel to combine the titled panel and the table
        JPanel overallPanel = new JPanel(new BorderLayout());
        overallPanel.setOpaque(false);
        overallPanel.add(titledPanel, BorderLayout.NORTH);
        overallPanel.add(tableDisplayPanel, BorderLayout.CENTER);

        contentPanel.add(overallPanel, BorderLayout.CENTER); // Adds to the main content panel

        refreshLowStock(); // Populates table on display

        contentPanel.revalidate();
        contentPanel.repaint();
    }
     private void refreshLowStock() {
            model.setRowCount(0); // Clearing existing data
            List<Stock> lowStocks = StockService.getLowStockProducts();
            if (lowStocks.isEmpty()) {
                showError("No low stock products found.");
            } else {
                for (Stock stock : lowStocks) {
                    model.addRow(new Object[]{
                            stock.getProductId(), stock.getStoreId(), stock.getStockQuantity()
                    });
                }
            }
        }
    
    private void menuSearchOtherStores() {

        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
 
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setOpaque(false); // Keep it transparent
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setOpaque(false);
        //formPanel.setBorder(BorderFactory.createEmptyBorder(80, 100, 80, 100));
          
        prodField = new JTextField(10);
        excludedStField = new JTextField(10);
      
        JTextField[] fields = {prodField, excludedStField};
        for (JTextField field : fields) {
            field.setFont(customFont);
            field.setPreferredSize(new Dimension(100, 35));
            field.setBorder(BorderFactory.createCompoundBorder(
                field.getBorder(),
                BorderFactory.createEmptyBorder(0, 8, 0, 8)
        ));
        }

    JButton search = new JButton("Search");
    search.setBackground(new Color(128, 0, 128));
    search.setForeground(Color.WHITE);
    search.setFont(customFont);

    JLabel storeLabel = new JLabel("Excluded Store ID");
    storeLabel.setFont(new Font("MinionPro", Font.BOLD, 20));
    formPanel.add(storeLabel); formPanel.add(excludedStField);

    JLabel productLabel = new JLabel("Product ID");
    productLabel.setFont(new Font("MinionPro", Font.BOLD, 20));
    formPanel.add(productLabel); formPanel.add(prodField);
    
   JButton searchOtherButton = new JButton("Search");
    searchOtherButton.setBackground(new Color(128, 0, 128)); // Purple background
    searchOtherButton.setForeground(Color.WHITE);
    searchOtherButton.setFont(new Font("MinionPro", Font.BOLD, 20));
    searchOtherButton.setPreferredSize(new Dimension(200, 40));
    searchOtherButton.addActionListener(e -> { searchOtherStores();
    
    // Μόνο αν δεν έχει ήδη προστεθεί:
    if (searchTableScrollPane.getParent() == null) {
        mainContentPanel.add(searchTableScrollPane, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }
    
    });

    JPanel buttonPanel = new JPanel();
    buttonPanel.setOpaque(false);
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
    buttonPanel.add(searchOtherButton);

    formPanel.add(new JLabel()); formPanel.add(searchOtherButton);
    
    JPanel formWrapper = new JPanel();
    formWrapper.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

    formWrapper.setOpaque(false);
    formWrapper.setLayout(new FlowLayout(FlowLayout.CENTER));
    formWrapper.add(formPanel);

  // Table to display results
        model = new DefaultTableModel(new String[]{"Store ID", "Quantity"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Makes table non-editable
            }
        };
        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.setRowHeight(25);
        table.setBackground(Color.WHITE); // ή table.setOpaque(false);

        searchTableScrollPane  = new JScrollPane(table);
        searchTableScrollPane .setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 50));
        searchTableScrollPane .setOpaque(false);
        searchTableScrollPane .getViewport().setOpaque(false);  // για διαφάνεια

        // Adds formPanel and scrollPane to the main content panel
        mainContentPanel.add(formWrapper, BorderLayout.NORTH);
        //mainContentPanel.add(scrollPane, BorderLayout.CENTER);

        // Adds the mainContentPanel to the SearchOtherStoresPanel to the CENTER, as the superclass already added controlPanel to NORTH.
        contentPanel.add(mainContentPanel, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

        private void searchOtherStores() {

            model.setRowCount(0); // Clears previous results
            try {
                long productId = Long.parseLong(prodField.getText());
                int excludedStoreId = Integer.parseInt(excludedStField.getText());

                List<Stock> stocks = StockService.searchProductInOtherStores(productId, excludedStoreId);

                if (stocks.isEmpty()) {
                    showSuccess("No stock found in other stores for Product ID " + productId + ".");
                } else {
                    for (Stock s : stocks) {
                        model.addRow(new Object[]{s.getStoreId(), s.getStockQuantity()});
                    }
                }
            } catch (NumberFormatException ex) {
                showError("Invalid number format for ID.");
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        }



     private void menuGetJson() {
        
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

         JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
         inputPanel.setOpaque(false);
         inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel Lprod = new JLabel("Enter product ID:");
        Lprod.setFont(new Font("MinionPro", Font.BOLD, 20));

        JTextField Fprod = new JTextField(10);
       
            Fprod.setFont(customFont);
            Fprod.setPreferredSize(new Dimension(200, 30));
            Fprod.setBorder(BorderFactory.createCompoundBorder(
                Fprod.getBorder(),
                BorderFactory.createEmptyBorder(5, 10, 1, 10)
        ));
     
        inputPanel.add(Lprod);
        inputPanel.add(Fprod);

        JButton showJsonBtn = new JButton("Show JSON");
        showJsonBtn.setBackground(new Color(128, 0, 128));
        showJsonBtn.setForeground(Color.WHITE);
        showJsonBtn.setFont(customFont);

        JPanel buttonHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonHolder.setOpaque(false);
        buttonHolder.add(showJsonBtn);
 
        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setOpaque(false);
        topSection.add(inputPanel, BorderLayout.NORTH);
        topSection.add(buttonHolder, BorderLayout.CENTER);

//         // === DISPLAY PANEL ===
        JTextArea jsonDisplayArea = new JTextArea(10, 50);
        jsonDisplayArea.setEditable(false);
        jsonDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 20));
        // jsonDisplayArea.setLineWrap(true);
        // jsonDisplayArea.setWrapStyleWord(true);
         jsonDisplayArea.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
         jsonDisplayArea.setBackground(Color.WHITE);
        // jsonDisplayArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(jsonDisplayArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 50));
        scrollPane.setOpaque(false);

        // Box layout to stack vertically
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));
        centerPanel.add(topSection);
        centerPanel.add(scrollPane);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

//         // === CENTER WRAPPER FOR GRID ALIGNMENT ===
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(centerPanel);

        contentPanel.add(centerWrapper, BorderLayout.CENTER);

//         // === ACTION HANDLER ===
         showJsonBtn.addActionListener(e -> {
             try {
                if (Fprod.getText().trim().isEmpty()) {
                    showError("Please enter a Product ID to view JSON.");
                    jsonDisplayArea.setText("");
                    return;
                }
                long productId = Long.parseLong(Fprod.getText());
                String json = StockService.getStockAsJson(productId);
                if (json != null && !json.contains("Stock not found")) {
                    jsonDisplayArea.setText(json);
                } else {
                    jsonDisplayArea.setText("Stock not found for Product ID: " + productId);
                    showError("Stock not found for Product ID: " + productId);
                }
            } catch (NumberFormatException ex) {
                showError("Invalid Product ID.");
                jsonDisplayArea.setText("");
            } catch (Exception ex) {
                showError(ex.getMessage());
                jsonDisplayArea.setText("");
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }}

//      // Updates the table with current client list
//      private void refreshTable() {
//         clientTableModel.setRowCount(0); // clears existing data

//         // Fetches all clients
//         List <Client> clients = ClientService.getClientDAO().getAllClients();
//         for (Client client : clients) {
//             clientTableModel.addRow(new Object[]{
//                 client.getClientId(),
//                 client.getFirstName(),
//                 client.getLastName(),
//                 client.getEmail(),
//                 client.getPhoneNumber(),
//                 client.getGender(),
//                 client.getBirthDate(),                    
//                 client.isActiveStatus(),                    
//                 client.getDateJoined(),                    
//                 client.getLastPurchaseDate(),
//                 client.getClientSumTotal()
//             });
//         }
//     }
// }

