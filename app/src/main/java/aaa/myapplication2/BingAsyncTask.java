package aaa.myapplication2;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by aAa on 03.11.2015.
 */
public class BingAsyncTask extends AsyncTask {
    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] params) {
        String API_KEY = "KOevhSWHRyykQWgIYax66BAXAjQZfWj4JY3K36xZoKA";
        String APILink = "https://api.datamarket.azure.com/Bing/Search/v1/Image?Query=%27xbox%27" +
                "&Market=%27en-US%27&Adult=%27Moderate%27" +
//                    "&ImageFilters=%27Size%3ASmall%27&" +
//                    "&ImageFilters=%27Size%3AMedium%27&" +
                "&ImageFilters=%27Size%3Alarge%27&" +
                "$format=json&" +
                "$top=50";
        String result = "";

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(APILink);

        String auth = API_KEY + ":" + API_KEY;
        String encodedAuth = Base64.encodeToString(auth.getBytes(), Base64.NO_WRAP);
        Log.i("LINK", APILink);
//        Log.i("LINK",link[0]);
        httpget.addHeader("Authorization", "Basic " + encodedAuth);

        //Execute and get the response.
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpget);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream inputStream = null;
            try {
                inputStream = entity.getContent();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            int i=0;
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                    i++;
                    Log.i("WTF", i +" - "+ line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
