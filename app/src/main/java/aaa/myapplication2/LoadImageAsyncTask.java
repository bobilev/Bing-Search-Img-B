package aaa.myapplication2;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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


public class LoadImageAsyncTask extends AsyncTask<String, Void, ArrayList<Uri>> {
    ArrayList<Uri> list = new ArrayList<>();
    File root;
    Context context;
    ProgressDialog progressDialog;

    public LoadImageAsyncTask(File root, Context context){
        this.root = root;
        this.context = context;
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
    protected ArrayList<Uri> doInBackground(String... params) {
        Bitmap bmap;
        BitmapFactory.Options bmOptions;
        bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;
        int name = 0;
        for(String link:params){
            bmap = LoadImage(link, bmOptions);

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

    public Bitmap LoadImage(String URL, BitmapFactory.Options options) {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in, null, options);
            in.close();
        } catch (IOException e1) {
            Log.i("LINK"," Не скачал");
        }
        return bitmap;
    }
    private InputStream OpenHttpConnection(String strURL) throws IOException{
        InputStream inputStream = null;
        URL url = new URL("http://"+strURL);
        URL urls = new URL("https://"+strURL);
        URLConnection conn = url.openConnection();

        try{
            HttpURLConnection httpConn = (HttpURLConnection)conn;
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpConn.getInputStream();
            } else {
                conn = urls.openConnection();
                httpConn = (HttpURLConnection)conn;
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                inputStream = httpConn.getInputStream();

                Log.i("LINK"," Не подключился -3" +urls);
            }
        } catch (Exception ex) {
            Log.i("LINK"," Не скачал -2");
        }
        return inputStream;
    }
}