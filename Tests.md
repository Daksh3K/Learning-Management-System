# TEST CASES
Please read everything in this document very carefully.

## IMPORTANT NOTES BEFORE STARTING TEST CASES. YOU NEED TO READ THIS!
Before beginning the test cases, please make sure that all of the Classes (.java files) that were initially submitted in Vocareum exist.


ADDITIONALLY, please make sure that only the .txt files that were submitted in Vocareum exist (These text files are Courses.txt and People.txt). If there are any other .txt files, please delete them. For the .txt files that are supposed to exist, make sure to delete ALL the contents in the files.


If for any reason you would like to restart the entire testing process, delete all text files besides the Courses.txt and People.txt
file, and delete all other .txt files. Additionally, delete all the contents in the Courses.txt and People.txt files.

*Not taking these steps before using the program can lead to errors.*

All tests are meant to be done chronologically; we have chosen the order of tests we did to simulate as if a user was actually using the program in a realistic way.

These tests assume that you are using one machine (so the server side code and client side code is running 
on the same machine) and that one user is logged in at a time.

(The reason why we designed the test cases like this is because we are assuming that you will only use one machine. We fully demonstrate our program's ability to have multiple people using multiple different machines in the presentation video, where we demonstrate our code’s use of proper concurrency and Network I/O. Furthermore, we see instant real time updates in the demonstration).

## Now let's start testing!
To begin the test, first run the LmsMain.java class; you will be running the server side code. 
After the server is running, please run ClientOne.java class, which is the class that contains the code for the client side.

When you do these two things, you should see a start menu pop up:

![start Menu Pic](https://user-images.githubusercontent.com/89658307/145688013-6ab42af5-94a9-4394-85e7-fc3f16fdd770.PNG)

## Test 1: Making Student Account
-Step 1: Once on the start menu (picture shown above), press the “Create Account” button, and you should see this window pop up:

![enteringNameCreatingAccount](https://user-images.githubusercontent.com/89658307/145688037-3135049a-05b2-43dc-9bfd-92c7c30bf21c.PNG)

-Step 2: In the text field, enter the name “studentName” (for the purposes of this test, we are using this particular name) and press the “OK” button.

-Step 3: A new JOptionPane will show up, asking you to put in your email (image shown below). In the text field, enter “studentEmail” and press the “OK” button.

![enterEmailCreatingAccount](https://user-images.githubusercontent.com/89658307/145688046-406d97f7-7867-439f-aad0-8c23386dcc39.PNG)

Step 4: Once you enter the email, you should see a JOptionPane that looks like the image below:

![enterRoleCreatingAccount](https://user-images.githubusercontent.com/89658307/145688054-0053d5ff-87e6-4aa0-80c1-ae5954f089b0.PNG)

Since we are making a student account, press the “Yes” button.
(After you press the Yes button, there should be no errors making the account, so the JOptionPane should disappear and all you should see is the Start Menu JFrame)

Expected Result: Successfully entered the account info through the JOptionPane windows and no error messages should pop up, which indicates that we have successfully created our student account

Test Result: Passed.

## Test 2: Making Teacher Account
We just made our student account, and now we are going to make a student account. You should at this point only see the Start Menu.

-Step 1: press the “Create Account” button, and you should see this window pop up:

![enteringNameCreatingAccount](https://user-images.githubusercontent.com/89658307/145688125-755dc4ff-3066-431c-85bb-4fbc27b626f0.PNG)

-Step 2: In the text field, since we are making a teacher account, enter the name “teacherName” and press the “OK” button.

-Step 3: After step 2, you should see this JOptionPane

![enterEmailCreatingAccount](https://user-images.githubusercontent.com/89658307/145688133-25e7ae01-2a75-4d3f-9b7d-34b5cd7a3486.PNG)

Now, for the email address, enter “studentEmail” and press the “OK” button. 

-Step 4: After step 3, you should see this JOptionPane error message:

![emailAlreadyUsed](https://user-images.githubusercontent.com/89658307/145688140-0971a271-f535-4947-aa42-98e5908f1a06.PNG)

We are getting an error message because remember, we already made an account using “studentEmail” (the unique identifier for every account is the email address used to create the account). Therefore, we must choose another email to create our teacher account. So let’s do that. Press the “OK” button that’s on the JOptionPane error window and we should be redirected to the “Start Menu” JFrame. 

-Step 5: Redo steps 1, 2, 3, but for step 3, use “teacherEmail” as the email we are going to use to create our account.

-Step 6: Once we do step 5, we should get this JOptionPane message:

![enterRoleCreatingAccount](https://user-images.githubusercontent.com/89658307/145688160-709e4713-833f-43d5-8eec-3ab79fc13eae.PNG)

Since we want to make a teacher account, press the “No” button, and afterward, we should only see the “Start Menu” JFrame. We have now successfully created the teacher account.

Expected Result: We tried to create a teacher account with the name “teacherName” and email “studentEmail”, but received an error notifying us that the email was already used. Therefore, we used the email “teacherEmail” instead, and we successfully created our teacher account afterward.

Test Result: Success

## Test 3: Logging into TEACHER account
After finishing Test 2, we should see the “Start Menu” JFrame. 

-Step 1: On the “Start Menu” Jframe, there should be a “Login” button. Press the “Login” button. We should see this prompt: 

![loginPrompt](https://user-images.githubusercontent.com/89658307/145689141-390d2d9b-9287-41cd-a6e5-c12cd14db4ee.PNG)

-Step 2: enter “teacherEmail” in the text field and press the “OK” button to login. You should see the teacher menu JFrame:

![teacherMenu](https://user-images.githubusercontent.com/89658307/145689147-c4a7b965-6335-4a76-b61d-ed28a7773935.PNG)

Expected Result: login using our email that we made for the teacher account, and once logged in, see the teacher menu.

Result: success

## Test 4: Managing Courses

Once we finish Test 3, you should see the teacher menu option that has the welcome message as well as buttons regarding what the teacher account can do. Let’s manage courses.

-Step 1: On the teacher menu JFrame, press the “Manage Course/Quiz” button. You should be redirect to this JFrame:

![noCoursesCourseMenu](https://user-images.githubusercontent.com/89658307/145689168-e43465e8-d633-4376-876c-fd638f158a27.PNG)

We should see nothing besides the buttons because no courses have been made yet.
Before we create a course, let’s press “edit course”.

-Step 2: Press the “Edit Course” button to show this JOptionPane:

![editingCourse](https://user-images.githubusercontent.com/89658307/145689181-bc111b20-3433-43cc-942b-43d79700ffaa.PNG)

-Step 3: Enter a String (any String) or no String, and press the “OK” button. You should see this error message:

![cantFindCourse](https://user-images.githubusercontent.com/89658307/145689190-6ff8e191-ac7f-4abb-a44e-4808e5d0c7c0.PNG)

This is expected, since no matter what String we put, it won’t indicate any course the teacher made because the teacher has made no course.

-Step 4: Press the “OK” button on the JOptionPane error message and then press the “Delete Course” button. You should see this prompt:

![deleteCourses](https://user-images.githubusercontent.com/89658307/145689201-65f5803a-e016-42ed-907b-85d43859541f.PNG)

-Step 5: In the text field, enter any String, or no String, and press the “OK” button. We should receive this error message since there are no courses to delete as of right now.

![cantDeleteCourse](https://user-images.githubusercontent.com/89658307/145689207-9d03586e-f5dd-430f-9b2c-e7c0df5743fd.PNG)

-Step 6: Press the “OK” button to display only the “Your Created Courses” JFrame.
-Step 7: Press the “Create Course” button and you should see this prompt:

![makingNewCourse](https://user-images.githubusercontent.com/89658307/145689212-38e8d3be-cc67-4531-9fdd-3d0e467ac08e.PNG)

-Step 8: In the text field, enter “Course1” and press the “OK” button. As soon as you press the “OK” button, you should see that the “Your Created Courses” window looks like this:

![updatedCoursesMenu](https://user-images.githubusercontent.com/89658307/145689227-dfa7b59c-adc2-442e-825d-2149ff9b8723.PNG)

Notice that there are real time updates happening: as soon as a course is made, that action is immediately reflected automatically. 

Expected Result: Teacher that’s logged in goes to see their created courses and tries to edit and delete a course that doesn’t exist, therefore getting a JOptionPane error message. Therefore, the teacher tries to create a course called “Course1”, and that action is reflected immediately and automatically.

Result: success

## Test 5: Managing quizzes for a course
-Step 1: Now that we made the “Course1” course, let’s edit that course to manage quizzes. Press the “Edit Course” button, and enter “Course1” in the text field of the JOptionPane. Press the “OK” button and you should see this JFrame now:

![quizMenu](https://user-images.githubusercontent.com/89658307/145690732-5802d6aa-2a4b-43a3-a076-d9e2289afc82.PNG)

-Step 2: Press the “Create Quiz” button to see this prompt:

![makingNewQuizName](https://user-images.githubusercontent.com/89658307/145690737-445e54ac-23c3-41a4-99c7-d6c15a6eed56.PNG)

-Step 3: Enter the quiz name “Quiz1” in the text box and press the “OK” button, to which you will see this JOptionPane:

![createQuizNumQuestions](https://user-images.githubusercontent.com/89658307/145690748-3960d39f-3fc6-4ca3-82cf-68629ee0da0f.PNG)

-Step 4: Enter the number “3” in the text box and press the “OK” button. You will see this JOptionPane now:

![quizAnswerChoices](https://user-images.githubusercontent.com/89658307/145690759-6a49dbc3-920a-455d-b1a2-712f3e03fed0.PNG)

-Step 5: Enter the number “3” in the text box and press the “OK” button. You will then see this JOptionPane:

![1stQuestionForQuiz](https://user-images.githubusercontent.com/89658307/145690769-24438fcd-0c2c-4bfe-8905-0997fc8461cf.PNG)

-Step 6: In the text field, enter “What’s 1+1?” and press the “OK” button. You will see this JOptionPane:

![answerChoiceFor1stQuestion](https://user-images.githubusercontent.com/89658307/145690774-9e528b25-8f7f-4b8f-8062-4335018d8282.PNG)

-Step 7: In the text field, enter “1” and press the “OK” button. You will see the above JOptionPane again but it will ask you to enter answer choice 2.
-Step 8: In the text field, enter “2” and press the “OK” button. You will see the JOptionPane again asking for answer choice 3. 
-Step 9: In the text field, enter “3” and press the “OK” button.
-Step 10: Now, you have made the first question and the 3 answer choices for the question. Since we indicated that we wanted to have 3 questions in total with 3 answer choices for each question, you will see JOptionPanes again that will ask you for the 2nd question, and then you’ll be prompted to enter 3 answer choices for that question. Afterward, you’ll be asked again to enter the 3rd question, and then you’ll be prompted to enter 3 answer choices for that question. Read below to see what to enter when making the rest of the quiz.

For the 2nd question, make this String the question: “What’s 2+2?”
For the 2nd question, make the 1st answer choice this: “4”
For the 2nd question, make the 2nd answer choice this: “5”
For the 2nd question, make the 3rd answer choice this: “6”

For the 3rd question, make this String the question: “What’s this class?”
For the 3nd question, make the 1st answer choice this: “CS 18000”
For the 3rd question, make the 2nd answer choice this: “CS 19300”
For the 3rd question, make the 3rd answer choice this: “CS 24000”

-Step 11: After you finish making the quiz question and answer choices, you should see this JOptionPane:

![randomizeQuiz](https://user-images.githubusercontent.com/89658307/145690783-9fdf1f27-6b91-4551-9e8c-b0a99817c2a4.PNG)

-Step 12: Press the “Yes” button to randomize the order of questions and answer choices for each question.

-Step 13: If you followed the steps correctly up to now, there should be no JOptionPane and only the JFrame that looks like this:

![updatedQuizMenu](https://user-images.githubusercontent.com/89658307/145690785-179c778f-642f-480e-ab02-981e7e4037a5.PNG)

Notice how as soon as the quiz is finished being made, there are real time updates displaying the new quiz we just made in the quizzes menu JFrame. 

## Test 6: Log in as student
