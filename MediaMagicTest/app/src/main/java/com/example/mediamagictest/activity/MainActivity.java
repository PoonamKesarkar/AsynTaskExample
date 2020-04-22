package com.example.mediamagictest.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mediamagictest.R;
import com.example.mediamagictest.adapter.ImageAdpater;
import com.example.mediamagictest.databinding.ActivityMainBinding;
import com.example.mediamagictest.model.ImageData;
import com.example.mediamagictest.model.ImageViewModel;
import com.example.mediamagictest.util.CommonMethods;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    private ImageAdpater adapter;
    private ArrayList<ImageData> list;
    private RecyclerView recyclerView;
    private ImageViewModel imageViewModel;
    private String READ_EXTERNAL_STORAGE_PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE;
    private String WRITE_EXTERNAL_STORAGE_PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(activityMainBinding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Image List");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        recyclerView = activityMainBinding.recyclerListView;
        activityMainBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.v("Permission","on take permission");
            checkMultiplePermissions(REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS, MainActivity.this);
        } else {
            init();
        }

    }

    private void init() {
        list = new ArrayList<>();
        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);
        adapter = new ImageAdpater(this, list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
        if (new CommonMethods().isInternetConnection(MainActivity.this)) {
            getImageData();
        } else {
            Toast.makeText(MainActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        }
    }
    private void checkMultiplePermissions(int permissionCode, Context context) {
        String[] PERMISSIONS = {READ_EXTERNAL_STORAGE_PERMISSION, WRITE_EXTERNAL_STORAGE_PERMISSION};
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, permissionCode);
        } else {
            init();
        }
    }

    private boolean hasPermissions(Context context, String[] permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    private void getImageData() {
        imageViewModel.getImageList(MainActivity.this).observe(this, new Observer<ArrayList<ImageData>>() {
            @Override
            public void onChanged(ArrayList<ImageData> imageData) {
                list.addAll(imageData);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    init();
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}


