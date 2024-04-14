package si.uni_lj.fe.tnuv.cppaplikacija;

public class Category {
    private String title;
    private int numQuestions;
    private int imageResource;

    public Category(String title, int numQuestions, int imageResource) {
        this.title = title;
        this.numQuestions = numQuestions;
        this.imageResource = imageResource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumQuestions() {
        return numQuestions;
    }

    public void setNumQuestions(int numQuestions) {
        this.numQuestions = numQuestions;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
