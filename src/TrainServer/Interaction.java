/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TrainServer;

import gui2.Done;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;
import javax.swing.DefaultListModel;
import prefix.AllOptions;
import prefix.IntegerPlotPoint;
import prefix.OptionItem;
import prefix.PPOptions;
import prefix.PlotPoint;
import prefix.PlotPointLibrary;
import prefix.Prefix;
import tools.CommonUtil;
import tools.PrefixUtil;

/**
 *
 * @author Bunnih
 */
public class Interaction implements Comparable<Interaction>{
    private int userID;
    private ArrayList<Prefix> currentShown = new ArrayList<Prefix>();
    private int currentStory = 1;
    private int currentPlotPoint = 1;
    
    private static ArrayList<Prefix> StorySpace = PrefixUtil.readStorySpace(PrefixUtil.storySpaceFile);;
    
    private static final int numStories = 5;
    private static final int numPPPStory = 6;
    private static AllOptions quiz = PrefixUtil.readOptions(PrefixUtil.quizFile);
    
    private  PlotPointLibrary ppl = PrefixUtil.readPlotPoints(PrefixUtil.plotpointFile);
    private  AllOptions ao = PrefixUtil.readOptions(PrefixUtil.optionFile);
    

    private ArrayList<Integer> avoidPlotPoints = new ArrayList<Integer>();
    private ArrayList<Integer> requiredPlotPoints = new ArrayList<Integer>();
    private ArrayList<Integer> preferedPlotPoints = new ArrayList<Integer>();
    private int numOptionsPerBranch = 2;
    private int numOptionsPerPreferBranch = 2;
    
    private String key;
    private int numQuestions = 0;
    private int numCorrectQuestions = 0;
    private boolean testFlag = false;
    private int numTrainForTest = 4;

    private int quizStatus = 0;
        
    private String ratingFile = null;
    private String optionPreferenceFile = null;
    private boolean finalPlot = false;
    
    private int correctChoice = -1;
    private boolean initPP = true;
    private boolean firstPP = true;
    
    public Interaction(int id){
        this.userID = id;
        key = "";
        Random r = new Random();
        for(int i = 0; i < 20; i++){
            int tt = r.nextInt(36);
            if(tt < 26){
                tt += 65;
                key = key + (char)tt;
            }
            else{
                tt -= 26;
                key = key + tt;
            }
        }
        PrefixUtil.addOptions2PlotPoints(ppl, ao);
        loadStorySelectionPreference();
        startNewStory();
    }
    
    public int getUserID(){
        return userID;
    }
        
    public int compareTo(Interaction i){
        if(this.userID == i.userID){
            return 0;
        }
        else if(this.userID < i.userID){
            return -1;
        }
        else{
            return 1;
        }
    }
    
    private void loadStorySelectionPreference(){
        requiredPlotPoints.add(0);
        if(testFlag){
            avoidPlotPoints.add(2);
        }
//        preferedPlotPoints.add(41);
    }
    
    private boolean checkStoryLegal(Prefix p){
        for(int i = 0; i < avoidPlotPoints.size(); i++){
            if(p.contains(avoidPlotPoints.get(i))){
                return false;
            }
        }
        for(int i = 0; i < requiredPlotPoints.size(); i++){
            if(!p.contains(requiredPlotPoints.get(i))){
                return false;
            }
        }
        return true;
    } 
    
    private void startNewStory(){
        Random rd = new Random();
        currentPlotPoint = 1;
//        firstPP = true;
        int tp = rd.nextInt(StorySpace.size());
        while(!checkStoryLegal(StorySpace.get(tp))){
            tp = rd.nextInt(StorySpace.size());
        }
        //currentShown.clear();
        int id = StorySpace.get(tp).itemList.get(0).id;
        IntegerPlotPoint ipp = ppl.getPP(id);
        Prefix p = new Prefix(id, getNextOption(ipp));
        currentShown.add(p);
    }
    
    private PPOptions getNextOption(IntegerPlotPoint pp){
        PPOptions ppo = pp.getOptions();
        if(ppo == null){
            return null;
        }
        PPOptions ret = new PPOptions(ppo.getPPID());
        ArrayList<Integer> aipp = ppo.getAllIndicatedPP();
        int numOptionPerPP;
        for(Integer ipp : aipp){
            boolean flag = false;
            for(int i : avoidPlotPoints){
                if(i == ipp.intValue()){
                    flag = true;
                    break;
                }
            }
//            remove option items which lead to plot points in the avoidPlotPoints
            if(flag){
                continue;
            }
            for(int i : preferedPlotPoints){
                if(i == ipp.intValue()){
                    flag = true;
                    break;
                }
            }
            if(flag){
                numOptionPerPP = numOptionsPerPreferBranch;
            }
            else{
                numOptionPerPP = numOptionsPerBranch;
            }
            ArrayList<OptionItem> oil = ppo.getItemListByIndicatedPP(ipp.intValue());
            if(oil.size() > numOptionPerPP){
                ArrayList<Integer> sel = CommonUtil.getRandom(oil.size(), numOptionPerPP);
                for(Integer i : sel){
                    ret.add(oil.get(i));
                }
            }
            else if(oil.size() >= 1){
                ret.add(oil);
            }
            else{
                System.err.println("Error in TrainingControl.getNexOption: Cannot find the indicated option item");
            }
        }
        return ret;
    }
    
    private String[] getQuestions(){
        if(currentPlotPoint < numPPPStory || currentStory == numStories){
            return null;
        }
        Random rd = new Random();
        numQuestions++;
        ArrayList<String> questions = new ArrayList<String>();
        PPOptions ppo = currentShown.get(currentShown.size()-2).options;
        ArrayList<OptionItem> oiList = ppo.getAllOptions();
        
        ArrayList<PlotPoint> tp = currentShown.get(currentShown.size()-1).itemList;
        ArrayList<PPOptions> quizList = quiz.getPPOptionList(tp.get(tp.size()-1).id);
            
//         Question type 1: option chosn question   
        if(rd.nextDouble() > 0.7 && oiList.size() > 1){
            for(int i = 0; i < oiList.size(); i++){
                questions.add("You just chose: \"" + oiList.get(i).getValue() + "\"");
            }
        }
//        Question type 2: story context question
        else{
            ArrayList<OptionItem> tao = quizList.get(rd.nextInt(quizList.size())).getAllOptions();
            for(int i = 0; i < tao.size(); i++){
                questions.add(tao.get(i).getValue());
                if(tao.get(i).getIndicatedPP() == 1){
                    correctChoice = i;
                }
            }
            
        }        
        String[] ret = new String[questions.size()];
        int i = 0;
        for(String s : questions){
            ret[i++] = s;
        }
        return ret;
        
        
    }
        
    public ServerResponse playerAction(InteractionList intl, PlayerResponse pr){
        if(userID != pr.userId){
            System.err.println("Sending the player action to the wrong interaction!");
            return null;
        }
//        record player ratings
        if((quizStatus == 0 || quizStatus == 1) && !initPP){
            Prefix cp = currentShown.get(currentShown.size()-1);
            cp.rating = pr.plotRating;
            if(cp.options != null){
                ArrayList<OptionItem> aoi = cp.options.getAllOptions();            
                for(int i = 0; i < aoi.size(); i++){
                    aoi.get(i).setPreference(pr.optionRatings[i]);
                }
            }
        }
        
        ServerResponse sr = new ServerResponse();
        sr.userId = pr.userId;
//        Final 
        if(finalPlot){
            nextPlotPoint(pr);
            sr.rateOptions = false;
            sr.ratePlot = false;
            sr.clear = true;
            sr.last = true;
            sr.plot = "Thank you very much for your help! You key code is <b>" + key + "</b><br>Please copy it back to Amazon MT.";
            
            intl.remove(this);
        }
//        normal mode
        else if(quizStatus == 0 || quizStatus == 2){

            if(currentPlotPoint == numPPPStory - 1){
                correctChoice = pr.choice;
            }
            
            if(!initPP && quizStatus != 2){
                nextPlotPoint(pr);
            }
            Prefix p = currentShown.get(currentShown.size()-1);
            PPOptions co = p.options;                        
            if(co != null){
                String[] optionStrings = new String[co.getAllOptions().size()];
                for(int i = 0; i < optionStrings.length; i++){
                    optionStrings[i] = co.getAllOptions().get(i).getValue();
                }
                sr.options = optionStrings;
                sr.oldOptionsPref = new int[optionStrings.length];
                for(int i = 0; i < optionStrings.length; i++){
                    sr.oldOptionsPref[i] = co.getAllOptions().get(i).getPreference();
                }
                if(optionStrings.length == 1 && optionStrings[0].compareTo("Continue.") == 0){
                    sr.rateOptions = false;
                }
            }
            else{
                if(currentPlotPoint == numPPPStory){
                    sr.options = new String[]{"END OF STORY"};    
                    sr.oldOptionsPref = new int[]{0};
                    sr.rateOptions = false;
                }
                else{
                    System.err.println("No next plot point found");                    
                }                
            }
            sr.plot = ppl.getPP(p.getLast().id).value();
      
            Prefix pre = getVisitedPrefix();
            if(pre != null){
                sr.oldPlotPref = pre.rating;
            }
            if(currentPlotPoint == 1){
                sr.clear = true;
            }
            if(quizStatus == 2){
                firstPP = true;
            }
            else{
                firstPP = false;
            }            
            if(quizStatus == 2){
                if(pr.choice == correctChoice){
                    numCorrectQuestions++;
                }
                quizStatus = 0;
            }
            if(quizStatus == 0 && currentPlotPoint == numPPPStory){
                quizStatus = 1;
            }
        }
//        send quiz question
        else if(quizStatus == 1){
            sr.rateOptions = false;
            sr.ratePlot = false;
            sr.options = getQuestions();
            sr.clear = true;
            sr.oldPlotPref = 0;
            sr.plot = "Please select the statement below that most accurately describes what happened in the story you just read: ";
            sr.oldOptionsPref = new int[sr.options.length];
            Arrays.fill(sr.oldOptionsPref, 0);
            nextPlotPoint(pr);
            quizStatus = 2;
        }
        initPP = false;

        return sr;
    }
    
    private void nextPlotPoint(PlayerResponse pr){
        Prefix cp = currentShown.get(currentShown.size()-1);
        //       the last plot point of the last story
        if(finalPlot){            
            PrefixUtil.writePreferencePrefixList(currentShown, ratingFile);
            PrefixUtil.writeOptionPreference(currentShown, optionPreferenceFile);
            if((double)numCorrectQuestions/numQuestions < 0.9){
                key = key+"00";
                PrefixUtil.writeString(key, key);
            }

            else{
                key = key + "11";
                PrefixUtil.writeString(key, key);
            }
            return;
        }
        
        if(currentPlotPoint < numPPPStory){
            currentPlotPoint++;
            ArrayList<OptionItem> ppo = cp.options.getAllOptions();
            Prefix p = new Prefix(cp);
            int nextPPID = ppo.get(pr.choice).getIndicatedPP();
            IntegerPlotPoint nextPP = ppl.getPP(nextPPID);
            p.append(nextPPID, getNextOption(nextPP));
            currentShown.add(p); 
            if(currentPlotPoint == numPPPStory && currentStory == numStories){
                finalPlot = true;
            }

        }
//        last plot point, but not the last story
        else if(currentStory < numStories ){            
            if(testFlag && currentStory >= numTrainForTest){
                avoidPlotPoints.clear();
                avoidPlotPoints.add(1);
            }
            
            if(ratingFile == null){
                Calendar d = Calendar.getInstance();
                int month = d.get(Calendar.MONTH) + 1;
                ratingFile = "Rating" + "_" + key + "_" + month + "." + d.get(Calendar.DAY_OF_MONTH) +  "." + d.get(Calendar.HOUR_OF_DAY) + "." + d.get(Calendar.MINUTE) + "_" + pr.userId +".txt";
                optionPreferenceFile = "OptionPreference" + "_" + key + "_" + month + "." + d.get(Calendar.DAY_OF_MONTH) +  "." + d.get(Calendar.HOUR_OF_DAY) + "." + d.get(Calendar.MINUTE) + "_" + pr.userId +".txt";
            }
            PrefixUtil.writePreferencePrefixList(currentShown, ratingFile);
            PrefixUtil.writeOptionPreference(currentShown, optionPreferenceFile);
            currentStory++;
//            quizStatus = 2;
            startNewStory();
            
        }
        
    }
    
    private Prefix getVisitedPrefix(){
        Prefix tp = null;
        Prefix current = currentShown.get(currentShown.size()-1);
        for(int i = 0; i < currentShown.size()-1; i++){
            if(current.compareTo(currentShown.get(i))==0){
                tp = currentShown.get(i);
                break;
            }
        }
        return tp;
    }
}