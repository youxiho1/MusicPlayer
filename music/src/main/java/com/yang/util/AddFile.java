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

    public static String openPictureChooser() {
        JFileChooser file = new JFileChooser (".");
        file.setAcceptAllFileFilterUsed(false);
        file.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                }
                String fileName = file.getName();
                int index = fileName.lastIndexOf('.');
                if (index > 0 && index < fileName.length() - 1) {
                    String extension = fileName.substring(index + 1).toLowerCase();
                    if (extension.equals("jpg")) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public String getDescription() {
                return ".jpg";
            }
        });
        int result = file.showOpenDialog(null);
        if(result == JFileChooser.APPROVE_OPTION) {
            String path = file.getSelectedFile().getAbsolutePath();
            return path;
        }
        else {
            return "";
        }
    }
}
   
