package aaa.myapplication2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import static android.support.v4.app.ActivityCompat.startActivity;

public class GridImageAdapter extends BaseAdapter {
    Context context;
    String[] itemsUrls;
    String[] itemsUrlsFull;
    public GridImageAdapter(Context contex, String[] itemsUrls,String[] itemsUrlsFull) {
        context = contex;
        this.itemsUrls = itemsUrls;
        this.itemsUrlsFull = itemsUrlsFull;
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
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_grid_adapter, null);
            holder.imageview = (ImageView) convertView.findViewById(R.id.imageItem);
            holder.radiobutton = (RadioButton) convertView.findViewById(R.id.itemRadioButton);

            convertView.setTag(holder);
            convertView.setPadding(2,2,2,2);

            Picasso.with(context)
                    .load(itemsUrls[position])
                    .resize(180,180)
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
//                CheckBox cb = (CheckBox) v;
//                int id = cb.getId();

//                if (itemsUrls[id]){
//                    cb.setChecked(false);
//                    itemsUrls[id] = false;
//                } else {
//                    cb.setChecked(true);
//                    itemsUrls[id] = true;
//                }
            }
        });

        return convertView;
    }
    class ViewHolder {
        ImageView imageview;
        RadioButton radiobutton;
    }
}
