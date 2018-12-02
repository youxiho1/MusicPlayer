package com.yang.view.west;

import com.yang.model.MusicSheet;
import com.yang.view.MusicPlayer;
import com.yang.view.center.MusicSheetInformation;
import com.yang.view.center.MusicSheetInformation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class StarMusicSheetPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);
//        setPreferredSize(new Dimension(170, 300));
        JPanel starPanel = new JPanel();
//        starPanel.setPreferredSize(new Dimension(910,33));
        starPanel.setBackground(new Color(0, 0, 0, 0));
        FlowLayout starLayout = (FlowLayout) starPanel.getLayout();
        starLayout.setAlignment(FlowLayout.LEADING);
        
        JLabel title = new JLabel("我收藏的歌单");
        Font font = new Font("幼圆", Font.PLAIN, 16);
        title.setFont(font);
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
        Font font1 = new Font("幼圆", Font.PLAIN, 14);
        list.setFont(font1);
        list.setBackground(new Color(0, 0, 0, 0));
        list.setPreferredSize(new Dimension(170, 150));
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
        
        starPanel.add(title);
        starPanel.add(list);
        this.add(starPanel);
    }
}