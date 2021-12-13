import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.*;
import java.io.*;
import javax.swing.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.text.*;

/**
 * Project 5 - ClientOne class
 *
 *  This class is the client side code. When a user wants to utilize our application
 *  , they run this class in order to connect to the server. Once the connection is made, that means
 *  a client will have a socket to the server, and the server will have a socket to the client.
 *
 *  This class will display the GUIs (JFrames and JOptionPanes) for the users to interact with, and their actions
 *  will cause information to be sent from the client to the server. Depending on what the user is doing, the server
 *  will receive the information sent from the client, do the necessary actions, and send information back to the
 *  client so things like real time updates can be displayed.
 *
 *  More information can be found in the documentation that's in the Readme file that's in our repository.
 *
 * Lab Section L24
 *
 * @author Yu Hyun Kim
 * @version final version 12/12/21
 */
public class ClientOne implements Runnable {
    static BufferedReader bfr;  //BufferedReader object
    static PrintWriter writer;  //PrintWriter object
    static Socket theSocket;  //socket object
    static String emailLoggedIn;  //email of user that's logged in

    /**
     * startMenu related GUI variables
     */
    static JFrame startMenu = new JFrame("Start Menu");  //start menu JFrame

    static JLabel studentLoginGreeting = new JLabel();  //Jlabel for student welcome message
    static JLabel teacherLoginGreeting = new JLabel();  //JLabel for teacher welcome message

    /**
     * GUI related to teacher
     */
    static JFrame teacherGradeMenu = new JFrame("Submissions to grade");  //JFrame that shows quizzes to grade
    static JTextArea listToGrade = new JTextArea();  //JLabel for list of quizzes to grade

    static JFrame teacherMenu = new JFrame("Welcome! You are a teacher!");  //JFrame for teacher Menu

    static JFrame coursesMenu = new JFrame("Your Created Courses");  //JFrame for teacher to see courses
    static JTextArea listOfCourses = new JTextArea();  //lists teacher's courses

    static JFrame quizMenu = new JFrame("Manage quizzes");  //JFrame for teacher to see their quizzes
    static JTextArea listOfQuizzesInCourse = new JTextArea();  //JLabel to list the quizzes made

    static String courseBeingEdited;  //the course that's being edited by the teacher
    /**
     * GUI related to student
     */
    static JFrame studentMenu = new JFrame("Welcome! You are a student!");  //JFrame for student Menu

    static JFrame studentCourseMenu = new JFrame("Here are all the courses that exist");  //JFrame to display
    //all courses
    static JTextArea allCoursesList =  new JTextArea();  //lists all courses

    static String courseStudentSeeing = "";  //the course the student is seeing

    static JFrame studentQuizMenu = new JFrame("Here are all the quizzes");  //JFrame for student to see
    //quizzes for specific course
    static JTextArea allQuizzesForCourse = new JTextArea();  //JTextArea that lists all Quizzes in course

    static JFrame studentSubmissionMenu = new JFrame("Here are all your submissions");  //JFrame for
    //student to see submissions
    static JTextArea listOfSubmissions = new JTextArea();  //lists submissions

    public static void main(String[] args) {
        Thread client1 = new Thread(new ClientOne());  //creates a new thread
        client1.start();  //starts the thread
        try {
            Socket socket = new Socket("localhost", 1234);  //socket object
            theSocket = socket;
            bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());

            /**
             * WE ARE CREATING START MENU
             * CREATE ACCOUNT/LOGIN/EXIT
             *
             * We are making the content panes, JPanels, JButtons, and the ActionListeners
             */
            Container startContent = startMenu.getContentPane();
            JPanel startJpanel = new JPanel();
            JLabel welcomeMessage = new JLabel("Welcome. Press a button to start");
            startContent.setLayout(new BorderLayout());
            startMenu.setSize(320, 250);
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
             * What happens when user presses the "Create account button"
             *
             * Asks user for account info, tells the server that account is being made, and sends the
             * account info to the server
             */
            ActionListener createAccListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == createAccBut) {
                        String name = JOptionPane.showInputDialog(null,
                                "Enter name", "Enter name", JOptionPane.QUESTION_MESSAGE);  //name
                        if (name != null) {
                            String email = "";  //email
                            email = JOptionPane.showInputDialog(null,
                                    "Enter email", "Enter email", JOptionPane.QUESTION_MESSAGE);
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
             * What happens when login button is pressed
             *
             * User enters their email, and the client sends a String to the server saying that a user wants to login
             * and client sends the email that the user entered.
             */
            ActionListener loginListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String enteredEmail = JOptionPane.showInputDialog(null,
                            "Enter your email to login", "Login", JOptionPane.QUESTION_MESSAGE);  //email
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
             * WE ARE CREATING THE GRADE SUBMISSIONS MENU FOR THE TEACHER
             *
             * We are making the content panes, JPanels, JButtons, and the ActionListeners
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

            /**
             * What happens when teacher wants to grade quiz
             *
             * Teacher enters the quiz name they want to grade, and information is sent to the server
             */
            ActionListener teacherGradingListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String quizName = JOptionPane.showInputDialog(null, "Enter quiz to grade",
                            "enter quiz name", JOptionPane.QUESTION_MESSAGE);
                    if (quizName != null) {
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
             * CREATING THE QUIZ MENU FOR THE TEACHER
             *
             * We are making the content panes, JPanels, JButtons, and the ActionListeners
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

            /**
             * What happens when teacher wants to create quiz
             *
             * Teacher enters all quiz info, and then information is sent to the server
             */
            ActionListener createQuizListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String quizName = JOptionPane.showInputDialog(null, "Enter quiz name",
                            "Making a new quiz!", JOptionPane.QUESTION_MESSAGE);  //quiz name
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
                        /**
                         * asks for number of questions
                         */
                        while (true) {
                            try {
                                numberOfQuestions = JOptionPane.showInputDialog(null,
                                        "Enter the number of questions for the quiz",
                                        "Number of questions", JOptionPane.QUESTION_MESSAGE);
                                if (numberOfQuestions == null) {
                                    JOptionPane.showMessageDialog(null, "You must finish " +
                                            "making the quiz", "Must make quiz", JOptionPane.ERROR_MESSAGE);
                                    continue;
                                }
                                numQuestions = Integer.parseInt(numberOfQuestions);
                                if (numQuestions < 1) {
                                    JOptionPane.showMessageDialog(null,
                                            "Make sure to enter a number equal to or greater than 1 " +
                                                    "next time!", "Error with number of questions",
                                            JOptionPane.ERROR_MESSAGE);
                                } else {
                                    break;
                                }
                            } catch (Exception numConvError) {
                                JOptionPane.showMessageDialog(null,
                                        "Make sure to enter a valid number and make sure " +
                                                "it's 1 or greater next time!", "Error with number of questions",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        //WE HAVE NUM QUESTIONS
                        /**
                         * asks for number of answers
                         */
                        String numAnswers;
                        int numAns = 0;
                        while (true) {
                            try {
                                numAnswers = JOptionPane.showInputDialog(null,
                                        "Enter the number of answer choices for each question for the quiz",
                                        "Number of answer choices per question", JOptionPane.QUESTION_MESSAGE);
                                //number of answer choices for each question
                                if (numAnswers == null) {
                                    JOptionPane.showMessageDialog(null, "You must finish " +
                                            "making the quiz", "Must make quiz", JOptionPane.ERROR_MESSAGE);
                                    continue;
                                }
                                numAns = Integer.parseInt(numAnswers);
                                if (numAns < 1) {
                                    JOptionPane.showMessageDialog(null, "Make sure to " +
                                            "enter a number equal to or greater than 1 next time!",
                                            "Error with " +
                                            "number of answers", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    break;
                                }
                            } catch (Exception numConvError) {
                                JOptionPane.showMessageDialog(null,
                                        "Make sure to enter a valid number and make sure it's 1 or " +
                                                "greater next time!", "Error with number of answers",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        //we have num answers

                        String questionString = "";  //String of questions
                        String answerString = "";  //String of answer choices

                        /**
                         * Get the question and the answer choices for each question
                         */
                        for (int i = 1; i <= numQuestions; i++) {
                            while (true) {
                                String question = JOptionPane.showInputDialog(null,
                                        "Enter the question for Question " + i + "\n***NOTE: " +
                                                "YOU CANNOT HAVE '*' IN THE QUESTION", "Enter question",
                                        JOptionPane.QUESTION_MESSAGE);
                                //the question
                                if (question == null) {
                                    JOptionPane.showMessageDialog(null,
                                            "You must finish making this quiz. Please type your question.",
                                            "Must finish making quiz", JOptionPane.ERROR_MESSAGE);
                                } else if (question.length() == 0) {
                                    JOptionPane.showMessageDialog(null,
                                            "You didn't enter any characters. Try again",
                                            "Enter a question please", JOptionPane.ERROR_MESSAGE);
                                } else if (question.indexOf("*") != -1) {
                                    JOptionPane.showMessageDialog(null,
                                            "You can't have '*' anywhere in your question.",
                                            "No '*' in question", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    questionString += (question + "**");  //question successfully made and break
                                    // from while loop to get answer choices;
                                    break;
                                }
                            }
                            for (int j = 1; j <= numAns; j++) {
                                while (true) {
                                    String answerChoice = JOptionPane.showInputDialog(null,
                                            "Enter answer choice " + j + "\n***NOTE: YOU CANNOT HAVE '*' " +
                                                    "IN ANSWER CHOICE", "Enter answer choice",
                                            JOptionPane.QUESTION_MESSAGE);
                                            //answer choice
                                    if (answerChoice == null) {
                                        JOptionPane.showMessageDialog(null,
                                                "You must finish making this quiz. Please type your" +
                                                        " answer choice.", "Must finish making quiz",
                                                JOptionPane.ERROR_MESSAGE);
                                    } else if (answerChoice.length() == 0) {
                                        JOptionPane.showMessageDialog(null,
                                                "You didn't enter any characters. Try again",
                                                "Enter something please", JOptionPane.ERROR_MESSAGE);
                                    } else if (answerChoice.indexOf("*") != -1) {
                                        JOptionPane.showMessageDialog(null,
                                                "You can't have '*' anywhere in your answer choice.",
                                                "No '*' in answer choice", JOptionPane.ERROR_MESSAGE);
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

                        /**
                         * asks the teacher whether they want to randomize the quiz or not
                         */
                        while (true) {
                            int randomOrNot = JOptionPane.showConfirmDialog(null,
                                    "Do you want the quiz to be randomized",
                                    "Should quiz be randomized?", JOptionPane.YES_NO_OPTION);
                            if (randomOrNot != JOptionPane.YES_OPTION && randomOrNot != JOptionPane.NO_OPTION) {
                                JOptionPane.showMessageDialog(null,
                                        "You must select yes or no.", "Select yes or no",
                                        JOptionPane.ERROR_MESSAGE);
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

            /**
             * What happens if the teacher wants to edit the quiz.
             *
             * The process is pretty similar to a teacher making a quiz, which is shown above.
             */
            ActionListener editQuizListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String quizName = JOptionPane.showInputDialog(null,
                            "Enter the name of the quiz that you want to change" +
                            "\n**Note: If you enter in an invalid quiz name,\nyou will still be prompted to " +
                                    "enter\nthe new information regarding the quiz, but\nafter " +
                            "you put in all the info, you will receive an\nerror message, and no changes will " +
                                    "be made.", "Editing a quiz!", JOptionPane.QUESTION_MESSAGE);
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
                                numberOfQuestions = JOptionPane.showInputDialog(null,
                                        "Enter the number of questions for the quiz",
                                        "Number of questions", JOptionPane.QUESTION_MESSAGE);
                                if (numberOfQuestions == null) {
                                    JOptionPane.showMessageDialog(null,
                                            "You must finish editing the quiz", "Must edit quiz",
                                            JOptionPane.ERROR_MESSAGE);
                                    continue;
                                }
                                numQuestions = Integer.parseInt(numberOfQuestions);

                                if (numQuestions < 1) {
                                    JOptionPane.showMessageDialog(null,
                                            "Make sure to enter a number equal to or greater " +
                                                    "than 1 next time!", "Error with number of questions",
                                            JOptionPane.ERROR_MESSAGE);
                                } else {
                                    break;
                                }
                            } catch (Exception numConvError) {
                                JOptionPane.showMessageDialog(null, "Make sure to enter a " +
                                        "valid number and make sure it's 1 or greater next time!", "Error with " +
                                        "number of questions", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        //WE HAVE NUM QUESTIONS
                        String numAnswers;
                        int numAns = 0;
                        while (true) {
                            try {
                                numAnswers = JOptionPane.showInputDialog(null,
                                        "Enter the number of answer choices for each question",
                                        "Number of answer choices per question", JOptionPane.QUESTION_MESSAGE);
                                if (numAnswers == null) {
                                    JOptionPane.showMessageDialog(null,
                                            "You must finish editing the quiz", "Must edit quiz",
                                            JOptionPane.ERROR_MESSAGE);
                                    continue;
                                }
                                numAns = Integer.parseInt(numAnswers);

                                if (numAns < 1) {
                                    JOptionPane.showMessageDialog(null,
                                            "Make sure to enter a number equal to or greater than 1 " +
                                                    "next time!", "Error with number of answers",
                                            JOptionPane.ERROR_MESSAGE);
                                } else {
                                    break;
                                }
                            } catch (Exception numConvError) {
                                JOptionPane.showMessageDialog(null, "Make sure to enter " +
                                        "a valid number and make sure it's 1 or greater next time!",
                                        "Error with number of answers", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        //we have num answers

                        String questionString = "";
                        String answerString = "";

                        for (int i = 1; i <= numQuestions; i++) {
                            while (true) {
                                String question = JOptionPane.showInputDialog(null,
                                        "Enter the question for Question " + i +
                                                "\n***NOTE: YOU CANNOT HAVE '*' IN THE QUESTION",
                                        "Enter question", JOptionPane.QUESTION_MESSAGE);
                                if (question == null) {
                                    JOptionPane.showMessageDialog(null,
                                            "You must finish making this quiz. Please type your question.",
                                            "Must finish making quiz", JOptionPane.ERROR_MESSAGE);
                                } else if (question.length() == 0) {
                                    JOptionPane.showMessageDialog(null,
                                            "You didn't enter any characters. Try again",
                                            "Enter a question please", JOptionPane.ERROR_MESSAGE);
                                } else if (question.indexOf("*") != -1) {
                                    JOptionPane.showMessageDialog(null,
                                            "You can't have '*' anywhere in your question.",
                                            "No '*' in question", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    questionString += (question + "**");  //question successfully made and
                                    // break from while loop to get answer choices;
                                    break;
                                }
                            }
                            for (int j = 1; j <= numAns; j++) {
                                while (true) {
                                    String answerChoice = JOptionPane.showInputDialog(null,
                                            "Enter answer choice " + j + "\n***NOTE: YOU CANNOT HAVE '*' " +
                                                    "IN ANSWER CHOICE", "Enter answer choice",
                                            JOptionPane.QUESTION_MESSAGE);
                                    if (answerChoice == null) {
                                        JOptionPane.showMessageDialog(null,
                                                "You must finish making this quiz. " +
                                                        "Please type your answer choice.",
                                                "Must finish making quiz", JOptionPane.ERROR_MESSAGE);
                                    } else if (answerChoice.length() == 0) {
                                        JOptionPane.showMessageDialog(null,
                                                "You didn't enter any characters. Try again",
                                                "Enter something please", JOptionPane.ERROR_MESSAGE);
                                    } else if (answerChoice.indexOf("*") != -1) {
                                        JOptionPane.showMessageDialog(null,
                                                "You can't have '*' anywhere in your answer choice.",
                                                "No '*' in answer choice", JOptionPane.ERROR_MESSAGE);
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
                            int randomOrNot = JOptionPane.showConfirmDialog(null,
                                    "Do you want the quiz to be randomized",
                                    "Should quiz be randomized?", JOptionPane.YES_NO_OPTION);
                            if (randomOrNot != JOptionPane.YES_OPTION && randomOrNot != JOptionPane.NO_OPTION) {
                                JOptionPane.showMessageDialog(null,
                                        "You must select yes or no.",
                                        "Select yes or no", JOptionPane.ERROR_MESSAGE);
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

            /**
             * What happens when a teacher wants to delete a quiz
             *
             * The quiz name is sent to the server, and the server process that info and
             * acts accordingly
             */
            ActionListener deleteQuizListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String quizToDelete = JOptionPane.showInputDialog(null,
                            "Enter name of quiz you want to delete",
                            "name of quiz you want to delete", JOptionPane.QUESTION_MESSAGE);
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
             * CREATING THE COURSES MENU FOR THE TEACHER
             *
             * We are making the content panes, JPanels, JButtons, and the ActionListeners
             */

            Container coursesContent = coursesMenu.getContentPane();
            coursesContent.setLayout(new BorderLayout());

            coursesMenu.setSize(600, 400);
            coursesMenu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

            /**
             * What happens when teacher wants to create course
             *
             * Get the course info and send it to the server
             */
            ActionListener createCourseListener = new ActionListener() {  //CREATING COURSE
                @Override
                public void actionPerformed(ActionEvent e) {
                    String courseName = JOptionPane.showInputDialog(null,
                            "Enter course name", "Making a new course!", JOptionPane.QUESTION_MESSAGE);
                                //course name trying to be made
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
            JButton editCourse = new JButton("Edit Course");

            /**
             * What happens when the teacher wants to edit a course
             *
             * Info is sent to the server regarding the course that's trying to be edited
             */
            ActionListener editCourseListener = new ActionListener() {  //EDITING COURSE
                @Override
                public void actionPerformed(ActionEvent e) {
                    String courseToEdit = JOptionPane.showInputDialog(null,
                            "Type course you want to edit", "Editing Course",
                            JOptionPane.QUESTION_MESSAGE);  //course name of course trying to be edited
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

            /**
             * What happens when the teacher wants to delete the course
             *
             * Info is sent to the server regarding the course that's trying to be deleted
             */
            ActionListener delCourseListener = new ActionListener() {   //Deleting course
                @Override
                public void actionPerformed(ActionEvent e) {
                    String courseToDelete = JOptionPane.showInputDialog(null,
                            "Enter the name of the course you want to delete",
                            "Deleting course", JOptionPane.QUESTION_MESSAGE);  //course name of course
                            //trying to be deleted
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
             * Creating JFrame for teacher menu.
             * 1) Manage Courses/Quizzes
             * 2) Grade Quizzes
             * 3) Edit Name
             * 4) Delete
             * 5) Logout
             *
             * We are making all the Containers, JPanels, Buttons, and ActionListeners
             */
            Container tMenuContent = teacherMenu.getContentPane();
            tMenuContent.setLayout(new BorderLayout());
            teacherMenu.setSize(700, 200);
            teacherMenu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

            /**
             * When teacher presses the button that shows them the courses they made
             */
            ActionListener manageCourseButton = new ActionListener() {
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
             * What happens when teacher presses button that shows them the submissions to grade
             */
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
                    String newName = JOptionPane.showInputDialog(null,
                            "Enter new name", "Name change", JOptionPane.QUESTION_MESSAGE);
                                //new name of teacher
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

                }
            };
            deleteButton.addActionListener(deleteAccListener);
            JButton logoutButton = new JButton("Logout");
            tMainJpanel.add(logoutButton, BorderLayout.SOUTH);

            /**
             * If teacher wants to logout
             */
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
             * Creating JFrame for student to see their submissions
             *
             * Making the Container, JPanels, JButtons, ActionListeners, and TextAreas
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
             * Making JFrame that enables student to see quizzes they can take for a course
             *
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

            /**
             * What happens when student wants to take quiz
             */
            ActionListener takeQuizListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String quizTaking = JOptionPane.showInputDialog(null,
                            "Enter the quiz you want to take", "Enter quiz name",
                            JOptionPane.QUESTION_MESSAGE);  //quiz name student is trying to take
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
             * Creating JFrame for student to see all courses that exist
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

            /**
             * what happens when student is trying to view a course
             */
            ActionListener stuViewCourseListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String course = JOptionPane.showInputDialog(null,
                            "Enter course you want to access", "Enter course name",
                            JOptionPane.QUESTION_MESSAGE);  //course name of course student trying to see
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
             * CREATING STUDENT MAIN MENU
             *
             * 1) See courses, take quizzes
             * 2) See grades
             * 3) Edit name
             * 4) Delete account
             * 5) Logout
             */
            Container studentContent = studentMenu.getContentPane();
            studentContent.setLayout(new BorderLayout());
            studentMenu.setSize(700, 200);
            studentMenu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

            /**
             * when student presses the button that causes them to be taken to JFrame where they can
             * see all courses
             */
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

            /**
             * When student presses button that indicates they want to see their grades/submissions
             */
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

            /**
             * when student wants to edit name
             */
            ActionListener stuNameListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newName = JOptionPane.showInputDialog(null,
                            "Enter new name", "Name change", JOptionPane.QUESTION_MESSAGE);
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

            /**
             * when student wants to delete account
             */
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

            /**
             * when student wants to log out
             */
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


    /**
     * The run() method is executed after the thread made in the top of the main method is started
     *
     * The purpose of this run method is to constantly wait for any information that the server sends to the client.
     * Depending on what the user is trying to do, there may be times in the run() method where information will be
     * sent to the server as well.
     *
     * Real time updates are able to happen due to this run method, as well as displaying error messages when the
     * user tries to do something that they can't do. One example would be if a user is trying to create an account
     * but the email they are using is already used. In that case, information will be sent from the server to the
     * client telling it that the account can't be made, and the error message is displayed to inform the user of
     * the issue.
     */
    public void run() {
        while (true) {
            try {
                String mesFromSer;  //String that stores the String that the server sends to the client
                try {
                    mesFromSer = bfr.readLine();
                } catch (Exception e) {
                    continue;
                }
                if (mesFromSer.equals("yes")) {  //when account can be made
                    int role = JOptionPane.showConfirmDialog(null,
                            "Are you a student or teacher\nPress 'Yes' if student, 'No' if teacher",
                            "Student or Teacher?", JOptionPane.YES_NO_OPTION);
                    if (role == JOptionPane.YES_OPTION || role == JOptionPane.NO_OPTION) {
                        String stuOrTeach = Integer.toString(role);
                        writer.write(stuOrTeach);
                        writer.println();
                        writer.flush();
                    }
                } else if (mesFromSer.equals("no")) {  //when account CANT be made
                    JOptionPane.showMessageDialog(null, "It seems like the email you " +
                            "entered is already used.\nChoose another email",
                            "Email already used/Can't make account", JOptionPane.ERROR_MESSAGE);
                } else if (mesFromSer.equals("accNotFound")) {  //when account can't be found with the
                                                                //email the user entered when trying to log in
                    JOptionPane.showMessageDialog(null, "Invalid login credential",
                            "Your account wasn't found", JOptionPane.ERROR_MESSAGE);
                } else if (mesFromSer.equals("loggedIn")) {  //when the user successfully logs in
                    String role = bfr.readLine();
                    String name = bfr.readLine();
                    startMenu.setVisible(false);
                    if (role.equals("teacher")) {  //if a teacher logged in
                        teacherMenu.setVisible(true);
                        teacherLoginGreeting.setText("Welcome " + name + "! You're a teacher");
                    } else {  //if a student logged in
                        studentMenu.setVisible(true);
                        studentLoginGreeting.setText("Welcome " + name + "! You're a student");
                    }
                } else if (mesFromSer.equals("manageCourse")) {  //when teacher wants to manage course
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
                    listOfCourses.setText(finalListCourses);
                } else if (mesFromSer.equals("cantMakeCourse")) {  //when course can't be made
                    JOptionPane.showMessageDialog(null, "The course name you selected " +
                            "is either being used by you or another teacher.\nPlease use a " +
                            "different course name next time", "Can't make course", JOptionPane.ERROR_MESSAGE);
                } else if (mesFromSer.equals("makeCourse")) {  //when course CAN be made
                    String newListOfCourses = bfr.readLine();
                    String finalListCourses = "";
                    int whereDivider = newListOfCourses.indexOf("**");
                    while (whereDivider != -1) {
                        finalListCourses += (newListOfCourses.substring(0, whereDivider) + "\n");
                        newListOfCourses = newListOfCourses.substring(whereDivider + 2);
                        whereDivider = newListOfCourses.indexOf("**");
                    }
                    listOfCourses.setText(finalListCourses);
                } else if (mesFromSer.equals("cantDeleteCourse")) {  //when course CANT be deleted
                    JOptionPane.showMessageDialog(null,
                            "We couldn't delete your course for either of these 2 reasons:" +
                            "\n1) The course name you entered can't be found" +
                            "\n2) You have quizzes within the course you are trying to delete." +
                            "\nIn order to delete a course, make sure there are 0 quizzes for that course.",
                            "Couldn't delete course", JOptionPane.ERROR_MESSAGE);
                } else if (mesFromSer.equals("deleteCourse")) {  //when course CAN be deleted
                    String listCourse = bfr.readLine();
                    String finalListCourses = "";
                    int whereDivider = listCourse.indexOf("**");
                    while (whereDivider != -1) {
                        finalListCourses += (listCourse.substring(0, whereDivider) + "\n");
                        listCourse = listCourse.substring(whereDivider + 2);
                        whereDivider = listCourse.indexOf("**");
                    }
                    listOfCourses.setText(finalListCourses);
                } else if (mesFromSer.equals("cantEditCourse")) {  //when course CANT be edited
                    JOptionPane.showMessageDialog(null, "Can't find the course",
                            "Can't find course", JOptionPane.ERROR_MESSAGE);
                } else if (mesFromSer.equals("editCourse")) {  //when course CAN be edited
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
                } else if (mesFromSer.equals("cantMakeQuiz")) {  //when quiz CANT be made
                    JOptionPane.showMessageDialog(null, "You cannot have multiple" +
                            " quizzes with the same name.", "Cannot make quiz", JOptionPane.ERROR_MESSAGE);

                } else if (mesFromSer.equals("makeQuiz")) {  //when quiz can be made
                    String listQuiz = bfr.readLine();
                    String finalListQuiz = "";
                    int whereDivider = listQuiz.indexOf("**");
                    while (whereDivider != -1) {
                        finalListQuiz += (listQuiz.substring(0, whereDivider) + "\n");
                        listQuiz = listQuiz.substring(whereDivider + 2);
                        whereDivider = listQuiz.indexOf("**");
                    }
                    listOfQuizzesInCourse.setText(finalListQuiz);
                } else if (mesFromSer.equals("cantDeleteQuiz")) {  //when quiz CANT be deleted
                    JOptionPane.showMessageDialog(null, "Couldn't find the quiz",
                            "Can't find quiz", JOptionPane.ERROR_MESSAGE);
                } else if (mesFromSer.equals("deleteQuiz")) {  //when quiz can be deleted
                    String listQuiz = bfr.readLine();
                    String finalListQuiz = "";
                    int whereDivider = listQuiz.indexOf("**");
                    while (whereDivider != -1) {
                        finalListQuiz += (listQuiz.substring(0, whereDivider) + "\n");
                        listQuiz = listQuiz.substring(whereDivider + 2);
                        whereDivider = listQuiz.indexOf("**");
                    }
                    listOfQuizzesInCourse.setText(finalListQuiz);
                } else if (mesFromSer.equals("cantEditQuiz")) {  //when quiz cant be edited
                    JOptionPane.showMessageDialog(null, "Can't find your quiz " +
                            "with the name you entered, so" +
                            "\n no changes have been made", "Can't find your quiz", JOptionPane.ERROR_MESSAGE);
                } else if (mesFromSer.equals("editQuiz")) {  //when quiz can be edited
                    JOptionPane.showMessageDialog(null, "Successfully edited quiz",
                            "Success!", JOptionPane.INFORMATION_MESSAGE);
                } else if (mesFromSer.equals("showStudentCourses")) {  //when student wants to see courses
                    String allCourses = bfr.readLine();
                    String finalListCourses = "";
                    int whereDivider = allCourses.indexOf("**");
                    while (whereDivider != -1) {
                        finalListCourses += (allCourses.substring(0, whereDivider) + "\n");
                        allCourses = allCourses.substring(whereDivider + 2);
                        whereDivider = allCourses.indexOf("**");
                    }
                    allCoursesList.setText(finalListCourses);
                } else if (mesFromSer.equals("cantFindCourse")) {  //when student can't access the course that
                                                                    //they specified
                    JOptionPane.showMessageDialog(null, "Can't find the course you entered",
                            "Can't find course", JOptionPane.ERROR_MESSAGE);
                } else if (mesFromSer.equals("foundCourse")) {  //when student is able to access course
                    studentCourseMenu.setVisible(false);
                    studentQuizMenu.setVisible(true);

                } else if (mesFromSer.equals("editStudentCourseList")) {  //real time updates to display
                                                                            //all the courses for student
                    String allCourses = bfr.readLine();
                    String finalListCourses = "";
                    int whereDivider = allCourses.indexOf("**");
                    while (whereDivider != -1) {
                        finalListCourses += (allCourses.substring(0, whereDivider) + "\n");
                        allCourses = allCourses.substring(whereDivider + 2);
                        whereDivider = allCourses.indexOf("**");
                    }
                    allCoursesList.setText(finalListCourses);
                } else if (mesFromSer.equals("updateStudentQuizList")) {  //real time updates to display all quizzes
                                                                            //for student
                    String listQuiz = bfr.readLine();
                    String finalListQuiz = "";
                    int whereDivider = listQuiz.indexOf("**");
                    while (whereDivider != -1) {
                        finalListQuiz += (listQuiz.substring(0, whereDivider) + "\n");
                        listQuiz = listQuiz.substring(whereDivider + 2);
                        whereDivider = listQuiz.indexOf("**");
                    }
                    allQuizzesForCourse.setText(finalListQuiz);
                } else if (mesFromSer.equals("cantTakeQuiz")) {  //when student enters a quiz name that doesn't exist,
                                                                //therefore making them unable to take a quiz
                    JOptionPane.showMessageDialog(null, "Can't find quiz",
                            "Can't find the quiz", JOptionPane.ERROR_MESSAGE);
                } else if (mesFromSer.equals("takeQuiz")) {  //when student is able to take the quiz they specified
                    studentQuizMenu.setVisible(false);
                    String questions = bfr.readLine();
                    String answers = bfr.readLine();
                    int numQuestions = Integer.parseInt(bfr.readLine());
                    int numAns = Integer.parseInt(bfr.readLine());

                    String finalQuestions = "";  //print this for all questions
                    String studentAnswers = "";
                    for (int i = 1; i <= numQuestions; i++) {
                        int whereDivider = questions.indexOf("**");
                        String currentQuestion = ("Question " + i + ": " + questions.substring(0, whereDivider));
                        finalQuestions += (currentQuestion + "**");
                        questions = questions.substring(whereDivider + 2);
                        //Questions list is asked
                        String[] answerSet = new String[numAns];
                        for (int j = 0; j < numAns; j++) {
                            int whereDividerAns = answers.indexOf("**");
                            answerSet[j] = answers.substring(0, whereDividerAns);
                            answers = answers.substring(whereDividerAns + 2);
                        }  //populate AnswerSet
                        while (true) {
                            String userChoice = (String) JOptionPane.showInputDialog(null,
                                    currentQuestion + "\n\n" + "Select answer for Question " + i,
                                    "Enter your answer", JOptionPane.QUESTION_MESSAGE,
                                    null, answerSet, answerSet[0]);
                            if (userChoice == null) {
                                JOptionPane.showMessageDialog(null,
                                        "You must finish taking your quiz. If you don't want to " +
                                                "submit your quiz, \nthen you have the option to not submit " +
                                                "after \nyou've attempted all questions.", "Pick answer",
                                        JOptionPane.ERROR_MESSAGE);
                                continue;
                            } else {
                                studentAnswers += ("Student's answer for question " + i + ": " + userChoice + "**");
                                break;
                            }
                        }
                    }
                    //student answered all questions and that information is now stored, now asks if submit
                    int ans;
                    while (true) {
                        ans = JOptionPane.showConfirmDialog(null, "You have answered " +
                                "all the questions, but you HAVEN'T submitted your quiz yet. \n" +
                                "If you want your quiz to be scored, you must submit your quiz for the" +
                                " teacher to see your work and give you a grade.\n" +
                                "Press the 'YES' button to submit\nPress the 'NO' button to NOT submit",
                                "Would you like to submit your quiz?", JOptionPane.YES_NO_OPTION);
                        if (ans != JOptionPane.YES_OPTION && ans != JOptionPane.NO_OPTION) {
                            JOptionPane.showMessageDialog(null, "You must either choose to" +
                                    " submit or not to submit", "Must click YES or NO",
                                    JOptionPane.ERROR_MESSAGE);
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
                    String theDate = dateFormat.format(theDateOfSubmission) + "**";

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

                        JOptionPane.showMessageDialog(null,
                                "You have successfully submitted your quiz" +
                                "\nHere is the date and time of your submission: " + theDate,
                                "Successfully submitted quiz!", JOptionPane.INFORMATION_MESSAGE);
                    }


                    studentQuizMenu.setVisible(true);

                } else if (mesFromSer.equals("updateStudentQuizListWhenCourseEdited")) {  //real time updates
                                                            //when student sees list of quizzes for a course
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
                } else if (mesFromSer.equals("cantGradeQuiz")) {  //when teacher can't grade quiz
                    JOptionPane.showMessageDialog(null, "Can't find the quiz",
                            "Can't grade quiz", JOptionPane.ERROR_MESSAGE);
                } else if (mesFromSer.equals("studentSeeSubs")) {  //when student wants to see their submissions
                    String submissionList = bfr.readLine();
                    String finalList = "";
                    int whereDivider = submissionList.indexOf("**");
                    while (whereDivider != -1) {
                        finalList += (submissionList.substring(0, whereDivider) + "\n");
                        submissionList = submissionList.substring(whereDivider + 2);
                        whereDivider = submissionList.indexOf("**");
                    }
                    listOfSubmissions.setText(finalList);
                } else if (mesFromSer.equals("teacherWantsToGrade")) {  //when teacher wants to grade submissions
                    String submissionList = bfr.readLine();
                    String finalList = "";
                    int whereDivider = submissionList.indexOf("**");
                    while (whereDivider != -1) {
                        finalList += (submissionList.substring(0, whereDivider) + "\n");
                        submissionList = submissionList.substring(whereDivider + 2);
                        whereDivider = submissionList.indexOf("**");
                    }
                    listToGrade.setText(finalList);
                } else if (mesFromSer.equals("gradeQuiz")) {  //when teacher can grade a specific quiz
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
                            int pointForQuestion = JOptionPane.showConfirmDialog(null,
                                    formattedQuizSheet + "\nGive a point for Question " + i + "?",
                                    "Give point?", JOptionPane.YES_NO_OPTION);
                            if (pointForQuestion != JOptionPane.YES_OPTION
                                    && pointForQuestion != JOptionPane.NO_OPTION) {
                                JOptionPane.showMessageDialog(null,
                                        "You must finish grading the quiz.", "Must finish grading",
                                        JOptionPane.ERROR_MESSAGE);
                            } else {
                                if (pointForQuestion == JOptionPane.YES_OPTION) {
                                    numCorrect++;
                                    finalScoreString += ("Result for Question " + i + ": CORRECT (+1 point)**");
                                } else {
                                    finalScoreString += ("Result for Question " + i + ": " +
                                            "INCORRECT (No points awarded)**");
                                }
                                break;
                            }
                        }
                    }
                    String percentScore = String.format("%.2f", (double) numCorrect / numQuestions * 100);
                    finalScoreString += ("Your score: " + numCorrect + "/" + numQuestions + ", or " +
                            percentScore + "%**");  //stores the score that the teacher gave
                    writer.write(finalScoreString);
                    writer.println();
                    writer.flush();

                } else if (mesFromSer.equals("updateStudentSubList")) {  //real time updates so student can see
                                                    //their graded quizzes automatically
                    String studentEmail = bfr.readLine();
                    String newSubListForStu = bfr.readLine();
                    if (studentEmail.equals(emailLoggedIn)) {
                        String finalList = "";
                        int whereDivider = newSubListForStu.indexOf("**");
                        while (whereDivider != -1) {
                            finalList += (newSubListForStu.substring(0, whereDivider) + "\n");
                            newSubListForStu = newSubListForStu.substring(whereDivider + 2);
                            whereDivider = newSubListForStu.indexOf("**");
                        }
                        listOfSubmissions.setText(finalList);
                    }
                } else if (mesFromSer.equals("updateTeacherSubList")) {  //real time updates so teacher can see
                                                    //that they don't have to grade a quiz they just graded
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
                } else if (mesFromSer.equals("updatedName")) {  //real time updates when user edits their name
                    String role = bfr.readLine();
                    String newName = bfr.readLine();
                    if (role.equals("teacher")) {  //if the logged in user is a teacher
                        teacherLoginGreeting.setText("Welcome " + newName + "! You're a teacher");
                    } else {   //if logged in user is a student
                        studentLoginGreeting.setText("Welcome " + newName + "! You're a student");
                    }

                } else if (mesFromSer.equals("stop")) {  //when a user closes the start menu JFrame and the program
                                                        //on the client side ends.
                    break;
                } else if (mesFromSer.equals("attemptedDeletion")) {  //when user tries to delete their account
                    String result = bfr.readLine();
                    if (result.equals("cantDeleteAccount")) {  //when teacher's account can't be deleted
                        JOptionPane.showMessageDialog(null,
                                "In order to delete your account," +
                                "\nyou must delete all courses you have made first",
                                "Can't delete account", JOptionPane.ERROR_MESSAGE);
                    } else {  //if the user's account can be deleted
                        JOptionPane.showMessageDialog(null,
                                "Account has been deleted", "Account deleted",
                                JOptionPane.INFORMATION_MESSAGE);
                        teacherMenu.setVisible(false);  //"teacher account is exited"
                        startMenu.setVisible(true);
                    }
                }
            } catch (Exception e) {
                //Any exceptions are caught
            }
        }
    }
}

