package com.example.mvvmwallpaper.ui.main;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.mvvmwallpaper.R;
import com.example.mvvmwallpaper.ui.search.SearchActivity;
import com.example.mvvmwallpaper.pojo.CategoryRVModel;
import com.example.mvvmwallpaper.pojo.WallpapersModel;
import com.example.mvvmwallpaper.ui.Wallpapers.WallpaperRVAdapter;
import com.example.mvvmwallpaper.ui.search.SearchCategoryActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoriesRVAdapter.CategoryClickInterface {

    WallpaperRVAdapter wallpaperRVAdapter;
    CategoriesRVAdapter categoriesRVAdapter;
    ProgressBar loadingPB;
    RecyclerView wallpaperRv;
    CategoriesRVAdapter.CategoryClickInterface categoryClickInterface;
    Button retryBtn;
    EditText searchBar;

    MainViewModel wallpapersModel;
    InputMethodManager imm;
    SearchActivity searchActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retryBtn = findViewById(R.id.IdRetryBtn);
        loadingPB = findViewById(R.id.idPBLoading);
        loadingPB.setVisibility(View.VISIBLE);
        searchBar=findViewById(R.id.idEdtSearch);

        wallpapersModel = ViewModelProviders.of(this).get(MainViewModel.class);

//        Toast.makeText(this, wallpapersModel.wallpaperDataList.size()+"", Toast.LENGTH_SHORT).show();
        wallpapersModel.getWallpapers();
        wallpapersModel.getCategories();
        wallpaperRv = findViewById(R.id.idRVWallpapers);
        wallpaperRv.setHasFixedSize(true);
        wallpaperRVAdapter = new WallpaperRVAdapter(wallpapersModel.wallpaperDataList, wallpapersModel.categoryDataList, this);
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
        getRetryButton();
        getLoadingProgressBar();


        wallpapersModel.searchBar.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                searchBar.setVisibility(integer);
                searchBar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    }
                });
            }
        });
    }


    //getting wallpapers view model
    private void getWallpapersViewModel() {
        loadingPB.setVisibility(View.VISIBLE);
        wallpapersModel.wallpapersMutableLiveData.observe(this, new Observer<List<WallpapersModel>>() {
            @Override
            public void onChanged(List<WallpapersModel> wallpapersModels) {
                loadingPB.setVisibility(View.GONE);
                wallpaperRVAdapter.setWallpaperList(wallpapersModels);
            }
        });
    }

    //getting categories view model
    private void getCategoriesViewModel() {
        wallpapersModel.categoryMutableLiveData.observe(this, new Observer<List<CategoryRVModel>>() {
            @Override
            public void onChanged(List<CategoryRVModel> categoryRVModels) {
                loadingPB.setVisibility(View.GONE);
                categoriesRVAdapter = new CategoriesRVAdapter(wallpapersModel.categoryDataList, MainActivity.this, MainActivity.this);
                categoriesRVAdapter.setCategoryRVModels(wallpapersModel.categoryDataList);
            }
        });
    }

    //on category click interface
    @Override
    public void onCategoryClick(int position) {
        String category = wallpapersModel.categoryDataList.get(position).getCategory();
        Intent intent = new Intent(MainActivity.this, SearchCategoryActivity.class);
        intent.putExtra("categories", wallpapersModel.wallpaperDataList);
        intent.putExtra("cat", wallpapersModel.categoryDataList.get(position).getCategory());
        Log.e(TAG, "onCategoryClick: " + wallpapersModel.wallpaperDataList);
        startActivity(intent);
//        wallpapersModel.getWallpapersByCategory(category);
//        wallpapersModel.wallpapersMutableLiveData.observe(MainActivity.this, new Observer<List<WallpapersModel>>() {
//            @Override
//            public void onChanged(List<WallpapersModel> wallpapersModels) {
//                wallpaperRVAdapter.setWallpaperList(wallpapersModels);
//            }
//        });
    }

    //getting loading progressBar view model
    public void getLoadingProgressBar() {
        wallpapersModel.getmProgressBar().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                loadingPB.setVisibility(integer);
            }
        });
    }

    //Retry button
    public void getRetryButton() {
        wallpapersModel.mButton.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                retryBtn.setVisibility(integer);
                retryBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        wallpapersModel.getWallpapers();
                    }
                });
            }
        });
    }
}