import java.util.ArrayList;
import java.util.Collections;

public class Quiz {
    private String quizName;
    private String questions;
    private String answers;
    private int numQuestions;
    private int numAnswers;
    private boolean isRandom;
    private String teacherEmail;

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

    public String randomize() {
        ArrayList<Question> questionAndAnswer = new ArrayList<>();
        ArrayList<String> questionList = new ArrayList<>();
        int indexQuestion = 0;
        int counterQuestion = 0;
        for (int x = 0; x < questions.length(); x++) {
            if (questions.substring(x, x + 2).equals("**")) {
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
            while (tempIndex + 1 < answers.length()) {
                ArrayList<String> answerList = new ArrayList<>();
                if (answers.substring(tempIndex, tempIndex + 2).equals("**")) {
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
        String newQuestions = "";
        String newAnswers = "";
        for (int x = 0; x < numQuestions; x++) {
            newQuestions += questionAndAnswer.get(x).getQuestion() + "**";
        }
        for (int x = 0; x < numQuestions; x++) {
            for (int y = 0; y < questionAndAnswer.get(x).getAnswerChoices().size(); y++) {
                newAnswers += questionAndAnswer.get(x).getAnswerChoices().get(y) + "**";
            }
        }
        return newQuestions + newAnswers;
    }
}
