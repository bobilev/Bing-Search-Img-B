package aaa.myapplication2;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class FullImageActivity extends AppCompatActivity {

    ImageView imageViewFull;
    ProverkaHttpAsyncTask proverkaHttpAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        imageViewFull = (ImageView) this.findViewById(R.id.imageViewFull);

        Intent intent = getIntent();

        String position = intent.getExtras().getString("url");
        proverkaHttpAsyncTask = new ProverkaHttpAsyncTask();
        proverkaHttpAsyncTask.execute(position);
    }

    public String proerka(String link) {
        URL url = null;
        String result = null;
        try {
            url = new URL("http://" + link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URLConnection conn = null;
        try {
            conn = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpURLConnection httpConn = (HttpURLConnection) conn;

        try {
            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                result = "http://" + link;
                Log.i("LINK","proverka = "+result);
            } else {
                result = "https://" + link;
                Log.i("LINK","proverka = "+result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    class ProverkaHttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Picasso.with(getApplicationContext())
                    .load(s)
                    .fit()
//                .resize(200, 250)
                    .centerCrop()
                    .into(imageViewFull);
        }

        @Override
        protected String doInBackground(String... params) {
            String link = proerka(params[0]);
            return link;
        }
    }
}