import java.util.ArrayList;

/**
 * Represents a Question object.
 * Questions are inside a quiz.
 * Questions contain a string that is the question,
 * and a String arraylist with the answer choices.
 *
 * @author Cole Priser
 * @version December 11 2021
 */
public class Question {
    private ArrayList<String> answerChoices;
    private String question;

    /**
     * Constructor to create a question object
     *
     * @param question      string that contains the question
     * @param answerChoices possible answers to the corresponding question string
     */
    public Question(String question, ArrayList<String> answerChoices) {
        this.answerChoices = answerChoices;
        this.question = question;
    }

    /**
     * Method to get the question string
     *
     * @return String
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Method to get the answer choices for it's corresponding question string
     *
     * @return ArrayList<String>
     */
    public ArrayList<String> getAnswerChoices() {
        return answerChoices;
    }
}

