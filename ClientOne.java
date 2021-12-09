import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.net.*;
import java.nio.channels.spi.AbstractInterruptibleChannel;
import java.util.*;
import java.awt.*;
import java.text.*;

public class ClientOne implements Runnable{
    static BufferedReader bfr;
    static PrintWriter writer;
    static String emailLoggedIn;
    static Socket theSocket;

    /**
     * Startmenu
     */
    static JFrame startMenu = new JFrame("Start Menu");

    static JLabel studentLoginGreeting = new JLabel();
    static JLabel teacherLoginGreeting = new JLabel();
    /**
     * Teacher seeing grade submissions
     */
    static JFrame teacherGradeMenu = new JFrame("Submissions to grade");
    static JTextArea listToGrade = new JTextArea();


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

    static String courseBeingEdited;

    /**
     * Student JFrame and JLabel with all the courses
     *
     */
    static JFrame studentMenu = new JFrame("Welcome! You are a student!");

    static JFrame studentCourseMenu = new JFrame("Here are all the courses that exist");
    static JTextArea allCoursesList =  new JTextArea();

    static String courseStudentSeeing = "";

    /**
     * Student JFrame FOR THE QUIZZES
     *
     */
    static JFrame studentQuizMenu = new JFrame("Here are all the quizzes");
    static JTextArea allQuizzesForCourse = new JTextArea();


    /**
     * Student JFrame for submissions
     *
     */
    static JFrame studentSubmissionMenu = new JFrame("Here are all your submissions");
    static JTextArea listOfSubmissions = new JTextArea();

    //10.192.91.225 dorm number
    //10.186.72.124 hicks number
    public static void main(String[] args) {
        Thread client1 = new Thread(new ClientOne());
        client1.start();
        try {
            Socket socket = new Socket("localhost", 1234);
            //System.out.println(InetAddress.getLocalHost());
            theSocket = socket;
            bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());

            /**
             * WE ARE CREATING START MENU
             * CREATE ACCOUNT/LOGIN/EXIT
             */
            Container startContent = startMenu.getContentPane();
            JPanel startJpanel = new JPanel();
            JLabel welcomeMessage = new JLabel("Welcome. Press a button to start");
            startContent.setLayout(new BorderLayout());
            startMenu.setSize(320,250);
            startMenu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            startMenu.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    writer.write("stop");
                    writer.println();
                    writer.flush();
                }
            });

            startJpanel.add(welcomeMessage, BorderLayout.NORTH);

            JButton createAccBut = new JButton("Create Account");
            startJpanel.add(createAccBut, BorderLayout.SOUTH);

            JButton loginBut = new JButton("Login");
            startJpanel.add(loginBut, BorderLayout.SOUTH);

            startContent.add(startJpanel, BorderLayout.CENTER);
            startMenu.setVisible(true);

            /**
             * WHAT HAPPENS GUI WISE WHEN USER CREATES ACCOUNT
             */
            ActionListener createAccListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == createAccBut) {
                        String name = JOptionPane.showInputDialog(null, "Enter name", "Enter name", JOptionPane.QUESTION_MESSAGE);
                        if (name != null) {
                            String email = "";
                            email = JOptionPane.showInputDialog(null, "Enter email", "Enter email", JOptionPane.QUESTION_MESSAGE);
                            if (email != null) {
                                writer.write("createAccount");
                                writer.println();
                                writer.flush();
                                writer.write(name);
                                writer.println();
                                writer.flush();
                                writer.write(email);
                                writer.println();
                                writer.flush();
                            }
                        }
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
                    if (enteredEmail != null) {
                        writer.write("loginAccount");
                        writer.println();
                        writer.flush();
                        writer.write(enteredEmail);
                        writer.println();
                        writer.flush();
                        emailLoggedIn = enteredEmail;
                    }
                }
            };
            loginBut.addActionListener(loginListener);



            /**
             * Teacher grading JFrame
             */
            Container gradeContent = teacherGradeMenu.getContentPane();
            gradeContent.setLayout(new BorderLayout());
            teacherGradeMenu.setSize(400, 400);
            teacherGradeMenu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            teacherGradeMenu.setVisible(false);
            listToGrade.setEditable(false);
            teacherGradeMenu.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    teacherMenu.setVisible(true);
                }
            });


            JPanel listToGradeJpanel = new JPanel();
            listToGradeJpanel.add(listToGrade, BorderLayout.NORTH);
            gradeContent.add(listToGradeJpanel, BorderLayout.NORTH);

            JPanel gradeMenuButtonJpanel = new JPanel();
            gradeContent.add(gradeMenuButtonJpanel, BorderLayout.SOUTH);

            JButton teacherGradeButton = new JButton("Grade a quiz");
            gradeMenuButtonJpanel.add(teacherGradeButton, BorderLayout.SOUTH);
            ActionListener teacherGradingListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String quizName = JOptionPane.showInputDialog(null, "Enter quiz to grade", "enter quiz name", JOptionPane.QUESTION_MESSAGE);
                    if(quizName != null) {
                        writer.write("gradeQuiz");
                        writer.println();
                        writer.flush();

                        writer.write(emailLoggedIn);
                        writer.println();
                        writer.flush();

                        writer.write(quizName);
                        writer.println();
                        writer.flush();
                    }
                }
            };
            teacherGradeButton.addActionListener(teacherGradingListener);

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
                    if (quizName != null) {
                        writer.write("makingQuiz");
                        writer.println();
                        writer.flush();

                        writer.write(courseBeingEdited);
                        writer.println();
                        writer.flush();

                        writer.write(emailLoggedIn);
                        writer.println();
                        writer.flush();

                        writer.write(quizName);
                        writer.println();
                        writer.flush();

                        String numberOfQuestions;
                        int numQuestions = 0;
                        while (true) {
                            try {
                                numberOfQuestions = JOptionPane.showInputDialog(null, "Enter the number of questions for the quiz", "Number of questions", JOptionPane.QUESTION_MESSAGE);
                                numQuestions = Integer.parseInt(numberOfQuestions);

                                if (numQuestions < 1) {
                                    JOptionPane.showMessageDialog(null, "Make sure to enter a number equal to or greater than 1 next time!", "Error with number of questions", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    break;
                                }
                            } catch (Exception numConvError) {
                                JOptionPane.showMessageDialog(null,"Make sure to enter a valid number and make sure it's 1 or greater next time!", "Error with number of questions", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        //WE HAVE NUM QUESTIONS
                        String numAnswers;
                        int numAns = 0;
                        while (true) {
                            try {
                                numAnswers = JOptionPane.showInputDialog(null, "Enter the number of questions for the quiz", "Number of questions", JOptionPane.QUESTION_MESSAGE);
                                numAns = Integer.parseInt(numAnswers);

                                if (numAns < 1) {
                                    JOptionPane.showMessageDialog(null, "Make sure to enter a number equal to or greater than 1 next time!", "Error with number of questions", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    break;
                                }
                            } catch (Exception numConvError) {
                                JOptionPane.showMessageDialog(null,"Make sure to enter a valid number and make sure it's 1 or greater next time!", "Error with number of questions", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        //we have num answers

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
                            for (int j = 1; j <= numAns; j++) {
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
                        writer.write(questionString);
                        writer.println();
                        writer.flush();
                        writer.write(answerString);
                        writer.println();
                        writer.flush();

                        writer.write(numberOfQuestions);
                        writer.println();
                        writer.flush();
                        writer.write(numAnswers);
                        writer.println();
                        writer.flush();

                        //tell server quiz is made
                        while (true) {
                            int randomOrNot = JOptionPane.showConfirmDialog(null, "Do you want the quiz to be randomized", "Should quiz be randomized?", JOptionPane.YES_NO_OPTION);
                            if (randomOrNot != JOptionPane.YES_OPTION && randomOrNot != JOptionPane.NO_OPTION) {
                                JOptionPane.showMessageDialog(null, "You must select yes or no.", "Select yes or no", JOptionPane.ERROR_MESSAGE);
                            } else {
                                if (randomOrNot == JOptionPane.NO_OPTION) {  //if user DOESNT WANT Random
                                    writer.write("noRandomize");
                                    writer.println();
                                    writer.flush();
                                } else {  //if user wants random
                                    writer.write("randomize");
                                    writer.println();
                                    writer.flush();
                                }
                                break;
                            }
                        }
                    }
                }
            };
            createQuiz.addActionListener(createQuizListener);

            JButton editQuiz = new JButton("Edit Quiz");
            ActionListener editQuizListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String quizName = JOptionPane.showInputDialog(null, "Enter quiz name", "Making a new quiz!", JOptionPane.QUESTION_MESSAGE);
                    if (quizName != null) {
                        writer.write("editingQuiz");
                        writer.println();
                        writer.flush();

                        writer.write(courseBeingEdited);
                        writer.println();
                        writer.flush();

                        writer.write(quizName);
                        writer.println();
                        writer.flush();

                        String numberOfQuestions;
                        int numQuestions = 0;
                        while (true) {
                            try {
                                numberOfQuestions = JOptionPane.showInputDialog(null, "Enter the number of questions for the quiz", "Number of questions", JOptionPane.QUESTION_MESSAGE);
                                numQuestions = Integer.parseInt(numberOfQuestions);

                                if (numQuestions < 1) {
                                    JOptionPane.showMessageDialog(null, "Make sure to enter a number equal to or greater than 1 next time!", "Error with number of questions", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    break;
                                }
                            } catch (Exception numConvError) {
                                JOptionPane.showMessageDialog(null,"Make sure to enter a valid number and make sure it's 1 or greater next time!", "Error with number of questions", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        //WE HAVE NUM QUESTIONS
                        String numAnswers;
                        int numAns = 0;
                        while (true) {
                            try {
                                numAnswers = JOptionPane.showInputDialog(null, "Enter the number of questions for the quiz", "Number of questions", JOptionPane.QUESTION_MESSAGE);
                                numAns = Integer.parseInt(numAnswers);

                                if (numAns < 1) {
                                    JOptionPane.showMessageDialog(null, "Make sure to enter a number equal to or greater than 1 next time!", "Error with number of questions", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    break;
                                }
                            } catch (Exception numConvError) {
                                JOptionPane.showMessageDialog(null,"Make sure to enter a valid number and make sure it's 1 or greater next time!", "Error with number of questions", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        //we have num answers

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
                            for (int j = 1; j <= numAns; j++) {
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
                        writer.write(questionString);
                        writer.println();
                        writer.flush();
                        writer.write(answerString);
                        writer.println();
                        writer.flush();

                        writer.write(numberOfQuestions);
                        writer.println();
                        writer.flush();
                        writer.write(numAnswers);
                        writer.println();
                        writer.flush();

                        //tell server quiz is made
                        while (true) {
                            int randomOrNot = JOptionPane.showConfirmDialog(null, "Do you want the quiz to be randomized", "Should quiz be randomized?", JOptionPane.YES_NO_OPTION);
                            if (randomOrNot != JOptionPane.YES_OPTION && randomOrNot != JOptionPane.NO_OPTION) {
                                JOptionPane.showMessageDialog(null, "You must select yes or no.", "Select yes or no", JOptionPane.ERROR_MESSAGE);
                            } else {
                                if (randomOrNot == JOptionPane.NO_OPTION) {  //if user DOESNT WANT Random
                                    writer.write("noRandomize");
                                    writer.println();
                                    writer.flush();
                                } else {  //if user wants random
                                    writer.write("randomize");
                                    writer.println();
                                    writer.flush();
                                }
                                break;
                            }
                        }
                    }
                }
            };
            editQuiz.addActionListener(editQuizListener);

            JButton deleteQuiz = new JButton("Delete Quiz");
            ActionListener deleteQuizListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String quizToDelete = JOptionPane.showInputDialog(null, "Enter name of quiz you want to delete", "name of quiz you want to delete", JOptionPane.QUESTION_MESSAGE);
                    if (quizToDelete != null) {
                        writer.write("deleteQuiz");
                        writer.println();
                        writer.flush();
                        writer.write(courseBeingEdited);
                        writer.println();
                        writer.flush();
                        writer.write(quizToDelete);
                        writer.println();
                        writer.flush();
                    }
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
                    String courseName = JOptionPane.showInputDialog(null, "Enter course name", "Making a new course!", JOptionPane.QUESTION_MESSAGE);
                    if (courseName != null) {
                        writer.write("makeCourse");
                        writer.println();
                        writer.flush();

                        writer.write(emailLoggedIn);
                        writer.println();
                        writer.flush();

                        writer.write(courseName);
                        writer.println();
                        writer.flush();
                    }
                }
            };
            createCourse.addActionListener(createCourseListener);

            JButton editCourse = new JButton("Edit Course");  //THIS IS WHERE I LEFT OFF / MAKE THE QUIZ MENU JFRAME AND THEN WORK ON STUDENT STUFF
            ActionListener editCourseListener = new ActionListener() {  //EDITING COURSE
                @Override
                public void actionPerformed(ActionEvent e) {
                    String courseToEdit = JOptionPane.showInputDialog(null, "Type course you want to edit", "Editing Course", JOptionPane.QUESTION_MESSAGE);
                    if (courseToEdit != null) {
                        writer.write("editCourse");
                        writer.println();
                        writer.flush();
                        writer.write(courseToEdit);
                        writer.println();
                        writer.flush();
                        courseBeingEdited = courseToEdit; // courseName
                    }
                }
            };
            editCourse.addActionListener(editCourseListener);

            JButton deleteCourse = new JButton("Delete Course");

            ActionListener delCourseListener = new ActionListener() {   //Deleting course
                @Override
                public void actionPerformed(ActionEvent e) {
                    String courseToDelete = JOptionPane.showInputDialog(null, "Enter the name of the course you want to delete", "Deleting course", JOptionPane.QUESTION_MESSAGE);
                    if (courseToDelete != null) {
                        writer.write("deleteCourse");
                        writer.println();
                        writer.flush();
                        writer.write(courseToDelete);
                        writer.println();
                        writer.flush();

                        writer.write(emailLoggedIn);
                        writer.println();
                        writer.flush();
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
            teacherMenu.setSize(700, 200);
            teacherMenu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  //change to DoNothingonclose and force teacher to press logout to leave
            teacherMenu.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    startMenu.setVisible(true);
                }
            });


            JPanel greetingTeacherJpanel = new JPanel();
            greetingTeacherJpanel.add(teacherLoginGreeting, BorderLayout.NORTH);

            JPanel tMainJpanel = new JPanel();
            JButton manageButton = new JButton("Manage Course/Quiz");
            tMainJpanel.add(manageButton, BorderLayout.CENTER);
            ActionListener manageCourseButton = new ActionListener() {   //button that teacher clicks to see courses
                @Override
                public void actionPerformed(ActionEvent e) {
                    writer.write("manageCourse");
                    writer.println();
                    writer.flush();
                    writer.write(emailLoggedIn);
                    writer.println();
                    writer.flush();
                }
            };
            manageButton.addActionListener(manageCourseButton);


            JButton gradeButton = new JButton("Grade Quizzes");
            tMainJpanel.add(gradeButton, BorderLayout.SOUTH);
            ActionListener gradeListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    writer.write("teacherWantsToGrade");
                    writer.println();
                    writer.flush();

                    writer.write(emailLoggedIn);
                    writer.println();
                    writer.flush();
                    teacherMenu.setVisible(false);
                    teacherGradeMenu.setVisible(true);
                }
            };
            gradeButton.addActionListener(gradeListener);

            /**
             * If teacher wants to edit name
             */
            JButton editNameButton = new JButton("Edit Name");
            tMainJpanel.add(editNameButton, BorderLayout.SOUTH);
            ActionListener changeNameListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newName = JOptionPane.showInputDialog(null, "Enter new name", "Name change", JOptionPane.QUESTION_MESSAGE);
                    if (newName != null) {
                        writer.write("changeName");
                        writer.println();
                        writer.flush();

                        writer.write(emailLoggedIn);
                        writer.println();
                        writer.flush();

                        writer.write(newName);
                        writer.println();
                        writer.flush();
                    }
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
                    writer.write(emailLoggedIn);
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
                    teacherMenu.setVisible(false);
                    studentMenu.setVisible(false);
                    startMenu.setVisible(true);
                }
            };
            logoutButton.addActionListener(logoutListener);

            tMenuContent.add(tMainJpanel, BorderLayout.SOUTH);
            tMenuContent.add(greetingTeacherJpanel, BorderLayout.NORTH);
            teacherMenu.setVisible(false);


            /**
             * Student stuff =======================================================================
             */

            /**
             * Student sees submissions
             */
            Container studentSubContent = studentSubmissionMenu.getContentPane();
            studentSubContent.setLayout(new BorderLayout());
            studentSubmissionMenu.setSize(400, 400);
            studentSubmissionMenu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            studentSubmissionMenu.setVisible(false);
            listOfSubmissions.setEditable(false);
            studentSubmissionMenu.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    studentMenu.setVisible(true);
                }
            });


            JPanel stuSubJpanel = new JPanel();
            stuSubJpanel.add(listOfSubmissions, BorderLayout.NORTH);
            studentSubContent.add(stuSubJpanel, BorderLayout.NORTH);

            /**
             * Student sees quizzes for course they are seeing
             */
            Container studentQuizContent = studentQuizMenu.getContentPane();
            studentQuizContent.setLayout(new BorderLayout());
            studentQuizMenu.setSize(400, 400);
            studentQuizMenu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            studentQuizMenu.setVisible(false);
            allQuizzesForCourse.setEditable(false);
            studentQuizMenu.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    studentCourseMenu.setVisible(true);
                }
            });

            JPanel stuQuizJpanel = new JPanel();
            stuQuizJpanel.add(allQuizzesForCourse, BorderLayout.NORTH);

            JPanel stuQuizButtonJpanel = new JPanel();

            JButton takeQuizButton = new JButton("Take Quiz");
            stuQuizButtonJpanel.add(takeQuizButton, BorderLayout.SOUTH);
            ActionListener takeQuizListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String quizTaking = JOptionPane.showInputDialog(null, "Enter the quiz you want to take", "Enter quiz name", JOptionPane.QUESTION_MESSAGE);
                    if (quizTaking != null) {
                        writer.write("takingQuiz");
                        writer.println();
                        writer.flush();

                        writer.write(emailLoggedIn);
                        writer.println();
                        writer.flush();

                        writer.write(courseStudentSeeing);
                        writer.println();
                        writer.flush();

                        writer.write(quizTaking);
                        writer.println();
                        writer.flush();
                    }
                }
            };
            takeQuizButton.addActionListener(takeQuizListener);

            studentQuizContent.add(stuQuizJpanel, BorderLayout.NORTH);
            studentQuizContent.add(stuQuizButtonJpanel, BorderLayout.SOUTH);

            /**
             * Student sees all courses
             */
            Container studentCourseContent = studentCourseMenu.getContentPane();
            studentCourseContent.setLayout(new BorderLayout());
            studentCourseMenu.setSize(400, 400);
            studentCourseMenu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            studentCourseMenu.setVisible(false);
            allCoursesList.setEditable(false);
            studentCourseMenu.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    studentMenu.setVisible(true);
                }
            });

            JPanel studentCourseJpanel = new JPanel();
            studentCourseJpanel.add(allCoursesList, BorderLayout.NORTH);
            JPanel studentCourseJPanelForButton = new JPanel();

            JButton stuViewCourse = new JButton("View Course's quizzes");
            studentCourseJPanelForButton.add(stuViewCourse, BorderLayout.SOUTH);
            ActionListener stuViewCourseListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String course = JOptionPane.showInputDialog(null, "Enter course you want to access", "Enter course name", JOptionPane.QUESTION_MESSAGE);
                    if (course != null) {
                        writer.write("studentViewCourse");
                        writer.println();
                        writer.flush();

                        writer.write(course);
                        writer.println();
                        writer.flush();
                        courseStudentSeeing = course;
                    }
                }
            };
            stuViewCourse.addActionListener(stuViewCourseListener);


            studentCourseContent.add(studentCourseJpanel, BorderLayout.NORTH);
            studentCourseContent.add(studentCourseJPanelForButton, BorderLayout.SOUTH);
            /**
             * student main menu
             */
            Container studentContent = studentMenu.getContentPane();
            studentContent.setLayout(new BorderLayout());
            studentMenu.setSize(700, 200);
            studentMenu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  //change to DoNothingonclose and force teacher to press logout to leave
            studentMenu.setVisible(false);
            studentMenu.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    startMenu.setVisible(true);
                }
            });

            JPanel studentGreetingJpanel = new JPanel();
            studentGreetingJpanel.add(studentLoginGreeting, BorderLayout.NORTH);

            JPanel sMainJpanel = new JPanel();
            JButton studentViewCourse = new JButton("View Courses To Take Quizzes");
            ActionListener studentCoursesListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    writer.write("displayAllCourses");
                    writer.println();
                    writer.flush();
                    studentCourseMenu.setVisible(true);
                    studentMenu.setVisible(false);
                }
            };
            studentViewCourse.addActionListener(studentCoursesListener);
            sMainJpanel.add(studentViewCourse, BorderLayout.CENTER);

            JButton viewGradesButton = new JButton("See your grades");
            ActionListener viewGradeListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    writer.write("studentSeeGrades");
                    writer.println();
                    writer.flush();

                    writer.write(emailLoggedIn);
                    writer.println();
                    writer.flush();

                    studentMenu.setVisible(false);
                    studentSubmissionMenu.setVisible(true);
                }
            };
            viewGradesButton.addActionListener(viewGradeListener);
            sMainJpanel.add(viewGradesButton, BorderLayout.SOUTH);

            JButton studentEdit = new JButton("Edit Name");
            sMainJpanel.add(studentEdit, BorderLayout.SOUTH);

            ActionListener stuNameListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newName = JOptionPane.showInputDialog(null, "Enter new name", "Name change", JOptionPane.QUESTION_MESSAGE);
                    if (newName != null) {
                        writer.write("changeName");
                        writer.println();
                        writer.flush();

                        writer.write(emailLoggedIn);
                        writer.println();
                        writer.flush();

                        writer.write(newName);
                        writer.println();
                        writer.flush();
                    }
                }
            };
            studentEdit.addActionListener(stuNameListener);

            JButton studentDeleteButton = new JButton("Delete account");
            sMainJpanel.add(studentDeleteButton, BorderLayout.SOUTH);
            ActionListener stuDelAccListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    writer.write("deleteAccount");
                    writer.println();
                    writer.flush();
                    writer.write(emailLoggedIn);
                    writer.println();
                    writer.flush();
                    teacherMenu.setVisible(false);
                    studentMenu.setVisible(false); //"teacher account is exited"
                    startMenu.setVisible(true);
                }
            };
            studentDeleteButton.addActionListener(stuDelAccListener);

            JButton studentLogoutButton = new JButton("Logout");
            sMainJpanel.add(studentLogoutButton, BorderLayout.SOUTH);

            ActionListener stuLogoutListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    writer.write("logout");
                    writer.println();
                    writer.flush();
                    teacherMenu.setVisible(false);
                    studentMenu.setVisible(false);
                    startMenu.setVisible(true);
                }
            };
            studentLogoutButton.addActionListener(stuLogoutListener);
            studentContent.add(sMainJpanel, BorderLayout.SOUTH);
            studentContent.add(studentGreetingJpanel, BorderLayout.NORTH);


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
                if (mesFromSer.equals("yes")) {
                    System.out.println("OKAY CREATING ACCOUNT");
                    int role = JOptionPane.showConfirmDialog(null, "Are you a student or teacher\nPress 'Yes' if student, 'No' if teacher", "Student or Teacher?", JOptionPane.YES_NO_OPTION);
                    if (role == JOptionPane.YES_OPTION || role == JOptionPane.NO_OPTION) {
                        String stuOrTeach = Integer.toString(role);
                        writer.write(stuOrTeach);
                        writer.println();
                        writer.flush();
                        System.out.println("Account made");
                    }
                } else if (mesFromSer.equals("no")) {
                    JOptionPane.showMessageDialog(null, "Cant make acc", "no", JOptionPane.ERROR_MESSAGE);
                    continue;
                } else if (mesFromSer.equals("accNotFound")) {
                    JOptionPane.showMessageDialog(null, "Cant find ur account", "No account", JOptionPane.ERROR_MESSAGE);
                    continue;
                } else if (mesFromSer.equals("loggedIn")) {
                    String role = bfr.readLine();
                    String name = bfr.readLine();
                    startMenu.setVisible(false);
                    if (role.equals("teacher")) {
                        teacherMenu.setVisible(true);
                        teacherLoginGreeting.setText("Welcome " + name + "! You're a teacher");

                    } else {  //if student logged in
                        studentMenu.setVisible(true);
                        studentLoginGreeting.setText("Welcome " + name + "! You're a student");
                    }
                } else if (mesFromSer.equals("manageCourse")) {
                    String listCourse = bfr.readLine();
                    teacherMenu.setVisible(false);
                    coursesMenu.setVisible(true);
                    String finalListCourses = "";
                    int whereDivider = listCourse.indexOf("**");
                    while (whereDivider != -1) {
                        finalListCourses += (listCourse.substring(0, whereDivider) + "\n");
                        listCourse = listCourse.substring(whereDivider + 2);
                        whereDivider = listCourse.indexOf("**");
                    }
                    System.out.println(finalListCourses);
                    listOfCourses.setText(finalListCourses);
                } else if (mesFromSer.equals("cantMakeCourse")) {
                    JOptionPane.showMessageDialog(null, "Can't make course", " ", JOptionPane.ERROR_MESSAGE);
                } else if (mesFromSer.equals("makeCourse")) {   //updates teacher courses as well student list of courses
                    String newListOfCourses = bfr.readLine();
                    String finalListCourses = "";
                    int whereDivider = newListOfCourses.indexOf("**");
                    while (whereDivider != -1) {
                        finalListCourses += (newListOfCourses.substring(0, whereDivider) + "\n");
                        newListOfCourses = newListOfCourses.substring(whereDivider + 2);
                        whereDivider = newListOfCourses.indexOf("**");
                    }
                    System.out.println(finalListCourses);
                    listOfCourses.setText(finalListCourses);
                } else if (mesFromSer.equals("cantDeleteCourse")) {
                    JOptionPane.showMessageDialog(null, "Couldn't find the course you entered.", "Couldn't delete course", JOptionPane.ERROR_MESSAGE);
                } else if (mesFromSer.equals("deleteCourse")) {
                    String listCourse = bfr.readLine();
                    String finalListCourses = "";
                    int whereDivider = listCourse.indexOf("**");
                    while (whereDivider != -1) {
                        finalListCourses += (listCourse.substring(0, whereDivider) + "\n");
                        listCourse = listCourse.substring(whereDivider + 2);
                        whereDivider = listCourse.indexOf("**");
                    }
                    listOfCourses.setText(finalListCourses);
                } else if (mesFromSer.equals("cantEditCourse")) {
                    JOptionPane.showMessageDialog(null, "Can't find the course", "Can't find course", JOptionPane.ERROR_MESSAGE);
                } else if (mesFromSer.equals("editCourse")) {
                    coursesMenu.setVisible(false);  //hide courses menu
                    quizMenu.setVisible(true);

                    String listQuiz = bfr.readLine();
                    String finalListQuiz = "";
                    int whereDivider = listQuiz.indexOf("**");
                    while (whereDivider != -1) {
                        finalListQuiz += (listQuiz.substring(0, whereDivider) + "\n");
                        listQuiz = listQuiz.substring(whereDivider + 2);
                        whereDivider = listQuiz.indexOf("**");
                    }
                    listOfQuizzesInCourse.setText(finalListQuiz);
                } else if (mesFromSer.equals("cantMakeQuiz")) {
                    JOptionPane.showMessageDialog(null, "You cannot have multiple quizzes with the same name.", "Cannot make quiz", JOptionPane.ERROR_MESSAGE);

                } else if (mesFromSer.equals("makeQuiz")) {
                    String listQuiz = bfr.readLine();
                    String finalListQuiz = "";
                    int whereDivider = listQuiz.indexOf("**");
                    while (whereDivider != -1) {
                        finalListQuiz += (listQuiz.substring(0, whereDivider) + "\n");
                        listQuiz = listQuiz.substring(whereDivider + 2);
                        whereDivider = listQuiz.indexOf("**");
                    }
                    listOfQuizzesInCourse.setText(finalListQuiz);
                } else if (mesFromSer.equals("cantDeleteQuiz")) {
                    JOptionPane.showMessageDialog(null, "Couldn't find the quiz", "Can't find quiz", JOptionPane.ERROR_MESSAGE);
                } else if (mesFromSer.equals("deleteQuiz")) {
                    String listQuiz = bfr.readLine();
                    String finalListQuiz = "";
                    int whereDivider = listQuiz.indexOf("**");
                    while (whereDivider != -1) {
                        finalListQuiz += (listQuiz.substring(0, whereDivider) + "\n");
                        listQuiz = listQuiz.substring(whereDivider + 2);
                        whereDivider = listQuiz.indexOf("**");
                    }
                    listOfQuizzesInCourse.setText(finalListQuiz);
                } else if (mesFromSer.equals("cantEditQuiz")) {
                    JOptionPane.showMessageDialog(null, "Can't Find your quiz", "Can't find your quiz", JOptionPane.ERROR_MESSAGE);
                } else if (mesFromSer.equals("editQuiz")) {
                    JOptionPane.showMessageDialog(null, "Successfully editted quiz", "Success!", JOptionPane.INFORMATION_MESSAGE);
                } else if (mesFromSer.equals("showStudentCourses")) {
                    String allCourses = bfr.readLine();
                    String finalListCourses = "";
                    int whereDivider = allCourses.indexOf("**");
                    while (whereDivider != -1) {
                        finalListCourses += (allCourses.substring(0, whereDivider) + "\n");
                        allCourses = allCourses.substring(whereDivider + 2);
                        whereDivider = allCourses.indexOf("**");
                    }
                    allCoursesList.setText(finalListCourses);
                } else if (mesFromSer.equals("cantFindCourse")) {
                    JOptionPane.showMessageDialog(null, "Can't find the course you entered", "Can't find course", JOptionPane.ERROR_MESSAGE);
                } else if (mesFromSer.equals("foundCourse")) {
                    studentCourseMenu.setVisible(false);
                    studentQuizMenu.setVisible(true);

                } else if (mesFromSer.equals("editStudentCourseList")) {
                    String allCourses = bfr.readLine();
                    String finalListCourses = "";
                    int whereDivider = allCourses.indexOf("**");
                    while (whereDivider != -1) {
                        finalListCourses += (allCourses.substring(0, whereDivider) + "\n");
                        allCourses = allCourses.substring(whereDivider + 2);
                        whereDivider = allCourses.indexOf("**");
                    }
                    allCoursesList.setText(finalListCourses);
                } else if (mesFromSer.equals("updateStudentQuizList")) {
                    String listQuiz = bfr.readLine();
                    String finalListQuiz = "";
                    int whereDivider = listQuiz.indexOf("**");
                    while (whereDivider != -1) {
                        finalListQuiz += (listQuiz.substring(0, whereDivider) + "\n");
                        listQuiz = listQuiz.substring(whereDivider + 2);
                        whereDivider = listQuiz.indexOf("**");
                    }
                    allQuizzesForCourse.setText(finalListQuiz);
                } else if (mesFromSer.equals("cantTakeQuiz")) {
                    JOptionPane.showMessageDialog(null, "Can't find quiz", "Can't find the quiz", JOptionPane.ERROR_MESSAGE);
                } else if (mesFromSer.equals("takeQuiz")) {
                    studentQuizMenu.setVisible(false);
                    String questions = bfr.readLine();
                    String answers = bfr.readLine();
                    System.out.println(answers);
                    int numQuestions = Integer.parseInt(bfr.readLine());
                    int numAns = Integer.parseInt(bfr.readLine());

                    String finalQuestions = "";  //print this for all questions
                    String studentAnswers = "";
                    for (int i = 1; i <= numQuestions; i++) {
                        int whereDivider = questions.indexOf("**");
                        String currentQuestion = ("Question " + i + ": " + questions.substring(0, whereDivider) + "**");
                        finalQuestions += currentQuestion;
                        questions = questions.substring(whereDivider + 2);
                        //Questions list is asked
                        String[] answerSet = new String[numAns];
                        for (int j = 0; j < numAns; j++) {
                            int whereDividerAns = answers.indexOf("**");
                            answerSet[j] = answers.substring(0, whereDividerAns);
                            answers = answers.substring(whereDividerAns + 2);
                        }  //populate AnswerSet
                        while (true) {
                            String userChoice = (String) JOptionPane.showInputDialog(null, currentQuestion + "\n\n" + "Select answer for Question " + i, "Enter your answer", JOptionPane.QUESTION_MESSAGE, null, answerSet, answerSet[0]);
                            if (userChoice == null) {
                                JOptionPane.showMessageDialog(null, "You must finish taking your quiz. If you don't want to submit your quiz, \nthen you have the option to not submit after \nyou've attempted all questions.", "Pick answer", JOptionPane.ERROR_MESSAGE);
                                continue;
                            } else {
                                studentAnswers += ("Student's answer for question " + i + ": " + userChoice + "**");
                                break;
                            }
                        }
                    }
                    //student answered all questions , now submit
                    int ans;
                    while (true) {
                        ans = JOptionPane.showConfirmDialog(null, "You have answered all the questions, but you HAVEN'T submitted your quiz yet. \n" +
                                "If you want your quiz to be scored, you must submit your quiz for the teacher to see your work and give you a grade.\n" +
                                "Press the 'YES' button to submit\nPress the 'NO' button to NOT submit", "Would you like to submit your quiz?", JOptionPane.YES_NO_OPTION);
                        if (ans != JOptionPane.YES_OPTION && ans != JOptionPane.NO_OPTION) {
                            JOptionPane.showMessageDialog(null, "You must either choose to submit or not to submit", "Must click YES or NO", JOptionPane.ERROR_MESSAGE);
                        } else {
                            break;
                        }
                    }
                    /*
                     * Putting time stamp on submission
                     *  and making new submission object
                     */
                    Date theDateOfSubmission = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String theDate = dateFormat.format(theDateOfSubmission);
                    String quizSheet = finalQuestions + "**" + studentAnswers + "**Submission Date & Time: " + theDate;

                    System.out.println(quizSheet);
                    if (ans == JOptionPane.NO_OPTION) {
                        writer.write("dontSubmit");
                        writer.println();
                        writer.flush();
                    } else {
                        writer.write("submit");
                        writer.println();
                        writer.flush();

                        writer.write(finalQuestions);
                        writer.println();
                        writer.flush();

                        writer.write(studentAnswers);
                        writer.println();
                        writer.flush();

                        writer.write(theDate);
                        writer.println();
                        writer.flush();

                        JOptionPane.showMessageDialog(null, "You have successfully submitted your quiz" +
                                "\nHere is the date and time of your submission: " + theDate, "Successfully submitted quiz!", JOptionPane.INFORMATION_MESSAGE);
                    }


                    studentQuizMenu.setVisible(true);




                } else if (mesFromSer.equals("updateStudentQuizListWhenCourseEdited")) {
                    String courseJustEditted = bfr.readLine();
                    String listQuiz = bfr.readLine();
                    if (courseJustEditted.equals(courseStudentSeeing)) {
                        String finalListQuiz = "";
                        int whereDivider = listQuiz.indexOf("**");
                        while (whereDivider != -1) {
                            finalListQuiz += (listQuiz.substring(0, whereDivider) + "\n");
                            listQuiz = listQuiz.substring(whereDivider + 2);
                            whereDivider = listQuiz.indexOf("**");
                        }
                        allQuizzesForCourse.setText(finalListQuiz);
                    }
                } else if (mesFromSer.equals("cantGradeQuiz")) {
                    JOptionPane.showMessageDialog(null, "Can't find the quiz", "Can't grade quiz", JOptionPane.ERROR_MESSAGE);
                } else if (mesFromSer.equals("studentSeeSubs")) {
                    String submissionList = bfr.readLine();
                    String finalList = "";
                    int whereDivider = submissionList.indexOf("**");
                    while (whereDivider != -1) {
                        finalList += (submissionList.substring(0, whereDivider) + "\n");
                        submissionList = submissionList.substring(whereDivider + 2);
                        whereDivider = submissionList.indexOf("**");
                    }
                    listOfSubmissions.setText(finalList);
                } else if (mesFromSer.equals("teacherWantsToGrade")) {
                    String submissionList = bfr.readLine();
                    String finalList = "";
                    int whereDivider = submissionList.indexOf("**");
                    while (whereDivider != -1) {
                        finalList += (submissionList.substring(0, whereDivider) + "\n");
                        submissionList = submissionList.substring(whereDivider + 2);
                        whereDivider = submissionList.indexOf("**");
                    }
                    listToGrade.setText(finalList);
                } else if (mesFromSer.equals("gradeQuiz")) {
                    String quizSheet = bfr.readLine();
                    String formattedQuizSheet = "";
                    int whereDivider = quizSheet.indexOf("**");
                    while (whereDivider != -1) {
                        formattedQuizSheet += (quizSheet.substring(0, whereDivider) + "\n");
                        quizSheet = quizSheet.substring(whereDivider + 2);
                        whereDivider = quizSheet.indexOf("**");
                    }
                    int numQuestions = Integer.parseInt(bfr.readLine());
                    int numCorrect = 0;
                    String finalScoreString = "";

                    for (int i = 1; i <= numQuestions; i++) {
                        while (true) {
                            int pointForQuestion = JOptionPane.showConfirmDialog(null, formattedQuizSheet + "\nGive a point for Question " + i + "?", "Give point?", JOptionPane.YES_NO_OPTION);
                            if (pointForQuestion != JOptionPane.YES_OPTION && pointForQuestion != JOptionPane.NO_OPTION) {
                                JOptionPane.showMessageDialog(null, "You must finish grading the quiz.", "Must finish grading", JOptionPane.ERROR_MESSAGE);
                            } else {
                                if (pointForQuestion == JOptionPane.YES_OPTION) {
                                    numCorrect++;
                                    finalScoreString += ("Result for Question " + i + ": CORRECT (+1 point)**");
                                } else {
                                    finalScoreString += ("Result for Question " + i + ": INCORRECT (No points awarded)**");
                                }
                                break;
                            }
                        }
                    }
                    String percentScore = String.format("%.2f", (double) numCorrect / numQuestions * 100);
                    finalScoreString += ("Your score: " + numCorrect + "/" + numQuestions + ", or " + percentScore + "%" + "**");
                    writer.write(finalScoreString);
                    writer.println();
                    writer.flush();

                } else if (mesFromSer.equals("updateStudentSubList")) {
                    String studentEmail = bfr.readLine();
                    String newSubListForStu = bfr.readLine();
                    if (studentEmail.equals(emailLoggedIn)) {
                        String finalList = "";
                        int whereDivider = newSubListForStu.indexOf("**");
                        while (whereDivider != -1) {
                            finalList += (newSubListForStu.substring(0, whereDivider) + "\n");
                            newSubListForStu = newSubListForStu.substring(whereDivider + 2);
                            whereDivider = newSubListForStu.indexOf("**");
                            finalList += (newSubListForStu.substring(0, whereDivider) + "\n\n");
                            newSubListForStu = newSubListForStu.substring(whereDivider + 2);
                            whereDivider = newSubListForStu.indexOf("**");
                        }
                        listOfSubmissions.setText(finalList);
                    }
                } else if (mesFromSer.equals("updateTeacherSubList")) {
                    String email = bfr.readLine();
                    String submissionList = bfr.readLine();
                    if (emailLoggedIn.equals(email)) {
                        String finalList = "";
                        int whereDivider = submissionList.indexOf("**");
                        while (whereDivider != -1) {
                            finalList += (submissionList.substring(0, whereDivider) + "\n");
                            submissionList = submissionList.substring(whereDivider + 2);
                            whereDivider = submissionList.indexOf("**");
                        }
                        listToGrade.setText(finalList);
                    }
                } else if (mesFromSer.equals("updatedName")) {
                    String role = bfr.readLine();
                    String newName = bfr.readLine();
                    if (role.equals("teacher")) {
                        teacherLoginGreeting.setText("Welcome " + newName + "! You're a teacher");
                    } else {
                        studentLoginGreeting.setText("Welcome " + newName + "! You're a student");
                    }

                } else if (mesFromSer.equals("stop")) {
                    break;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

