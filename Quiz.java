import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a Quiz object.
 * Quizzes can be created manually by a teacher,
 * where the quiz can be created by teachers and accessed by students.
 *
 * @author Cole Priser
 * @version December 11 2021
 */

public class Quiz {
    private String quizName; //Name of Quiz
    private String questions; //Questions in the quiz
    private String answers; //answers of the questions
    private int numQuestions; //number of questions in quiz
    private int numAnswers; //number of answer choices for each question in quiz
    private boolean isRandom; //decides if quiz questions and answer choices will be put in random order
    private String teacherEmail; //email of teacher that creates quiz
    private String randomQuestion; //randomized order of questions
    private String randomAnswer; //randomized order of answer choices for a question

    /**
     * Constructor to create a quiz object using a file that contains the quiz
     *
     * @param quizName     name of the quiz
     * @param questions    the questions for the quiz
     * @param answers      the answers of the questions
     * @param numQuestions number of questions in quiz
     * @param numAnswers   number of answer choices for each question in the quiz
     * @param isRandom     decides if quiz questions and answer choices will be put in random order
     * @param teacherEmail email of the teacher that created the quiz
     */
    public Quiz(String quizName, String questions, String answers, int numQuestions, int numAnswers, boolean isRandom, String teacherEmail) {
        this.quizName = quizName;
        this.questions = questions;
        this.answers = answers;
        this.numQuestions = numQuestions;
        this.numAnswers = numAnswers;
        this.isRandom = isRandom;
        this.teacherEmail = teacherEmail;
    }

    /**
     * Method to get name of quiz
     *
     * @return String
     */
    public String getQuizName() {
        return quizName;
    }

    /**
     * Method to set the name of the quiz
     *
     * @param quizName is new name of quiz
     */
    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    /**
     * Method to get the of questions in quiz
     *
     * @return String
     */
    public String getQuestions() {
        return questions;
    }

    /**
     * Method to set the questions of a quiz
     *
     * @param questions new string of questions for quiz
     */
    public void setQuestions(String questions) {
        this.questions = questions;
    }

    /**
     * Method to get answer choices for a quiz
     *
     * @return String
     */
    public String getAnswers() {
        return answers;
    }

    /**
     * Method to set the answer choices of a quiz
     *
     * @param answers new string of a answers for quiz
     */
    public void setAnswers(String answers) {
        this.answers = answers;
    }

    /**
     * Method to get the number of questions in a quiz
     *
     * @return int
     */
    public int getNumQuestions() {
        return numQuestions;
    }

    /**
     * Method to set the number of questions in a quiz
     *
     * @param numQuestions new number of questions for a quiz
     */
    public void setNumQuestions(int numQuestions) {
        this.numQuestions = numQuestions;
    }

    /**
     * Method to get the number of answer choices in a quiz
     *
     * @return int
     */
    public int getNumAnswers() {
        return numAnswers;
    }

    /**
     * Method to set the number of answer choices for each question in a quiz
     *
     * @param numAnswers new number of answer choices for quiz
     */
    public void setNumAnswers(int numAnswers) {
        this.numAnswers = numAnswers;
    }

    /**
     * Method to see if quiz will be randomized
     *
     * @return boolean
     */
    public boolean isRandom() {
        return isRandom;
    }

    /**
     * Method to set the value of random for a quiz to see if it should be randomized
     *
     * @param random new value of random to see if the quiz should be randomized
     */
    public void setRandom(boolean random) {
        isRandom = random;
    }

    /**
     * Method to get the email of teacher that created quiz
     *
     * @return String
     */
    public String getTeacherEmail() {
        return teacherEmail;
    }

    /**
     * Method to get the random order of questions
     *
     * @return String
     */
    public String getRandomQuestion() {
        return randomQuestion;
    }

    /**
     * Method to set the questions of a quiz to the new random order
     *
     * @param randomQuestion new string of random order of questions for quiz
     */
    public void setRandomQuestion(String randomQuestion) {
        this.randomQuestion = randomQuestion;
    }

    /**
     * Method to get the random order of answer choices
     *
     * @return String
     */
    public String getRandomAnswer() {
        return randomAnswer;
    }

    /**
     * Method to set the answer choices of a quiz to the new random order
     *
     * @param randomAnswer new string of random order of answer choices for questions in a quiz
     */
    public void setRandomAnswer(String randomAnswer) {
        this.randomAnswer = randomAnswer;
    }

    /**
     * Randomize the order of the questions and their corresponding answer choices
     */
    public void randomize() {
        ArrayList<Question> questionAndAnswer = new ArrayList<>();
        ArrayList<String> questionList = new ArrayList<>();
        int indexQuestion = 0;
        int counterQuestion = 0;
        for (int x = 0; x < questions.length(); x++) {
            if (questions.startsWith("**", x)) {
                questionList.add(questions.substring(indexQuestion, x));
                counterQuestion++;
                indexQuestion = x + 2;
            }
            if (counterQuestion == numQuestions) {
                break;
            }
        }
        int numAnswersUsed = 0;
        int answerIndex = 0;
        int tempIndex = 0;
        while (numAnswersUsed != numQuestions) {
            int numAnswerChoicesUsed = 0;
            ArrayList<String> answerList = new ArrayList<>();
            while (tempIndex + 1 < answers.length()) {
                if (answers.startsWith("**", tempIndex)) {
                    answerList.add(answers.substring(answerIndex, tempIndex));
                    numAnswerChoicesUsed++;
                    answerIndex = tempIndex + 2;
                }
                tempIndex++;
                if (numAnswerChoicesUsed == numAnswers) {
                    questionAndAnswer.add(new Question(questionList.get(numAnswersUsed), answerList));
                    tempIndex += 1;
                    answerIndex = tempIndex;
                    break;
                }
            }
            numAnswersUsed++;
        }
        Collections.shuffle(questionAndAnswer);
        for (int x = 0; x < numQuestions; x++) {
            Collections.shuffle(questionAndAnswer.get(x).getAnswerChoices());
        }
        String newOrderQuestion = "";
        String newOrderAnswer = "";
        for (int x = 0; x < numQuestions; x++) {
            newOrderQuestion += questionAndAnswer.get(x).getQuestion() + "**";
        }
        for (int x = 0; x < numQuestions; x++) {
            for (int y = 0; y < questionAndAnswer.get(x).getAnswerChoices().size(); y++) {
                newOrderAnswer += questionAndAnswer.get(x).getAnswerChoices().get(y) + ("**");
            }
        }
        setRandomQuestion(newOrderQuestion);
        setRandomAnswer(newOrderAnswer);
    }
}
