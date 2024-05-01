package si.uni_lj.fe.tnuv.cppaplikacija;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.ArrayList;
import java.util.List;

// Shrani fetched data iz baze, dostopno iz vseh aktivnosti
public class QuestionViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Question>[]> questionListLiveData = new MutableLiveData<>();

    public void setQuestionList(ArrayList<Question>[] questions) {
        questionListLiveData.setValue(questions);
    }

    public LiveData<ArrayList<Question>[]> getQuestionListLiveData() {
        return questionListLiveData;
    }
}
