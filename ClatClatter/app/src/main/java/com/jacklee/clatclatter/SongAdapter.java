package com.jacklee.clatclatter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SongAdapter extends ArrayAdapter<Song>{
  private int resourceId;
  public SongAdapter(Context context,int textViewResourceId,List<Song>songs){
      super(context,textViewResourceId,songs);
      resourceId=textViewResourceId;
  }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       Song song = getItem(position);
       View view =LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
       ImageView songimage = (ImageView)view.findViewById(R.id.song_imageview);
       TextView songtext = (TextView)view.findViewById(R.id.song_textview);
       songimage.setImageResource(song.getImageId());
       songtext.setText(song.getSongName());
       return view;
    }
}
