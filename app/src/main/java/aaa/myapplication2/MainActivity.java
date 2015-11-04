package aaa.myapplication2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;


public class MainActivity extends AppCompatActivity {
    ImageButton btn;
    EditText editText;
    GridView gridView;
    BingAsyncTask bingAsyncTask;
    String[] urlsImages;
    String[] urlsImagesFull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setCustomView(R.layout.actionbar);

        gridView = (GridView) this.findViewById(R.id.gridView);
        btn = (ImageButton) actionBar.getCustomView().findViewById(R.id.button);
        editText = (EditText) actionBar.getCustomView().findViewById(R.id.editText);

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        //обработка нажатия на item в grid и открытие нового активити
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), FullImageActivity.class);
                urlsImagesFull = bingAsyncTask.getUrlsImagesFull();
                intent.putExtra("url", urlsImagesFull[position]);
                Log.i("LINK", urlsImagesFull[position] + "");
                startActivity(intent);

            }
        });
        //Запуск поиска
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = String.valueOf(editText.getText());
                Log.i("LINK",query);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(btn.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);//убирать клавиатуру
                YoYo.with(Techniques.ZoomOut).duration(700).playOn(gridView);
                bingAsyncTask = new BingAsyncTask(query, urlsImages, urlsImagesFull, gridView,MainActivity.this);
                bingAsyncTask.execute(query);
            }
        });

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


}
