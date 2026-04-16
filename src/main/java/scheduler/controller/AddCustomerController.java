package scheduler.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import scheduler.dao.CountriesDao;
import scheduler.dao.CustomerDao;
import scheduler.dao.FirstLevelDivisionDao;
import scheduler.model.Countries;
import scheduler.model.FirstLevelDivision;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class AddCustomerController implements Initializable {
    public TextField addCustomerID;
    public TextField addCustomerName;
    public TextField addCustomerAddress;
    public TextField addCustomerPostalCode;
    public TextField addCustomerPhoneNumber;
    public ComboBox<Countries> modifyCustomerCountry;
    public ComboBox<FirstLevelDivision> modifyCustomerState;

    /**
     * Initializes the controller and sets up data for the country and first level division combo boxes.
     *
     * @param url The location used to resolve relative paths for the root object.
     * @param resources The resources used to localize the root object.
     */
    public void initialize(URL url, ResourceBundle resources) {
        try {
            modifyCustomerCountry.setItems(CountriesDao.getAllCountries());
            modifyCustomerState.setItems(null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * This method handles the save button action event.
     * If any fields are blank or entered incorrectly it will display an error message.
     *
     * @param actionEvent The event that triggers the save button.
     * @throws IOException If an error occurs while loading the CustomerScreen.
     */
    public void addCustomerSaveButton(ActionEvent actionEvent) throws IOException {
        String customerName = addCustomerName.getText();
        String address = addCustomerAddress.getText();
        String postalCode = addCustomerPostalCode.getText();
        String phone = addCustomerPhoneNumber.getText();
        FirstLevelDivision selectDivisionID = modifyCustomerState.getValue();

        if (customerName.isEmpty() || address.isEmpty() || postalCode.isEmpty() || phone.isEmpty() || selectDivisionID == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("All fields are required");
            alert.setContentText("Please fill out all the fields on the form.");
            alert.showAndWait();
            return;
        }

        int divisionID = selectDivisionID.getDivisionID();

        try {
            CustomerDao.addCustomer(customerName, address, postalCode, phone, divisionID);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Parent root = FXMLLoader.load(getClass().getResource("/scheduler/CustomerInformationScreen.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Return to Appointment screen");
        stage.setScene(scene);
        stage.show();

    }

    /** This method handles the cancel button.
     * When pressed it should send you back to the appointment screen.
     *
     * @param actionEvent The event that triggers the cancel button.
     * @throws IOException If an error occurs while loading the AppointmentScreen.
     */
    public void addCustomerCancelButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/scheduler/AppointmentScreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Return to Appointment screen");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * This method retrieves the selected country from the modifyCustomerCountry ComboBox.
     * Retrieves the list of first level divisions associated with the selected country from the database and fills in
     * the modifyCustomerState ComboBox.
     *
     * @param actionEvent Event triggered when a country is selected from the country ComboBox.
     * @throws SQLException If a database access error occurs when retrieving the first level divisions.
     */
    public void loadStatesForSelectedCountry(ActionEvent actionEvent) throws SQLException {
        Countries selectedCountry = modifyCustomerCountry.getValue();
        if (selectedCountry != null) {
            modifyCustomerState.setItems(FirstLevelDivisionDao.getDivisionsByCountryID(selectedCountry.getCountryID()));
        }
    }
}
