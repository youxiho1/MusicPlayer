package com.yang.view.center;

import com.yang.model.Music;
import com.yang.model.MusicSheet;
import com.yang.util.SQLiteDatabase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicSheetInformation extends JPanel {
    private static MusicSheetInformation musicSheetInformation;
    private MusicSheet musicSheet;
    private List<Music> preMusic;

    public static MusicSheetInformation getInstance() {
        return musicSheetInformation;
    }

    public MusicSheetInformation(MusicSheet nmusicSheet) {
        this.musicSheet = nmusicSheet;
        JLabel label_name = new JLabel(musicSheet.getName());
        JLabel label_creator = new JLabel(musicSheet.getCreator());
        JLabel label_createDate = new JLabel(musicSheet.getDatecreated());
        JButton btn_playAll = new JButton("播放全部");
        JButton btn_star = new JButton("收藏");
        JButton btn_download = new JButton("下载全部");
        JButton btn_revise = new JButton("编辑歌单");
        JButton btn_add = new JButton("添加歌曲");
        /*Map<String, String> map = new HashMap<>();
        map = musicSheet.getMusicItems();
        int size = map.size();
        String[][] rowData = new String[size][];
        int i = 0;
        SQLiteDatabase db = new SQLiteDatabase("music.db");
        for(String md5value : map.keySet()) {
            rowData[i][0] = String.valueOf(i + 1);
            rowData[i][1] = map.get(md5value);
            //如果歌曲都在数据库里？
            List<Music> list = db.query(Music.class, "Music", null, "md5value=?", new String[] {md5value});
            if(list != null) {
                Music temp = list.get(0);
                preMusic.add(temp);
                rowData[i][2] = temp.getSinger();
                rowData[i][3] = "";
            }
        }*/
        SQLiteDatabase db = new SQLiteDatabase("music.db");
        preMusic = db.rawQuery(Music.class, "select music.id,md5value,name,singer,url,count,islike from music inner join musicsheet_music on music.id=musicsheet_music.id where musicsheet_music.musicsheetid=?", new String[] {String.valueOf(musicSheet.getId())});
        String[][] rowData = null;
        if(preMusic != null) {
            rowData = new String[preMusic.size()][4];
            int i = 0;
            for(Music music : preMusic) {
                rowData[i][0] = String.valueOf(i + 1);
                rowData[i][1] = music.getName();
                rowData[i][2] = music.getSinger();
                i++;
                //rowData[i][3] =
            }
        }

        String[] columnNames = new String[] {"", "音乐标题", "歌手", "时长"};
        DefaultTableModel tableModel = new DefaultTableModel(rowData, columnNames);
        JTable table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //封面图？？？

        this.add(label_name);
        this.add(label_creator);
        this.add(label_createDate);
        this.add(btn_playAll);
        this.add(btn_download);
        this.add(btn_star);
        this.add(btn_revise);
        this.add(btn_add);
        this.add(table.getTableHeader());
        this.add(table);
    }
}