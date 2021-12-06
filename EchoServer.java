import java.lang.*;
import java.io.*;
import javax.swing.*;
import java.net.*;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.*;

public class EchoServer implements Runnable {
    static Object gateKeeper = new Object();
    Socket socket;
    BufferedReader bfr;
    PrintWriter writer;
    static ArrayList<Socket> clientsConnected = new ArrayList<>();
    public EchoServer(Socket socket) {
        this.socket = socket;
        clientsConnected.add(this.socket);
        try {
            this.bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer =  new PrintWriter(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }


        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("State.txt"))) {
            //nothing
        } catch (Exception e) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("State.txt"))) {
                State newStateObj = new State("There are no students", "There are no teachers", "There are no courses", "There are no quizzes");
                oos.writeObject(newStateObj);   //Put the new state object in the State.txt File
            } catch (Exception p) {
                p.printStackTrace();
            }
        }
    }

    public void run() {  //Write all the code and logic here
        while (true) {
            try {
                System.out.println("Accepting input from client");
                String whatUserWants = bfr.readLine();

                if (whatUserWants.equals("createAccount")){
                    String userName = bfr.readLine();
                    System.out.println(userName);
                    String userEmail = bfr.readLine();
                    System.out.println(userEmail);
                    boolean continueMaking = true;
                    for(People peep: getPeopleListInFile()) {
                        if (peep.getEmail().equals(userEmail)) {
                            continueMaking = false;
                            break;
                        }
                    }
                    if (!continueMaking) {
                        System.out.println("No is sent");
                        writer.write("no");
                        writer.println();
                        writer.flush();
                        continue;
                    } else {
                        System.out.println("yes is sent");
                        writer.write("yes");
                        writer.println();
                        writer.flush();
                    }
                    String userRole = bfr.readLine();
                    if (userRole.equals("0")) {  //when user is Student
                        Student newStudent = new Student(userName, userEmail);
                        synchronized (gateKeeper) {
                            ArrayList<People> p = getPeopleListInFile();
                            p.add(newStudent);
                            updatePeopleListInFile(p);  //we have added new Student to database
                        }
                    } else {  //teacher is made
                        Teacher newTeacher = new Teacher(userName, userEmail);
                        synchronized (gateKeeper) {
                            ArrayList<People> p = getPeopleListInFile();
                            p.add(newTeacher);
                            updatePeopleListInFile(p);  //we have added new Teacher to database
                        }
                    }
                    for(People person: getPeopleListInFile()) {
                        System.out.println(person.getName() + "      "+ person.getEmail());
                    }
                } else if (whatUserWants.equals("loginAccount")) {
                    String email = bfr.readLine();  //email of person logged in #######
                    boolean foundAccount = false;
                    String role = "";
                    for(People p: getPeopleListInFile()) {
                        if (p.getEmail().equals(email)) {
                            foundAccount = true;
                            if (p instanceof Teacher) {
                                role = "teacher";
                            } else {
                                role = "student";
                            }
                            break;
                        }
                    }
                    if (!foundAccount) {
                        writer.write("accNotFound");
                        writer.println();
                        writer.flush();
                        continue;
                    }
                    writer.write("loggedIn");
                    writer.println();
                    writer.flush();
                    writer.write(role);
                    writer.println();
                    writer.flush();
                } else if (whatUserWants.equals("manageCourse")) {
                    String teacherEmail = bfr.readLine();
                    String listOfCourse = "";
                    for (Course c: LmsMain.getCoursesInFile()) {
                        if (c.getTeacherWhoMadeCourse().equals(teacherEmail)) {
                            listOfCourse += (c.getCourseName() + "**");
                        }
                    }
                    writer.write("manageCourse");
                    writer.println();
                    writer.flush();

                    writer.write(listOfCourse);
                    writer.println();
                    writer.flush();

                } else if (whatUserWants.equals("makeCourse")) {     //YOU HAVE TO CHANGE THE STUDENT JLABEL AS WELL
                    String teacherEmail = bfr.readLine();
                    String course = bfr.readLine();
                    boolean courseExist = false;
                    for (Course c: LmsMain.getCoursesInFile()) {
                        if (c.getCourseName().equals(course)) {
                            courseExist = true;
                            break;
                        }
                    }
                    if (courseExist) {
                        writer.write("cantMakeCourse");
                        writer.println();
                        writer.flush();
                        continue;
                    }
                    String newListOfCourse = "";
                    Course newCourse = new Course(course, teacherEmail);
                    ArrayList<Course> courses = LmsMain.getCoursesInFile();
                    courses.add(newCourse);
                    for (Course c: courses) {
                        newListOfCourse += (c.getCourseName() + "**");
                    }
                    LmsMain.updateCoursesInFile(courses);
                    writer.write("makeCourse");
                    writer.println();
                    writer.flush();

                    writer.write(newListOfCourse);
                    writer.println();
                    writer.flush();

                    /**
                     * This is for all clients
                     */
                    String courseListForStudents = "";
                    for (Course c: LmsMain.getCoursesInFile()) {
                        courseListForStudents += (c.getCourseName() + "**");
                    }
                    for (Socket s: clientsConnected) {
                        writer = new PrintWriter(s.getOutputStream());
                        writer.write("editStudentCourseList");
                        writer.println();
                        writer.flush();

                        writer.write(courseListForStudents);
                        writer.println();
                        writer.flush();
                    }
                } else if (whatUserWants.equals("deleteCourse")) {  //YOU HAVE TO CHANGE THE STUDENT JLABEL AS WELL
                    String courseToDel = bfr.readLine();
                    String teacherEmail = bfr.readLine();
                    boolean foundCourse = false;
                    ArrayList<Course> courses = LmsMain.getCoursesInFile();
                    String newCourseList = "";
                    for (int i = 0; i < courses.size(); i++) {
                        if (courses.get(i).getCourseName().equals(courseToDel)) {
                            foundCourse = true;
                            courses.remove(i);
                            LmsMain.updateCoursesInFile(courses);
                            break;
                        }
                    }
                    if (!foundCourse) {
                        writer.write("cantDeleteCourse");
                        writer.println();
                        writer.flush();
                        continue;
                    }
                    for(Course c: LmsMain.getCoursesInFile()) {  //list for every specific teacher
                        if (c.getTeacherWhoMadeCourse().equals(teacherEmail)) {
                            newCourseList += (c.getCourseName() + "**");
                        }
                    }
                    writer.write("deleteCourse");
                    writer.println();
                    writer.flush();

                    writer.write(newCourseList);
                    writer.println();
                    writer.flush();

                    /**
                     * This is for all clients
                     */
                    String courseListForStudents = "";
                    for (Course c: LmsMain.getCoursesInFile()) {
                        courseListForStudents += (c.getCourseName() + "**");
                    }
                    for (Socket s: clientsConnected) {
                        writer = new PrintWriter(s.getOutputStream());
                        writer.write("editStudentCourseList");
                        writer.println();
                        writer.flush();

                        writer.write(courseListForStudents);
                        writer.println();
                        writer.flush();
                    }
                } else if (whatUserWants.equals("editCourse")) {
                    String courseEdit = bfr.readLine();
                    boolean foundCourse = false;
                    ArrayList<Course> courses = LmsMain.getCoursesInFile();
                    String newQuizList = "";
                    for (Course c: LmsMain.getCoursesInFile()) {
                        if (c.getCourseName().equals(courseEdit)) {
                            foundCourse = true;
                            String courseFileName = c.getCourseTextFileName();
                            for (Quiz q: LmsMain.getQuizListInFile(courseFileName)) {
                                newQuizList += (q.getQuizName() + "**");
                            }
                            break;
                        }
                    }
                    if (!foundCourse) {
                        writer.write("cantEditCourse");
                        writer.println();
                        writer.flush();
                        continue;
                    }
                    writer.write("editCourse");
                    writer.println();
                    writer.flush();

                    writer.write(newQuizList);
                    writer.println();
                    writer.flush();
                } else if (whatUserWants.equals("makingQuiz")) {
                    String course = bfr.readLine();
                    String teacherEmail = bfr.readLine();
                    String courseTextFile = "";

                    String quizName = bfr.readLine();
                    boolean quizExist = false;
                    for (Course c: LmsMain.getCoursesInFile()) {
                        if (course.equals(c.getCourseName())) {
                            //use c to do stuff
                            String courseFile = c.getCourseTextFileName();
                            courseTextFile = c.getCourseTextFileName();
                            for (Quiz q: LmsMain.getQuizListInFile(courseFile)) {
                                if (q.getQuizName().equals(quizName)) {
                                    quizExist = true;
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    String questions = bfr.readLine();
                    String answerChoices = bfr.readLine();
                    int numQuestions = Integer.parseInt(bfr.readLine());
                    int numAnswers = Integer.parseInt(bfr.readLine());
                    boolean random = false;
                    if (bfr.readLine().equals("randomize")) {
                        random = true;
                    }
                    if (quizExist) {
                        writer.write("cantMakeQuiz");
                        writer.println();
                        writer.flush();
                        continue;
                    }
                    //make the quiz object
                    Quiz newQuiz = new Quiz(quizName, questions, answerChoices, numQuestions, numAnswers, random, teacherEmail);
                    ArrayList<Quiz> quizzes = LmsMain.getQuizListInFile(courseTextFile);
                    quizzes.add(newQuiz);
                    LmsMain.updateQuizInFile(quizzes, courseTextFile);

                    String quizList = "";
                    for(Quiz q: LmsMain.getQuizListInFile(courseTextFile)) {
                        quizList += (q.getQuizName() + "**");
                    }

                    writer.write("makeQuiz");
                    writer.println();
                    writer.flush();

                    writer.write(quizList);
                    writer.println();
                    writer.flush();

                    /**
                     * For all clients
                     */
                    for (Socket s: clientsConnected) {
                        writer = new PrintWriter(s.getOutputStream());
                        writer.write("updateStudentQuizListWhenCourseEdited");
                        writer.println();
                        writer.flush();

                        writer.write(course);
                        writer.println();
                        writer.flush();

                        writer.write(quizList);
                        writer.println();
                        writer.flush();

                    }

                } else if (whatUserWants.equals("deleteQuiz")) {
                    String course = bfr.readLine();
                    String quizDelete = bfr.readLine();

                    String listQuiz = "";

                    boolean quizFound = false;
                    for (Course c: LmsMain.getCoursesInFile()) {
                        if (course.equals(c.getCourseName())) {
                            //use c to do stuff
                            String courseFile = c.getCourseTextFileName();
                            ArrayList<Quiz> quizzes = LmsMain.getQuizListInFile(courseFile);
                            for (int i = 0; i < quizzes.size(); i++) {
                                if (quizzes.get(i).getQuizName().equals(quizDelete)) {
                                    quizFound = true;
                                    quizzes.remove(i);
                                    for (Quiz q: quizzes) {
                                        listQuiz += (q.getQuizName() + "**");
                                    }
                                    LmsMain.updateQuizInFile(quizzes, courseFile);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    if (!quizFound) {
                        writer.write("cantDeleteQuiz");
                        writer.println();
                        writer.flush();
                        continue;
                    }
                    writer.write("deleteQuiz");
                    writer.println();
                    writer.flush();

                    writer.write(listQuiz);
                    writer.println();
                    writer.flush();

                    /**
                     * For all clients
                     */

                    for (Socket s: clientsConnected) {
                        writer = new PrintWriter(s.getOutputStream());
                        writer.write("updateStudentQuizListWhenCourseEdited");
                        writer.println();
                        writer.flush();

                        writer.write(course);
                        writer.println();
                        writer.flush();

                        writer.write(listQuiz);
                        writer.println();
                        writer.flush();

                    }

                } else if (whatUserWants.equals("editingQuiz")) {
                    String course = bfr.readLine();
                    String quizName = bfr.readLine();

                    String questions = bfr.readLine();
                    String answerChoices = bfr.readLine();
                    int numQuestions = Integer.parseInt(bfr.readLine());
                    int numAnswers = Integer.parseInt(bfr.readLine());
                    boolean random = false;
                    if (bfr.readLine().equals("randomize")) {
                        random = true;
                    }
                    //check to see if quiz exists
                    boolean quizFound = false;
                    for (Course c: LmsMain.getCoursesInFile()) {
                        if (course.equals(c.getCourseName())) {
                            //use c to do stuff
                            String courseFile = c.getCourseTextFileName();
                            ArrayList<Quiz> quizzes = LmsMain.getQuizListInFile(courseFile);
                            for (int i = 0; i < quizzes.size(); i++) {
                                if (quizzes.get(i).getQuizName().equals(quizName)) {
                                    quizFound = true;
                                    quizzes.get(i).setQuestions(questions);
                                    quizzes.get(i).setAnswers(answerChoices);
                                    quizzes.get(i).setNumQuestions(numQuestions);
                                    quizzes.get(i).setNumAnswers(numAnswers);
                                    quizzes.get(i).setRandom(random);
                                    LmsMain.updateQuizInFile(quizzes, courseFile);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    if (!quizFound) {
                        writer.write("cantEditQuiz");
                        writer.println();
                        writer.flush();
                        continue;
                    }
                    writer.write("editQuiz");
                    writer.println();
                    writer.flush();

                } else if (whatUserWants.equals("displayAllCourses")) {  ///NOW THIS IS STUDENT STUFF
                    // when student sees courses menu
                    String allCourses = "";
                    for(Course c: LmsMain.getCoursesInFile()) {
                        allCourses += (c.getCourseName() + "**");
                    }
                    writer.write("showStudentCourses");
                    writer.println();
                    writer.flush();

                    writer.write(allCourses);
                    writer.println();
                    writer.flush();
                } else if (whatUserWants.equals("studentViewCourse")) {    //student enters certain course name
                    String nameOfCourse = bfr.readLine();
                    boolean foundCourse = false;
                    for(Course c: LmsMain.getCoursesInFile()) {
                        if (c.getCourseName().equals(nameOfCourse)) {
                            foundCourse = true;
                            break;
                        }
                    }
                    if (!foundCourse) {
                        writer.write("cantFindCourse");
                        writer.println();
                        writer.flush();
                        continue;
                    }
                    String listQuizzesForStudents = "";
                    for (Course c: LmsMain.getCoursesInFile()) {
                        if (c.getCourseName().equals(nameOfCourse)) {
                            ArrayList<Quiz> quizzes = c.getQuizListInFile();
                            for (Quiz q: quizzes) {
                                listQuizzesForStudents += (q.getQuizName() + "**");
                            }
                            break;
                        }
                    }
                    writer.write("foundCourse");
                    writer.println();
                    writer.flush();

                    /**
                     * This is for all clients
                     */
                    for (Socket s: clientsConnected) {
                        writer = new PrintWriter(s.getOutputStream());
                        writer.write("updateStudentQuizList");
                        writer.println();
                        writer.flush();

                        writer.write(listQuizzesForStudents);
                        writer.println();
                        writer.flush();
                    }





                } else if (whatUserWants.equals("")) {

                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
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



    /**
     * Method returns the arrayList of Quiz object from the course's text file
     * @return
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
     * Method puts the new ArrayList of quiz objects into the
     * course's text file
     * @param newArrayList, courseTextFileName
     */
    public static void updateQuizInFile(ArrayList<Quiz> newArrayList, String courseTextFileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(courseTextFileName))) {
            oos.writeObject(newArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}




