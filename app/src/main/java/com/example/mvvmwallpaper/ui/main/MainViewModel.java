package com.example.mvvmwallpaper.ui.main;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmwallpaper.data.ClientResponse;
import com.example.mvvmwallpaper.pojo.CategoryRVModel;
import com.example.mvvmwallpaper.pojo.WallpaperResponse;
import com.example.mvvmwallpaper.pojo.WallpapersModel;
import com.example.mvvmwallpaper.ui.Wallpapers.WallpaperRVAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {

    Context context;
    public ArrayList<WallpapersModel> wallpaperDataList = new ArrayList<>();
    public ArrayList<CategoryRVModel> categoryDataList = new ArrayList<>();
    WallpaperRVAdapter wallpaperRVAdapter;
    private String API_KEY = "563492ad6f91700001000001408647ec35334e55b9aae2cbadca91fd";
    private static String BASE_URL = "https://api.pexels.com/v1/";


    RecyclerView wallpaperRv;
    public MutableLiveData<List<WallpapersModel>> wallpapersMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<CategoryRVModel>> categoryMutableLiveData = new MutableLiveData<>();
    MutableLiveData<Integer> mProgressBar = new MutableLiveData<>();
    MutableLiveData<Integer> mButton = new MutableLiveData<>();
    MutableLiveData<Integer> searchBar = new MutableLiveData<>();

    public MainViewModel() {
        mProgressBar.postValue(View.GONE);
        mButton.postValue(View.GONE);
        searchBar.postValue(1);
    }

    public MutableLiveData<Integer> getSearchBar() {
        return searchBar;
    }

    public MutableLiveData<Integer> getmProgressBar() {
        return mProgressBar;
    }

    public MutableLiveData<Integer> getmButton() {
        return mButton;
    }

    public void getWallpapers() {
        mButton.postValue(View.GONE);
        mProgressBar.postValue(View.VISIBLE);
        wallpaperDataList.clear();

        Call<WallpaperResponse> wallpaperResponseCall = ClientResponse
                .getINSTANCE()
                .getAPI()
                .getWallpapers(API_KEY);

        wallpaperResponseCall.enqueue(new Callback<WallpaperResponse>() {
            @Override
            public void onResponse(Call<WallpaperResponse> call, Response<WallpaperResponse> response) {
                WallpaperResponse wallpaperResponse = response.body();

                if (response.isSuccessful()) {
                    mProgressBar.postValue(View.GONE);
                    try {
                        wallpaperDataList = response.body().getPhotosList();
                        Collections.shuffle(wallpaperDataList);
                        wallpapersMutableLiveData.setValue(wallpaperDataList);
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WallpaperResponse> call, Throwable t) {
//                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                mProgressBar.postValue(View.GONE);
                mButton.postValue(View.VISIBLE);
//                retrieveDataOffline();
            }
        });
    }

    public void getCategories() {

        categoryDataList = new ArrayList<>();
        categoryDataList.add(new CategoryRVModel("Travel", "https://images.pexels.com/photos/1157255/pexels-photo-1157255.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        categoryDataList.add(new CategoryRVModel("Nature", "https://images.pexels.com/photos/1761279/pexels-photo-1761279.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        categoryDataList.add(new CategoryRVModel("Tropical Plants", "https://images.pexels.com/photos/3722570/pexels-photo-3722570.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        categoryDataList.add(new CategoryRVModel("Flowers", "https://images.pexels.com/photos/1086178/pexels-photo-1086178.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        categoryDataList.add(new CategoryRVModel("Trees", "https://images.pexels.com/photos/1423600/pexels-photo-1423600.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        categoryDataList.add(new CategoryRVModel("Music", "https://images.pexels.com/photos/1010518/pexels-photo-1010518.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        categoryDataList.add(new CategoryRVModel("Architecture", "https://images.pexels.com/photos/256150/pexels-photo-256150.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        categoryDataList.add(new CategoryRVModel("Arts", "https://images.pexels.com/photos/1194420/pexels-photo-1194420.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        categoryDataList.add(new CategoryRVModel("Cars", "https://images.pexels.com/photos/4065818/pexels-photo-4065818.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        categoryDataList.add(new CategoryRVModel("Technology", "https://images.pexels.com/photos/3082341/pexels-photo-3082341.jpeg?&auto=format&fit=crop&w=500&q=60"));
        categoryDataList.add(new CategoryRVModel("Beach", "https://images.pexels.com/photos/853199/pexels-photo-853199.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260"));
        categoryDataList.add(new CategoryRVModel("Mountain", "https://images.pexels.com/photos/1820563/pexels-photo-1820563.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260"));
        categoryDataList.add(new CategoryRVModel("Field", "https://images.pexels.com/photos/259280/pexels-photo-259280.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260"));
        categoryDataList.add(new CategoryRVModel("Clouds", "https://images.pexels.com/photos/417063/pexels-photo-417063.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260"));
        categoryDataList.add(new CategoryRVModel("Condensation", "https://images.pexels.com/photos/7175544/pexels-photo-7175544.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260"));
        categoryDataList.add(new CategoryRVModel("4K", "https://images.pexels.com/photos/2486168/pexels-photo-2486168.jpeg?cs=srgb&dl=pexels-roberto-nickson-2486168.jpg&fm=jpg"));
        categoryDataList.add(new CategoryRVModel("8K", "https://images.pexels.com/photos/3849167/pexels-photo-3849167.jpeg?cs=srgb&dl=pexels-kehn-hermano-3849167.jpg&fm=jpg"));

        Collections.shuffle(categoryDataList);
        categoryMutableLiveData.setValue(categoryDataList);
    }

    public void getWallpapersByCategory(String category) {
        mProgressBar.postValue(View.VISIBLE);
        wallpaperDataList.clear();
        Call<WallpaperResponse> wallpaperResponseCall = ClientResponse
                .getINSTANCE()
                .getAPI()
                .getCategories(API_KEY, category);

        wallpaperResponseCall.enqueue(new Callback<WallpaperResponse>() {
            @Override
            public void onResponse(Call<WallpaperResponse> call, Response<WallpaperResponse> response) {
                WallpaperResponse wallpaperResponse = response.body();
                if (response.isSuccessful()) {
                    mProgressBar.postValue(View.GONE);
                    try {
                        wallpaperDataList = response.body().getPhotosList();
                        wallpapersMutableLiveData.setValue(wallpaperDataList);
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WallpaperResponse> call, Throwable t) {
//                Toast.makeText(context, "t.getMessage()", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
