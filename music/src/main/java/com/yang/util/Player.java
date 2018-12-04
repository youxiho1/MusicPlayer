package com.yang.util;

import com.yang.model.Music;
import com.yang.view.bottom.Operation;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;
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

import javax.swing.JOptionPane;

public class Player {
    public enum MODE {ORDER, RANDOM, SINGLE};
    private MODE playMode;
    private Music nowMusic;
    private List<Music> nowList;
    private List<Music> playList;
    private static Player player;
    private Object playerLock;
    private int startPosition;
    private Thread playThread;
    private Thread AdvancedPlayerThread;
    private AdvancedPlayer advancedPlayer;
    private MyAdvancedPlayer myAdvancedPlayer;
    private volatile boolean isPaused;
    private static Operation operation;

    static {
        player = null;
    }
    public Thread getPlayThread() {
        return playThread;
    }

    public void setPlayThread(Thread playThread) {
        this.playThread = playThread;
    }
    public boolean isPaused() {
        return isPaused;
    }
    @SuppressWarnings("deprecation")
	public void pause(){
//        startPosition = getStartPosition(getNowMusicTime());
        isPaused = true;
        if (myAdvancedPlayer != null)
            myAdvancedPlayer.setClosed(true);
        try {
            playThread.stop();
            AdvancedPlayerThread.stop();
        }catch (NullPointerException e){
        }
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
        playerLock = new Object();
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
    	operation.changeMusicInformation(nowMusic); 
        if(playList == null) {
        	JOptionPane.showMessageDialog(null, "无可播放歌曲", "错误", JOptionPane.ERROR_MESSAGE); 
            return;
        }
        if(playList != null && nowMusic == null) {
            nowMusic = playList.get(0);
        }
        //test
//        isPaused = false;
//        while (!isPaused){
//            synchronized (playerLock){
//                //创建播放解码线程
//                try {
////                	String filePath = System.getProperty("user.dir") + "/demo/";
////                    String fileName = nowMusic.getSinger() + " - " + nowMusic.getName();
//                	BufferedInputStream buffer = new BufferedInputStream(new FileInputStream("C:\\Users\\yang\\Desktop\\demo\\王源 - 一样 [mqms2].mp3"));
//                    if(buffer != null){
//                        myAdvancedPlayer = new MyAdvancedPlayer(buffer, playerLock);
//                        AdvancedPlayerThread = new Thread(myAdvancedPlayer);
//                        AdvancedPlayerThread.start();
//                        //等待解码完毕
//                        playerLock.wait();
//                    }
//                    //播放下一首
//                    if(playMode != MODE.SINGLE)
//                    playAuto();
//                } catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        }
        new Thread(new Runnable() { 
            @Override 
            public void run() { 
                BufferedInputStream buffer = null; 
                try { 
                    buffer = new BufferedInputStream(new FileInputStream(nowMusic.getUrl())); 
                    advancedPlayer = new AdvancedPlayer(buffer);
                    advancedPlayer.play(); 
                } catch (FileNotFoundException | JavaLayerException e) { 
                    e.printStackTrace(); 
                } 
            } 
        }).start(); 
    }

  
    //按照歌单顺序播放下一首
    public void playNext() {
        int size = (playList == null) ? 0 : playList.size();
        if(size == 0) {
        	JOptionPane.showMessageDialog(null, "无可播放歌曲", "错误", JOptionPane.ERROR_MESSAGE); 
        	return;
        }
        pause();
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
        if(size == 0) {
        	JOptionPane.showMessageDialog(null, "无可播放歌曲", "错误", JOptionPane.ERROR_MESSAGE); 
        	return;
        }
        pause();
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
//            case SINGLE:
//                nowMusic = playList.get(0);
//                play();
//                break;
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
    	Collections.shuffle(nowList); 
        playList.clear();
        playList.addAll(nowList);
    }

    public void changeNowList(List<Music> newList) {
        nowList.clear();
        nowList.addAll(newList);
    }
}