package com.yang.view.bottom;

import com.yang.model.Music;
import com.yang.service.ThreadList;
import com.yang.util.ContentValues;
import com.yang.service.Player;
import com.yang.util.SQLiteDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Operation extends JPanel {
    private JLabel label_name;
    private JLabel label_singer;
    private JButton btn_like;
    private JSlider slider;
    private ImageIcon ic_unlike = new ImageIcon("resources/unlike.png");
	private ImageIcon ic_like = new ImageIcon("resources/like.png");
	private ImageIcon ic_single = new ImageIcon("resources/single.png");
    private ImageIcon ic_order = new ImageIcon("resources/order.png");
    private ImageIcon ic_random = new ImageIcon("resources/random.png");
    private Font font = new Font("幼圆", Font.PLAIN, 13);

    public Operation() {
        setLayout(new FlowLayout());
        setBackground(new Color(244,244,244,244));
        setBorder(BorderFactory.createLineBorder(new Color(219,219,219)));//设置边框
        
        JPanel westPanel = new JPanel();
        westPanel.setPreferredSize(new Dimension(230,35));
        westPanel.setBackground(new Color(244,244,244,244));
        label_name = new JLabel();
        label_name.setText("");
        label_name.setFont(font);
//        label_name.setPreferredSize(new Dimension(50,50));

        label_singer = new JLabel();
        label_singer.setText("");
//        label_singer.setPreferredSize(new Dimension(50,50));
        westPanel.add(label_name);
//        westPanel.add(label_singer);
        JPanel eastPanel = new JPanel();
        eastPanel.setBackground(new Color(244,244,244,244));
        GridLayout eastGrid = new GridLayout();
        eastGrid.setHgap(20);
        eastPanel.setLayout(eastGrid);
        
        //label初始化
        ImageIcon ic_prev = new ImageIcon("resources/prev1.png");
    	JButton btn_prev = new JButton(ic_prev);
    	setButtonStyle(btn_prev);
        
        ImageIcon ic_play = new ImageIcon("resources/play1.png");
    	JButton btn_play = new JButton(ic_play);
    	setButtonStyle(btn_play);
        
        ImageIcon icon3 = new ImageIcon("resources/next1.png");
    	JButton btn_next = new JButton(icon3);
    	setButtonStyle(btn_next);
    
    	btn_like = new JButton(ic_like);
    	setButtonStyle(btn_like);
        //btn_like初始化???????????

        slider = new JSlider(0, 100, 0);
        slider.setPreferredSize(new Dimension(600, 60));
        slider.setOpaque(false);
        slider.setUI(new  javax.swing.plaf.metal.MetalSliderUI(){
            @Override
            public void paintThumb(Graphics g) {
                //绘制指示物
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(23,171,227));
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.fillOval(thumbRect.x, thumbRect.y, thumbRect.width,
                        thumbRect.height);//修改为圆形
                //也可以帖图(利用鼠标事件转换image即可体现不同状态)
                //g2d.drawImage(image, thumbRect.x, thumbRect.y, thumbRect.width,thumbRect.height,null);

            }
            public void paintTrack(Graphics g) {
                //绘制刻度的轨迹
                int cy,cw;
                Rectangle trackBounds = trackRect;
                if (slider.getOrientation() == JSlider.HORIZONTAL) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setPaint(new Color(248,248,248));
                    cy = (trackBounds.height/2) - 2;
                    cw = trackBounds.width;

                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.translate(trackBounds.x, trackBounds.y + cy);
                    g2.fillRect(0, -cy + 5, cw, cy);

                    int trackLeft = 0;
                    int trackRight = 0;
                    trackRight = trackRect.width - 1;

                    int middleOfThumb = 0;
                    int fillLeft = 0;
                    int fillRight = 0;
                    //换算坐标
                    middleOfThumb = thumbRect.x + (thumbRect.width / 2);
                    middleOfThumb -= trackRect.x;

                    if (!drawInverted()) {
                        fillLeft = !slider.isEnabled() ? trackLeft : trackLeft + 1;
                        fillRight = middleOfThumb;
                        } else {
                        fillLeft = middleOfThumb;
                        fillRight = !slider.isEnabled() ? trackRight - 1
                        : trackRight - 2;
                        }
                    //设定渐变
                    g2.setPaint(new Color(125,197,235));
                    g2.fillRect(0, -cy + 5, fillRight - fillLeft, cy);

                    g2.setPaint(slider.getBackground());
                    g2.fillRect(10, 10, cw, 5);

                    g2.setPaint(new Color(125,197,235));
                    g2.drawLine(0, cy, cw - 1, cy);

                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_OFF);
                    g2.translate(-trackBounds.x, -(trackBounds.y + cy));                    
                }
                else {
                    super.paintTrack(g);
                    }
            }

});

        //btn_mode = new JButton("模式切换");
    	final JButton btn_mode = new JButton(ic_order);
    	setButtonStyle(btn_mode);
        Player.MODE mode = Player.MODE.ORDER;
        Player.getInstance().setPlayMode(mode);

        btn_prev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final Player player = Player.getInstance();
                Thread thread = new Thread() {
                    public void run() {
                        player.playPrev();
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
                Music music = player.getNowMusic();
                if(player.getNowMusic() == null)
                    return;
                changeMusicInformation(music);
            }
        });


        btn_play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//            	 Player player = Player.getInstance();
//            	 if(player.isPaused())
//            	 player.play();
//            	 else player.pause();
//                 if(player.getNowMusic() == null)
//                 	return;
            }
        });


        btn_next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final Player player = Player.getInstance();
                Thread thread = new Thread() {
                    public void run() {
                        player.playNext();
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
                Music music = player.getNowMusic();
                if(player.getNowMusic() == null)
                	return;
                changeMusicInformation(music);
            }
        });


        //模式切换按钮文字初始化？？？》持久化存储？？？

        btn_mode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //切换ui
                Player player = Player.getInstance();
                Player.MODE mode = player.getPlayMode();
                if(mode == Player.MODE.SINGLE) {
                    player.setPlayMode(Player.MODE.ORDER);
                    btn_mode.setIcon(ic_order);
                }
                else if(mode == Player.MODE.ORDER) {
                    player.setPlayMode(Player.MODE.RANDOM);
                    btn_mode.setIcon(ic_random);
                }
                else {
                    player.setPlayMode(Player.MODE.SINGLE);
                    btn_mode.setIcon(ic_single);
                }
            }
        });

        btn_like.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SQLiteDatabase db = new SQLiteDatabase("music.db");
                Player player = Player.getInstance();
                Music music = player.getNowMusic();
                String md5value = music.getMd5value();
                List<Music> result = db.query(Music.class, "Music", new String[] {"isLike"}, "md5value=?", new String[] {md5value});
                if(result != null) {
                    music = result.get(0);
                }
                int is_liked = music.getIslike();
                ContentValues values = new ContentValues();
                int res = db.update("Music", values, "md5value=?", new String[] {md5value});
                if(res != -1 && is_liked == 0) {
                    btn_like.setIcon(ic_like);
                    values.put("isLike", 1);
                }
                else if(res != -1 && is_liked == 1){
                    btn_like.setIcon(ic_unlike);
                    values.put("isLike", 0);
                }
            }
        });

        eastPanel.add(btn_mode);
        eastPanel.add(btn_like);
        this.add(westPanel);
//        this.add(label_name);
//        this.add(label_singer);
        this.add(btn_prev);
        this.add(btn_play);
        this.add(btn_next);
        this.add(slider);
        this.add(eastPanel);
    }

    public void changeMusicInformation(Music music) {
    	System.out.println(music.getName());
        label_name.setText(music.getName());
        label_singer.setText(music.getSinger());
        if(music.getIslike() == 0) {
        	btn_like.setIcon(ic_like);
        }
        else {
            btn_like.setIcon(ic_unlike);
        }
        String md5value = music.getMd5value();
        SQLiteDatabase db = new SQLiteDatabase("music.db");
        List<Music> result = db.query(Music.class, "Music", new String[] {"isLike"}, "md5value=?", new String[] {md5value});
        if(result != null) {
            music = result.get(0);
        }
        int is_liked = music.getIslike();
        if(is_liked == 0) {
        	btn_like.setIcon(ic_like);
        }
        else {
        	btn_like.setIcon(ic_unlike);
        }
    }
    public void setButtonStyle(JButton btn) {
    	btn.setOpaque(false);
    	btn.setContentAreaFilled(false);
    	btn.setFocusPainted(false);
    	btn.setBorderPainted(false);
    	btn.setBorder(null);
    }
}
