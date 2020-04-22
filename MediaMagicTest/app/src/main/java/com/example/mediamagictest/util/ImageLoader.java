package com.example.mediamagictest.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.mediamagictest.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageLoader extends AsyncTask<String, Void, Bitmap> {
    private Context context;
    private ImageView imageView;
    private ProgressBar progressBar;

    public ImageLoader(Context context, ImageView imageView, ProgressBar progressBar) {
        this.context = context;
        this.imageView = imageView;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Bitmap doInBackground(String... url) {
        Bitmap map = getBitMapFromUrl(url[0]);
        return map;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        progressBar.setVisibility(View.GONE);
        if (result != null)
            imageView.setImageBitmap(result);
        else
            imageView.setImageResource(R.drawable.logo);
    }


    private Bitmap getBitMapFromUrl(String imageuri) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(imageuri);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream is = connection.getInputStream();
            Bitmap mybitmap = BitmapFactory.decodeStream(is);
            return mybitmap;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
