package com.example.tomsdeath.filem2;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tomsdeath.filem2.adapter.ListView_Adapter;

import java.util.ArrayList;

import butterknife.Bind;

public class SearchActivity extends AppCompatActivity {
    @Bind(R.id.actv_search)
    AutoCompleteTextView autoCompleteTextView;
    @Bind(R.id.cardview)
    CardView cardview;
    @Bind(R.id.search_list)
    ListView listview;

    String wordToSearch;
    ProgressDialog progressDialog;
    ArrayList<String> path_lis1t;
    ArrayList<String> name_list1;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            progressDialog.dismiss();
            path_lis1t = intent.getStringArrayListExtra("pathlist");
            name_list1 = intent.getStringArrayListExtra("namelist");
            listview.setAdapter(new ListView_Adapter(context,
                    path_lis1t, name_list1));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        registerBroadcastReceiver();
        autoCompleteTextView.setOnEditorActionListener(editorActionListener);
    }

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter("cccccccc");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            wordToSearch = autoCompleteTextView.getText().toString();
            Intent intent = new Intent(SearchActivity.this, SearchService.class);
            intent.putExtra("search_name", wordToSearch);
            startService(intent);
            progressDialog = new ProgressDialog(SearchActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setTitle("search");
            progressDialog.setMessage("please waiting");
            progressDialog.show();
            return false;
        }
    };
}
