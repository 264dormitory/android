package com.jacklee.clatclatter;;

public class Song {
    private String songName; //歌名
    private int imageId;//图标
    //构造函数
    public Song(String name,int imageId){
        this.songName=name;
        this.imageId=imageId;
    }
    public String getSongName(){
        return songName;
    }

    public int getImageId() {
        return imageId;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
