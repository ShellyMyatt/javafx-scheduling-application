package myatt.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import myatt.dao.*;
import myatt.model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static myatt.dao.CountriesDao.testQuery;

public class ModifyCustomerController implements Initializable {
    public TextField modifyCustomerID;
    public TextField modifyCustomerName;
    public TextField modifyCustomerAddress;
    public TextField modifyCustomerPostalCode;
    public TextField modifyCustomerPhoneNumber;
    public ComboBox<Countries> modifyCustomerCountry;
    public ComboBox<FirstLevelDivision> modifyCustomerState;
    private CountriesDao countriesDao = new CountriesDao();
    private FirstLevelDivisionDao firstLevelDivisionDao = new FirstLevelDivisionDao();
    private Customer selectedCustomer;

    /**
     * Initializes the modify customer information form with the customer data you selected.
     *
     * @throws SQLException
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            ObservableList<Countries> getAllCountries = CountriesDao.getAllCountries();
            modifyCustomerCountry.setItems(CountriesDao.getAllCountries());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //use to test if it is printing the country names
    public static void main(String[] args) {
        try {
            testQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the selected customer information into the modify customer form fields for modification.
     * This method fills the form with the information from the selected customer being modified.
     *
     * @param customer the customer being modified.
     */
    public void setSelectedCustomer(Customer customer) throws SQLException {
        this.selectedCustomer = customer;
        modifyCustomerID.setText(String.valueOf(customer.getCustomerID()));
        modifyCustomerName.setText(customer.getCustomerName());
        modifyCustomerAddress.setText(customer.getAddress());
        modifyCustomerPostalCode.setText(customer.getPostalCode());
        modifyCustomerPhoneNumber.setText(customer.getPhone());

        fillCountryComboBox();

        modifyCustomerCountry.getSelectionModel().select(getCountryByDivisionID(customer.getDivisionID()));
        loadStatesForSelectedCountry(null);
        modifyCustomerState.getSelectionModel().select(getDivisionByID(customer.getDivisionID()));
    }

    /**
     * Fills the Country combo box with all the countries from the database.
     * This method  retrieves a list of all countries from the database using the countriesDao and populates
     * the modifyCustomerCountry combo box with these countries.
     *
     * @throws SQLException if there is an issue accessing the database.
     */
    private void fillCountryComboBox() throws SQLException {
        ObservableList<Countries> countriesList = countriesDao.getAllCountries();
        modifyCustomerCountry.setItems(countriesList);
    }

    /**
     * Fills the state/providence combo box with the appropriate states/providences.
     * Based on what country you selected in the country combobox.
     * This method is triggered when a country is selected in the modifyCustomerCountry combo box.
     *
     * @param actionEvent the event triggered by selecting a country from the combo box.
     * @throws SQLException if there is an issue retrieving the state/province data from the database.
     */
    @FXML
    private void loadStatesForSelectedCountry(ActionEvent actionEvent) throws SQLException {
        Countries selectedCountry = modifyCustomerCountry.getValue();
            try {
                modifyCustomerState.setItems(FirstLevelDivisionDao.getDivisionsByCountryID(selectedCountry.getCountryID()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    /**
     * Retrieves the country associated with a specific divisionID.
     * This method fetches the country that corresponds to the selected divisionID using the countriesDao.
     *
     * @param divisionID the ID of the first level division from which the country is being retrieved.
     * @return the Countries object representing the country associated with the divisionID.
     * @throws SQLException if there is an issue retrieving the country data from the database.
     */
    private Countries getCountryByDivisionID(int divisionID) throws SQLException {
        return countriesDao.getCountriesByDivisionID(divisionID);
    }

    /**
     * Retrieves the division state/province associated with a specific divisionID.
     * This method fetches the state/province associated with the given divisionID using the FirstLevelDivisionDao.
     *
     * @param divisionID the division ID to be retrieved.
     * @return the FirstLevelDivision object associated with the given ID.
     * @throws SQLException if there is an issue retrieving the division data from the database.
     */
    private FirstLevelDivision getDivisionByID(int divisionID) throws SQLException {
        return FirstLevelDivisionDao.getDivisionByID(divisionID);
    }

    /**
     * This method handles the modify customer save button action event.
     * This method retrieves the updated customer information from the selected form field, validates the data, and updates
     * the customer record in the database.
     *
     * @param actionEvent the event that triggers the save button.
     */
        public void modifyCustomerSaveButton(ActionEvent actionEvent)  {
        try {
            int updatedCustomerID = Integer.parseInt(modifyCustomerID.getText());
            String updatedCustomerName = modifyCustomerName.getText();
            String updatedAddress = modifyCustomerAddress.getText();
            String updatedPostalCode = modifyCustomerPostalCode.getText();
            String updatedPhone = modifyCustomerPhoneNumber.getText();
            int updatedDivisionID = modifyCustomerState.getValue().getDivisionID();
            int countryID = modifyCustomerCountry.getValue().getCountryID();

            CustomerDao.update(updatedCustomerID, updatedCustomerName, updatedAddress, updatedPostalCode, updatedPhone, updatedDivisionID, countryID);

                Parent root = FXMLLoader.load(getClass().getResource("/myatt/CustomerInformationScreen.fxml"));
                Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("Return to customer information screen");
                stage.setScene(scene);
                stage.show();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Customer could not be updated");
            alert.showAndWait();
        }
    }
    /** This method handles the cancel button.
     * When pressed it should send you back to the Customer Information screen.
     *
     * @param actionEvent The event that triggers the cancel button.
     * @throws IOException If an error occurs while loading the CustomerInformationScreen.
     */
    public void modifyCustomerCancelButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/myatt/CustomerInformationScreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Return to Appointment screen");
        stage.setScene(scene);
        stage.show();
        
    }
}
