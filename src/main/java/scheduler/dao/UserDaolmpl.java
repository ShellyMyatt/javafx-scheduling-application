package scheduler.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.helper.DBConnection;
import scheduler.model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class UserDaolmpl {

    /**
     * This Method retrieves all the user information from the database.
     *
     * @return an ObservableList of User objects containing information for the users in the database.
     */
    public static ObservableList<User> getAllUsers() {
        ObservableList<User> userObservableList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM users";

        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                int userID = resultSet.getInt("User_ID");
                String userName = resultSet.getString("User_Name");
                String password = resultSet.getString("Password");
                LocalDateTime createDate = resultSet.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = resultSet.getString("Created_By");
                LocalDateTime lastUpdate = resultSet.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = resultSet.getString("Last_Updated_By");

                User user = new User(userID, userName, password, createDate, createdBy, lastUpdate, lastUpdatedBy);

                userObservableList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userObservableList;
    }

}

