package com.example.mediamagictest.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mediamagictest.R;
import com.example.mediamagictest.adapter.ImageAdpater;
import com.example.mediamagictest.databinding.ActivityMainBinding;
import com.example.mediamagictest.model.ImageData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.mediamagictest.util.Constant.imageListUrl;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    private ImageAdpater adapter;
    private ArrayList<ImageData> list;
    private RecyclerView recyclerView;

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
        HttpRequest asyncTask = new HttpRequest(MainActivity.this);
        asyncTask.execute();
    }

    public class HttpRequest extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;
        private Context context;

        HttpRequest(Context context) {
            this.context = context;

        }

        @Override
        protected String doInBackground(String... strings) {

            URL url = null;
            HttpURLConnection httpURLConnection = null;
            String response = "";
            String inputLine;
            try {
                url = new URL(imageListUrl);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                // setting the  Request Method Type
                httpURLConnection.setRequestMethod("GET");

                Log.d("TAG", "MyHttpRequestTask doInBackground : " + httpURLConnection.getResponseCode());
                Log.d("TAG", "MyHttpRequestTask doInBackground : " + httpURLConnection.getResponseMessage());

                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    InputStreamReader streamReader = new
                            InputStreamReader(httpURLConnection.getInputStream());
                    //Create a new buffered reader and String Builder
                    BufferedReader reader = new BufferedReader(streamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    //Check if the line we are reading is not null
                    while((inputLine = reader.readLine()) != null){
                        stringBuilder.append(inputLine);
                    }
                    //Close our InputStream and Buffered reader
                    reader.close();
                    streamReader.close();
                    response = stringBuilder.toString();
                }
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (result != null && !result.equals("")) {
                try {
                    // JSON Parsing of data
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        ImageData model = new ImageData();
                        model.setId(object.getInt("id"));
                        model.setAuthorName(object.getString("author"));
                        list.add(model);
                    }
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }


        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

    }

}


