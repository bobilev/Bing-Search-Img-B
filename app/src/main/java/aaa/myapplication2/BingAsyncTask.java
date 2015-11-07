package aaa.myapplication2;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by aAa on 03.11.2015.
 */
public class BingAsyncTask extends AsyncTask<String, Void, String[]> {
    String query;
    String[] urlsImages;
    String[] urlsImagesFull;
    GridView gridView;
    GridImageAdapter gridImageAdapter;
    Context contex;
    ArrayList<Integer> list;
    ImageButton btnMail;
    ImageButton btnClear;
    ProgressDialog progressDialog;

    public BingAsyncTask(String query, String[] urlsImages, String[] urlsImagesFull, GridView gridView,Context context, ArrayList list,ImageButton btnMail,ImageButton btnClear){
        this.query = query;
        this.urlsImages = urlsImages;
        this.urlsImagesFull = urlsImagesFull;
        this.gridView = gridView;
        this.contex = context;
        this.list = list;
        this.btnMail = btnMail;
        this.btnClear = btnClear;
    }

    public String[] getUrlsImages() {
        return urlsImages;
    }

    public String[] getUrlsImagesFull() {
        return urlsImagesFull;
    }

    @Override
    protected void onPostExecute(String[] urls) {
        super.onPostExecute(urls);
        gridView.setAdapter(null);
        if(urls.length == 0){
            Toast toast = Toast.makeText(contex, "Bing ничего не нашел", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        gridImageAdapter = new GridImageAdapter(contex, urls, urlsImagesFull, list, btnMail, btnClear);
        gridView.setAdapter(gridImageAdapter);
        progressDialog.dismiss();
        YoYo.with(Techniques.FadeInUp).duration(1000).playOn(gridView);
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(contex, "","");
    }

    @Override
    protected String[] doInBackground(String... params) {
        String API_KEY = "KOevhSWHRyykQWgIYax66BAXAjQZfWj4JY3K36xZoKA";
        String result = "";
        String bingUrl = null;

        try {
            bingUrl = "https://api.datamarket.azure.com/Bing/Search/v1/Image?Query=%27"+ URLEncoder.encode(params[0], "utf8")+"%27" +
                    "&Market=%27ru-RU%27&Adult=%27Moderate%27" +
    //                    "&ImageFilters=%27Size%3ASmall%27&" +
    //                    "&ImageFilters=%27Size%3AMedium%27&" +
                    "&ImageFilters=%27Size%3Alarge%27&" +
                    "$format=json&" +
                    "$top=50";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String auth = API_KEY + ":" + API_KEY;
        String encodedAuth = Base64.encodeToString(auth.getBytes(), Base64.NO_WRAP);
        String accountKeyEnc = new String(encodedAuth);


        URL url = null;
        URLConnection urlConnection = null;

        InputStream response = null;
        try {
            url = new URL(bingUrl);
            Log.i("LINK","url valid = "+url);
            urlConnection = url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
            response = urlConnection.getInputStream();
            result = readStream(response);//получаем ответ в виде строки
            Log.i("LINK", result);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Json
        JSONObject jsonObject = null;
        if (JSONValue.isValidJson(result)) {
            jsonObject = (JSONObject) JSONValue.parse(result);//превращаем строку в json объект
        }

        JSONObject jsonObjectFull = (JSONObject) jsonObject.get("d");

        int size = ((JSONArray) jsonObjectFull.get("results")).size();
        Log.i("LINK",size+"");
        urlsImages = new String[size];
        urlsImagesFull = new String[size];
        for(int i=0; i<size; i++){
            jsonObject = (JSONObject) ((JSONArray) jsonObjectFull.get("results")).get(i);
            String urlFull = (String) jsonObject.get("MediaUrl");
            urlsImagesFull[i] = regEx(urlFull);
            jsonObject = (JSONObject) jsonObject.get("Thumbnail");
            String urlImg = (String) jsonObject.get("MediaUrl");
            Log.i("LINK","url = "+urlImg);
            urlsImages[i] = regEx(urlImg);

        }
        Log.i("LINK",urlsImages.length+" 2");
        return urlsImages;
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
    public String regEx(String link) {
        Pattern pattern = Pattern.compile("http://");
        Matcher matcher = pattern.matcher(link);
        String result = matcher.replaceFirst("");
        return  result;
    }
    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            Log.i("LINK","da");
            return true;
        }
        Log.i("LINK","Non E");
        return false;
    }
}
