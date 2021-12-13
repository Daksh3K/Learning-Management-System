import java.io.*;
import java.util.ArrayList;

/**
 * The People class
 * Parent class of Teacher and Student
 *
 * The Teachers' and Students' names are stored
 *
 * @version December 12 2021
 * @author Yu Hyun Kim
 */

public class People implements Serializable {
    private String name;  //name of Teacher or Student
    private String email;  //email of Teacher or Student
    private String textFileName;

    /**
     * constructor for People class
     * instantiates the name and email instance variables
     * @param name
     * @param email
     */
    public People(String name, String email) {
        this.name = name;
        this.email = email;
        this.textFileName = email + ".txt";
    }

    /**
     * getter methods for getting the value of name and email instance variables
     */
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getTextFileName() {
        return textFileName;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Method to delete user's account and their .txt file
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
