package aaa.myapplication2;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;


public class LoadImageAsyncTask extends AsyncTask<Void, Void, ArrayList<Uri>> {
    ArrayList<Uri> list = new ArrayList<>();
    File root;
    Context context;
    ProgressDialog progressDialog;
    String[] urlsImagesFull;
    String[] urlsImages;


    public LoadImageAsyncTask(File root, Context context,String[] urlsImagesFull,String[] urlsImages){
        this.root = root;
        this.context = context;
        this.urlsImagesFull = urlsImagesFull;
        this.urlsImages = urlsImages;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context, "","");
    }

    @Override
    protected void onPostExecute(ArrayList<Uri> uris) {
        super.onPostExecute(uris);
        progressDialog.dismiss();
        Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);//нужно для отправки нескольких фото (ACTION_SEND_MULTIPLE)
        i.setType("image/*");
        i.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"Dimitri011s@yandex.ru"});
//        i.putExtra(Intent.EXTRA_STREAM, list.get(0));
        i.putParcelableArrayListExtra(Intent.EXTRA_STREAM, list);//нужно для отправки нескольких фото
        Log.i("LINK", " ;) 6 - " + list);
        context.startActivity(i);
    }

    @Override
    protected ArrayList<Uri> doInBackground(Void... params) {
        Bitmap bmap;
        BitmapFactory.Options bmOptions;
        bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;
        int name = 0;
        for(String link:urlsImagesFull){
            bmap = LoadImage(link,urlsImages[name], bmOptions);

            File file = new File(root+"/test"+name+".jpg");
            OutputStream out = null;

            Log.i("LINK", " ;) 2");
            try {
                out = new FileOutputStream(file);
                bmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                Log.i("LINK", " ;) 3");
                out.flush();
                out.close();
                Log.i("LINK", " ;) 4");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("LINK", " ;) 4.1" + file);

            list.add(Uri.fromFile(file));
            Log.i("LINK", " ;) 5"+file);
            name++;
        }

        return list;
    }

    public Bitmap LoadImage(String URLfull, String URLmin, BitmapFactory.Options options) {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URLfull,URLmin);
            bitmap = BitmapFactory.decodeStream(in, null, options);
            in.close();
        } catch (IOException e1) {
            Log.i("LINK"," Не скачал");
        }
        return bitmap;
    }
    private InputStream OpenHttpConnection(String URLfull, String URLmin){
        InputStream inputStream = null;
        URL url;
        URL urls;
        URL urlmin;
        URLConnection conn;
        HttpURLConnection httpConn;

        try{
            url = new URL("http://"+URLfull);
            urls = new URL("https://"+URLfull);

            conn = url.openConnection();
            httpConn = (HttpURLConnection)conn;
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {//http
                inputStream = httpConn.getInputStream();
            } else {
                conn = urls.openConnection();
                httpConn = (HttpURLConnection)conn;
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                inputStream = httpConn.getInputStream();
//                if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK){//https
//                    inputStream = httpConn.getInputStream();
//                } else {
//                    conn = urls.openConnection();
//                    httpConn = (HttpURLConnection)conn;
//                    httpConn.setRequestMethod("GET");
//                    httpConn.connect();
//                    inputStream = httpConn.getInputStream();//запаска
//                }
                Log.i("LINK"," Не подключился -3" +urls);
            }
        } catch (IOException ex) {
            try{
                urlmin = new URL("http://"+URLmin);
                conn = urlmin.openConnection();
                httpConn = (HttpURLConnection) conn;
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                inputStream = httpConn.getInputStream();//запаска
                Log.i("LINK", " Не скачал -2 " + urlmin);
            }catch (IOException ex1) {}
        }
        return inputStream;
    }

}