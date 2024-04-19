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
    private final List<Question> questionList;

    public QuestionAdapter (Context context, List<Question> questionList) {
        this.context = context;
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public QuestionListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.question_list_card, parent, false);
        return new QuestionListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionListViewHolder holder, int position) {
        Question question = questionList.get(position);
        holder.bind(question);

        // dodamo poslušalca, če uprabnik klikne se odpre izbira tipa kviza
        holder.itemView.setOnClickListener(new View.OnClickListener() {
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
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

}
