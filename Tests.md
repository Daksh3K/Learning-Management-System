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
