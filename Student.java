import java.io.*;
import java.util.*;


/**
 * Student class
 *
 * Stores the name of the Student's text file
 * When a student object is made, the student's text file
 * is also made
 *
 * There are methods in this class that deal with deleting the student account,
 * outputting the student menu, and getting the arrayList of Submission objects
 *
 * @author Yu Hyun Kim
 * @version 12/12/21
 */
public class Student extends People implements Serializable {

    /**
     * Constructor for Student object
     * @param name name of the student
     * @param email email of the student
     */
    public Student(String name, String email) {
        super(name, email);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(super.getTextFileName()))) {
            ArrayList<Submission> studentSubmission = new ArrayList<>();
            oos.writeObject(studentSubmission);
        } catch (Exception e) {
            //catch exception
        }
    }

    /**
     * Method returns the arrayList of all submission in student's txt file
     * @return ArrayList of all submission objects of that student
     */
    public ArrayList<Submission> getSubmissionListInFile() {
        try (ObjectInputStream oos = new ObjectInputStream(new FileInputStream(super.getTextFileName()))) {
            Object o = oos.readObject();
            if (o == null) {
                return null;
            }
            ArrayList<Submission> theSubs = (ArrayList<Submission>) o;
            return theSubs;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Method puts the new submission arrayList into student's txt file and saves it
     * @param newArrayList the updated ArrayList of Submission objects to be written into the student's file
     */
    public void updateSubmissionListInFile(ArrayList<Submission> newArrayList) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(super.getTextFileName()))) {
            oos.writeObject(newArrayList);
        } catch (Exception e) {
            //catch exception
        }
    }
}
