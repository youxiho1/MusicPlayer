package com.yang.view.bottom;

import com.yang.model.Music;
import com.yang.util.ContentValues;
import com.yang.util.Player;
import com.yang.util.SQLiteDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Operation extends JPanel {
    private static Operation operation;
    private JLabel label_name;
    private JLabel label_singer;
    private JButton btn_prev;
    private JButton btn_play;
    private JButton btn_next;
    private JButton btn_like;
    private JButton btn_mode;
    private JSlider slider;

    public static Operation getInstance() {
        if(operation == null) {
            operation =  new Operation();
        }
        return operation;
    }

    private Operation() {
        setLayout(new FlowLayout());
        label_name = new JLabel();
        label_name.setText("");

        label_singer = new JLabel();
        label_singer.setText("");

        //label初始化

        btn_prev = new JButton("上一首");
        btn_play = new JButton("播放");
        btn_next = new JButton("下一首");
        btn_like = new JButton("喜欢");

        //btn_like初始化???????????
        //???????????????????????

        slider = new JSlider(0, 100, 0);
        slider.setPreferredSize(new Dimension(700, 60));

        btn_mode = new JButton("模式切换");
        Player.MODE mode = Player.MODE.ORDER;
        Player.getInstance().setPlayMode(mode);

        btn_prev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player player = Player.getInstance();
                player.playPrev();
                Music music = player.getNowMusic();
                changeMusicInformation(music);
            }
        });


        btn_play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });


        btn_next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player player = Player.getInstance();
                player.playNext();
                Music music = player.getNowMusic();
                changeMusicInformation(music);
            }
        });


        //模式切换按钮文字初始化？？？》持久化存储？？？

        btn_mode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //切换ui
                Player player = Player.getInstance();
                Player.MODE mode = player.getPlayMode();
                if(mode == Player.MODE.SINGLE) {
                    player.setPlayMode(Player.MODE.ORDER);
                }
                else if(mode == Player.MODE.ORDER) {
                    player.setPlayMode(Player.MODE.RANDOM);
                }
                else {
                    player.setPlayMode(Player.MODE.SINGLE);
                }
            }
        });

        btn_like.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SQLiteDatabase db = new SQLiteDatabase("music.db");
                Player player = Player.getInstance();
                Music music = player.getNowMusic();
                String md5value = music.getMd5value();
                List<Music> result = db.query(Music.class, "Music", new String[] {"isLike"}, "md5value=?", new String[] {md5value});
                if(result != null) {
                    music = result.get(0);
                }
                int is_liked = music.getIslike();
                ContentValues values = new ContentValues();
                int res = db.update("Music", values, "md5value=?", new String[] {md5value});
                if(res != -1 && is_liked == 0) {
                    btn_like.setText("喜欢");
                    values.put("isLike", 1);
                }
                else if(res != -1 && is_liked == 1){
                    btn_like.setText("取消喜欢");
                    values.put("isLike", 0);
                }
            }
        });

        this.add(label_name);
        this.add(label_singer);
        this.add(btn_like);
        this.add(btn_prev);
        this.add(btn_play);
        this.add(btn_next);
        this.add(slider);
        this.add(btn_mode);
        this.add(btn_like);
    }

    public void changeMusicInformation(Music music) {
        label_name.setText(music.getName());
        label_singer.setText(music.getSinger());
        if(music.getIslike() == 0) {
            btn_like.setText("喜欢");
        }
        else {
            btn_like.setText("取消喜欢");
        }
        String md5value = music.getMd5value();
        SQLiteDatabase db = new SQLiteDatabase("music.db");
        List<Music> result = db.query(Music.class, "Music", new String[] {"isLike"}, "md5value=?", new String[] {md5value});
        if(result != null) {
            music = result.get(0);
        }
        int is_liked = music.getIslike();
        if(is_liked == 0) {
            btn_like.setText("喜欢");
        }
        else {
            btn_like.setText("取消喜欢");
        }
    }
}
