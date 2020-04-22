package com.example.mediamagictest.model;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.mediamagictest.R;
import com.example.mediamagictest.util.CommonMethods;
import com.example.mediamagictest.util.ImageLoader;

import androidx.databinding.BindingAdapter;

public class ImageData {

    int id;
    String authorName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

}
