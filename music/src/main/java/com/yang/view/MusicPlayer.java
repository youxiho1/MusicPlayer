package com.yang.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.yang.model.MusicSheet;
import com.yang.util.SQLiteDatabase;
import com.yang.view.bottom.Operation;
import com.yang.view.west.LocalMusicSheetPanel;
import com.yang.view.west.StarMusicSheetPanel;

public class MusicPlayer extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	
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

        //North
        
        //South
        Operation operation = Operation.getInstance();

        //Finally
        add(BorderLayout.WEST, westPanel);
        add(BorderLayout.SOUTH, operation);
	}

}
