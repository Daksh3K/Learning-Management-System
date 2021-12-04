import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.net.*;
import java.util.*;
import java.awt.*;


public class ClientOne implements Runnable{
    static BufferedReader bfr;
    static PrintWriter writer;
    static String emailLoggedIn;

    /**
     * Startmenu
     */
    static JFrame startMenu = new JFrame("Start Menu");
    static Container startContent = startMenu.getContentPane();
    static JPanel startJpanel = new JPanel();
    static JLabel welcomeMessage = new JLabel("Welcome. Press a button to start");


    /**
     * Teacher JFrame
     */
    static JFrame teacherMenu = new JFrame("Welcome! You are a teacher!");

    /**
     * course Jframe and jtextarea
     *
     */
    static JFrame coursesMenu = new JFrame("Your Created Courses");
    static JTextArea listOfCourses = new JTextArea();


    /**
     * Quiz Jframe
     *
     *
     */
    static JFrame quizMenu = new JFrame("Manage quizzes");
    static JTextArea listOfQuizzesInCourse = new JTextArea();

    static Course courseBeingEdited;





    public static void main(String[] args) {
        Thread client1 = new Thread(new ClientOne());
        client1.start();

        try {
            Socket socket = new Socket("localhost", 1234);
            bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());

            /**
             * WE ARE CREATING START MENU
             * CREATE ACCOUNT/LOGIN/EXIT
             */
            startContent.setLayout(new BorderLayout());
            startMenu.setSize(320,250);
            startMenu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            startJpanel.add(welcomeMessage, BorderLayout.NORTH);

            JButton createAccBut = new JButton("Create Account");
            startJpanel.add(createAccBut, BorderLayout.SOUTH);

            JButton loginBut = new JButton("Login");
            startJpanel.add(loginBut, BorderLayout.SOUTH);

            startContent.add(startJpanel, BorderLayout.CENTER);
            startMenu.setVisible(true);

            /**
             * Quiz menu stuff
             *
             */
            Container quizMenuContent = quizMenu.getContentPane();
            quizMenuContent.setLayout(new BorderLayout());
            quizMenu.setSize(400, 400);
            quizMenu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            quizMenu.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    coursesMenu.setVisible(true);
                }
            });
            listOfQuizzesInCourse.setEditable(false);
            quizMenu.setVisible(false);

            JPanel quizJpanel = new JPanel();

            JPanel quizJpanelButtons = new JPanel();

            JButton createQuiz = new JButton("Create Quiz");
            ActionListener createQuizListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {  //THIS IS MAKING THE QUIZ THIS IS MOST COMPLEX
                    String quizName = JOptionPane.showInputDialog(null, "Enter quiz name", "Making a new quiz!", JOptionPane.QUESTION_MESSAGE);
                    boolean quizAlreadyExists = false;
                    for (Quiz q: courseBeingEdited.getQuizListInFile()) {
                        if (quizName.equals(q.getQuizName())) {
                            quizAlreadyExists = true;
                            break;
                        }
                    }
                    if (quizAlreadyExists) {
                        JOptionPane.showMessageDialog(null, "You cannot have multiple quizzes with the same name.", "Cannot make quiz", JOptionPane.ERROR_MESSAGE);
                    } else {
                        try {
                            String numberOfQuestions = JOptionPane.showInputDialog(null, "Enter the number of questions for the quiz", "Number of questions", JOptionPane.QUESTION_MESSAGE);
                            int numQuestions = Integer.parseInt(numberOfQuestions);
                            if (numQuestions < 1) {
                                JOptionPane.showMessageDialog(null, "Make sure to enter a number equal to or greater than 1 next time!", "Error with number of questions", JOptionPane.ERROR_MESSAGE);
                            } else {  //asking for number of answer choices for questinos
                                try {
                                    int numAnswerChoices = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter number of answer choices per question", "Number of answer choices", JOptionPane.QUESTION_MESSAGE));
                                    if (numAnswerChoices < 1) {
                                        JOptionPane.showMessageDialog(null, "Make sure to enter a number equal to or greater than 1 next time", "Error with number of answers", JOptionPane.ERROR_MESSAGE);
                                    } else {  //for loop runs
                                        String questionString = "";
                                        String answerString = "";
                                        for (int i = 1; i <= numQuestions; i++) {
                                            while (true) {
                                                String question = JOptionPane.showInputDialog(null, "Enter the question for Question " + i + "\n***NOTE: YOU CANNOT HAVE '*' IN THE QUESTION", "Enter question", JOptionPane.QUESTION_MESSAGE);
                                                if (question == null) {
                                                    JOptionPane.showMessageDialog(null, "You must finish making this quiz. Please type your question.", "Must finish making quiz", JOptionPane.ERROR_MESSAGE);
                                                } else if (question.length() == 0) {
                                                    JOptionPane.showMessageDialog(null, "You didn't enter any characters. Try again", "Enter a question please", JOptionPane.ERROR_MESSAGE);
                                                } else if (question.indexOf("*") != -1) {
                                                    JOptionPane.showMessageDialog(null, "You can't have '*' anywhere in your question.", "No '*' in question", JOptionPane.ERROR_MESSAGE);
                                                } else {
                                                    questionString += (question + "**");  //question successfully made and break from while loop to get answer choices;
                                                    break;
                                                }
                                            }
                                            for (int j = 1; j <= numAnswerChoices; j++) {
                                                while (true) {
                                                    String answerChoice = JOptionPane.showInputDialog(null, "Enter answer choice " + j + "\n***NOTE: YOU CANNOT HAVE '*' IN ANSWER CHOICE", "Enter answer choice", JOptionPane.QUESTION_MESSAGE);
                                                    if (answerChoice == null) {
                                                        JOptionPane.showMessageDialog(null, "You must finish making this quiz. Please type your answer choice.", "Must finish making quiz", JOptionPane.ERROR_MESSAGE);
                                                    } else if (answerChoice.length() == 0) {
                                                        JOptionPane.showMessageDialog(null, "You didn't enter any characters. Try again", "Enter something please", JOptionPane.ERROR_MESSAGE);
                                                    } else if (answerChoice.indexOf("*") != -1) {
                                                        JOptionPane.showMessageDialog(null, "You can't have '*' anywhere in your answer choice.", "No '*' in answer choice", JOptionPane.ERROR_MESSAGE);
                                                    } else {
                                                        answerString += (answerChoice + "**");  //answerChoices made
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        //tell server quiz is made
                                        while (true) {
                                            int randomOrNot = JOptionPane.showConfirmDialog(null, "Do you want the quiz to be randomized", "Should quiz be randomized?", JOptionPane.YES_NO_OPTION);
                                            if (randomOrNot != JOptionPane.YES_OPTION && randomOrNot != JOptionPane.NO_OPTION) {
                                                JOptionPane.showMessageDialog(null, "You must select yes or no.", "Select yes or no", JOptionPane.ERROR_MESSAGE);
                                            } else {
                                                if (randomOrNot == JOptionPane.NO_OPTION) {  //if user DOESNT WANT Random

                                                } else {  //if user wants random

                                                }
                                                break;
                                            }
                                        }
                                    }
                                } catch (Exception error) {
                                    JOptionPane.showMessageDialog(null, "Make sure to enter a valid number and make sure it's 1 or greater next time!");
                                }
                            }
                        } catch (Exception error) {
                            JOptionPane.showMessageDialog(null,"Make sure to enter a valid number and make sure it's 1 or greater next time!", "Error with number of questions", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            };
            createQuiz.addActionListener(createQuizListener);

            JButton editQuiz = new JButton("Edit Quiz");
            ActionListener editQuizListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                }
            };
            editQuiz.addActionListener(editQuizListener);

            JButton deleteQuiz = new JButton("Delete Quiz");
            ActionListener deleteQuizListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                }
            };
            deleteQuiz.addActionListener(deleteQuizListener);

            quizJpanel.add(listOfQuizzesInCourse);
            quizMenuContent.add(quizJpanel, BorderLayout.NORTH);

            quizJpanelButtons.add(createQuiz);
            quizJpanelButtons.add(editQuiz);
            quizJpanelButtons.add(deleteQuiz);
            quizMenuContent.add(quizJpanelButtons, BorderLayout.SOUTH);


            /**
             * THIS IS NOW THE COURSES JFRAME
             *
             */
            Container coursesContent = coursesMenu.getContentPane();
            coursesContent.setLayout(new BorderLayout());

            coursesMenu.setSize(600, 400);
            coursesMenu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  //change to DoNothingonclose and force teacher to press logout to leave
            coursesMenu.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    teacherMenu.setVisible(true);
                }
            });
            listOfCourses.setEditable(false);

            JPanel courseJpanel = new JPanel();

            JPanel courseMenuButtons = new JPanel();

            JButton createCourse = new JButton("Create Course");
            ActionListener createCourseListener = new ActionListener() {  //CREATING COURSE
                @Override
                public void actionPerformed(ActionEvent e) {
                    while (true) {
                        String courseName = JOptionPane.showInputDialog(null, "Enter course name", "Making a new course!", JOptionPane.QUESTION_MESSAGE);
                        boolean courseExists = false;
                        for(Course c: LmsMain.getCoursesInFile()) {
                            if (c.getCourseName().equals(courseName)) {
                                courseExists = true;
                                break;
                            }
                        }
                        if (courseExists) {
                            JOptionPane.showMessageDialog(null, "The name of your course is already being used! Please select another name", "Error with making course", JOptionPane.ERROR_MESSAGE);
                        } else {  //making course
                            writer.write("makingCourse");
                            writer.println();
                            writer.flush();

                            writer.write(courseName);
                            writer.println();
                            writer.flush();

                            writer.write(emailLoggedIn);
                            writer.println();
                            writer.flush();

                            break;
                        }
                    }
                }
            };
            createCourse.addActionListener(createCourseListener);

            JButton editCourse = new JButton("Edit Course");  //THIS IS WHERE I LEFT OFF / MAKE THE QUIZ MENU JFRAME AND THEN WORK ON STUDENT STUFF
            ActionListener editCourseListener = new ActionListener() {  //EDITING COURSE
                @Override
                public void actionPerformed(ActionEvent e) {
                    while (true) {
                        String courseToEdit = JOptionPane.showInputDialog(null, "Type course you want to edit", "Editing Course", JOptionPane.QUESTION_MESSAGE);
                        boolean courseFound = false;
                        for (Course c : LmsMain.getCoursesInFile()) {
                            if (c.getCourseName().equals(courseToEdit)) {   //if course is foundm use "c"
                                courseFound = true;
                                courseBeingEdited = c;
                                String listOfQuizzes = "";
                                for (Quiz q : c.getQuizListInFile()) {
                                    listOfQuizzes += (q.getQuizName() + "\n");
                                }
                                if (listOfQuizzes.equals("")) {
                                    listOfQuizzes = "There are no quizzes made for this course";
                                }
                                listOfQuizzesInCourse.setText(listOfQuizzes);
                                coursesMenu.setVisible(false);  //hide courses menu
                                quizMenu.setVisible(true);   //show quizzes menu
                                break;
                            }
                        }
                        if (!courseFound) {  //course matches
                            JOptionPane.showMessageDialog(null, "We can't find the course you entered", "Can't find course", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
                        break;
                    }
                }
            };
            editCourse.addActionListener(editCourseListener);

            JButton deleteCourse = new JButton("Delete Course");
            ActionListener delCourseListener = new ActionListener() {   //Deleting course
                @Override
                public void actionPerformed(ActionEvent e) {
                    while (true) {
                        String courseToDelete = JOptionPane.showInputDialog(null, "Enter the name of the course you want to delete", "Deleting course", JOptionPane.QUESTION_MESSAGE);
                        boolean foundCourse = false;
                        for (Course c: LmsMain.getCoursesInFile()) {
                            if (c.getCourseName().equals(courseToDelete)) {
                                foundCourse = true;
                                break;
                            }
                        }
                        if (foundCourse) {
                            writer.write("deleteCourse");
                            writer.println();
                            writer.flush();

                            writer.write(courseToDelete);
                            writer.println();
                            writer.flush();
                            break;
                        } else {
                            JOptionPane.showMessageDialog(null, "Couldn't find the course you entered.", "Couldn't delete course", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            };
            deleteCourse.addActionListener(delCourseListener);

            courseJpanel.add(listOfCourses, BorderLayout.NORTH);

            courseMenuButtons.add(createCourse);
            courseMenuButtons.add(editCourse);
            courseMenuButtons.add(deleteCourse);

            coursesContent.add(courseJpanel, BorderLayout.NORTH);
            coursesContent.add(courseMenuButtons, BorderLayout.SOUTH);
            coursesMenu.setVisible(false);

            /**
             * JFrame for teacher menu.
             * 1) Manage Courses/Quizzes
             * 2) Grade Quizzes
             * 3) Edit Name
             * 4) Delete
             * 5) Logout
             */  //TEACHER STUFF STARTS HERE--------------------------------------------------------------------
            Container tMenuContent = teacherMenu.getContentPane();
            tMenuContent.setLayout(new BorderLayout());
            teacherMenu.setSize(400, 400);
            teacherMenu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  //change to DoNothingonclose and force teacher to press logout to leave

            JPanel tMainJpanel = new JPanel();
            JLabel tMainMessage = new JLabel("Welcome teacher! Press a button to start");
            tMainJpanel.add(tMainMessage, BorderLayout.NORTH);

            JButton manageButton = new JButton("Manage Course/Quiz");
            tMainJpanel.add(manageButton, BorderLayout.SOUTH);
            ActionListener manageCourseButton = new ActionListener() {   //button that teacher clicks to see courses
                @Override
                public void actionPerformed(ActionEvent e) {
                    teacherMenu.setVisible(false);
                    coursesMenu.setVisible(true);

                    /**
                     * Setting coursesList Text
                     */
                    String courseList = "";
                    for(Course c: LmsMain.getCoursesInFile()) {
                        if (c.getTeacherWhoMadeCourse().equals(emailLoggedIn)) {
                            courseList += (c.getCourseName() + "\n");
                        }
                    }
                    if (courseList.equals("")) {
                        courseList = "No Courses Made Yet";
                    }
                    listOfCourses.setText(courseList);
                }
            };
            manageButton.addActionListener(manageCourseButton);


            JButton gradeButton = new JButton("Grade Quizzes");
            tMainJpanel.add(gradeButton, BorderLayout.SOUTH);


            /**
             * If teacher wants to edit name
             */
            JButton editNameButton = new JButton("Edit Name");
            tMainJpanel.add(editNameButton, BorderLayout.SOUTH);
            ActionListener changeNameListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newName = JOptionPane.showInputDialog(null, "Enter new name", "Name change", JOptionPane.QUESTION_MESSAGE);
                    writer.write("changeName");
                    writer.println();
                    writer.flush();

                    writer.write(newName);
                    writer.println();
                    writer.flush();
                }
            };
            editNameButton.addActionListener(changeNameListener);
            /**
             * if teacher wants to delete account
             */
            JButton deleteButton = new JButton("Delete account");
            tMainJpanel.add(deleteButton, BorderLayout.SOUTH);
            ActionListener deleteAccListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    writer.write("deleteAccount");
                    writer.println();
                    writer.flush();
                    teacherMenu.setVisible(false);  //"teacher account is exited"
                    startMenu.setVisible(true);
                }
            };
            deleteButton.addActionListener(deleteAccListener);

            /**
             * If teacher wants to logout account
             */
            JButton logoutButton = new JButton("Logout");
            tMainJpanel.add(logoutButton, BorderLayout.SOUTH);
            ActionListener logoutListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    writer.write("logout");
                    writer.println();
                    writer.flush();
                    teacherMenu.setVisible(false);  //"teacher account is exited/logged out"
                    startMenu.setVisible(true);
                }
            };
            logoutButton.addActionListener(logoutListener);










            tMenuContent.add(tMainJpanel, BorderLayout.CENTER);
            teacherMenu.setVisible(false);

            /**
             * WHAT HAPPENS GUI WISE WHEN USER CREATES ACCOUNT
             */
            ActionListener createAccListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == createAccBut) {
                        String name = JOptionPane.showInputDialog(null, "Enter name", "Enter name", JOptionPane.QUESTION_MESSAGE);
                        String email = "";
                        boolean emailExists = false;
                        while (true) {
                            email = JOptionPane.showInputDialog(null, "Enter email", "Enter email", JOptionPane.QUESTION_MESSAGE);
                            for (People p: getPeopleListInFile()) {
                                if (p.getEmail().equals(email)) {
                                    emailExists = true;
                                    break;
                                }
                            }
                            if (emailExists) {
                                JOptionPane.showMessageDialog(null, "Email already used", "Email already used", JOptionPane.ERROR_MESSAGE);
                                emailExists = false;
                            } else {
                                break;
                            }
                        }
                        int role = JOptionPane.showConfirmDialog(null, "Are you a student or teacher\nPress 'Yes' if student, 'No' if teacher", "Student or Teacher?", JOptionPane.YES_NO_OPTION);
                        String stuOrTeach = Integer.toString(role);
                        writer.write("createAccount");
                        writer.println();
                        writer.flush();
                        writer.write(name);
                        writer.println();
                        writer.flush();
                        writer.write(email);
                        writer.println();
                        writer.flush();
                        writer.write(stuOrTeach);
                        writer.println();
                        writer.flush();
                        System.out.println("Account made");
                    }
                }
            };
            createAccBut.addActionListener(createAccListener);

            /**
             * What happens when login button is pressed!
             */
            ActionListener loginListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String enteredEmail = JOptionPane.showInputDialog(null, "Enter your email to login", "Login", JOptionPane.QUESTION_MESSAGE);
                    boolean foundAccount = false;
                    for(People p: getPeopleListInFile()) {
                        if (p.getEmail().equals(enteredEmail)) {
                            foundAccount = true;
                            break;
                        }
                    }
                    if (foundAccount) {  //At this point user logged in
                        writer.write("loginAccount");
                        writer.println();
                        writer.flush();

                        writer.write(enteredEmail);
                        writer.println();
                        writer.flush();

                        /**
                         * Starting from this point, user is logged in.
                         */
                        startMenu.setVisible(false);

                        for (People p: getPeopleListInFile()) {
                            if (p.getEmail().equals(enteredEmail)) {
                                if (p instanceof Student) {  //student logged in
                                    Student user = (Student) p;
                                    emailLoggedIn = user.getEmail();

                                } else {    //teacher logged in
                                    Teacher user = (Teacher) p;
                                    emailLoggedIn = user.getEmail();
                                    teacherMenu.setVisible(true);

                                }
                            }
                        }



                    } else if (enteredEmail != null){
                        JOptionPane.showMessageDialog(null, "Invalid Account Credentials", "Invalid Email", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            loginBut.addActionListener(loginListener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            //have the code here read the People file to display output Strings for teacher list and student list

            try {
                String mesFromSer;
                try {
                    mesFromSer = bfr.readLine();
                } catch (Exception e) {
                    continue;
                }
                System.out.println(mesFromSer);
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("State.txt"))) {
                    Object readStateObj = ois.readObject();
                    State theCurrentState = (State) readStateObj;
                    String stringDisplay = theCurrentState.getStudentsString();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            /**
                             * Setting list of courses that teacher sees
                             */
                            String courseList = "";
                            for(Course c: LmsMain.getCoursesInFile()) {
                                if (c.getTeacherWhoMadeCourse().equals(emailLoggedIn)) {
                                    courseList += (c.getCourseName() + "\n");
                                }
                            }
                            if (courseList.equals("")) {
                                courseList = "No Courses Made Yet";
                            }
                            listOfCourses.setText(courseList);

                            /**
                             * Setting list of quizzes teacher sees for specific course
                             */
                            String quizList = "";
                            if (courseBeingEdited != null) {
                                System.out.println("here made it");
                                for (Quiz q : courseBeingEdited.getQuizListInFile()) {
                                    quizList += (q.getQuizName() + "\n");
                                }
                                if (quizList.equals("")) {
                                    quizList = "No Quizzes Made Yet";
                                }
                                listOfQuizzesInCourse.setText(quizList);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }catch (Exception e) {
                e.printStackTrace();
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
}
