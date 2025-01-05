import java.util.Scanner;

/**
 * UI file for console based running of this system.
 */

public class ScheduleSystem {
    private static Scanner input = new Scanner(System.in);
    private static VetDB db;
    private static String date;
    private static Queue<Visit> room1 = new Queue<>();
    private static Queue<Visit> room2 = new Queue<>();
    private static Queue<Visit> room3 = new Queue<>();

    /**
     * main method
     * @param args command line arguments
     */
    public static void main(String[] args) {
        System.out.println("*** Vet Schedule System ***");
        date = getDate("Please enter today's date(MM-DD-YYYY): ");

        // set up date file.
        db = new VetDB(date + ".txt");
        db.readDaySchedule();
        db.readPastVisits();
        db.readPets(); // this needs to be last for the past visits to appear in the pets

        int choice = 0;
        while (choice != 7) {
            System.out.println("** Main Menu **");
            System.out.println("1. View Today's Visits");
            System.out.println("2. Add Visit");
            System.out.println("3. Assign Next Visit to Room");
            System.out.println("4. Complete Visit");
            System.out.println("5. Search Pet");
            System.out.println("6. Add Pet");
            System.out.println("7. Exit");
            choice = getInt("Operation: ", 1, 7);
            System.out.println();
            switch (choice) {
                case 1 -> viewToday();
                case 2 -> addVisit();
                case 3 -> assignVisitToRoom();
                case 4 -> completeVisit();
                case 5 -> searchPet();
                case 6 -> addPet();
                case 7 -> exitSystem();
            }
            System.out.println();
        }
    }

    /**
     * YOUR TODO
     *
     * This method will loop through and print out the daily schedule from the DB object.
     *
     * You should print as the following format:
     * Animal ID - animal basic ID
     * animalVisitString() from the visit - extra new line
     *
     */
    private static void viewToday() {
        System.out.println("** Today's Schedule **");
        for (Visit visit : db.getDailySchedule().toList()) {
            Pet pet = db.getPet(visit.getAnimalID());
            if (pet != null) {
                System.out.println(visit.getAnimalID() + " - " + pet.basicPetString());
                System.out.println(visit.animalVisitString());
                System.out.println();
            }
        }
    }

    /**
     * This method will get information on a new visit to add into the system.
     */
    private static void addVisit() {
        System.out.println("** Add a Visit **");
        String vDate = getDate("Enter date(MM-DD-YYYY): ");
        System.out.print("Enter time: ");
        String time = input.nextLine();

        int animalID = getInt("Enter animal ID: ");
        // check if ID matches a valid pet in system
        while (db.getPet(animalID) == null) {
            System.out.println("No animal with that ID.");
            animalID = getInt("Enter animal ID: ");
        }

        System.out.print("Enter details of visit: ");
        String details = input.nextLine();

        Visit v = new Visit(db.getNextVisitID(), vDate, time, animalID, details);

        if (db.addVisit(v)) {
            System.out.println("Visit added.");
        }
        else {
            System.out.println("Error adding visit. No open time slot or open day. Try Again.");
        }

    }

    /**
     * YOUR TODO
     *
     * This method assigns the next visit in the daily schedule queue to the next available room.
     *
     * You should use room1, room2, and room3 as queues for the rooms and if they are empty add the next
     * Visit from the db object.  Note there is a getNextVisit() method on the DB object that dequeues and
     * sends it to you to enqueue onto the room
     *
     */
    private static void assignVisitToRoom() {
        if (db.getDailySchedule().isEmpty()) {
            System.out.println("No daily visits available.");
            return;
        }

        Visit nextVisit = db.getNextVisit();
        if (room1.isEmpty()) {
            room1.enqueue(nextVisit);
            System.out.println("* Assigned to Room 1 *");
        } else if (room2.isEmpty()) {
            room2.enqueue(nextVisit);
            System.out.println("* Assigned to Room 2 *");
        } else if (room3.isEmpty()) {
            room3.enqueue(nextVisit);
            System.out.println("* Assigned to Room 3 *");
        } else {
            System.out.println("Can't assign next visit as rooms are all full.");
            db.getDailySchedule().enqueue(nextVisit); // Re-add the visit back to the queue
        }
    }

    /**
     * This method will have you choose which room to "complete" the visit
     * Then add extra notes to the Visit and put on the completed stack
     * for the DB and on the Pet the visit was for.
     */
    private static void completeVisit() {
        Visit current = null;
        while (current == null) {
            System.out.println("** Complete Pet Visit **");
            System.out.println("Room 1");
            System.out.println("Room 2");
            System.out.println("Room 3");
            System.out.println("4 to Cancel");
            int choice = getInt("Room: ", 1, 4);

            if (choice == 4) {
                return; // don't do anymore checking.
            }

            if (choice == 1 && !room1.isEmpty()) {
                current = room1.dequeue();
            } else if (choice == 2 && !room2.isEmpty()) {
                current = room2.dequeue();
            } else if (choice == 3 && !room3.isEmpty()) {
                current = room3.dequeue();
            } else {
                System.out.println("Can't complete, no visit assigned to room.");
            }
        }

        // current should have a visit, add doc notes to it
        System.out.print("* Completing Visit *\n" + current.animalVisitString() + "\nEnter doctor notes: ");
        String notes = input.nextLine();

        current.addDetails(notes);

        // add to the completed list on db
        db.completeVisit(current);
    }

    /**
     * Searches for a pet using the db
     */
    private static void searchPet() {
        System.out.println("** Pet Search **");
        int id = getInt("Enter pet ID: ");
        Pet p = db.getPet(id);
        if (p == null) {
            System.out.println("Pet not found.");
        }
        else {
            System.out.println("* Pet Found *");
            System.out.println(p);
        }

    }

    /**
     * Get info for a new pet and attempts to add to the DB.
     */
    private static void addPet() {
        System.out.println("** Add a Pet **");
        int id = getInt("Enter animal ID: ");
        System.out.print("Enter pet name: ");
        String petName = input.nextLine();
        System.out.print("Enter pet type: ");
        String petType = input.nextLine();
        System.out.print("Enter pet subtype: ");
        String petSubtype = input.nextLine();
        System.out.print("Enter pet owner: ");
        String petOwner = input.nextLine();
        System.out.print("Enter owner address: ");
        String ownerAddress = input.nextLine();
        System.out.print("Enter owner contact: ");
        String ownerContact = input.nextLine();

        Pet p = new Pet(petName, petType, petSubtype, petOwner, ownerAddress, ownerContact);

        if (db.addPet(p, id)) {
            System.out.println("Pet added.");
        }
        else {
            System.out.println("Error adding pet.");
        }
    }

    /**
     * calls functions to save out information from the db.
     */
    private static void exitSystem() {
        // write out completed visits
        System.out.println("** System exiting **");
        db.writeOutPets();
        db.writeOutPastVisits();

    }

    /**
     * Gets a valid data in MM-DD-YYYY format. If not then ask again.
     * @param prompt
     * @return
     */
    private static String getDate(String prompt) {
        System.out.print(prompt);
        String d = input.nextLine();
        while (d.length() != 10 || (d.charAt(2) != '-' && d.charAt(5) != '-')) {
            System.out.print("Date not valid. " + prompt);
            d = input.nextLine();
        }

        return d;

    }

    /**
     * Helper method to get a valid integer
     * @param prompt String to print out for prompt
     * @return valid integer
     */
    private static int getInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(input.nextLine());
            }
            catch (NumberFormatException e) {
                System.out.println("Error, must have number input.");
            }
        }
    }

    /**
     * Returns a valid integer in the range sent
     * @param prompt String to print for prompt
     * @param min int for the lowest value in range
     * @param max int for the highest value in range
     * @return valid integer
     */
    private static int getInt(String prompt, int min, int max) {
        while(true) {
            int value = getInt(prompt);
            if (value < min || value > max) {
                System.out.println("Error, value must be between " + min + " and " + max);
                continue;
            }
            return value;
        }
    }

}

