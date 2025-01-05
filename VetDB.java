import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


/**
 * The DB class that holds the Pet list, daily schedule and all past visits. Does all the
 * operations on those Collections that we need.
 */
public class VetDB {
    private HashMap<Integer, Pet> petList = new HashMap<>();
    private Queue<Visit> dailySchedule = new Queue<>();
    private ArrayList<Visit> pastVisitList = new ArrayList<>();
    private int nextVisitID = -1;
    private String petFileName = "PetList.txt";
    private String currentDateFile;
    private String visitFileName = "CompletedVisit.txt";

    /**
     * Main constructor takes on the date for the file.
     * @param currentDateFile String of the daily schedule file to open and read.
     */
    public VetDB(String currentDateFile) {
        this.currentDateFile = currentDateFile;
    }

    /**
     * Sends the pet list back
     * @return HashMap<Integer, Pet> of the pet list.
     */
    public HashMap<Integer, Pet> getPetList() {
        return petList;
    }

    /**
     * Sends the daily schedule queue back.
     * @return Queue<Visit> for the daily schedule.
     */
    public Queue<Visit> getDailySchedule() {
        return dailySchedule;
    }

    /**
     *
     * @return
     */
    public ArrayList<Visit> getPastVisitList() {
        return pastVisitList;
    }

    /**
     * returns the nextVisitID which is when adding a visit the unique ID number.
     * @return int for the ID.
     */
    public int getNextVisitID() {
        return nextVisitID;
    }

    /**
     * Dequeues the next visit from daily schedule and returns.
     * @return Visit of the next in the queue
     */
    public Visit getNextVisit() {
        return dailySchedule.dequeue();
    }

    /**
     * Reads in the pets from file.
     */
    public void readPets() {
        try (BufferedReader in = new BufferedReader(new FileReader(petFileName))) {
            String line = in.readLine(); // reads header
            line = in.readLine(); // reads first pet
            while (line != null) {
                String[] data = line.split("\t");
                int id = Integer.parseInt(data[0]);
                Pet p = new Pet(data[1], data[2], data[3], data[4], data[5], data[6]);
                for (Visit past : pastVisitList) {
                    if (past.getAnimalID() == id) {
                        p.addVisit(past);
                    }
                }

                Collections.sort(p.getPastVisits()); // sort visits since stack to see newest to oldest

                petList.put(id, p); // add to map

                line = in.readLine(); // read next line
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Pet file not found");
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid pet id in file. Fix before running again.");
        }
        catch(IOException e) {
            System.out.println("Error reading pet file");
        }
    }

    /**
     * Writes out the pet list to file.
     */
    public void writeOutPets() {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(petFileName)))) {
            out.println("ID\tName\tType\tSubtype\tOwner\townerAddress\townerContact");
            for (var entry : petList.entrySet()) {
                Pet p = entry.getValue();
                out.println(entry.getKey() + "\t" + p.getName() + "\t" + p.getType() + "\t" + p.getSubtype() + "\t" +
                        p.getOwner() + "\t" + p.getOwnerAddress() + "\t" + p.getOwnerContact());
            }
        }
        catch (IOException e) {
            System.out.println("Error writing pet file");
        }
    }

    /**
     * Reads in the daily schedule and adds to the queue.
     */
    public void readDaySchedule() {
        try (BufferedReader in = new BufferedReader(new FileReader(currentDateFile))) {
            String date = currentDateFile.substring(0, currentDateFile.lastIndexOf(".")); // used for the visit and eliminate the .txt
            String line = in.readLine(); // read header
            line = in.readLine(); // read first line
            while (line != null) {
                String[] data = line.split("\t");

                String details = "";
                for (String d : data[3].split("~")) {
                    details += d + "\n";
                }
                details = details.substring(0, details.lastIndexOf("\n")); // take out last new character line

                Visit v = new Visit(Integer.parseInt(data[0]), date, data[1], Integer.parseInt(data[2]), details);

                dailySchedule.enqueue(v); // add to the queue

                line = in.readLine(); // read next line
            }

        }
        catch (FileNotFoundException e) {
            System.out.println("Schedule file not found, will create a new one upon saving out.");
            // get the previous day's visitID for writing?
        }
        catch(IOException e) {
            System.out.println("Error reading schedule file");
        }
    }

    /**
     * Reads in the past visits
     * Uses '~' as new line marker on details of the visit.
     */
    public void readPastVisits() {
        try (BufferedReader in = new BufferedReader(new FileReader(visitFileName))) {
            String line = in.readLine(); //read header
            line = in.readLine(); // read first line
            while (line != null) {
                String[] data = line.split("\t");
                if (data.length == 1) { // last line has the next visit number and no other data
                    nextVisitID = Integer.parseInt(data[0]);
                }
                else {
                    String details = "";
                    for (String d : data[4].split("~")) {
                        details += d + "\n";
                    }
                    details = details.substring(0, details.length() - 1); // take out last new character.
                    Visit v = new Visit(Integer.parseInt(data[0]), data[1], data[2], Integer.parseInt(data[3]), details);

                    pastVisitList.add(v); // add to the stack
                }

                line = in.readLine(); // read next visit
            }
            Collections.sort(pastVisitList); // sort as most likely it is backwards
        }
        catch (FileNotFoundException e) {
            System.out.println("Visit file not found");
        }
        catch(IOException e) {
            System.out.println("Error reading visit file");
        }
    }

    /**
     * Writes out the past visit stack to the file.
     * Uses '~' as new line marker in the details.
     */
    public void writeOutPastVisits() {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(visitFileName)))) {
            out.println("ID\tDate\tTime\tAnimal ID\tdetails");
            for (Visit v : pastVisitList) {
                out.println(v.getId() + "\t" + v.getDate() + "\t" + v.getTime() + "\t" + v.getAnimalID() + "\t" + v.getDetailsFileOut());
                out.println(nextVisitID); // this is the next visit ID for adding gotta save it somewhere.
            }
        }
        catch (IOException e) {
            System.out.println("Error writing visit file");
        }
    }

    /**
     * YOUR TODO
     *
     * You need to check to make sure that the pet isn't already in the HashMap via the id sent. If it is
     * return false, otherwise use put() to add into the petList.
     *
     * Adds pet into the system via the id.
     * @param p Pet to add
     * @param id int for the id
     * @return true if pet is unique via the ID and added in, false otherwise.
     */
    public boolean addPet(Pet p, int id) {
        if (petList.containsKey(id)) {
            return false; // ID already exists, cannot add
        }
        petList.put(id, p); // Add the pet to the HashMap
        return true;
    }

    /**
     * YOUR TODO
     *
     * This needs to send back a Pet object from the petList. If no pet is found via the id sent then
     * null should be returned.
     *
     * Gets a pet from the map
     * @param id int for the pet id to look for
     * @return Pet object - null if no pet found
     */
    public Pet getPet(int id) {
        return petList.getOrDefault(id, null); // Return the pet or null if not found
    }

    /**
     * This method will add a visit into the system. If date is the current day it will check the time
     * and make sure there is no overlap. If date is different, first checks if we have a file for the date
     * if so opens it, if not it will create a new file for that date. This adds the appointment to it.
     * @param v Visit to add
     * @return true if successfully added, false otherwise.
     */
    public boolean addVisit(Visit v) {
        if (v.getDate().equalsIgnoreCase(currentDateFile.substring(0, currentDateFile.lastIndexOf(".")))) {
            for (Visit daily : dailySchedule.toList()) {
                if (daily.getTime().equalsIgnoreCase(v.getTime())) {
                    return false; // matching times so can't add
                }
            }
            // no times match so good to add
            dailySchedule.enqueue(v);
        }
        else { // different day
            // open file if exists, read in for that day then check times and add
            try (BufferedReader in = new BufferedReader(new FileReader(v.getDate() + ".txt"))) {
                in.readLine(); // headers
                String line = in.readLine(); // read in first data
                while (line != null) {
                    String[] data = line.split("\t");

                    // data[1] is the time so check
                    if (data[1].equalsIgnoreCase(v.getTime())) {
                        System.out.println("bad time");
                        return false;
                    }

                    line = in.readLine(); // read next
                }
            } catch (FileNotFoundException e) {
                // if no file exists then write out to new file
                try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(v.getDate() + ".txt")))) {
                    out.println("ID\tTime\tanimalID\tdetails");
                    out.println(v.getId() + "\t" + v.getTime() + "\t" + v.getAnimalID() + "\t" + v.getDetails());
                    out.close();
                    nextVisitID++; // increase the ID for next visit.
                    return true;
                } catch (IOException ex) {
                    System.out.println(ex);
                }

            } catch (Exception e) {
                System.out.println(e);
                return false;
            }
        }

        // after the try up above runs if no return false for matching time, then add into file
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(v.getDate()+".txt", true)))){
            out.println("\n" + v.getId() + "\t" + v.getTime() + "\t" + v.getAnimalID() + "\t" + v.getDetails());
            out.close();
            nextVisitID++; // increase the ID for next visit.
            return true; // everything works and writes out.
        }
        catch (IOException ex) {
            System.out.println(ex);
        }
        return false;
    }

    /**
     * puts the pet visit on the completed list along with saving on the pet object.
     * @param v visit that is complete.
     */
    public void completeVisit(Visit v) {
        pastVisitList.add(v);
        getPet(v.getAnimalID()).addVisit(v); // adds to the pet in their past visits stack.
    }

}

