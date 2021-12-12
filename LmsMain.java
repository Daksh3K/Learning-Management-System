import java.lang.*;
import java.io.*;
import javax.swing.*;
import java.net.*;
import java.util.ArrayList;

public class LmsMain {
    private static ArrayList<Socket> connectedUsers = new ArrayList<>();
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(1234);
        if (getPeopleListInFile() == null) {
            ArrayList<People> newArrayList = new ArrayList<>();
            updatePeopleListInFile(newArrayList);
        }
        if (getCoursesInFile() == null) {
            ArrayList<Course> newCourseList = new ArrayList<>();
            updateCoursesInFile(newCourseList);
        }


        while (true) {
            Socket socket = serverSocket.accept();
            //add socket connected to arrayList connectedUsers
            //connectedUsers.add(socket);




            EchoServer server = new EchoServer(socket);
            new Thread(server).start();
            System.out.println("Connection made");
        }
    }

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
     * Method puts the new People arrayList into People.txt and saves it
     * @param newArrayList
     */
    public static void updatePeopleListInFile(ArrayList<People> newArrayList) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("People.txt"))) {
            oos.writeObject(newArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }







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
     * Method writes the ArrayList of courses into
     * the file associated with the teacher.
     * @param newArrayList
     */
    public static void updateCoursesInFile(ArrayList<Course> newArrayList) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Courses.txt"))) {
            oos.writeObject(newArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



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
     * Method puts the new ArrayList of quiz objects into the
     * course's text file
     * @param newArrayList
     */
    public static void updateQuizInFile(ArrayList<Quiz> newArrayList, String courseTextFileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(courseTextFileName))) {
            oos.writeObject(newArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



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
     * Method puts the new submission arrayList into student's txt file and saves it
     * @param newArrayList
     */
    public static void updateSubmissionListInFile(ArrayList<Submission> newArrayList, String studentSubmissionFileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(studentSubmissionFileName))) {
            oos.writeObject(newArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
