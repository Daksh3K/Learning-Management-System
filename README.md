# Project-05 Option 2: Learning Management System Quiz Manager Documentation
Group members: Yu Hyun Kim, Cole Priser, Dakshesh Gupta

## How to compile and run our program.
***Before we begin, please know that our program was made using IntelliJ, and the instructions below should also be done in IntelliJ.

First, create a project on IntelliJ, and in the src folder, put all the classes inside it. Meanwhile, place all the text files outside of the src folder, but inside the project folder.

The classes that are NOT testing classes are Course, LmsMain, People, Question, Quiz, Student, Submission, and Teacher; all other classes are testing classes.

***IMPORTANT: Make sure that if you want to run the program as if it is being run for the first time, make sure that the People.txt file is empty, and that only the text files that are submitted on the Vocareum workspace exist. If you don't do this, testing and non testing classes will not function properly.

## Who Submitted Which Parts on BrightSpace/Vocareum

## Description of Classes

### LmsMain.java: 

The LmsMain.java class is the class that acts as the server. It creates a serverSocket and blocks, waiting for clients to connect. Once a client connects by running the ClientOne.java class, the LmsMain.java class makes a connection with the client, allowing the client to start using the program. When a client connects, a new EchoServer object is made using the socket that's accepted, and a new Thread is created for every connected client. Additionally, this class puts an empty arrayList (meant to store Student and Teacher objects) in the People.txt if the People.txt file is empty, and the class also puts an empty arrayList (meant to store Course objects) in the Courses.txt file if the Courses.txt file is empty. This class also has several methods that when called, can return the arrayList stored inside any file, and it can also get an arrayList and store it in a file. 

### EchoServer.java:

This class is also part of the server-side code. After a client connects to the server and the LmsMain.java class creates a new EchoServer object for every socket made between a client and the server, a new Thread is started, which means that the run() method in the EchoServer class begins to run. Inside the run() method is a loop that constantly loops over and over to detect information (in the form of Strings) that is sent from any of the connected clients. When a client wants to do something, the client sends Strings to the server, and the loop in the EchoServer's run() method allows that String to be read. Additionally, inside the while loop of the run() method, Strings regarding information (some examples include login info, account info when new account is made, quiz info) is also received from the client, and the method can do what it needs to with that information (some examples include making a Student object, editting a Quiz object, reading a .txt file, storing an arrayList back into a .txt file). Depending on what the client is doing, the run() method can also send information in the form of Strings to either one client or multiple clients, so that the client can do things like taking a quiz, or seeing real time updates. The EchoServer class also has a few instance variables. It has an instance variable that stores an object from the Object class, which is used in synchronized blocks, an instance variable that stores the socket object, an instance variable that stores a bufferedReader, and an instnce variable that stores an arrayList of all sockets created. The constructor of the EchoServer class instantiates the instance variables and adds created sockets to the arrayList of Socket objects.


### ClientOne.java:

### People.java

### Student.java

### Teacher.java

### Course.java

### Quiz.java

### Question.java

### Submission.java
