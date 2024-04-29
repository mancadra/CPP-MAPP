package si.uni_lj.fe.tnuv.cppaplikacija;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// hranimo reference pogledov
public class AllQuestionListViewHolder extends RecyclerView.ViewHolder {
    TextView questionIdTextView;
    TextView questionTextTextView;

    public AllQuestionListViewHolder(@NonNull View itemView) {
        super(itemView);
        questionIdTextView = itemView.findViewById(R.id.questionIdTextView);
        questionTextTextView = itemView.findViewById((R.id.questionTextTextView));
    }

    public void bind(Question question) {
        questionIdTextView.setText(String.valueOf(question.getId()));
        questionTextTextView.setText(question.getQuestionText());
    }
}
