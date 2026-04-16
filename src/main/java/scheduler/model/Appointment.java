package scheduler.model;

import java.time.LocalDateTime;

public class Appointment {

private int appointmentID;
private String title;
private String description;
private String location;
private Contacts contacts;
private String type;
private LocalDateTime start;
private LocalDateTime end;
private int customerID;
private int userID;

private int total;
private String reportMonth;

/**
 * Constructs an appointment with all the fields specified.
 *
 * @param appointmentID the ID for the appointment.
 * @param title the appointment title.
 * @param description the appointment description.
 * @param location the appointment location.
 * @param contacts the appointment's associated contacts.
 * @param type the appointment type.
 * @param start the appointment start date and time.
 * @param end the appointment end date and time.
 * @param customerID the appointment's associated customers.
 * @param userID the userID of the user who created the appointment.
 */

public Appointment(int appointmentID, String title, String description, String location, Contacts contacts, String type, LocalDateTime start, LocalDateTime end, int customerID, int userID) {
    this.appointmentID = appointmentID;
    this.title = title;
    this.description = description;
    this.location = location;
    this.contacts = contacts;
    this.type = type;
    this.start = start;
    this.end = end;
    this.customerID = customerID;
    this.userID = userID;
}

    /**
     * Constructor without an appointmentID for when its auto generated.
     *
     * @param title the appointment title.
     * @param description the appointment description.
     * @param location the appointment location.
     * @param contacts the appointment's associated contacts.
     * @param type the appointment type.
     * @param start the appointment start date and time.
     * @param end the appointment end date and time.
     * @param customerID the appointment's associated customers.
     * @param userID the userID of the user who created the appointment.
     */
    public Appointment(String title, String description, String location, Contacts contacts, String type, LocalDateTime start, LocalDateTime end, int customerID, int userID) {
    this.title = title;
    this.description = description;
    this.location = location;
    this.contacts = contacts;
    this.type = type;
    this.start = start;
    this.end = end;
    this.customerID = customerID;
    this.userID = userID;
}

    /**
     * Constructor where only fields associated with customers are needed for the reports.
     *
     * @param appointmentID the ID for the appointment.
     * @param title the appointment title.
     * @param description the appointment description.
     * @param type the appointment type.
     * @param start the appointment start date and time.
     * @param end the appointment end date and time.
     * @param customerID the appointment's associated customers.
     */
    public Appointment(int appointmentID, String title, String description, String type, LocalDateTime start, LocalDateTime end, int customerID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
    }

    /**
     * Constructor for the reports where only the total and type are needed.
     *
     * @param total the total number of appointments per type.
     * @param type the type of appointment being counted.
     */
    public Appointment(int total,String type) {
    this.total = total;
    this.type = type;
    
    }

public int getContactID() {
    return contacts.getContactID();
}

public Contacts getContacts() {
    return contacts;
}
public void setContacts(Contacts contacts) {
    this.contacts = contacts;
}

    public int getAppointmentID() {
        return appointmentID;
}
public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
}

public String getTitle() {
        return title;
}
public void setTitle(String title) {
        this.title = title;
}

public String getDescription() {
        return description;
}
public void setDescription(String description) {
        this.description = description;
}

public String getLocation() {
        return location;
}
public void setLocation(String location) {
        this.location = location;
}

public String getType() {
        return type;
}
public void setType(String type) {
        this.type = type;
}

public LocalDateTime getStart() {
        return start;
}
public void setStart(LocalDateTime start) {
        this.start = start;
}

public LocalDateTime getEnd() {
        return end;
}
public void setEnd(LocalDateTime end) {
        this.end = end;
}

public int getCustomerID() {
        return customerID;
}
public void setCustomerID(int customerID) {
        this.customerID = customerID;
}

public int getUserID() {
        return userID;
}
public void setUserID(int userID) {
        this.userID = userID;
}

public int getTotal() {
    return total;
}
public void setTotal(int total) {
    this.total = total;
}
public String getReportMonth() {
    return reportMonth;
}
public void setReportMonth(String reportMonth) {
    this.reportMonth = reportMonth;
}

}
