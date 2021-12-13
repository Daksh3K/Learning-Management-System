import java.io.*;

/**
 * This class represents a quiz that's submitted by the student.
 * The student's answer choices can be accessed within this class, and
 * the teacher can use this class to give student's their grades on their
 * quizzes.
 *
 * @version December 11, 2021
 * @author Yu Hyun Kim
 */
public class Submission implements Serializable {
    private String quizQuestionString;  //the questions in the format the student saw them and their answers
    private String quizAnsweredString;  //the answered quiz from the student that took it
    private String subDateString;    //the subdate of when the quiz was taken
    private String teacherWhoCreatedQuiz;  //the email of the teacher who created the quiz
    private String gradeForStudent;  //the grade for the student
    private String nameOfQuizTaken;  //name of the quiz
    private int numOfQuestions;  //number of quiz questions
    private String studentWhoTookQuiz;   //the student who took the quiz

    /**
     * constructor for Submission class
     * When submission objects are made, the gradeForStudent instance variable is instantiated to "Not graded yet"
     * However, the teacher can give grades and set the gradeForStudent instance variable to the student's grade
     *
     * @param quizQuestionString    the questions in the quiz
     * @param quizAnswerString      the completed quiz
     * @param subDateString         the subdate of the quiz completion
     * @param teacherWhoCreatedQuiz teacher that made this quiz
     * @param nameOfQuizTaken       the name of the quiz that the student takes
     * @param numOfQuestions        the number of questions in the quiz
     * @param student               the student that took the quiz
     *
     */
    public Submission(String quizQuestionString, String quizAnswerString,
                      String subDateString, String teacherWhoCreatedQuiz,
                      String nameOfQuizTaken, int numOfQuestions, String student) {
        this.quizQuestionString = quizQuestionString;
        this.quizAnsweredString = quizAnswerString;
        this.subDateString = subDateString;
        this.teacherWhoCreatedQuiz = teacherWhoCreatedQuiz;
        this.nameOfQuizTaken = nameOfQuizTaken;
        this.gradeForStudent = "Not graded yet**";
        this.numOfQuestions = numOfQuestions;
        this.studentWhoTookQuiz = student;
    }

    /**
     * The following are getter and setter methods for the instance variables
     * in the Submission class.
     */


    /**
     * Method to get the name of the quiz taken
     *
     * @return String
     */
    public String getNameOfQuizTaken() {
        return nameOfQuizTaken;
    }


    /**
     * Method to get the teacher who created the quiz
     *
     * @return String
     */
    public String getTeacherWhoCreatedQuiz() {
        return teacherWhoCreatedQuiz;
    }

    /**
     * Method to get the grade for the student
     *
     * @return String
     */
    public String getGradeForStudent() {
        return gradeForStudent;
    }

    /**
     * Method to set the grade for the student that took the quiz
     *
     * @param grade student's new grade
     */
    public void setGradeForStudent(String grade) {
        this.gradeForStudent = grade;
    }

    /**
     * Method to get the quiz questions
     *
     * @return String
     */
    public String getQuizQuestionString() {
        return quizQuestionString;
    }

    /**
     * Method to get the quiz answered
     *
     * @return String
     */
    public String getQuizAnsweredString() { return quizAnsweredString; }

    /**
     * Method to get the subdate of quiz
     *
     * @return String
     */
    public String getSubDateString() { return subDateString; }

    /**
     * Method to get the number of questions in the quiz
     *
     * @return int
     */
    public int getNumOfQuestions() {
        return numOfQuestions;
    }

    /**
     * Method to get the student that took the quiz
     *
     * @return String
     */
    public String getStudentWhoTookQuiz() {
        return studentWhoTookQuiz;
    }
}
