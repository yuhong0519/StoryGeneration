/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Peng
 */
public class Plot {
    public static final StoryGenerate storyGenerator = new StoryGenerate();
    private ArrayList<PlotPoint> plotPoints;
    private int plotRating;
    
    public Plot(int ... ids) {
        plotPoints = new ArrayList<PlotPoint>();
        plotRating = 3;
        
        // Fill the plot according to the given ids
        for (int i = 0; i < ids.length; i++) {
            plotPoints.add(storyGenerator.getPlotPointFromId(ids[i]));
        }
    }

    public Plot(PlotPoint pp){
        plotPoints = new ArrayList<PlotPoint>();
        plotRating = 3;
        plotPoints.add(pp);
    }
//    public Plot(int[] ids){
//        plotPoints = new ArrayList<PlotPoint>();
//        plotRating = 3;
//        
//    }
//    
    public void setPlotPoints(ArrayList<PlotPoint> plot) {
        plotPoints = plot;
    }
    
    public void setRating(int rating) {
        plotRating = rating;
    }
    
    public int getRating() {
        return plotRating;
    }
    
    public ArrayList<PlotPoint> getPlotPoints() {
        return plotPoints;
    }
    
    public ArrayList<Plot> getAllPrefixes() {
        ArrayList<Plot> prefixes = new ArrayList<Plot>();
        
        for (int i = 0; i < plotPoints.size(); i++) {
            Plot p = new Plot();
            
            for (int j = 0; j <= i; j++) {
                p.plotPoints.add(plotPoints.get(j));
            }
            
            prefixes.add(p);
        }
        
        return prefixes;
    }
    
    public ArrayList<Plot> getPrefixes(int num){
        ArrayList<Plot> prefixes = new ArrayList<Plot>();
        
        for (int i = 0; i < num; i++) {
            Plot p = new Plot();
            
            for (int j = 0; j <= i; j++) {
                p.plotPoints.add(plotPoints.get(j));
            }
            
            prefixes.add(p);
        }
        return prefixes;
        
    }
    
    public Plot getSubPlot(int num){
            
            Plot p = new Plot();
            
            for (int j = 0; j < num; j++) {
                p.plotPoints.add(plotPoints.get(j));
            }
            
            
        
           return p;
        
    }
    
    public String toString() {
        String ret = "";
        Iterator<PlotPoint> i = plotPoints.iterator();
        while (i.hasNext()) {
            ret = ret + i.next() + "\n\n";
        }
        return ret;
    }
    
    public String toRatingString() {
        String ret = "";
        
        ret += getRating() + ": ";
        Iterator<PlotPoint> i = getPlotPoints().iterator();
        while (i.hasNext()) {
            PlotPoint pp = i.next();
            ret += pp.getId();
            if (i.hasNext()) {
                ret += " ";
            }
        }
        
        return ret;
    }
}
