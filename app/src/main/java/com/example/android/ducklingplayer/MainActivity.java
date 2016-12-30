package com.example.android.ducklingplayer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.jar.Manifest;

import static android.R.attr.path;
import static android.R.attr.start;
import static java.security.AccessController.getContext;


public class MainActivity extends AppCompatActivity {

    public String name, fp, path;
    public File[] files;
    public ImageButton imgbutton;
    public String extpath=null,rootpath="/storage";
    static final int MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE=0x1;


    public ArrayList<SongInfo> playlist = new ArrayList<>();
    private SongInfo currsong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        askForPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
        /*imgbutton=(ImageButton)findViewById(R.id.nowplaying);
        imgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ti=new Intent(MainActivity.this,PlayerActivity.class);
                ti.addFlags(ti.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(ti);
            }
        });*/
        /*if (isExternalStorageReadable())
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(path);
        chkDirectories(file);
        File root= new File(rootpath);
        getExtrnlDirs(root);
        if(extpath!=null)
        {
            File ext=new File(extpath);
            chkExtrnlDirs(ext);
        }
        for(int i=0;i<playlist.size();i++)
        {
            for(int j=1;j<(playlist.size()-i);j++)
            {
                if((playlist.get(j).getsongname().compareTo(playlist.get(j-1).getsongname()))<0)
                {
                    Collections.swap(playlist,j,j-1);
                }
            }
        }*/
        //setPlaylist();
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
            imgbutton=(ImageButton)findViewById(R.id.nowplaying);
            imgbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ti=new Intent(MainActivity.this,PlayerActivity.class);
                    ti.addFlags(ti.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(ti);
                }
            });
            if (isExternalStorageReadable())
                path = Environment.getExternalStorageDirectory().getAbsolutePath();
            File file = new File(path);
            chkDirectories(file);
            File root= new File(rootpath);
            getExtrnlDirs(root);
            if(extpath!=null)
            {
                File ext=new File(extpath);
                chkExtrnlDirs(ext);
            }
            for(int i=0;i<playlist.size();i++)
            {
                for(int j=1;j<(playlist.size()-i);j++)
                {
                    if((playlist.get(j).getsongname().compareTo(playlist.get(j-1).getsongname()))<0)
                    {
                        Collections.swap(playlist,j,j-1);
                    }
                }
            }
            setPlaylist();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(MainActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                    imgbutton=(ImageButton)findViewById(R.id.nowplaying);
                    imgbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent ti=new Intent(MainActivity.this,PlayerActivity.class);
                            ti.addFlags(ti.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(ti);
                        }
                    });
                    if (isExternalStorageReadable())
                        path = Environment.getExternalStorageDirectory().getAbsolutePath();
                    File file = new File(path);
                    chkDirectories(file);
                    File root= new File(rootpath);
                    getExtrnlDirs(root);
                    if(extpath!=null)
                    {
                        File ext=new File(extpath);
                        chkExtrnlDirs(ext);
                    }
                    for(int i=0;i<playlist.size();i++)
                    {
                        for(int j=1;j<(playlist.size()-i);j++)
                        {
                            if((playlist.get(j).getsongname().compareTo(playlist.get(j-1).getsongname()))<0)
                            {
                                Collections.swap(playlist,j,j-1);
                            }
                        }
                    }
                    setPlaylist();
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }


    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public void chkDirectories(File dir) {
        File[] files = dir.listFiles();
        for (File f : files) {
            if ((f.exists()) && (f.isDirectory())) {
                if (f.getName().toString().startsWith("com") || f.getName().startsWith("."))
                    continue;
                chkDirectories(f);
            } else if (f.exists() && f.isFile()) {
                if (f.getAbsolutePath().endsWith(".mp3")) {
                    Log.v("EXT",f.getName());
                    playlist.add(new SongInfo(f.getName(),f.getAbsolutePath()));
                }
            }
        }
    }
    public void getExtrnlDirs(File root)
    {
        File[] storage=root.listFiles();
        for(File f:storage)
        {
            Log.v("Root",f.getName());
            if((!f.getName().equals("self")) && (!f.getName().equals("emulated")) && (!f.getName().equals("sdcard0")))
                extpath=f.getAbsolutePath();
        }

    }
    public void chkExtrnlDirs(File ext){
        File[] extfiles=ext.listFiles();
        for(File f:extfiles)
        {
            if((f.exists()) && (f.isDirectory()) && (!f.getName().startsWith(".")) && (!f.getName().startsWith("com")))
            {
                chkExtrnlDirs(f);
            }
            else if((f.exists()) && (f.isFile()))
            {
                if(f.getAbsolutePath().endsWith(".mp3")) {
                    playlist.add(new SongInfo(f.getName(), f.getAbsolutePath()));
                    Log.v("EXT", f.getName());
                }
            }
        }
    }

    public void setPlaylist() {

        SongAdapter adapter = new SongAdapter(this, playlist);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                currsong=playlist.get(position);
                if(PlayerActivity.getInstance().isPlaying())
                    PlayerActivity.getInstance().stop();
                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                intent.putExtra("List",playlist);
                intent.putExtra("SongIndex",position);
                intent.putExtra("Name",currsong.getsongname());
                intent.putExtra("Filepath",currsong.getsongpath());
                startActivity(intent);
            }
        });
    }
}


