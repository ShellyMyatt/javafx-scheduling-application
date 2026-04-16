package scheduler.model;


public class Customer {

    private int customerID;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private int divisionID;

    /**
     * Constructs a Customer object with all the customer information.
     *
     * @param customerID the customers ID.
     * @param customerName the customers name.
     * @param address the customers address.
     * @param postalCode the customers postal code.
     * @param phone the customers phone number.
     * @param divisionID the divisionID associated with the customer.
     */
    public Customer(int customerID, String customerName, String address, String postalCode, String phone, int divisionID) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionID = divisionID;
    }

    /**
     * Constructs a Customer object with a customerID and customer name.
     *
     * @param selectedCustomerID the customerID.
     * @param customerName the customers name.
     */
    public Customer(int selectedCustomerID, String customerName) {
        this.customerID = selectedCustomerID;
        this.customerName = customerName;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getDivisionID() {
        return divisionID;
    }
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    @Override
    public String toString() {
        return String.valueOf(this.customerID);
    }

}
