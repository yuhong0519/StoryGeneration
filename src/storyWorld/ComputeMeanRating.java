/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package storyWorld;

import tools.PrefixUtil;
import java.util.*;

import gridWorld.GenerateGridStorySpace;
import PPCA.*;
import cfcontrol.Test;
import prefix.*;
import nmf.*;
import no.uib.cipr.matrix.*;
import java.io.*;

public class ComputeMeanRating {
    static int numRandom = 5, numRecommend = 5;
    
    static double[] calculateMean(String file, BufferedWriter bw) throws IOException{
        double[] means = new double[3];
        double[] temp = new double[4];
        
        ArrayList<Prefix> result = PrefixUtil.readPrefixList(file, 1);
        int step = 6, current = step-1;
   
        for(int i = 0; i < (numRandom + numRecommend)*2; i++){
            if(i < numRandom){
                means[0] += result.get(current).rating;
                temp[0] += result.get(current).rating;
            }
            else if(i >= numRandom && i < numRandom+numRecommend){
                means[1] += result.get(current).rating;
                temp[1] += result.get(current).rating;
            }
            else if(i >= numRandom+numRecommend && i < numRandom + numRecommend*2){
                means[1] += result.get(current).rating;
                temp[2] += result.get(current).rating;
            }
            else{
                means[0] += result.get(current).rating;
                temp[3] += result.get(current).rating;
            }

            current += step;
        }
        means[0] /= numRandom * 2;
        means[1] /= numRecommend * 2;
        temp[0] /= numRandom;
        temp[1] /= numRecommend;
        temp[2] /= numRecommend;
        temp[3] /= numRandom;
        
        double tp = temp[1] - temp[0];            
        bw.write("" +tp+"\n");
        tp = temp[2] - temp[3];            
        bw.write("" +tp+"\n");
        
            

        if(temp[1] > temp[0])
            means[2] += 1;
        else if(temp[1] == temp[0])
            means[2] += 0.5;
        if(temp[2] > temp[3])
            means[2] += 1;
        else if(temp[2] == temp[3])
            means[2] += 0.5;
        
        return means;
        
    }

    /**
     * Compute the average rating for random story and personalized story.
     */
    public static void printMean(){
        
        
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter("diff.txt"));
            File folder = new File("ratings_testing");
            String[] files = folder.list();
            int max = files.length;
            double[][] result = new double[max][];
            for (int i = 0; i < max; i ++){
                result[i] = calculateMean("ratings_testing" + "\\" + files[i], bw);
            }
            bw.close();
            double t1=0, t2=0, t3 = 0;
            for (int i = 1; i < max; i ++){
                t1 += result[i][0];
                t2 += result[i][1];
                t3 += result[i][2];
            }
            t1 /= max;
            t2 /= max;
            t3 /= max*2;
            System.out.println("Random: " + t1 + ", Optimized: " + t2 + ", accuracy: " + t3);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        
    }
    
    public static void main(String args[]){
        
//        double[][] result = new double[maxIter][];
//        try{
//            BufferedWriter bw = new BufferedWriter(new FileWriter("diff.txt"));
//            for (int i = 0; i < maxIter; i ++){
//                result[i] = calculateMean(".//temp//t" +i+".txt", bw);
//            }
//            bw.close();
//            double t1=0, t2=0, t3 = 0;
//            for (int i = 1; i < maxIter; i ++){
//                t1 += result[i][0];
//                t2 += result[i][1];
//                t3 += result[i][2];
//            }
//            t1 /= maxIter;
//            t2 /= maxIter;
//            t3 /= maxIter*2;
//            System.out.println("Random: " + t1 + ", Optimized: " + t2 + ", accuracy: " + t3);
//        }
//        catch(IOException e){
//            e.printStackTrace();
//        }     
        printMean();
    }
    
}
