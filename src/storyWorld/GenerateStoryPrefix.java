/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package storyWorld;
import tools.PrefixUtil;
import java.io.*;
import prefix.*;
import java.util.*;

/**
 *
 * @author Hong Yu
 */
public class GenerateStoryPrefix {
    
    public static void generatePrefix(){
        String path = "ratings";
        File folder = new File(path);
        String[] files = folder.list();
        try{
            ArrayList<Prefix> prefixList = new ArrayList<Prefix>();
		
            
            String line;

            for (int i = 0; i < files.length; i++) {
                BufferedReader br = new BufferedReader(
                      new FileReader(path + "\\" + files[i]));
                while((line = br.readLine()) != null){
                      String[] t = line.split(" ");
                      int[] list = new int[t.length-1];
                      for(int j = 0; j < list.length; j++){
                            list[j] = Integer.parseInt(t[j+1]);
                      }
                      Prefix pi = new Prefix(list);
                      if(Collections.binarySearch(prefixList, pi) < 0){
                            prefixList.add(pi);
                            Collections.sort(prefixList);
                      }
                }
                br.close();
            
            }
            PrefixUtil.writePrefixList(prefixList);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public static void main(String[] args){

        
    }
}
