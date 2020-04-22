package com.example.mediamagictest.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.mediamagictest.R;

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


    public Bitmap decodeFile(String filePath, int width, int hight) {
        try {

            File f = new File(filePath);

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            final int REQUIRED_WIDTH = width;
            final int REQUIRED_HIGHT = hight;
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_WIDTH
                    && o.outHeight / scale / 2 >= REQUIRED_HIGHT)
                scale *= 2;

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap getBitMapFromUrl(String imageuri) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(imageuri);
            //  Log.d("bucky","bitmap" + imageuri);
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
