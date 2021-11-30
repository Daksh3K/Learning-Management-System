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
}
