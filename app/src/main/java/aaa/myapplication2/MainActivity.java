package aaa.myapplication2;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BingAsyncTask2 bingAsyncTask = new BingAsyncTask2();
        bingAsyncTask.execute();
    }
    public class BingAsyncTask2 extends AsyncTask {

        @Override
        protected Void doInBackground(Object... params) {
            String API_KEY = "KOevhSWHRyykQWgIYax66BAXAjQZfWj4JY3K36xZoKA";

            String link_test = "http://ip.jsontest.com/";
            String result = "";


//            String bingUrl = "https://api.datamarket.azure.com/Bing/SearchWeb/v1/Web?Query=%27" + searchStr + "%27" + numOfResultsStr + "&$format=json";
            String bingUrl = "https://api.datamarket.azure.com/Bing/Search/v1/Image?Query=%27xbox%27" +
                    "&Market=%27en-US%27&Adult=%27Moderate%27" +
//                    "&ImageFilters=%27Size%3ASmall%27&" +
//                    "&ImageFilters=%27Size%3AMedium%27&" +
                    "&ImageFilters=%27Size%3Alarge%27&" +
                    "$format=json&" +
                    "$top=50";
            String auth = API_KEY + ":" + API_KEY;
            String encodedAuth = Base64.encodeToString(auth.getBytes(), Base64.NO_WRAP);
            String accountKeyEnc = new String(encodedAuth);

            URL url = null;
            URLConnection urlConnection = null;

            InputStream response = null;
            try {
                url = new URL(bingUrl);
                urlConnection = url.openConnection();
                urlConnection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
                response = urlConnection.getInputStream();
                String res = readStream(response);
                Log.i("LINK", res);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            Gson gson = (new GsonBuilder()).create();
//            BingSearchResults mBingSearchResults; = gson.fromJson(res, BingSearchResults.class);
            return null;
        }
        private String readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuilder sb = new StringBuilder();
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return sb.toString();
        }
    }
}
