import javax.swing.*;
import java.awt.*;
import java.util.*;

public class TransactionFrame extends JFrame {
    public TransactionFrame() {
        setTitle("Transactions");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 1, 5, 5));

        JButton createBtn = new JButton("Create Transaction");
        JButton viewAllBtn = new JButton("View All Transactions");
        JButton jsonBtn = new JButton("Get Transaction as JSON");
        JButton closeBtn = new JButton("Close");

        createBtn.addActionListener(e -> {
            try {
                String input = JOptionPane.showInputDialog("Client email or phone:");
                Client client = ClientService.authenticateClient(clients, input);

                String storeIdStr = JOptionPane.showInputDialog("Store ID:");
                int storeId = Integer.parseInt(storeIdStr);

                List<Long> productIds = new ArrayList<>();
                List<Integer> quantities = new ArrayList<>();

                boolean more = true;
                while (more) {
                    String pid = JOptionPane.showInputDialog("Product ID:");
                    String qty = JOptionPane.showInputDialog("Quantity:");
                    productIds.add(Long.parseLong(pid));
                    quantities.add(Integer.parseInt(qty));

                    int option = JOptionPane.showConfirmDialog(this, "Add another product?", "Continue", JOptionPane.YES_NO_OPTION);
                    more = (option == JOptionPane.YES_OPTION);
                }

                String payment = JOptionPane.showInputDialog("Payment Method (Cash/Card/Credit):");

                Transaction t = TransactionService.createTransaction(
                        productIds, quantities, storeId, client, productService, stockService, payment
                );
                transactions.add(t);
                JOptionPane.showMessageDialog(this, "Transaction created:\n" + t);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        viewAllBtn.addActionListener(e -> {
            List<Transaction> txs = TransactionService.getAllTransactions();
            if (txs.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No transactions found.");
            } else {
                JOptionPane.showMessageDialog(this, txs.toString());
            }
        });

        jsonBtn.addActionListener(e -> {
            String tid = JOptionPane.showInputDialog("Transaction ID:");
            long id = Long.parseLong(tid);

            Transaction found = null;
            for (Transaction t : transactions) {
                if (t.getTransactionId() == id) {
                    found = t;
                    break;
                }
            }

            if (found == null) {
                JOptionPane.showMessageDialog(this, "Transaction not found.");
            } else {
                String json = TransactionService.getTransactionAsJson(found, TransactionService.getAllIncludes());
                JOptionPane.showMessageDialog(this, json);
            }
        });

        closeBtn.addActionListener(e -> dispose());

        add(createBtn);
        add(viewAllBtn);
        add(jsonBtn);
        add(closeBtn);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
