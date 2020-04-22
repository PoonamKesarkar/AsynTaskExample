package com.example.mediamagictest.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mediamagictest.R;
import com.example.mediamagictest.adapter.ImageAdpater;
import com.example.mediamagictest.databinding.ActivityMainBinding;
import com.example.mediamagictest.model.ImageData;
import com.example.mediamagictest.model.ImageViewModel;
import com.example.mediamagictest.util.CommonMethods;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
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
        init();
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

    private void getImageData() {
        imageViewModel.getImageList(MainActivity.this).observe(this, new Observer<ArrayList<ImageData>>() {
            @Override
            public void onChanged(ArrayList<ImageData> imageData) {
                list.addAll(imageData);
                adapter.notifyDataSetChanged();
            }
        });
    }
}


