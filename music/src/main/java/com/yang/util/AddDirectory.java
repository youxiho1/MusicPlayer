package com.yang.util;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class AddDirectory {
   private JFileChooser chooser = null;
   private File file;
   private JFrame jframe;

   public AddDirectory(JFrame jframe) {
       this.jframe = jframe;
       chooser=new JFileChooser();
       chooser.setFileFilter(new FileFilter() {
           @Override
           public boolean accept(File f) {
               return f.isDirectory();
           }

           @Override
           public String getDescription() {
               return "目录";
           }
       });
       chooser.setAcceptAllFileFilterUsed(false);
       chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
       chooser.setMultiSelectionEnabled(false);
   }

   public void open() {
      file = null;
      int result = chooser.showOpenDialog(jframe);
      if(result == JFileChooser.APPROVE_OPTION){
    	  file = chooser.getSelectedFile();
      }
   }

   public File getFile() {
   		return file;
   }

}
