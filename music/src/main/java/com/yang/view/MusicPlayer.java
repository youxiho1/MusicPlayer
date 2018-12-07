package com.yang.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import org.apache.commons.codec.digest.DigestUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yang.model.Music;
import com.yang.model.MusicSheet;
import com.yang.service.Player;
import com.yang.util.ContentValues;
import com.yang.util.SQLiteDatabase;
import com.yang.util.ZoneHttp;
import com.yang.view.bottom.Operation;
import com.yang.view.center.MusicSheetInformation;
import com.yang.view.west.LocalMusicSheetPanel;
import com.yang.view.west.StarMusicSheetPanel;
import okhttp3.Call;
import okhttp3.Response;

public class MusicPlayer extends JFrame {
	private JPanel centerPanel;
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new MusicPlayer().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	/**
	 * Create the frame.
	 */
	public MusicPlayer() {
		centerPanel = new JPanel();
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
        final String CREATE_MUSICSHEET_MUSIC = "create table if not exists MusicSheet_Music ("
                + "rowId integer primary key autoincrement, "
                + "musicsheetId integer, "
                + "id integer)";
        db.executeSQL(CREATE_MUSICSHEET);
        db.executeSQL(CREATE_MUSIC);
        db.executeSQL(CREATE_MUSICSHEET_MUSIC);

        //测试数据，记得删除
//        ContentValues values = new ContentValues();
//        values.put("name", "Test");
//        values.put("creator", "弋晓洋");
//        values.put("dateCreated", "2018/12/1");
//        values.put("flag", 1);
//        values.put("creatorId", "17020031119");
//        db.insert("MusicSheet", values);
//        values.clear();
//        values.put("name", "测试歌单2");
//        values.put("creator", "李林宇");
//        values.put("dateCreated", "2018/1/2");
//        values.put("flag", 1);
//        values.put("creatorId", "17020031119");
//        db.insert("MusicSheet", values);
//        values.clear();
//        values.put("name", "测试歌单3");
//        values.put("creator", "TESTER");
//        values.put("dateCreated", "2018/1/30");
//        values.put("flag", 2);
//        values.put("creatorId", "00000");
//        db.insert("MusicSheet", values);
//        values.clear();
//        values.put("name", "测试歌单4");
//        values.put("creator", "TESTER2");
//        values.put("dateCreated", "2018/1/30");
//        values.put("flag", 1);
//        values.put("creatorId", "00100");
//        db.insert("MusicSheet", values);
//        values.clear();
//        values.put("name", "测试歌单5");
//        values.put("creator", "TESTERY");
//        values.put("dateCreated", "2018/1/30");
//        values.put("flag", 2);
//        values.put("creatorId", "00100");
//        db.insert("MusicSheet", values);
//        values.clear();

//        values.put("url", "C:\\Users\\yang\\Desktop\\demo\\王源 - 一样.mp3");
//        values.put("name", "一样 [mqms2]");
//        values.put("singer", "王源");
//        try {
//            values.put("md5value", DigestUtils.md5Hex(new FileInputStream("C:\\Users\\yang\\Desktop\\demo\\王源 - 一样.mp3")));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        db.insert("Music", values);
//        values.clear();
//
//        values.put("musicsheetId", 1);
//        values.put("id", 1);
//        db.insert("MusicSheet_Music", values);
//        values.clear();
//        values.put("musicsheetId", 1);
//        values.put("id", 2);
//        db.insert("MusicSheet_Music", values);
//        values.clear();
//        values.put("musicsheetId", 1);
//        values.put("id", 3);
//        db.insert("MusicSheet_Music", values);
//        values.clear();
//        values.put("musicsheetId", 2);
//        values.put("id", 2);
//        db.insert("MusicSheet_Music", values);
//        values.clear();
//        values.put("musicsheetId", 2);
//        values.put("id", 3);
//        db.insert("MusicSheet_Music", values);
//        values.clear();
//        values.put("musicsheetId", 5);
//        values.put("id", 3);
//        db.insert("MusicSheet_Music", values);


        //setTitle("音乐播放器");
        //setUndecorated(true);
        setSize(1100,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//West
		JPanel westPanel = new JPanel();
		BoxLayout westLayout = new BoxLayout(westPanel, BoxLayout.Y_AXIS);
		westPanel.setLayout(westLayout);
		westPanel.setPreferredSize(new Dimension(170, 0));
		final List<MusicSheet> localMusicSheetList = db.rawQuery(MusicSheet.class, "select * from MusicSheet where flag = ?", new String[] {"1"});
        final List<MusicSheet> starMusicSheetList = new ArrayList<>();
		Map<String, Object> map = new HashMap();
        try {
            String responseData = ZoneHttp.syncGet("http://222.195.146.185:58080/music.server/queryMusicSheets?type=all", map);
            System.out.println(responseData);
            JSONObject jsonObject = JSONObject.parseObject(responseData);
            JSONArray jsonArray = jsonObject.getJSONArray("musicSheetList");
            for(int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                MusicSheet musicSheet = new MusicSheet();
                musicSheet.setName(jsonObject1.getString("name"));
                musicSheet.setDatecreated(jsonObject1.getString("dateCreated"));
                musicSheet.setCreatorid(jsonObject1.getString("creatorId"));
                musicSheet.setCreator(jsonObject1.getString("creator"));
                musicSheet.setId(jsonObject1.getInteger("id"));
                musicSheet.setPicture(jsonObject1.getString("picture"));
                musicSheet.setUuid(jsonObject1.getString("uuid"));
                musicSheet.setFlag(2);
                starMusicSheetList.add(musicSheet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("size" + starMusicSheetList.size());
		LocalMusicSheetPanel localMusicSheetPanel = LocalMusicSheetPanel.getInstance(localMusicSheetList, this);
        StarMusicSheetPanel starMusicSheetPanel = StarMusicSheetPanel.getInstance(starMusicSheetList, this);
        //LocalMusicSheetPanel localMusicSheetPanel = LocalMusicSheetPanel.getInstance(starMusicSheetList, this);
		westPanel.add(localMusicSheetPanel);
        westPanel.add(starMusicSheetPanel);
		//westPanel.add(new JScrollBar());

		//Center
		centerPanel = new JPanel();
		centerPanel.setBackground(Color.WHITE);
		
		//South
		Operation operation = Operation.getInstance();
		
		//North
        JPanel northPanel = new JPanel();
        northPanel.setBackground(new Color(248,248,248,248));
        FlowLayout northLayout = (FlowLayout) northPanel.getLayout();
        northLayout.setAlignment(FlowLayout.LEADING);

        JLabel label = new JLabel("");
        Font font = new Font("幼圆", Font.PLAIN, 18);//创建1个字体实例
        label.setFont(font);//设置JLabel的字体
        label.setIcon(new ImageIcon("resources/main.png"));
        label.setText("音乐播放器");
        northPanel.add(label);
        
        Player player = Player.getInstance(operation); 

		//Finally
		add(BorderLayout.WEST, westPanel);
		add(BorderLayout.SOUTH, operation);
		add(BorderLayout.CENTER, centerPanel);
		add(BorderLayout.NORTH, northPanel);
		
	}

	public void changeCenterPanel(MusicSheetInformation musicSheetInformation) {
		centerPanel.removeAll();
		centerPanel = musicSheetInformation;
		add(BorderLayout.CENTER, centerPanel);
		revalidate();
	}

}