import java.util.Stack;
import java.util.Collections;
import java.util.List;

/**
 * Pet class that holds all the information for the pet.
 */
public class Pet {
    // private int ID; // ID is also the key in the Map...do I need this here as well?
    private String name;
    private String type;
    private String subtype;
    private String owner;
    private String ownerAddress;
    private String ownerContact;
    Stack<Visit> pastVisits = new Stack<Visit>();

    /**
     * Main constructor
     * @param name String for pet's name
     * @param type String for the type of pet
     * @param subtype String for the subtype of the pet/breed
     * @param owner String for the owner name
     * @param ownerAddress String for the address of the owner
     * @param ownerContact String for the contact of the owner
     * @param pastVisits Stack of Visit objects
     */
    public Pet(String name, String type, String subtype, String owner, String ownerAddress, String ownerContact, Stack<Visit> pastVisits) {
        this.name = name;
        this.type = type;
        this.subtype = subtype;
        this.owner = owner;
        this.ownerAddress = ownerAddress;
        this.ownerContact = ownerContact;
        this.pastVisits = pastVisits;
    }

    /**
     * Constructor for new pet no visits
     * @param name String for the name of the pet
     * @param type String for the type of the pet
     * @param subtype String for the subtype of the pet
     * @param owner String for the owner of the pet
     * @param ownerAddress String for the owner's address
     * @param ownerContact String for the owner's contact information
     */
    public Pet(String name, String type, String subtype, String owner, String ownerAddress, String ownerContact) {
        this.name = name;
        this.type = type;
        this.subtype = subtype;
        this.owner = owner;
        this.ownerAddress = ownerAddress;
        this.ownerContact = ownerContact;
        pastVisits = new Stack<>(); // create new for new pet in system
    }

    /**
     * Returns the name of the pet
     * @return String for the name
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the the name of the pet
     * @param name String for the pet
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the type of the pet
     * @return String for the type
     */
    public String getType() {
        return type;
    }

    /**
     * Changes the type of the pet
     * @param type String for the new type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns the sub type of the pet
     * @return String of the subtype
     */
    public String getSubtype() {
        return subtype;
    }

    /**
     * Changes the subtype of the pet
     * @param subtype String for the new sub type
     */
    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    /**
     * Returns the owner of the pet
     * @return String of the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Changes the owner of the pet
     * @param owner String for the new owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Returns the owner address
     * @return String of the owner address
     */
    public String getOwnerAddress() {
        return ownerAddress;
    }

    /**
     * Changes the owner address
     * @param ownerAddress String for new owner address
     */
    public void setOwnerAddress(String ownerAddress) {
        this.ownerAddress = ownerAddress;
    }

    /**
     * Returns the owner contact
     * @return String for the contact
     */
    public String getOwnerContact() {
        return ownerContact;
    }

    /**
     * Changes the owner contact
     * @param ownerContact String for new owner contact
     */
    public void setOwnerContact(String ownerContact) {
        this.ownerContact = ownerContact;
    }

    /**
     * Returns past visits
     * @return Stack<Visit> of the past visits
     */
    public Stack<Visit> getPastVisits() {
        return pastVisits;
    }

    /**
     * Changes the past visit
     * @param pastVisits Stack<Visit> of new visits
     */
    public void setPastVisits(Stack<Visit> pastVisits) {
        this.pastVisits = pastVisits;
    }

    /**
     * Adds visit to the past visits.
     * @param visit Visit that are past ones.
     */
    public void addVisit(Visit visit) {
        pastVisits.push(visit);
    }

    /**
     * Sorts and returns the past visits for better display.
     * @return List<Visit> of sorted past visits
     */
    public List<Visit> getSortedPastVisits() {
        List<Visit> sortedVisits = new Stack<>();
        sortedVisits.addAll(pastVisits);
        Collections.sort(sortedVisits); // Requires Visit class to implement Comparable
        return sortedVisits;
    }

    /**
     * Basic printout of the pet for looking at the schedule.
     * @return String representation of basic pet information
     */
    public String basicPetString() {
        return "Name: " + name + " - Type: " + type + " - Subtype: " + subtype + " - Owner: " + owner;
    }

    /**
     * Printout of the past visits to print out
     * @return String of the visit prints
     */
    public String pastVisitsToString() {
        StringBuilder visits = new StringBuilder();
        for (Visit visit : getSortedPastVisits()) {
            visits.append("Visit: \n").append(visit.animalVisitString());
        }
        return visits.toString();
    }

    /**
     * Returns a string printout of the pet
     * @return String of the pet
     */
    public String toString() {
        return "Name: " + name + "\nType: " + type + " - Subtype: " + subtype + "\nOwner:   " + owner +
                "\nAddress: " + ownerAddress + "\nContact: " + ownerContact + "\n" + pastVisitsToString();
    }
}

