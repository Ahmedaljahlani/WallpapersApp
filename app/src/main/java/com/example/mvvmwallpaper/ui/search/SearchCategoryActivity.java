package com.example.mvvmwallpaper.ui.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mvvmwallpaper.R;
import com.example.mvvmwallpaper.pojo.WallpapersModel;
import com.example.mvvmwallpaper.ui.main.CategoriesWallpapersAdapter;
import com.example.mvvmwallpaper.ui.main.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchCategoryActivity extends AppCompatActivity{

    ArrayList<WallpapersModel> wallpapersList;
    MainViewModel model;
    CategoriesWallpapersAdapter wallpaperRVAdapter;
    TextView categoryTV;
    ProgressBar loadingPB;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_search);

        categoryTV=findViewById(R.id.categoryTxt);
        loadingPB=findViewById(R.id.idPBLoading);
        back=findViewById(R.id.goBack);

//        wallpapersList=new ArrayList<>();
//        wallpapersList = (ArrayList<WallpapersModel>) getIntent().getExtras().getSerializable("categories");
        wallpapersList=getIntent().getParcelableArrayListExtra("categories");
        String category=getIntent().getStringExtra("cat");
        categoryTV.setText(category);

        model = ViewModelProviders.of(this).get(MainViewModel.class);
//
        model.getWallpapersByCategory(category);
        RecyclerView recyclerView = findViewById(R.id.categoriesWallpapers);
        wallpaperRVAdapter = new CategoriesWallpapersAdapter(model.wallpaperDataList, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(wallpaperRVAdapter);

        model.wallpapersMutableLiveData.observe(this, new Observer<List<WallpapersModel>>() {
            @Override
            public void onChanged(List<WallpapersModel> wallpapersModels) {
                wallpaperRVAdapter.setWallpaperList(wallpapersModels);

            }
        });

        model.getmProgressBar().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                loadingPB.setVisibility(integer);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

//    @Override
//    public void onCategoryClick(int position) {
//        String category = model.categoryDataList.get(position).getCategory();
////        Intent intent=new Intent(MainActivity.this, WallpaperActivity.class);
////        startActivity(intent);
//        model.getWallpapersByCategory(category);
//        model.wallpapersMutableLiveData.observe(CategoriesActivity.this, new Observer<List<WallpapersModel>>() {
//            @Override
//            public void onChanged(List<WallpapersModel> wallpapersModels) {
//            }
//        });
//    }
}