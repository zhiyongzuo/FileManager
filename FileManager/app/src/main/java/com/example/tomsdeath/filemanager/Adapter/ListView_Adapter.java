package com.example.tomsdeath.filemanager.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.tomsdeath.filemanager.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tomsdeath on 2016/1/23.
 */
public class ListView_Adapter extends BaseAdapter {
    private List<String> mFileNameList;
    private List<String> mFilePathList;
    private Context mcontext;
    private Bitmap mBackRoot;
    private Bitmap mBackToUp;
    private Bitmap mFolder;
    private Bitmap mVideo;
    private Bitmap mAPK;
    private Bitmap mPicture;
    private Bitmap mTxt;
    private Bitmap mWeb;
    private Bitmap mOther;
    private Bitmap mRar;
    private Bitmap mAudio;

    public ListView_Adapter(Context context, List<String> mFileNameList, List<String> mFilePathList) {
        this.mcontext = context;
        this.mFileNameList = mFileNameList;
        this.mFilePathList = mFilePathList;
        mBackRoot = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.back_to_root);
        mBackToUp = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.back_to_up);
        mFolder = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.folder);
        mVideo = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.video);
        mRar = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.zip_icon);
        mAudio = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.audio);
        mTxt = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.txt);
        mWeb = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.web_browser);
        mOther = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.others);
        mAPK = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.apk);
        mPicture = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.image);
    }

    @Override
    public int getCount() {
        return mFilePathList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFileNameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = ((LayoutInflater)mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.baseadapter_item,
                    null);
            viewHolder.mIV = (ImageView)convertView.findViewById(R.id.base_imageView);
            viewHolder.mTV = (TextView)convertView.findViewById(R.id.base_textView);
            convertView.setTag(viewHolder);
        } else {
            //convertView.getTag();
            viewHolder = (ViewHolder)convertView.getTag();
        }

        File mFile = new File(mFilePathList.get(position)); //java.lang.NullPointerException:
        // Attempt to invoke virtual method 'char[
        // ] java.lang.String.toCharArray()' on a null object reference
        if(mFileNameList.get(position).equals("BackToRoot")) {
            viewHolder.mIV.setImageBitmap(mBackRoot);
            viewHolder.mTV.setText("back to root");
        } else if(mFileNameList.get(position).equals("BackToUp")) {
            viewHolder.mIV.setImageBitmap(mBackToUp);
            viewHolder.mTV.setText("back to up");
        } else if(mFileNameList.get(position).equals("BackToSearchBefore")) {
            viewHolder.mIV.setImageBitmap(mBackRoot);
            viewHolder.mTV.setText("back to search before");
        } else {
            String mName = mFile.getName();
            Log.d("test", "c");
            viewHolder.mTV.setText(mName);
            Log.d("test", "d1");
            //viewHolder.mTV.setText(mName);//what's wrong
            if (mFile.isDirectory()) {
                Log.d("test", "d1.1");
                viewHolder.mIV.setImageBitmap(mFolder);
                Log.d("test", "d1.2");
            } else {
                Log.d("test", "d2");
                String fileEnds = mName.substring(mName.lastIndexOf(".")+1, mName.length()).toLowerCase();
                if(fileEnds.equals("m4a") || fileEnds.equals("mp3") || fileEnds.equals("mid") || fileEnds.equals("xmf")
                        || fileEnds.equals("ogg") || fileEnds.equals("wav")) {
                    viewHolder.mIV.setImageBitmap(mVideo);
                } else if(fileEnds.equals("png") || fileEnds.equals("jpg") || fileEnds.equals("gif") || fileEnds.equals("jpeg") ||fileEnds.equals("bmp")) {
                    viewHolder.mIV.setImageBitmap(mPicture);
                } else if(fileEnds.equals("apk")) {
                    viewHolder.mIV.setImageBitmap(mAPK);
                } else if (fileEnds.equals("html") || fileEnds.equals("htm") || fileEnds.equals("mht")) {
                    viewHolder.mIV.setImageBitmap(mWeb);
                } else if (fileEnds.equals("zip") || fileEnds.equals("rar")) {
                    viewHolder.mIV.setImageBitmap(mRar);
                } else if (fileEnds.equals("txt")) {
                    viewHolder.mIV.setImageBitmap(mTxt);
                } else if (fileEnds.equals("3gp") || fileEnds.equals("mp4")) {
                    viewHolder.mIV.setImageBitmap(mAudio);
                } else {
                    viewHolder.mIV.setImageBitmap(mOther);
                    Log.d("test", "d23");
                }
            }
        }
        return convertView;
    }

    class ViewHolder {
        TextView mTV;
        ImageView mIV;
    }
}
