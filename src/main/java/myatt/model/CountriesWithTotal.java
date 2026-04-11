package myatt.model;

/**
 * Represents a country with the total number of appointments for the country.
 * This extends the Countries class and includes an additional field for the total.
 */
 public class CountriesWithTotal extends Countries {
    private int total;

    /**
     * Constructs a CountriesWithTotal object with country name and total.
     *
     * @param country the countries' name.
     * @param total the total number of appointments per country.
     */
    public CountriesWithTotal(String country, int total) {
        super(0, country); //Sets countryID to a default value
        this.total = total;
    }

    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return super.toString() + "(Total: " + total + ")";
    }

}
