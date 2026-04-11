package myatt.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import myatt.helper.DBConnection;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;



/** This is the class for the LoginController.*/
public class LoginController implements Initializable {


 @FXML
 private Label signInLabel;

 @FXML
 private Label usernameLabel;

 @FXML
 private TextField usernameTextField;

 @FXML
 private Label passwordLabel;

 @FXML
 private PasswordField passwordTextField;

 @FXML
 private Button logInButton;

 @FXML
 private Button resetButton;

 @FXML
 public TextField textFieldLocalTimeZone;

 private ResourceBundle resourceBundle;

 private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyy hh:mm a");


  /**
  * Initializes the controller class.
  * I used a lambda expression here for the reset button.
  * Using the lambda expression (actionEvent ->) it clears the username and password fields
  * when the reset button is clicked.
  * The Lambda expression is justified here because:
  * It is located with the UI initialization code.
  * If the logic for resetting the fields changes, it is simple to update the lambda without having to find the handler method.
  * Creating a method for clearing text fields add unnecessary complexity.
  * Using a lambda expression avoids an extra method and embeds the logic where it is needed.
  * Sets the language based on the computer setting.
  * Sets the login credentials to the test user.
  *
  * @param url the location used to resolve relative paths for the root object.
  * @param resourceBundle the resource used to localize the root object.
  */
 @Override
 public void initialize(URL url, ResourceBundle resourceBundle) {
  Locale locale = Locale.getDefault();
  this.resourceBundle = ResourceBundle.getBundle("Language", locale);
  signInLabel.setText(resourceBundle.getString("signInLabel"));
  usernameLabel.setText(resourceBundle.getString("usernameLabel"));
  passwordLabel.setText(resourceBundle.getString("passwordLabel"));
  logInButton.setText(resourceBundle.getString("logInButton"));
  resetButton.setText(resourceBundle.getString("resetButton"));
  textFieldLocalTimeZone.setText(resourceBundle.getString("timeZoneLabel") + ZoneId.systemDefault());

  // Lambda expression to clear the text fields when the reset button is clicked
  resetButton.setOnAction(actionEvent -> {
   usernameTextField.clear();
   passwordTextField.clear();
  });
 }

 /**
  * This method handles the login button.
  * This method retrieves the entered username and password, validates and authenticates the user in the database.
  * If authentication is successful the attempt is logged, then navigates to the appointment screen,
  * and displays a 15-minute appointment reminder.
  * If authentication fails it displays a pop-up error alert.
  *
  * @param actionEvent the Login Button click event
  * @throws IOException if an error occurs while navigating to the Appointment Screen
  * @throws SQLException if an error occurs during the database authentication
  */
 public void logInButtonOnAction(ActionEvent actionEvent) throws IOException, SQLException {
  String username = usernameTextField.getText();
  String password = passwordTextField.getText();

  if (username.isEmpty()) {
   showAlert(Alert.AlertType.WARNING, resourceBundle.getString("usernameRequirements"));
   return;
  }

  if (password.isEmpty()) {
   showAlert(Alert.AlertType.WARNING, resourceBundle.getString("passwordRequirements"));
   return;
  }

  boolean isAuthenticated = authenticate(username, password);
  logLoginAttempt(username, isAuthenticated);

  if (isAuthenticated) {
   showAlert(Alert.AlertType.CONFIRMATION, resourceBundle.getString("successfulLogIn"));

   Parent root = FXMLLoader.load(getClass().getResource("/myatt/AppointmentScreen.fxml"));
   Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
   Scene scene = new Scene(root);
   stage.setTitle("Appointment Screen");
   stage.setScene(scene);
   stage.show();

   getFifteenMinuteReminder();

  } else {
   showAlert(Alert.AlertType.ERROR, resourceBundle.getString("invalidLogIn"));
  }
 }

 public static void getFifteenMinuteReminder() throws SQLException {
  String sql = "SELECT Appointment_ID, Start FROM appointments WHERE Start BETWEEN NOW() AND ADDTIME(NOW(),'00:15:00') Limit 1";

  Connection connection = null;
          PreparedStatement statement = null;
  ResultSet resultSet = null;
  try {
   connection = DBConnection.getConnection();

   if (!connection.isValid(2)) {
    throw new SQLException("Connection is not valid");
   }
   statement = connection.prepareStatement(sql);
   resultSet = statement.executeQuery();

       if (!resultSet.isBeforeFirst()) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("No upcoming appointments");
        alert.setHeaderText(null);
        alert.setContentText("You have no appointments within the next 15 minutes.");
        alert.showAndWait();

       } else {
        while (resultSet.next()) {
         int appointmentID = resultSet.getInt("Appointment_ID");
         Timestamp startTimestamp = resultSet.getTimestamp("Start");
         LocalDateTime startTime = startTimestamp.toLocalDateTime();

         Alert alert = new Alert(Alert.AlertType.INFORMATION);
         alert.setTitle("Appointment Reminder");
         alert.setHeaderText("You have an appointment in the next 15 minutes. Appointment ID: " + appointmentID);
         alert.setContentText("Your appointment is scheduled at: " + startTime.toString());
         alert.showAndWait();
        }
       }
      } catch (SQLException e) {
       System.out.println("Error retrieving appointments: " + e.getMessage());
       throw e;
      }
 }

 /**
  * Authenticates the user's credentials by querying the database.
  * This method checks the username and password against the database. If they match then the user is authentic.
  *
  * @param username the username entered by the user
  * @param password the password entered by the user
  * @return {@code true} if the credentials are valid, or {@code false} if they are not valid
  */
  private boolean authenticate(String username, String password) {
   String query = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";

   Connection connection = null;
   PreparedStatement statement = null;
   ResultSet resultSet = null;
//keeps connection open
   try {
    connection = DBConnection.getConnection();
    statement = connection.prepareStatement(query);
    statement.setString(1, username);
    statement.setString(2, password);
    resultSet = statement.executeQuery();

    return resultSet.next();
   } catch (SQLException e) {
    System.out.println("ERROR could not authenticate" + e.getMessage());
    return false;
   } finally {
//closes resources here if needed
    try {
     if (resultSet != null) resultSet.close();
     if (statement != null) statement.close();
    } catch (SQLException e) {
     System.out.println("ERROR could not close" + e.getMessage());
    }
   }
  }

 /**
  * Logs the result of any  Login attempt to the login_activity.txt file.
  * This method logs the username, and if the login attempt was successful or failed.
  * It logs the timestamp of the attempt to the file named login_activity.txt.
  *
  * @param username the username entered during the login attempt
  * @param success {@code true} if the Login attempt was successful and {@code false} if the Login attempt failed
  */
  private void logLoginAttempt (String username,boolean success){
   String timestamp = LocalDateTime.now().format(formatter);
   String status = success ? resourceBundle.getString("successfulLogIn") : resourceBundle.getString("invalidLogIn");
   String logEntry = String.format("Username: %s, Status: %s, Timestamp: %s", username, status, timestamp);

   try (BufferedWriter writer = new BufferedWriter(new FileWriter("login_activity.txt", true))) {
    writer.write(logEntry);
    writer.newLine();
   } catch (IOException e) {
    System.err.println("ERROR failed to log login attempt" + e.getMessage());
   }
  }

 /**
  * Displays an alert with the type and content.
  * This method creates and shows an alert.
  *
  * @param alertType the type of alert
  * @param content the message being displayed in the alert
  */
  private void showAlert (Alert.AlertType alertType, String content){
   Alert alert = new Alert(alertType);
   alert.setContentText(content);
   alert.showAndWait();
  }
 }





