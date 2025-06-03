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
import javax.swing.table.DefaultTableModel;

public class ProductFrame extends JFrame {
    private JPanel backgroundPanel;
    private JPanel currentContentPanel; // Holds the active sub-panel
   // private static ProductService productService;
    private String path;

    private JPanel cardPanel;  // the center area with switchable views
    private CardLayout cardLayout;

    private JTextField descField, priceField, costField, idField;
    private JComboBox<String> categoryBox;
 
    public ProductFrame() {
        super();
        setTitle("Products Menu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Closes the frame
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
//-----------------------------------------------------------------------------------------
        //=== BACKGROUND PANEL===
        backgroundPanel = new JPanel() ;
        backgroundPanel.setBackground(Color.WHITE);
       // backgroundPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        backgroundPanel.setLayout(new BorderLayout());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
       // bottomPanel.add(backButton);
       // background.add(bottomPanel, BorderLayout.SOUTH);
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);
        cardPanel.add(createProductForm(false),"create");
        //cardPanel.add(createProductForm(true), "update");
        //cardPanel.add(createProductPanel(), "create");
        cardPanel.add(updateProductPanel(), "update");

        //=== TOP PANEL WITH Web-style top navigation bar
        JPanel topBar = new JPanel();
        topBar.setLayout(new BoxLayout(topBar, BoxLayout.X_AXIS));
        topBar.setPreferredSize(new Dimension(200,80));
        topBar.setBackground(new Color(239, 247, 255));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
//--------------------------------------------------------------------------------------------
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
        //===PRPODUCT ICON TOP LEFT CORNER====
        // path = "/retaillogo.png";
        // ImageIcon icon = MainMenu.getIcon(path);
        // JLabel logo = new JLabel(icon);
        // logo.setPreferredSize(new Dimension(200,200));
        // logo.setBorder(BorderFactory.createEmptyBorder(-30, 0, 0, 0));

        topBar.add(logoLabel);
        topBar.add(Box.createHorizontalStrut(30)); // space before nav items
        topBar.add(Box.createHorizontalGlue());
        
        // Navigation items
        String[] navItems  = {
            "Create", "Update", "Delete", "Activate/Deactivate","View all", "Export JSON"};
                 
        Font navFont = new Font("Arial", Font.PLAIN, 20);

        for (String item : navItems) {
            JButton navButton = new JButton(item);
            navButton.setFont(navFont);
            navButton.setFocusPainted(false);
            navButton.setForeground(new Color(0,0,205));
            navButton.setBackground(Color.WHITE);
            navButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            navButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            navButton.setOpaque(false);
            navButton.setBorderPainted(false);
            navButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            navButton.setAlignmentY(Component.CENTER_ALIGNMENT);
            navButton.setMaximumSize(new Dimension(250, 30));
            
            // Adds spacing between buttons
            topBar.add(navButton);
            topBar.add(Box.createHorizontalStrut(10));

            // Adds action listeners based on button text
            switch (item) {
               case "Create" -> navButton.addActionListener(e -> cardLayout.show(cardPanel, "create"));//new CreateProductPanel()));
                // case "Show" -> navButton.addActionListener(e -> menuShowClient());
                case "Update" -> navButton.addActionListener(e -> cardLayout.show(cardPanel, "update"));
                // case "Delete" -> navButton.addActionListener(e -> menuDeleteClient());
                // case "Delete Inactive" -> navButton.addActionListener(e -> menuDeleteInactiveClients());
                // case "Export JSON" -> navButton.addActionListener(e -> menuGetJson());
            }
        }
               
        JButton backButton = new JButton();
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("left-arrow.png"));
        ImageIcon scaledIcon = null;

        if (originalIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            Image image = originalIcon.getImage();
            Image newImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Scale to fit 40x30 button (e.g., 30x20 with padding)
            scaledIcon = new ImageIcon(newImage);
            backButton.setIcon(scaledIcon);
        } else {
            System.err.println("Warning: Could not load left-arrow.png, or it's not a valid image.");
            backButton.setText("Back"); // Fallback
        }

        backButton.setPreferredSize(new Dimension(50, 50)); // Keep your desired button size
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false); // Set to false for transparency
        backButton.setContentAreaFilled(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> dispose());
        
        bottomPanel.add(backButton);
       // bottomPanel.add(Box.createHorizontalGlue());
              
        backgroundPanel.add(cardPanel, BorderLayout.CENTER); 
        backgroundPanel.add(topBar,BorderLayout.NORTH);
        backgroundPanel.add(bottomPanel,BorderLayout.SOUTH);

        // Initial content will be the main product menu buttons
       //showProductMainMenu();
        setContentPane(backgroundPanel);
        setVisible(true);
    }

    Font labelFont = new Font("Arial", Font.BOLD, 16);
    Font fieldFont = new Font("Arial", Font.PLAIN, 16);

    private JPanel createProductForm (boolean isUpdate) {

       // ImageIcon icon = new ImageIcon(getClass().getResource("/fadelogo.png")); // adjust path
       // Image bgImage = icon.getImage();
       // BackgroundPanel formPanel = new BackgroundPanel(bgImage);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setLayout(new GridBagLayout());
        formPanel.setOpaque(false);
        //formPanel.setBorder(BorderFactory.createEmptyBorder(50, 200, 0, 200));
        //formPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // spacing between components
        gbc.anchor = GridBagConstraints.WEST; // left-align labels
        gbc.fill = GridBagConstraints.HORIZONTAL; // fields stretch
        
        descField = new JTextField(20);
        categoryBox = new JComboBox<>(new String[]{"clothing", "beauty", "electronics", "home goods", "kitchen appliances"});
        categoryBox.setBackground(Color.WHITE);;
        priceField = new JTextField(10);
        costField = new JTextField(10);

        JLabel descLabel = new JLabel("Description:");
        JLabel catLabel = new JLabel("Category:");
        JLabel priceLabel = new JLabel("Price:");
        JLabel costLabel = new JLabel("Cost:");

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(descLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(descField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(catLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(categoryBox, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(priceLabel, gbc);
        
        gbc.gridx = 1;
        formPanel.add(priceField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(costLabel, gbc);
        
        gbc.gridx = 1;
        formPanel.add(costField, gbc);
        
        descLabel.setFont(labelFont);
        catLabel.setFont(labelFont);
        priceLabel.setFont(labelFont);
        costLabel.setFont(labelFont);

        descField.setFont(fieldFont);
        categoryBox.setFont(fieldFont);
        priceField.setFont(fieldFont);
        costField.setFont(fieldFont);

        JTextField[] fields = { descField, priceField, costField };
        for (JTextField field : fields) {
            field.setBorder(BorderFactory.createLineBorder(Color.GRAY,1));
        }

        JButton actionBtn = new JButton(isUpdate ? "Update Product" : "Create Product");
        actionBtn.setBackground(new Color(128, 0, 128)); // Purple background
        actionBtn.setForeground(Color.WHITE);
        actionBtn.setFont(new Font("MinionPro", Font.BOLD, 20));
        actionBtn.setPreferredSize(new Dimension(200, 30));
        actionBtn.addActionListener(e -> createProduct());

        if (isUpdate) {
            actionBtn.addActionListener(e -> updateProduct());
        } else {
            actionBtn.addActionListener(e -> createProduct());
        }

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;  
        formPanel.add(actionBtn, gbc);
        
        return formPanel;
    }

    private JPanel updateProductPanel () {

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));

        wrapper.setOpaque(false);

        JPanel idPanel = new JPanel();
        idPanel.setLayout(new BoxLayout(idPanel, BoxLayout.Y_AXIS));
        idPanel.setPreferredSize(new Dimension(200,100));
        idField = new JTextField(20); 
        idField.setFont(fieldFont);
        idField.setBorder(BorderFactory.createLineBorder(Color.GRAY,1));
        JLabel idLabel = new JLabel("Product ID to Update:");
        idLabel.setFont(labelFont);
        
        idPanel.add(idLabel);
        idPanel.add(idField);

        JPanel formPanel = createProductForm(true);
        formPanel.setMaximumSize(new Dimension(600, 500));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        wrapper.add(Box.createVerticalStrut(40));   // απόσταση από πάνω
        wrapper.add(idPanel);
        wrapper.add(Box.createVerticalStrut(20));   // απόσταση από το idPanel
        wrapper.add(formPanel);
        wrapper.add(Box.createVerticalGlue()); 
       
        wrapper.setVisible(true);

        return wrapper;
    }

   
    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, "Error: " + message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }    

    private void createProduct() {
        try {
            ProductService.createProduct(
            descField.getText(),
            categoryBox.getSelectedItem().toString().toLowerCase(),
            Double.parseDouble(priceField.getText()),
            Double.parseDouble(costField.getText()));
        
            showSuccess("Product created with ID: "); //+ p.getProductId());
            descField.setText("");
            priceField.setText("");
            costField.setText("");
            categoryBox.setSelectedIndex(0);
            }
        catch (NumberFormatException ex) {
            showError("Invalid number format for Price or Cost.");
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void updateProduct() {
        try {
            if (idField.getText().trim().isEmpty()) {
                showError("Please enter a Product ID to update.");
                return;
            }
            long id = Long.parseLong(idField.getText());

            Product product = ProductService.findProductById(id); // Fetching product using id
            if (product == null) {
                showError("Product not found with ID: " +id);
                return;
            }

            String descInput = descField.getText().trim();
            String categoryInput = ((String)categoryBox.getSelectedItem()).trim().toLowerCase();
            String priceTextInput = priceField.getText().trim();
            String costTextInput = costField.getText().trim();

            // If the input field is empty, use the existing product's value.
            // Otherwise, parse and use the new input.
            String newDesc = descInput.isEmpty() ? product.getDescription() : descInput;
            String newCategory = categoryInput.isEmpty() ? product.getCategory() : categoryInput;

            double newPrice = priceTextInput.isEmpty() ? product.getPrice() : Double.parseDouble(priceTextInput);
            double newCost = costTextInput.isEmpty() ? product.getCost() : Double.parseDouble(costTextInput);

            ProductService.updateProduct(id, newDesc, newCategory, newPrice, newCost);
            showSuccess("Product ID " + id + " updated successfully.");
            idField.setText("");
            descField.setText("");
            priceField.setText("");
            costField.setText("");
            categoryBox.setSelectedIndex(0);
        } catch (NumberFormatException ex) {
            showError("Invalid number format for Product ID, Price or Cost.");
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }
}
    