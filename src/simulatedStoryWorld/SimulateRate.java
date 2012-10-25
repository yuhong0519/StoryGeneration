/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulatedStoryWorld;

/**
 *
 * @author Hong Yu
 */
import tools.CommonUtil;
import tools.PrefixUtil;
import java.io.*;
import java.util.*;

//import gridWorld.GenerateGridStorySpace;
import PPCA.*;
import cfcontrol.Test;
import prefix.*;
//import nmf.*;
import no.uib.cipr.matrix.*;

public class SimulateRate {
    public static int numRandom = 5, numRecommend = 5;
//  algorith 1: PPCA, 2: NMF, 3: thue
    private static int algorithm = 2;
    private static double[][] people = new double[][]{{0.8,0,0.2,0,0},{0,0.8,0,0,-0.2},{0,0.3,0.7,0,0}, {0,0,0,0.9,0.1},{0,0,0,0.5,-0.5},};
//    private static double[][] people = new double[][]{{1,0,0,0,0},{0,1,0,0,0},{0,0,1,0,0}, {0,0,0,1,0},{0,0,0,0,1},};
    private static ArrayList<Prefix> prefixList = null;
    private static DenseMatrix cov = null;
    private static DenseMatrix mean = null;
    private static DenseMatrix currentRatings = null;
//    private static double[][] mask = null;
    private static DenseMatrix W = null;
    private static int[][] storySpace = CommonUtil.readIntData(PrefixUtil.storySpaceFile);
    private static ArrayList<Prefix> tags = PrefixUtil.readPrefixList("Tag.txt", 1);
    
    static String intArray2String(int[] a){
        String str = "";
        for(int i = 0; i < a.length; i++){
            str = str + a[i] + " ";
            
        }
        return str;
    }
    static int[] intArray(int max, int num){
        Random rd = new Random();
        int[] values = new int[num];
        boolean flag = true;
        for(int i = 0; i < num; i++){
            flag = true;
Continueflag:
            while(flag){
                values[i] = rd.nextInt(max);
                for(int j = 0; j < i; j++){
                    if(values[j] == values[i]){
                        continue Continueflag;
                    }
                    
                }
                flag = false;
            }
        }
        
        return values;
        
    }
    
    static String prefix2string(Prefix p, int rating){
        String str = "";
        if(p == null){
            return str;
        }
        str = str + rating + ": ";
        for(int i = 0; i < p.itemList.size(); i++){
            str = str + p.itemList.get(i).id;
            if(i < p.itemList.size()-1)
                str = str + " ";
        }
        return str;
        
    }
    
    static void startSimulate(String outputFile, double[] people){
        if(prefixList == null)
            prefixList = PrefixUtil.readPrefixList(PrefixUtil.prefixListFile, 1);
        if(cov == null)
            cov = new DenseMatrix(CommonUtil.readData("cov1.txt", false));
        if(mean == null)
            mean = new DenseMatrix(CommonUtil.readData("mean1.txt", false));
//        if(mask == null)
//            mask = MyUtil.readData("Mask.txt", false);
        if(W == null)
            W = new DenseMatrix(CommonUtil.readData("W1.txt", false));
        if(currentRatings == null)
            currentRatings = new DenseMatrix(prefixList.size(), 1);
        
//        Random rd = new Random();
        int[] randomStories = intArray(storySpace.length, numRandom*2);
//        int currentPlotpoint = 0;
        int maxPlotpoint = 6;
        Prefix currentPrefix = new Prefix(Arrays.copyOf(storySpace[randomStories[0]], 1));
        
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
            int randomID = 0;
            for(int i = 0; i < 2*(numRandom + numRecommend); i++){
                for(int j = 0; j < maxPlotpoint; j++){
                    
                    int rating = 0;
                    if(currentPrefix != null)
                            rating = Math.round((float)(GenerateRatings.calculateValue(tags.get(Collections.binarySearch(prefixList, currentPrefix)), people)));
                    String ratingString = prefix2string(currentPrefix, rating);
                    bw.write(ratingString);
                    bw.newLine();
                    int[] recommend;
                    if(i < numRandom || i >= numRandom + 2*numRecommend)
                        recommend = Test.getNextRecommendation(ratingString, algorithm, true);
                    else
                        recommend = Test.getNextRecommendation(ratingString, algorithm, true);
                    
                    if(j < maxPlotpoint - 1)
                        recommend = Arrays.copyOf(recommend, j+2);
                    else
                        recommend = Arrays.copyOf(recommend, 1);
                    
//                    System.out.println("i: " + i + ",j: " + j + ", Recommend: " + intArray2String(recommend));
                    
                    if(i < numRandom || i >= numRandom + 2*numRecommend){
                        if(j < maxPlotpoint -1)
                            recommend = Arrays.copyOf(storySpace[randomStories[randomID]], j+2);
                        else if(i != numRandom -1 && i != 2*(numRandom+numRecommend)-1){
                            randomID++;
                            recommend = Arrays.copyOf(storySpace[randomStories[randomID]], 1);
                        }
                        else 
                            randomID++;
                    }
                    if(i == numRandom + 2*numRecommend -1 && j == maxPlotpoint - 1){
                        recommend = Arrays.copyOf(storySpace[randomStories[randomID]], 1);
                    }
//                    System.out.println("Actual: " + intArray2String(recommend));
                    
                    currentPrefix = new Prefix(recommend);
                }
                
                    
                
            }
            
            bw.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public static void main(String[] args){

        int max = 50;
        Random rd = new Random();
        for(int num = 5; num < 6; num++){
            numRandom = num;
            numRecommend = num;
            System.out.println("Algorithm: "  + algorithm + ", number: " + num);
            try{
                BufferedWriter bw = new BufferedWriter(new FileWriter("SelectedPeople.txt"));
                for(int i = 0; i < max; i++){
                    if (i % 100 == 0)
                        System.out.println("Iteration: " + i);
                    int id = rd.nextInt(people.length);
                    startSimulate(".//temp//t" +i+".txt", people[id]);
                    Test.restart(); 
                    for(int j = 0; j < people[0].length; j++){
                        bw.write("" + people[id][j] + " ");
                    }
                    bw.newLine();
                    bw.flush();
                }
                bw.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
            ComputeMeanRating.printMean(max);
        }
    }
    
    
}
