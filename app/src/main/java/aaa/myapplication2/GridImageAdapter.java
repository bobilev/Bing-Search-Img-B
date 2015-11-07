package aaa.myapplication2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridImageAdapter extends BaseAdapter {
    Context context;
    String[] itemsUrls;
    String[] itemsUrlsFull;
    ArrayList<Integer> list = new ArrayList();
    ImageButton btnMail;
    ImageButton btnClear;
    public GridImageAdapter(Context contex, String[] itemsUrls,String[] itemsUrlsFull, ArrayList list, ImageButton btnMail,ImageButton btnClear) {
        context = contex;
        this.itemsUrls = itemsUrls;
        this.itemsUrlsFull = itemsUrlsFull;
        this.list = list;
        this.btnMail = btnMail;
        this.btnClear = btnClear;
    }
    @Override
    public int getCount() {
        return itemsUrls.length;
    }

    @Override
    public Object getItem(int position) {
        return itemsUrls[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_grid_adapter, null);
            holder.imageview = (ImageView) convertView.findViewById(R.id.imageItem);
            holder.radiobutton = (RadioButton) convertView.findViewById(R.id.itemRadioButton);

            convertView.setTag(holder);
            convertView.setPadding(2, 2, 2, 2);

            Picasso.with(context)
                    .load("http://"+itemsUrls[position])
                    .resize(180,180)
                    .centerCrop()
                    .into(holder.imageview);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageview.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(context, FullImageActivity.class);
                intent.putExtra("url", itemsUrlsFull[position]);
                Log.i("LINK", itemsUrlsFull[position] + "");
                context.startActivity(intent);
            }
        });

        holder.radiobutton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                RadioButton cb = (RadioButton) v;

                if (list.contains(position)) {
                    cb.setChecked(false);
                    int idList = list.indexOf(position);
                    list.remove(idList);
                    if (list.size() == 0) {
                        btnMail.setVisibility(View.GONE);
                        btnClear.setVisibility(View.VISIBLE);
                    }
                } else {
                    if(list.size()<5){
                        list.add(position);
                        cb.setChecked(true);
                        btnMail.setVisibility(View.VISIBLE);
                        btnClear.setVisibility(View.GONE);
                        Log.i("LINK", "+ " + list.size());
                    } else {
                        cb.setChecked(false);
                        Toast toast = Toast.makeText(context, "Только 5 картинок", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                }
            }
        });

        return convertView;
    }

    class ViewHolder {
        ImageView imageview;
        RadioButton radiobutton;
    }
}
