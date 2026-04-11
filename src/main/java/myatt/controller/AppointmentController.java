package myatt.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import myatt.dao.AppointmentDao;
import myatt.helper.DBConnection;
import myatt.helper.TimeConversions;
import myatt.model.Appointment;
import myatt.model.Contacts;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class AppointmentController implements Initializable {

    public TableView<Appointment> appointmentsTable;
    public TableColumn<Appointment, Integer> appointmentIDCol;
    public TableColumn<Appointment, String> titleCol;
    public TableColumn<Appointment, String> descriptionCol;
    public TableColumn<Appointment, String> locationCol;
    public TableColumn<Appointment, String> contactsCol;
    public TableColumn<Appointment, String> typeCol;
    public TableColumn<Appointment, LocalDateTime> startDateTimeCol;
    public TableColumn<Appointment, LocalDateTime> endDateTimeCol;
    public TableColumn<Appointment, Integer> customerIDCol;
    public TableColumn<Appointment, Integer> userIDCol;
    public TableColumn<Appointment, Integer> contactIDCol;
    public ToggleGroup appointmentRadioButtons;
    public RadioButton allAppointmentsRadioButton;

    @FXML
    private RadioButton weeklyAppointmentsRadioButton;
    @FXML
    private RadioButton monthlyAppointmentsRadioButton;

    /**
     * This method navigates the user to the reports screen.
     *
     * @param actionEvent The event is triggered when the user clicks the Reports button.
     * @throws IOException is thrown if the ReportsScreen file can't be loaded.
     */
    public void reportsButton(ActionEvent actionEvent) throws IOException {
        System.out.println("Reports Screen opened");
        Parent root = FXMLLoader.load(getClass().getResource("/myatt/ReportsScreen.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Reports Screen");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method navigates the user to the customer information screen.
     *
     * @param actionEvent The event is triggered when the user clicks the Customer Information Screen button.
     * @throws IOException is thrown if the CustomerInformationScreen file can't be loaded.
     */
    public void viewCustomersButton(ActionEvent actionEvent) throws IOException {
        System.out.println("Customer Information Screen opened");
        Parent root = FXMLLoader.load(getClass().getResource("/myatt/CustomerInformationScreen.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Customer Information Screen");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method fetches all appointments from the database.
     * Populates the appointments table with the retrieved data.
     *
     * @param actionEvent is triggered when the user selects the option to view all appointments.
     */
    public void allAppointmentsOnAction(ActionEvent actionEvent) {
        try{
        ObservableList<Appointment> appointmentObservableList = AppointmentDao.getAllAppointments();
        appointmentsTable.setItems(appointmentObservableList);
    } catch (Exception e){
            System.out.println("Error selecting all appointments" + e.getMessage());
        }
    }

    /**
     * Displays the current week's appointments in the appointments table view.
     * This method fetches the current week's appointments.
     *
     * @param actionEvent is triggered when the user selects the Weekly Appointments radio button.
     */
    public void weekAppointmentsOnAction(ActionEvent actionEvent) {
        try {
            ObservableList<Appointment> weeklyAppointments = AppointmentDao.getWeeklyAppointments();
            appointmentsTable.setItems(weeklyAppointments);
        } catch (SQLException e) {
            System.err.println("Error selecting current week's appointments" + e.getMessage());
        }
    }
    /**
     * Displays the current month's appointments in the appointments table view.
     * This method fetches the current month's appointments.
     *
     * @param actionEvent is triggered when the user selects the Monthly Appointments radio button.
     */
    public void monthsAppointmentsOnAction(ActionEvent actionEvent) {
        try {
            ObservableList<Appointment> monthlyAppointments = AppointmentDao.getMonthlyAppointments();
            appointmentsTable.setItems(monthlyAppointments);
        } catch (SQLException e) {
            System.err.println("Error selecting current month's appointments" + e.getMessage());
        }
    }

    /**
     * This method initializes the controller class.
     * This method is called automatically after the fxml file has been loaded.
     * It populates the appointments table in the AppointmentScreen.fxml.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resource bundle to use for localizing the root object or null if the root object was not localized.
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = DBConnection.openConnection();
        try {
            appointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));

            contactsCol.setCellValueFactory(data -> {
                Appointment appointment = data.getValue();
                Contacts contacts = appointment.getContacts();
                return new SimpleStringProperty(contacts.getContactName() + "(ID: " + contacts.getContactID() + ", Email: " + contacts.getEmail() + ")");
            });

            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            startDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("start"));
            endDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("end"));
            customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            userIDCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
            appointmentsTable.setItems(AppointmentDao.getAllAppointments());

        } catch (SQLException e) {
            System.err.println("Error selecting current month's appointments" + e.getMessage());

        }

    }

    /**
     * This method handles the add appointment button event.
     * It takes you to the AddAppointmentScreen and allows you to add a new appointment to the Appointments table.
     *
     * @param actionEvent The add appointment button click event.
     * @throws IOException If an error occurs while loading the AddAppointmentScreen
     */
    public void addAppointmentButton(ActionEvent actionEvent) throws IOException {
        System.out.println("Appointment has been added");
        Parent root = FXMLLoader.load(getClass().getResource("/myatt/AddAppointmentScreen.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Add Appointment");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * This method handles the modify appointment button for the appointments table.
     * It will take you to a new screen that allows you to modify an appointment.
     * If no appointment is selected an alert will prompt you to first select an appointment to modify.
     *
     * @param actionEvent The modify appointment button click event.
     * @throws IOException If an error occurs while loading the ModifyAppointmentScreen
     */
    public void modifyAppointmentButton(ActionEvent actionEvent) throws IOException {
        Appointment selectModifyAppointment = (Appointment) appointmentsTable.getSelectionModel().getSelectedItem();

        if (selectModifyAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No Appointment Selected");
            alert.setContentText("Please select an appointment to Modify");
            alert.showAndWait();

        } else {
            System.out.println("Appointment has been modified");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/myatt/ModifyAppointmentScreen.fxml"));
            Parent root = loader.load();
            ModifyAppointmentController controller = loader.getController();
            controller.setSelectedAppointment(selectModifyAppointment);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Modify Appointment");
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * This method handles the delete appointment button.
     * It allows you to delete a selected appointment from the appointments table.
     * A pop-up will ask if your sure you want to delete.
     * If you click OK, the appointment will be deleted.
     * If you click Cancel, the appointment will not be deleted and no changes will be made.
     *
     * @param actionEvent The delete appointment button click event.
     */
    public void deleteAppointmentButton(ActionEvent actionEvent) throws SQLException {
        Appointment selectedAppointment = (Appointment) appointmentsTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No Appointment Selected");
            alert.setContentText("Please select an Appointment to delete");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("CONFIRMATION");
            alert.setHeaderText("Delete");
            alert.setContentText("Are you sure you want to delete this Appointment?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean success = AppointmentDao.delete(selectedAppointment);
                if (success) {
                    appointmentsTable.getItems().remove(selectedAppointment);
                    Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
                    informationAlert.setTitle("Appointment Deleted");
                    informationAlert.setHeaderText("Appointment Deleted successfully");
                    informationAlert.setContentText("Appointment ID: " + selectedAppointment.getAppointmentID() + "\nType: " + selectedAppointment.getType());
                    informationAlert.showAndWait();
                    System.out.println("Appointment has been deleted");
                } else {
                    System.err.println("Appointment not deleted");
                }
            }
        }
    }


        /**
         * This method handles the Sign-Out button
         * When the Sign-Out button is clicked, the user is signed out and exits the application.
         *
         * @param actionEvent The Sigh-Out button click event.
         */
        public void signOutButton(ActionEvent actionEvent){
            Platform.exit();
        }

}


