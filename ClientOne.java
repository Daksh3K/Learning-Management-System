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
    static Socket theSocket;

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


    public static void main(String[] args) {
        Thread client1 = new Thread(new ClientOne());
        client1.start();
        try {
            Socket socket = new Socket("10.192.91.225", 1234);
            //System.out.println(InetAddress.getLocalHost());
            theSocket = socket;
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
                    writer.write("makingQuiz");
                    writer.println();
                    writer.flush();

                    writer.write(courseBeingEdited);
                    writer.println();
                    writer.flush();

                    writer.write(emailLoggedIn);
                    writer.println();
                    writer.flush();

                    String quizName = JOptionPane.showInputDialog(null, "Enter quiz name", "Making a new quiz!", JOptionPane.QUESTION_MESSAGE);
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
            };
            createQuiz.addActionListener(createQuizListener);

            JButton editQuiz = new JButton("Edit Quiz");
            ActionListener editQuizListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    writer.write("editingQuiz");
                    writer.println();
                    writer.flush();

                    writer.write(courseBeingEdited);
                    writer.println();
                    writer.flush();

                    String quizName = JOptionPane.showInputDialog(null, "Enter quiz name", "Making a new quiz!", JOptionPane.QUESTION_MESSAGE);
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
            };
            editQuiz.addActionListener(editQuizListener);

            JButton deleteQuiz = new JButton("Delete Quiz");
            ActionListener deleteQuizListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    writer.write("deleteQuiz");
                    writer.println();
                    writer.flush();
                    writer.write(courseBeingEdited);
                    writer.println();
                    writer.flush();

                    String quizToDelete = JOptionPane.showInputDialog(null, "Enter name of quiz you want to delete", "name of quiz you want to delete", JOptionPane.QUESTION_MESSAGE);
                    writer.write(quizToDelete);
                    writer.println();
                    writer.flush();
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
                    writer.write("makeCourse");
                    writer.println();
                    writer.flush();

                    writer.write(emailLoggedIn);
                    writer.println();
                    writer.flush();

                    String courseName = JOptionPane.showInputDialog(null, "Enter course name", "Making a new course!", JOptionPane.QUESTION_MESSAGE);
                    writer.write(courseName);
                    writer.println();
                    writer.flush();
                }
            };
            createCourse.addActionListener(createCourseListener);

            JButton editCourse = new JButton("Edit Course");  //THIS IS WHERE I LEFT OFF / MAKE THE QUIZ MENU JFRAME AND THEN WORK ON STUDENT STUFF
            ActionListener editCourseListener = new ActionListener() {  //EDITING COURSE
                @Override
                public void actionPerformed(ActionEvent e) {
                    writer.write("editCourse");
                    writer.println();
                    writer.flush();
                    String courseToEdit = JOptionPane.showInputDialog(null, "Type course you want to edit", "Editing Course", JOptionPane.QUESTION_MESSAGE);
                    writer.write(courseToEdit);
                    writer.println();
                    writer.flush();
                    courseBeingEdited = courseToEdit; // courseName
                }
            };
            editCourse.addActionListener(editCourseListener);

            JButton deleteCourse = new JButton("Delete Course");

            ActionListener delCourseListener = new ActionListener() {   //Deleting course
                @Override
                public void actionPerformed(ActionEvent e) {
                    writer.write("deleteCourse");
                    writer.println();
                    writer.flush();
                    String courseToDelete = JOptionPane.showInputDialog(null, "Enter the name of the course you want to delete", "Deleting course", JOptionPane.QUESTION_MESSAGE);
                    writer.write(courseToDelete);
                    writer.println();
                    writer.flush();

                    writer.write(emailLoggedIn);
                    writer.println();
                    writer.flush();
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
                    teacherMenu.setVisible(false);
                    studentMenu.setVisible(false);
                    startMenu.setVisible(true);
                }
            };
            logoutButton.addActionListener(logoutListener);

            tMenuContent.add(tMainJpanel, BorderLayout.CENTER);
            teacherMenu.setVisible(false);


            /**
             * Student stuff =======================================================================
             */

            /**
             * Student sees quizzes for course they are seeing
             */
            Container studentQuizContent = studentQuizMenu.getContentPane();
            studentQuizContent.setLayout(new BorderLayout());
            studentQuizMenu.setSize(400, 400);
            studentQuizMenu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            studentQuizMenu.setVisible(false);
            allQuizzesForCourse.setEditable(false);


            JPanel stuQuizJpanel = new JPanel();
            stuQuizJpanel.add(allQuizzesForCourse, BorderLayout.NORTH);

            JPanel stuQuizButtonJpanel = new JPanel();

            JButton takeQuizButton = new JButton("Take Quiz");
            stuQuizButtonJpanel.add(takeQuizButton, BorderLayout.SOUTH);
            ActionListener takeQuizListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

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


            JPanel studentCourseJpanel = new JPanel();
            studentCourseJpanel.add(allCoursesList, BorderLayout.NORTH);
            JPanel studentCourseJPanelForButton = new JPanel();

            JButton stuViewCourse = new JButton("View Course's quizzes");
            studentCourseJPanelForButton.add(stuViewCourse, BorderLayout.SOUTH);
            ActionListener stuViewCourseListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    writer.write("studentViewCourse");
                    writer.println();
                    writer.flush();

                    String course = JOptionPane.showInputDialog(null, "Enter course you want to access", "Enter course name", JOptionPane.QUESTION_MESSAGE);
                    writer.write(course);
                    writer.println();
                    writer.flush();
                    courseStudentSeeing = course;
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
            studentMenu.setSize(400, 400);
            studentMenu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  //change to DoNothingonclose and force teacher to press logout to leave
            studentMenu.setVisible(false);

            JPanel sMainJpanel = new JPanel();
            JLabel sMainMessage = new JLabel("Welcome Student! Press a button to start");

            sMainJpanel.add(sMainMessage, BorderLayout.NORTH);

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
            sMainJpanel.add(studentViewCourse, BorderLayout.SOUTH);

            JButton viewGradesButton = new JButton("See your grades");
            ActionListener viewGradeListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

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
                    writer.write("changeName");
                    writer.println();
                    writer.flush();

                    writer.write(newName);
                    writer.println();
                    writer.flush();
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
            studentContent.add(sMainJpanel, BorderLayout.CENTER);






















































            /**
             * WHAT HAPPENS GUI WISE WHEN USER CREATES ACCOUNT
             */
            ActionListener createAccListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == createAccBut) {
                        writer.write("createAccount");
                        writer.println();
                        writer.flush();

                        String name = JOptionPane.showInputDialog(null, "Enter name", "Enter name", JOptionPane.QUESTION_MESSAGE);
                        writer.write(name);
                        writer.println();
                        writer.flush();
                        String email = "";
                        email = JOptionPane.showInputDialog(null, "Enter email", "Enter email", JOptionPane.QUESTION_MESSAGE);
                        writer.write(email);
                        writer.println();
                        writer.flush();







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
                    writer.write("loginAccount");
                    writer.println();
                    writer.flush();
                    String enteredEmail = JOptionPane.showInputDialog(null, "Enter your email to login", "Login", JOptionPane.QUESTION_MESSAGE);
                    writer.write(enteredEmail);
                    writer.println();
                    writer.flush();
                    emailLoggedIn = enteredEmail;
//                        /**
//                         * Starting from this point, user is logged in.
//                         */
//                        startMenu.setVisible(false);
//
//                        for (People p: getPeopleListInFile()) {
//                            if (p.getEmail().equals(enteredEmail)) {
//                                if (p instanceof Student) {  //student logged in
//                                    Student user = (Student) p;
//                                    emailLoggedIn = user.getEmail();
//
//                                } else {    //teacher logged in
//                                    Teacher user = (Teacher) p;
//                                    emailLoggedIn = user.getEmail();
//                                    teacherMenu.setVisible(true);
//
//                                }
//                            }
//                        }
//
//
//
//                    } else if (enteredEmail != null){
//                        JOptionPane.showMessageDialog(null, "Invalid Account Credentials", "Invalid Email", JOptionPane.ERROR_MESSAGE);
//                    }
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
                if (mesFromSer.equals("yes")) {
                    System.out.println("OKAY CREATING ACCOUNT");
                    int role = JOptionPane.showConfirmDialog(null, "Are you a student or teacher\nPress 'Yes' if student, 'No' if teacher", "Student or Teacher?", JOptionPane.YES_NO_OPTION);
                    String stuOrTeach = Integer.toString(role);

                    writer.write(stuOrTeach);
                    writer.println();
                    writer.flush();
                    System.out.println("Account made");
                    continue;
                } else if (mesFromSer.equals("no")) {
                    JOptionPane.showMessageDialog(null, "Cant make acc", "no", JOptionPane.ERROR_MESSAGE);
                    continue;
                } else if (mesFromSer.equals("accNotFound")) {
                    JOptionPane.showMessageDialog(null, "Cant find ur account", "No account", JOptionPane.ERROR_MESSAGE);
                    continue;
                } else if (mesFromSer.equals("loggedIn")) {
                    String role = bfr.readLine();
                    startMenu.setVisible(false);
                    if (role.equals("teacher")) {
                        teacherMenu.setVisible(true);
                    } else {  //if student logged in
                        studentMenu.setVisible(true);
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
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

