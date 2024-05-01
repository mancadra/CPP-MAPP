package si.uni_lj.fe.tnuv.cppaplikacija;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// glede na klik vrne izbran sezanm vprasanj
public class AllQuestionAdapter extends RecyclerView.Adapter<AllQuestionListViewHolder> {
    private List<Question> questions;
    public int categoryId;
    public String categoryTitle;

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
        holder.itemView.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                Question question1 = questions.get(adapterPosition);
                int questionId = question1.getId();
                String questionText = question1.getQuestionText();

                Intent intent = new Intent(v.getContext(), QuizActivity.class);
                intent.putExtra("question_id", questionId);
                intent.putExtra("category_id", categoryId);
                intent.putExtra("category_title", categoryTitle);
                intent.putExtra("type", "list");
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions != null ? questions.size() : 0;
    }
}
