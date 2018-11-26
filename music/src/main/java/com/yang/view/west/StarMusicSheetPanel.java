package com.yang.view.west;

import com.yang.model.MusicSheet;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StarMusicSheetPanel extends JPanel {
    public StarMusicSheetPanel(List<MusicSheet> musicSheetList) {
        setPreferredSize(new Dimension(100, 0));
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);
        JLabel title = new JLabel("我收藏的歌单");
        this.add(title);
        int size = (musicSheetList == null)? 0 : musicSheetList.size();
        String[] data = null;
        if(size > 0) {
            String[] columnNames = new String[]{""};
            data = new String[size];
            for (int i = 0; i < size; i++) {
                data[i] = musicSheetList.get(i).getName();
            }
        }
        //构建一个JList
        JList<String> list = new JList<String>();
        list.setPreferredSize(new Dimension(200, 100));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setListData(data);
        JScrollPane scrollPane = new JScrollPane(list);
        this.add(scrollPane);
    }
}
