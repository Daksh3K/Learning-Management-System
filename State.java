/**
 * State object is used to store data about the current state of the program.
 * It includes information of all the objects that exist, and are being used.
 * The objects of this class are used for information exchange between client
 * and the server
 */
public class State {
    private String studentsString; // "John Doe,jdoe@gmail.com\nJane Doe,janed@gmail.com\n ..."
    private String teachersString; // "John Doe,jdoe@gmail.com\nJane Doe,janed@gmail.com\n ..."
    private String coursesString; // "CS180,CS193,CS191 ..."
    private String quizzesString; // "quiz 1,quiz 2,quiz 3 ...


    /**
     * Constructor the State object
     * @param studentsString String containing name and email of all the students that exist
     * @param teachersString String containing name and email of all the teachers that exist
     * @param coursesString String containing name and email of all the courses that exist
     * @param quizzesString String containing name and email of all the quizzes that exist
     */
    public State(String studentsString, String teachersString, String coursesString, String quizzesString) {
        this.studentsString = studentsString;
        this.teachersString = teachersString;
        this.coursesString = coursesString;
        this.quizzesString = quizzesString;
    }

    /**
     * Method to access the studentString
     * @return String containing name and email of all students that currently exist
     */
    public String getStudentsString() {
        return studentsString;
    }

    /**
     * Method to modify the studentString
     * @param studentsString the new studentString
     */
    public void setStudentsString(String studentsString) {
        this.studentsString = studentsString;
    }

    /**
     * Method to access the teacherString
     * @return String containing name and email of all teachers that currently exist
     */
    public String getTeachersString() {
        return teachersString;
    }

    /**
     * Method to update teacherString
     * @param teachersString the new teacherString
     */
    public void setTeachersString(String teachersString) {
        this.teachersString = teachersString;
    }

    /**
     * Method to access coursesString
     * @return String containing information about all the courses that currently exist
     */
    public String getCoursesString() {
        return coursesString;
    }

    /**
     * Method to update the coursesString
     * @param coursesString the new coursesString
     */
    public void setCoursesString(String coursesString) {
        this.coursesString = coursesString;
    }

    /**
     *  Method to access QuizzesString
     * @return String containing information about all the quizzes that currently exist
     */
    public String getQuizzesString() {
        return quizzesString;
    }

    /**
     * Method to update quizzesString
     * @param quizzesString the latest quizzesString
     */
    public void setQuizzesString(String quizzesString) {
        this.quizzesString = quizzesString;
    }

    /**
     * Returns the number of Students accounts that currently exist
     * @return int indicating the number of student accounts currently existing
     */
    public int getNumberOfStudents() {
        String[] studentsList = this.studentsString.split("\\r\\n|\\r|\\n");

        return studentsList.length;
    }

    /**
     * Returns the number of Teacher accounts that currently exist
     * @return int indicating the number of teacher accounts currently existing
     */
    public int getNumberOfTeachers() {
        String[] teachersList = this.teachersString.split("\\r\\n|\\r|\\n");

        return teachersList.length;
    }

    /**
     * Returns the number of courses that currently exist
     * @return int indicating the number of courses currently existing
     */
    public int getNumberOfCourses() {
        String[] coursesList = this.coursesString.split(",");

        return coursesList.length;
    }

    /**
     * Returns the number of quizzes that currently exist
     * @return int indicating the number of quizzes currently existing
     */
    public int getNumberOfQuizzes() {
        String[] quizList = this.quizzesString.split(",");
        return quizList.length;
    }
}
