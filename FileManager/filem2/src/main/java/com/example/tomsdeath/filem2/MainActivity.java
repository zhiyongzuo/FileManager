package com.example.tomsdeath.filem2;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomsdeath.filem2.Fragment.BlankFragment;
import com.example.tomsdeath.filem2.adapter.ListView_Adapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends ListActivity{
    public static String SD_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
    String ROOT_PATH = File.separator;
    String[] grid_name = {"phone", "sdcard", "search", "exit"};
    int[] grid_drawable = {R.drawable.menu_phone, R.drawable.menu_sdcard, R.drawable.menu_search, R.drawable.menu_exit};
    List<Map<String, Object>> grid_list = new ArrayList<>();
    TextView text_view_path;
    ArrayList<String> path_list;
    ArrayList<String> name_list;
    public static String mCurrentPath;
    int i;
    String search_name;
    String copy_name;
    String copy_path;
    EditText editText;
    boolean isSearched;
    boolean isCopy;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initGrid();
        initListView(SD_PATH);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file;
                    file = new File(path_list.get(position));
                String mName = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase();
                if (file.canRead()) {
                    if (file.isDirectory()) {
                        Toast.makeText(MainActivity.this, "folder", Toast.LENGTH_SHORT).show();
                        initListView(path_list.get(position));
                    } else if (mName.equals("avi") || mName.equals("mp4") || mName.equals("3gp")  || mName.equals("mkv")) {
                        Toast.makeText(MainActivity.this, "movie", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent("android.intent.action.VIEW");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("oneshot", 0);
                        intent.putExtra("configchange", 0);
                        Uri uri = Uri.fromFile(file);
                        intent.setDataAndType(uri, "video/*");
                        startActivity(intent);
                    } else if (mName.equals("txt")) {
                        Toast.makeText(MainActivity.this, "txt", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent("android.intent.action.VIEW");
                        intent.addCategory("android.intent.category.DEFAULT");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Uri uri2 = Uri.fromFile(file);
                            intent.setDataAndType(uri2, "text/plain");
                        startActivity(intent);
                    } else if (mName.equals("zip") || mName.equals("rar")) {
                        Toast.makeText(MainActivity.this, "zip", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setType("*/*");
                        startActivity(intent);
                    } else if (mName.equals("mp3")) {
                        Toast.makeText(MainActivity.this, "music", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent("android.intent.action.VIEW");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("oneshot", 0);
                        intent.putExtra("configchange", 0);
                        Uri uri = Uri.fromFile(file);
                        intent.setDataAndType(uri, "audio/*");
                        startActivity(intent);
                    } else if (mName.equals("html")) {
                        Toast.makeText(MainActivity.this, "web", Toast.LENGTH_SHORT).show();
                        Uri uri = Uri.parse(file.getPath()).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(file.getPath()).build();
                        Intent intent = new Intent("android.intent.action.VIEW");
                        intent.setDataAndType(uri, "text/html");
                        startActivity(intent);
                    } else if (mName.equals("png") || mName.equals("jpg")) {
                        Toast.makeText(MainActivity.this, "img", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(file), "image/*");
                        //intent.setType("image/*");
                        startActivity(intent);
                        /*Intent intent = new Intent();
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(file), "image/*");
                        //intent.setType("image/*");
                        startActivity(intent);*/
                    } else if (mName.equals("apk")) {
                        Toast.makeText(MainActivity.this, "apk", Toast.LENGTH_SHORT).show();
                        /*Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setType("apk/*");
                        startActivity(intent);*/

                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(android.content.Intent.ACTION_VIEW);
                        Uri uri = Uri.fromFile(file);
                        intent.setDataAndType(uri,"application/vnd.android.package-archive");
                        startActivity(intent);
                    } else {
                        //new AlertDialog(MainActivity.this).setMessage("cannot find the application to open it").show;
                        new AlertDialog.Builder(MainActivity.this).setMessage("cannot find the application to open it").setCancelable(true).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "cannot read this file", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                String[] menu_item = {"create", "copy", "paste", "rename", "delete"};
                final File file;
                    file = new File(path_list.get(position));
                if (file.canRead()) {
                    DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0://create
                                    View view1 = getLayoutInflater().inflate(R.layout.list_view_longclick, null);
                                    final RadioGroup radioGroup = (RadioGroup) view1.findViewById(R.id.radio_group);
                                    final RadioButton radioButton_folder = (RadioButton) view1.findViewById(R.id.radio_folder);
                                    final RadioButton radioButton_txt = (RadioButton) view1.findViewById(R.id.radio_txt);
                                    final EditText editText = (EditText) view1.findViewById(R.id.edit_txt_create_name);

                                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                                            switch (checkedId) {
                                                case R.id.radio_folder:
                                                    radioButton_folder.setChecked(true);
                                                    break;
                                                case R.id.radio_txt:
                                                    radioButton_txt.setChecked(true);
                                            }
                                        }
                                    });

                                    new AlertDialog.Builder(MainActivity.this).setView(view1).setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String create_name = editText.getText().toString();
                                            if (radioButton_folder.isChecked()) {
                                                final File create_folder_name = new File(mCurrentPath + File.separator + create_name);
                                                if (create_folder_name.exists()) {
                                                    new AlertDialog.Builder(MainActivity.this).setTitle("tip").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            create_folder_name.mkdir();
                                                            initListView(mCurrentPath);
                                                        }
                                                    }).setNegativeButton("cancel", null).setMessage("override the folder?").show();
                                                } else if (create_name.length() == 0 | create_name == null) {
                                                    Toast.makeText(MainActivity.this, "name is null", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    if (create_folder_name.mkdirs()) {
                                                        initListView(mCurrentPath);
                                                    }
                                                }
                                            } else if (radioButton_txt.isChecked()) {
                                                final File create_txt_name = new File(mCurrentPath + File.separator + create_name + ".txt");
                                                createTextFile(create_name, create_txt_name);
                                            } else {
                                                Toast.makeText(MainActivity.this, "which type do you want to create?", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).setNegativeButton("cancel", null).show();
                                    break;
                                case 1://copy ,only txt for now
                                    isCopy = true;
                                        copy_name = name_list.get(position);
                                        copy_path = path_list.get(position);
                                        Toast.makeText(MainActivity.this, "copy success", Toast.LENGTH_SHORT).show();
                                    break;
                                case 2://paste
                                    if(!isCopy) {
                                        Toast.makeText(MainActivity.this, "please copy first", Toast.LENGTH_SHORT).show();
                                    } else {
                                        try {
                                            String s = name_list.get(position);
                                            String suffix = s.substring(s.lastIndexOf(".") + 1, s.length()).toLowerCase();
                                            File new_file = new File(path_list.get(position) + File.separator + copy_name + "." + suffix);
                                            InputStream inputStream = new FileInputStream(new File(copy_path + File.separator + copy_name));
                                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                                            OutputStream outputStream = new FileOutputStream(new_file);
                                            String sss;
                                            byte[] bytes = new byte[1024];
                                            while ((sss=bufferedReader.readLine()) != null) {
                                                outputStream.write(bytes);
                                                createTextFile(copy_name, new_file);
                                            }
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException ioe) {
                                            ioe.printStackTrace();
                                        }
                                        initListView(path_list.get(position));
                                    }
                                    isCopy = false;
                                    break;
                                case 3://rename
                                    View view2 = getLayoutInflater().inflate(R.layout.rename, null);
                                    final EditText editText_rename = (EditText) view2.findViewById(R.id.edit_txt_rename);
                                    final File file1 = new File(path_list.get(position));
                                    new AlertDialog.Builder(MainActivity.this).setView(view2).setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String create_name = editText_rename.getText().toString();
                                            final File create_folder_name = new File(mCurrentPath + File.separator + create_name);
                                            if (create_folder_name.exists()) {
                                                new AlertDialog.Builder(MainActivity.this).setTitle("tip").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        file1.renameTo(create_folder_name);
                                                        initListView(mCurrentPath);
                                                    }
                                                }).setNegativeButton("cancel", null).setMessage("override the folder?").show();
                                            } else if (create_name.length() == 0 | create_name == null) {
                                                Toast.makeText(MainActivity.this, "name is null", Toast.LENGTH_SHORT).show();
                                            } else {
                                                if (create_folder_name.mkdirs()) {
                                                    file1.renameTo(create_folder_name);
                                                    initListView(mCurrentPath);
                                                }
                                            }
                                        }
                                    }).setNegativeButton("cancel", null).show();
                                            break;
                                case 4://delete
                                    if (!isSearched) {
                                        delete(path_list.get(position));
                                        initListView(mCurrentPath);
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }

                        public void delete(String path) {
                            File file1 = new File(path);
                            if (!file1.isFile()) {
                                File[] files = file1.listFiles();
                                if(files.length == 0) {
                                    file1.delete();
                                }
                                for (File file2 : files) {
                                    if (file2.isFile()) {
                                        file2.delete();
                                    } else {
                                        delete(file2.getPath());
                                    }
                                }
                                file1.delete();
                            }
                        }
                    };
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setItems(menu_item, onClickListener).show();
                } else {
                    Toast.makeText(MainActivity.this, "you hava no permisson to operate", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    private void initListView(String path) {
        isSearched = false;
        mCurrentPath = path;
        text_view_path.setText(path);
        path_list = new ArrayList<>();
        name_list = new ArrayList<>();
        File file_sd = new File(path);
        File[] array_file = file_sd.listFiles();
        for (int i = 0; i < array_file.length; i++) {
            path_list.add(array_file[i].getPath());
            name_list.add(array_file[i].getName());
        }
        setListAdapter(new ListView_Adapter(MainActivity.this, path_list, name_list));
    }

    private void initGrid() {
        for (int i=0; i<grid_name.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", grid_name[i]);
            map.put("drawable", grid_drawable[i]);
            grid_list.add(map);
        }
        text_view_path = (TextView)findViewById(R.id.text_view_path);
        GridView grid_view = (GridView)findViewById(R.id.grid_view);
        grid_view.setNumColumns(grid_drawable.length);
        grid_view.setAdapter(new SimpleAdapter(MainActivity.this, grid_list, R.layout.grid_view_item, new String[]{"name", "drawable"}, new int[]{R.id.item_name, R.id.item_drawable}));
        grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(21)
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://phone
                        if (i == 0) {
                            initListView(ROOT_PATH);
                            i=1;
                        }
                        break;
                    case 1://sdcard
                        if(i == 1) {
                            initListView(SD_PATH);
                            i=0;
                        }
                        break;
                    case 2://search
                        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(intent);
                        break;
                    case 3://exit
                        finish();
                        break;
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if(mCurrentPath.equals(SD_PATH) || mCurrentPath.equals(ROOT_PATH)) {
                finish();
            } else {
                initListView(new File(mCurrentPath).getParent());
            }
        }
        return true;
    }
    public void createTextFile(String create_name, final File create_txt_name) {
        if (create_txt_name.exists()) {
            new AlertDialog.Builder(MainActivity.this).setTitle("tip").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        create_txt_name.createNewFile();
                        initListView(mCurrentPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).setNegativeButton("cancel", null).setMessage("override the folder?").show();
        } else if (create_name.length() == 0 | create_name == null) {
            Toast.makeText(MainActivity.this, "txtname is null", Toast.LENGTH_SHORT).show();
        } else {
            try {
                create_txt_name.createNewFile();
                initListView(mCurrentPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
