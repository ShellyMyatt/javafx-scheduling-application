package scheduler.dao;

import scheduler.helper.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDao {

    /**
     * This method adds a new customer into the database.
     *
     * @param customerName the customers name.
     * @param address the customers address
     * @param postalCode the customers postal code.
     * @param phone the customers phone number.
     * @param divisionID the customers divisionID
     * @throws SQLException if a database error occurs.
     */
  public static void addCustomer(String customerName, String address, String postalCode, String phone, int divisionID) throws SQLException {
    Connection connection = DBConnection.getConnection();
    if (connection == null) {
        throw new SQLException(" Data base connection is not working you can not add a customer");
    }
        String sql= "INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES (?,?,?,?,?)";

            try (PreparedStatement insertSqlStatement = DBConnection.getConnection().prepareStatement(sql)) {
                insertSqlStatement.setString(1, customerName);
                insertSqlStatement.setString(2, address);
                insertSqlStatement.setString(3, postalCode);
                insertSqlStatement.setString(4, phone);
                insertSqlStatement.setInt(5, divisionID);
                insertSqlStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    /**
     * This Method retrieves all customer information from the database.
     *
     * @return and ObservableList containing all the customer information in the database.
     * @throws SQLException if a database error occurs.
     */
    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM customers";

        try(PreparedStatement statement = DBConnection.getConnection().prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                int customerID = resultSet.getInt("Customer_ID");
                String customerName = resultSet.getString("Customer_Name");
                String address = resultSet.getString("Address");
                String postalCode = resultSet.getString("Postal_Code");
                String phone = resultSet.getString("Phone");
                int divisionID = resultSet.getInt("Division_ID");

                Customer customer = new Customer(customerID, customerName, address, postalCode, phone, divisionID);

                customerObservableList.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerObservableList;
    }

    /**
     * This method updates a customer's information in the database.
     *
     * @param updatedCustomerID the customer ID.
     * @param updatedCustomerName the updated customers name.
     * @param updatedAddress the updated customers address.
     * @param updatedPostalCode the updated customers postal code.
     * @param updatedPhone the updated customers phone number.
     * @param updatedDivisionID the updated customers divisionID.
     * @param countryID the associated countryID.
     * @throws SQLException if there is an issue with the SQL update query.
     */
    public static void update(int updatedCustomerID, String updatedCustomerName, String updatedAddress, String updatedPostalCode, String updatedPhone, int updatedDivisionID, int countryID) throws SQLException {

        String updateCustomer = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";
        try(PreparedStatement updateSqlStatement = DBConnection.getConnection().prepareStatement(updateCustomer)) {

            updateSqlStatement.setString(1, updatedCustomerName);
            updateSqlStatement.setString(2, updatedAddress);
            updateSqlStatement.setString(3, updatedPostalCode);
            updateSqlStatement.setString(4, updatedPhone);
            updateSqlStatement.setInt(5, updatedDivisionID);
            updateSqlStatement.setInt(6, updatedCustomerID);
            updateSqlStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error updating customer information:"+e.getMessage());

        }

    }

    /**
     * This Method deletes a customer from the MySQL database if it matches the Customer_ID.
     * Returns true if successfully deleted.
     *
     * @param customer the customer being deleted.
     * @return true if the customer was successfully deleted, false if not.
     * @throws SQLException if there is an issue with the database query.
     */
   public static boolean delete(Customer customer) throws SQLException {
        String sql = "DELETE FROM customers WHERE Customer_ID = ?";
        boolean deleted = false;

        try (PreparedStatement deleteSqlStatement = DBConnection.getConnection().prepareStatement(sql)) {
            deleteSqlStatement.setInt(1, customer.getCustomerID());
            deleteSqlStatement.executeUpdate();
            deleted = true;
        } catch (SQLException e) {
            System.err.println("Error deleting customer:"+e.getMessage());
        }
       return deleted;
   }

}




