package com.example.mvvmwallpaper.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mvvmwallpaper.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class WallpaperActivity extends AppCompatActivity {

    WallpaperManager wallpaperManager;
    ImageView imageView;
    LinearLayout failToLoad, bottomBtns;
    String url;
    Bitmap bitmap;
    BitmapDrawable drawable;
    OutputStream outputStream;
    Button download, setWallpaper, retry;
    private ProgressBar LoadingPB;
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);

        failToLoad = findViewById(R.id.fail);
        bottomBtns = findViewById(R.id.IdBottomButtons);

        url = getIntent().getStringExtra("imgUrl");
        imageView = findViewById(R.id.image);
        LoadingPB = findViewById(R.id.idPBLoading);

        loadingWallpapers();

        wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        setWallpaper = findViewById(R.id.idBtnSetWallpaper);


    }


    public void loadingWallpapers() {

        //Picasso.get().load(url).into(imageView);
        //LoadingPB.setVisibility(View.GONE);
        Glide.with(this).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                LoadingPB.setVisibility(View.GONE);
                failToLoad.setVisibility(View.VISIBLE);
                bottomBtns.setVisibility(View.GONE);
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
            Toast.makeText(WallpaperActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImage() {
        //checking permission
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE_CODE);
        bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        String time = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());

        File path = Environment.getExternalStorageDirectory();
        File dir = new File(path + "/WallpaperApp");
        if (!path.exists()) {
            dir.mkdir();
        }

        String imgName = time + ".JPEG";
        File file = new File(dir, imgName);
        OutputStream out;

        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Toast.makeText(WallpaperActivity.this, "تم التحميل بنجاح", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(WallpaperActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
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

//        if (requestCode == WRITE_EXTERNAL_STORAGE_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                saveImage();
//            } else {
//                Toast.makeText(WallpaperActivity.this, "Please provide the required permission", Toast.LENGTH_SHORT).show();
//            }
//        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}