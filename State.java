public class State {
    private String studentsString; // "John Doe,jdoe@gmail.com\nJane Doe,janed@gmail.com\n ..."
    private String teachersString; // "John Doe,jdoe@gmail.com\nJane Doe,janed@gmail.com\n ..."
    private String coursesString; // "CS180,CS193,CS191 ..."
    private String quizzesString; // "quiz 1,quiz 2,quiz 3 ...

    public State(String studentsString, String teachersString, String coursesString, String quizzesString) {
        this.studentsString = studentsString;
        this.teachersString = teachersString;
        this.coursesString = coursesString;
        this.quizzesString = quizzesString;
    }

    public String getStudentsString() {
        return studentsString;
    }

    public void setStudentsString(String studentsString) {
        this.studentsString = studentsString;
    }

    public String getTeachersString() {
        return teachersString;
    }

    public void setTeachersString(String teachersString) {
        this.teachersString = teachersString;
    }

    public String getCoursesString() {
        return coursesString;
    }

    public void setCoursesString(String coursesString) {
        this.coursesString = coursesString;
    }

    public String getQuizzesString() {
        return quizzesString;
    }

    public void setQuizzesString(String quizzesString) {
        this.quizzesString = quizzesString;
    }

    public int getNumberOfStudents() {
        String[] studentsList = this.studentsString.split("\\r\\n|\\r|\\n");

        return studentsList.length;
    }

    public int getNumberOfTeachers() {
        String[] teachersList = this.teachersString.split("\\r\\n|\\r|\\n");

        return teachersList.length;
    }

    public int getNumberOfCourses() {
        String[] coursesList = this.coursesString.split(",");

        return coursesList.length;
    }

    public int getNumberOfQuizzes() {
        String[] quizList = this.quizzesString.split(",");

        return quizList.length;
    }
}
