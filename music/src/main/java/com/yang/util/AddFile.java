package com.yang.util;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class AddFile {
   private JFileChooser chooser = null;
   private File[] files;
   private JPanel jpanel;

   public AddFile(JPanel jpanel) {
       this.jpanel = jpanel;
       chooser = new JFileChooser("");
       chooser.setFileFilter(new FileFilter() {
           @Override
           public boolean accept(File file) {
               if(file.isDirectory())
                   return true;
               return file.getName().endsWith(".mp3");
           }

           @Override
           public String getDescription() {
               return ".mp3";
           }
       });
       chooser.setAcceptAllFileFilterUsed(false);
       chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
       chooser.setMultiSelectionEnabled(true);
   }

   public void open() {
      files = null;
      int result = chooser.showOpenDialog(jpanel);
      if(result == JFileChooser.APPROVE_OPTION) {
    	  files = chooser.getSelectedFiles();
      }
   }

   public String[] getFileNames() {
       String[] names = null;
   		if(files != null) {
   			names = new String[files.length];
   		    for(int i = 0; i < files.length; i++)
   		        names[i] = files[i].getPath();
   		}
   		return names;
   }

   public File[] getFiles() {
   		return files;
   }
}
   
