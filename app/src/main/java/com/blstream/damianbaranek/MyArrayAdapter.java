package com.blstream.damianbaranek;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyArrayAdapter extends ArrayAdapter<ListItem>{
    Context context;

    public MyArrayAdapter(Context context, int resourceId,
                                 List<ListItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }
    private class ViewHolder {
        ImageView imageView;
        TextView textTitle;
        TextView textDesc;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        ListItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.objects_list_item, null);
            holder = new ViewHolder();
            holder.textDesc = (TextView) convertView.findViewById(R.id.desc);
            holder.textTitle = (TextView) convertView.findViewById(R.id.title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.noimage);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.textDesc.setText(rowItem.desc);
        holder.textTitle.setText(rowItem.title);
        if(rowItem.imageBitmap != null)
            holder.imageView.setImageBitmap(rowItem.imageBitmap);

        return convertView;
    }
}
