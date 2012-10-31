/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import cfcontrol.*;

/**
 *
 * @author Peng
 */
public class StoryTestControl {
    /* 10 stories, 5 plot points each (= total 50 prefixes)

    Total 10 plot points

    */
    private ArrayList<Plot> plotPrefixes;
    private ArrayList<Plot> ratedPlotPrefixes;
    private int currentStory;
    private int current;
    private int currentRandomStory;
    private static final int numStories = 20;
    private static final int beginGeneratedStory = 6, endGeneratedStory = 15, numRandomStory = 10;
    private static final int storyLength = 6;
    private ArrayList<Plot> generatedPlots;
    private final int alg = 2; // 1 - PPCA; 2 - NMF
    
    public StoryTestControl() {
        currentStory = 0;
        current = 0;     
        currentRandomStory = 0;
//        plotPrefixes = new ArrayList<Plot>();
        ratedPlotPrefixes = new ArrayList<Plot>();
        generatedPlots = Plot.storyGenerator.generatePlots(numRandomStory);
        // We'll add the first prefix from a randomly generated story as
        // a starting point
//        Plot first = Plot.storyGenerator.generatePlots(1).get(0).getAllPrefixes().get(0);
//        plotPrefixes.add(first);
        plotPrefixes = generatedPlots.get(currentRandomStory).getPrefixes(1);
    }
    
    public int currentStoryNumber() {
        return currentStory;
    }
    
    public String showStory() {
        return plotPrefixes.get(current).toString();
    }
    
    public void advanceStory() {
        // If current story has reached storyLength and advanceStory is 
        // requested, then write its prefixes to file and go to the next story 
        if (current >= storyLength - 1) {
            for (int i = 0; i < plotPrefixes.size(); i++) {
                ratedPlotPrefixes.add(plotPrefixes.get(i));
            }
            writePrefixToFile();
            
            if (currentStory < numStories - 1) {                
                currentStory++;                
                
                Plot currentPlot = plotPrefixes.get(current);
                String ratingString = currentPlot.toRatingString();
                //Plot first = Plot.storyGenerator.generatePlots(1).get(0).getAllPrefixes().get(0);
                int[] preferredPlot = Test.getNextRecommendation(ratingString, alg, true);

        
                current = 0;
                plotPrefixes.clear();
                plotPrefixes.add(new Plot(preferredPlot[0]));
                //Use random story if needed
                if(currentStory < beginGeneratedStory - 1 || currentStory >= endGeneratedStory){
                    currentRandomStory++;
                    plotPrefixes = generatedPlots.get(currentRandomStory).getPrefixes(1);
                    System.out.println("Random Story: " + plotPrefixes.get(0).toRatingString());
                }
            }
            else {
               System.out.println("Cannot advance story - end of stories reached."); 
               System.exit(0);
                 
            }
            
            return;
        }

        Plot currentPlot = plotPrefixes.get(current);
        String ratingString = currentPlot.toRatingString();
        int[] preferredPlot = Test.getNextRecommendation(ratingString, alg, true);
        
        // Debug
        System.out.println("Input: " + ratingString);
        System.out.print("Output: ");
        for (int j = 0; j < preferredPlot.length; j++) {
            System.out.print(preferredPlot[j] + " ");
        }
        System.out.println();

       
        
        // New prefix is preferredPlot trimmed to current story length + 1
        int[] newPlot = new int[current + 2];
        for (int i = 0; i < newPlot.length; i++) {
            newPlot[i] = preferredPlot[i];
        }
        Plot tp = new Plot(newPlot);
        
        //        Use random story if needed
        if(currentStory < beginGeneratedStory - 1 || currentStory >= endGeneratedStory){
            tp = generatedPlots.get(currentRandomStory).getSubPlot(current+2); 
            //plotPrefixes = generatedPlots.get(currentRandomStory).getPrefix(current+2);
            //System.out.println("Random Story: " + plotPrefixes.get(current+1).toRatingString());
        }
        plotPrefixes.add(tp);
        current++;
    }
    
     public void backtrackStory() {
        if (current > 0) {            
            plotPrefixes.remove(current);
            current--;
        }
        else {
            System.out.println("Cannot backtrack story - beginning of story reached.");
        }
    }
     
    public int getRating() {
        return plotPrefixes.get(current).getRating();
    }
    
    
    public void setRating(int rating) {
        plotPrefixes.get(current).setRating(rating);
    }
        
    public boolean endOfStory(){
        return current == storyLength - 1;
    
    }
        
    public void writePrefixToFile() {
        String text = "";
        
        Iterator<Plot> i = ratedPlotPrefixes.iterator();
        while (i.hasNext()) {
            Plot p = i.next();
            text += p.getRating() + ": ";
            Iterator<PlotPoint> i2 = p.getPlotPoints().iterator();
            while (i2.hasNext()) {
                PlotPoint pp = i2.next();
                text += pp.getId();
                if (i2.hasNext()) {
                    text += " ";
                }
            }
            text += "\n";
        }
        
        try {
            FileWriter fstream = new FileWriter("ratings.txt");
            BufferedWriter writer = new BufferedWriter(fstream);
            writer.write(text);
            writer.close();
            fstream.close();
        } catch (IOException ex) {
            Logger.getLogger(StoryTestControl.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
}
