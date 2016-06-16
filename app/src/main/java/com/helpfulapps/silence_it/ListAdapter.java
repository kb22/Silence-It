package com.helpfulapps.silence_it;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] titles;
    private final String[] desc;
    private final Integer[] images;

    public ListAdapter(Activity context, String[] titles,String[] desc, Integer[] images) {
        super(context, R.layout.listview_settings, titles);
        this.context = context;
        this.titles = titles;
        this.images = images;
        this.desc = desc;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_settings, null,true);

        TextView title = (TextView) rowView.findViewById(R.id.title);
        ImageView image = (ImageView) rowView.findViewById(R.id.icon);
        TextView description = (TextView) rowView.findViewById(R.id.description);

        title.setText(titles[position]);
        image.setImageResource(images[position]);
        description.setText(desc[position]);
        return rowView;

    };
}
