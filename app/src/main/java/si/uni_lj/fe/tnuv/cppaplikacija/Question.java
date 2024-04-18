package si.uni_lj.fe.tnuv.cppaplikacija;

public class Question {
    private String questionText;
    private int nrCorrAnswers;

    private String[] answers;

    private int categoryId;

    private String[] correctAnswers;

    private int imageResource;

    private final int id;

    public Question(String questionText, int nrCorrAnswers, String[] answers, int categoryId, String[] correctAnswers, int imageResource, int id) {
        this.questionText = questionText;
        this.nrCorrAnswers = nrCorrAnswers;
        this.answers = answers;
        this.categoryId = categoryId;
        this.correctAnswers = correctAnswers;
        this.imageResource = imageResource;
        this.id = id;
    }

    public String getQuestionText() { return questionText; }

    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public int getNrCorrAnswers() { return nrCorrAnswers; }

    public void setNrCorrAnswers(int nrCorrAnswers) { this.nrCorrAnswers = nrCorrAnswers; }

    public String[] getAnswers() { return answers; }

    public void setAnswers(String[] answers) { this.answers = answers; }

    public int getCategoryId() { return categoryId; }

    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String[] getCorrectAnswers() { return correctAnswers; }

    public void setCorrectAnswers(String[] correctAnswers) { this.correctAnswers = correctAnswers; }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }


    public int getId() { return id; }


}
