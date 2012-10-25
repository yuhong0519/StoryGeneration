package gui2;
import tools.PrefixUtil;
import java.util.*;
import javax.swing.*;
import prefix.*;
import java.net.*;

/**
 *
 * @author Hong Yu
 */
public class TrainingControl {
    private ArrayList<Prefix> prefixList = null;
    private ArrayList<Prefix> StorySpace = null;
    private PlotPointLibrary ppl = null;
    private static final int numStories = 20;
    private static final int numPPPStory = 6;
    private int currentStory;
    private int currentPlotPoint;
    private DefaultListModel dlm;
    private AllOptions quiz = null;
    
    private ArrayList<Prefix> currentShown = null;
    
    private String ratingFile = null;
    private String optionPreferenceFile = null;
    public String ip;
    private JApplet ja;
    private TrainingPanel trainingp;
    private int numQuestions = 0;
    private int numCorrectQuestions = 0;
    private String key;
//    private char[] keyTable = new char[]{'A','B','C','D','E','F','G','H','I','J',};
    public TrainingControl(JApplet ja, TrainingPanel tp){
        this.ja = ja;
        this.trainingp = tp;
        prefixList = PrefixUtil.readPrefixList(PrefixUtil.prefixListFile, 1);
        StorySpace = PrefixUtil.readStorySpace(PrefixUtil.storySpaceFile);
        currentShown = new ArrayList<Prefix>();
        ppl = PrefixUtil.readPlotPoints(PrefixUtil.plotpointFile);
        AllOptions ao = PrefixUtil.readOptions(PrefixUtil.optionFile);
        quiz = PrefixUtil.readOptions(PrefixUtil.quizFile);
        
        PrefixUtil.addOptions2PlotPoints(ppl, ao);
//        Create key~~
        Random r = new Random();
        key = "";
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
        
        currentStory = 1;
        currentPlotPoint = 0;
        dlm = new DefaultListModel();
        restart();
        

    }
    
    public String getTitle(){
        return("Story: " + currentStory);
        
    }
    
    public void restart(){
        Random rd = new Random();
        currentPlotPoint = 0;
        int tp = rd.nextInt(StorySpace.size());
//        int tp = 0;
        //currentShown.clear();
        int id = StorySpace.get(tp).itemList.get(0).id;
        IntegerPlotPoint ipp = ppl.getPP(id);
        Prefix p = new Prefix(id, new PPOptions(ipp.getOptions()));
        currentShown.add(p);
        
    }
    
    public void next(int index, int rating) {

            
        currentShown.get(currentShown.size()-1).rating = rating;
        if(currentPlotPoint < numPPPStory - 1){
            currentPlotPoint++;
            int ppid = currentShown.get(currentShown.size()-1).getLast().id;
            IntegerPlotPoint tp = ppl.getPP(ppid);
            ArrayList<OptionItem> ppo = tp.getOptions().getAllOptions();
            Prefix p = new Prefix(currentShown.get(currentShown.size()-1));
            int nextPPID = ppo.get(index).getOID();
            IntegerPlotPoint nextPP = ppl.getPP(nextPPID);
            p.append(nextPPID, new PPOptions(nextPP.getOptions()));
            currentShown.add(p);
                    
        }
        
        else if(currentStory < numStories ){

            if(ratingFile == null){
                Calendar d = Calendar.getInstance();
                int month = d.get(Calendar.MONTH) + 1;
                ratingFile = "Rating" + "_" + key + "_" + month + "." + d.get(Calendar.DAY_OF_MONTH) +  "." + d.get(Calendar.HOUR_OF_DAY) + "." + d.get(Calendar.MINUTE) + "_" + ip+".txt";
                //ratingFile = "Rating.txt";
                       
//                System.out.println(ratingFile);
                optionPreferenceFile = "OptionPreference" + "_" + key + "_" + month + "." + d.get(Calendar.DAY_OF_MONTH) +  "." + d.get(Calendar.HOUR_OF_DAY) + "." + d.get(Calendar.MINUTE) + "_" + ip+".txt";
            }
            PrefixUtil.writePrefixList2Server(currentShown, ratingFile);
            PrefixUtil.writeOptionPreference2Server(currentShown, optionPreferenceFile);
            currentStory++;
            restart();
        }
        else{
            trainingp.disableNext();
            
            PrefixUtil.writePrefixList2Server(currentShown, ratingFile);
            PrefixUtil.writeOptionPreference2Server(currentShown, optionPreferenceFile);
            
            if((double)numCorrectQuestions/numQuestions < 0.9){
//                JOptionPane jo = new JOptionPane();
//                jo.showMessageDialog(null,"Thank you very much for your help! Unfortunately you did not pass the quiz. You could restart it from the beginning again.");   
                key = key+"00";
                PrefixUtil.writeString2Server(key, key);
                Done doneDialog = new Done(null, true, key);
                doneDialog.setMessage("<html>Thank you very much for your help! Unfortunately, you did not pass the quiz. You key code is as follows. Please copy it back to Amazon:</html>");
                doneDialog.setVisible(true);
            }

            else{
                key = key + "11";
                PrefixUtil.writeString2Server(key, key);
                Done doneDialog = new Done(null, true, key);
                doneDialog.setVisible(true);
            }
            
//            PrefixUtil.writeString2Server(key, key);
//            PrefixUtil.writePrefixList2Server(currentShown, ratingFile);
//            PrefixUtil.writeOptionPreference2Server(currentShown, optionPreferenceFile);
            try{
                ja.getAppletContext().showDocument(new URL("http://scarecrow.cc.gt.atl.ga.us//Finish.html"));
            }
            catch(MalformedURLException e){
                e.printStackTrace();
            }
        }
        
    }
    public void correctQuestion(int value){
        if(value == correctChoice)
            numCorrectQuestions++;
    }
    
    int correctChoice = -1;
//    Only store the second to last selection from the player. 
    public void setCorrectChoice(int cc){
        if(currentPlotPoint == numPPPStory - 2){
            correctChoice = cc;
        }
    }
    
    public ArrayList<String> getQuestions(){
        if(currentPlotPoint < numPPPStory - 1 || currentStory == numStories){
            return null;
        }
        Random rd = new Random();
//        20 percent of times no questions.
//        if(rd.nextDouble() > 0.9)
//            return null;
        
        numQuestions++;
        ArrayList<String> options = new ArrayList<String>();
        
        
        PPOptions ppo = currentShown.get(currentShown.size()-2).options;
        ArrayList<OptionItem> oiList = ppo.getAllOptions();
        
        ArrayList<PlotPoint> tp = currentShown.get(currentShown.size()-1).itemList;
        ArrayList<PPOptions> quizList = quiz.getPPOptionList(tp.get(tp.size()-1).id);
            
            
        if(rd.nextDouble() > 0.7 && oiList.size() > 1){

            for(int i = 0; i < oiList.size(); i++){
                options.add("You just chose: \"" + oiList.get(i).getValue() + "\"");
            }
        }
        else{

            ArrayList<OptionItem> tao = quizList.get(rd.nextInt(quizList.size())).getAllOptions();
            for(int i = 0; i < tao.size(); i++){
                options.add(tao.get(i).getValue());
                if(tao.get(i).getOID() == 1){
                    correctChoice = i;
                }
            }
            
        }
        
        return options;
        
        
    }
    
    public String show(){
        String ret = "";
        for(int i = 0; i < currentShown.get(currentShown.size()-1).itemList.size(); i++){
            ret = ret + ppl.getPP(currentShown.get(currentShown.size()-1).itemList.get(i).id).value() + "\n\n";
            
        }
        //System.out.println(ret);       
        return ret;
        
    }
    
    public DefaultListModel getListModel(){
        dlm.clear();
        if(currentPlotPoint < numPPPStory-1){
            int ppid = currentShown.get(currentShown.size()-1).getLast().id;
            IntegerPlotPoint tp = ppl.getPP(ppid);
            ArrayList<OptionItem> ppo = tp.getOptions().getAllOptions();
            for(int i = 0; i < ppo.size(); i++){
                int ii = i+1;
                dlm.addElement("" + ii + ": " + ppo.get(i).getValue());
            }
        }
        else{
            dlm.addElement("END OF STORY!");
        }
        return dlm;
    }
    
    public void getPreferenceCB(TrainingPanel trainingPanel, JRadioButton[][] jcb){
        if(currentPlotPoint < numPPPStory){
     
            Prefix tp = null;
            for(int i = 0; i < currentShown.size()-1; i++){
                if(currentShown.get(currentShown.size()-1).compareTo(currentShown.get(i))==0){
                    tp = currentShown.get(i);
                    break;
                }
            }
            if(tp != null){
                ArrayList<OptionItem> ppo = tp.options.getAllOptions();
                ArrayList<OptionItem> ppo2 = currentShown.get(currentShown.size()-1).options.getAllOptions();
                
                for(int i = 0; i < ppo.size(); i++){
                    jcb[i][ppo.get(i).getPreference()-1].setSelected(true);
                    ppo2.get(i).setPreference(ppo.get(i).getPreference());
                    trainingPanel.preferenceArray[i] = ppo.get(i).getPreference();
                }
                
                currentShown.get(currentShown.size()-1).rating = tp.rating;
                trainingPanel.jrbA.get(tp.rating-1).setSelected(true);
                trainingPanel.rating = tp.rating;
                trainingPanel.showSame();
            } 
            else{
                for(int i = 0; i < jcb.length; i++){
//                    jcb.get(i).setValue(3);
                    jcb[i][5].setSelected(true);
                }
            }
            
        }
                
        else{
            for(int i = 0; i < jcb.length; i++){
                jcb[i][5].setSelected(true);
            }

        }
    }
    
    public void setPreferenceCB(int index, int value){
        if(currentPlotPoint < numPPPStory-1){
//            int ppid = currentShown.get(currentShown.size()-1).getLast().id;
//            IntegerPlotPoint tp = ppl.getPP(ppid);
//            ArrayList<OptionItem> ppo = tp.getOptions().getAllOptions();
// 
//            ppo.get(index).setPreference(value);
              
            ArrayList<OptionItem> ppo = currentShown.get(currentShown.size()-1).options.getAllOptions();
            ppo.get(index).setPreference(value);
        }

    }
    
    public int getNumOptions(){
        if(currentPlotPoint < numPPPStory-1){
            int ppid = currentShown.get(currentShown.size()-1).getLast().id;
            IntegerPlotPoint tp = ppl.getPP(ppid);
            ArrayList<OptionItem> ppo = tp.getOptions().getAllOptions();
            return ppo.size();
        }
        else
            return 1;
    }
}
