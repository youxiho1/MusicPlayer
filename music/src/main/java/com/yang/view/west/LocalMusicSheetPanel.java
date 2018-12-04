package com.yang.view.west;

import com.yang.model.MusicSheet;
import com.yang.util.ContentValues;
import com.yang.util.DateUtil;
import com.yang.util.SQLiteDatabase;
import com.yang.view.MusicPlayer;
import com.yang.view.center.MusicSheetInformation;
import com.yang.view.center.MusicSheetInformation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Vector;

public class LocalMusicSheetPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static LocalMusicSheetPanel localMusicSheetPanel;
    private JList<String> list;
    private Vector<String> data; 
    private List<MusicSheet> localMusicSheetList;

    public static LocalMusicSheetPanel getInstance() {
        if(localMusicSheetPanel == null) {
            localMusicSheetPanel = new LocalMusicSheetPanel(null, null);
        }
        return localMusicSheetPanel;
    }

    public static LocalMusicSheetPanel getInstance(List<MusicSheet> musicSheetList, final MusicPlayer musicPlayer) { 
        localMusicSheetPanel = new LocalMusicSheetPanel(musicSheetList, musicPlayer); 
        return localMusicSheetPanel; 
    } 
    private LocalMusicSheetPanel(List<MusicSheet> musicSheetList, final MusicPlayer musicPlayer) {
        localMusicSheetList = musicSheetList;
        System.out.println(localMusicSheetList.size()); 
        final BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);
//        setPreferredSize(new Dimension(170, 300));
        //鼠标右键出现对歌单的删除和编辑
        JMenuItem edit = new JMenuItem("编辑");
        JMenuItem del = new JMenuItem("删除");
        final JPopupMenu pop = new JPopupMenu();
        pop.add(edit);
        pop.add(del);
        JPanel localPanel = new JPanel();
//      starPanel.setPreferredSize(new Dimension(910,33));
        localPanel.setBackground(new Color(0, 0, 0, 0));
        FlowLayout localLayout = (FlowLayout) localPanel.getLayout();
        localLayout.setAlignment(FlowLayout.LEADING);
        
        JPanel titlePanel = new JPanel();
        JLabel title = new JLabel("我创建的歌单");
        Font font = new Font("幼圆", Font.PLAIN, 16);//创建1个字体实例
        title.setFont(font);//设置JLabel的字体
        final JLabel op = new JLabel("确认删除？");
        Font font1 = new Font("幼圆", Font.PLAIN, 14);
        Font font2 = new Font("微软雅黑", Font.PLAIN, 14);
        op.setFont(font2);//设置JLabel的字体
        
        ImageIcon ic_add = new ImageIcon("resources/add.png");
		JButton btn_add = new JButton(ic_add);
		btn_add.setOpaque(false);
    	btn_add.setContentAreaFilled(false);
    	btn_add.setFocusPainted(false);
    	btn_add.setBorderPainted(false);
    	btn_add.setBorder(null);
		btn_add.addActionListener(new ActionListener() { 
			@Override 
			public void actionPerformed(ActionEvent e) { 
				String inputValue = JOptionPane.showInputDialog("请输入歌单的名字"); 
				if(inputValue == null || inputValue.length() == 0) { 
					//????????????????? 
				} 
				else { 
					SQLiteDatabase db = new SQLiteDatabase("music.db");
					ContentValues values = new ContentValues(); 
					values.put("name", inputValue); 
					values.put("creatorId", "17020031119"); 
					values.put("creator", "Yi Xiaoyang"); 
					String nowTime = DateUtil.getNowDateTime("yyyy-MM-dd HH:mm:ss"); 
					values.put("dateCreated", nowTime); 
					values.put("flag", 1); 
					db.insert("musicsheet", values); 
					MusicSheet sheet = new MusicSheet(); 
					sheet.setName(inputValue); 
					sheet.setCreatorid("17020031119"); 
					sheet.setCreator("Yi Xiaoyang"); 
					sheet.setDatecreated(nowTime); 
					sheet.setFlag(1); 
					localMusicSheetList.add(sheet); 
					LocalMusicSheetPanel localMusicSheetPanel = LocalMusicSheetPanel.getInstance(); 
					localMusicSheetPanel.addMusicSheet(sheet); 
				} 
			} 
		}); 
		titlePanel.add(title);
		titlePanel.add(btn_add);
        
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
                    com.setBackground(new Color(0, 0, 0, 0)); 
                } 
                return com; 
            } 
        });
        list.setFont(font1);
        list.setBackground(new Color(0, 0, 0, 0));
        list.setSelectionBackground(new Color(0)); 
        list.setPreferredSize(new Dimension(170, 150));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setListData(data);
        list.add(pop);
        
        list.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	if(e.getButton() == 1) { 
                    int index = list.getSelectedIndex();
                    if(index == -1) { 
                        JOptionPane.showMessageDialog(null, "您未选中任何歌单", "提示", JOptionPane.INFORMATION_MESSAGE); 
                    } 
                    else { 
                        MusicSheet musicSheet = localMusicSheetList.get(index); 
                        MusicSheetInformation musicSheetInformation = new MusicSheetInformation(musicSheet); 
                        musicPlayer.changeCenterPanel(musicSheetInformation); 
                    } 
                    revalidate();
                    repaint();
            	}
            }

            @Override
            public void mousePressed(MouseEvent e) {
                  list.setSelectedIndex(list.locationToIndex(e.getPoint()));  //获取鼠标点击的项
                  showpop(e);
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
            //弹出菜单
            public void showpop(MouseEvent e){
                if(e.getButton() == 3 && list.getSelectedIndex() >=0){
                        Object selected = list.getModel().getElementAt(list.getSelectedIndex());
                        System.out.println(selected);
                        pop.show(list, e.getX(), e.getY());
                }
            }
        });
     
        //删除和编辑对应事件
        edit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                

            }
        });
        del.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, op, "提示",JOptionPane.YES_NO_CANCEL_OPTION );
                System.out.println("选择结果:"+result);
            }
        });

    
        localPanel.add(titlePanel);
        localPanel.add(list);
        this.add(localPanel);
    }
    public void addMusicSheet(MusicSheet sheet) { 
        System.out.println(localMusicSheetList.size()); 
        localMusicSheetList.add(sheet); 
        data.add(sheet.getName()); 
        list.setListData(data); 
    } 
}