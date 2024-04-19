package si.uni_lj.fe.tnuv.cppaplikacija;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// hranimo reference pogledov
public class QuestionListViewHolder extends RecyclerView.ViewHolder {

    TextView questionIdTextView;
    TextView questionTextTextView;

    public QuestionListViewHolder(@NonNull View itemView) {
        super(itemView);
        questionIdTextView = itemView.findViewById(R.id.questionIdTextView);
        questionTextTextView = itemView.findViewById((R.id.questionTextTextView));
    }

    public void bind(Question question) {
        questionIdTextView.setText(question.getId());
        questionTextTextView.setText(question.getQuestionText());
    }
}
