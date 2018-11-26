package com.yang.view.bottom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Operation extends JPanel {
    public Operation() {
        setLayout(new FlowLayout());

        JButton btn_prev = new JButton("上一首");
        btn_prev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        JButton btn_play = new JButton("播放");
        btn_play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        JButton btn_next = new JButton("下一首");
        btn_next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        JSlider slider = new JSlider(0, 100, 0);
        slider.setPreferredSize(new Dimension(700, 60));

        JButton btn_mode = new JButton("模式切换");

        //模式切换按钮文字初始化？？？》

        btn_mode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        this.add(btn_prev);
        this.add(btn_play);
        this.add(btn_next);
        this.add(slider);
        this.add(btn_mode);
    }
}
