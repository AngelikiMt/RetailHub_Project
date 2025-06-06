
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
        String insertTransactionSQL = "INSERT INTO transaction (clientId, storeId, dateTime, paymentMethod, sumTotal, discount) VALUES (?, ?, ?, ?, ?, ?)";
        String insertIncludesSQL = "INSERT INTO includes (transactionId, productId, soldQuantity) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection()) {
            conn.setAutoCommit(false);
            try (
                    // Add RETURN_GENERATED_KEYS here
                    PreparedStatement stmtTransaction = conn.prepareStatement(insertTransactionSQL, PreparedStatement.RETURN_GENERATED_KEYS);
                    PreparedStatement stmtIncludes = conn.prepareStatement(insertIncludesSQL)
            ) {
                stmtTransaction.setLong(1, transaction.getClientId());
                stmtTransaction.setLong(2, transaction.getStoreId());
                stmtTransaction.setTimestamp(3, Timestamp.valueOf(transaction.getDateTime()));
                stmtTransaction.setString(4, transaction.getPaymentMethod());
                stmtTransaction.setDouble(5, transaction.getSumTotal());
                stmtTransaction.setDouble(6, transaction.getDiscount());

                int affectedRows = stmtTransaction.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Creating transaction failed, no rows affected.");
                }

                try (ResultSet generatedKeys = stmtTransaction.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long id = generatedKeys.getLong(1);
                        transaction.setTransactionId(id); // set the generated transactionId
                    } else {
                        throw new SQLException("Creating transaction failed, no ID obtained.");
                    }
                }

                for (Includes inc : includesList) {
                    stmtIncludes.setLong(1, transaction.getTransactionId());
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
                throw new RuntimeException("Failed to insert transaction: " + e.getMessage(), e);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection error: " + e.getMessage(), e);
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
                int storeId = rs.getInt("storeId");
                LocalDateTime dateTime = rs.getTimestamp("DateTime").toLocalDateTime();
                String paymentMethod = rs.getString("paymentMethod");
                double sumTotal = rs.getDouble("sumTotal");
                double discount = rs.getDouble("discount");

                transactions.add(new Transaction(transactionId, clientId, storeId, dateTime, paymentMethod, sumTotal, discount));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve all transactions: " + e.getMessage(), e);
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
            throw new RuntimeException("Failed to retrieve all includes: " + e.getMessage(), e);
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
                return "{}"; // No transaction found to display
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "{}";
        }
    return json.toString();
    }
}
