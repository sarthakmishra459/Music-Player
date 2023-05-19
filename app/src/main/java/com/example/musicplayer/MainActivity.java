package com.example.musicplayer;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    String[] items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.listViewSong);
        runtimePermission();

    }
    public void runtimePermission(){
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                displaySongs();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }
    public ArrayList<File> findSong (File file){
        ArrayList<File> arrayList=new ArrayList<>();
        File[] files=file.listFiles();
        //assert files != null;
        if(files!=null){
            for(File singlefile: files){
                if(singlefile.isDirectory() && !singlefile.isHidden()){
                    arrayList.addAll(findSong(singlefile));
                }
                else{
                    if(singlefile.getName().endsWith((".mp3")) && !singlefile.getName().startsWith(".") || singlefile.getName().endsWith((".wav")) && !singlefile.getName().startsWith(".")){
                        arrayList.add(singlefile);
                    }
                }
            }

        }
        return arrayList;
    }
    /*public ArrayList<File> findSong(File file) {
        ArrayList<File> arrayList = new ArrayList<>();
        if (file != null) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File singlefile : files) {
                    if (singlefile.isDirectory() && !singlefile.isHidden()) {
                        arrayList.addAll(findSong(singlefile));
                    } else {
                        if (singlefile.getName().endsWith((".mp3")) || singlefile.getName().endsWith((".wav"))) {
                            arrayList.add(singlefile);
                        }
                    }
                }
            }
        }
        return arrayList;
    }*/

    void displaySongs(){
        ArrayList<File> mySongs=findSong(Environment.getExternalStorageDirectory());
        //String [] items=new String[mySongs.size()];
        items=new String[mySongs.size()];
        for(int i=0;i<mySongs.size();i++){
            items[i]= mySongs.get(i).getName().replace(".mp3","").replace((".wav"),"");
        }
        /*ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,PlayerActivity.class);
                String currentSong=listView.getItemAtPosition(position).toString();
                intent.putExtra("songs",mySongs);
                intent.putExtra("currentSong",currentSong);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });*/
        customAdapter customAdapter=new customAdapter();
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            String songName =(String) listView.getItemAtPosition(i);
            startActivity(new Intent(getApplicationContext(),PlayerActivity.class).putExtra("songs",mySongs).putExtra("songname",songName).putExtra("pos",i));
        });
    }
    class customAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View myView=getLayoutInflater().inflate(R.layout.list_item,null);
            TextView textsong=myView.findViewById(R.id.txtsongname);
            textsong.setSelected(true);
            textsong.setText(items[i]);
            return myView;
        }
    }
}