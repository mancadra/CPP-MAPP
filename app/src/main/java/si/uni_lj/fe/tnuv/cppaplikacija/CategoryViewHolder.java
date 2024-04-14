package si.uni_lj.fe.tnuv.cppaplikacija;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// hranimo reference pogledov
public class CategoryViewHolder extends RecyclerView.ViewHolder {
    TextView categoryTitleTextView;
    TextView numQuestionsTextView;
    ImageView categoryImageView;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        categoryTitleTextView = itemView.findViewById(R.id.categoryTitleTextView);
        numQuestionsTextView = itemView.findViewById(R.id.numQuestionsTextView);
        categoryImageView = itemView.findViewById(R.id.categoryImageView);
    }

    public void bind(Category category) {
        categoryTitleTextView.setText(category.getTitle());
        numQuestionsTextView.setText(itemView.getContext().getString(R.string.num_questions_placeholder, category.getNumQuestions()));
        categoryImageView.setImageResource(category.getImageResource());
    }

}

