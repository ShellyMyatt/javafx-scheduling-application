package scheduler.dao;

import javafx.fxml.Initializable;
import scheduler.helper.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.model.Countries;
import scheduler.model.CountriesWithTotal;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CountriesDao implements Initializable {

    public static void testQuery() throws SQLException {
        String sql = "SELECT Country FROM countries";
        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int countryID = resultSet.getInt("Country_ID");
                String countryName = resultSet.getString("Country");

                System.out.println("Country ID: " + countryID + ", Country: " + countryName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This Method retrieves all the countries from the database.
     *
     * @return a ObservableList of Countries containing country ID's and names.
     * @throws SQLException if a database access error occurs.
     */
    public static ObservableList<Countries> getAllCountries() throws SQLException {
        ObservableList<Countries> countriesObservableList = FXCollections.observableArrayList();
        String sql = "SELECT Country_ID, Country FROM countries";

        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                int countryID = resultSet.getInt("Country_ID");
                String countryName = resultSet.getString("Country");

                Countries countryObject = new Countries(countryID, countryName);

                countriesObservableList.add(countryObject);
            }
            for (Countries country : countriesObservableList) {
                System.out.println(country.toString());
            }
        }
        return countriesObservableList;
    }

    /**
     * Retrieves a country associated with a divisionID.
     *
     * @param divisionID the divisionID to retrieve the associated country.
     * @return a Countries Object containing the country information.
     * @throws SQLException if a database error occurs.
     */
    public static Countries getCountriesByDivisionID(int divisionID) throws SQLException {
        Countries country = null;
        String sql = "SELECT c. * FROM countries c" + " JOIN first_level_divisions d ON c.Country_ID = d.Country_ID" + " WHERE d.Division_ID = ?";

        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(sql)) {
            statement.setInt(1, divisionID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                country = new Countries(resultSet.getInt("Country_ID"), resultSet.getString("Country"));
            }
        }
        return country;
    }

    /**
     * Retrieves the total number of appointments for each country.
     *
     * @return an ObservableList of CountriesWithTotal, containing the country name and total appointments.
     */
    public static ObservableList<Countries> getCustomersByCountry() {
        ObservableList<Countries> customersByCountry = FXCollections.observableArrayList();
        String sql = "SELECT countries.Country, COUNT(appointments.Appointment_ID) AS Total FROM appointments INNER JOIN customers ON appointments.Customer_ID =  customers.Customer_ID INNER JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID INNER JOIN countries ON first_level_divisions.Country_ID = countries.Country_ID GROUP BY countries.Country";

        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String country = resultSet.getString("Country");
                int total = resultSet.getInt("Total");

                System.out.println("Country:" + country + "Total Appointments: " + total);

                customersByCountry.add(new CountriesWithTotal(country, total));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customersByCountry;
    }

    /**
     * Initializes the controller.
     *
     * @param url the Location of the FXML file.
     * @param resourceBundle the resource used to Localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
