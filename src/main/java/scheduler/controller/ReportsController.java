package scheduler.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import scheduler.dao.AppointmentDao;
import scheduler.dao.ContactsDao;
import scheduler.dao.CountriesDao;
import scheduler.model.Appointment;
import scheduler.model.Contacts;
import scheduler.model.Countries;
import scheduler.model.Reports;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {

    public TableView<Appointment> tableviewAppointmentsByType;
    public TableColumn<Appointment, Integer> totalTypeCol;
    public TableColumn<Appointment, String>  typeOfAppointmentCol;
    public TableView<Reports> tableviewAppointmentsByMonth;
    public TableColumn<Appointment,Integer> totalMonthCol;
    public TableColumn<Appointment, String> monthOfAppointmentCol;
    public Tab tabContactSchedule;
    public ComboBox<Contacts> contactsComboBox;
    public TableView<Appointment> tableviewContactSchedule;
    public TableColumn<Appointment, Integer> reportAppointmentID;
    public TableColumn<Appointment, String> reportTitle;
    public TableColumn<Appointment, String> reportDescription;
    public TableColumn<Appointment, String> reportType;
    public TableColumn<Appointment, LocalDateTime> reportStart;
    public TableColumn<Appointment, LocalDateTime> reportEnd;
    public TableColumn<Appointment, Integer>  reportCustomer_ID;
    public Tab tabCustomerByCountry;
    public TableView<Countries> tableviewCustomersByCountry;
    public TableColumn<Countries, Integer>  totalCustomerCol;
    public TableColumn<Countries, String> countryCol;



    /**
     * Initializes the report screen.
     * It sets up the cell value factories for the columns in the report tables.
     *
     * @param url the location used to resolve relative paths for the root object.
     * @param resourceBundle the resource used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeOfAppointmentCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        totalTypeCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        getAppointmentsByType();

        monthOfAppointmentCol.setCellValueFactory(new PropertyValueFactory<>("reportMonth"));
        totalMonthCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        getAppointmentsByMonth();

        reportAppointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        reportTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        reportDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        reportType.setCellValueFactory(new PropertyValueFactory<>("type"));
        reportStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        reportEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        reportCustomer_ID.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        totalCustomerCol.setCellValueFactory(new PropertyValueFactory<>("total"));

        try {
            getCustomersByCountry();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            getContactsComboBox();
            contactsComboBox.setOnAction(event -> {
                try { getContactsSchedule();
            } catch (SQLException e) {
                    e.printStackTrace();
                }
                });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * retrieves all appointments by type from the database and populates the tableview.
     */
    public void getAppointmentsByType() {
        ObservableList<Appointment> appointmentsByType = AppointmentDao.getAppointmentsByType();
        tableviewAppointmentsByType.setItems(appointmentsByType);
    }

    /**
     * Retrieves all appointments by month from the database and populates the tableview.
     */
    public void getAppointmentsByMonth() {
        ObservableList<Reports> appointmentsByMonth = AppointmentDao.getAppointmentsByMonth();
        tableviewAppointmentsByMonth.setItems(appointmentsByMonth);
    }

    /**
     * Retrieves the schedule of appointments for the selected contact from the database and populates the tableview.
     *
     * @throws SQLException if an error occurs while accessing the database.
     */
    public void getContactsSchedule() throws SQLException {
        Contacts selectedContact = contactsComboBox.getValue();

        if (selectedContact != null) {
            int contactID = selectedContact.getContactID();
            ObservableList<Appointment> contactsSchedule =AppointmentDao.getContactsSchedule(contactID);
            tableviewContactSchedule.setItems(contactsSchedule);
        } else {
            System.out.println("No contact selected");
        }
    }

    /**
     * Populates the contacts combobox with a list of all available contacts from the database.
     *
     * @throws SQLException if an error occurs while accessing the database.
     */
    private void getContactsComboBox() throws SQLException {
        ObservableList<Contacts> contacts = ContactsDao.getAllContacts();
        contactsComboBox.setItems(contacts);
    }

    /**
     * Retrieves the customer information by country from the database and populates the table view.
     *
     * @throws SQLException if an error occurs while accessing the database.
     */
    private void getCustomersByCountry() throws SQLException {
        ObservableList<Countries> customersByCountry = CountriesDao.getCustomersByCountry();
        System.out.println("Countries data:" + customersByCountry);
        tableviewCustomersByCountry.setItems(customersByCountry);

    }

    /**
     * This method handles the Appointment Screen button.
     * It navigates the user back to the Appointments screen.
     *
     * @param actionEvent The appointment button click event.
     * @throws IOException if the AppointmentScreen.fxml file cannot be loaded.
     */
    public void appointmentsButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/scheduler/AppointmentScreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Return to Appointment screen");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * This method handles the Customer Information Screen button.
     * It navigates the user back to the Customer Information screen.
     *
     * @param actionEvent The customer information button click event.
     * @throws IOException if the CustomerInformationScreen.fxml file cannot be loaded.
     */
    public void customerButton(ActionEvent actionEvent) throws IOException {
        System.out.println("Customer Information Screen opened");
        Parent root = FXMLLoader.load(getClass().getResource("/scheduler/CustomerInformationScreen.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Customer Information Screen");
        stage.setScene(scene);
        stage.show();
    }
}
