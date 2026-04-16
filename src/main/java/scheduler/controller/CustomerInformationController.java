package scheduler.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import scheduler.dao.AppointmentDao;
import scheduler.dao.CustomerDao;
import scheduler.model.Appointment;
import scheduler.model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerInformationController implements Initializable {

    public TableView<Customer> customerInformationTable;
    public TableColumn<Customer, Integer> customerIDCol;
    public TableColumn<Customer, String> customerNameCol;
    public TableColumn<Customer, String> addressCol;
    public TableColumn<Customer, String> postalCodeCol;
    public TableColumn<Customer, String> phoneNumberCol;
    public TableColumn<Customer, Integer> divisionIDCol;


    ObservableList<Customer> customer = FXCollections.observableArrayList();

    /**
     * This method initializes the controller class.
     * This method is called automatically after the fxml file has been loaded.
     * It populates the appointments table in the AppointmentScreen.fxml.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resource bundle to use for localizing the root object or null if the root object was not localized.
     */

    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            customerInformationTable.setItems(CustomerDao.getAllCustomers());

            customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
            postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
            divisionIDCol.setCellValueFactory(new PropertyValueFactory<>("divisionID"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * This method handles the add customer information button event.
     * It takes you to the AddCustomerScreen and allows you to add a new customer to the Customer information table.
     *
     * @param actionEvent The add customer button click event.
     * @throws IOException If an error occurs while loading the AddCustomerScreen data.
     */
    public void addCustomerButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/scheduler/AddCustomerScreen.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Return to Appointment screen");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * This method handles the modify customer button event.
     * It takes you to the ModifyCustomerScreen and allows you to modify an existing customers information in the customer information table.
     *
     * @param actionEvent The modify customer information button click event.
     * @throws IOException If the ModifyCustomerScreen.fxml file cannot be loaded.
     * @throws SQLException If an error occurs while loading the ModifyCustomerScreen data.
     */
    public void modifyCustomerButton(ActionEvent actionEvent) throws IOException, SQLException {
        Customer selectedCustomer = customerInformationTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scheduler/ModifyCustomerScreen.fxml"));
            Parent root = loader.load();

            ModifyCustomerController modifyCustomerController = loader.getController();

            modifyCustomerController.setSelectedCustomer(selectedCustomer);

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("You can modify Customer Information");
            stage.setScene(scene);
            stage.show();

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No Customer Selected");
            alert.setContentText("Please select a customer to Modify");
            alert.showAndWait();
        }
    }

    /**
     * This method handles the Main Menu button.
     * It navigates the user back to the Appointments screen.
     *
     * @param actionEvent The Main Menu button click event.
     * @throws IOException if the AppointmentScreen.fxml file cannot be loaded.
     * @throws SQLException If an error occurs while loading the AppointmentScreen data.
     */
    public void mainMenuButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/scheduler/AppointmentScreen.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Return to Appointment screen");
        stage.setScene(scene);
        stage.show();

    }
    /**
     * This method handles the delete customer button.
     * It allows you to delete a selected customer from the Customer Information table.
     * A pop-up will ask if your sure you want to delete.
     * If you click OK, the customer's information will be deleted.
     * If you click Cancel, the customer's information will not be deleted and no changes will be made.
     *
     * @param actionEvent The delete customer button click event.
     * @throws SQLException If an error occurs while loading the CustomerScreen data.
     */
    public void deleteCustomerButton(ActionEvent actionEvent) throws SQLException {
        Customer selectedCustomer = (Customer) customerInformationTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No Customer Selected");
            alert.setContentText("Please select a Customer to delete");
            alert.showAndWait();
            return;
            }

            ObservableList<Appointment> appointments = AppointmentDao.getAppointmentsByCustomer(selectedCustomer.getCustomerID());

            if (!appointments.isEmpty()) {
                Alert appointmentAlert = new Alert(Alert.AlertType.WARNING);
                appointmentAlert.setTitle("Warning");
                appointmentAlert.setHeaderText("Can't delete Customer " + selectedCustomer.getCustomerID());
                appointmentAlert.setContentText("Customer has associated appointments and cannot be deleted");
                appointmentAlert.showAndWait();
                return;
            }
            Alert ConformationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            ConformationAlert.setTitle("CONFIRMATION");
            ConformationAlert.setHeaderText("Delete");
            ConformationAlert.setContentText("Are you sure you want to delete this Customer's Information?");

            Optional<ButtonType> result = ConformationAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean success = CustomerDao.delete(selectedCustomer);
                if (success) {
                    customerInformationTable.getItems().remove(selectedCustomer);
                    System.out.println("Customer Information has been deleted");
                } else {
                    System.err.println("Customer information was not deleted.");
                }
            }
    }
}