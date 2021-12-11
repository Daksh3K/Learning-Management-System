import java.io.*;
import java.util.ArrayList;

/**
 * The People class
 * Parent class of Teacher and Student
 * <p>
 * The Teachers' and Students' names are stored
 *
 * @author Yu Hyun Kim
 * @version December 11 2021
 */
public class People implements Serializable {
    private String name;  //name of Teacher or Student
    private String email;  //email of Teacher or Student
    private String textFileName; //the text file that stores person

    /**
     * constructor for People class
     * instantiates the name and email instance variables
     *
     * @param name  the name of the person
     * @param email the email of the person
     */
    public People(String name, String email) {
        this.name = name;
        this.email = email;
        this.textFileName = email + ".txt";
    }

    /**
     * Method to get the name of the person
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Method to get the email of the person
     *
     * @return String
     */
    public String getEmail() {
        return email;
    }

    /**
     * Method to get the text file associated with the person
     *
     * @return String
     */
    public String getTextFileName() {
        return textFileName;
    }

    /**
     * Method to set the name of the person
     *
     * @param newName is the new name of the person
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Method to delete a user
     */
    public void deleteUser() {
        ArrayList<People> people = LmsMain.getPeopleListInFile();
        for (int i = 0; i < people.size(); i++) {
            if (people.get(i).getEmail().equals(this.getEmail())) {
                File f = new File(getTextFileName());
                f.delete();
                people.remove(i);
                LmsMain.updatePeopleListInFile(people);
                break;
            }
        }
    }
}
