package aaa.myapplication2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridImageAdapter extends BaseAdapter {
    Context context;
    String[] itemsUrls;
    String[] itemsUrlsFull;
    ArrayList list = new ArrayList();
    public GridImageAdapter(Context contex, String[] itemsUrls,String[] itemsUrlsFull, ArrayList list) {
        context = contex;
        this.itemsUrls = itemsUrls;
        this.itemsUrlsFull = itemsUrlsFull;
        this.list = list;
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
//            holder.id = holder.radiobutton.getId();
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
                RadioButton cb = (RadioButton) v;

                if (list.contains(position)){
                    cb.setChecked(false);
                    int idList = list.indexOf(position);
                    list.remove(idList);
                } else {
                    list.add(position);
                    cb.setChecked(true);
                    Log.i("LINK","+ "+list.size());
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
