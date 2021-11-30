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

    public String randomizeQuestions() {
        ArrayList<String> questionList = new ArrayList<>();
        int index = 0;
        int counter = 0;
        for(int x = 0; x < questions.length(); x++) {
            if (questions.substring(x, x+2).equals("**")) {
                questionList.add(questions.substring(index, x));
                counter++;
                index = x+2;
            }
            if (counter == numQuestions) {
                break;
            }
        }
        Collections.shuffle(questionList);
        String newOrder = "";
        for (int x = 0; x < numQuestions; x++) {
            newOrder += questionList.get(x) + "**";
        }
        return newOrder;
    }
}
