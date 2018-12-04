package com.yang.view.center;

import com.yang.model.Music;
import com.yang.model.MusicSheet;
import com.yang.util.SQLiteDatabase;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicSheetInformation extends JPanel {

	private static final long serialVersionUID = 1L;
	private static MusicSheetInformation musicSheetInformation;
    private MusicSheet musicSheet;
    private List<Music> preMusic;
    private Font font = new Font("幼圆", Font.PLAIN, 16);//创建1个字体实例
    private Font font1 = new Font("幼圆", Font.PLAIN, 18);//创建1个字体实例

    public static MusicSheetInformation getInstance() {
        return musicSheetInformation;
    }
    public void setTableHeaderColor(JTable table, int columnIndex, Color c) {
        TableColumn column = table.getTableHeader().getColumnModel()
                .getColumn(columnIndex);
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            /** serialVersionUID */
            private static final long serialVersionUID = 43279841267L;

            @Override
            public Component getTableCellRendererComponent(JTable table, 
                    Object value, boolean isSelected,boolean hasFocus,
                    int row, int column) {

                setHorizontalAlignment(JLabel.LEFT);// 表格内容居中
                setPreferredSize(new Dimension(910,37));
                ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer())
                        .setHorizontalAlignment(DefaultTableCellRenderer.CENTER);// 列头内容居中

                return super.getTableCellRendererComponent(table, value, 
                        isSelected, hasFocus, row, column);
            }
        };
        cellRenderer.setBackground(c);
        column.setHeaderRenderer(cellRenderer);
    }

    public MusicSheetInformation(MusicSheet nmusicSheet) {
    	setBackground(Color.WHITE);
        this.musicSheet = nmusicSheet;
        JPanel northPanel = new JPanel();
        northPanel.setPreferredSize(new Dimension(910,33));
        northPanel.setBackground(new Color(244,244,244,244));
        GridLayout northGrid = new GridLayout();
        northGrid.setHgap(10);
        northPanel.setLayout(northGrid);
        
        JPanel southPanel = new JPanel();
        southPanel.setPreferredSize(new Dimension(910,440));
        southPanel.setBackground(Color.white);
        BoxLayout southLayout = new BoxLayout(southPanel, BoxLayout.Y_AXIS);
		southPanel.setLayout(southLayout);
        
        JLabel label_name = new JLabel(musicSheet.getName());
        JLabel label_creator = new JLabel(musicSheet.getCreator());
        JLabel label_createDate = new JLabel(musicSheet.getDatecreated());
        JButton btn_playAll = new JButton("播放全部");
        JButton btn_star = new JButton("收藏");
        JButton btn_download = new JButton("下载");
        JButton btn_revise = new JButton("编辑");
        JButton btn_add = new JButton("添加歌曲");
        label_name.setFont(font1);
//        label_name.setIcon(new ImageIcon("resources\\main.png"));
        label_creator.setFont(font1);
        label_createDate.setFont(font1);
        btn_playAll.setFont(font);
        btn_playAll.setBackground(Color.white);
        btn_star.setFont(font);
        btn_star.setBackground(Color.white);
        btn_download.setFont(font);
        btn_download.setBackground(Color.white);
        btn_revise.setFont(font);
        btn_revise.setBackground(Color.white);
        btn_add.setFont(font);
        btn_add.setBackground(Color.white);
        northPanel.add(label_name);
        northPanel.add(label_creator);
        northPanel.add(label_createDate);
        northPanel.add(btn_playAll);
        northPanel.add(btn_add);
        northPanel.add(btn_download);
        northPanel.add(btn_star);
        northPanel.add(btn_revise);
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

        String[] columnNames = new String[] {"", "歌曲", "歌手", "时长"};
        DefaultTableModel tableModel = new DefaultTableModel(rowData, columnNames);
        JTable table = new JTable(tableModel) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				  Component component = super.prepareRenderer(renderer, row, column);
				  if (row % 2 == 0) {  //将row改为column，则分列以不同颜色显示
					  component.setBackground(new Color(244,244,244,244));
					  }
				  if (row % 2 == 1) {
					  component.setBackground(Color.WHITE);
					  }
				  return component;
			}
			@Override
          public boolean isCellEditable(int row, int column) {
              return false;
          }

        };
        
        table.setFont(font);
        //table.setPreferredSize(new Dimension(910,550));
        table.setBackground(Color.WHITE);
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.getTableHeader().setBorder(new EmptyBorder(0, 0, 0, 0));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //封面图？？？
        southPanel.add(table.getTableHeader());
        southPanel.add(table);
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);
        this.add(northPanel);
        this.add(southPanel); 
        this.setTableHeaderColor(table,0,Color.WHITE);
        this.setTableHeaderColor(table,1,Color.WHITE);
        this.setTableHeaderColor(table,2,Color.WHITE);
        this.setTableHeaderColor(table,3,Color.WHITE);
    }
    protected Color colorForRow(int row) {
        return (row % 2 == 0) ? Color.RED : Color.PINK;
    }

}