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

    /**
     * Startmenu
     */
    static JFrame startMenu = new JFrame("Start Menu");
    static Container startContent = startMenu.getContentPane();
    static JPanel startJpanel = new JPanel();
    static JLabel welcomeMessage = new JLabel("Welcome. Press a button to start");








    static JFrame teacherMenu = new JFrame("Welcome! You are a teacher!");

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
             * THIS IS NOW THE COURSES JFRAME
             *
             */
            JFrame coursesMenu = new JFrame("Your Created Courses");
            Container coursesContent = coursesMenu.getContentPane();
            coursesContent.setLayout(new BorderLayout());

            coursesMenu.setSize(400, 400);
            coursesMenu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  //change to DoNothingonclose and force teacher to press logout to leave
            coursesMenu.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    teacherMenu.setVisible(true);

                }
            });

            JPanel courseJpanel = new JPanel();
            JLabel listOfCourses = new JLabel();  //we have to set the jlabel in the actionListener that deals with login

            JButton createCourse = new JButton("Create Course");
            ActionListener createCourseListener = new ActionListener() {  //CREATING COURSE
                @Override
                public void actionPerformed(ActionEvent e) {

                }
            };
            createCourse.addActionListener(createCourseListener);

            JButton editCourse = new JButton("Edit Course");
            ActionListener editCourseListener = new ActionListener() {  //EDITING COURSE
                @Override
                public void actionPerformed(ActionEvent e) {

                }
            };
            editCourse.addActionListener(editCourseListener);

            JButton deleteCourse = new JButton("Delete Course");
            ActionListener delCourseListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                }
            };
            deleteCourse.addActionListener(delCourseListener);

            courseJpanel.add(listOfCourses);
            courseJpanel.add(createCourse);
            courseJpanel.add(editCourse);
            courseJpanel.add(deleteCourse);

            coursesContent.add(courseJpanel, BorderLayout.CENTER);
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
            ActionListener manageCourseButton = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    teacherMenu.setVisible(false);
                    coursesMenu.setVisible(true);
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

                                } else {    //teacher logged in
                                    Teacher user = (Teacher) p;
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
                            //edit stuff here JLbael .set method here
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
