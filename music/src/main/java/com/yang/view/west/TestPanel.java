package com.yang.view.west;

import javax.swing.*;

public class TestPanel extends JPanel {
    private static TestPanel testPanel;

    public static TestPanel getInstance() {
        if(testPanel == null) {
            testPanel = new TestPanel();
        }
        return testPanel;
    }

    private TestPanel() {
        JButton btn = new JButton("添加新歌单");

    }
    public static void main(String args[]) {  
    }
}
