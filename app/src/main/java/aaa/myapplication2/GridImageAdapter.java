package aaa.myapplication2;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class GridImageAdapter extends BaseAdapter {
    Context context;
    String[] itemsUrls;

    public GridImageAdapter(Context contex, String[] itemsUrls) {
        super();
        this.context = contex;
        this.itemsUrls = itemsUrls;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView img;
        if(convertView == null) {//esli pustai
            img = new ImageView(context);
            convertView = img;
            img.setPadding(2,2,2,2);
        } else {
            img = (ImageView) convertView;
        }
        Picasso.with(context)
                .load(itemsUrls[position])
                .resize(180,180)
                .into(img);

        return convertView;
    }
}
