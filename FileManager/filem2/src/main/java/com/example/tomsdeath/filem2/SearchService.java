package com.example.tomsdeath.filem2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.File;
import java.util.ArrayList;

public class SearchService extends Service {
    String search_name;
    ArrayList<String> name_list1;
    ArrayList<String> path_lis1t;

    public SearchService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        search_name = intent.getStringExtra("search_name");
        name_list1 = new ArrayList<>();
        path_lis1t = new ArrayList<>();
        SearchThread searchThread = new SearchThread();
        searchThread.start();
        return 0;
    }

    /*class SearchThread extends Thread {
        @Override
        public void run() {
            search(MainActivity.SD_PATH);
            super.run();
        }
    }*/

    class SearchThread extends Thread {
        @Override
        public void run() {
            super.run();
            search(MainActivity.SD_PATH);
            Intent intent1 = new Intent();
            intent1.putStringArrayListExtra("namelist", name_list1);
            intent1.putStringArrayListExtra("pathlist", path_lis1t);
            intent1.setAction("cccccccc");
            sendBroadcast(intent1);
        }
    }

    public void search(String path) {
        File file_sd = new File(path);
        File[] array_file = file_sd.listFiles();
        for(File file : array_file) {
            if(file.getName().contains(search_name)) {
                name_list1.add(file.getName());
                path_lis1t.add(file.getPath());
            } else if(file.isDirectory()) {
                search(file.getAbsolutePath());
            }
        }
    }
}
