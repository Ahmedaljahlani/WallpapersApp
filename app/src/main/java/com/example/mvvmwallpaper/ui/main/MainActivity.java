package com.example.mvvmwallpaper.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.mvvmwallpaper.R;
import com.example.mvvmwallpaper.pojo.CategoryRVModel;
import com.example.mvvmwallpaper.pojo.WallpapersModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoriesRVAdapter.CategoryClickInterface {

    WallpaperRVAdapter wallpaperRVAdapter;
    CategoriesRVAdapter categoriesRVAdapter;
    ProgressBar loadingPB;
    RecyclerView wallpaperRv;
    CategoriesRVAdapter.CategoryClickInterface categoryClickInterface;

    MainViewModel wallpapersModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Window window = this.getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.setStatusBarColor(this.getResources().getColor(R.color.transparent));

        loadingPB=findViewById(R.id.idPBLoading);
        loadingPB.setVisibility(View.VISIBLE);

        wallpapersModel= ViewModelProviders.of(this).get(MainViewModel.class);

        wallpapersModel.getWallpapers();
        wallpapersModel.getCategories();
        wallpaperRv=findViewById(R.id.idRVWallpapers);
        wallpaperRv.setHasFixedSize(true);
        wallpaperRVAdapter = new WallpaperRVAdapter(wallpapersModel.wallpaperDataList,wallpapersModel.categoryDataList,this);
        wallpaperRVAdapter.notifyDataSetChanged();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 3;
                } else {
                    return 1;
                }
            }
        });
        wallpaperRv.setLayoutManager(layoutManager);
        wallpaperRv.setAdapter(wallpaperRVAdapter);

        getWallpapersViewModel();
        getCategoriesViewModel();

        wallpapersModel.getmProgressBar().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                loadingPB.setVisibility(integer);
            }
        });
    }

    private void getWallpapersViewModel(){
        loadingPB.setVisibility(View.VISIBLE);
        wallpapersModel.wallpapersMutableLiveData.observe(this, new Observer<List<WallpapersModel>>() {
            @Override
            public void onChanged(List<WallpapersModel> wallpapersModels) {
                loadingPB.setVisibility(View.GONE);
                wallpaperRVAdapter.setWallpaperList(wallpapersModels);
            }
        });
    }

    private void getCategoriesViewModel(){
        wallpapersModel.categoryMutableLiveData.observe(this, new Observer<List<CategoryRVModel>>() {
            @Override
            public void onChanged(List<CategoryRVModel> categoryRVModels) {
                loadingPB.setVisibility(View.GONE);
                categoriesRVAdapter=new CategoriesRVAdapter(wallpapersModel.categoryDataList,MainActivity.this,MainActivity.this);
                categoriesRVAdapter.setCategoryRVModels(wallpapersModel.categoryDataList);
            }
        });
    }

    @Override
    public void onCategoryClick(int position) {
        String category = wallpapersModel.categoryDataList.get(position).getCategory();
//        Intent intent=new Intent(MainActivity.this, WallpaperActivity.class);
//        startActivity(intent);
        wallpapersModel.getWallpapersByCategory(category);
        wallpapersModel.wallpapersMutableLiveData.observe(MainActivity.this, new Observer<List<WallpapersModel>>() {
            @Override
            public void onChanged(List<WallpapersModel> wallpapersModels) {
                wallpaperRVAdapter.setWallpaperList(wallpapersModels);
            }
        });

    }

}