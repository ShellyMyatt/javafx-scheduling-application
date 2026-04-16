package scheduler.dao;

import scheduler.helper.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.model.Contacts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactsDao {

    /**
     * This Method retrieves all the contacts information from the database.
     *
     * @return an ObservableList of Contacts containing all the contact information in the database.
     */
    public static ObservableList<Contacts> getAllContacts()  {
        ObservableList<Contacts> contactsObservableList = FXCollections.observableArrayList();
        String sql = "SELECT Contact_ID, Contact_Name, Email FROM contacts";

        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                int contactID = resultSet.getInt("Contact_ID");
                String contactName = resultSet.getString("Contact_Name");
                String email = resultSet.getString("Email");

                Contacts contacts = new Contacts(contactID, contactName, email);

                contactsObservableList.add(contacts);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactsObservableList;
    }

    /**
     * Retrieves a contact from the database by the Contact_ID.
     *
     * @param contactID the ID of the contact being retrieved.
     * @return a Contacts object containing the contact's details.
     * @throws SQLException if a database access error occurs.
     */
    public static Contacts getContactByID(int contactID) throws SQLException {
            String sql = "SELECT * FROM contacts WHERE Contact_ID = ?";
            try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(sql)) {
                statement.setInt(1, contactID);
                ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        String contactName = resultSet.getString("Contact_Name");
                        String email = resultSet.getString("Email");
                        return new Contacts (contactID, contactName, email);
                    }
                } catch (SQLException e) {
                e.printStackTrace();
            }
        return null;
    }
}

