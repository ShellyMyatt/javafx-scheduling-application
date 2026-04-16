package scheduler.model;


public class Countries {
    private int countryID;
    private String country;

    /**
     * Constructs a Countries object with a countryID and country name.
     *
     * @param countryID the countries' ID.
     * @param country the countries' name.
     */
    public Countries(int countryID, String country) {
        this.countryID = countryID;
        this.country = country;
    }

    public int getCountryID() {
        return countryID;
    }
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

@Override
    public String toString() {
        return this.country;
}

}
