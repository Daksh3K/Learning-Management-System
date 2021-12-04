import java.util.ArrayList;
import java.util.Collections;
//to randomize the quiz, all you have to do is call object.randomize() and then use getters to return randomized questions and corresponding answer choices

public class Quiz implements Serializable {
    private String quizName;
    private String questions;
    private String answers;
    private int numQuestions;
    private int numAnswers;
    private boolean isRandom;
    private String teacherEmail;
    private String randomQuestion;
    private String randomAnswer;

    public Quiz(String quizName, String questions, String answers, int numQuestions, int numAnswers, boolean isRandom, String teacherEmail) {
        this.quizName = quizName;
        this.questions = questions;
        this.answers = answers;
        this.numQuestions = numQuestions;
        this.numAnswers = numAnswers;
        this.isRandom = isRandom;
        this.teacherEmail = teacherEmail;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public int getNumQuestions() {
        return numQuestions;
    }

    public void setNumQuestions(int numQuestions) {
        this.numQuestions = numQuestions;
    }

    public int getNumAnswers() {
        return numAnswers;
    }

    public void setNumAnswers(int numAnswers) {
        this.numAnswers = numAnswers;
    }

    public boolean isRandom() {
        return isRandom;
    }

    public void setRandom(boolean random) {
        isRandom = random;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public String getRandomQuestion() {
        return randomQuestion;
    }

    public void setRandomQuestion(String randomQuestion) {
        this.randomQuestion = randomQuestion;
    }

    public String getRandomAnswer() {
        return randomAnswer;
    }

    public void setRandomAnswer(String randomAnswer) {
        this.randomAnswer = randomAnswer;
    }

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
