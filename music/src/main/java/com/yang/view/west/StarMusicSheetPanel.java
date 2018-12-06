package com.yang.view.west;

import com.yang.model.MusicSheet;
import com.yang.util.ContentValues;
import com.yang.util.DateUtil;
import com.yang.util.SQLiteDatabase;
import com.yang.view.MusicPlayer;
import com.yang.view.center.MusicSheetInformation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class StarMusicSheetPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static StarMusicSheetPanel starMusicSheetPanel;
    private JList<String> list;
    private List<MusicSheet> starMusicSheetList;
    private Vector<String> data;

    public static StarMusicSheetPanel getInstance() {
        if(starMusicSheetPanel == null) {
            starMusicSheetPanel = new StarMusicSheetPanel(null, null);
        }
        return starMusicSheetPanel;
    }

    public static StarMusicSheetPanel getInstance(List<MusicSheet> musicSheetList, final MusicPlayer musicPlayer) {
        starMusicSheetPanel = new StarMusicSheetPanel(musicSheetList, musicPlayer);
        return starMusicSheetPanel;
    }

    private StarMusicSheetPanel(List<MusicSheet> musicSheetList, final MusicPlayer musicPlayer) {
        starMusicSheetList = musicSheetList;
        System.out.println(starMusicSheetList.size());
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);
        //setPreferredSize(new Dimension(250, 300));
        JPanel starPanel = new JPanel();
//      starPanel.setPreferredSize(new Dimension(910,33));
        //starPanel.setBackground(new Color(0, 0, 0, 0));
        FlowLayout starLayout = (FlowLayout) starPanel.getLayout();
        starLayout.setAlignment(FlowLayout.LEADING);
        JPanel titlePanel = new JPanel();
        JLabel title = new JLabel("别人都在听");
        Font font = new Font("幼圆", Font.PLAIN, 16);//创建1个字体实例
        title.setFont(font);//设置JLabel的字体
        Font font1 = new Font("幼圆", Font.PLAIN, 14);
        Font font2 = new Font("微软雅黑", Font.PLAIN, 14);
        ImageIcon ic_add = new ImageIcon("resources/add.png");

        titlePanel.add(title);

        int size = (musicSheetList == null)? 0 : musicSheetList.size();
        if(size > 0) {
            data = new Vector<String>();
            for (int i = 0; i < size; i++) {
                data.add(musicSheetList.get(i).getName());
            }
        }
        //构建一个JList
        list = new JList<String>();
        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component com = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if(isSelected) {
                    com.setBackground(list.getSelectionBackground());
                } else {
                    com.setBackground(new Color(0,0,0,0));
                }
                return com;
            }
        });
        list.setFont(font1);
        list.setBackground(new Color(0, 0, 0, 0));
        //list.setSelectionBackground(new Color(0));
        list.setPreferredSize(new Dimension(170, 150));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setListData(data);

        starPanel.add(titlePanel);
        starPanel.add(list);
        this.add(starPanel);
    }
}