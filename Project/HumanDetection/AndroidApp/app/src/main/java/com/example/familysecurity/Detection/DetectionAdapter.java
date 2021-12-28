package com.example.familysecurity.Detection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familysecurity.R;

import java.util.List;

public class DetectionAdapter extends BaseAdapter {
    private final List<DetectionData> listData;
    private Context context;
    private LayoutInflater layoutInflater;

    public DetectionAdapter(Context context, List<DetectionData> listData) {
        this.listData = listData;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.listData.size();
    }

    @Override
    public Object getItem(int i) {
        return listData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.list_detection_item, container, false);
        }

        DetectionData data = this.listData.get(position);
        Bitmap bmp= BitmapFactory.decodeByteArray(data.src,0,data.src.length);
        ImageView image= convertView.findViewById(R.id.img_detection);
        image.setImageBitmap(bmp);
        ((TextView) convertView.findViewById(R.id.text_img_name))
                .setText(data.name);
        ((TextView) convertView.findViewById(R.id.text_date))
                .setText(data.date);
        ((TextView) convertView.findViewById(R.id.text_time))
                .setText(data.time);
        return convertView;
    }
}
