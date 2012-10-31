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

/**
 *
 * @author Peng
 */
public class StoryControl {
    /* 10 stories, 5 plot points each (= total 50 prefixes)

    Total 10 plot points

    */
    private ArrayList<Plot> generatedPlots;
    private ArrayList<Plot> plotPrefixes;
    private ArrayList<Plot> ratedPlotPrefixes;
    private int currentStory;
    private int current;
    private static final int numStories = 10;
    
    public StoryControl() {
        currentStory = 0;
        current = 0;     
        generatedPlots = Plot.storyGenerator.generatePlots(numStories);
        plotPrefixes = generatedPlots.get(currentStory).getAllPrefixes();
        ratedPlotPrefixes = new ArrayList<Plot>();
    }
    
    public int currentStoryNumber() {
        return currentStory;
    }
    
    public String showStory() {
        return plotPrefixes.get(current).toString();
    }
    
    public boolean endOfStory(){
        return current == plotPrefixes.size()-1;
    
    }
    
    public void advanceStory() {
        if (current < plotPrefixes.size() - 1) {
            current++;
        }
        else {
            for (int i = 0; i < plotPrefixes.size(); i++) {
                ratedPlotPrefixes.add(plotPrefixes.get(i));
            }
            writePrefixToFile();
            
            if (currentStory < numStories - 1) {                
                currentStory++;
                plotPrefixes = generatedPlots.get(currentStory).getAllPrefixes();
                current = 0;
            }
            else {
               System.out.println("Cannot advance story - end of stories reached.");   
               System.exit(0);
            }
        }
    }
    
     public void backtrackStory() {
        if (current > 0) {
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
            Logger.getLogger(StoryControl.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
}
