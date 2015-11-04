package aaa.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class FullImageActivity extends AppCompatActivity {

    ImageView imageViewFull;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        imageViewFull = (ImageView) this.findViewById(R.id.imageViewFull);

        Intent intent = getIntent();

        String position = intent.getExtras().getString("url");
        Log.i("LINK", "full - " + position);
        Picasso.with(this)
                .load(position)
                .fit()
//                .resize(200, 250)
                .into(imageViewFull);

    }
}