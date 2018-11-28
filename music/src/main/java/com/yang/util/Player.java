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
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Player{
    public enum MODE {ORDER, RANDOM, SINGLE};
    private MODE playMode;
    private Music nowMusic;
    private List<Music> nowList;
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
        nowList = new ArrayList<Music>();
        advancedPlayer = null;
    }

    public List<Music> getPlayList() {
        return playList;
    }

    public void setPlayList(List<Music> playList) {
        this.playList = playList;
    }

    public List<Music>  getNowList() {
        return nowList;
    }

    public void setNowList(List<Music> nowList) {
        this.nowList = nowList;
    }

    public MODE getPlayMode() {
        return playMode;
    }

    public void setPlayMode(MODE playMode) {
        this.playMode = playMode;
        switch (this.playMode) {
            case ORDER:     //ORDER由SINGLE变过来的
                break;
            case SINGLE:    //SINGLE由RANDOM变过来的
                changePlayList();
                break;
            case RANDOM:    //RADOM由ORDER变过来的
                Collections.shuffle(playList);
                break;
        }
    }

    public void setNowMusic(Music nowMusic) {
        this.nowMusic = nowMusic;
    }

    public Music getNowMusic() {
        return nowMusic;
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
        if(size == 0) {
            //???????????????????????????????????????????????
            //???????????????????????????????????????????????
        }
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
                //上面这句似乎不写也行？
                play();
                break;
        }
    }

    public void close() {
        nowMusic = null;
        player.close();
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

    public void addPlayMusic(Music music) {
        playList.add(music);
    }

    public void addNowMusic(Music music) {
        nowList.add(music);
    }

    public void clearAll() {
        playList.clear();
        nowMusic = null;
    }

    //切换歌单时调用
    private void changePlayList() {
        playList.clear();
        playList.addAll(nowList);
    }

    public void changeNowList(List<Music> newList) {
        nowList.clear();
        nowList.addAll(newList);
    }
}

