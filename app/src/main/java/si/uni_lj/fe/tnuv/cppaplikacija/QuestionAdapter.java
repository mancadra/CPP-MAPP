package si.uni_lj.fe.tnuv.cppaplikacija;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

// glede na klik vrne izbrano vprašanje
public class QuestionAdapter extends RecyclerView.Adapter<QuestionListViewHolder> {
    private final Context context;
    private List<Question> questions;

    public QuestionAdapter (Context context, List<Question> questions) {
        this.context = context;
        this.questions = questions;
    }

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

        // dodamo poslušalca, če uprabnik klikne se odpre izbira tipa kviza
        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int questionId = question.getId();
                String questionText = question.getQuestionText();

                /*
                Intent intent = new Intent(context, ChooseQuizTypeActivity.class);
                intent.putExtra("category_title", categoryTitle);
                intent.putExtra("category_id", categoryId);
                intent.putExtra("image", image);

                context.startActivity(intent);*/
       //     }
      //  });
    }

    @Override
    public int getItemCount() {
        return questions != null ? questions.size() : 0;
    }

}
