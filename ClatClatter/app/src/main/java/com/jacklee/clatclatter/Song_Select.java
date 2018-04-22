package com.jacklee.clatclatter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Song_Select extends AppCompatActivity {
    private List<String> songnames=new ArrayList<>();
    private List<Song> songlist=new ArrayList<>();
    private List<Song> songlist_local=new ArrayList<>();
    private List<Song1>list=new ArrayList<>();
    PlayVoice playVoice = new PlayVoice(0);
    private  ListView listView1 ;
    MediaPlayer player = new MediaPlayer();
    int songs[]=new int[]{R.raw.time,R.raw.gem};
    int song;
    int selected =-1;
    int lastselected=-1;
    int selected_local=-1;
   boolean local_select=false;//最终选择本地
   boolean system_select=false;//最终选择系统
    private  int index;
    boolean playing =false;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    Intent intent=new Intent();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ringtone_selection);

        initSongs();
        initSongs1();
        final ListView listView = (ListView) findViewById(R.id.ring_listview);

        listView1 = (ListView)findViewById(R.id.ring_listview1);
        preferences=getSharedPreferences("data",MODE_PRIVATE);
        //通过存储的布尔值判断选取的是系统铃声还是本地铃声，并在相应位置设置图标
       boolean system= preferences.getBoolean("system_select",false);
       boolean local0= preferences.getBoolean("local_select",false);
        //如果选取了系统的在相应位置设置图标，选取了本地的在相应位置设置图标，没有选取都不设
        int inDex=preferences.getInt("index",0);
        Log.i("tag","111111");
        Log.i("tag","111111"+local0);
        Log.i("tag","111111"+system);
        if(system){
            songlist.get(inDex).setImageId(R.drawable.select);

        }else if(local0){
            songlist_local.get(inDex).setImageId(R.drawable.select);

        }
        //设置相应的适配器，并应用到listview控件中
        SongAdapter songAdapter=new SongAdapter(Song_Select.this,R.layout.song_selection_item,songlist);
        listView.setAdapter(songAdapter);
        SongAdapter songAdapter1= new SongAdapter(Song_Select.this,R.layout.song_selection_item,songlist_local);
        listView1.setAdapter(songAdapter1);
        Log.i("tag","222222");
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                local_select = true;
                system_select=false;
                PlayVoice.stopVoice();
                playing=false;
                lastselected=selected=-1;
                //正在播放而且是当前的点击之后停止播放
                if(player.isPlaying()&&(selected_local==i)){
                   //player.stop();
                    player.pause();
                   //player.release();
                }else if((player.isPlaying())&&(selected_local!=i)){
                    //如果正在播放当前选的不是之前的，停止播放的，播放刚选的
                    player.pause();
                    try {
                        Log.i("tag", list.get(i).path);
                        player.reset();
                       player.setDataSource(list.get(i).path);
                       player.prepare();
                       player.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }  // PlayVoice.stopVoice();
                else if(!player.isPlaying()){
                    //如果当前没有播放就播放所选的
                    try {
                        Log.i("tag", list.get(i).path);
                        player.reset();
                        player.setDataSource(list.get(i).path);
                        player.prepare();
                        player.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                selected_local=i;
                if (local_select) {
                    intent.putExtra("data_return", songnames.get(i)); //传歌名
                    intent.putExtra("songPath", list.get(i).path);//传路径
                }
                for(int n=0;n<songlist.size();n++){
                    songlist.get(n).setImageId(0);  //都不设图标
                }
                for (int n1=0;n1<songlist_local.size();n1++){
                    songlist_local.get(n1).setImageId(0);//都不设图标
                }
                songlist_local.get(i).setImageId(R.drawable.select); //所选的歌曲设上已选图标
                //应用适配器，index记录位置
                SongAdapter songAdapter=new SongAdapter(Song_Select.this,R.layout.song_selection_item,songlist);
                listView.setAdapter(songAdapter);
                SongAdapter songAdapter2=new SongAdapter(Song_Select.this,R.layout.song_selection_item,songlist_local);
                listView1.setAdapter(songAdapter2);
                index = i;
                editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                Log.i("tag","local"+local_select+"system"+system_select);
                editor.putBoolean("local_select",local_select);
                editor.putBoolean("system_select",system_select);
                editor.apply();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                local_select = false;
                system_select=true;
                player.pause();  //停止当前播放的本地音乐
                switch (i){
                    case 0:
                        song=songs[i];
                        if(selected!=i){  //如果selected不等于i,selected记录i的位置
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
                if(playing==false) {  //当前未播放，点击之后可以播放，并设为播放状态

                    PlayVoice playVoice=new PlayVoice(song);
                    playVoice.playVoice(view.getContext());
                    playing=true;
                }else {   //当前播放的和之前一样，点击之后暂停播放
                    if(lastselected==selected){
                        PlayVoice playVoice=new PlayVoice(song);
                        playVoice.stopVoice();
                        playing = false;
                    }else {
                        //当前播放的和选的不一样，暂停当前，播放刚选的
                        PlayVoice playVoice=new PlayVoice(song);
                        playVoice.stopVoice();
                        playVoice.playVoice(view.getContext());
                        playing=true;
                    }

                }
                lastselected = i;
                if(system_select) {
                    intent.putExtra("data_return",songlist.get(i).getSongName());//传歌名
                    intent.putExtra("song", songs[i]);//传位置
                }
                for(int n=0;n<songlist.size();n++){
                    if(n!=i)
                    {
                        songlist.get(n).setImageId(0);//设图标
                    }
                }
                for(int n1=0;n1<songlist_local.size();n1++)
                {
                    songlist_local.get(n1).setImageId(0);//设图标
                }
                songlist.get(i).setImageId(R.drawable.select);//给当前播放的音乐设图标
                //应用适配器
                SongAdapter songAdapter=new SongAdapter(Song_Select.this,R.layout.song_selection_item,songlist);
                listView.setAdapter(songAdapter);
                SongAdapter songAdapter2=new SongAdapter(Song_Select.this,R.layout.song_selection_item,songlist_local);
                listView1.setAdapter(songAdapter2);
                index = i;//记录位置
                editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                Log.i("tag","local"+local_select+"system"+system_select);
                editor.putBoolean("local_select",local_select);
                editor.putBoolean("system_select",system_select);
                editor.apply();
            }
        });
        //将选的系统音乐还是本地音乐存起来

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
    private void initSongs1(){
        list=MusicUtils.getMusicData(Song_Select.this);
       for(Song1 song1 :list){
            Song song = new Song (song1.getSong(),0);
            songnames.add(song1.getSong());
            songlist_local.add(song);
        }
        Log.i("tag",list.toString());
        Log.i("tag",songlist_local.toString());
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
                intent.putExtra("localselect",local_select);
                intent.putExtra("systemselect",system_select);
                setResult(RESULT_OK,intent);
                editor=getSharedPreferences("data",MODE_PRIVATE).edit();
                editor.putInt("index",index);
                editor.apply();
                playVoice.stopVoice();
                player.pause();
                this.finish();
                break;
            case android.R.id.home:
                Toast.makeText(this, "You click the wrong", Toast.LENGTH_SHORT).show();

                playVoice.stopVoice();
                player.pause();
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
        player.pause();
        finish();
    }
    //选取本地音乐

}

