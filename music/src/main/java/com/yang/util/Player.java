package com.yang.util;

import com.yang.model.Music;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Player{
    private enum MODE {ORDER, RANDOM, SINGLE};
    private MODE playMode;
    private Music nowMusic;
    private List<Music> playList;
    private static Player player;
    private AdvancedPlayer advancedPlayer;

    static {
        player = null;
    }

    //单例模式
    public static Player getInstance() {
        if(player == null) {
            player = new Player();
        }
        return player;
    }

    private Player() {
        playMode = MODE.ORDER;
        nowMusic = null;
        playList = new ArrayList<Music>();
        advancedPlayer = null;
    }

    public void play() {
        if(playList == null) {
            //如何处理？？弹出提示？？？
            return;
        }
        if(playList != null && nowMusic == null) {
            nowMusic = playList.get(0);
        }
        try {
            BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(nowMusic.getUrl()));
            advancedPlayer = new AdvancedPlayer(buffer);
            advancedPlayer.play();
        } catch (JavaLayerException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //按照歌单顺序播放下一首
    public void playNext() {
        int size = (playList == null) ? 0 : playList.size();
        int now = getNowPosition();
        if(now > 0) {
            if(now < size - 1) {
                nowMusic = playList.get(now + 1);
            }
            else {
                nowMusic = playList.get(0);
            }
        }
        play();
    }

    //按照歌单顺序播放上一首
    public void playPrev() {
        int size = (playList == null) ? 0 : playList.size();
        int now = getNowPosition();
        if(now > 0) {
            nowMusic = playList.get(now - 1);
        }
        else if(now == 0){
            nowMusic = playList.get(size - 1);
        }
        else {
            System.out.println("Player:playPrev BUG Occurs");
        }
        play();
    }

    //按照歌单顺序自动播放
    public void playAuto() {
        switch (playMode) {
            case ORDER:
            case RANDOM:
                playNext();
                break;
            case SINGLE:
                nowMusic = playList.get(getNowPosition());
                play();
                break;
        }
    }

    public void close() {
        nowMusic = null;
        player.close();
    }

    public MODE getPlayMode() {
        return playMode;
    }

    public void setPlayMode(MODE playMode) {
        this.playMode = playMode;
    }

    public void setNowMusic(Music nowMusic) {
        this.nowMusic = nowMusic;
    }

    public Music getNowMusic() {
        return nowMusic;
    }

    private int getNowPosition() {
        int i = 0;
        for(Music music : playList) {
            if(music.equals(playList.get(i))) {
                return i;
            }
            i++;
        }
        System.out.println("Player:getNowPosition BUG Occurs");
        return -1;
    }

    public void addMusic(Music music) {
        playList.add(music);
    }

    public void clearAll() {
        playList.clear();
        nowMusic = null;
    }

    //切换歌单时调用
    public void addMusicList(List<Music> list) {
        list.clear();
        for(Music music : list) {
            list.add(music);
        }
    }
}

