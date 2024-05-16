package si.uni_lj.fe.tnuv.cppaplikacija;

import java.util.ArrayList;

public class QuestionsSingleton {
    private static final QuestionsSingleton instance = new QuestionsSingleton();
    private ArrayList<Question>[] questionList;

    private QuestionsSingleton() {}

    public static QuestionsSingleton getInstance() {
        return instance;
    }

    public void setQuestionList(ArrayList<Question>[] questions) {
        this.questionList = questions;
    }

    public ArrayList<Question>[] getQuestionList() {
        return questionList;
    }

    public int getTotalNumberOfQuestions() {
        if (questionList == null) {
            return 0;
        }

        int totalQuestions = 0;
        for (ArrayList<Question> categoryQuestions : questionList) {
            if (categoryQuestions != null) {
                totalQuestions += categoryQuestions.size();
            }
        }
        return totalQuestions;
    }

}
