package com.example.mvvmwallpaper.ui.Wallpapers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.mvvmwallpaper.R;
import com.example.mvvmwallpaper.pojo.CategoryRVModel;
import com.example.mvvmwallpaper.pojo.WallpapersModel;
import com.example.mvvmwallpaper.ui.main.CategoriesRVAdapter;
import com.example.mvvmwallpaper.ui.main.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class WallpaperRVAdapter extends RecyclerView.Adapter<WallpaperRVAdapter.viewHolder> {

    private final Context context;
    List<WallpapersModel> wallpaperList = new ArrayList<>();
    List<CategoryRVModel> categoryRVModels = new ArrayList<>();
    CategoriesRVAdapter categoriesRVAdapter;
    MainViewModel model;
    Animation translate_anim;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public WallpaperRVAdapter(List<WallpapersModel> wallpaperList, List<CategoryRVModel> categoryRVModels, Context context) {
        this.wallpaperList = wallpaperList;
        this.categoryRVModels = categoryRVModels;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.wallpaper_rv_item, parent, false);
            return new viewHolder(v);
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories, parent, false);
            return new categoriesViewHolder(v);
        } else {
            Toast.makeText(context, "No View", Toast.LENGTH_SHORT).show();
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {

        try {
            Glide.with(context).load(wallpaperList.get(position).getSrc().getPortrait())
                    .thumbnail(0.01f).placeholder(R.color.blure_image)
                    .transition(DrawableTransitionOptions.withCrossFade(500)).into(holder.wallpaper);
//            Toast.makeText(context, wallpaperList.get(0).getAvg_color(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

//            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if (position != 0) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WallpaperActivity.class);
//                    Toast.makeText(context, wallpaperList.get(position).getSrc().getPortrait(), Toast.LENGTH_SHORT).show();
                    intent.putExtra("imgUrl", wallpaperList.get(position).getSrc().getPortrait());
                    intent.putExtra("avg_color", wallpaperList.get(position).getAvg_color());
                    context.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return wallpaperList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionCategories(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionCategories(int position) {
        return position == 0;
    }


    public void setWallpaperList(List<WallpapersModel> wallpaperList) {
        this.wallpaperList = wallpaperList;
        notifyDataSetChanged();
    }


    class viewHolder extends RecyclerView.ViewHolder {
        private final CardView imgCV;
        private final ImageView wallpaper;

        public viewHolder(View itemView) {
            super(itemView);
            imgCV = itemView.findViewById(R.id.idCVWallpaper);
            wallpaper = itemView.findViewById(R.id.idIVWallpaper);
//            translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
//            imgCV.setAnimation(translate_anim);
        }
    }

    private class categoriesViewHolder extends viewHolder {
        RecyclerView categoriesRV;

        public categoriesViewHolder(@NonNull View itemView) {
            super(itemView);

//            model= ViewModelProviders.of((FragmentActivity) context).get(MainViewModel.class);

//            model.getCategories();
            categoriesRV = itemView.findViewById(R.id.categories_rv);
            categoriesRVAdapter = new CategoriesRVAdapter(categoryRVModels, context, (CategoriesRVAdapter.CategoryClickInterface) context);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            categoriesRV.setLayoutManager(layoutManager);
            categoriesRV.setAdapter(categoriesRVAdapter);

//            model.categoryMutableLiveData.observe((LifecycleOwner) context, new Observer<List<CategoryRVModel>>() {
//                @Override
//                public void onChanged(List<CategoryRVModel> categoryRVModels) {
//                    categoriesRVAdapter.setCategoryRVModels(categoryRVModels);
//                }
//            });
        }
    }
}