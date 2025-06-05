import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.awt.Insets;
import java.util.List;

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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.mysql.cj.xdevapi.Client;

public class ProductFrame extends JFrame {
    private final JPanel contentPanel;
    Font customFont = new Font("MinionPro", Font.PLAIN, 20);
    // Table model and table for "View All Clients" to be accessible for refresh
    private DefaultTableModel productTableModel;
    private JTable productTable;
    // private JTextField inputField;
    // private JPanel wrapper;

 
    public ProductFrame() {
        super();
        setTitle("Products Menu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Closes the frame
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        
        setResizable(true);
        setMinimumSize(new Dimension(800, 600));

//-----------------------------------------------------------------------------------------
        //=== BACKGROUND PANEL===
        JPanel background = new JPanel();
        background.setOpaque(false);
        this.add(background);
        getContentPane().setBackground(Color.WHITE);

        productTableModel = new DefaultTableModel(new String[]{
            "ID", "Description", "Category", "Price", "Cost" ,"Active status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Makes table non-editable
            }
        };
        productTable = new JTable(productTableModel);
        productTable.setFont(new Font("MinionPro", Font.PLAIN, 15));
        productTable.getTableHeader().setFont(new Font("MinionPro", Font.BOLD, 17));
        productTable.setRowHeight(30);

         // Top navigation bar
        JPanel topBar = new JPanel();
        topBar.setLayout(new BoxLayout(topBar, BoxLayout.X_AXIS));
        topBar.setBackground(new Color(239, 247, 255));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Navigation items
        String[] navItems  = {
            "Create", "Update", "Delete", "Activate/Deactivate", "View All", "Export JSON"};
        
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
            logoLabel.setText("ProductMenu");
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
                case "Create" -> navButton.addActionListener(e -> menuCreateProduct());
                case "Update" -> navButton.addActionListener(e -> menuUpdateProduct());
                case "Delete" -> navButton.addActionListener(e -> menuDeleteProduct());
                case "Activate/Deactivate" -> navButton.addActionListener(e -> menuActivateProduct());
                case "View All" -> navButton.addActionListener(e -> menuViewAllProducts());
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

        menuCreateProduct(); // Shows create form by default
        setVisible(true); // Shows window
    }

    protected void showError(String message) {
       JOptionPane.showMessageDialog(this, "Error: " + message, "Error", JOptionPane.ERROR_MESSAGE);
     }

     protected void showSuccess(String message) {
         JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }  

    private void menuCreateProduct() {
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
            addIcon = new ImageIcon(getClass().getResource("/setproduct.png"));

            if (addIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                Image image = addIcon.getImage();
                Image scaledImage = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                addIcon = new ImageIcon(scaledImage);
            } else {
                System.err.println("Warning: Could not load add.png, or it's not a valid image.");
                // Fallback to text if icon fails to load
                iconLabel.setText("Add New Product");
                iconLabel.setFont(new Font("MinionPro", Font.BOLD, 22));
                iconLabel.setForeground(Color.DARK_GRAY);
            }
        } catch (Exception e) {
            System.err.println("Error loading add.png: " + e.getMessage());
            // Fallback to text if an exception occurs
            iconLabel.setText("Add New Product");
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

        JTextField description = new JTextField(15);
        description.setFont(customFont);
        description.setPreferredSize(new Dimension(200, 10));
        description.setBorder(BorderFactory.createCompoundBorder(
            description.getBorder(),
            BorderFactory.createEmptyBorder(1, 5, 1, 5)
        ));

        String[] categoryOptions = { "clothing", "beauty", "electronics"};
        JComboBox<String> categoryBox = new JComboBox<>(categoryOptions);
        categoryBox.setFont(customFont);
        categoryBox.setPreferredSize(new Dimension(200, 20));
        categoryBox.setBorder(BorderFactory.createCompoundBorder(
            categoryBox.getBorder(),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));


        JTextField price = new JTextField(15);
        price.setFont(customFont);
        price.setPreferredSize(new Dimension(200, 10));
        price.setBorder(BorderFactory.createCompoundBorder(
            price.getBorder(),
            BorderFactory.createEmptyBorder(1, 5, 1, 5)
        ));

        JTextField cost = new JTextField(15);
        cost.setFont(customFont);
        cost.setPreferredSize(new Dimension(200, 10));
        cost.setBorder(BorderFactory.createCompoundBorder(
            cost.getBorder(),
            BorderFactory.createEmptyBorder(1, 5, 1, 5)
        ));

        
        // Add fields to formFieldsPanel
        JLabel label = new JLabel("Description:");
        label.setFont(new Font("MinionPro", Font.BOLD, 20));
        formFieldsPanel.add(label); formFieldsPanel.add(description);

        JLabel lLabel = new JLabel("Category:");
        lLabel.setFont(new Font("MinionPro", Font.BOLD, 20));
        formFieldsPanel.add(lLabel); formFieldsPanel.add(categoryBox);

        JLabel pLabel = new JLabel("Price:");
        pLabel.setFont(new Font("MinionPro", Font.BOLD, 20));
        formFieldsPanel.add(pLabel); formFieldsPanel.add(price);

        JLabel eLabel = new JLabel("Cost:");
        eLabel.setFont(new Font("MinionPro", Font.BOLD, 20));
        formFieldsPanel.add(eLabel); formFieldsPanel.add(cost);

        // Adds formFieldsPanel to the center of the fieldsAndButtonPanel
        fieldsAndButtonPanel.add(formFieldsPanel, BorderLayout.CENTER);

        // Submit button
        JButton createBtn = new JButton("Create");
        createBtn.setBackground(new Color(128, 0, 128)); // Purple background
        createBtn.setForeground(Color.WHITE);
        createBtn.setFont(new Font("MinionPro", Font.BOLD, 20));
        createBtn.setPreferredSize(new Dimension(200, 40));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonPanel.add(createBtn);

        fieldsAndButtonPanel.add(buttonPanel, BorderLayout.SOUTH);
        borderedContentPanel.add(fieldsAndButtonPanel, BorderLayout.CENTER);

        centerWrapper.add(borderedContentPanel);
        contentPanel.add(centerWrapper, BorderLayout.CENTER);

              // Create client on submit
        createBtn.addActionListener(e -> {
            try {
                    String pdesc = description.getText().trim();
                    double pprice = Double.parseDouble(price.getText().trim().replace(",", "."));
                    double pcost = Double.parseDouble(cost.getText().trim().replace(",", "."));
                    String pcategory = categoryBox.getSelectedItem().toString().toLowerCase();

                    Product p = ProductService.createProduct(pdesc, pcategory, pprice, pcost);
                    showSuccess("Product created with ID: "+ p.getProductId());
            
                    refreshTable(); // Update table
                    //JOptionPane.showMessageDialog(contentPanel, "Client created with ID: " + client.getClientId());

                    // Clear fields after successful submission
                    description.setText("");
                    price.setText("");
                    cost.setText("");
                    categoryBox.setSelectedIndex(0); // Reset gender to first option
            } catch (Exception ex) {
                showError(ex.getMessage());
                //JOptionPane.showMessageDialog(contentPanel, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace(); // Print stack trace for debugging
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }


    private void menuUpdateProduct() {
        contentPanel.removeAll();

        String input = JOptionPane.showInputDialog(this, "Enter product ID:");
        Product product = null;
        for (int i = 0; i < ProductService.getAllProducts().size(); i++) {
            Product p = ProductService.getAllProducts().get(i);
            if (input == null || input.trim().isEmpty()) {
                JOptionPane.showMessageDialog(contentPanel, "Please enter a product ID");
            }
            if (Long.parseLong(input) == p.getProductId()) {
                product = p;
                break;
            }
        }

        if (product == null) {
            JOptionPane.showMessageDialog(this, "Product not found.");
            return;
        }

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setOpaque(false);

        JTextField newDesc = new JTextField(product.getDescription(), 15);
        JTextField newCat = new JTextField(product.getCategory(), 15);
        JTextField newPrice = new JTextField(String.valueOf(product.getPrice()));
        JTextField newCost = new JTextField(String.valueOf(product.getCost()));

        double newprice = Double.parseDouble(newPrice.getText().trim());
        double newcost = Double.parseDouble(newCost.getText().trim());

        JTextField[] fields = {newDesc, newCat, newPrice, newCost};
        for (JTextField field : fields) {
            field.setFont(customFont);
            field.setPreferredSize(new Dimension(200, 35));
            field.setBorder(BorderFactory.createCompoundBorder(
                field.getBorder(),
                BorderFactory.createEmptyBorder(0, 8, 0, 8)
        ));
    }

    // Add fields to form
    JLabel dLabel = new JLabel("Description:");
    dLabel.setFont(new Font("MinionPro", Font.BOLD, 20));
    form.add(dLabel); form.add(newDesc);

    JLabel cLabel = new JLabel("Category:");
    cLabel.setFont(new Font("MinionPro", Font.BOLD, 20));
    form.add(cLabel); form.add(newCat);

    JLabel pLabel = new JLabel("Price:");
    pLabel.setFont(new Font("MinionPro", Font.BOLD, 20));
    form.add(pLabel); form.add(newPrice);

    JLabel coLabel = new JLabel("Cost:");
    coLabel.setFont(new Font("MinionPro", Font.BOLD, 20));
    form.add(coLabel); form.add(newCost);

    // Title with icon
    ImageIcon rawIcon = new ImageIcon(getClass().getResource("/refresh.png"));
    Image scaledImage = rawIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
    ImageIcon icon = new ImageIcon(scaledImage);    
    JLabel titleLabel = new JLabel("", icon, JLabel.CENTER);
    titleLabel.setFont(new Font("MinionPro", Font.BOLD, 20));
    titleLabel.setForeground(Color.DARK_GRAY);
    titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 30, 40, 30));

    // Custom titled border
    TitledBorder titled = BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(Color.GRAY, 1),
        "",
        TitledBorder.LEFT,
        TitledBorder.TOP
    );

    // Wrap title in JPanel
    JPanel titledPanel = new JPanel(new BorderLayout());
    titledPanel.setOpaque(false);
    titledPanel.setBorder(BorderFactory.createCompoundBorder(
        titled,
        BorderFactory.createEmptyBorder(5, 30, 30, 30)
    ));
    titledPanel.add(titleLabel, BorderLayout.NORTH);
    titledPanel.add(form, BorderLayout.CENTER);

    // Submit button
    JButton submit = new JButton("Update");
    submit.setBackground(new Color(128, 0, 128)); // Purple
    submit.setForeground(Color.WHITE);
    submit.setFont(new Font("MinionPro", Font.BOLD, 20));
    submit.setPreferredSize(new Dimension(200, 40));
    JPanel buttonPanel = new JPanel();
    buttonPanel.setOpaque(false);
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
    buttonPanel.add(submit);

    // Wrapper layout
    JPanel wrapper = new JPanel(new BorderLayout(10, 10));
    wrapper.setOpaque(false);
    wrapper.setBorder(BorderFactory.createEmptyBorder(80, 80, 80, 80));
    wrapper.add(titledPanel, BorderLayout.CENTER);
    wrapper.add(buttonPanel, BorderLayout.SOUTH);
    wrapper.setMaximumSize(new Dimension(1000, 700));

    JPanel centerWrapper = new JPanel(new GridBagLayout());
    centerWrapper.setOpaque(false);
    centerWrapper.add(wrapper);

    contentPanel.setLayout(new BorderLayout());
    contentPanel.add(centerWrapper, BorderLayout.CENTER);

     // Action
    Product finalProduct = product;
    submit.addActionListener(e -> {
        try {

            ProductService.updateProduct(
                finalProduct.getProductId(),
                newDesc.getText(),
                newCat.getText(),
                newprice,
                newcost
            );
            refreshTable();
            JOptionPane.showMessageDialog(this, "Product updated.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Update Failed", JOptionPane.ERROR_MESSAGE);
        }
    });

    contentPanel.revalidate();
    contentPanel.repaint();

    }

private void menuDeleteProduct() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        JPanel form = new JPanel(new BorderLayout());
        form.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20),
            "",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("MinionPro", Font.BOLD, 20),
            Color.DARK_GRAY
        ));
        form.setBackground(Color.WHITE);

        JTextField inputField = new JTextField(20);
        inputField.setFont(customFont);
        inputField.setPreferredSize(new Dimension(200, 30));
        inputField.setBorder(BorderFactory.createCompoundBorder(
        inputField.getBorder(),
        BorderFactory.createEmptyBorder(1, 10, 1, 10)));

        JButton delete = new JButton("Delete");
        delete.setBackground(new Color(128, 0, 128));
        delete.setForeground(Color.WHITE);
        delete.setFont(new Font("MinionPro", Font.BOLD, 20));
        delete.setPreferredSize(new Dimension(200, 40));

        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel enterTextLabel = new JLabel("Enter Product ID:");
        enterTextLabel.setFont(new Font("MinionPro", Font.BOLD, 20));
        topPanel.add(enterTextLabel);        
        topPanel.add(inputField);
        topPanel.add(delete);
        topPanel.setBackground(Color.WHITE);

        form.add(topPanel, BorderLayout.NORTH);

        delete.addActionListener(e -> {
            String input = inputField.getText();
            Product product = ProductService.getAllProducts().stream()
                .filter(p -> Long.parseLong(input) == p.getProductId())
                .findFirst()
                .orElse(null);

            if (product == null) {
                JOptionPane.showMessageDialog(this, "Product not found.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Delete product: " + product.getProductId() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                ProductService.deleteProduct(product.getProductId());
                refreshTable();
                JOptionPane.showMessageDialog(this, "Product deleted.");
            }
        });
        contentPanel.add(form, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

private void menuActivateProduct() {

        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        JPanel form = new JPanel(new BorderLayout());
        form.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20),
            "",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("MinionPro", Font.BOLD, 20),
            Color.DARK_GRAY
        ));
        form.setBackground(Color.WHITE);

        JTextField inputField = new JTextField(20);
        inputField.setFont(customFont);
        inputField.setPreferredSize(new Dimension(200, 30));
        inputField.setBorder(BorderFactory.createCompoundBorder(
        inputField.getBorder(),
        BorderFactory.createEmptyBorder(1, 10, 1, 10)));

        JButton activate = new JButton("Activate/Deactivate");
        activate.setBackground(new Color(128, 0, 128));
        activate.setForeground(Color.WHITE);
        activate.setFont(new Font("MinionPro", Font.BOLD, 20));
        activate.setPreferredSize(new Dimension(200, 40));

        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel enterTextLabel = new JLabel("Enter Product ID:");
        enterTextLabel.setFont(new Font("MinionPro", Font.BOLD, 20));
        topPanel.add(enterTextLabel);        
        topPanel.add(inputField);
        topPanel.add(activate);
        topPanel.setBackground(Color.WHITE);

        form.add(topPanel, BorderLayout.NORTH);

        activate.addActionListener(e -> {
            long id = Long.parseLong(inputField.getText().trim());
            Product product = ProductService.findProductById(id);

            if (product == null) {
                showError("Product not found.");
                return;
            }

            Boolean status = product.getActive();

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to change product's status ?",
                    "Confirm", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                ProductService.setProductActiveStatus(id,!status);
                showSuccess("Product's status with ID " + id + " was setted successfully.");
            }});

                // String input = inputField.getText();
            // Product product = ProductService.getAllProducts().stream()
            //     .filter(p -> Long.parseLong(input) == p.getProductId())
            //     .findFirst()
            //     .orElse(null);

            // if (product == null) {
            //     JOptionPane.showMessageDialog(this, "Product not found.");
            //     return;
            // }

            // int confirm = JOptionPane.showConfirmDialog(this, "Activate/Deactivate product: " + product.getProductId() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            // if (confirm == JOptionPane.YES_OPTION) {
            //     ProductService.setProductActiveStatus(product.getProductId());
            //     refreshTable();
            //     JOptionPane.showMessageDialog(this, "Product status changed.");
            // }
        contentPanel.add(form, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
    }


 private void menuViewAllProducts() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        JPanel tableDisplayPanel = new JPanel(new BorderLayout());
        tableDisplayPanel.setOpaque(false);
        tableDisplayPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JScrollPane scrollPane = new JScrollPane(productTable);
        tableDisplayPanel.add(scrollPane, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("All Products");
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

        refreshTable(); // Populates table on display

        contentPanel.revalidate();
        contentPanel.repaint();
    }

   private void menuGetJson() {

        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.setOpaque(false);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel enterTextLabel = new JLabel("Enter product ID:");
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

        inputPanel.add(enterTextLabel);
        inputPanel.add(idField);
        inputPanel.add(showJsonBtn);

        // === DISPLAY PANEL ===
        JTextArea jsonDisplayArea = new JTextArea(10, 50);
        jsonDisplayArea.setEditable(false);
        jsonDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 20));
        jsonDisplayArea.setLineWrap(true);
        jsonDisplayArea.setWrapStyleWord(true);
        jsonDisplayArea.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        jsonDisplayArea.setBackground(Color.WHITE);
        jsonDisplayArea.setMargin(new Insets(10, 10, 10, 10));

        // Box layout to stack vertically
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));
        centerPanel.add(inputPanel);
        centerPanel.add(jsonDisplayArea);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // === CENTER WRAPPER FOR GRID ALIGNMENT ===
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(centerPanel);

        contentPanel.add(centerWrapper, BorderLayout.CENTER);

        // === ACTION HANDLER ===
        showJsonBtn.addActionListener(e -> {
            try {
                String input = idField.getText().trim();
                if (input.isEmpty()) {
                    jsonDisplayArea.setText("Please enter a product ID.");
                    return;
                }
                if (!input.matches("\\d+")) {
                    jsonDisplayArea.setText("Product ID must be a number.");
                    return;
                }

                long id = Long.parseLong(input);
                //ClientService clientService = new ClientService();
                String json = ProductService.getProductAsJson(id);

                if (json != null && !json.contains("Product not found")) {
                    jsonDisplayArea.setText(json);
                } else {
                    jsonDisplayArea.setText("Product not found with ID: " + id);
                }
            } catch (Exception ex) {
                jsonDisplayArea.setText("Error: " + ex.getMessage());
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }


 private void refreshTable() {
        productTableModel.setRowCount(0); // clears existing data

        // Fetches all clients
        List <Product> products = ProductService.getAllProducts();
        for (Product product : products) {
            productTableModel.addRow(new Object[]{
                product.getProductId(),
                product.getDescription(),
                product.getCategory(),
                product.getPrice(),
                product.getCost(),
                product.getActive(),
            });
        }
    }



}
    //form.add(search);





