package com.yang.view;

import com.yang.model.Music;
import com.yang.model.MusicSheet;
import com.yang.util.SQLiteDatabase;
import com.yang.view.bottom.Operation;
import com.yang.view.west.LocalMusicSheetPanel;
import com.yang.view.west.StarMusicSheetPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayer extends JFrame {

    public MusicPlayer() {
        SQLiteDatabase db = new SQLiteDatabase("music.db");
        final String CREATE_MUSICSHEET = "create table if not exists MusicSheet ("
                + "id integer primary key autoincrement, "
                + "uuid text, "
                + "name text, "
                + "creatorId text, "
                + "creator text, "
                + "dateCreated text, "
                + "picture text, "
                + "flag integer)";
        final String CREATE_MUSIC = "create table if not exists Music ("
                + "id integer primary key autoincrement, "
                + "md5value text, "
                + "name text, "
                + "singer text, "
                + "url text, "
                + "count integer, "
                + "isLike integer)";
        db.executeSQL(CREATE_MUSICSHEET);
        db.executeSQL(CREATE_MUSIC);

        setTitle("音乐播放器");
        setSize(1100,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //West
        JPanel westPanel = new JPanel();
        BoxLayout westLayout = new BoxLayout(westPanel, BoxLayout.Y_AXIS);
        westPanel.setLayout(westLayout);
        List<MusicSheet> localMusicSheetList = db.rawQuery(MusicSheet.class, "select * from MusicSheet where flag = ?", new String[] {"1"});
        List<MusicSheet> starMusicSheetList = db.rawQuery(MusicSheet.class, "select * from MusicSheet where flag = ?", new String[] {"2"});
        LocalMusicSheetPanel localMusicSheetPanel = new LocalMusicSheetPanel(localMusicSheetList);
        StarMusicSheetPanel starMusicSheetPanel = new StarMusicSheetPanel(starMusicSheetList);
        westPanel.add(localMusicSheetPanel);
        westPanel.add(starMusicSheetPanel);


        //South
        Operation operation = Operation.getInstance();

        //Finally
        add(BorderLayout.WEST, westPanel);
        add(BorderLayout.SOUTH, operation);

    }

    public static void main(String args[]) {
        new MusicPlayer().setVisible(true);
    }
}
