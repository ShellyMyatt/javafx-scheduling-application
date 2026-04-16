package scheduler.controller;

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
import scheduler.dao.AppointmentDao;
import scheduler.dao.CustomerDao;
import scheduler.dao.ContactsDao;
import scheduler.dao.UserDaolmpl;
import scheduler.helper.TimeConversions;
import scheduler.model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.*;


public class AddAppointmentController {


    @FXML
    private TextField addAppointmentID;
    @FXML
    private TextField addAppTitle;
    @FXML
    private TextField addAppDescription;
    @FXML
    private TextField addAppLocation;
    @FXML
    private ComboBox<Contacts> addAppContactsComboBox;
    @FXML
    private TextField addAppType;
    @FXML
    private DatePicker addAppStartDate;
    @FXML
    private ComboBox<LocalTime> addAppStartTime;
    @FXML
    private DatePicker addAppEndDate;
    @FXML
    private ComboBox<LocalTime> addAppEndTime;
    @FXML
    private ComboBox<Customer> addAppCustomerID;
    @FXML
    private ComboBox<User> addAppUserID;

    private CustomerDao customerDao = new CustomerDao();

    private UserDaolmpl userDaolmpl = new UserDaolmpl();

    private ContactsDao contactsDao = new ContactsDao();

    public void initialize() {
        try {
            fillComboBoxes();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fills customer, user, and contact combo boxes.
     * Fills start and end time combo boxes with 15 minute appointment slots during business hours from 8:00am to 10:00pm.
     * The display times are adjusted to the user's local time zone while maintaining the business hours in Eastern Time.
     *
     * @throws SQLException
     */
    private void fillComboBoxes() throws SQLException {
        ObservableList<Customer> customerObservableList = FXCollections.observableArrayList(customerDao.getAllCustomers());
        addAppCustomerID.setItems(customerObservableList);

        ObservableList<User> userObservableList = FXCollections.observableArrayList(userDaolmpl.getAllUsers());
        addAppUserID.setItems(userObservableList);

        ObservableList<Contacts> contactsObservableList = FXCollections.observableArrayList(contactsDao.getAllContacts());
        addAppContactsComboBox.setItems(contactsObservableList);

        addAppContactsComboBox.setCellFactory(contactsListView -> new ListCell<Contacts>() {
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
        addAppContactsComboBox.setButtonCell(new ListCell<Contacts>() {
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
        ZoneId systemZone = ZoneId.systemDefault();
        ZoneId easternZone = ZoneId.of("America/New_York");

        ZonedDateTime start = ZonedDateTime.of(LocalDate.now(), LocalTime.of(8,0), easternZone);
        ZonedDateTime end = ZonedDateTime.of(LocalDate.now(), LocalTime.of(22,0), easternZone);

        while (start.isBefore(end)) {
            LocalTime localTime = start.withZoneSameInstant(systemZone).toLocalTime();
            times.add(localTime);
            start = start.plusMinutes(15);
        }

//        ObservableList<LocalTime> times = FXCollections.observableArrayList();
//        LocalTime start = LocalTime.of(8, 0);
//        LocalTime end = LocalTime.of(22, 0);

//        while (start.isBefore(end)) {
//            times.add(start);
//            start = start.plusMinutes(15);
//        }
        addAppStartTime.setItems(times);
        addAppEndTime.setItems(times);
    }

    /**
     * This method handles the save button action event.
     * If any fields are blank or entered incorrectly it will display an error message.
     *
     * @param actionEvent The event that triggers the save button.
     * @throws IOException If an error occurs while loading the ApplicationScreen.
     */
    public void addAppSaveButton(ActionEvent actionEvent) throws IOException {
        String title = addAppTitle.getText();
        String description = addAppDescription.getText();
        String location = addAppLocation.getText();
        Contacts contacts = addAppContactsComboBox.getValue();
        String type = addAppType.getText();
        LocalDate startDate = addAppStartDate.getValue();
        LocalTime startTime = addAppStartTime.getValue();
        LocalDate endDate = addAppEndDate.getValue();
        LocalTime endTime = addAppEndTime.getValue();
        Customer customerID = addAppCustomerID.getValue();
        User userID = addAppUserID.getValue();

        if (title.isEmpty() || description.isEmpty() || location.isEmpty() || contacts == null || type.isEmpty() || startDate == null || startTime == null || endDate == null || endTime == null || customerID == null || userID == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("All fields are required");
            alert.setContentText("Please fill out all the fields on the form.");
            alert.showAndWait();
            return;
        }
        LocalDateTime start = LocalDateTime.of(startDate, startTime);
        LocalDateTime end = LocalDateTime.of(endDate, endTime);
        //Convert times to UTC for storage
//        LocalDateTime startUTC = TimeConversions.toUTC(startDate, startTime);
//        LocalDateTime endUTC = TimeConversions.toUTC(endDate, endTime);

        //Validate business hours are in eastern time
        if (!TimeConversions.isWithinBusinessHours(start.toLocalDate(), start.toLocalTime()) ||
            !TimeConversions.isWithinBusinessHours(end.toLocalDate(), end.toLocalTime())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Appointment time is outside the business hours of 8:00am and 10:00pm Eastern Time.");
            alert.showAndWait();
            return;
        }
        //Validate end time is after start time
        if (end.isBefore(start)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Invalid date");
            alert.setContentText("End time is before start time");
            alert.showAndWait();
            return;
        }
        //Validate overlapping appointments
            AppointmentDao appointmentDao = new AppointmentDao();
            ObservableList<Appointment> existingAppointments = appointmentDao.getAppointmentsByCustomer(customerID.getCustomerID());
            for (Appointment existingAppointment : existingAppointments) {
                LocalDateTime existingStartUTC = existingAppointment.getStart();
                LocalDateTime existingEndUTC = existingAppointment.getEnd();
                if (start.isBefore(existingEndUTC) && end.isAfter(existingStartUTC)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Overlapping appointment");
                    alert.setContentText("The selected time overlaps with another appointment. Please choose a different appointment time.");
                    alert.showAndWait();
                    return;
                }
            }

            Appointment newAppointment;
            newAppointment = new Appointment(
                    title,
                    description,
                    location,
                    contacts,
                    type,
                    start,
                    end,
                    customerID.getCustomerID(),
                    userID.getUserID());

            try {
                appointmentDao = new AppointmentDao();
                appointmentDao.addAppointment(newAppointment);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Appointment added");
                alert.setContentText("Appointment added successfully");
                alert.showAndWait();

                Parent root = FXMLLoader.load(getClass().getResource("/scheduler/AppointmentScreen.fxml"));
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("Return to Appointment screen");
                stage.setScene(scene);
                stage.show();

            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Database ERROR");
                alert.setHeaderText("Failed to add appointment");
                alert.setContentText("An error occurred while saving the appointment." + e.getMessage());
                alert.showAndWait();
            }
        }

    /** This method handles the cancel button.
     * When pressed it should send you back to the appointment screen.
     *
     * @param actionEvent The event that triggers the cancel button.
     * @throws IOException If an error occurs while loading the AppointmentScreen.
     */
    public void addAppCancelButton(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/scheduler/AppointmentScreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(parent);
        stage.setTitle("Return to Appointment screen.");
        stage.setScene(scene);
        stage.show();
    }

}
