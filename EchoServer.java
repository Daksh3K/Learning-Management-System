import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Project 5 - EchoServer class
 *
 * This class is the other class that consists of the server-side code for our project. Objects of the EchoServer
 * class are made every time a new user connects to the server and creates a socket to the server. There is a run()
 * method in the EchoServer class which is run by a thread, and this thread enables every user to connect and
 * communicate with the server.
 *
 * This class is where data from the client is received, and depending on what data was sent from the client, the code
 * in this class enables the information from the client to be interpreted, and the program will do the
 * necessary actions it needs to do based on what the user is trying to do. Additionally, in the run() method of
 * the EchoServer class, information from the server can be sent back to the client as well.
 *
 * There is one method inside this class, which enables the ArrayList of Student and Teacher objects to be read
 * from the People.txt file.
 *
 * More information can be found in the Readme file in our repository
 *
 * Lab Section L24
 *
 * @author Yu Hyun Kim
 * @version 12/12/21
 *
 */
public class EchoServer implements Runnable {
    static Object gateKeeper = new Object();  //object used in synchronized block
    /**
     * we have a lot of instances where threads/users are accessing the same source of data and write to the same
     * sources of data, so we used synchronized blocks to prevent race conditions
     *
     */
    Socket socket;  //socket object
    BufferedReader bfr;  //bufferedReader object
    PrintWriter writer;  //printWriter object
    static ArrayList<Socket> clientsConnected = new ArrayList<>();  //arrayList of sockets connected to server
    public EchoServer(Socket socket) {
        this.socket = socket;
        clientsConnected.add(this.socket);
        try {
            this.bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer =  new PrintWriter(socket.getOutputStream());
        } catch (Exception e) {
            //exception caught
        }
    }

    public void run() {
        while (true) {
            try {
                String whatUserWants = bfr.readLine();  //what the client wants to do
                                                    //this String is received from the client

                if (whatUserWants.equals("createAccount")) {  //if user wants to create an account
                    String userName = bfr.readLine();
                    String userEmail = bfr.readLine();
                    boolean continueMaking = true;
                    synchronized (gateKeeper) {
                        for (People peep : getPeopleListInFile()) {
                            if (peep.getEmail().equals(userEmail)) {
                                continueMaking = false;
                                break;
                            }
                        }
                    }
                    writer = new PrintWriter(this.socket.getOutputStream());
                    if (!continueMaking) {  //if account can't be made
                        writer.write("no");
                        writer.println();
                        writer.flush();
                        continue;
                    } else {   //if account can be made
                        writer.write("yes");
                        writer.println();
                        writer.flush();
                    }
                    String userRole = bfr.readLine();

                    /**
                     * We are either making the Teacher or Student object and then we are saving
                     * that account in the People.txt file.
                     */
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

                } else if (whatUserWants.equals("loginAccount")) {  //if user wants to login account
                    String email = bfr.readLine();  //email user entered to login
                    boolean foundAccount = false;
                    String name = "";
                    String role = "";
                    synchronized (gateKeeper) {
                        for (People p : getPeopleListInFile()) {
                            if (p.getEmail().equals(email)) {
                                foundAccount = true;
                                name = p.getName();
                                if (p instanceof Teacher) {
                                    role = "teacher";
                                } else {
                                    role = "student";
                                }
                                break;
                            }
                        }
                    }
                    if (!foundAccount) {  //if the email doesn't match with any account that exists
                        writer.write("accNotFound");
                        writer.println();
                        writer.flush();
                        continue;
                    }
                    //if email does match with an account
                    writer.write("loggedIn");
                    writer.println();
                    writer.flush();
                    writer.write(role);
                    writer.println();
                    writer.flush();
                    writer.write(name);
                    writer.println();
                    writer.flush();
                } else if (whatUserWants.equals("manageCourse")) {  //if teacher wants to manage course
                    String teacherEmail = bfr.readLine();
                    String listOfCourse = "";

                    synchronized (gateKeeper) {
                        for (Course c : LmsMain.getCoursesInFile()) {
                            if (c.getTeacherWhoMadeCourse().equals(teacherEmail)) {
                                listOfCourse += (c.getCourseName() + "**");
                            }
                        }
                    }
                    writer = new PrintWriter(this.socket.getOutputStream());
                    writer.write("manageCourse");
                    writer.println();
                    writer.flush();

                    writer.write(listOfCourse);
                    writer.println();
                    writer.flush();

                } else if (whatUserWants.equals("makeCourse")) {   //if teacher wants to make course
                    String teacherEmail = bfr.readLine();
                    String course = bfr.readLine();
                    boolean courseExist = false;

                    /**
                     * Determine whether course exists
                     */
                    synchronized (gateKeeper) {
                        for (Course c : LmsMain.getCoursesInFile()) {
                            if (c.getCourseName().equals(course)) {
                                courseExist = true;
                                break;
                            }
                        }
                    }
                    if (courseExist) {  //if teacher can't make course
                        writer.write("cantMakeCourse");
                        writer.println();
                        writer.flush();
                        continue;
                    }
                    //if course can be made
                    String newListOfCourse = "";
                    Course newCourse = new Course(course, teacherEmail);

                    synchronized (gateKeeper) {
                        ArrayList<Course> courses = LmsMain.getCoursesInFile();
                        courses.add(newCourse);
                        for (Course c : courses) {
                            newListOfCourse += (c.getCourseName() + "**");
                        }
                        LmsMain.updateCoursesInFile(courses);
                    }
                    writer = new PrintWriter(this.socket.getOutputStream());
                    writer.write("makeCourse");
                    writer.println();
                    writer.flush();

                    writer.write(newListOfCourse);
                    writer.println();
                    writer.flush();

                    /**
                     * String that consists of the list of all courses that exist
                     *
                     * Sent to clients for real time updates to happen
                     */
                    String courseListForStudents = "";
                    synchronized (gateKeeper) {
                        for (Course c : LmsMain.getCoursesInFile()) {
                            courseListForStudents += (c.getCourseName() + "**");
                        }
                    }
                    /**
                     * Sending info to all clients for real time updates
                     */
                    synchronized (gateKeeper) {
                        for (Socket s : clientsConnected) {
                            try {
                                writer = new PrintWriter(s.getOutputStream());
                                writer.write("editStudentCourseList");
                                writer.println();
                                writer.flush();

                                writer.write(courseListForStudents);
                                writer.println();
                                writer.flush();
                            } catch (Exception e) {
                                continue;
                            }
                        }
                    }

                } else if (whatUserWants.equals("deleteCourse")) {  //if teacher deletes a course
                    String courseToDel = bfr.readLine();
                    String teacherEmail = bfr.readLine();
                    String newCourseList = "";
                    boolean foundCourse = false;
                    boolean canDeleteCourse = true;

                    synchronized (gateKeeper) {
                        ArrayList<Course> courses = LmsMain.getCoursesInFile();
                        for (int i = 0; i < courses.size(); i++) {
                            if (courses.get(i).getCourseName().equals(courseToDel)) {
                                foundCourse = true;
                                if (LmsMain.getQuizListInFile(courses.get(i).getCourseTextFileName()).size()
                                        != 0) {
                                    canDeleteCourse = false;
                                    break;
                                }
                                File f = new File(courses.get(i).getCourseTextFileName());
                                f.delete();
                                courses.remove(i);
                                LmsMain.updateCoursesInFile(courses);
                                break;
                            }
                        }
                    }
                    writer = new PrintWriter(this.socket.getOutputStream());
                    if (!foundCourse || !canDeleteCourse) {  //if course can't be deleted
                        writer.write("cantDeleteCourse");
                        writer.println();
                        writer.flush();
                        continue;
                    }
                    //if course can be deleted
                    synchronized (gateKeeper) {
                        for (Course c : LmsMain.getCoursesInFile()) {
                            if (c.getTeacherWhoMadeCourse().equals(teacherEmail)) {
                                newCourseList += (c.getCourseName() + "**");
                            }
                        }
                    }

                    writer.write("deleteCourse");
                    writer.println();
                    writer.flush();

                    writer.write(newCourseList);
                    writer.println();
                    writer.flush();

                    String courseListForStudents = "";
                    synchronized (gateKeeper) {
                        for (Course c : LmsMain.getCoursesInFile()) {
                            courseListForStudents += (c.getCourseName() + "**");
                        }
                    }

                    /**
                     * sending info to all clients for real time updates
                     *
                     *
                     */
                    synchronized (gateKeeper) {
                        for (Socket s : clientsConnected) {
                            try {
                                writer = new PrintWriter(s.getOutputStream());
                                writer.write("editStudentCourseList");
                                writer.println();
                                writer.flush();

                                writer.write(courseListForStudents);
                                writer.println();
                                writer.flush();
                            } catch (Exception e) {
                                continue;
                            }
                        }
                    }

                } else if (whatUserWants.equals("editCourse")) {  //if teacher wants to edit course
                    String courseEdit = bfr.readLine();
                    String newQuizList = "";
                    boolean foundCourse = false;

                    synchronized (gateKeeper) {
                        for (Course c : LmsMain.getCoursesInFile()) {
                            if (c.getCourseName().equals(courseEdit)) {
                                foundCourse = true;
                                String courseFileName = c.getCourseTextFileName();
                                for (Quiz q : LmsMain.getQuizListInFile(courseFileName)) {
                                    newQuizList += (q.getQuizName() + "**");
                                }
                                break;
                            }
                        }
                    }
                    writer = new PrintWriter(this.socket.getOutputStream());
                    if (!foundCourse) {  //if course can't be edited
                        writer.write("cantEditCourse");
                        writer.println();
                        writer.flush();
                        continue;
                    }
                    //if course can be edited
                    writer.write("editCourse");
                    writer.println();
                    writer.flush();

                    writer.write(newQuizList);
                    writer.println();
                    writer.flush();
                } else if (whatUserWants.equals("makingQuiz")) {  //if teacher wants to make quiz
                    String course = bfr.readLine();
                    String teacherEmail = bfr.readLine();
                    String courseTextFile = "";

                    String quizName = bfr.readLine();
                    boolean quizExist = false;

                    synchronized (gateKeeper) {
                        for (Course c : LmsMain.getCoursesInFile()) {
                            if (course.equals(c.getCourseName())) {
                                //use c to do stuff
                                String courseFile = c.getCourseTextFileName();
                                courseTextFile = c.getCourseTextFileName();
                                for (Quiz q : LmsMain.getQuizListInFile(courseFile)) {
                                    if (q.getQuizName().equals(quizName)) {
                                        quizExist = true;
                                        break;
                                    }
                                }
                                break;
                            }
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
                    if (quizExist) {  //if quiz can't be made
                        writer = new PrintWriter(this.socket.getOutputStream());
                        writer.write("cantMakeQuiz");
                        writer.println();
                        writer.flush();
                        continue;
                    }
                    /**
                     * If quiz can be made, quiz object is made and stored in the Course's .txt file
                     */
                    String quizList = "";
                    Quiz newQuiz = new Quiz(quizName, questions, answerChoices, numQuestions, numAnswers,
                            random, teacherEmail);

                    synchronized (gateKeeper) {
                        ArrayList<Quiz> quizzes = LmsMain.getQuizListInFile(courseTextFile);
                        quizzes.add(newQuiz);
                        LmsMain.updateQuizInFile(quizzes, courseTextFile);
                        for (Quiz q : LmsMain.getQuizListInFile(courseTextFile)) {
                            quizList += (q.getQuizName() + "**");
                        }
                    }

                    writer = new PrintWriter(this.socket.getOutputStream());
                    writer.write("makeQuiz");
                    writer.println();
                    writer.flush();

                    writer.write(quizList);
                    writer.println();
                    writer.flush();

                    /**
                     * Sending info to all clients for real time updates
                     *
                     * Updates the list of quizzes the student sees
                     */
                    synchronized (gateKeeper) {
                        for (Socket s : clientsConnected) {
                            try {
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
                            } catch (Exception e) {
                                continue;
                            }
                        }
                    }

                } else if (whatUserWants.equals("deleteQuiz")) {  //if teacher wants to delete quiz
                    String course = bfr.readLine();
                    String quizDelete = bfr.readLine();

                    String listQuiz = "";

                    boolean quizFound = false;

                    /**
                     * Trying to scan through all the saved data and try to delete the quiz
                     */
                    synchronized (gateKeeper) {
                        for (Course c : LmsMain.getCoursesInFile()) {
                            if (course.equals(c.getCourseName())) {
                                //use c to do stuff
                                String courseFile = c.getCourseTextFileName();
                                ArrayList<Quiz> quizzes = LmsMain.getQuizListInFile(courseFile);
                                for (int i = 0; i < quizzes.size(); i++) {
                                    if (quizzes.get(i).getQuizName().equals(quizDelete)) {
                                        quizFound = true;
                                        quizzes.remove(i);
                                        for (Quiz q : quizzes) {
                                            listQuiz += (q.getQuizName() + "**");
                                        }
                                        LmsMain.updateQuizInFile(quizzes, courseFile);
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                    }
                    if (!quizFound) {  //if quiz can't be deleted
                        writer = new PrintWriter(this.socket.getOutputStream());
                        writer.write("cantDeleteQuiz");
                        writer.println();
                        writer.flush();
                        continue;
                    }
                    //if quiz can be deleted
                    writer = new PrintWriter(this.socket.getOutputStream());
                    writer.write("deleteQuiz");
                    writer.println();
                    writer.flush();

                    writer.write(listQuiz);
                    writer.println();
                    writer.flush();

                    /**
                     * Sending info to all clients for real time updates
                     */

                    synchronized (gateKeeper) {
                        for (Socket s : clientsConnected) {
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
                    }


                } else if (whatUserWants.equals("editingQuiz")) {  //if teacher wants to edit quiz
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

                    /**
                     * Trying to scan through all the data and edit the quiz with what the teacher
                     * inputted using the JOptionPane
                     */
                    synchronized (gateKeeper) {
                        for (Course c : LmsMain.getCoursesInFile()) {
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
                    }
                    writer = new PrintWriter(this.socket.getOutputStream());
                    if (!quizFound) {  //if quiz can't be edited
                        writer.write("cantEditQuiz");
                        writer.println();
                        writer.flush();
                        continue;
                    }
                    //if quiz can be edited
                    writer.write("editQuiz");
                    writer.println();
                    writer.flush();

                } else if (whatUserWants.equals("displayAllCourses")) {  //if student needs to see courses
                    // when student sees courses menu
                    String allCourses = "";

                    synchronized (gateKeeper) {
                        for (Course c : LmsMain.getCoursesInFile()) {
                            allCourses += (c.getCourseName() + "**");
                        }
                    }
                    writer = new PrintWriter(this.socket.getOutputStream());
                    writer.write("showStudentCourses");
                    writer.println();
                    writer.flush();

                    writer.write(allCourses);
                    writer.println();
                    writer.flush();
                } else if (whatUserWants.equals("studentViewCourse")) {   //student enters certain course name
                                                                    //to try and view
                    String nameOfCourse = bfr.readLine();
                    boolean foundCourse = false;

                    synchronized (gateKeeper) {
                        for (Course c : LmsMain.getCoursesInFile()) {
                            if (c.getCourseName().equals(nameOfCourse)) {
                                foundCourse = true;
                                break;
                            }
                        }
                    }

                    if (!foundCourse) {  //if course that student entered can't be found
                        writer.write("cantFindCourse");
                        writer.println();
                        writer.flush();
                        continue;
                    }
                    //if course that student entered can be found
                    String listQuizzesForStudents = "";

                    synchronized (gateKeeper) {
                        for (Course c : LmsMain.getCoursesInFile()) {
                            if (c.getCourseName().equals(nameOfCourse)) {
                                ArrayList<Quiz> quizzes = c.getQuizListInFile();
                                for (Quiz q : quizzes) {
                                    listQuizzesForStudents += (q.getQuizName() + "**");
                                }
                                break;
                            }
                        }
                    }

                    writer.write("foundCourse");
                    writer.println();
                    writer.flush();

                    /**
                     * Sending info to all clients or real time updates
                     */
                    synchronized (gateKeeper) {
                        for (Socket s : clientsConnected) {
                            try {
                                writer = new PrintWriter(s.getOutputStream());
                                writer.write("updateStudentQuizList");
                                writer.println();
                                writer.flush();

                                writer.write(listQuizzesForStudents);
                                writer.println();
                                writer.flush();
                            } catch (Exception e) {
                                continue;
                            }
                        }
                    }


                } else if (whatUserWants.equals("takingQuiz")) {  //if student is trying to take quiz
                    String studentEmail = bfr.readLine();
                    String courseQuizIn = bfr.readLine();
                    String quiz = bfr.readLine();
                    boolean alreadyTaken = false;

                    synchronized (gateKeeper) {
                        ArrayList<Submission> subs = LmsMain.getSubmissionListInFile(
                                studentEmail + ".txt");
                        for (Submission s : subs) {
                            if (s.getNameOfQuizTaken().equals(quiz)) {
                                alreadyTaken = true;
                                break;
                            }
                        }
                    }


                    /**
                     * Strings that store information regarding quiz that student is trying to take
                     * (assuming that the quiz actually exists)
                     */
                    String teacherWhoMadeQuiz = "";
                    String quizQuestions = "";
                    String quizAnswers = "";
                    String numQuestions = "";
                    String numAnsPerQuestion = "";


                    boolean quizFound = false;

                    /**
                     * scan through all the data and try to take get the quiz the student is trying to take
                     * (assuming that the quiz exists)
                     *
                     * The data regarding the quiz is sent back to the client/student in order for them to see
                     * the quiz and take the quiz.
                     */
                    synchronized (gateKeeper) {
                        for (Course c : LmsMain.getCoursesInFile()) {
                            if (c.getCourseName().equals(courseQuizIn)) {
                                for (Quiz q : c.getQuizListInFile()) {
                                    if (q.getQuizName().equals(quiz)) {
                                        quizFound = true;
                                        if (alreadyTaken || q.isRandom()) {
                                            q.randomize();
                                            quizQuestions = q.getRandomQuestion();
                                            quizAnswers = q.getRandomAnswer();
                                        } else {
                                            quizQuestions = q.getQuestions();
                                            quizAnswers = q.getAnswers();
                                        }
                                        numQuestions = Integer.toString(q.getNumQuestions());
                                        numAnsPerQuestion = Integer.toString(q.getNumAnswers());
                                        teacherWhoMadeQuiz = q.getTeacherEmail();
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                    }

                    if (!quizFound) {  //if the quiz name the student entered doesn't refer to a quiz
                                        //that actually exists
                        writer = new PrintWriter(this.socket.getOutputStream());
                        writer.write("cantTakeQuiz");
                        writer.println();
                        writer.flush();
                        continue;
                    } else {  //if the quiz name the student entered refers to a quiz that exists
                        writer = new PrintWriter(this.socket.getOutputStream());
                        writer.write("takeQuiz");
                        writer.println();
                        writer.flush();

                        writer.write(quizQuestions);
                        writer.println();
                        writer.flush();

                        writer.write(quizAnswers);
                        writer.println();
                        writer.flush();

                        writer.write(numQuestions);
                        writer.println();
                        writer.flush();

                        writer.write(numAnsPerQuestion);
                        writer.println();
                        writer.flush();

                        //waiting for input from student taking quiz

                        /**
                         * After the student takes the quiz, they have to indicate whether they want
                         * to submit or not.
                         *
                         * If student DOESN'T submit, nothing happens
                         *
                         * If student wants to submit, a Submission object is made and stored in the student's .txt
                         * file.
                         */
                        String submitOrNo = bfr.readLine();
                        if (submitOrNo.equals("submit")) {  //if student decides to submit their quiz
                            String quizQuestionString = bfr.readLine();
                            String quizAnswerString = bfr.readLine();
                            String submissionDateString = bfr.readLine();

                            Submission newSub = new Submission(quizQuestionString, quizAnswerString,
                                    submissionDateString, teacherWhoMadeQuiz, quiz,
                                    Integer.parseInt(numQuestions), studentEmail);
                            synchronized (gateKeeper) {
                                ArrayList<Submission> subList = LmsMain.getSubmissionListInFile(studentEmail + ".txt");
                                subList.add(newSub);
                                LmsMain.updateSubmissionListInFile(subList,
                                        studentEmail + ".txt");

                            }
                        }

                        /**
                         * Updating the list of submissions a teacher has to grade when a student just finished
                         * submitting their quiz
                         */
                        String listToGrade = "";
                        synchronized (gateKeeper) {
                            for (People p : LmsMain.getPeopleListInFile()) {
                                if (p instanceof Student) {
                                    String studentSubFile = p.getTextFileName();
                                    for (Submission s : LmsMain.getSubmissionListInFile(studentSubFile)) {
                                        if (s.getGradeForStudent().equals("Not graded yet**")
                                                && s.getTeacherWhoCreatedQuiz().equals(teacherWhoMadeQuiz)) {
                                            listToGrade += (s.getNameOfQuizTaken() + "**");
                                        }
                                    }
                                }
                            }
                        }

                        /**
                         * Sending info to all clients for real time updates
                         */
                        synchronized (gateKeeper) {
                            for (Socket s : clientsConnected) {
                                try {
                                    writer = new PrintWriter(s.getOutputStream());
                                    writer.write("updateTeacherSubList");
                                    writer.println();
                                    writer.flush();

                                    writer.write(teacherWhoMadeQuiz);
                                    writer.println();
                                    writer.flush();

                                    writer.write(listToGrade);
                                    writer.println();
                                    writer.flush();
                                } catch (Exception e) {
                                    continue;
                                }
                            }
                        }

                    }


                } else if (whatUserWants.equals("studentSeeGrades")) {  //if student is trying to see
                                                                    //their submissions
                    String emailStudent = bfr.readLine();
                    String submissions = "";

                    synchronized (gateKeeper) {
                        for (Submission s : LmsMain.getSubmissionListInFile(
                                emailStudent + ".txt")) {
                            submissions += (s.getNameOfQuizTaken() + "**" + s.getQuizQuestionString()
                                    + s.getQuizAnsweredString() + s.getSubDateString() + s.getGradeForStudent());
                        }
                    }

                    writer.write("studentSeeSubs");
                    writer.println();
                    writer.flush();

                    writer.write(submissions);
                    writer.println();
                    writer.flush();
                } else if (whatUserWants.equals("teacherWantsToGrade")) {  //when teacher wants to see what
                                                                    //quizzes they have to grade
                    String teacherEmail = bfr.readLine();
                    String listToGrade = "";

                    synchronized (gateKeeper) {
                        for (People p : LmsMain.getPeopleListInFile()) {
                            if (p instanceof Student) {
                                String studentSubFile = p.getTextFileName();
                                for (Submission s : LmsMain.getSubmissionListInFile(studentSubFile)) {
                                    if (s.getGradeForStudent().equals("Not graded yet**")
                                            && s.getTeacherWhoCreatedQuiz().equals(teacherEmail)) {
                                        listToGrade += (s.getNameOfQuizTaken() + "**");
                                    }
                                }
                            }
                        }
                    }
                    writer.write("teacherWantsToGrade");
                    writer.println();
                    writer.flush();

                    writer.write(listToGrade);
                    writer.println();
                    writer.flush();


                } else if (whatUserWants.equals("gradeQuiz")) {  //when teacher is trying to grade
                                                            //a certain quiz they entered the name for
                    String teacherEmail = bfr.readLine();
                    String quizName = bfr.readLine();
                    String quizSheetToGrade = "";
                    String numQuestionsOnQuiz = "";
                    String studentWhoTookQuiz = "";

                    boolean foundQuiz = false;

                    /**
                     * Scan through all the data and try to find the submission info regarding a quiz
                     * that a student submitted
                     */
                    synchronized (gateKeeper) {
                        for (People p : LmsMain.getPeopleListInFile()) {
                            if (p instanceof Student) {
                                String studentSubFile = p.getTextFileName();
                                for (Submission s : LmsMain.getSubmissionListInFile(studentSubFile)) {
                                    if (s.getGradeForStudent().equals("Not graded yet**") &&
                                            s.getTeacherWhoCreatedQuiz().equals(teacherEmail) &&
                                            s.getNameOfQuizTaken().equals(quizName)) {
                                        quizSheetToGrade = (s.getQuizQuestionString() + s.getQuizAnsweredString()
                                                + s.getSubDateString() + "**");
                                        numQuestionsOnQuiz = s.getNumOfQuestions() + "";
                                        studentWhoTookQuiz = s.getStudentWhoTookQuiz();
                                        foundQuiz = true;
                                        break;
                                    }
                                }
                                if (foundQuiz) {
                                    break;
                                }
                            }
                        }
                    }
                    writer = new PrintWriter(this.socket.getOutputStream());
                    if (!foundQuiz) {  //if quiz can't be graded
                        writer.write("cantGradeQuiz");
                        writer.println();
                        writer.flush();
                        continue;
                    }
                    //if quiz can be graded
                    writer.write("gradeQuiz");
                    writer.println();
                    writer.flush();

                    writer.write(quizSheetToGrade);
                    writer.println();
                    writer.flush();

                    writer.write(numQuestionsOnQuiz);
                    writer.println();
                    writer.flush();

                    String newSubListForStudent = "";
                    String givenGrade = bfr.readLine();

                    synchronized (gateKeeper) {
                        ArrayList<Submission> subs = LmsMain.getSubmissionListInFile(
                                studentWhoTookQuiz + ".txt");
                        for (Submission s : subs) {
                            if (s.getNameOfQuizTaken().equals(quizName)
                                    && s.getGradeForStudent().equals("Not graded yet**")) {
                                s.setGradeForStudent(givenGrade);
                                LmsMain.updateSubmissionListInFile(subs,
                                        studentWhoTookQuiz + ".txt");
                                break;
                            }
                        }

                        for (Submission s : LmsMain.getSubmissionListInFile(
                                studentWhoTookQuiz + ".txt")) {
                            newSubListForStudent += (s.getNameOfQuizTaken() + "**" +
                                    s.getQuizQuestionString() + s.getQuizAnsweredString() +
                                    s.getSubDateString() + s.getGradeForStudent());
                        }
                    }

                    String listToGrade = "";

                    synchronized (gateKeeper) {
                        for (People p : LmsMain.getPeopleListInFile()) {
                            if (p instanceof Student) {
                                String studentSubFile = p.getTextFileName();
                                for (Submission s : LmsMain.getSubmissionListInFile(studentSubFile)) {
                                    if (s.getGradeForStudent().equals("Not graded yet**")
                                            && s.getTeacherWhoCreatedQuiz().equals(teacherEmail)) {
                                        listToGrade += (s.getNameOfQuizTaken() + "**");
                                    }
                                }
                            }
                        }
                    }

                    /**
                     * Sending info to all clients so real time updates can happen
                     */
                    synchronized (gateKeeper) {
                        for (Socket s : clientsConnected) {
                            try {
                                writer = new PrintWriter(s.getOutputStream());
                                writer.write("updateStudentSubList");
                                writer.println();
                                writer.flush();

                                writer.write(studentWhoTookQuiz);
                                writer.println();
                                writer.flush();

                                writer.write(newSubListForStudent);
                                writer.println();
                                writer.flush();

                                writer.write("updateTeacherSubList");
                                writer.println();
                                writer.flush();

                                writer.write(teacherEmail);
                                writer.println();
                                writer.flush();

                                writer.write(listToGrade);
                                writer.println();
                                writer.flush();
                            } catch (Exception e) {
                                continue;
                            }
                        }
                    }


                } else if (whatUserWants.equals("changeName")) {  // if user wants to change their name
                    String email = bfr.readLine();
                    String newName = bfr.readLine();
                    String role = "";

                    /**
                     * Editing the user's name and saving that info
                     */
                    synchronized (gateKeeper) {
                        ArrayList<People> peopleList = LmsMain.getPeopleListInFile();
                        for (People p : peopleList) {
                            if (p.getEmail().equals(email)) {
                                p.setName(newName);
                                LmsMain.updatePeopleListInFile(peopleList);
                                if (p instanceof Teacher) {
                                    role = "teacher";
                                } else {
                                    role = "student";
                                }
                                break;
                            }
                        }
                    }

                    writer = new PrintWriter(this.socket.getOutputStream());
                    writer.write("updatedName");
                    writer.println();
                    writer.flush();

                    writer.write(role);
                    writer.println();
                    writer.flush();

                    writer.write(newName);
                    writer.println();
                    writer.flush();


                } else if (whatUserWants.equals("deleteAccount")) {  //if user is trying to delete
                                                                //their account
                    String email = bfr.readLine();
                    boolean canDelete = true;
                    synchronized (gateKeeper) {
                        ArrayList<People> people = LmsMain.getPeopleListInFile();
                        for (People p : people) {
                            if (p.getEmail().equals(email)) {
                                if (p instanceof Teacher) {
                                    //check to see if quizzes and courses exist
                                    for (Course c: LmsMain.getCoursesInFile()) {
                                        if (c.getTeacherWhoMadeCourse().equals(email)) {
                                            canDelete = false;
                                            break;
                                        }
                                    }
                                }
                                if (canDelete) {
                                    p.deleteUser();
                                }
                                break;
                            }
                        }
                    }
                    writer.write("attemptedDeletion");
                    writer.println();
                    writer.flush();
                    if (canDelete) {  //if user can delete their account
                        writer.write("deletedAccount");
                        writer.println();
                        writer.flush();
                    } else {  //if user can't delete their account
                        writer.write("cantDeleteAccount");
                        writer.println();
                        writer.flush();
                    }

                } else if (whatUserWants.equals("stop")) {  //if user closes the start menu
                    writer.write("stop");
                    writer.println();
                    writer.flush();

                    /**
                     * removing the Socket object from the arrayList of Socket objects since the
                     * client just indicated that they aren't going to use the program anymore
                     */
                    synchronized (gateKeeper) {
                        for (int i = 0; i < clientsConnected.size(); i++) {
                            if (clientsConnected.get(i) == this.socket) {
                                clientsConnected.remove(i);
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                //catching Exception
            }
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
}

