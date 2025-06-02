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
import java.awt.MediaTracker;
import java.util.List;

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

public class StoreFrame extends JFrame {
    private final JPanel contentPanel; 
    private final StoreService storeService;
    Font customFont = new Font("MinionPro", Font.PLAIN, 25);

    public StoreFrame() {
        setTitle("Store Menu"); // Changed title for consistency
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Closes only this window

        // Background with image - set up similarly to ClientFrame
        JPanel background = new JPanel();
        background.setOpaque(false);
        this.add(background);
        getContentPane().setBackground(Color.WHITE); // Set background color

        // Web-style top navigation bar
        JPanel topBar = new JPanel();
        topBar.setLayout(new BoxLayout(topBar, BoxLayout.X_AXIS));
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Navigation items
        String[] navItems = {"Create", "Update", "Activate/Deactivate", "Show Store", "View All", "Export JSON"};

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
            logoLabel.setText("StoreMenu");
            logoLabel.setFont(new Font("MinionPro", Font.BOLD, 25));
            logoLabel.setForeground(Color.BLACK);
        }

        if (logoIcon != null) {
        logoLabel.setIcon(logoIcon);
        // If you want text AND icon: logoLabel.setText("ClientMenu");
        // If you only want the icon, you don't need font/foreground for text unless it's a fallback
        }
        topBar.add(logoLabel);
        topBar.add(Box.createHorizontalStrut(30)); // space before nav items

        topBar.add(Box.createHorizontalGlue()); // Pushes elements to the right

        Font navFont = new Font("MinionPro", Font.PLAIN, 24);

        for (String item : navItems) {
            JButton navButton = new JButton(item);
            navButton.setFont(navFont);
            navButton.setFocusPainted(false);
            navButton.setForeground(Color.BLACK);
            navButton.setBackground(Color.WHITE);
            navButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            navButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            navButton.setOpaque(true);
            navButton.setBorderPainted(false);
            navButton.setAlignmentY(Component.CENTER_ALIGNMENT);
            navButton.setMaximumSize(new Dimension(250, 30));

            // Adds spacing between buttons
            topBar.add(navButton);
            topBar.add(Box.createHorizontalStrut(10));

            // Adds action listeners based on button text
            switch (item) {
                case "Create" -> navButton.addActionListener(e -> menuCreateStore());
                case "Update" -> navButton.addActionListener(e -> menuUpdateStore());
                case "Activate/Deactivate" -> navButton.addActionListener(e -> menuToggleStoreStatus());
                case "Show Store" -> navButton.addActionListener(e -> menuShowSpecificStore());
                case "View All" -> navButton.addActionListener(e -> menuViewAllStores());
                case "Export JSON" -> navButton.addActionListener(e -> menuGetStoreAsJson());
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

        backButton.setPreferredSize(new Dimension(50, 30));
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> dispose());

        // Add to the left or right side of the top bar
        topBar.add(Box.createHorizontalStrut(10));
        topBar.add(backButton);

        background.setLayout(new BorderLayout());
        background.add(topBar, BorderLayout.NORTH);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        background.add(contentPanel, BorderLayout.CENTER);

        storeService = new StoreService(); // Initialize StoreService

        menuCreateStore(); // Shows create form by default
        setVisible(true); // Show window
    }

   private void menuCreateStore() {
    contentPanel.removeAll(); // Clear previous content
    contentPanel.setLayout(new BorderLayout());

    // Main panel to hold the form, centered on the screen
    JPanel centerWrapper = new JPanel(new GridBagLayout());
    centerWrapper.setOpaque(false); // Make it transparent to show contentPanel's background

    // This is the panel that will have the border and contain the icon and the form fields
    JPanel borderedContentPanel = new JPanel(new BorderLayout()); // Use BorderLayout for this container
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
        BorderFactory.createEmptyBorder(30, 30, 30, 30) // Inner padding
    ));

    // --- ADD ICON HERE ---
    JLabel iconLabel = new JLabel();
    ImageIcon addIcon = null;

    try {
        // Option 1: Load from classpath (recommended for bundled images)
        addIcon = new ImageIcon(getClass().getResource("/add-store.png"));

        // If the icon is too large or small, scale it
        if (addIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            Image image = addIcon.getImage();
            Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Adjust size as needed
            addIcon = new ImageIcon(scaledImage);
        } else {
            System.err.println("Warning: Could not load add.png, or it's not a valid image.");
            // Fallback to text if icon fails to load
            iconLabel.setText("Add New Store");
            iconLabel.setFont(new Font("MinionPro", Font.BOLD, 22));
            iconLabel.setForeground(Color.DARK_GRAY);
        }
    } catch (Exception e) {
        System.err.println("Error loading add.png: " + e.getMessage());
        // Fallback to text if an exception occurs
        iconLabel.setText("Add New Store");
        iconLabel.setFont(new Font("MinionPro", Font.BOLD, 22));
        iconLabel.setForeground(Color.DARK_GRAY);
    }

    if (addIcon != null) {
        iconLabel.setIcon(addIcon);
    }

    iconLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the icon
    borderedContentPanel.add(iconLabel, BorderLayout.NORTH); // Add icon at the top of the bordered panel

    // Create a panel to hold the form fields and button
    JPanel fieldsAndButtonPanel = new JPanel(new BorderLayout());
    fieldsAndButtonPanel.setOpaque(false);
    fieldsAndButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // Padding below icon

    // Form layout (your existing GridLayout for fields)
    JPanel formFieldsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
    formFieldsPanel.setOpaque(false);

    JTextField nameField = new JTextField(15);
    nameField.setFont(customFont);
    nameField.setPreferredSize(new Dimension(200, 30));
    nameField.setBorder(BorderFactory.createCompoundBorder(
        nameField.getBorder(),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));

    JTextField addressField = new JTextField(15);
    addressField.setFont(customFont);
    addressField.setPreferredSize(new Dimension(200, 30));
    addressField.setBorder(BorderFactory.createCompoundBorder(
        addressField.getBorder(),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));

    JTextField cityField = new JTextField(15);
    cityField.setFont(customFont);
    cityField.setPreferredSize(new Dimension(200, 30));
    cityField.setBorder(BorderFactory.createCompoundBorder(
        cityField.getBorder(),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));

    JTextField countryField = new JTextField(15);
    countryField.setFont(customFont);
    countryField.setPreferredSize(new Dimension(200, 30));
    countryField.setBorder(BorderFactory.createCompoundBorder(
        countryField.getBorder(),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));

    JTextField phoneField = new JTextField(15);
    phoneField.setFont(customFont);
    phoneField.setPreferredSize(new Dimension(200, 30));
    phoneField.setBorder(BorderFactory.createCompoundBorder(
        phoneField.getBorder(),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));

    JLabel nameLabel = new JLabel("Name:");
    nameLabel.setFont(customFont);
    formFieldsPanel.add(nameLabel); formFieldsPanel.add(nameField);

    JLabel addressLabel = new JLabel("Address:");
    addressLabel.setFont(customFont);
    formFieldsPanel.add(addressLabel); formFieldsPanel.add(addressField);

    JLabel cityLabel = new JLabel("City:");
    cityLabel.setFont(customFont);
    formFieldsPanel.add(cityLabel); formFieldsPanel.add(cityField);

    JLabel countryLabel = new JLabel("Country:");
    countryLabel.setFont(customFont);
    formFieldsPanel.add(countryLabel); formFieldsPanel.add(countryField);

    JLabel phoneLabel = new JLabel("Phone Number:");
    phoneLabel.setFont(customFont);
    formFieldsPanel.add(phoneLabel); formFieldsPanel.add(phoneField);

    // Add formFieldsPanel to the center of the fieldsAndButtonPanel
    fieldsAndButtonPanel.add(formFieldsPanel, BorderLayout.CENTER);

    // Submit button
    JButton submit = new JButton("Submit");
    submit.setBackground(new Color(128, 0, 128));
    submit.setForeground(Color.WHITE);
    submit.setFont(new Font("MinionPro", Font.BOLD, 20));
    submit.setPreferredSize(new Dimension(200, 40));
    JPanel buttonPanel = new JPanel();
    buttonPanel.setOpaque(false);
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
    buttonPanel.add(submit);

    // Add buttonPanel to the south of the fieldsAndButtonPanel
    fieldsAndButtonPanel.add(buttonPanel, BorderLayout.SOUTH);

    // Add the fieldsAndButtonPanel to the borderedContentPanel
    borderedContentPanel.add(fieldsAndButtonPanel, BorderLayout.CENTER);

    // Add borderedContentPanel to the centerWrapper, then add centerWrapper to contentPanel
    centerWrapper.add(borderedContentPanel); // Center the bordered panel
    contentPanel.add(centerWrapper, BorderLayout.CENTER);

    submit.addActionListener(e -> {
        try {
            // Basic validation
            if (nameField.getText().isEmpty() || addressField.getText().isEmpty() ||
                cityField.getText().isEmpty() || countryField.getText().isEmpty() ||
                phoneField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(contentPanel, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Store store = storeService.createStore(
                nameField.getText(),
                addressField.getText(),
                cityField.getText(),
                countryField.getText(),
                phoneField.getText()
            );

            if (store != null) {
                JOptionPane.showMessageDialog(contentPanel, "Store created with ID: " + store.getStoreId(), "Success", JOptionPane.INFORMATION_MESSAGE);
                // Clear fields after successful submission
                nameField.setText("");
                addressField.setText("");
                cityField.setText("");
                countryField.setText("");
                phoneField.setText("");
            } else {
                JOptionPane.showMessageDialog(contentPanel, "Failed to create store. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(contentPanel, "Error: " + ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(contentPanel, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace(); // Print stack trace for debugging
        }
    });

    contentPanel.revalidate();
    contentPanel.repaint();
    }

    private void menuUpdateStore() {
        contentPanel.removeAll();

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setOpaque(false);

        JTextField idField = new JTextField(15);
        JTextField nameField = new JTextField(15);
        JTextField addressField = new JTextField(15);
        JTextField cityField = new JTextField(15);
        JTextField countryField = new JTextField(15);
        JTextField phoneField = new JTextField(15);

        JTextField[] fields = {idField, nameField, addressField, cityField, countryField, phoneField};
        for (JTextField field : fields) {
            field.setFont(customFont);
            field.setPreferredSize(new Dimension(200, 30));
            field.setBorder(BorderFactory.createCompoundBorder(
                field.getBorder(),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
        }

        JLabel idLabel = new JLabel("Store ID:");
        idLabel.setFont(customFont);
        form.add(idLabel); form.add(idField);

        JLabel nameLabel = new JLabel("New Name (optional):");
        nameLabel.setFont(customFont);
        form.add(nameLabel); form.add(nameField);

        JLabel addressLabel = new JLabel("New Address (optional):");
        addressLabel.setFont(customFont);
        form.add(addressLabel); form.add(addressField);

        JLabel cityLabel = new JLabel("New City (optional):");
        cityLabel.setFont(customFont);
        form.add(cityLabel); form.add(cityField);

        JLabel countryLabel = new JLabel("New Country (optional):");
        countryLabel.setFont(customFont);
        form.add(countryLabel); form.add(countryField);

        JLabel phoneLabel = new JLabel("New Phone (optional):");
        phoneLabel.setFont(customFont);
        form.add(phoneLabel); form.add(phoneField);

        ImageIcon rawIcon = new ImageIcon(getClass().getResource("/refresh.png"));
        Image scaledImage = rawIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(scaledImage);
        JLabel titleLabel = new JLabel("", icon, JLabel.CENTER);
        titleLabel.setFont(new Font("MinionPro", Font.BOLD, 20));
        titleLabel.setForeground(Color.DARK_GRAY);

        TitledBorder titled = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            "", // no text
            TitledBorder.CENTER,
            TitledBorder.TOP
        );

        JPanel titledPanel = new JPanel(new BorderLayout());
        titledPanel.setOpaque(false);
        titledPanel.setBorder(BorderFactory.createCompoundBorder(
            titled,
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        titledPanel.add(titleLabel, BorderLayout.NORTH);
        titledPanel.add(form, BorderLayout.CENTER);

        JButton submit = new JButton("Update");
        submit.setBackground(new Color(128, 0, 128));
        submit.setForeground(Color.WHITE);
        submit.setFont(new Font("MinionPro", Font.BOLD, 20));
        submit.setPreferredSize(new Dimension(200, 40));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonPanel.add(submit);

        JPanel wrapper = new JPanel(new BorderLayout(10, 10));
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(80, 80, 80, 80));
        wrapper.add(titledPanel, BorderLayout.CENTER);
        wrapper.add(buttonPanel, BorderLayout.SOUTH);
        wrapper.setMaximumSize(new Dimension(1000, 700));

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(wrapper);

        contentPanel.add(centerWrapper, BorderLayout.CENTER);

        submit.addActionListener(e -> {
            try {
                if (idField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter a Store ID to update.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int id = Integer.parseInt(idField.getText());

                Store store = storeService.getStoreById(id);
                if (store == null) {
                    JOptionPane.showMessageDialog(this, "Store not found with ID: " + id, "Update Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String newName = nameField.getText().trim().isEmpty() ? store.getStoreName() : nameField.getText().trim();
                String newAddress = addressField.getText().trim().isEmpty() ? store.getAddress() : addressField.getText().trim();
                String newCity = cityField.getText().trim().isEmpty() ? store.getCity() : cityField.getText().trim();
                String newCountry = countryField.getText().trim().isEmpty() ? store.getCountry() : countryField.getText().trim();
                String newPhone = phoneField.getText().trim().isEmpty() ? store.getPhone() : phoneField.getText().trim();

                storeService.updateStore(id, newName, newAddress, newCity, newCountry, newPhone);
                JOptionPane.showMessageDialog(this, "Store ID " + id + " updated successfully.");
                idField.setText("");
                nameField.setText("");
                addressField.setText("");
                cityField.setText("");
                countryField.setText("");
                phoneField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number format for Store ID.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Update Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void menuToggleStoreStatus() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        JPanel form = new JPanel(new FlowLayout());
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(100, 200, 100, 200));

        JTextField idField = new JTextField(15);
        idField.setFont(customFont);
        idField.setPreferredSize(new Dimension(200, 30));
        idField.setBorder(BorderFactory.createCompoundBorder(
            idField.getBorder(),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JLabel idLabel = new JLabel("Store ID to change Status:");
        idLabel.setFont(customFont);
        form.add(idLabel);
        form.add(idField);

        JButton toggleBtn = new JButton("Activate/Deactivate");
        toggleBtn.setBackground(new Color(128, 0, 128));
        toggleBtn.setForeground(Color.WHITE);
        toggleBtn.setFont(new Font("MinionPro", Font.BOLD, 20));
        toggleBtn.setPreferredSize(new Dimension(200, 40));
        form.add(toggleBtn);

        JLabel titleLabel = new JLabel("");
        titleLabel.setFont(new Font("MinionPro", Font.BOLD, 20));
        titleLabel.setForeground(Color.DARK_GRAY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

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
        titledPanel.add(form, BorderLayout.CENTER);

        JPanel wrapper = new JPanel(new BorderLayout(10, 10));
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(80, 80, 80, 80));
        wrapper.add(titledPanel, BorderLayout.CENTER);
        wrapper.setMaximumSize(new Dimension(1000, 700));

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(wrapper);
        contentPanel.add(centerWrapper, BorderLayout.CENTER);

        toggleBtn.addActionListener(e -> {
            try {
                String idText = idField.getText().trim();
                if (idText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter a Store ID to toggle status.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int id = Integer.parseInt(idText);
                Store s = storeService.getStoreById(id);
                if (s == null) {
                    JOptionPane.showMessageDialog(this, "No store found with ID: " + id, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                boolean newStatus = !s.isActive();
                storeService.setStoreActiveStatus(id, newStatus);

                JOptionPane.showMessageDialog(this, "Store ID " + id + " status changed to: " + (newStatus ? "Active" : "Inactive"));
                idField.setText("");
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Invalid Store ID format.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Unexpected Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void menuShowSpecificStore() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        JPanel form = new JPanel(new FlowLayout());
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField idField = new JTextField(15);
        idField.setFont(customFont);
        idField.setPreferredSize(new Dimension(200, 30));
        idField.setBorder(BorderFactory.createCompoundBorder(
            idField.getBorder(),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JButton search = new JButton("Search");
        search.setBackground(new Color(128, 0, 128));
        search.setForeground(Color.WHITE);
        search.setFont(customFont);

        JLabel enterTextLabel = new JLabel("Enter Store ID:");
        enterTextLabel.setFont(customFont);
        form.add(enterTextLabel);
        form.add(idField);
        form.add(search);

        JTextArea resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 20));
        JScrollPane scroll = new JScrollPane(resultArea);

        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setOpaque(false);
        topSection.add(form, BorderLayout.NORTH); // Adjusted to put input/search at the top

        JLabel titleLabel = new JLabel(""); // Added text to title
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
        titledPanel.add(form, BorderLayout.CENTER);


        JPanel wrapper = new JPanel(new BorderLayout(10, 10));
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(80, 80, 80, 80));
        wrapper.add(titledPanel, BorderLayout.NORTH); // Changed to NORTH to make room for results
        wrapper.add(scroll, BorderLayout.CENTER); // Added scrollable result area

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(wrapper);

        contentPanel.add(centerWrapper, BorderLayout.CENTER);

        search.addActionListener(e -> {
            try {
                String input = idField.getText();
                if (input.trim().isEmpty()) {
                    resultArea.setText("Please enter a Store ID.");
                    return;
                }
                int id = Integer.parseInt(input);
                Store store = storeService.getStoreById(id);
                if (store != null) {
                    resultArea.setText(store.toString());
                } else {
                    resultArea.setText("Store not found with ID: " + id);
                }
            } catch (NumberFormatException ex) {
                resultArea.setText("Invalid Store ID. Please enter a number.");
            } catch (Exception ex) {
                resultArea.setText("An error occurred: " + ex.getMessage());
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private DefaultTableModel storeTableModel; // Keep reference to table model
    private JTable storeTable; // Keep reference to table

    private void menuViewAllStores() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        JPanel tableDisplayPanel = new JPanel(new BorderLayout());
        tableDisplayPanel.setOpaque(false);
        tableDisplayPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50)); // Padding

        storeTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Address", "City", "Country", "Phone", "Active"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Makes table non-editable
            }
        };
        storeTable = new JTable(storeTableModel);
        storeTable.setFont(new Font("MinionPro", Font.PLAIN, 18)); // Custom font
        storeTable.getTableHeader().setFont(new Font("MinionPro", Font.BOLD, 20)); // Custom font for header
        storeTable.setRowHeight(30); // Larger row height

        JScrollPane scrollPane = new JScrollPane(storeTable);
        tableDisplayPanel.add(scrollPane, BorderLayout.CENTER);

        ImageIcon rawIcon = new ImageIcon(getClass().getResource("/refresh.png")); // Example icon
        Image scaledImage = rawIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(scaledImage);

        JLabel titleLabel = new JLabel("", icon, JLabel.LEFT);
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


        // Add the titled panel to the NORTH of the table display panel
        JPanel overallPanel = new JPanel(new BorderLayout());
        overallPanel.setOpaque(false);
        overallPanel.add(titledPanel, BorderLayout.NORTH);
        overallPanel.add(tableDisplayPanel, BorderLayout.CENTER); // Table panel below the title

        contentPanel.add(overallPanel, BorderLayout.CENTER);


        refreshStoreTable(); // Populate table on display

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void refreshStoreTable() {
        storeTableModel.setRowCount(0); // Clear existing data
        List<Store> allStores = storeService.getStores();
        for (Store s : allStores) {
            storeTableModel.addRow(new Object[]{
                s.getStoreId(), s.getStoreName(), s.getAddress(), s.getCity(), s.getCountry(), s.getPhone(), s.isActive()
            });
        }
    }

    private void menuGetStoreAsJson() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        JPanel form = new JPanel(new FlowLayout());
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField idField = new JTextField(15);
        idField.setFont(customFont);
        idField.setPreferredSize(new Dimension(200, 30));
        idField.setBorder(BorderFactory.createCompoundBorder(
            idField.getBorder(),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JButton showJsonBtn = new JButton("Show JSON");
        showJsonBtn.setBackground(new Color(128, 0, 128));
        showJsonBtn.setForeground(Color.WHITE);
        showJsonBtn.setFont(customFont);

        JLabel enterTextLabel = new JLabel("Enter Store ID:");
        enterTextLabel.setFont(customFont);
        form.add(enterTextLabel);
        form.add(idField);
        form.add(showJsonBtn);

        JTextArea jsonDisplayArea = new JTextArea(15, 50);
        jsonDisplayArea.setEditable(false);
        jsonDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 18)); // Larger font for JSON
        JScrollPane scrollPane = new JScrollPane(jsonDisplayArea);


        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setOpaque(false);
        topSection.add(form, BorderLayout.NORTH);

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
        titledPanel.add(form, BorderLayout.CENTER);

        JPanel wrapper = new JPanel(new BorderLayout(10, 10));
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(80, 80, 80, 80));
        wrapper.add(titledPanel, BorderLayout.NORTH);
        wrapper.add(scrollPane, BorderLayout.CENTER);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(wrapper);

        contentPanel.add(centerWrapper, BorderLayout.CENTER);

        showJsonBtn.addActionListener(e -> {
            try {
                if (idField.getText().trim().isEmpty()) {
                    jsonDisplayArea.setText("Please enter a Store ID to view JSON.");
                    return;
                }
                int id = Integer.parseInt(idField.getText());
                String json = storeService.getStoreAsJson(id);
                if (json != null && !json.contains("Store not found")) {
                    jsonDisplayArea.setText(json);
                } else {
                    jsonDisplayArea.setText("Store not found with ID: " + id);
                }
            } catch (NumberFormatException ex) {
                jsonDisplayArea.setText("Invalid Store ID. Please enter a number.");
            } catch (Exception ex) {
                jsonDisplayArea.setText("An error occurred: " + ex.getMessage());
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }
}