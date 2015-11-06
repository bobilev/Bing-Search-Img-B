package aaa.myapplication2;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;

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

public class MainActivity extends AppCompatActivity {
    ImageButton btn,btnClear,btnMail;
    EditText editText;
    GridView gridView ;
    BingAsyncTask bingAsyncTask;
    String[] urlsImages ;
    String[] urlsImagesFull ;
    ArrayList<Integer> list = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setCustomView(R.layout.actionbar);

        gridView = (GridView) this.findViewById(R.id.gridView);
        Log.i("LINK", gridView.getId()+"");
        btn = (ImageButton) actionBar.getCustomView().findViewById(R.id.button);
        btnClear = (ImageButton) actionBar.getCustomView().findViewById(R.id.buttonClear);
        btnMail = (ImageButton) actionBar.getCustomView().findViewById(R.id.buttonMail);
        editText = (EditText) actionBar.getCustomView().findViewById(R.id.editText);

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        btnClear.setVisibility(View.GONE);
        btnMail.setVisibility(View.GONE);
        //Запуск поиска
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClear.setVisibility(View.VISIBLE);
                btnMail.setVisibility(View.GONE);

                String query = String.valueOf(editText.getText());
                Log.i("LINK", query);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(btn.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);//убирать клавиатуру
                YoYo.with(Techniques.ZoomOut).duration(700).playOn(gridView);
                bingAsyncTask = new BingAsyncTask(query, urlsImages, urlsImagesFull, gridView, MainActivity.this,list,btnMail,btnClear);
                bingAsyncTask.execute(query);
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {//clear
            @Override
            public void onClick(View v) {
                btnClear.setVisibility(View.GONE);
                gridView.setAdapter(null);
            }
        });
        btnMail.setOnClickListener(new View.OnClickListener() {//отправка на почту
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                int choois = list.get(0);
                String urlImage = bingAsyncTask.getUrlsImagesFull()[choois];
                Bitmap bmap;
                BitmapFactory.Options bmOptions;
                bmOptions = new BitmapFactory.Options();
                bmOptions.inSampleSize = 1;
                bmap = LoadImage(urlImage, bmOptions);

                Log.i("LINK", " ;) 1");
                File root=getExternalFilesDir(null);
                File file = new File(root+"/test.jpg");
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
                File imgFile = new File(root+"/test.jpg");

                i.setType("image/*");
                i.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"Dimitri011s@yandex.ru"});
                i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imgFile));

                Log.i("LINK", " ;)" + file);
                startActivity(i);
                //////////////

            }
        });
    }
    public Bitmap LoadImage(String URL, BitmapFactory.Options options)
    {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in, null, options);
            in.close();
        } catch (IOException e1) {
        }
        return bitmap;
    }
    private InputStream OpenHttpConnection(String strURL) throws IOException{
        InputStream inputStream = null;
        URL url = new URL(strURL);
        URLConnection conn = url.openConnection();

        try{
            HttpURLConnection httpConn = (HttpURLConnection)conn;
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpConn.getInputStream();
            }
        }
        catch (Exception ex)
        {
        }
        return inputStream;
    }
    //creat menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public static void asdfas() {}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("LINK"," ;)");
    }
}
