package com.example.mediamagictest.model;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import static com.example.mediamagictest.util.Constant.imageListUrl;

public class ImageViewModel extends ViewModel {
    MutableLiveData<ArrayList<ImageData>> imageList;

    public LiveData<ArrayList<ImageData>> getImageList(Context context) {
        imageList = new MutableLiveData<>();
        getImageListApi(context);
        return imageList;
    }

    private void getImageListApi(Context context) {
        HttpRequest asyncTask = new HttpRequest(context);
        asyncTask.execute();
    }

    class HttpRequest extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;
        ArrayList<ImageData> list = new ArrayList<>();
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
                // setting the Request Method Type
                httpURLConnection.setRequestMethod("GET");

                Log.d("TAG", "HttpRequestTask doInBackground : " + httpURLConnection.getResponseCode());
                Log.d("TAG", "HttpRequestTask doInBackground : " + httpURLConnection.getResponseMessage());

                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    InputStreamReader streamReader = new
                            InputStreamReader(httpURLConnection.getInputStream());
                    //Create a new buffered reader and String Builder
                    BufferedReader reader = new BufferedReader(streamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    //Check if the line we are reading is not null
                    while ((inputLine = reader.readLine()) != null) {
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
                    imageList.setValue(list);
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


