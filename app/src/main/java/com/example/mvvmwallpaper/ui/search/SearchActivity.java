package com.example.mvvmwallpaper.ui.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mvvmwallpaper.R;
import com.example.mvvmwallpaper.pojo.WallpapersModel;
import com.example.mvvmwallpaper.ui.main.CategoriesWallpapersAdapter;
import com.example.mvvmwallpaper.ui.main.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    ArrayList<WallpapersModel> wallpapersList;
    CategoriesWallpapersAdapter adapter;

    EditText searchEditTxt;
    RelativeLayout main;
    InputMethodManager imm;
    RecyclerView wallpaperRV;
    ProgressBar loadingPB;
    ImageView back;

    MainViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //Instantiating viewModel
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        //getting views
        searchEditTxt = findViewById(R.id.idEdtSearch);
        main = findViewById(R.id.mainLayout);
        wallpaperRV = findViewById(R.id.idRVWallpapers);
        loadingPB = findViewById(R.id.idPBLoading);
        back=findViewById(R.id.goBack);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });

        searchEditTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String category = searchEditTxt.getText().toString();
                    viewModel.getWallpapersByCategory(category);

                    adapter = new CategoriesWallpapersAdapter(viewModel.wallpaperDataList, SearchActivity.this);
                    wallpaperRV.setLayoutManager(new GridLayoutManager(SearchActivity.this, 3));
                    wallpaperRV.setAdapter(adapter);

                    getWallpapers();
                    handled = true;

                }
                return handled;
            }
        });

        viewModel.getmProgressBar().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                loadingPB.setVisibility(integer);
            }
        });

        getSearchView();

    }


    public void getWallpapers() {
        imm.hideSoftInputFromWindow(searchEditTxt.getWindowToken(), 0);
        viewModel.wallpapersMutableLiveData.observe(this, new Observer<List<WallpapersModel>>() {
            @Override
            public void onChanged(List<WallpapersModel> wallpapersModels) {
                adapter.setWallpaperList(wallpapersModels);

            }
        });


    }

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void getSearchView() {
        searchEditTxt.requestFocus();
        showSoftKeyboard(searchEditTxt);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        searchEditTxt.clearFocus();
        imm.hideSoftInputFromWindow(searchEditTxt.getWindowToken(), 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        searchEditTxt.clearFocus();
        imm.hideSoftInputFromWindow(searchEditTxt.getWindowToken(), 0);
    }
}