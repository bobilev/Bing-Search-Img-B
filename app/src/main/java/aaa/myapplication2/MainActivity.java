package aaa.myapplication2;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener {
    ImageButton btn,btnClear,btnMail;
    EditText editText;
    BingAsyncTask bingAsyncTask;
    LoadImageAsyncTask loadImageAsyncTask;
    String[] urlsImages ;
    String[] urlsImagesFull ;
    ArrayList<Integer> list = new ArrayList();
    GridView gridView;
    String query;
    int orentir;

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
        editText.setOnKeyListener(this);

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        btnClear.setVisibility(View.GONE);
        btnMail.setVisibility(View.GONE);
        //Запуск поиска
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = String.valueOf(editText.getText());
                searchBing(query);
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {//clear
            @Override
            public void onClick(View v) {
                btnClear.setVisibility(View.GONE);
                gridView.setAdapter(null);
                editText.setText(null);
            }
        });
        btnMail.setOnClickListener(new View.OnClickListener() {//отправка на почту
            @Override
            public void onClick(View v) {
                String[] urlsImagesFull = new String[list.size()];
                String[] urlsImagesMin = new String[list.size()];
                File root = getExternalFilesDir(null);
                int i = 0;
                for (int choois : list) {
                    urlsImagesFull[i] = bingAsyncTask.getUrlsImagesFull()[choois];
                    urlsImagesMin[i] = bingAsyncTask.getUrlsImages()[choois];
                    i++;
                }
                Log.i("LINK", "urlsImages" + urlsImagesFull.length);
                list.clear();
                loadImageAsyncTask = new LoadImageAsyncTask(root, MainActivity.this, urlsImagesFull, urlsImagesMin);
                loadImageAsyncTask.execute();
            }
        });
    }
    public void searchBing(String poisk){
        btnClear.setVisibility(View.VISIBLE);
        btnMail.setVisibility(View.GONE);
        list.clear();

//        String query = String.valueOf(editText.getText());
        Log.i("LINK", poisk);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(btn.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);//убирать клавиатуру
        YoYo.with(Techniques.ZoomOut).duration(700).playOn(gridView);

        bingAsyncTask = new BingAsyncTask(poisk, urlsImages, urlsImagesFull, gridView, MainActivity.this,list,btnMail,btnClear);
        bingAsyncTask.execute(poisk);
    }

    public int getScreenOrientation(){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            return 0;//Портретная ориентация
        } else {
            return 1;//Альбомная ориентация
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("LINK", "было  " + orentir);
        Log.i("LINK", "Стало  " + getScreenOrientation());
        if(orentir != getScreenOrientation()){
            orentir = getScreenOrientation();
            searchBing(query);
        }
        if(query == null){
            Log.i("LINK", "Пусто "+query);

        } else {
            Log.i("LINK", "чето есть");
//            query = String.valueOf(editText.getText());
//            searchBing(query);
        }
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

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            Log.i("LINK","+++");
            return false;
        }
        return false;
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        onDestroy();
//        String query = String.valueOf(editText.getText());
//        searchBing(query);
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("LINK", "+++");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        query = editText.getText().toString();
        outState.putString("string", query);
        outState.putInt("int", orentir);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        query = savedInstanceState.getString("string");
        orentir = savedInstanceState.getInt("int");
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("LINK", "+++ R" + query);
    }

}
