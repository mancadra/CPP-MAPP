package si.uni_lj.fe.tnuv.cppaplikacija;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

// adapter poveže vir podatkov z RecycleView iz ChooseCategoryActivity
public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    private final Context context;
    private final List<Category> categoryList;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_card, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.bind(category);

        // dodamo poslušalca, če uorabnik klikne se odpre izbira tipa kviza
        holder.itemView.setOnClickListener(v -> {
            Log.d("CategoryAdapter","OnClick: category selected : " + position);
            String categoryTitle = category.getTitle();
            int categoryId = category.getId();
            int image = category.getImageResource();

            Intent intent = new Intent(context, ChooseQuizTypeActivity.class);
            intent.putExtra("category_title", categoryTitle);
            intent.putExtra("category_id", categoryId);
            intent.putExtra("image", image);

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
