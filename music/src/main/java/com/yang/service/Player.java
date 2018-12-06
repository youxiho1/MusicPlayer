package com.yang.service;

import com.yang.model.Music;
import com.yang.view.bottom.Operation;

import javazoom.jl.decoder.JavaLayerException;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sound.midi.SoundbankResource;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;

public class Player {

    private List<Music> nowList;
    private List<Music> playList;
    private static Player player;       //单例用
    public enum MODE {ORDER, RANDOM, SINGLE};
    private MODE playMode;
    private Music nowMusic;
    private static Operation operation;
    private javazoom.jl.player.Player myPlayer;
    Object lock = new Object();

    private JSlider jSliderProgress;

    private long time = 0;

    private boolean paused = false;
    private boolean over = false;
    private FloatControl volume = null;
    private SourceDataLine line=null;


    static {
        player = null;
    }

    public static Player getInstance(Operation operation2) {
        operation = operation2;
        return getInstance();
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
        //playerLock = new Object();
        playList = new ArrayList<Music>();
        nowList = new ArrayList<Music>();
        myPlayer = null;
    }

    //按照歌单顺序播放下一首
    public void playNext() {
        int size = (playList == null) ? 0 : playList.size();
        if(size == 0) {
            JOptionPane.showMessageDialog(null, "无可播放歌曲", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        System.out.println("Playlist:");
        for(int i = 0; i < playList.size(); i++) {
            System.out.println(playList.get(i).getName());
        }
        int now = getNowPosition();
        System.out.println("now=" + now);
        if(now >= 0) {
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
        if(size == 0) {
            JOptionPane.showMessageDialog(null, "无可播放歌曲", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
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

    private int getNowPosition() {
        int i = 0;
        for(Music music : playList) {
            if(nowMusic.equals(music)) {
                return i;
            }
            i++;
        }
        System.out.println("Player:getNowPosition BUG Occurs");
        return -1;
    }

    public void addMusic(Music music) {
        nowList.add(music);
        playList.add(music);
    }

    public void setNowMusic(Music nowMusic) {
        this.nowMusic = nowMusic;
    }

    public Music getNowMusic() {
        return nowMusic;
    }

    public void play() {
        SourceDataLine line = null;
        operation.changeMusicInformation(nowMusic);
        if(playList == null) {
            JOptionPane.showMessageDialog(null, "无可播放歌曲", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(nowMusic == null) {
            nowMusic = playList.get(0);
        }
        try {
            BufferedInputStream buffer = null;
            buffer = new BufferedInputStream(new FileInputStream(nowMusic.getUrl()));
            myPlayer = new javazoom.jl.player.Player(buffer);
            //myPlayer.play();

            myPlayer.close();
            buffer.close();
            playAuto();
        } catch (JavaLayerException | IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isComplete() {
        return myPlayer.isComplete();
    }

    public void close() {
        nowMusic = null;
        myPlayer.close();
    }

    public int getWhere() {
        return myPlayer.getPosition();
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

    public void clearAll() {
        playList.clear();
        nowMusic = null;
    }

//    public boolean isPaused() {
//        return paused;
//    }
//
//    public void setPaused(boolean paused) {
//        this.paused = paused;
//

//    @SuppressWarnings("deprecation")
//	public void pause(){
////        startPosition = getStartPosition(getNowMusicTime());
//        isPaused = true;
//        if (myAdvancedPlayer != null)
//            myAdvancedPlayer.setClosed(true);
//        try {
//            playThread.stop();
//            AdvancedPlayerThread.stop();
//        }catch (NullPointerException e){
//            e.printStackTrace();
//        }
//    }

    //切换歌单时调用
    public void changePlayList() {
        playList.clear();
        playList.addAll(nowList);
    }

    public void changeNowList(List<Music> newList) {
        nowList.clear();
        nowList.addAll(newList);
    }
}