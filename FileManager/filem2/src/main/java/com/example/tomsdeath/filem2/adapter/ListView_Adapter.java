package com.example.tomsdeath.filem2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tomsdeath.filem2.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by tomsdeath on 2016/3/8.
 */
public class ListView_Adapter extends BaseAdapter {
    Context context;
    ArrayList<String> path_list;
    ArrayList<String> name_list;

    public ListView_Adapter(Context context, ArrayList<String> path_list, ArrayList<String> name_list) {
        this.context = context;
        this.name_list = name_list;
        this.path_list = path_list;
    }

    @Override
    public int getCount() {
        return name_list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String path = path_list.get(position);
        String name = name_list.get(position);

        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.list_view_item, null);
        ImageView list_item_imageView = (ImageView) convertView.findViewById(R.id.list_item_image);
        TextView list_item_textView = (TextView) convertView.findViewById(R.id.list_view_text);

        //list_item_imageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
        list_item_textView.setText(name);

        File mFile = new File(path_list.get(position));
        String mName = name.substring(name.lastIndexOf(".") + 1, name.length()).toLowerCase();
        //@TargetApi(21)
        if (mFile.isDirectory()) {
            list_item_imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.folder));
        } else if (mName.equals("avi") || mName.equals("mp4") || mName.equals("3gp")) {
            list_item_imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.video));
        } else if (mName.equals("txt")) {
            list_item_imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.txt));
        } else if (mName.equals("zip") || mName.equals("rar")) {
            list_item_imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.zip_icon));
        } else if (mName.equals("mp3")) {
            list_item_imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.audio));
        } else if (mName.equals("html")) {
            list_item_imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.web_browser));
        } else if (mName.equals("png") || mName.equals("jpg")) {
            list_item_imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.image));
        } else if (mName.equals("apk")) {
            list_item_imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.apk));
        } else {
            list_item_imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.others));
        }
        return convertView;
    }
}
