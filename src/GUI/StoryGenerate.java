/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.util.*;
import java.io.*;
import prefix.*;
import tools.*;
/**
 *
 * @author Peng
 */
public class StoryGenerate {
    private ArrayList<PlotPoint> plotLibrary;
    private ArrayList<Plot> plots;
    private String plotpointsFile = PrefixUtil.plotpointFile;
    private String plotsFile = PrefixUtil.storySpaceFile;
            
    public StoryGenerate() {
        plotLibrary = new ArrayList<PlotPoint>();    
        String line = null;
        try{
            BufferedReader br = new BufferedReader(new FileReader(plotpointsFile));
            
            while((line = br.readLine()) != null){
                    String[] t = line.split(":::");
                    plotLibrary.add(new PlotPoint(t[1], Integer.parseInt(t[0])));
            }
        }
        catch(Exception e){
            System.out.println(line);
            
            e.printStackTrace();
        }
                
                        
//        plotLibrary.add(new PlotPoint("The local liquor store is Jack's target. It isn't far - Jack's always joked that the best part about where he lives is the proximity to beer - and it's always open at night.", 0));
//        plotLibrary.add(new PlotPoint("Jack arrives at the liquor store. A buzzing, neon sign outside welcomes him, as it declares nonchalantly: \"Gas and Liquor\". Jack pulls open the door, and a musky smell assails his nose. Eyes adjusting to the light, he enters.", 1));
//        plotLibrary.add(new PlotPoint("Suddenly, something lunges from the darkness, baring its fangs. Jack stumbles back in surprise and focuses his gaze incredulously. It's a vampire!", 2));
//        plotLibrary.add(new PlotPoint("The vampire wrestles Jack to the ground with superhuman strength. Helpless, Jack could only watch in horror as the vampire sinks its fangs in his neck. A sudden, piercing pain marks the end of Jack's life as a human... And the beginning of his life as a vampire.", 3));
//        plotLibrary.add(new PlotPoint("Grabbing a nearby gas canister, Jack tosses it at the vampire impulsively. The canister breaks and spills its slippery contents. Undeterred, the vampire continues advancing. Desperate but sober, Jack lights his cigarette lighter and throws it at the vampire, who instantly explodes into colorful flames. It turns out that gasoline, the black blood of the modern world, is too much for even a mythical blood sucker to swallow.", 4));
//        plotLibrary.add(new PlotPoint("Pumping his legs as fast as he can, Jack turns tail and runs. Around him, the wind whistles its disapproval, but Jack is too busy to care.", 5));
//        plotLibrary.add(new PlotPoint("Jack takes out his cell phone and dials the number for his girlfriend. Shortly after, a female voice answers, \"Hi Jack, how's it going?\" \"Listen, Sarah, I have something to tell you...\"", 6));
//        plotLibrary.add(new PlotPoint("Ancient, eldritch power flows through Jack's veins, as he is no longer bound by the laws of physics.", 7));        
//        plotLibrary.add(new PlotPoint("Jack decides to go for a walk. Outside, he takes a whiff of the fresh night air. The temperature is cooler than is usual for a midsummer eve.", 8));   
//        plotLibrary.add(new PlotPoint("Jack pauses for a moment, and then starts walking towards Sarah's house. Sarah, Jack's girlfriend, lives three blocks away in a cozy suburban home. It won't be long before Jack arrives.", 9));   
    }
    
    public PlotPoint getPlotPointFromId(int index) {
//        assert(index < plotLibrary.size());
        for(int i = 0; i < plotLibrary.size(); i++){
            PlotPoint pp = plotLibrary.get(i);
            if(pp.getId() == index){
                return pp;
            }
        }
        return null;
    }
    
    public PlotPoint getPlotPoint(int index){
        return plotLibrary.get(index);
        
    }
    
    
//    Hong 11.19.11
//    public Plot getPlotPointPlot(int id){
//        Plot p = new Plot(id);        
//        return p;        
//    }
    
    public ArrayList<Plot> generatePlots(int num) {
        // Hack
        if (plots == null) {
            
            plots = new ArrayList<Plot>();
            try{
                BufferedReader br = new BufferedReader(new FileReader(plotsFile));
                String line;
                while((line = br.readLine()) != null){
                        String[] t = line.split("\t");
                        int[] list = new int[t.length];
                        for(int i = 0; i < t.length; i++){
                                list[i] = Integer.parseInt(t[i]);
                        }
                        plots.add(new Plot(list));
                }
	
            }
            catch(Exception e){
                e.printStackTrace();
            }
//            plots.add(new Plot(0, 2, 3, 8, 6));
//            plots.add(new Plot(9, 2, 6, 7, 5));
//            plots.add(new Plot(3, 8, 0, 1, 7));
//            plots.add(new Plot(6, 8, 2, 5, 0));
//            plots.add(new Plot(5, 0, 6, 1, 9));
//            plots.add(new Plot(7, 2, 4, 8, 6));
//            plots.add(new Plot(0, 2, 4, 5, 1));
//            plots.add(new Plot(8, 6, 2, 5, 9));
//            plots.add(new Plot(1, 2, 3, 7, 5));
//            plots.add(new Plot(6, 9, 1, 3, 8));
        
        }
        
        assert(num < plots.size());        
        Random r = new Random();
        ArrayList<Integer> randomPlotIndices = new ArrayList<Integer>();
        ArrayList<Plot> randomPlots = new ArrayList<Plot>();
        
        for (int i = 0; i < num; i++) {
            Integer randomPlot;
            
            do {
                randomPlot = r.nextInt(plots.size());
            } while (randomPlotIndices.contains(randomPlot));
        
            randomPlots.add(plots.get(randomPlot));
            randomPlotIndices.add(randomPlot);
        }
        
        //System.out.println(randomPlotIndices);
        return randomPlots;
    }
    
    public static void main (String [] args) {
        StoryGenerate s = new StoryGenerate();
        
        ArrayList<Plot> testPlots = s.generatePlots(5);
        
        System.out.println(testPlots);
    }
}
