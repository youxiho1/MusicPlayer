package com.yang.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.yang.model.MusicSheet;
import com.yang.util.ContentValues;
import com.yang.util.SQLiteDatabase;
import com.yang.view.bottom.Operation;
import com.yang.view.center.MusicSheetInformation;
import com.yang.view.center.MusicSheetInformation;
import com.yang.view.west.LocalMusicSheetPanel;
import com.yang.view.west.StarMusicSheetPanel;
import org.apache.commons.codec.digest.DigestUtils;

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
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setBounds(100, 100, 450, 300);
//		contentPane = new JPanel();
//		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
//		contentPane.setLayout(new BorderLayout(0, 0));
//		setContentPane(contentPane);
		centerPanel = new JPanel();
		SQLiteDatabase db = new SQLiteDatabase("music.db");
        /*final String CREATE_MUSICSHEET = "create table if not exists MusicSheet ("
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
        ContentValues values = new ContentValues();
        values.put("name", "测试歌单1");
        values.put("creator", "lly1");
        values.put("dateCreated", "2018/1/1");
        values.put("flag", 1);
        values.put("creatorId", "17020031025");
        db.insert("MusicSheet", values);
        values.clear();
        values.put("name", "测试歌单2");
        values.put("creator", "李林宇");
        values.put("dateCreated", "2018/1/2");
        values.put("flag", 1);
        values.put("creatorId", "17020031119");
        db.insert("MusicSheet", values);
        values.clear();
        values.put("name", "测试歌单3");
        values.put("creator", "TESTER");
        values.put("dateCreated", "2018/1/30");
        values.put("flag", 2);
        values.put("creatorId", "00000");
        db.insert("MusicSheet", values);
        values.clear();
        values.put("name", "测试歌单4");
        values.put("creator", "TESTER2");
        values.put("dateCreated", "2018/1/30");
        values.put("flag", 1);
        values.put("creatorId", "00100");
        db.insert("MusicSheet", values);
        values.clear();
        values.put("name", "测试歌单5");
        values.put("creator", "TESTERY");
        values.put("dateCreated", "2018/1/30");
        values.put("flag", 2);
        values.put("creatorId", "00100");
        db.insert("MusicSheet", values);
        values.clear();

        values.put("url", "/home/riddleli/Kalafina - to the beginning.mp3");
        values.put("name", "to the beginning");
        values.put("singer", "Kalafina");
        try {
            values.put("md5value", DigestUtils.md5Hex(new FileInputStream("/home/riddleli/Kalafina - to the beginning.mp3")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.insert("Music", values);
        values.clear();
        values.put("url", "/home/riddleli//home/riddleli/春奈るな - 空は高く風は歌う.mp3");
        values.put("name", "空は高く風は歌う");
        values.put("singer", "春奈るな");
        try {
            values.put("md5value", DigestUtils.md5Hex(new FileInputStream("/home/riddleli/春奈るな - 空は高く風は歌う.mp3")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.insert("Music", values);
        values.clear();
        values.put("url", "/home/riddleli/杉森雅和 - 尋問 ～アレグロ 2001.mp3");
        values.put("name", "尋問 ～アレグロ 2001");
        values.put("singer", "杉森雅和");
        try {
            values.put("md5value", DigestUtils.md5Hex(new FileInputStream("/home/riddleli/春奈るな - 空は高く風は歌う.mp3")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.insert("Music", values);
        values.clear();

        values.put("musicsheetId", 1);
        values.put("id", 1);
        db.insert("MusicSheet_Music", values);
        values.clear();
        values.put("musicsheetId", 1);
        values.put("id", 2);
        db.insert("MusicSheet_Music", values);
        values.clear();
        values.put("musicsheetId", 1);
        values.put("id", 3);
        db.insert("MusicSheet_Music", values);
        values.clear();
        values.put("musicsheetId", 2);
        values.put("id", 2);
        db.insert("MusicSheet_Music", values);
        values.clear();
        values.put("musicsheetId", 2);
        values.put("id", 3);
        db.insert("MusicSheet_Music", values);
        values.clear();
        values.put("musicsheetId", 5);
        values.put("id", 3);
        db.insert("MusicSheet_Music", values);*/


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
		final List<MusicSheet> starMusicSheetList = db.rawQuery(MusicSheet.class, "select * from MusicSheet where flag = ?", new String[] {"2"});
		LocalMusicSheetPanel localMusicSheetPanel = new LocalMusicSheetPanel(localMusicSheetList, this);
		StarMusicSheetPanel starMusicSheetPanel = new StarMusicSheetPanel(starMusicSheetList, this);
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
        
//    	ImageIcon icon = new ImageIcon("resources\\min.png");
//    	JButton min = new JButton(icon);
//        min.setOpaque(false);//设置控件是否透明，true为不透明，false为透明
//        min.setContentAreaFilled(false);//设置图片填满按钮所在的区域
//        min.setFocusPainted(false);//设置这个按钮是不是获得焦点
//        min.setBorderPainted(false);//设置是否绘制边框
//        min.setBorder(null);//设置边框
//        northPanel.add(min);
//
//        ImageIcon icon2 = new ImageIcon("resources\\close.png");
//    	JButton close = new JButton(icon2);
//    	close.setOpaque(false);
//    	close.setContentAreaFilled(false);
//    	close.setFocusPainted(false);
//    	close.setBorderPainted(false);
//    	close.setBorder(null);
//        northPanel.add(close);

		//Finally
		add(BorderLayout.WEST, westPanel);
		add(BorderLayout.SOUTH, operation);
		add(BorderLayout.CENTER, centerPanel);
		add(BorderLayout.NORTH, northPanel);
		
//		JButton btn = new JButton("有毒");
//		add(BorderLayout.NORTH, btn);
//		btn.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				//changeCenterPanel(null);
//				MusicSheet musicSheet = localMusicSheetList.get(1);
//				System.out.println(musicSheet.getName());
//				MusicSheetInformation musicSheetInformation = new MusicSheetInformation(musicSheet);
//				//MusicPlayer.getInstance().changeCenterPanel(musicSheetInformation);
//				changeCenterPanel(musicSheetInformation);
//			}
//		});
	}

	public void changeCenterPanel(MusicSheetInformation musicSheetInformation) {
		centerPanel.removeAll();
		centerPanel = musicSheetInformation;
		add(BorderLayout.CENTER, centerPanel);
		revalidate();
	}

}
