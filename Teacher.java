import java.io.*;
import java.util.*;


/**
 * Represents a teacher user.
 * Takes name and email as unique identifiers for the teacher.
 * Each teacher has a unique file associated with it, storing
 * their courses and quizzes.
 *
 * @author Dakshesh Gupta
 * @version 12/12/21
 */
public class Teacher extends People implements Serializable {


    /**
     * Constructor For Teacher
     * Teacher's email and name are saved
     * Teacher's text file is made
     * @param name
     * @param email
     */
    public Teacher(String name, String email) {
        super(name, email);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(super.getTextFileName()))) {
            ArrayList<Course> teacherCourse = new ArrayList<>();
            oos.writeObject(teacherCourse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
