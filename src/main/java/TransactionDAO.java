
/*  Connects Transaction.Java and TransactionService.java with the MySQL database.
 *  Handles: Inserting, Retrieving, Updating, Deleting products * * (CRUD).
*/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class TransactionDAO {
    
    /* Adds a transaction with the includes info into the database (tables transaction and includes)*/
    public void insertTransaction(Transaction transaction, List<Includes> includesList) {
        String insertTransactionSQL = "INSERT INTO transactions (transactionId, clientId, storeId, dateTime, paymentMethod, sumTotal, discount) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String insertIncludesSQL = "INSERT INTO includes (transactionId, productId, soldQuantity) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmtTransaction = conn.prepareStatement(insertTransactionSQL);
            PreparedStatement stmtIncludes = conn.prepareStatement(insertIncludesSQL)) {
                stmtTransaction.setLong(1, transaction.getTransactionId());
                stmtTransaction.setLong(2, transaction.getClientId());
                stmtTransaction.setLong(3, transaction.getStoreId());
                stmtTransaction.setTimestamp(4, Timestamp.valueOf(transaction.getDateTime()));
                stmtTransaction.setString(5, transaction.getPaymentMethod());
                stmtTransaction.setDouble(6, transaction.getSumTotal());
                stmtTransaction.setDouble(7, transaction.getDiscount());
                
                stmtTransaction.executeUpdate();

                for (Includes inc : includesList) {
                    stmtIncludes.setLong(1, inc.getTransactionId());
                    stmtIncludes.setLong(2, inc.getProductId());
                    stmtIncludes.setInt(3, inc.getSoldQuantity());
                    stmtIncludes.addBatch();
                }
                stmtIncludes.executeBatch();

                conn.commit();
                System.out.println("Transaction saved successfully!");
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Transaction rollback due to error: " + e.getMessage());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* Retrieves all transactions and returns them as a list */
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions";

        try (Connection conn = DatabaseConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                long transactionId = rs.getLong("transactionId");
                long clientId = rs.getLong("clientId");
                long storeId = rs.getLong("storeId");
                LocalDateTime dateTime = rs.getTimestamp("DateTime").toLocalDateTime();
                String paymentMethod = rs.getString("paymentMethod");
                double sumTotal = rs.getDouble("sumTotal");
                double discount = rs.getDouble("discount");

                Transaction transaction = new Transaction(clientId, storeId, sumTotal, discount, paymentMethod);
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    /* Retrieves all includes and returns them as a list */
    public List<Includes> getAllIncludes() {
        List<Includes> includesList = new ArrayList<>();
        String sql = "SELECT * FROM includes";

        try (Connection conn = DatabaseConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                long transactionId = rs.getLong("transactionId");
                long productId = rs.getLong("productId");
                int soldQuantity = rs.getInt("soldQuantity");

                includesList.add(new Includes(transactionId, productId, soldQuantity));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return includesList;
    }

    /* Retrieves transaction and includes info from the database for the transaction ID given.
     * Builds and returns info in JSON format. */
    public String getTransactionAsJson(long transactionId) {
        StringBuilder json = new StringBuilder();

        String sqlTransaction = "SELECT * FROM transaction WHERE transactionId = ?";
        try (Connection connTransaction = DatabaseConnector.getConnection();
        PreparedStatement stmTransaction = connTransaction.prepareStatement(sqlTransaction)) {
            
            stmTransaction.setLong(1, transactionId);
            ResultSet rsTransaction = stmTransaction.executeQuery();

            if (rsTransaction.next()) {
                json.append("{\n   \"transactionId\": ").append(transactionId);
                json.append("  \"clientId\": ").append(rsTransaction.getLong("clientId")).append(",\n");
                json.append("  \"storeId\": ").append(rsTransaction.getInt("storeId")).append(",\n");
                json.append("  \"dateTime\": ").append(rsTransaction.getTimestamp("dateTime").toLocalDateTime()).append("\",\n");
                json.append("  \"paymentMethod\": ").append(rsTransaction.getString("paymentMethod")).append(",\n");
                json.append("  \"sumTotal\": ").append(rsTransaction.getDouble("sumTotal")).append("\n}");
                json.append("  \"discount\": ").append(rsTransaction.getDouble("discount")).append("\n}");
                json.append(",\n   \"includesPerTransaction\": [\n");

                String sqlIncludes = "SELECT * FROM includes WHERE transactionId = ?";
                try (PreparedStatement stmtIncludes = connTransaction.prepareStatement(sqlIncludes)) {

                    stmtIncludes.setLong(1, transactionId);
                    ResultSet rs = stmtIncludes.executeQuery();

                    boolean first = true;
                    while (rs.next()) {
                        if (!first) json.append(",\n");
                        json.append("   {\n \"productId\": ").append(rs.getLong("productId"))
                        .append(", \"soldQuantity\": ").append(rs.getInt("soldQuantity }"));
                        first = false;
                    }
                }
            json.append("\n  ]\n}");
            } else {
                return "{}"; //No transaction found to display
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "{}";
        }
    return json.toString();
    }
}
