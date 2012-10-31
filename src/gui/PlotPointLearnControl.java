/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.util.*;
import java.io.*;
/**
 *
 * @author Hong Yu
 */
public class PlotPointLearnControl {
    
    private int numShow = 30;
    private int currentNum = 0;
    private int current = 0;
    private int previous = -1;
    private Random rd = new Random();
    private Plot currentPlot = null;
    private int totalPlotPoints = 326;
    private ArrayList<Integer> plotPointIndices = new ArrayList<Integer>();
    
    public PlotPointLearnControl(){
        current = rd.nextInt(totalPlotPoints);
        currentPlot = new Plot(Plot.storyGenerator.getPlotPoint(current));
        previous = current;
        plotPointIndices.add(current);
    }
    
    public int currentStoryNumber() {
        return currentNum;
    }
    
    public String showStory() {
        return currentPlot.toString();
    }
    
    public void advanceStory() {
        writePlot2File("ratings.txt");
        
        if (currentNum < numShow ) {
            currentNum++;
            boolean flag = true;
            while(flag){
                current = rd.nextInt(totalPlotPoints);
                if(!(plotPointIndices.contains(current))
                     && Plot.storyGenerator.getPlotPoint(current).getId() / 100 != Plot.storyGenerator.getPlotPoint(previous).getId() / 100){
                    flag = false;
                }
            }
            currentPlot = new Plot(Plot.storyGenerator.getPlotPoint(current));
            previous = current;
        }
        
        

        if (currentNum >= numShow){
            System.exit(0);
        }
    }
    
    public void setRating(int rating) {
        currentPlot.setRating(rating);
    }
    
    public int getRating() {
        return currentPlot.getRating();
    }
    
    private void writePlot2File(String filename){
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true));
            bw.write(currentPlot.toRatingString()+"\n");
            bw.close();
            
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    
    
    
        
}
