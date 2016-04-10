package com.example.tomsdeath.filemanager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SearchService extends Service {
    String searchContent;
    ArrayList<String> namelist;
    ArrayList<String> pathlist;

    public SearchService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);
        searchContent = intent.getStringExtra("key");
        namelist = new ArrayList<>();
        pathlist = new ArrayList<>();
        SearchThread searchThread = new SearchThread();
        searchThread.start();
        Toast.makeText(getApplicationContext(), "searching, please wait", Toast.LENGTH_LONG).show();
        return 0;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    class SearchThread extends Thread {
        @Override
        public synchronized void start() {
            super.start();
            Log.d("MainActivity", "before initSearchFile");
            initSearchFile(MainActivity.mSDcard);
            Log.d("MainActivity", "after initSearchFile");
            Intent intent = new Intent();
            intent.setAction(MainActivity.KEYWORD_BROADCAST);
            intent.putStringArrayListExtra("namelist", namelist);
            intent.putStringArrayListExtra("pathlist", pathlist);
            Log.d("MainActivity", "before SendBroadcast");
            sendBroadcast(intent);
        }
    }

    public void initSearchFile(String s) {
        File file = new File(s);
        File[] files  =file.listFiles();
        Log.d("MainActivity", "after listFiles");
        for (File currentFile : files) {
            if(currentFile.isDirectory()) {
                initSearchFile(currentFile.getAbsolutePath());
            } else if (currentFile.getName().contains(searchContent)) {
                namelist.add(currentFile.getName());
                pathlist.add(currentFile.getPath());
            }
        }
    }
}
