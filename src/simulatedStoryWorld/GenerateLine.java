/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulatedStoryWorld;

/**
 *
 * @author Hong Yu
 */
import java.io.*;

public class GenerateLine {
    
    public static void main(String[] args){
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter("Tag.txt"));
            for(int i = 0; i < 326; i++){
                bw.write(""+i+":");
                bw.newLine();
            }
            bw.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
