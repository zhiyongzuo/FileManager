package com.example.tomsdeath.filemanager;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomsdeath.filemanager.Adapter.ListView_Adapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends ListActivity {

    int[] imageResourceName = new int[] {
            R.drawable.menu_phone,
            R.drawable.menu_sdcard,
            R.drawable.menu_search,
            R.drawable.menu_create,
            R.drawable.paste,
            R.drawable.menu_exit
    };
    int[] menuName = new int[] {
          R.string.phone, R.string.sdCard, R.string.search, R.string.create ,R.string.paste, R.string.exit
    };

    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>() {};
    TextView mPath;
    EditText editText;
    String rootPath= File.separator;
    public static String mSDcard = Environment.getExternalStorageDirectory().toString();
    ListView_Adapter listView_adapter;
    public static int menuposition = 1;
    public static String mCurrentFilePath;
    String copyPath;
    String keyWords;
    String mCopyFileName;
    boolean isTextDataOk = false;
    String textData;
    int mChecked = 0;
    List<String> fileNameList;
    List<String> filePathList;
    List<String> fileNameList1;
    List<String> filePathList1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGridViewMenu();
        mPath = (TextView)findViewById(R.id.mPath);
        //listView = (ListView)findViewById(R.id.listview); no need to do this
        initListView(rootPath);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.d("test", "a1111111");
                final File file = new File(filePathList.get(position));
                Log.d("test", "a22222222");
                if (file.canRead()) { //if don't write this line, why would throw a nullpoint Exception about array null(line117
                    Log.d("test", "a3333333333");
                    if (file.isDirectory()) {
                        Log.d("test", "a1");
                        initListView(filePathList.get(position));
                        Log.d("test", "a2");
                    } else {
                        final ProgressDialog mProgressDialog = new ProgressDialog(MainActivity.this);
                        mProgressDialog.setMessage("Processing");
                        mProgressDialog.setTitle("tips");
                        mProgressDialog.setCancelable(true);
                        mProgressDialog.setButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mProgressDialog.dismiss();
                                Log.d("MainActivity", "progress_dialog canceled");
                            }
                        });
                        mProgressDialog.show();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Log.d("MainActivity", "run0");
                                    FileInputStream fis = new FileInputStream(file);
                                    StringBuilder mSB = new StringBuilder();
                                    int i;
                                    Log.d("MainActivity", "run1");
                                    if((i = fis.read()) != -1) {
                                        mSB.append((char)i);
                                    }
                                    Log.d("MainActivity", "run2");
                                    fis.close();
                                    textData = mSB.toString();
                                    isTextDataOk = true;
                                    Log.d("MainActivity", "run3");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("MainActivity", "run00");
                                while (true) {
                                    if(isTextDataOk) {
                                        Log.d("MainActivity", "run01");
                                        Intent intent = new Intent(MainActivity.this, EditTextFullscreenActivity.class);
                                        intent.putExtra("data", textData);
                                        intent.putExtra("path", file.getPath());
                                        startActivity(intent);
                                        mProgressDialog.dismiss();
                                        break;
                                    }
                                }
                            }
                        }).start();
                    }
                }
            }
        });
    }

    private IntentFilter intentFilter;
    public static String KEYWORD_BROADCAST = "keyword_broadcast";
    @Override
    protected void onStart() {
        super.onStart();
        intentFilter = new IntentFilter(KEYWORD_BROADCAST);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("MainActivity", "before setListAdapter");
            setListAdapter(new ListView_Adapter(context, intent.getStringArrayListExtra("namelist"),
                    intent.getStringArrayListExtra("pathlist")));
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        filePathList.clear();
        fileNameList.clear();
        unregisterReceiver(broadcastReceiver);
    }

    private void initListView(String path) {
        fileNameList = new ArrayList<>();
        filePathList = new ArrayList<>();
        mCurrentFilePath = path;
        mPath.setText(path);
        File file = new File(path);
        File[] files =  file.listFiles();
        if(menuposition == 1 && !path.equals(rootPath)) {
            Log.d("test", "i0.11");
            fileNameList.add("BackToRoot");
            filePathList.add(rootPath);
            fileNameList.add("BackToUp");
            filePathList.add(new File(path).getParent());
            Log.d("test", "i0.12");
        }else if (menuposition == 2 && !path.equals(mSDcard)) {
                Log.d("test", "i0.2");
                fileNameList.add("BackToRoot");
                filePathList.add(mSDcard);
                fileNameList.add("BackToUp");
                filePathList.add(new File(path).getParent());
                Log.d("test", "i0.22");
        }
        Log.d("test", "i1");
        for(File mCurrentFile:files) { //what's wrong in this line
            Log.d("test", "i1.1");
            fileNameList.add(mCurrentFile.getName());
            filePathList.add(mCurrentFile.getPath());
        }
        listView_adapter = new ListView_Adapter(MainActivity.this, fileNameList, filePathList);
        setListAdapter(listView_adapter);
        //listView.setAdapter(listView_adapter);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!mCurrentFilePath.equals(rootPath) || !mCurrentFilePath.equals(mSDcard)) {
                    String[] mMenu = {"copy", "rename", "delete"};
                    final File file1 = new File(filePathList.get(position));
                    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (file1.canRead()) {
                                switch (which) {
                                    case 0:
                                        //copy the file, cannot copy folder
                                        copyTheFile(file1);
                                        Toast.makeText(MainActivity.this, copyPath, Toast.LENGTH_SHORT).show();
                                        break;
                                    case 1:
                                        //rename the file
                                        View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                                                .inflate(R.layout.rename_dialog, null);
                                        EditText rename_edittext = (EditText) view.findViewById(R.id.edit_text);
                                        String new_name = rename_edittext.getText().toString();
                                        File new_file = new File(mCurrentFilePath);
                                        file1.renameTo(new_file);
                                        break;
                                    case 2:
                                        //delete
                                        break;
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "not get the access right", Toast.LENGTH_SHORT).show();
                            }
                        }
                    };
                    new AlertDialog.Builder(MainActivity.this).setTitle("please take the choise").
                            setItems(mMenu, listener).setPositiveButton(
                            "cancel", null).show();
                }
                return true;
            }
        });

    }

    private void copyTheFile(File file1) {
        if (file1.isFile() && "txt".equals(file1.getName().substring(file1.getName().lastIndexOf(".") + 1,
                file1.getName().length()))) {
            copyPath = mCurrentFilePath + File.separator + file1.getName();
        } else{
                copyPath = mCurrentFilePath + File.separator + file1.getName(); //except txt file
            }
        mCopyFileName = file1.getName();
    }

    private void initGridViewMenu() {
        GridView mGridViewToolbar = (GridView)findViewById(R.id.gridView);
        //mGridViewToolbar.setVerticalSpacing(10);
        //mGridViewToolbar.setHorizontalSpacing(10);
        for (int i=0; i<menuName.length; i++) {
            HashMap<String, Object> map = new HashMap<>();//
            map.put("image", imageResourceName[i]);
            map.put("title", getString(menuName[i]));
            list.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, list, R.layout.simpleadapter_item, new String[] {"image", "title"},
                new int[] {R.id.imageView, R.id.textView});
        mGridViewToolbar.setAdapter(simpleAdapter);
        mGridViewToolbar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Toast.makeText(MainActivity.this, "root", Toast.LENGTH_SHORT).show();
                        menuposition = 1;
                        initListView(rootPath);
                        break;
                    case 1:
                        Log.d("test", "case1");
                        Toast.makeText(MainActivity.this, "sdcard", Toast.LENGTH_SHORT).show();
                        Log.d("test", "case1, 1");
                        menuposition = 2;
                        File file = new File(mSDcard);
                        if (!file.exists()) {
                            Toast.makeText(MainActivity.this, "nnnnnnnnnnnnnnnnnnnosdcard", Toast.LENGTH_SHORT).show();
                        } else {
                            initListView(mSDcard);
                        }
                        Log.d("test", "case1, 2");
                        break;
                    case 2://search
                        searchDialog();
                        break;
                    case 3:
                        Toast.makeText(MainActivity.this, "3 to createFile", Toast.LENGTH_SHORT).show();
                        String[] itemsOptions = {"text", "file"};
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View mview = inflater.inflate(R.layout.create_dialog, null);
                        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
                        RadioButton create_file = (RadioButton) findViewById(R.id.create_file);
                        RadioButton create_folder = (RadioButton) findViewById(R.id.create_folder);
                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                switch (checkedId) {
                                    case R.id.create_file:
                                        mChecked = 1;
                                        break;
                                    case R.id.create_folder:
                                        mChecked = 2;
                                        break;
                                }
                            }
                        });
                        new AlertDialog.Builder(MainActivity.this).setTitle("create new file").setPositiveButton("create", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editText = (EditText) findViewById(R.id.edit_text);
                                String edit_name = editText.getText().toString();
                                if (mChecked == 1) {
                                    File mCreateFile = new File(mCurrentFilePath + File.separator + edit_name + ".txt");
                                    try {
                                        mCreateFile.createNewFile();
                                        List<String> filenamelist = new ArrayList<String>();
                                        List<String> filePathlist = new ArrayList<String>();
                                        if (!mCurrentFilePath.equals(rootPath)) {
                                            filenamelist.add("backtoroot");
                                            filePathlist.add(rootPath);
                                            filenamelist.add("backtoup");
                                            filenamelist.add(new File(mCurrentFilePath).getParent());
                                        } else if (!mCurrentFilePath.equals(mSDcard)) {
                                            filenamelist.add("backtoroot");
                                            filePathlist.add(rootPath);
                                            filenamelist.add("backtoup");
                                            filePathlist.add(new File(mCurrentFilePath).getParent());
                                        }
                                        File mfile = new File(mCurrentFilePath);
                                        File[] files = mfile.listFiles();
                                        for (File mCurrentFile : files) {
                                            filenamelist.add(mCurrentFile.getName());
                                            filePathlist.add(mCurrentFile.getName());
                                        }
                                        ListView_Adapter listAdapter = new ListView_Adapter(MainActivity.this, filenamelist, filePathlist);
                                        //listView.setAdapter(listAdapter);
                                        setListAdapter(listAdapter);
                                    } catch (IOException e) {
                                        Toast.makeText(MainActivity.this, "wrong file name", Toast.LENGTH_SHORT).show();
                                    }
                                } else if (mChecked == 2) {
                                    File mCreateFolder = new File(mCurrentFilePath + File.separator + edit_name);
                                    if (!mCreateFolder.exists() && !mCreateFolder.isDirectory() && edit_name.length() != 0) {
                                        mCreateFolder.mkdirs();
                                        initListView(mCurrentFilePath);
                                    }
                                }
                            }
                        }).setNegativeButton("cancel", null).setView(mview);
                        break;
                    case 4:
                        int i = 0;
                        Toast.makeText(MainActivity.this, "4paste", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(MainActivity.this, Horizontal.class));
                        if (copyPath == null) {
                            Toast.makeText(MainActivity.this, "copypath is null, please copy the file", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                FileInputStream fileInputStream = new FileInputStream(new File(copyPath));
                                FileOutputStream fileOutputStream = new FileOutputStream(new File(mCurrentFilePath + File.separator + mCopyFileName));
                                if ((i = fileInputStream.read()) != -1) {
                                    fileOutputStream.write(i);
                                }
                                if (fileInputStream != null) {
                                    fileInputStream.close();
                                }
                                if (fileOutputStream != null) {
                                    fileOutputStream.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            initListView(mCurrentFilePath);
                        }
                        break;
                    case 5:
                        Toast.makeText(MainActivity.this, "5exit", Toast.LENGTH_SHORT).show();
                        MainActivity.this.finish();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void searchDialog() {
        Toast.makeText(MainActivity.this, "search", Toast.LENGTH_SHORT).show();
        final View mLL = (View)(LayoutInflater.from(MainActivity.this).inflate(R.layout.search, null));
        new AlertDialog.Builder(MainActivity.this).setTitle("search").setView(mLL).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(MainActivity.this, "yes", Toast.LENGTH_SHORT).show();
                //RadioButton local = (RadioButton)mLL.findViewById(R.id.local_search);
                //RadioButton all = (RadioButton)mLL.findViewById(R.id.search_all);
                editText = (EditText) mLL.findViewById(R.id.keyword);
                keyWords = editText.getText().toString();
                /*if (keyWords.length() != 0) {
                    Intent keywordIntent = new Intent(MainActivity.this, SearchService.class);
                    //keywordIntent.setAction()
                    keywordIntent.putExtra("key", keyWords);
                    startService(keywordIntent);
                } else {
                    Toast.makeText(MainActivity.this, " edittext is null", Toast.LENGTH_SHORT).show();
                    searchDialog();
                }*/
                // other solve method
                fileNameList1 = new ArrayList<>();
                filePathList1 = new ArrayList<>();
                initSearchFile(mSDcard);
                setListAdapter(new ListView_Adapter(MainActivity.this, fileNameList1, filePathList1));
            }
        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(MainActivity.this, DrawingActivity.class));
            }
        }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, DrawingActivity.class));// to launch a drawing activity
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void initSearchFile(String s) {
        File file = new File(s);
        File[] files  =file.listFiles();
        for (File currentFile : files) {
            if(currentFile.isDirectory()) {
                initSearchFile(currentFile.getAbsolutePath());
            } else if (currentFile.getName().contains(keyWords)) {
                fileNameList1.add(currentFile.getName());
                filePathList1.add(currentFile.getPath());
            }
        }
    }
}
