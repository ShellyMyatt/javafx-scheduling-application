package scheduler.model;


public class FirstLevelDivision {
    private int divisionID;
    private String division;
    private int countryID;

    /**
     * Constructs a FirstLevelDivision object with the divisionID, division name and countryID.
     *
     * @param divisionID the first level divisions' ID.
     * @param division the division's name.
     * @param countryID the associated countryID.
     */
    public FirstLevelDivision(int divisionID, String division, int countryID) {
        this.divisionID = divisionID;
        this.division = division;
        this.countryID = countryID;
    }

    /**
     * Constructs a FirstLevelDivision object with the divisionId and name.
     *
     * @param divisionID the first level divisions' ID.
     * @param division the division's name.
     */
    public FirstLevelDivision(int divisionID, String division) {
        this.divisionID = divisionID;
        this.division = division;
    }

    public int getDivisionID() {
        return divisionID;
    }
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    public String getDivision() {
        return division;
    }
    public void setDivision(String division) {
        this.division = division;
    }

    public int getCountryID() {
        return countryID;
    }
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }


    @Override
    public String toString() {
        return this.division;
    }

}
