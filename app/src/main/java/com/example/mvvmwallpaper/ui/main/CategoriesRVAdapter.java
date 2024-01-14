package com.example.mvvmwallpaper.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.mvvmwallpaper.R;
import com.example.mvvmwallpaper.pojo.CategoryRVModel;

import java.util.ArrayList;
import java.util.List;

public class CategoriesRVAdapter extends RecyclerView.Adapter<CategoriesRVAdapter.viewHolder> {

    List<CategoryRVModel> categoryRVModels=new ArrayList<>();
    Context context;
    private final CategoryClickInterface categoryClickInterface;


    public CategoriesRVAdapter(List<CategoryRVModel> categoryRVModels, Context context,CategoryClickInterface categoryClickInterface) {
        this.categoryRVModels = categoryRVModels;
        this.context=context;
        this.categoryClickInterface=categoryClickInterface;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_rv_item, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        CategoryRVModel model = categoryRVModels.get(position);
        holder.categoryTV.setText(model.getCategory());
        if (!model.getImgUrl().isEmpty()) {
//            Glide.with(context).load(model.getImgUrl()).placeholder(R.color.black_shade_1).into(holder.categoryIV);
            Glide.with(context).load(model.getImgUrl()).thumbnail(0.01f).placeholder(R.color.blure_image).transition(DrawableTransitionOptions.withCrossFade(500)).into(holder.categoryIV);
        } else {
            holder.categoryIV.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryClickInterface.onCategoryClick(position);
//                Intent intent=new Intent(context, CategoriesActivity.class);
//                intent.putExtra("categories",categoryRVModels.get(position).getCategory());
//                Log.e(TAG, "onClick: "+categoryRVModels.get(position) );
//                context.startActivity(intent);
            }
        });
//        if (!model.getImgUrl().isEmpty()){
//            try {
//                Glide.with(context).load(model.getImgUrl()).placeholder(R.color.black_shade_1).into(holder.categoryIV);
//            }catch (Exception e){
//                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
//            }
//
//        }else {
//            holder.categoryIV.setImageResource(R.drawable.ic_launcher_background);
//        }


    }

    @Override
    public int getItemCount() {
        return categoryRVModels.size();
    }

    public void setCategoryRVModels(List<CategoryRVModel> categoryRVModels){
        this.categoryRVModels=categoryRVModels;
        notifyDataSetChanged();
    }
    public interface CategoryClickInterface {
        void onCategoryClick(int position);
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private final TextView categoryTV;
        private final ImageView categoryIV;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            categoryIV = itemView.findViewById(R.id.idIVCategory);
            categoryTV = itemView.findViewById(R.id.idTVCategory);
        }
    }
}
