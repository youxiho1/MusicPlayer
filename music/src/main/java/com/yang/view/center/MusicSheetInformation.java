package com.yang.view.center;

import com.yang.model.Music;
import com.yang.model.MusicSheet;
import com.yang.service.ThreadList;
import com.yang.util.AddFile;
import com.yang.util.ContentValues;
import com.yang.service.Player;
import com.yang.util.SQLiteDatabase;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.apache.commons.codec.digest.DigestUtils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicSheetInformation extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static MusicSheetInformation musicSheetInformation;
    private MusicSheet musicSheet;
    private List<Music> preMusic;
    private JTable table; 
    private ImageIcon img;
    private AddFile addFile; 
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

                Component com = super.getTableCellRendererComponent(table, value, 
                        isSelected, hasFocus, row, column);
                return com; 
            }
        };
        cellRenderer.setBackground(c);
        column.setHeaderRenderer(cellRenderer);
    }

    public MusicSheetInformation(MusicSheet nmusicSheet) {
    	setBackground(Color.WHITE);
        this.musicSheet = nmusicSheet;
        JPanel northPanel = new JPanel();
//        northPanel.setPreferredSize(new Dimension(910,33));
        northPanel.setBackground(new Color(244,244,244,244));
        GridLayout northGrid = new GridLayout();
        northGrid.setHgap(10);
        northPanel.setLayout(northGrid);
        
        JPanel panel = new JPanel();
        panel.setBackground(new Color(244,244,244,244));
        FlowLayout layout = (FlowLayout) panel.getLayout();
        layout.setAlignment(FlowLayout.LEADING);
//        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
//		panel.setLayout(layout);
        
        JPanel southPanel = new JPanel();
        southPanel.setPreferredSize(new Dimension(910,440));
        southPanel.setBackground(Color.white);
        BoxLayout southLayout = new BoxLayout(southPanel, BoxLayout.Y_AXIS);
		southPanel.setLayout(southLayout);
        
		img=new ImageIcon("resources/default.jpg");
//		ImageIO.read(new FileInputStream(fnSrc) );//取源图
		int height = 120; 
		int width = img.getIconWidth()*120/img.getIconHeight();//按比例，将高度缩减
		img.setImage(img.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
		JLabel label_pic = new JLabel("");
		label_pic.setIcon(img);
//		label_pic.setSize(new Dimension(80, 80));
        JLabel label_name = new JLabel(musicSheet.getName());
        JLabel label_creator = new JLabel(musicSheet.getCreator());
        JLabel label_createDate = new JLabel(musicSheet.getDatecreated());
        JButton btn_playAll = new JButton("播放全部");
        JButton btn_del = new JButton("删除");
        JButton btn_download = new JButton("下载");
        JButton btn_revise = new JButton("编辑");
        JButton btn_add = new JButton("添加"); 
        btn_add.addActionListener(this);
        label_name.setFont(font1);
        label_creator.setFont(font1);
        label_createDate.setFont(font1);
        btn_playAll.setFont(font);
        btn_playAll.setBackground(Color.white);
        btn_del.setFont(font);
        btn_del.setBackground(Color.white);
        btn_download.setFont(font);
        btn_download.setBackground(Color.white);
        btn_revise.setFont(font);
        btn_revise.setBackground(Color.white);
        btn_add.setFont(font);
        btn_add.setBackground(Color.white);
        panel.add(label_pic);
        northPanel.add(label_name);
        northPanel.add(label_creator);
        northPanel.add(label_createDate);
        northPanel.add(btn_playAll);
        northPanel.add(btn_add);
//        northPanel.add(btn_download);
        northPanel.add(btn_del);
        northPanel.add(btn_revise);
        panel.add(northPanel);
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
        table = new JTable(tableModel) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component component = super.prepareRenderer(renderer, row, column); 
                Object value = getValueAt(row, column); 
                boolean isSelected = false; 
                boolean hasFocus = false; 
                if (!isPaintingForPrint()) { 
                    isSelected = isCellSelected(row, column); 
                    boolean rowIsLead = 
                            (selectionModel.getLeadSelectionIndex() == row); 
                    boolean colIsLead = 
                            (columnModel.getSelectionModel().getLeadSelectionIndex() == column);
                    hasFocus = (rowIsLead && colIsLead) && isFocusOwner(); 
                } 
                if(!hasFocus && !isSelected) { 
                    if (row % 2 == 0) {  //将row改为column，则分列以不同颜色显示 
                        component.setBackground(new Color(244,244,244,244)); 
                    } 
                    if (row % 2 == 1) { 
                        component.setBackground(Color.WHITE); 
                    } 
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
        table.getTableHeader().setReorderingAllowed(false); 
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //table.setSelectionBackground(new Color(0));
        table.addMouseListener(new MouseAdapter() { 
            @Override 
            public void mouseClicked(MouseEvent e) { 
                if(e.getClickCount() == 2) { 
                    int row = table.getSelectedRow(); 
                    Music music = preMusic.get(row);
                    final Player player = Player.getInstance();
                    System.out.println("preMusic:");
                    for (int i = 0; i < preMusic.size(); i++) {
                        System.out.println(preMusic.get(i).getName());
                    }
                    player.changeNowList(preMusic);
                    player.changePlayList();
                    player.setNowMusic(music);
                    Thread thread = new Thread() {
                        public void run() {

                            player.play();
                        }
                    };
                    ArrayList<Thread> threadList = ThreadList.getList();
                    if(threadList.size() == 0) {
                        ThreadList.add(thread);
                    } else {
                        for (Thread thread1 : threadList) {
                            if (thread1.isAlive()) {
                                thread1.stop();
                            }
                        }
                        threadList.add(thread);
                    }
                    thread.start();
                } 
            }
        });
        //封面图？？？
        southPanel.add(table.getTableHeader());
        southPanel.add(table);
        BoxLayout Layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(Layout);
        this.add(panel);
        this.add(southPanel); 
        this.setTableHeaderColor(table,0,Color.WHITE);
        this.setTableHeaderColor(table,1,Color.WHITE);
        this.setTableHeaderColor(table,2,Color.WHITE);
        this.setTableHeaderColor(table,3,Color.WHITE);
    }
    protected Color colorForRow(int row) {
        return (row % 2 == 0) ? Color.RED : Color.PINK;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
    	if(addFile == null)
            addFile = new AddFile(this);
        addFile.open();
        File[] files = addFile.getFiles();
        if(files != null){
            DefaultTableModel dtm = (DefaultTableModel) table.getModel();//获取表格模型
            for (int i = 0; i < files.length; i++) {
                Music music = new Music();
                String name = files[i].getName();
                String singer = name;

                music.setName(name);
                music.setSinger(singer);
                music.setUrl(files[i].getAbsolutePath());
                String md5value = null;
                try {
                    md5value = DigestUtils.md5Hex(new FileInputStream(files[i].getAbsolutePath()));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                music.setMd5value(md5value);
                music.setCount(0);
                music.setIslike(0);
                boolean flag = false;
                for(int j = 0; j < preMusic.size();j++) {
                    //查重
                    Music music1 = preMusic.get(j);
                    if(music.getMd5value().equals(music1.getMd5value())) {
                        flag = true;
                        //弹出提示框？？？
                    }
                }
                if(!flag) {
                    preMusic.add(music);
                    SQLiteDatabase db = new SQLiteDatabase("music.db");
                    List<Music> list = db.query(Music.class, "Music", null, "md5value=?", new String[] {md5value});
                    if(list == null || list.size() == 0) {
                        //插入
                        ContentValues values = new ContentValues();
                        values.put("md5value", md5value);
                        values.put("name", name);
                        values.put("singer", singer);
                        values.put("url", files[i].getAbsolutePath());
                        values.put("count", 0);
                        values.put("islike", 0);
                        db.insert("Music", values);
                        list = db.query(Music.class, "Music", new String[] {"id"}, "md5value=?", new String[] {md5value});
                        System.out.println(list.size());
                    }
                    int id = list.get(0).getId();
                    int sheetId = musicSheet.getId();
                    ContentValues values = new ContentValues();
                    values.put("id", id);
                    values.put("musicsheetid", sheetId);
                    db.insert("Musicsheet_Music", values);
                    String[] nrow = new String[4];
                    nrow[0] = String.valueOf(preMusic.size());
                    nrow[1] = name;
                    nrow[2] = singer;
                    //nrow[3] = ??
                    dtm.addRow(nrow);
                }
            }
            dtm.fireTableStructureChanged();
            this.setTableHeaderColor(table,0,Color.WHITE);
            this.setTableHeaderColor(table,1,Color.WHITE);
            this.setTableHeaderColor(table,2,Color.WHITE);
            this.setTableHeaderColor(table,3,Color.WHITE);
        }
    }
}