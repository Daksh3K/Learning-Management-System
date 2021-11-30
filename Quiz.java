public class Quiz {
    private String quizName;
    private String questions;
    private String answers;
    private int numQuestions;
    private int numAnswers;
    private boolean isRandom;

    public Quiz(String quizName, String questions, String answers, int numQuestions, int numAnswers, boolean isRandom) {
        this.quizName = quizName;
        this.questions = questions;
        this.answers = answers;
        this.numQuestions = numQuestions;
        this.numAnswers = numAnswers;
        this.isRandom = isRandom;
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
}
