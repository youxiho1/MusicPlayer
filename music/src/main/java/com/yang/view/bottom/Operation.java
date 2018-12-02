package com.yang.view.bottom;

import com.yang.model.Music;
import com.yang.util.ContentValues;
import com.yang.util.Player;
import com.yang.util.SQLiteDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Operation extends JPanel {
    private static Operation operation;
    private JLabel label_name;
    private JLabel label_singer;
    private JButton btn_prev;
    private JButton btn_play;
    private JButton btn_next;
    private JButton btn_like;
    private JButton btn_mode;
    private JSlider slider;
    private ImageIcon icon4 = new ImageIcon("resources\\unlike.png");
	private ImageIcon icon8 = new ImageIcon("resources\\like.png");

    public static Operation getInstance() {
        if(operation == null) {
            operation =  new Operation();
        }
        return operation;
    }

    private Operation() {
        setLayout(new FlowLayout());
        setBackground(new Color(233,233,233));
        setBorder(BorderFactory.createLineBorder(new Color(191,191,191)));//设置边框
        label_name = new JLabel();
        label_name.setText("");

        label_singer = new JLabel();
        label_singer.setText("");

        //label初始化

        ImageIcon icon1 = new ImageIcon("resources\\prev1.png");
    	JButton btn_prev = new JButton(icon1);
    	btn_prev.setOpaque(false);//设置控件是否透明，true为不透明，false为透明
    	btn_prev.setContentAreaFilled(false);//设置图片填满按钮所在的区域
    	btn_prev.setFocusPainted(false);//设置这个按钮是不是获得焦点
        btn_prev.setBorderPainted(false);//设置是否绘制边框
        btn_prev.setBorder(null);//设置边框
        
        ImageIcon icon2 = new ImageIcon("resources\\play1.png");
    	JButton btn_play = new JButton(icon2);
    	btn_play.setOpaque(false);
    	btn_play.setContentAreaFilled(false);
    	btn_play.setFocusPainted(false);
    	btn_play.setBorderPainted(false);
    	btn_play.setBorder(null);
        
        ImageIcon icon3 = new ImageIcon("resources\\next1.png");
    	JButton btn_next = new JButton(icon3);
    	btn_next.setOpaque(false);
    	btn_next.setContentAreaFilled(false);
    	btn_next.setFocusPainted(false);
    	btn_next.setBorderPainted(false);
    	btn_next.setBorder(null);
    
    	final JButton btn_like = new JButton(icon4);
    	btn_like.setOpaque(false);
    	btn_like.setContentAreaFilled(false);
    	btn_like.setFocusPainted(false);
    	btn_like.setBorderPainted(false);
    	btn_like.setBorder(null);
        //btn_like初始化???????????

        slider = new JSlider(0, 100, 0);
        slider.setPreferredSize(new Dimension(700, 60));
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
        ImageIcon icon5 = new ImageIcon("resources\\single.png");
        ImageIcon icon6 = new ImageIcon("resources\\order.png");
        ImageIcon icon7 = new ImageIcon("resources\\random.png");
    	JButton btn_mode = new JButton(icon5);
    	btn_mode.setOpaque(false);
    	btn_mode.setContentAreaFilled(false);
    	btn_mode.setFocusPainted(false);
    	btn_mode.setBorderPainted(false);
    	btn_mode.setBorder(null);
        Player.MODE mode = Player.MODE.ORDER;
        Player.getInstance().setPlayMode(mode);

        btn_prev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player player = Player.getInstance();
                player.playPrev();
                Music music = player.getNowMusic();
                changeMusicInformation(music);
            }
        });


        btn_play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });


        btn_next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player player = Player.getInstance();
                player.playNext();
                Music music = player.getNowMusic();
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
                }
                else if(mode == Player.MODE.ORDER) {
                    player.setPlayMode(Player.MODE.RANDOM);
                }
                else {
                    player.setPlayMode(Player.MODE.SINGLE);
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
                    btn_like.setIcon(icon8);
                    values.put("isLike", 1);
                }
                else if(res != -1 && is_liked == 1){
                    btn_like.setIcon(icon4);
                    values.put("isLike", 0);
                }
            }
        });

        this.add(label_name);
        this.add(label_singer);
        this.add(btn_like);
        this.add(btn_prev);
        this.add(btn_play);
        this.add(btn_next);
        this.add(slider);
        this.add(btn_mode);
        this.add(btn_like);
    }

    public void changeMusicInformation(Music music) {
        label_name.setText(music.getName());
        label_singer.setText(music.getSinger());
        if(music.getIslike() == 0) {
        	btn_like.setIcon(icon8);
        }
        else {
            btn_like.setIcon(icon4);
        }
        String md5value = music.getMd5value();
        SQLiteDatabase db = new SQLiteDatabase("music.db");
        List<Music> result = db.query(Music.class, "Music", new String[] {"isLike"}, "md5value=?", new String[] {md5value});
        if(result != null) {
            music = result.get(0);
        }
        int is_liked = music.getIslike();
        if(is_liked == 0) {
        	btn_like.setIcon(icon8);
        }
        else {
        	btn_like.setIcon(icon4);
        }
    }
}
