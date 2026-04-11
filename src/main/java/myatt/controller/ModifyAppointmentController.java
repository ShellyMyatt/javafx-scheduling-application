package myatt.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import myatt.dao.AppointmentDao;
import myatt.dao.ContactsDao;
import myatt.dao.CustomerDao;
import myatt.dao.UserDaolmpl;
import myatt.helper.TimeConversions;
import myatt.model.Appointment;
import myatt.model.Contacts;
import myatt.model.Customer;
import myatt.model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.time.*;

public class ModifyAppointmentController {


    private Appointment selectedAppointment;

    public TextField modifyAppointmentID;

    @FXML
    public TextField modifyAppTitle;
    @FXML
    public TextField modifyAppDescription;
    @FXML
    public TextField modifyAppLocation;
    @FXML
    public TextField modifyAppType;
    @FXML
    public DatePicker modifyAppStartDate;
    @FXML
    public ComboBox<LocalTime> modifyAppStartTime;
    @FXML
    public DatePicker modifyAppEndDate;
    @FXML
    public ComboBox<LocalTime> modifyAppEndTime;
    @FXML
    public ComboBox<Customer> modifyAppCustomerID;
    @FXML
    public ComboBox<User> modifyAppUserID;
    @FXML
    private myatt.dao.AppointmentDao appointmentDao;
    @FXML
    public ComboBox<Contacts> modifyAppContactsComboBox;

    private CustomerDao customerDao = new CustomerDao();
    private UserDaolmpl userDaolmpl = new UserDaolmpl();
    private ContactsDao contactsDao = new ContactsDao();


    public void initialize() {
        try {
            fillComboBoxes();
        } catch (SQLException e ) {
            e.printStackTrace();
        }
    }

    /**
     * Populates the CombBoxes in the modify appointment form with data from the database.
     * This method uses a Lambda expression to define a custom CellFactory for the modifyAppContactsComboBox.
     * The Lambda expression is justified here because:
     * It allows customization of how the contact items are displayed.
     * It eliminates the need for lengthy anonymous inner class implementation.
     * It enhances readability.
     * It reduces boilerplate code.
     *
     * @throws SQLException if a database access error occurs
     */
    private void fillComboBoxes() throws SQLException{
        ObservableList<Customer> customerObservableList = customerDao.getAllCustomers();
        modifyAppCustomerID.setItems(customerObservableList);

        ObservableList<User> userObservableList = userDaolmpl.getAllUsers();
        modifyAppUserID.setItems(userObservableList);

        ObservableList<Contacts> contactsObservableList = contactsDao.getAllContacts();
        modifyAppContactsComboBox.setItems(contactsObservableList);

// Lambda expression is used to set a custom CellFactory for the modifyAppContactsComboBox
        modifyAppContactsComboBox.setCellFactory(contactsListView -> new ListCell<Contacts>() {
            @Override
            protected void updateItem(Contacts contacts, boolean empty) {
                super.updateItem(contacts, empty);
                if (empty || contacts == null) {
                    setText(null);
                } else {
                    setText(contacts.getContactName());
                }
            }
        });
        modifyAppContactsComboBox.setButtonCell(new ListCell<Contacts>() {
            @Override
            protected void updateItem(Contacts contacts, boolean empty) {
                super.updateItem(contacts, empty);
                if (empty || contacts == null) {
                    setText(null);
                } else {
                    setText(contacts.getContactName());
                }
            }
        });

        ObservableList<LocalTime> times = FXCollections.observableArrayList();
        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(22, 0);

        while (start.isBefore(end)) {
            times.add(start);
            start = start.plusMinutes(15);
        }
        modifyAppStartTime.setItems(times);
        modifyAppEndTime.setItems(times);
    }

    /**
     * Sets the selected appointment details in the form fields for modification.
     * This method fills the form with the information from the selected appointment being modified.
     *
     * @param appointment the appointment being modified.
     */
    public void setSelectedAppointment(Appointment appointment) {
        this.selectedAppointment = appointment;
        modifyAppointmentID.setText(String.valueOf(appointment.getAppointmentID()));
        modifyAppTitle.setText(selectedAppointment.getTitle());
        modifyAppDescription.setText(selectedAppointment.getDescription());
        modifyAppLocation.setText(selectedAppointment.getLocation());
        modifyAppType.setText(selectedAppointment.getType());
        modifyAppStartDate.setValue(selectedAppointment.getStart().toLocalDate());
        modifyAppStartTime.setValue(selectedAppointment.getStart().toLocalTime());
        modifyAppEndDate.setValue(selectedAppointment.getEnd().toLocalDate());
        modifyAppEndTime.setValue(selectedAppointment.getEnd().toLocalTime());


        for (Customer customer : modifyAppCustomerID.getItems()) {
            if (customer.getCustomerID() == appointment.getCustomerID()) {
                modifyAppCustomerID.setValue(customer);
                break;
            }
        }
        for (User user : modifyAppUserID.getItems()) {
            if (user.getUserID() == appointment.getUserID()) {
                modifyAppUserID.setValue(user);
                break;
            }
        }
        for (Contacts contacts : modifyAppContactsComboBox.getItems()) {
            if (contacts.getContactID() == appointment.getContactID()) {
                modifyAppContactsComboBox.setValue(contacts);
                break;
            }
        }
    }

    /**
     * This method handles the save button action event.
     * This method validates the form fields and checks for overlapping appointments.
     * It ensures the appointment is with in business hours, and updates the modified appointment details in the database.
     *
     * @param actionEvent the event that triggers the save button.
     * @throws IOException if an error occurs while loading the appointment screen.
     */
    public void modifyAppSaveButton(ActionEvent actionEvent) throws IOException {
        try {
            String title = modifyAppTitle.getText();
            String description = modifyAppDescription.getText();
            String location = modifyAppLocation.getText();
            Contacts contacts = modifyAppContactsComboBox.getValue();
            String type = modifyAppType.getText();
            LocalDate startDate = modifyAppStartDate.getValue();
            LocalTime startTime = modifyAppStartTime.getValue();
            LocalDate endDate = modifyAppEndDate.getValue();
            LocalTime endTime = modifyAppEndTime.getValue();
            Customer customerID = modifyAppCustomerID.getValue();
            User userID = modifyAppUserID.getValue();

            if (title.isEmpty() || description.isEmpty() || location.isEmpty() || contacts == null || type == null || startDate == null || startTime == null || endDate == null || endTime == null || customerID == null || userID == null) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("All fields are required");
                alert.setContentText("Please fill out all fields on the form.");
                alert.showAndWait();
                return;
            }
            LocalDateTime start = LocalDateTime.of(startDate, startTime);
            LocalDateTime end = LocalDateTime.of(endDate, endTime);
//            // Converts times to UTC for storing
//            LocalDateTime startUTC = TimeConversions.toUTC(startDate, startTime);
//            LocalDateTime endUTC = TimeConversions.toUTC(endDate, endTime);

            // Validate business hours are in eastern time
            if (!TimeConversions.isWithinBusinessHours(startDate, startTime) ||
                !TimeConversions.isWithinBusinessHours(endDate, endTime)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Appointment time is outside the business hours of 8:00am and 10:00pm Eastern Time.");
                alert.showAndWait();
                return;
            }

            //Validate  end time is after start time
            if (end.isBefore(start)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Invalid date");
                alert.setContentText("End time is before start time");
                alert.showAndWait();
                return;
            }

            ObservableList<Appointment> existingAppointments = appointmentDao.getAppointmentsByCustomer(customerID.getCustomerID());
            for (Appointment existingAppointment : existingAppointments) {
                if (existingAppointment.getAppointmentID() == selectedAppointment.getAppointmentID()) {
                    continue;
                }
                if (start.isBefore(existingAppointment.getEnd()) && end.isAfter(existingAppointment.getStart())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Overlapping appointment");
                    alert.setContentText("The selected time overlaps with another appointment. Please choose a different appointment time.");
                    alert.showAndWait();
                    return;
                }
            }

            int appointmentID = selectedAppointment.getAppointmentID();
            Appointment updatedAppointment = new Appointment(
                    appointmentID,
                    title,
                    description,
                    location,
                    contacts,
                    type,
                    start,
                    end,
                    customerID.getCustomerID(),
                    userID.getUserID());

            AppointmentDao appointmentDao = new AppointmentDao();

            boolean valid = appointmentDao.update(updatedAppointment);

            if (valid) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Success");
                alert.setContentText("Appointment successfully updated.");
                alert.showAndWait();

                Parent root = FXMLLoader.load(getClass().getResource("/myatt/AppointmentScreen.fxml"));
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("Return to Appointment screen");
                stage.setScene(scene);
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Error");
                alert.setContentText("Appointment update failed.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** This method handles the cancel button.
     * When pressed it should send you back to the appointment screen.
     *
     * @param actionEvent The event that triggers the cancel button.
     * @throws IOException If an error occurs while loading the AppointmentScreen.
     */
    public void modifyAppCancelButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/myatt/AppointmentScreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Return to Appointment screen");
        stage.setScene(scene);
        stage.show();
    }
}
