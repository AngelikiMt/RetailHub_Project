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

public class ClientFrame extends JFrame {
    private final JPanel contentPanel;
    Font customFont = new Font("MinionPro", Font.PLAIN, 25);
    // Table model and table for "View All Clients" to be accessible for refresh
    private DefaultTableModel clientTableModel;
    private JTable clientTable;

    public ClientFrame() {
        setTitle("Client Menu");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel background = new JPanel();
        background.setOpaque(false);
        
        this.add(background);
        getContentPane().setBackground(Color.WHITE);

        clientTableModel = new DefaultTableModel(new String[]{
            "ID", "First Name", "Last Name", "Email", "Phone", "Gender",
            "Birth Date", "Active", "Date Joined", "Last Purchase", "Sum"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Makes table non-editable
            }
        };
        clientTable = new JTable(clientTableModel);
        clientTable.setFont(new Font("MinionPro", Font.PLAIN, 18));
        clientTable.getTableHeader().setFont(new Font("MinionPro", Font.BOLD, 20));
        clientTable.setRowHeight(30);

        // Top navigation bar
        JPanel topBar = new JPanel();
        topBar.setLayout(new BoxLayout(topBar, BoxLayout.X_AXIS));
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Navigation items
        String[] navItems  = {
            "Create", "Show Client", "View All", "Update", "Delete", "Delete Inactive", "Export JSON"};

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
        // If you want text AND icon: logoLabel.setText("ClientMenu");
        // If you only want the icon, you don't need font/foreground for text unless it's a fallback
        }
        topBar.add(logoLabel);
        topBar.add(Box.createHorizontalStrut(30)); // space before nav items

        topBar.add(Box.createHorizontalGlue());

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
                case "Create" -> navButton.addActionListener(e -> menuCreateClient());
                case "Show Client" -> navButton.addActionListener(e -> menuShowClient());
                case "View All" -> navButton.addActionListener(e -> menuViewAllClients());
                case "Update" -> navButton.addActionListener(e -> menuUpdateClient());
                case "Delete" -> navButton.addActionListener(e -> menuDeleteClient());
                case "Delete Inactive" -> navButton.addActionListener(e -> menuDeleteInactiveClients());
                case "Export JSON" -> navButton.addActionListener(e -> menuGetJson());
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

        backButton.setPreferredSize(new Dimension(50, 30)); // Keep your desired button size
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false); // Set to false for transparency
        backButton.setContentAreaFilled(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> dispose());
        // Adds to the right side of the top bar
        topBar.add(Box.createHorizontalStrut(10));
        topBar.add(backButton);

        background.setLayout(new BorderLayout());
        background.add(topBar, BorderLayout.NORTH);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        background.add(contentPanel, BorderLayout.CENTER); 

        menuCreateClient(); // Shows create form by default
        setVisible(true); // Shows window
    }

    // Creates a new client
    private void menuCreateClient() {
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
            addIcon = new ImageIcon(getClass().getResource("/add-user.png"));

            if (addIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                Image image = addIcon.getImage();
                Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Adjust size as needed
                addIcon = new ImageIcon(scaledImage);
            } else {
                System.err.println("Warning: Could not load add.png, or it's not a valid image.");
                // Fallback to text if icon fails to load
                iconLabel.setText("Add New Client");
                iconLabel.setFont(new Font("MinionPro", Font.BOLD, 22));
                iconLabel.setForeground(Color.DARK_GRAY);
            }
        } catch (Exception e) {
            System.err.println("Error loading add.png: " + e.getMessage());
            // Fallback to text if an exception occurs
            iconLabel.setText("Add New Client");
            iconLabel.setFont(new Font("MinionPro", Font.BOLD, 22));
            iconLabel.setForeground(Color.DARK_GRAY);
        }

        if (addIcon != null) {
            iconLabel.setIcon(addIcon);
        }

        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        borderedContentPanel.add(iconLabel, BorderLayout.NORTH);

        // Create a panel to hold the form fields and button
        JPanel fieldsAndButtonPanel = new JPanel(new BorderLayout());
        fieldsAndButtonPanel.setOpaque(false);
        fieldsAndButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // Padding below icon

        // Form layout (your existing GridLayout for fields)
        JPanel formFieldsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formFieldsPanel.setOpaque(false);

        JTextField fname = new JTextField(15);
        fname.setFont(customFont);
        fname.setPreferredSize(new Dimension(200, 30));
        fname.setBorder(BorderFactory.createCompoundBorder(
            fname.getBorder(),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JTextField lname = new JTextField(15);
        lname.setFont(customFont);
        lname.setPreferredSize(new Dimension(200, 30));
        lname.setBorder(BorderFactory.createCompoundBorder(
            lname.getBorder(),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JTextField phone = new JTextField(15);
        phone.setFont(customFont);
        phone.setPreferredSize(new Dimension(200, 30));
        phone.setBorder(BorderFactory.createCompoundBorder(
            phone.getBorder(),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JTextField email = new JTextField(15);
        email.setFont(customFont);
        email.setPreferredSize(new Dimension(200, 30));
        email.setBorder(BorderFactory.createCompoundBorder(
            email.getBorder(),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        String[] genderOptions = {"MALE", "FEMALE", "OTHER"};
        JComboBox<String> genderBox = new JComboBox<>(genderOptions);
        genderBox.setFont(customFont);
        genderBox.setPreferredSize(new Dimension(200, 30));
        genderBox.setBorder(BorderFactory.createCompoundBorder(
            genderBox.getBorder(),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JTextField birth = new JTextField(15);
        birth.setFont(customFont);
        birth.setPreferredSize(new Dimension(200, 40));
        birth.setBorder(BorderFactory.createCompoundBorder(
            birth.getBorder(),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Add fields to formFieldsPanel
        JLabel label = new JLabel("First Name:");
        label.setFont(customFont);
        formFieldsPanel.add(label); formFieldsPanel.add(fname);

        JLabel lLabel = new JLabel("Last Name:");
        lLabel.setFont(customFont);
        formFieldsPanel.add(lLabel); formFieldsPanel.add(lname);

        JLabel pLabel = new JLabel("Phone Number:");
        pLabel.setFont(customFont);
        formFieldsPanel.add(pLabel); formFieldsPanel.add(phone);

        JLabel eLabel = new JLabel("Email:");
        eLabel.setFont(customFont);
        formFieldsPanel.add(eLabel); formFieldsPanel.add(email);

        JLabel gLabel = new JLabel("Gender:");
        gLabel.setFont(customFont);
        formFieldsPanel.add(gLabel); formFieldsPanel.add(genderBox);

        JLabel bLabel = new JLabel("Birth Date (dd/MM/yyyy):");
        bLabel.setFont(customFont);
        formFieldsPanel.add(bLabel); formFieldsPanel.add(birth);

        // Adds formFieldsPanel to the center of the fieldsAndButtonPanel
        fieldsAndButtonPanel.add(formFieldsPanel, BorderLayout.CENTER);

        // Submit button
        JButton submit = new JButton("Submit");
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
                LocalDate birthDate = birth.getText().isEmpty() ? null :
                    LocalDate.parse(birth.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                Client client = ClientService.createClient(fname.getText(), lname.getText(), birthDate,
                    phone.getText(), email.getText(), genderBox.getSelectedItem().toString(), true);

                refreshTable(); // Update table
                JOptionPane.showMessageDialog(contentPanel, "Client created with ID: " + client.getClientId());

                // Clear fields after successful submission
                fname.setText("");
                lname.setText("");
                phone.setText("");
                email.setText("");
                birth.setText("");
                genderBox.setSelectedIndex(0); // Reset gender to first option

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(contentPanel, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace(); // Print stack trace for debugging
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void menuUpdateClient() {
        contentPanel.removeAll();

        String input = JOptionPane.showInputDialog(this, "Enter Email or Phone:");
        Client client = null;
        for (int i = 0; i < ClientService.getClientDAO().getAllClients().size(); i++) {
            Client c = ClientService.getClientDAO().getAllClients().get(i);
            if (input == null || input.trim().isEmpty()) {
                JOptionPane.showMessageDialog(contentPanel, "Please enter an email or PhoneNumber");
            }
            if (input.equals(c.getEmail()) || input.equals(c.getPhoneNumber())) {
                client = c;
                break;
            }
        }

        if (client == null) {
            JOptionPane.showMessageDialog(this, "Client not found.");
            return;
        }

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setOpaque(false);

        JTextField newFname = new JTextField(client.getFirstName(), 15);
        JTextField newLname = new JTextField(client.getLastName(), 15);
        JTextField newEmail = new JTextField(client.getEmail(), 15);
        JTextField newPhone = new JTextField(client.getPhoneNumber(), 15);

        JTextField[] fields = {newFname, newLname, newEmail, newPhone};
        for (JTextField field : fields) {
            field.setFont(customFont);
            field.setPreferredSize(new Dimension(200, 35));
            field.setBorder(BorderFactory.createCompoundBorder(
                field.getBorder(),
                BorderFactory.createEmptyBorder(0, 8, 0, 8)
        ));
    }

    // Add fields to form
    JLabel fLabel = new JLabel("First Name:");
    fLabel.setFont(customFont);
    form.add(fLabel); form.add(newFname);

    JLabel lLabel = new JLabel("Last Name:");
    lLabel.setFont(customFont);
    form.add(lLabel); form.add(newLname);

    JLabel eLabel = new JLabel("Email:");
    eLabel.setFont(customFont);
    form.add(eLabel); form.add(newEmail);

    JLabel pLabel = new JLabel("Phone Number:");
    pLabel.setFont(customFont);
    form.add(pLabel); form.add(newPhone);

    // Title with icon
    ImageIcon rawIcon = new ImageIcon(getClass().getResource("/refresh.png"));
    Image scaledImage = rawIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
    ImageIcon icon = new ImageIcon(scaledImage);    
    JLabel titleLabel = new JLabel("", icon, JLabel.CENTER);
    titleLabel.setFont(new Font("MinionPro", Font.BOLD, 20));
    titleLabel.setForeground(Color.DARK_GRAY);

    // Custom titled border
    TitledBorder titled = BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(Color.GRAY, 1),
        "", // no text here
        TitledBorder.LEFT,
        TitledBorder.TOP
    );

    // Wrap title in JPanel
    JPanel titledPanel = new JPanel(new BorderLayout());
    titledPanel.setOpaque(false);
    titledPanel.setBorder(BorderFactory.createCompoundBorder(
        titled,
        BorderFactory.createEmptyBorder(30, 30, 30, 30)
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
    Client finalClient = client;
    submit.addActionListener(e -> {
        try {
            ClientService.updateClient(
                finalClient,
                newFname.getText(),
                newLname.getText(),
                newEmail.getText(),
                newPhone.getText()
            );
            refreshTable();
            JOptionPane.showMessageDialog(this, "Client updated.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Update Failed", JOptionPane.ERROR_MESSAGE);
        }
    });

    contentPanel.revalidate();
    contentPanel.repaint();
    }

    private void menuShowClient() {
    contentPanel.removeAll();
    contentPanel.setLayout(new BorderLayout());

    JPanel form = new JPanel(new FlowLayout());
    form.setOpaque(false);
    form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JTextField inputField = new JTextField(15);
    inputField.setFont(customFont);
    inputField.setPreferredSize(new Dimension(200, 30));
    inputField.setBorder(BorderFactory.createCompoundBorder(
            inputField.getBorder(),
            BorderFactory.createEmptyBorder(2, 10, 2, 10)
    ));

    JButton search = new JButton("Search");
    search.setBackground(new Color(128, 0, 128));
    search.setForeground(Color.WHITE);
    search.setFont(customFont);

    JLabel enterTextLabel = new JLabel("Enter Email or Phone Number:");
    enterTextLabel.setFont(customFont);
    form.add(enterTextLabel);
    form.add(inputField);
    form.add(search);

    JTextArea resultArea = new JTextArea(10, 40);
    resultArea.setEditable(false);
    resultArea.setFont(new Font("Monospaced", Font.PLAIN, 20));
    JScrollPane scroll = new JScrollPane(resultArea);

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
    wrapper.add(scroll, BorderLayout.CENTER);

    JPanel centerWrapper = new JPanel(new GridBagLayout());
    centerWrapper.setOpaque(false);
    centerWrapper.add(wrapper);

    contentPanel.add(centerWrapper, BorderLayout.CENTER);

    search.addActionListener(e -> {
        try {
            String input = inputField.getText().trim(); // Trim whitespace
            if (input.isEmpty()) {
                resultArea.setText("Please enter an email or phone number.");
                return;
            }

            Optional<Client> clientOptional = ClientService.getClientDAO().getAllClients().stream()
                    .filter(c -> input.equalsIgnoreCase(c.getEmail()) || input.equals(c.getPhoneNumber())) // Case-insensitive email check
                    .findFirst();

            if (clientOptional.isPresent()) {
                resultArea.setText(clientOptional.get().toString());
            } else {
                resultArea.setText("Client not found for: " + input);
            }
        } catch (Exception ex) {
            resultArea.setText("An error occurred: " + ex.getMessage());
        }
    });

    contentPanel.revalidate();
    contentPanel.repaint();
    }

    private void menuViewAllClients() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        JPanel tableDisplayPanel = new JPanel(new BorderLayout());
        tableDisplayPanel.setOpaque(false);
        tableDisplayPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JScrollPane scrollPane = new JScrollPane(clientTable);
        tableDisplayPanel.add(scrollPane, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("");
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

    private void menuDeleteClient() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        JPanel form = new JPanel(new BorderLayout());
        form.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20),
            "",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("MinionPro", Font.BOLD, 20),
            Color.DARK_GRAY
        ));

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
        JLabel enterTextLabel = new JLabel("Enter Email or Phone:");
        enterTextLabel.setFont(customFont);
        topPanel.add(enterTextLabel);        
        topPanel.add(inputField);
        topPanel.add(delete);

        form.add(topPanel, BorderLayout.NORTH);

        delete.addActionListener(e -> {
            String input = inputField.getText();
            Client client = ClientService.getClientDAO().getAllClients().stream()
                .filter(c -> input.equals(c.getEmail()) || input.equals(c.getPhoneNumber()))
                .findFirst()
                .orElse(null);

            if (client == null) {
                JOptionPane.showMessageDialog(this, "Client not found.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Delete client: " + client.getFirstName() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                ClientService.deleteClient(client.getClientId());
                refreshTable();
                JOptionPane.showMessageDialog(this, "Client deleted.");
            }
        });
        contentPanel.add(form, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void menuDeleteInactiveClients() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        JPanel form = new JPanel(new BorderLayout());
        form.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20),
            "",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("MinionPro", Font.BOLD, 20),
            Color.DARK_GRAY
        ));

        JTextField pinField = new JPasswordField(10);
        pinField.setFont(customFont);
        pinField.setPreferredSize(new Dimension(200, 30));
        pinField.setBorder(BorderFactory.createCompoundBorder(
        pinField.getBorder(),
        BorderFactory.createEmptyBorder(1, 10, 1, 10)));

        JButton confirm = new JButton("Confirm");
        confirm.setBackground(new Color(128, 0, 128));
        confirm.setForeground(Color.WHITE);
        confirm.setFont(new Font("MinionPro", Font.BOLD, 20));
        confirm.setPreferredSize(new Dimension(200, 40));

        JPanel centerPanel = new JPanel(new FlowLayout());
        JLabel enterTextLabel = new JLabel("Enter Admin PIN:");
        enterTextLabel.setFont(customFont);
        centerPanel.add(enterTextLabel);
        centerPanel.add(pinField);
        centerPanel.add(confirm);

        form.add(centerPanel, BorderLayout.CENTER);

        confirm.addActionListener(e -> {
            String pin = pinField.getText();
            if ("12345".equals(pin)) {
                ClientService.isInactiveMoreThan5Years();
                refreshTable();
                JOptionPane.showMessageDialog(this, "Inactive clients deleted.");
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect PIN.");
            }
        });

        contentPanel.add(form, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void menuGetJson() {
        /*contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        JPanel form = new JPanel(new BorderLayout());
        form.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20),
            "",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("MinionPro", Font.BOLD, 20),
            Color.DARK_GRAY
        ));

        JTextArea jsonArea = new JTextArea(20, 40);
        jsonArea.setEditable(false);
        jsonArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        StringBuilder sb = new StringBuilder();
        for (Client client : ClientService.getClientDAO().getAllClients()) {
            sb.append(ClientService.getClientAsJson(client)).append("\n\n");
        }
        jsonArea.setText(sb.toString());

        JScrollPane scroll = new JScrollPane(jsonArea);
        form.add(scroll, BorderLayout.CENTER);

        contentPanel.add(form, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();*/
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
            BorderFactory.createEmptyBorder(1, 10, 1, 10)
        ));

        JButton showJsonBtn = new JButton("Show JSON");
        showJsonBtn.setBackground(new Color(128, 0, 128));
        showJsonBtn.setForeground(Color.WHITE);
        showJsonBtn.setFont(customFont);

        JLabel enterTextLabel = new JLabel("Enter client ID:");
        enterTextLabel.setFont(customFont);
        form.add(enterTextLabel);
        form.add(idField);
        form.add(showJsonBtn);

        JTextArea jsonDisplayArea = new JTextArea(15, 50);
        jsonDisplayArea.setEditable(false);
        jsonDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
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
                    jsonDisplayArea.setText("Please enter a client ID to view JSON.");
                    return;
                }
                ClientService clientService = new ClientService();
                long id = Long.parseLong(idField.getText());
                String json = clientService.getClientAsJson(id);
                if (json != null && !json.contains("Client not found")) {
                    jsonDisplayArea.setText(json);
                } else {
                    jsonDisplayArea.setText("Client not found with ID: " + id);
                }
            } catch (NumberFormatException ex) {
                jsonDisplayArea.setText("Invalid Client ID. Please enter a number.");
            } catch (Exception ex) {
                jsonDisplayArea.setText("An error occurred: " + ex.getMessage());
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }

     // Updates the table with current client list
     private void refreshTable() {
        clientTableModel.setRowCount(0); // clear existing data

         // Fetching all clients
         List <Client> clients = ClientService.getClientDAO().getAllClients();
         for (Client client : clients) {
                clientTableModel.addRow(new Object[]{
                    client.getClientId(),
                    client.getFirstName(),
                    client.getLastName(),
                    client.getEmail(),
                    client.getPhoneNumber(),
                    client.getGender(),
                    client.getBirthDate(),                    
                    client.isActiveStatus(),                    
                    client.getDateJoined(),                    
                    client.getLastPurchaseDate(),
                    client.getClientSumTotal()
                });
            }
        //  for (int i = 0; i < ClientService.getClientDAO().getAllClients().size(); i++) {
        //      Client c = ClientService.getClientDAO().getAllClients().get(i);
        //      tableModel.addRow(new Object[]{
        //              c.getClientId(), c.getFirstName(), c.getLastName(), c.getEmail(),
        //          c.getPhoneNumber(), c.getGender(), c.getBirthDate(),
        //              c.isActiveStatus(), c.getDateJoined(), c.getLastPurchaseDate(), c.getClientSumTotal()
        //      });
        //  }
     }
}

