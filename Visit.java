/**
 * Object for the visit of a pet to the vet.
 * Implements Comparable for sorting based on visit dates.
 */
public class Visit implements Comparable<Visit> {
    private int id; // used to store in text file to read in past visits and add to Pet as read in.
    private String date;
    private String time;
    private int animalID;
    private String details;

    /**
     * Main constructor
     * @param id int for the unique ID of the visit
     * @param date String for the date
     * @param time String for the time
     * @param animalID int for the animal ID to link this visit to
     * @param details String of the details about the visit
     */
    public Visit(int id, String date, String time, int animalID, String details) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.animalID = animalID;
        this.details = details;
    }

    /**
     * Returns the id of the visit
     * @return int for the id
     */
    public int getId() {
        return id;
    }

    /**
     * Changes the id
     * @param id int for new id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the date of the visit
     * @return String for the date
     */
    public String getDate() {
        return date;
    }

    /**
     * Changes the date of the visit
     * @param date String for the new date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Returns the time of the visit
     * @return String for the time
     */
    public String getTime() {
        return time;
    }

    /**
     * Changes the time of the visit
     * @param time String for the new time.
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Returns the animal ID that is linked to this visit
     * @return int for the animal ID
     */
    public int getAnimalID() {
        return animalID;
    }

    /**
     * Changes the ID for what animal this visit is linked to
     * @param animalID int for the animal ID
     */
    public void setAnimalID(int animalID) {
        this.animalID = animalID;
    }

    /**
     * Returns the details of the visit
     * @return String for the details
     */
    public String getDetails() {
        return details;
    }

    /**
     * Changes details of the visit
     * @param details String of the new details
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * Adds details to the end like once the vet/tech
     * see's the animal add their notes in.
     * @param details
     */
    public void addDetails(String details) {
        this.details += "\n" + details;
    }

    /**
     * Used on the animal itself so we don't need to see the animalID
     * as this get's printed on it.
     * @return of visit information
     */
    public String animalVisitString() {
        return " Time: " + time + "\nDetails:\n" + details;
    }

    /**
     * String output of the object
     * @return String version of object
     */
    public String toString() {
        return "ID: " + id + " - Date: " + date + " Time: " + time + " Animal ID: " + animalID + "\nDetails:\n" + details;
    }

    /**
     * This breaks the details down by new line and puts '~' in between to keep everything
     * on one line for printout back to a file.
     * @return String for the details printed out on one line
     */
    public String getDetailsFileOut() {
        String deets = "";
        for (String d : details.split("\n")) {
            deets += d + "~";
        }
        deets = deets.substring(0, deets.length() - 1); // remove the last ~
        return deets;
    }

    /**
     * Compares this Visit to another Visit based on their dates.
     * The date format is MM-DD-YYYY.
     * @param other the other Visit to compare to
     * @return -1 if this date is earlier, 1 if later, 0 if equal
     */
    @Override
    public int compareTo(Visit other) {
        String[] thisDateParts = this.date.split("-");
        String[] otherDateParts = other.date.split("-");

        // Compare year
        int thisYear = Integer.parseInt(thisDateParts[2]);
        int otherYear = Integer.parseInt(otherDateParts[2]);
        if (thisYear != otherYear) {
            return Integer.compare(thisYear, otherYear);
        }

        // Compare month
        int thisMonth = Integer.parseInt(thisDateParts[0]);
        int otherMonth = Integer.parseInt(otherDateParts[0]);
        if (thisMonth != otherMonth) {
            return Integer.compare(thisMonth, otherMonth);
        }

        // Compare day
        int thisDay = Integer.parseInt(thisDateParts[1]);
        int otherDay = Integer.parseInt(otherDateParts[1]);
        return Integer.compare(thisDay, otherDay);
    }
}

