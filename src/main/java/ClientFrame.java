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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class ClientFrame extends JFrame {
    private final JPanel contentPanel;
    private final DefaultTableModel tableModel = new DefaultTableModel(new String[]{
        "ID", "First Name", "Last Name", "Email", "Phone", "Gender",
        "Birth Date", "Active", "Date Joined", "Last Purchase", "Sum"
    }, 0);
    Font customFont = new Font("MinionPro", Font.PLAIN, 25);
    // Table model and table for "View All Clients" to be accessible for refresh
    private DefaultTableModel clientTableModel;
    private JTable clientTable;

    public ClientFrame() {
        setTitle("Client Menu");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Background with image
        JPanel background = new JPanel();
        background.setOpaque(false);
        //BackgroundPanel background = new BackgroundPanel("RETAIL1.png");
        //background.setLayout(new BorderLayout());
        
        this.add(background);
        getContentPane().setBackground(Color.WHITE);

        // Web-style top navigation bar
        JPanel topBar = new JPanel();
        topBar.setLayout(new BoxLayout(topBar, BoxLayout.X_AXIS));
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Navigation items
        String[] navItems  = {
            "Create", "Show Client", "View All", "Update", "Delete", "Delete Inactive", "Export JSON"};

        JLabel logo = new JLabel("ClientMenu");
        logo.setFont(new Font("MinionPro", Font.BOLD, 25));
        logo.setForeground(Color.BLACK);
        topBar.add(logo);
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
        try {
            backButton.setIcon(new ImageIcon(getClass().getResource("left-arrow.png"))); 
        } catch (Exception e) {
            System.err.println("Could not find image");
        }
        backButton.setPreferredSize(new Dimension(40, 30));
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);
        backButton.setBackground(Color.WHITE);
        backButton.setContentAreaFilled(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> dispose()); // Closes the window

        // Add to the left or right side of the top bar
        topBar.add(Box.createHorizontalStrut(10));
        topBar.add(backButton);

        background.setLayout(new BorderLayout());
        background.add(topBar, BorderLayout.NORTH);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        background.add(contentPanel, BorderLayout.CENTER); 

        menuCreateClient(); // Shows create form by default
        setVisible(true); // Show window
    }

    // Create a new client
    private void menuCreateClient() {
        contentPanel.removeAll(); // Clear previous content

        // Form layout
        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setOpaque(false);
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

        // Add fields to form
        JLabel label = new JLabel("First Name:");
        label.setFont(customFont);
        form.add(label); form.add(fname);

        JLabel lLabel = new JLabel("Last Name:");
        lLabel.setFont(customFont);
        form.add(lLabel); form.add(lname);

        JLabel pLabel = new JLabel("Phone Number:");
        pLabel.setFont(customFont);
        form.add(pLabel); form.add(phone);

        JLabel eLabel = new JLabel("Email:");
        eLabel.setFont(customFont);
        form.add(eLabel); form.add(email);

        JLabel gLabel = new JLabel("Gender:");
        gLabel.setFont(customFont);
        form.add(gLabel); form.add(genderBox);

        JLabel bLabel = new JLabel("Birth Date (dd/MM/yyyy):");
        bLabel.setFont(customFont);
        form.add(bLabel); form.add(birth);

        TitledBorder titled = BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(Color.GRAY, 2),"",
        TitledBorder.LEFT,
        TitledBorder.TOP,
        new Font("MinionPro", Font.BOLD, 20),
        Color.DARK_GRAY
        );

        form.setBorder(BorderFactory.createCompoundBorder(titled,BorderFactory.createEmptyBorder(30, 30, 30, 30)));

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

        // Wrapper for layout
        JPanel wrapper = new JPanel(new BorderLayout(10, 10));
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(80, 80, 80, 80));
        wrapper.add(form, BorderLayout.CENTER);
        wrapper.add(buttonPanel, BorderLayout.SOUTH);
        wrapper.setMaximumSize(new Dimension(1000, 700));

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(wrapper);

        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(centerWrapper, BorderLayout.CENTER);

        // Create client on submit
        submit.addActionListener(e -> {
            try {
                LocalDate birthDate = birth.getText().isEmpty() ? null :
                    LocalDate.parse(birth.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                Client client = ClientService.createClient(
                    fname.getText(), lname.getText(), birthDate,
                    phone.getText(), email.getText(),
                    genderBox.getSelectedItem().toString(), true
                );

               // clients.add(client);
                refreshTable(); // Update table
                JOptionPane.showMessageDialog(this, "Client created with ID: " + client.getClientId());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
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
        JTextField newDateOfBirth = new JTextField(client.getBirthDate().toString(), 15);

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

    JLabel dLabel = new JLabel("Date Of Birth:");
    dLabel.setFont(customFont);
    form.add(dLabel); form.add(newDateOfBirth);

    // Title with icon
    ImageIcon rawIcon = new ImageIcon(getClass().getResource("/refresh.png"));
    Image scaledImage = rawIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
    ImageIcon icon = new ImageIcon(scaledImage);    
    //Dimension updateIconD = new Dimension(1,1);
    //setPreferredSize(updateIconD);
    JLabel titleLabel = new JLabel("", icon, JLabel.LEFT);
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
            LocalDate birthDate = finalClient.getBirthDate();
            if (birthDate != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Or any desired format
                newDateOfBirth.setText(dateFormat.toString());
            } else {
                newDateOfBirth.setText(""); // Clear if no birth date
            }
            newDateOfBirth.setEditable(false); // User cannot update this field
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
        contentPanel.removeAll(); // Clear previous content
        contentPanel.setLayout(new BorderLayout());

        JPanel form = new JPanel();
        form.setLayout(new BorderLayout());
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

        JButton search = new JButton("Search");
        search.setBackground(new Color(128, 0, 128));
        search.setForeground(Color.WHITE);
        search.setFont(customFont);

        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel enterTextLabel = new JLabel("Enter Email or Phone Number:");
        enterTextLabel.setFont(customFont);
        topPanel.add(enterTextLabel);
        topPanel.add(inputField);
        topPanel.add(search);
        JTextArea resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("MinionPro", Font.PLAIN, 20));
        JScrollPane scroll = new JScrollPane(resultArea);

        form.add(topPanel, BorderLayout.NORTH);
        form.add(scroll, BorderLayout.CENTER);

        search.addActionListener(e -> {
            String input = inputField.getText();
            Client client = ClientService.getClientDAO().getAllClients().stream()
                .filter(c -> input.equals(c.getEmail()) || input.equals(c.getPhoneNumber()))
                .findFirst()
                .orElse(null);

            if (client != null) {
                resultArea.setText(client.toString());
            } else {
                resultArea.setText("Client not found.");
            }
        });

        contentPanel.add(form, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void menuViewAllClients() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        JPanel tableDisplayPanel = new JPanel(new BorderLayout());
        tableDisplayPanel.setOpaque(false);
        tableDisplayPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50)); // Padding

        // Define table model with column headers
        clientTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Phone", "Active"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Makes table non-editable
            }
        };
        clientTable = new JTable(clientTableModel);
        clientTable.setFont(new Font("MinionPro", Font.PLAIN, 18)); // Custom font for table data
        clientTable.getTableHeader().setFont(new Font("MinionPro", Font.BOLD, 20)); // Custom font for header
        clientTable.setRowHeight(30); // Larger row height for better readability

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

        contentPanel.add(overallPanel, BorderLayout.CENTER); // Add to the main content panel

        refreshTable(); // Populate table on display

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
         tableModel.setRowCount(0); // clear existing data

         // Fetching all clients
         List <Client> clients = ClientService.getClientDAO().getAllClients();
         for (Client client : clients) {
                clientTableModel.addRow(new Object[]{
                    client.getClientId(),
                    client.getFirstName(),
                    client.getLastName(),
                    client.getEmail(),
                    client.getPhoneNumber(),
                    client.isActiveStatus() // Assuming Client has an isActive() method
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

