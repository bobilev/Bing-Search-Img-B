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

        String positionFull = intent.getExtras().getString("urlFull");
        String positionMin = intent.getExtras().getString("urlmin");
        proverkaHttpAsyncTask = new ProverkaHttpAsyncTask(positionFull,positionMin);
        proverkaHttpAsyncTask.execute();
    }

    public String proverka(String linkFull,String linkMin) {
        URL urlhttp = null;
        URL urlhttps = null;
        String result = null;
        try {
            urlhttp = new URL("http://" + linkFull);
            urlhttps = new URL("https://" + linkFull);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URLConnection conn = null;
        URLConnection conns = null;
        try {
            conn = urlhttp.openConnection();
            conns = urlhttps.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpURLConnection httpConn = (HttpURLConnection) conn;
        HttpURLConnection httpConns = (HttpURLConnection) conns;

        try {
            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                result = "http://" + linkFull;
                Log.i("LINK","proverka 1= "+httpConn.getResponseCode() +"/"+result);
            } else if(httpConns.getResponseCode() == HttpURLConnection.HTTP_OK){
                result = "https://" + linkFull;
                Log.i("LINK","proverka 2 "+httpConn.getResponseCode() +"/"+result);
            } else {
                result = "http://"+linkMin;
                Log.i("LINK","proverka 3= "+httpConn.getResponseCode() +"/"+result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    class ProverkaHttpAsyncTask extends AsyncTask<Void, Void, String> {
        String positionFull;
        String positionMin;

        public ProverkaHttpAsyncTask(String positionFull,String positionMin){
            this.positionFull = positionFull;
            this.positionMin = positionMin;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Picasso.with(getApplicationContext())
                    .load(s)
                    .fit()
//                    .resize(200, 250)
//                    .centerCrop()
                    .centerInside()
                    .into(imageViewFull);
        }

        @Override
        protected String doInBackground(Void... params) {
            String link = proverka(positionFull, positionMin);
            return link;
        }
    }
}