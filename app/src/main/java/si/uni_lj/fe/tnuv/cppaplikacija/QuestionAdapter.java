package si.uni_lj.fe.tnuv.cppaplikacija;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

// glede na klik vrne izbrano vprašanje
public class QuestionAdapter extends RecyclerView.Adapter<QuestionListViewHolder> {
    private List<Question> questions;
    public int categoryId;
    public String categoryTitle;

    public QuestionAdapter (List<Question> questions) {
        this.questions = questions;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QuestionListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_list_card, parent, false);
        return new QuestionListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionListViewHolder holder, int position) {
        Question question = questions.get(position);
        holder.bind(question);

        holder.itemView.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                Question question1 = questions.get(adapterPosition);
                int questionId = question1.getId();

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
