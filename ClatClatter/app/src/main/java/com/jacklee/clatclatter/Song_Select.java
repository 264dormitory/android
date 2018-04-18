package com.jacklee.clatclatter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Song_Select extends AppCompatActivity {

    private List<Song> songlist=new ArrayList<>();
    PlayVoice playVoice = new PlayVoice(0);
    int songs[]=new int[]{R.raw.time,R.raw.gem};
    int song;
    int selected =-1;
    int lastselected=-1;
    private  int index;

    boolean playing =false;
    Intent intent=new Intent();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ringtone_selection);

        initSongs();
        final ListView listView = (ListView) findViewById(R.id.ring_listview);
        SharedPreferences preferences=getSharedPreferences("data",MODE_PRIVATE);
        if(preferences.getInt("index",-1)!=-1){
            songlist.get(preferences.getInt("index",-1)).setImageId(R.drawable.forword);
        }
        SongAdapter songAdapter=new SongAdapter(Song_Select.this,R.layout.song_selection_item,songlist);
        listView.setAdapter(songAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        song=songs[i];
                        if(selected!=i){

                            selected=i;
                        }else {

                        }
                        break;
                    case 1:
                        song=songs[i];
                        if(selected!=i){

                            selected=i;
                        }else {

                        }
                        break;
                        default:
                            break;
                }
                if(playing==false) {

                    PlayVoice playVoice=new PlayVoice(song);
                    playVoice.playVoice(view.getContext());
                    playing=true;
                }else {
                    if(lastselected==selected){
                        PlayVoice playVoice=new PlayVoice(song);
                        playVoice.stopVoice();
                        playing = false;
                    }else {

                        PlayVoice playVoice=new PlayVoice(song);
                        playVoice.stopVoice();
                        playVoice.playVoice(view.getContext());
                        playing=true;
                    }

                }
                lastselected = i;
                intent.putExtra("data_return",songlist.get(i).getSongName());
                intent.putExtra("song",songs[i]);
                for(int n=0;n<songlist.size();n++){
                    if(n!=i)
                    {
                        songlist.get(n).setImageId(0);
                    }
                }
                songlist.get(i).setImageId(R.drawable.forword);
                SongAdapter songAdapter=new SongAdapter(Song_Select.this,R.layout.song_selection_item,songlist);
                listView.setAdapter(songAdapter);
                index = i;
            }
        });
        //Toolbar Setting
        Toolbar yyy_toolbar = (Toolbar) findViewById(R.id.yyy_toolbar);
        yyy_toolbar.setTitle("铃声选择");
        //这句话用来实现使用toolbar 代替 actionbar
        setSupportActionBar(yyy_toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_cancel);
        }

    }

    private void initSongs() {
            Song song1=new Song("Time",0);
            songlist.add(song1);
            Song song2=new Song("泡沫",0);
            songlist.add(song2);

    }

    //  进行Toolbar的初始化操作
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.floatbutton_tick, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.tick:
                Toast.makeText(this, "You click the tick", Toast.LENGTH_SHORT).show();

                setResult(RESULT_OK,intent);
                SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                editor.putInt("index",index);
                editor.apply();
                playVoice.stopVoice();
                this.finish();
                break;
            case android.R.id.home:
                Toast.makeText(this, "You click the wrong", Toast.LENGTH_SHORT).show();

                playVoice.stopVoice();
                this.finish();
                break;
            default:
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        PlayVoice playVoice = new PlayVoice(0);
        playVoice.stopVoice();
        finish();
    }
}

