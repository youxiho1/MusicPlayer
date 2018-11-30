package com.yang.view.west;

import com.yang.model.MusicSheet;
import com.yang.view.MusicPlayer;
import com.yang.view.center.MusicSheetInformation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class StarMusicSheetPanel extends JPanel {
    private static StarMusicSheetPanel starMusicSheetPanel;
    private JList<String> list;
    private List<MusicSheet> starMusicSheetList;

    public static StarMusicSheetPanel getInstance() {
        if(starMusicSheetPanel == null) {
            starMusicSheetPanel = new StarMusicSheetPanel(null, null);
        }
        return starMusicSheetPanel;
    }

    public StarMusicSheetPanel(List<MusicSheet> musicSheetList, final MusicPlayer musicPlayer) {
        starMusicSheetList = musicSheetList;
        setPreferredSize(new Dimension(100, 0));
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);
        JLabel title = new JLabel("我收藏的歌单");
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
        list = new JList<String>();
        list.setPreferredSize(new Dimension(400, 100));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setListData(data);

        list.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = list.getSelectedIndex();
                if(index == -1) {
                    JOptionPane.showMessageDialog(null, "您未选中任何歌单", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    MusicSheet musicSheet = starMusicSheetList.get(index);
                    MusicSheetInformation musicSheetInformation = new MusicSheetInformation(musicSheet);
                    musicPlayer.changeCenterPanel(musicSheetInformation);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        this.add(title);
        this.add(list);
    }
}