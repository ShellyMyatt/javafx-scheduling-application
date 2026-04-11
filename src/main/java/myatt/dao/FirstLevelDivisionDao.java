package myatt.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import myatt.helper.DBConnection;
import myatt.model.FirstLevelDivision;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FirstLevelDivisionDao {

    /**
     * This Method retrieves a list of the first level division information associated with a specific countryID from the database.
     *
     * @param countryID the countryID whose divisions are being retrieved.
     * @return an ObservableList containing FirstLevelDivision objects associated with the selected countryID.
     * @throws SQLException if an error occurs with the database.
     */
    public static ObservableList<FirstLevelDivision> getDivisionsByCountryID(int countryID) throws SQLException {
        ObservableList<FirstLevelDivision> divisionObservableList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = ? ";

        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(sql)) {
            statement.setInt(1, countryID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                int divisionID = resultSet.getInt("Division_ID");
                String divisionName = resultSet.getString("Division");
                countryID = resultSet.getInt("Country_ID");
                divisionObservableList.add(new FirstLevelDivision(divisionID, divisionName, countryID));
            }
        }
        System.out.println("Total divisions retrieved:" + divisionObservableList.size()); //debugging
        return divisionObservableList;
    }

    /**
     * Retrieves a specific first level division by its divisionID.
     *
     * @param divisionID the divisionID being retrieved.
     * @return a FirstLevelDivision object containing the division's information.
     * @throws SQLException if an error occurs with the database.
     */
    public static FirstLevelDivision getDivisionByID(int divisionID) throws SQLException {

        String sql = "SELECT Division_ID, Division FROM first_level_divisions WHERE Division_ID = ?";

        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(sql)) {
            statement.setInt(1, divisionID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int selectedDivisionID = resultSet.getInt("Division_ID");
                String divisionName = resultSet.getString("Division");

                FirstLevelDivision divisionByID = new FirstLevelDivision(selectedDivisionID, divisionName);
                return divisionByID;
            }
        }

        return null;
    }
}


