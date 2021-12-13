# Project-05 Option 2: Learning Management System Quiz Manager Documentation
Group members: Yu Hyun Kim, Cole Priser, Dakshesh Gupta

## How to compile and run our program.
***Before we begin, please know that our program was made using IntelliJ, and the instructions below should also be done in IntelliJ.

First, create a project on IntelliJ, and in the src folder, put all the classes inside it. 

The classes (.java files) are the following: LmsMain.java, EchoServer.java, ClientOne.java, People.java, Quiz.java, Question.java, Submission.java, Student.java, Teacher.java, and Course.java.

Meanwhile, place all the text files outside of the src folder, but inside the project folder.

The text files that should be present are People.txt and Courses.txt

***IMPORTANT: Make sure that if you want to run the program as if it is being run for the first time at any point, make sure that the People.txt and Courses.txt file are empty, and that only the text files that are submitted on the Vocareum workspace exist. If you don't do this, testing and non testing classes will not function properly. ***

In order to run the program, first, you must run LmsMain.java. When you run LmsMain.java class, you will be running the server.

Next, you run the ClientOne.java class. When you run the ClientOne.java class, you will be running the client-side code. 

This will allow you to have full access to the program, as the server and client will connect. From here, you can use the learning management system. You have to run the server before the client, as otherwise, the program won't work properly.

** 2 additional pieces of information

1) The port number we used for the sockets is 1234 (this is also mentioned in the documentation/comments in the LmsMain class)
2) If you see our video presentation, we performed a demo where multiple users utilizing DIFFERENT machines are able to utilize our program. When we did that demo, we
changed the part in the ClientOne class where you would put "localhost", when making a socket, to an address that belongs to the machine that hosted the server-side code. In order to get this address, we utilized the InetAddress.getLocalHost() method call.

***If you look at ClientOne.java, you will notice that it says "localhost". We made it this way because we are assuming that you will have BOTH the server-side code AND client
side code on the same machine when testing our program.*** 

## Who Submitted Which Parts on BrightSpace/Vocareum
Dakshesh Gupta submitted the Vocareum workspace and Video Presentation

Yu Hyun Kim submitted the report

## Description of Classes

### LmsMain.java: 

The LmsMain.java class is the class that acts as the server. It creates a serverSocket and blocks, waiting for clients to connect. Once a client connects by running the ClientOne.java class, the LmsMain.java class makes a connection with the client, allowing the client to start using the program. When a client connects, a new EchoServer object is made using the socket that's accepted, and a new Thread is created for every connected client. Additionally, this class puts an empty arrayList (meant to store Student and Teacher objects) in the People.txt if the People.txt file is empty, and the class also puts an empty arrayList (meant to store Course objects) in the Courses.txt file if the Courses.txt file is empty. This class also has several methods that when called, can return the arrayList stored inside any file, and it can also get an arrayList and store it in a file. 

### EchoServer.java:

This class is also part of the server-side code. After a client connects to the server and the LmsMain.java class creates a new EchoServer object for every socket made between a client and the server, a new Thread is started, which means that the run() method in the EchoServer class begins to run. Inside the run() method is a loop that constantly loops over and over to detect information (in the form of Strings) that is sent from any of the connected clients. When a client wants to do something, the client sends Strings to the server, and the loop in the EchoServer's run() method allows that String to be read. Additionally, inside the while loop of the run() method, Strings regarding information (some examples include login info, account info when new account is made, quiz info) is also received from the client, and the method can do what it needs to with that information (some examples include making a Student object, editting a Quiz object, reading a .txt file, storing an arrayList back into a .txt file). Depending on what the client is doing, the run() method can also send information in the form of Strings to either one client or multiple clients, so that the client can do things like taking a quiz, or seeing real time updates. The EchoServer class also has a few instance variables. It has an instance variable that stores an object from the Object class, which is used in synchronized blocks, an instance variable that stores the socket object, an instance variable that stores a bufferedReader, and an instnce variable that stores an arrayList of all sockets created. The constructor of the EchoServer class instantiates the instance variables and adds created sockets to the arrayList of Socket objects.


### ClientOne.java:

This class contains all of the client-side code. In order for clients to connect to the server, all they have to do is run this class. The ClientOne.java class creates and displays all the GUIs (JFrames and JOptionPanes) and creates a Thread that causes the run() method in the ClientOne class to run. The run() method inside the ClientOne class serves a similar function to the run() method inside the EchoServer class; there's a loop that receives Strings sent from the server and sometimes, depending on what the client is doing, the run() method inside the ClientOne class will also send Strings back to the server. Everytime something happens where any user's JFrame needs to reflect new information, the loop inside the run() method that's in the ClientOne class will handle that, allowing for real time updates to happen by getting the new information that's sent from the server. Since there are buttons in our JFrames, there are several ActionListeners, one for each button. Depending on what button is pressed, there will be a certain String that notifies the server what the user is trying to do, and there will also be certain information (also in the form of Strings) that's sent to the server, and the information sent depends on what the user is trying to do. 

### People.java

The purpose of the People class is to create generalized characteristics that both Students and Teachers have, including a name, email, and text file name. This class creates a People object with a name, email, and text file name, and allows access to these fields through getters. There is also a setter that allows the name of a person to be changed, as well as a method that deletes a user from the list of people that have been created at that point. People.java was tested by creating a People object with a name, email, and text file name. Next, this object was called by the name, email, and text file getters, where it properly returned the object's name/email/text file name. Next, the name of the object was changed properly using a name setter. Finally, a user was properly deleted.

### Quiz.java

The purpose of the Quiz class is for teachers to create something that can be accessed by students, and to let students manipulate the object and give it back to the teacher to look at. A Quiz object is created with a String that contains the quiz title, questions, and answers. A quiz object can be randomized, which means that all the quiz questions are put into a random order and the answer choices for the corresponding question are also put in a random order. A quiz object has the option to be edited upon if the teacher pleases, but they must manually create new quiz questions and answer choices. A teacher can use setters to change the number of questions in a quiz, or the specific quiz questions and answer choices. Quiz.java is tested by creating one quiz object with a String. Next, the methods corresponding to the quiz created manually are executed and returned properly.

### Question.java

The purpose of the Question class is to assist the Quiz class with the randomization selection. A question object has an ArrayList of Strings that act as answer choices, and a corresponding question that is a String. The question class only contains getter methods to return the question with its corresponding answer choices. Question.java is tested by creating a question with a String that is the question, along with an ArrayList of strings that is the answer choices. The methods return the question and answer choices properly. 

### Submission.java

This class is used to create Submission objects that represent a quiz submission that a student makes. Objects of the Submission class have a String that consists of the quiz questions and the student’s answers to those questions, a String that stores the email of the teacher that made the quiz that the student submitted, a String that represents the grade that the teacher gave for the student, a String for the name of the quiz taken, and an integer of the number of question in the quiz that the student submitted. Inside the Submission class is a constructor that instantiates the instance variables listed above when a submission object is made, and there are several getter and setter methods. Submission objects are made in the main method (which is located in the LmsMain class), and the getter and setter methods are also used in the main method. Specifically, the getter methods are used when the teacher object calls them; this is done so the teacher can see the quiz details of the students’ submissions. Furthermore, the getters are used in the main method in order to find out whether a student is retaking a quiz or not. Additionally, the setter method is used by the teacher object when the teacher is giving the student a grade. Submission objects are stored in each student’s unique text file; this is how the data regarding quiz submissions is stored. This class is tested when students submit a quiz and teachers can view them using all the Submission.java methods.

### Student.java

The Student class represents a student user in the program. Objects of the student class contain 2 String fields for storing the student's name and email. Whenever a student object is created, the constructor also creates a text file associated with the student. This text file stores an ArrayList of Submission objects that are created whenever this Student takes a Quiz created by a Teacher. This class includes a method that retrieve the ArrayList of Submission objects that are stored in the student's text file, and another method that updates aforementioned ArrayList of Submission obects with a new ArrayList. Both of these methods are used in the Server class to access and update the ArrayList of Submission objects in Student's text file.

### Teacher.java

The Teacher class represents a Teacher user in the program. Objects of the teacher class contain 2 String fields for storing the teacher's name and email. This class extends the People class and inherits all its methods such as the getters and setters for the name and email, deleting the user, and getting the name of the text file associated with the Teacher object. The name and email of the Teacher have to be unique. Whenever a Teacher object is created, the constructor also creates a text file associated with the Teacher. This text file stores an ArrayList of Course objects.

### Course.java

The Course class represents a course that a teacher can make. It contains three String fields for storing the name of the course, the information about the teacher who made the course, and the text file associated with the course. The text file is meant to store an ArrayList of Quiz objects that the teacher creates for that course. This class also contains methods for getting the course name, information about the teacher who created the course, and the file name of text file associated with the course object. There are also methods for reading the text file associated with the course and returning an Arraylist of Quiz objects that were stored in the text file, and for updating the ArrayList of Quiz objects stored withing the text file with an ArrayList of Quiz objects.

