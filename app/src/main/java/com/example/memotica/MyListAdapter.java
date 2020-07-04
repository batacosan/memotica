package com.example.memotica;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.memotica.ListItem;
import com.example.memotica.R;

import java.util.ArrayList;

public class MyListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ListItem> data;
    private int resource;

    MyListAdapter(Context context,
                  ArrayList<ListItem> data, int resource) {
        this.context = context;
        this.data = data;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Activity activity = (Activity) context;
        ListItem item = (ListItem) getItem(position);
        if (convertView == null) {
            convertView = activity.getLayoutInflater()
                    .inflate(resource, null);
        }
        ((TextView) convertView.findViewById(R.id.text_title)).setText(item.getTitle());
        ((TextView) convertView.findViewById(R.id.text_content)).setText(item.getContent());
        ((TextView) convertView.findViewById(R.id.text_updated)).setText(item.getUpdated());
        Log.i("debug", "item.getupdated() = " + item.getUpdated());
        return convertView;
    }
}
