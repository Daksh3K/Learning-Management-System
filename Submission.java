import java.io.*;

/**
 * This class represents a quiz that's submitted by the student.
 * The student's answer choices can be accessed within this class, and
 * the teacher can use this class to give student's their grades on their
 * quizzes.
 *
 * @version November 14 2021
 * @author Yu Hyun Kim
 */
public class Submission implements Serializable {
    private String quizQuestionString;  //the questions in the format the student saw them and their answers
    private String quizAnsweredString;
    private String subDateString;
    private String teacherWhoCreatedQuiz;  //the email of the teacher who created the quiz
    private String gradeForStudent;  //the grade for the student
    private String nameOfQuizTaken;  //name of the quiz
    private int numOfQuestions;  //number of quiz questions
    private String studentWhoTookQuiz;

    /**
     * constructor for Submission class
     * When submission objects are made, the gradeForStudent instance variable is instantiated to "Not graded yet"
     * However, the teacher can give grades and set the gradeForStudent instance variable to the student's grade
     *
     * @param teacherWhoCreatedQuiz
     * @param nameOfQuizTaken
     * @param numOfQuestions
     */
    public Submission(String quizQuestionString, String quizAnswerString, String subDateString, String teacherWhoCreatedQuiz,
                      String nameOfQuizTaken, int numOfQuestions, String student) {
        this.quizQuestionString = quizQuestionString;
        this.quizAnsweredString = quizAnswerString;
        this.subDateString = subDateString;
        this.teacherWhoCreatedQuiz = teacherWhoCreatedQuiz;
        this.nameOfQuizTaken = nameOfQuizTaken;
        this.gradeForStudent = "Not graded yet";
        this.numOfQuestions = numOfQuestions;
        this.studentWhoTookQuiz = student;
    }

    /**
     * The following are getter and setter methods for some of the instance variables
     * in the Submission class.
     */
    public String getNameOfQuizTaken() {
        return nameOfQuizTaken;
    }

    public String getTeacherWhoCreatedQuiz() {
        return teacherWhoCreatedQuiz;
    }

    public String getGradeForStudent() {
        return gradeForStudent;
    }

    public void setGradeForStudent(String grade) {
        this.gradeForStudent = grade;
    }

    public String getQuizQuestionString() {
        return quizQuestionString;
    }

    public String getQuizAnsweredString() {return quizAnsweredString;}

    public String getSubDateString() {return subDateString;}

    public int getNumOfQuestions() {
        return numOfQuestions;
    }

    public String getStudentWhoTookQuiz() {
        return studentWhoTookQuiz;
    }
}
