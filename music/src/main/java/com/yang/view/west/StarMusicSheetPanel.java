package com.yang.view.west;

import com.yang.model.MusicSheet;
import com.yang.view.MusicPlayer;
import com.yang.view.center.MusicSheetInformation;
import com.yang.view.center.MusicSheetInformation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Vector;

public class StarMusicSheetPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static StarMusicSheetPanel starMusicSheetPanel;
    private JList<String> list;
    private Vector<String> data;
    private List<MusicSheet> starMusicSheetList;
    final JPopupMenu pop;

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
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);
//        setPreferredSize(new Dimension(170, 300));
        JPanel starPanel = new JPanel();
//        starPanel.setPreferredSize(new Dimension(910,33));
        starPanel.setBackground(new Color(0, 0, 0, 0));
        FlowLayout starLayout = (FlowLayout) starPanel.getLayout();
        starLayout.setAlignment(FlowLayout.LEADING);
        
        JMenuItem edit = new JMenuItem("编辑");
        JMenuItem del = new JMenuItem("删除");
        pop = new JPopupMenu();
        pop.add(edit);
        pop.add(del);
        JPanel titlePanel = new JPanel();
        JLabel title = new JLabel("我收藏的歌单");
        Font font = new Font("幼圆", Font.PLAIN, 16);
        title.setFont(font);
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
                    com.setBackground(new Color(0,0,0,0));
                }
                return com;
            }
        });
        list.setFont(font1);
        list.setBackground(new Color(0, 0, 0, 0));
        list.setPreferredSize(new Dimension(170, 150));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setListData(data);
        list.add(pop);
        list.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                
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
            
            public void showpop(MouseEvent e){
                if(e.getButton() == 3 && list.getSelectedIndex() >=0){
                    Object selected = list.getModel().getElementAt(list.getSelectedIndex());
                    System.out.println(selected);
                    pop.show(list, e.getX(), e.getY());
                }
            }
        });
        
        list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				int index = list.getSelectedIndex();
                if(index == -1) {
                    JOptionPane.showMessageDialog(null, "您未选中任何歌单", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    MusicSheet musicSheet = starMusicSheetList.get(index);
                    MusicSheetInformation musicSheetInformation = new MusicSheetInformation(musicSheet);
                    musicPlayer.changeCenterPanel(musicSheetInformation);
                }
                revalidate();
                repaint();
			}
		});

        edit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
        
        del.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int result = JOptionPane.showConfirmDialog(null, op, "提示",JOptionPane.YES_NO_CANCEL_OPTION );
                System.out.println("选择结果:"+result);
			}
		});
        starPanel.add(titlePanel);
        starPanel.add(list);
        this.add(starPanel);
    }
}