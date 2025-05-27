import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.List;

import javax.swing.BorderFactory;
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
import javax.swing.table.DefaultTableModel;

public class StoreFrame extends JFrame {
    private JPanel backgroundPanel;
    private JPanel currentContentPanel; // Holds the active sub-panel
    private StoreService storeService;

    public StoreFrame() {
        setTitle("Store Management Menu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Closes the frame
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen

        storeService = new StoreService();

        // === PANEL WITH BACKGROUND ===
        backgroundPanel = new JPanel() {
            Image bg = new ImageIcon(getClass().getResource("RetailHub.png")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        // Initial content will be the main store menu buttons
        showStoreMainMenu();

        setContentPane(backgroundPanel);
        setVisible(true);
    }

    private void showStoreMainMenu() {
        if (currentContentPanel != null) {
            backgroundPanel.remove(currentContentPanel);
        }

        // === BUTTONS ===
        JButton createBtn = new JButton("Create Store");
        JButton updateBtn = new JButton("Update Store");
        JButton deactivateBtn = new JButton("Activate/Deactivate Store");
        JButton showStoreBtn = new JButton("Show Specific Store");
        JButton showAllBtn = new JButton("View All Stores");
        JButton jsonBtn = new JButton("Get Store as JSON");
        JButton backToMainBtn = new JButton("Back to Main Menu");

        Font buttonFont = new Font("Arial", Font.BOLD, 20);
        Dimension buttonSize = new Dimension(250, 60);

        JButton[] buttons = {createBtn, updateBtn, deactivateBtn, showStoreBtn, showAllBtn, jsonBtn, backToMainBtn};
        for (JButton btn : buttons) {
            btn.setFont(buttonFont);
            btn.setPreferredSize(buttonSize);
        }

        // === PANEL WITH GRID LAYOUT ===
        JPanel buttonGrid = new JPanel(new GridLayout(3, 3, 30, 30));
        buttonGrid.setOpaque(false); // Transparent so the background is visible
        for (JButton btn : buttons) {
            buttonGrid.add(btn);
        }

        // === CENTERING BUTTONS ===
        JPanel centerPanel = new JPanel(new GridBagLayout()); // Using GridBagLayout for true centering
        centerPanel.setOpaque(false);
        centerPanel.add(buttonGrid);

        backgroundPanel.add(centerPanel, BorderLayout.SOUTH); // Add to the center
        currentContentPanel = centerPanel;
        backgroundPanel.revalidate();
        backgroundPanel.repaint();

        // === ACTIONS ===
        createBtn.addActionListener(e -> showPanel(new CreateStorePanel()));
        updateBtn.addActionListener(e -> showPanel(new UpdateStorePanel()));
        deactivateBtn.addActionListener(e -> showPanel(new ToggleStoreStatusPanel()));
        showStoreBtn.addActionListener(e -> showPanel(new ShowSpecificStorePanel()));
        showAllBtn.addActionListener(e -> showPanel(new ViewAllStoresPanel()));
        jsonBtn.addActionListener(e -> showPanel(new GetStoreAsJsonPanel()));
        backToMainBtn.addActionListener(e -> {
            dispose(); // Closes StoreFrame
        });
    }

    // Method to switch between content panels
    private void showPanel(JPanel panel) {
        if (currentContentPanel != null) {
            backgroundPanel.remove(currentContentPanel);
        }
        backgroundPanel.add(panel, BorderLayout.SOUTH);
        currentContentPanel = panel;
        backgroundPanel.revalidate();
        backgroundPanel.repaint();
    }

    // Abstract Base Panel for common elements like 'Back' button 
    private abstract class StoreOperationPanel extends JPanel {
        protected JTextField idField; // Common for update, deactivate, show, json
        protected StoreService storeService;
        protected JButton backButton;
        protected JPanel controlPanel; // Holds the back button

        public StoreOperationPanel() {
            this.storeService = new StoreService(); // Ensure StoreService is initialized
            setLayout(new BorderLayout());
            setOpaque(false); // Transparent background

            controlPanel = new JPanel(new BorderLayout());
            controlPanel.setOpaque(false);

            backButton = new JButton("Back to Store Menu");
            backButton.setFont(new Font("Arial", Font.BOLD, 18));
            backButton.addActionListener(e -> showStoreMainMenu());

            JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            backButtonPanel.setOpaque(false);
            backButtonPanel.add(backButton);
            controlPanel.add(backButtonPanel, BorderLayout.NORTH);

            add(controlPanel, BorderLayout.NORTH); // Adds the instance's controlPanel to the top
        }

        protected void showError(String message) {
            JOptionPane.showMessageDialog(this, "Error: " + message, "Error", JOptionPane.ERROR_MESSAGE);
        }

        protected void showSuccess(String message) {
            JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Create Store Panel
    private class CreateStorePanel extends StoreOperationPanel {
        private JTextField nameField, addressField, cityField, countryField, phoneField;

        public CreateStorePanel() {
            super();
            JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
            formPanel.setOpaque(false);
            formPanel.setBorder(BorderFactory.createEmptyBorder(50, 200, 50, 200));

            nameField = new JTextField(20);
            addressField = new JTextField(20);
            cityField = new JTextField(20);
            countryField = new JTextField(20);
            phoneField = new JTextField(20);

            Font labelFont = new Font("Arial", Font.PLAIN, 16);
            Font fieldFont = new Font("Arial", Font.PLAIN, 16);

            JLabel nameLabel = new JLabel("Name:");
            nameLabel.setFont(labelFont);
            JLabel addressLabel = new JLabel("Address:");
            addressLabel.setFont(labelFont);
            JLabel cityLabel = new JLabel("City:");
            cityLabel.setFont(labelFont);
            JLabel countryLabel = new JLabel("Country:");
            countryLabel.setFont(labelFont);
            JLabel phoneLabel = new JLabel("Phone:");
            phoneLabel.setFont(labelFont);

            nameField.setFont(fieldFont);
            addressField.setFont(fieldFont);
            cityField.setFont(fieldFont);
            countryField.setFont(fieldFont);
            phoneField.setFont(fieldFont);

            formPanel.add(nameLabel);
            formPanel.add(nameField);
            formPanel.add(addressLabel);
            formPanel.add(addressField);
            formPanel.add(cityLabel);
            formPanel.add(cityField);
            formPanel.add(countryLabel);
            formPanel.add(countryField);
            formPanel.add(phoneLabel);
            formPanel.add(phoneField);

            JButton createBtn = new JButton("Create Store");
            createBtn.setFont(new Font("Arial", Font.BOLD, 18));
            createBtn.setPreferredSize(new Dimension(180, 50));
            createBtn.addActionListener(e -> createStore());

            JPanel buttonHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonHolder.setOpaque(false);
            buttonHolder.add(createBtn);
            formPanel.add(new JLabel()); // Empty cell for alignment
            formPanel.add(buttonHolder);

            add(formPanel, BorderLayout.CENTER);
        }

        private void createStore() {
            try {
                Store store = storeService.createStore(nameField.getText(), addressField.getText(), cityField.getText(), countryField.getText(), phoneField.getText());
                if (store != null) {
                    showSuccess("Store created with ID: " + store.getStoreId());
                    nameField.setText("");
                    addressField.setText("");
                    cityField.setText("");
                    countryField.setText("");
                    phoneField.setText("");
                }
            } catch (IllegalArgumentException ex) {
                showError(ex.getMessage());
            } catch (Exception ex) {
                showError("An unexpected error occurred: " + ex.getMessage());
            }
        }
    }

    // Update Store Panel
    private class UpdateStorePanel extends StoreOperationPanel {
        private JTextField nameField, addressField, cityField, countryField, phoneField;

        public UpdateStorePanel() {
            super();
            JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
            formPanel.setOpaque(false);
            formPanel.setBorder(BorderFactory.createEmptyBorder(50, 200, 50, 200));

            idField = new JTextField(10);
            nameField = new JTextField(20);
            addressField = new JTextField(20);
            cityField = new JTextField(20);
            countryField = new JTextField(20);
            phoneField = new JTextField(20);

            Font labelFont = new Font("Arial", Font.PLAIN, 16);
            Font fieldFont = new Font("Arial", Font.PLAIN, 16);

            JLabel idLabel = new JLabel("Store ID to Update:");
            idLabel.setFont(labelFont);
            JLabel nameLabel = new JLabel("New Name (optional):");
            nameLabel.setFont(labelFont);
            JLabel addressLabel = new JLabel("New Address (optional):");
            addressLabel.setFont(labelFont);
            JLabel cityLabel = new JLabel("New City (optional):");
            cityLabel.setFont(labelFont);
            JLabel countryLabel = new JLabel("New Country (optional):");
            countryLabel.setFont(labelFont);
            JLabel phoneLabel = new JLabel("New Phone (optional):");
            phoneLabel.setFont(labelFont);

            idField.setFont(fieldFont);
            nameField.setFont(fieldFont);
            addressField.setFont(fieldFont);
            cityField.setFont(fieldFont);
            countryField.setFont(fieldFont);
            phoneField.setFont(fieldFont);

            formPanel.add(idLabel);
            formPanel.add(idField);
            formPanel.add(nameLabel);
            formPanel.add(nameField);
            formPanel.add(addressLabel);
            formPanel.add(addressField);
            formPanel.add(cityLabel);
            formPanel.add(cityField);
            formPanel.add(countryLabel);
            formPanel.add(countryField);
            formPanel.add(phoneLabel);
            formPanel.add(phoneField);

            JButton updateBtn = new JButton("Update Store");
            updateBtn.setFont(new Font("Arial", Font.BOLD, 18));
            updateBtn.setPreferredSize(new Dimension(180, 50));
            updateBtn.addActionListener(e -> updateStore());

            JPanel buttonHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonHolder.setOpaque(false);
            buttonHolder.add(updateBtn);
            formPanel.add(new JLabel());
            formPanel.add(buttonHolder);

            add(formPanel, BorderLayout.CENTER);
        }

        private void updateStore() {
            try {
                if (idField.getText().trim().isEmpty()) {
                    showError("Please enter a Store ID to update.");
                    return;
                }
                int id = Integer.parseInt(idField.getText());

                Store store = storeService.getStoreById(id); // Fetching store
                if (store == null) {
                    showError("Store not found with ID: " + id);
                    return;
                }

                // Get values from fields, using existing value if field is empty
                String newName = nameField.getText().trim().isEmpty() ? store.getStoreName() : nameField.getText().trim();
                String newAddress = addressField.getText().trim().isEmpty() ? store.getAddress() : addressField.getText().trim();
                String newCity = cityField.getText().trim().isEmpty() ? store.getCity() : cityField.getText().trim();
                String newCountry = countryField.getText().trim().isEmpty() ? store.getCountry() : countryField.getText().trim();
                String newPhone = phoneField.getText().trim().isEmpty() ? store.getPhone() : phoneField.getText().trim();


                storeService.updateStore(id, newName, newAddress, newCity, newCountry, newPhone);
                showSuccess("Store ID " + id + " updated successfully.");
                idField.setText("");
                nameField.setText("");
                addressField.setText("");
                cityField.setText("");
                countryField.setText("");
                phoneField.setText("");
            } catch (NumberFormatException ex) {
                showError("Invalid number format for Store ID.");
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        }
    }

    // Toggle Store Status Panel
    private class ToggleStoreStatusPanel extends StoreOperationPanel {
        public ToggleStoreStatusPanel() {
            super();
            JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
            formPanel.setOpaque(false);
            formPanel.setBorder(BorderFactory.createEmptyBorder(100, 200, 100, 200));

            idField = new JTextField(10);
            idField.setFont(new Font("Arial", Font.PLAIN, 16));

            JLabel idLabel = new JLabel("Store ID for changing active/inactive Status:");
            idLabel.setFont(new Font("Arial", Font.PLAIN, 16));

            formPanel.add(idLabel);
            formPanel.add(idField);

            JButton toggleBtn = new JButton("Activate/Deactivate");
            toggleBtn.setFont(new Font("Arial", Font.BOLD, 18));
            toggleBtn.setPreferredSize(new Dimension(180, 50));
            toggleBtn.addActionListener(e -> toggleStoreStatus());

            JPanel buttonHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonHolder.setOpaque(false);
            buttonHolder.add(toggleBtn);
            formPanel.add(new JLabel());
            formPanel.add(buttonHolder);

            add(formPanel, BorderLayout.CENTER);
        }

        private void toggleStoreStatus() {
            try {
                if (idField.getText().trim().isEmpty()) {
                    showError("Please enter a Store ID to activate/deactivate status.");
                    return;
                }
                int id = Integer.parseInt(idField.getText());
                Store s = storeService.getStoreById(id);
                if (s != null) {
                    boolean newStatus = !s.isActive();
                    storeService.setStoreActiveStatus(id, newStatus);
                    showSuccess("Store ID " + id + " status changed to: " + (newStatus ? "Active" : "Inactive"));
                    idField.setText("");
                } else {
                    showError("Store not found with ID: " + id);
                }
            } catch (NumberFormatException ex) {
                showError("Invalid Store ID.");
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        }
    }

    // Show Specific Store Panel
    private class ShowSpecificStorePanel extends StoreOperationPanel {
        private JTextArea storeDisplayArea;

        public ShowSpecificStorePanel() {
            super();
            JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
            inputPanel.setOpaque(false);
            inputPanel.setBorder(BorderFactory.createEmptyBorder(50, 200, 10, 200));

            idField = new JTextField(10);
            idField.setFont(new Font("Arial", Font.PLAIN, 16));

            JLabel idLabel = new JLabel("Enter Store ID:");
            idLabel.setFont(new Font("Arial", Font.PLAIN, 16));

            inputPanel.add(idLabel);
            inputPanel.add(idField);

            JButton showStoreBtn = new JButton("Show Store");
            showStoreBtn.setFont(new Font("Arial", Font.BOLD, 18));
            showStoreBtn.setPreferredSize(new Dimension(150, 50));
            showStoreBtn.addActionListener(e -> showStoreDetails());

            JPanel buttonHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonHolder.setOpaque(false);
            buttonHolder.add(showStoreBtn);

            JPanel topSection = new JPanel(new BorderLayout());
            topSection.setOpaque(false);
            topSection.add(inputPanel, BorderLayout.NORTH);
            topSection.add(buttonHolder, BorderLayout.CENTER);

            storeDisplayArea = new JTextArea(10, 40);
            storeDisplayArea.setEditable(false);
            storeDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
            JScrollPane scrollPane = new JScrollPane(storeDisplayArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 50));

            add(topSection, BorderLayout.CENTER);
            add(scrollPane, BorderLayout.SOUTH);
            add(controlPanel, BorderLayout.NORTH); // Adds the common control panel with the back button
        }

        private void showStoreDetails() {
            try {
                if (idField.getText().trim().isEmpty()) {
                    showError("Please enter a Store ID.");
                    storeDisplayArea.setText("");
                    return;
                }
                int id = Integer.parseInt(idField.getText());
                Store store = storeService.getStoreById(id);
                if (store != null) {
                    storeDisplayArea.setText(store.toString());
                } else {
                    storeDisplayArea.setText("Store not found with ID: " + id);
                    showError("Store not found with ID: " + id);
                }
            } catch (NumberFormatException ex) {
                showError("Invalid Store ID.");
                storeDisplayArea.setText("");
            } catch (Exception ex) {
                showError(ex.getMessage());
                storeDisplayArea.setText("");
            }
        }
    }

    // View All Stores Panel
    private class ViewAllStoresPanel extends StoreOperationPanel {
        private JTable table;
        private DefaultTableModel model;

        public ViewAllStoresPanel() {
            super();
            setLayout(new BorderLayout());
            JPanel tablePanel = new JPanel(new BorderLayout());
            tablePanel.setOpaque(false);
            tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));

            model = new DefaultTableModel(new String[]{"ID", "Name", "Address", "City", "Country", "Phone", "Active"}, 0) {
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
            tablePanel.add(scrollPane, BorderLayout.CENTER);

            // Add back button to the top of this panel
            JPanel topControl = new JPanel(new FlowLayout(FlowLayout.LEFT));
            topControl.setOpaque(false);
            topControl.add(backButton); // Uses the back button from the superclass
            add(topControl, BorderLayout.NORTH);

            add(tablePanel, BorderLayout.CENTER);

            refreshAll(); // Populates table on creation
        }

        private void refreshAll() {
            model.setRowCount(0);
            List<Store> allStores = storeService.getStores();
            for (Store s : allStores) {
                model.addRow(new Object[]{
                        s.getStoreId(), s.getStoreName(), s.getAddress(), s.getCity(), s.getCountry(), s.getPhone(), s.isActive()
                });
            }
        }
    }

    // Get Store as JSON Panel
    private class GetStoreAsJsonPanel extends StoreOperationPanel {
        private JTextArea jsonDisplayArea;

        public GetStoreAsJsonPanel() {
            super();
            JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
            inputPanel.setOpaque(false);
            inputPanel.setBorder(BorderFactory.createEmptyBorder(50, 200, 10, 200));

            idField = new JTextField(10);
            idField.setFont(new Font("Arial", Font.PLAIN, 16));

            JLabel idLabel = new JLabel("Store ID to View as JSON:");
            idLabel.setFont(new Font("Arial", Font.PLAIN, 16));

            inputPanel.add(idLabel);
            inputPanel.add(idField);

            JButton showJsonBtn = new JButton("Show JSON");
            showJsonBtn.setFont(new Font("Arial", Font.BOLD, 18));
            showJsonBtn.setPreferredSize(new Dimension(150, 50));
            showJsonBtn.addActionListener(e -> showStoreJson());

            JPanel buttonHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonHolder.setOpaque(false);
            buttonHolder.add(showJsonBtn);

            JPanel topSection = new JPanel(new BorderLayout());
            topSection.setOpaque(false);
            topSection.add(inputPanel, BorderLayout.NORTH);
            topSection.add(buttonHolder, BorderLayout.CENTER);

            jsonDisplayArea = new JTextArea(15, 50);
            jsonDisplayArea.setEditable(false);
            jsonDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
            JScrollPane scrollPane = new JScrollPane(jsonDisplayArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 50)); // Padding

            add(topSection, BorderLayout.CENTER);
            add(scrollPane, BorderLayout.SOUTH);
            add(controlPanel, BorderLayout.NORTH); // Adds the common control panel with the back button
        }

        private void showStoreJson() {
            try {
                if (idField.getText().trim().isEmpty()) {
                    showError("Please enter a Store ID to view JSON.");
                    jsonDisplayArea.setText("");
                    return;
                }
                int id = Integer.parseInt(idField.getText());
                String json = storeService.getStoreAsJson(id);
                if (json != null && !json.contains("Store not found")) { 
                    jsonDisplayArea.setText(json);
                } else {
                    jsonDisplayArea.setText("Store not found with ID: " + id);
                    showError("Store not found with ID: " + id);
                }
            } catch (NumberFormatException ex) {
                showError("Invalid Store ID.");
                jsonDisplayArea.setText("");
            } catch (Exception ex) {
                showError(ex.getMessage());
                jsonDisplayArea.setText("");
            }
        }
    }
}