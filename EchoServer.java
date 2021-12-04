import java.lang.*;
import java.io.*;
import javax.swing.*;
import java.net.*;
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
                    String userEmail = bfr.readLine();
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
                    while (true) {  //if you want to exit loop, user logout
                        //System.out.println("Okay we going to identify student or teacher");
                        //email of person logged in #######
                        //System.out.println(email + "Logged in person's");//person has logged in


                        String userChoice = bfr.readLine();
                        if (userChoice.equals("deleteAccount")) {
                            //INSTANCE WHERE WE HAVE TO CHANGE THE DATA USING BIG OBJECT
                            ArrayList<People> people = LmsMain.getPeopleListInFile();
                            for (int i = 0; i < people.size(); i++) {
                                if (people.get(i).getEmail().equals(email)) {
                                    File f = new File(people.get(i).getTextFileName());
                                    f.delete();
                                    people.remove(i);
                                    updatePeopleListInFile(people);
                                    System.out.println("Account Deleted");
                                    break;
                                }
                            }
                            break;
                        } else if (userChoice.equals("logout")) {  //when user logs out
                            break;
                        } else if (userChoice.equals("changeName")) {  //changing user's name
                            //INSTANCE WHERE WE HAVE TO CHANGE THE DATA USING BIG OBJECT
                            String newName = bfr.readLine();
                            ArrayList<People> thePeople = getPeopleListInFile();  //get the arrayList of People
                            for (int i = 0; i < thePeople.size(); i++) {   //update arrayList and put in People.txt
                                if (thePeople.get(i).getEmail().equals(email)) {
                                    thePeople.get(i).setName(newName);
                                    updatePeopleListInFile(thePeople);
                                    break;
                                }
                            }
                        } else if (userChoice.equals("makingCourse")) {      //WE NOW HAVE TEACHER STUFF
                            String newCourseName = bfr.readLine();
                            String teacherWhoMadeCourse = bfr.readLine();
                            Course newCourse = new Course(newCourseName, teacherWhoMadeCourse);
                            ArrayList<Course> theCourseList = LmsMain.getCoursesInFile();
                            theCourseList.add(newCourse);
                            LmsMain.updateCoursesInFile(theCourseList);  //made course and stored it

                        } else if (userChoice.equals("deleteCourse")) {
                            String courseToDelete = bfr.readLine();
                            ArrayList<Course> courses = LmsMain.getCoursesInFile();
                            for(int i = 0; i < courses.size(); i++) {
                                if (courses.get(i).getCourseName().equals(courseToDelete)) {
                                    File f = new File(courses.get(i).getCourseTextFileName());
                                    f.delete();
                                    courses.remove(i);
                                    LmsMain.updateCoursesInFile(courses);
                                    break;
                                }
                            }
                        } else if (userChoice.equals("createQuiz")) {
                            String quizName = bfr.readLine();
                            String quizQuestions = bfr.readLine();
                            String quizAnswers = bfr.readLine();
                            int numQuestions = Integer.parseInt(bfr.readLine());
                            int numAnsChoicesPerQuestion = Integer.parseInt(bfr.readLine());
                            String randomOrNot = bfr.readLine();
                            boolean random = false;
                            if (randomOrNot.equals("randomize")) {
                                random = true;
                            }
                            String teacherWhoMadeQuiz = bfr.readLine();
                            //making quiz object now


                        }




                        //put the for loop here as well to update change
                        for(Socket s: clientsConnected) {
                            writer = new PrintWriter(s.getOutputStream());
                            writer.write("Change has been made");
                            writer.println();
                            writer.flush();
                        }
                    }
                }


                for(Socket s: clientsConnected) {
                    writer = new PrintWriter(s.getOutputStream());
                    writer.write("Change has been made");
                    writer.println();
                    writer.flush();
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
}
