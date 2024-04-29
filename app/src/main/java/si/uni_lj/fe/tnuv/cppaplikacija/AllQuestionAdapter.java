package si.uni_lj.fe.tnuv.cppaplikacija;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// glede na klik vrne izbran sezanm vprasanj
public class AllQuestionAdapter extends RecyclerView.Adapter<AllQuestionListViewHolder> {
    private List<Question> questions;

    public AllQuestionAdapter(List<Question> questions) {
        this.questions = questions;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AllQuestionListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_question_list_card, parent, false);
        return new AllQuestionListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllQuestionListViewHolder holder, int position) {
        Question question = questions.get(position);
        holder.bind(question);


        // dodamo poslušalca, če uprabnik klikne se odpre izbira tipa kviza
        /*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Question question = questions.get(position);
                int questionId = question.getId();
                String questionText = question.getQuestionText();

                Intent intent = new Intent(v.getContext(), QuizActivity.class);
                intent.putExtra("question_id", questionId);
                intent.putExtra("question_text", questionText);
                v.getContext().startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return questions != null ? questions.size() : 0;
    }
}
