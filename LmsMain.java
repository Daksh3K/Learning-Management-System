import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.*;


/**
 * Project 5 - LmsMain class
 *
 * This class is one of the 2 classes for the server side code. There is a serverSocket object that's made and
 * there is a loop that constantly runs, trying to enable new connections when users want to utilize the application
 * and connect to the server.
 *
 * Every time a user connects by running the ClientOne class, a new EchoServer object is made, and a Thread is also
 * made and ran, causing the run() method in the EchoServer class to run. By having every connection between a user
 * and the server be a new thread, multiple clients can utilize the application at the same time.
 *
 * There are also methods inside this class that return arrayLists that are stored in several files, as well as
 * methods that store arrayLists into files so that data can be saved. Even if the server shuts down, all the data
 * will be saved.
 *
 * More information regarding any of the classes, including LmsMain, can be found in the documentation
 * in the Readme file in the repository
 *
 * Test cases are explained in the Tests.md file in the repository. Please read the Tests.md file very carefully.
 *
 * ***The port number we used was 1234
 *
 * @author Yu Hyun Kim
 * @version 12/12/21
 */
public class LmsMain {
    private static ArrayList<Socket> connectedUsers = new ArrayList<>();
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(1234);  //serverSocket object

        /**
         * the if statements below make a new arrayList and stores it into the People.txt and/or Courses.txt file
         * if one or both files are empty
         */
        if (getPeopleListInFile() == null) {
            ArrayList<People> newArrayList = new ArrayList<>();
            updatePeopleListInFile(newArrayList);
        }
        if (getCoursesInFile() == null) {
            ArrayList<Course> newCourseList = new ArrayList<>();
            updateCoursesInFile(newCourseList);
        }


        /**
         * loop that runs that enables clients to connect
         */
        while (true) {
            Socket socket = serverSocket.accept();  //block and wait for new client to connect
            EchoServer server = new EchoServer(socket);  //make a new EchoServer object
            new Thread(server).start();   //create and start a new thread for each client that's connected
                                        //so they can do what they want client-side and communicate with the
                                        //server.
        }
    }

    /**
     * Method that gets the arrayList of Student and Teacher objects in the People.txt file
     *
     * @returns arrayList<People>
     */
    public static ArrayList<People> getPeopleListInFile() {
        try (ObjectInputStream oos = new ObjectInputStream(new FileInputStream("People.txt"))) {
            Object o = oos.readObject();
            if (o == null) {
                return null;
            }
            ArrayList<People> thePeople = (ArrayList<People>) o;
            return thePeople;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Method puts the new People arrayList into People.txt
     * This is one form of data saving
     *
     * @param newArrayList
     */
    public static void updatePeopleListInFile(ArrayList<People> newArrayList) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("People.txt"))) {
            oos.writeObject(newArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Method gets the arrayList of Course objects from Courses.txt file
     *
     * @return ArrayList<Course>
     */
    public static ArrayList<Course> getCoursesInFile() {
        try (ObjectInputStream oos = new ObjectInputStream(new FileInputStream("Courses.txt"))) {
            Object o = oos.readObject();
            if (o == null) {
                return null;
            }
            ArrayList<Course> theCourses = (ArrayList<Course>) o;
            return theCourses;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Method writes the updated ArrayList of courses into Courses.txt
     *
     * @param newArrayList
     */
    public static void updateCoursesInFile(ArrayList<Course> newArrayList) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Courses.txt"))) {
            oos.writeObject(newArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Method gets the arrayList of Quiz objects from a course's .txt file
     *
     * @param courseTextFileName
     * @return ArrayList<Quiz>
     */
    public static ArrayList<Quiz> getQuizListInFile(String courseTextFileName) {
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
     * Method puts the new ArrayList of quiz objects into the course's text file
     * @param newArrayList
     */
    public static void updateQuizInFile(ArrayList<Quiz> newArrayList, String courseTextFileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(courseTextFileName))) {
            oos.writeObject(newArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Method gets the arrayList of Submission objects from a student's .txt file
     *
     * @param studentSubmissionFileName
     * @return ArrayList<Submission>
     */
    public static ArrayList<Submission> getSubmissionListInFile(String studentSubmissionFileName) {
        try (ObjectInputStream oos = new ObjectInputStream(new FileInputStream(studentSubmissionFileName))) {
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
     * Method puts the new submission arrayList into student's txt file
     *
     * @param newArrayList
     */
    public static void updateSubmissionListInFile(ArrayList<Submission> newArrayList,
                                                  String studentSubmissionFileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(studentSubmissionFileName))) {
            oos.writeObject(newArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

