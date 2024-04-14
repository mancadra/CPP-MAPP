package si.uni_lj.fe.tnuv.cppaplikacija;

public class Category {
    private String title;
    private int numQuestions;
    private int imageResource;

    private final int id;

    public Category(String title, int numQuestions, int imageResource, int id) {
        this.title = title;
        this.numQuestions = numQuestions;
        this.imageResource = imageResource;
        this.id = id;
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

    public int getId() {
        return id;
    }

}
