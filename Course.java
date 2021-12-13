import java.io.*;
import java.util.ArrayList;

/**
 * This is the Course class
 * Represents the course that a teacher can make
 * Teachers can manage their courses with the Course class
 * They can add, delete, or edit the course
 * When it comes to editing course content, the teachers can either add, delete,
 * or edit quizzes for each course
 *
 * @author Yu Hyun Kim
 * @version 12/12/21
 */

public class Course implements Serializable {
    private String courseName;  //name of course
    private String courseTextFileName;  //text file name for the course (text file contains arrayList of Quiz objects)
    private String teacherWhoMadeCourse;  //email of teacher who made course


    /**
     * Constructor for the Course class
     * initializes courseName and courseTextFileName instance variables
     * Makes a text file for the course, and this text file is meant to store an ArrayList
     * of Quiz objects
     * After making the text file, an ArrayList of Quiz objects is made and placed in the text file
     * @param courseName
     */
    public Course(String courseName, String teacherWhoMadeCourse) {
        this.courseName = courseName;
        this.teacherWhoMadeCourse = teacherWhoMadeCourse;
        this.courseTextFileName = courseName + ".txt";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(courseTextFileName))) {
            ArrayList<Quiz> quizzes = new ArrayList<>();
            oos.writeObject(quizzes);
        } catch (Exception e) {
            //exception caught
        }
    }

    /**
     * returns the course name
     * @return String containing course name
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Returns the course's text file name
     * @return String of the course's text file name
     */
    public String getCourseTextFileName() {
        return courseTextFileName;
    }

    /**
     * Returns information about the teacher who
     * created the course
     * @return String containing teacher information
     */
    public String getTeacherWhoMadeCourse() {
        return teacherWhoMadeCourse;
    }

    /**
     * Method returns the arrayList of Quiz object from the course's text file
     * @return ArrayList of Quiz objects
     */
    public ArrayList<Quiz> getQuizListInFile() {
        try (ObjectInputStream oos = new ObjectInputStream(new FileInputStream(courseTextFileName))) {
            Object o = oos.readObject();
            if (o == null) {
                return null;
            }
            ArrayList<Quiz> theQuiz = (ArrayList<Quiz>) o;
            return theQuiz;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Method puts the new ArrayList of quiz objects into the
     * course's text file
     * @param newArrayList the new ArrayList of Quiz objects
     */
    public void updateQuizInFile(ArrayList<Quiz> newArrayList) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(courseTextFileName))) {
            oos.writeObject(newArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
