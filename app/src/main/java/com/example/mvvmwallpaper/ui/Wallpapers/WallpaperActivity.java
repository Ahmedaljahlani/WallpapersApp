package com.example.mvvmwallpaper.ui.Wallpapers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mvvmwallpaper.R;
import com.example.mvvmwallpaper.ui.main.MainActivity;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class WallpaperActivity extends AppCompatActivity {

    private static final String TAG = "error";
    WallpaperManager wallpaperManager;
    ImageView imageView, failToLoad,
            blurred, back, setWallpaper, download,mainImage;
    String url;
    RelativeLayout mRelativeLayout,relative;
    LinearLayout linearLayout;
    Bitmap bitmap;
    BitmapDrawable drawable;
    OutputStream outputStream;
    Button retry;
    private ProgressBar LoadingPB;
    EditText searchEditTxt;
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_main_test);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        init();

        String avg_color = getIntent().getStringExtra("avg_color");
//        int placeholder = Color.parseColor(avg_color);
        url = getIntent().getStringExtra("imgUrl");

        Glide.with(this).load(url)
                .thumbnail(0.01f).placeholder(R.color.blure_image)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .transform(new BlurTransformation(25, 50))
                .placeholder(Drawable.createFromPath(avg_color))
                .into(blurred);

        Glide.with(this).load(url).into(mainImage);

        loadingWallpapers();

        wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        setWallpaper = findViewById(R.id.idBtnSetWallpaper);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(WallpaperActivity.this).asBitmap().load(url).listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        Toast.makeText(WallpaperActivity.this, "فشل التحميل..", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        try {
                            wallpaperManager.setBitmap(resource);
                            Toast.makeText(WallpaperActivity.this, "تم التعيين بنجاح", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(WallpaperActivity.this, "فشل التحميل", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        return false;
                    }
                }).submit();
                FancyToast.makeText(WallpaperActivity.this, "تم التعيين بنجاح", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
            }
        });

        getStatusBarHeight();
//        relative.setPadding(0,getStatusBarHeight(),0,0);
        linearLayout.setPadding(0,getStatusBarHeight(),0,0);

    }
    private int getStatusBarHeight() {
        int height;
        Resources myResources = getResources();
        int idStatusBarHeight = myResources.getIdentifier( "status_bar_height", "dimen", "android");
        if (idStatusBarHeight > 0) {
            height = getResources().getDimensionPixelSize(idStatusBarHeight);
//            Toast.makeText(this, "Status Bar Height = " + height, Toast.LENGTH_LONG).show();
        } else {
            height = 0;
//            Toast.makeText(this, "Resources NOT found", Toast.LENGTH_LONG).show();
        }
        return height;
    }


    public void init() {
        mRelativeLayout=findViewById(R.id.root);
//        relative=findViewById(R.id.relative);
        linearLayout=findViewById(R.id.linearlayout);
        imageView = findViewById(R.id.image);
        mainImage=findViewById(R.id.mainImage);
        LoadingPB = findViewById(R.id.idPBLoading);
        failToLoad = findViewById(R.id.fail);
        searchEditTxt = findViewById(R.id.idEdtSearch);
        blurred = findViewById(R.id.blurred_image);
        back = findViewById(R.id.goBack);
        setWallpaper = findViewById(R.id.idBtnSetWallpaper);
        download = findViewById(R.id.idBtnDownload);
    }


    public void loadingWallpapers() {

        //Picasso.get().load(url).into(imageView);
        LoadingPB.setVisibility(View.VISIBLE);
        Glide.with(this).load(url)
                .thumbnail(0.01f).placeholder(R.color.blure_image)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                LoadingPB.setVisibility(View.VISIBLE);
                failToLoad.setVisibility(View.VISIBLE);
                Toast.makeText(WallpaperActivity.this, "فشل التحميل", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                LoadingPB.setVisibility(View.GONE);
                return false;
            }
        }).into(imageView);
    }

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(WallpaperActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(WallpaperActivity.this, new String[]{permission}, requestCode);
        } else {
//            Toast.makeText(WallpaperActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImage() {
        //checking permission

        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE_CODE);
        try {
            bitmap = ((BitmapDrawable) mainImage.getDrawable()).getBitmap();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "saveImage: "+e.getMessage());
        }
//        String time = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
//
//        File path = Environment.getExternalStorageDirectory();
//
//        File dir = new File(path + "/WallpaperApp");
//        if (!path.exists()) {
//            dir.mkdir();
//        }

//        String imgName = time + ".JPEG";
//        File file = new File(dir, imgName);
//        OutputStream out;
//
//        try {
//            out = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//            out.flush();
//            out.close();
//            Toast.makeText(WallpaperActivity.this, "تم التحميل بنجاح", Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            Toast.makeText(WallpaperActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//            Log.e("error",e.getMessage());
//        }

        saveImageToGallery(bitmap);
        //Quick Gallery refresh
//        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        intent.setData(Uri.fromFile(file));
//        sendBroadcast(intent);
    }

    private void saveImageToGallery(Bitmap bitmap){

        OutputStream fos;

        try{

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){

                ContentResolver resolver = getContentResolver();
                ContentValues contentValues =  new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Image_" + ".jpg");
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "TestFolder");
                Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

                fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                Objects.requireNonNull(fos);

                Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(imageUri);
                sendBroadcast(intent);
            }
            else{

                // Save image to gallery
                String savedImageURL = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Bird", "Image of bird");

                // Parse the gallery image url to uri
                Uri savedImageURI = Uri.parse(savedImageURL);

                Toast.makeText(this, "Image saved to internal!!", Toast.LENGTH_SHORT).show();
//                resetOpTimes();

            }

        }catch(Exception e){

            Toast.makeText(this, "Image not saved \n" + e.toString(), Toast.LENGTH_SHORT).show();
        }

//        try{
//
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
//
//                ContentResolver resolver = getContentResolver();
//                ContentValues contentValues =  new ContentValues();
//                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Image_" + ".jpg");
//                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
//                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "TestFolder");
//                Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
//
//                fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                Objects.requireNonNull(fos);
//
//                Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT).show();
//
//                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                intent.setData(imageUri);
//                sendBroadcast(intent);
//            }
//            else{
//
//                // Save image to gallery
//                String savedImageURL = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Bird", "Image of bird");
//
//                // Parse the gallery image url to uri
//                Uri savedImageURI = Uri.parse(savedImageURL);
//
//                Toast.makeText(this, "Image saved to internal!!", Toast.LENGTH_SHORT).show();
////                resetOpTimes();
//
//            }
//
//        }catch(Exception e){
//
//            Toast.makeText(this, "Image not saved \n" + e.toString(), Toast.LENGTH_SHORT).show();
//        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_CODE) {
            if (grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                saveImage();
            } else {
                Toast.makeText(this, "Permission enable", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == WRITE_EXTERNAL_STORAGE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveImage();
            } else {
                Toast.makeText(WallpaperActivity.this, "Please provide the required permission", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}