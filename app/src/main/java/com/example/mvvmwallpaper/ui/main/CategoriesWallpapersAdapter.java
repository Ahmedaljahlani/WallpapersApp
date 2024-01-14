package com.example.mvvmwallpaper.ui.main;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.mvvmwallpaper.R;
import com.example.mvvmwallpaper.pojo.WallpapersModel;
import com.example.mvvmwallpaper.ui.Wallpapers.WallpaperActivity;

import java.util.ArrayList;
import java.util.List;

public class CategoriesWallpapersAdapter extends RecyclerView.Adapter<CategoriesWallpapersAdapter.viewHolder> {

    List<WallpapersModel> wallpapersList = new ArrayList<>();
    Context context;

    public CategoriesWallpapersAdapter(ArrayList<WallpapersModel> wallpapersList, Context context) {
        this.wallpapersList = wallpapersList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.wallpaper_rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        try {
            Glide.with(context).load(wallpapersList.get(position).getSrc().getPortrait())
                    .thumbnail(0.01f).placeholder(R.color.blure_image)
                    .transition(DrawableTransitionOptions.withCrossFade(500)).into(holder.wallpaper);
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, WallpaperActivity.class);
                intent.putExtra("imgUrl", wallpapersList.get(position).getSrc().getPortrait());
                intent.putExtra("avg_color", wallpapersList.get(position).getAvg_color());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return wallpapersList.size();
    }

    public void setWallpaperList(List<WallpapersModel> mWallpaperList){
        this.wallpapersList=mWallpaperList;
        notifyDataSetChanged();
    }


    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView wallpaper;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            wallpaper = itemView.findViewById(R.id.idIVWallpaper);
        }
    }
}
