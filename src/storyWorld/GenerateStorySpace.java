/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package storyWorld;
import java.io.*;
import prefix.*;
import java.util.*;
import tools.*;

/**
 *
 * @author Hong Yu
 */
public class GenerateStorySpace {
        public static void main(String[] args){
        String path = "ratings";
        String storySpace = PrefixUtil.storySpaceFile;
        File folder = new File(path);
        String[] files = folder.list();
        try{
            
            BufferedWriter sw = new BufferedWriter(new FileWriter(storySpace));
            
            String line;

            for (int i = 0; i < files.length; i++) {
                BufferedReader br = new BufferedReader(
                      new FileReader(path + "\\" + files[i]));
                while((line = br.readLine()) != null){
                      String[] t = line.split(" ");
                      int[] list = new int[t.length-1];
                      for(int j = 0; j < list.length; j++){
                            list[j] = Integer.parseInt(t[j+1]);
                            sw.write(""+list[j]);
                            if(j != list.length-1){
                                sw.write("\t");
                            }
                      }
                      sw.newLine();

                }
                br.close();
            
            }
            sw.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
}
