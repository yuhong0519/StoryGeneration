/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package optionTraining.optionPreferencePrediction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import prefix.AllOptions;
import prefix.OptionItem;
import prefix.PPOptions;
import prefix.Prefix;
import tools.PrefixUtil;

/**
 *
 * @author Bunnih
 */
public class OptionListOperation {
    
//    true if the training and testing data only contain the first story (abominable snowman)
    private static boolean firstStoryOnly = true;
//    private static String firstStoryOIList = "par/firstStoryOptionItemList.txt";
    private static String firstStoryOIList = "par/firstTwoStoryOptionItemList.txt";
    
    
//    Create a list of option items in a sequence. 
    private static ArrayList<OptionItem> createOptionItemList(){
        ArrayList<Prefix> prefixList = PrefixUtil.readPrefixList(PrefixUtil.prefixListFile, 1);
        AllOptions ao = PrefixUtil.readOptions(PrefixUtil.optionFile);
        
        ArrayList<OptionItem> optionItemList = new ArrayList<OptionItem>();
        
        PPOptions ppo;
        for(int i = 0; i < prefixList.size(); i++){
            Prefix p = prefixList.get(i);
            if((ppo = ao.getPPOptionsByPPID(p.itemList.get(p.itemList.size()-1).id)) != null){
                for(int j = 0; j < ppo.getAllOptions().size(); j++){
                    OptionItem oi = new OptionItem(ppo.getAllOptions().get(j));
                    oi.setPrefixID(i);
                    optionItemList.add(oi);
                    Collections.sort(optionItemList);
                }
            }
                          
        }
        PrefixUtil.writeOptionItemList(optionItemList, PrefixUtil.optionListFile);
        return optionItemList;
    }
    
    private static OptionList optionList = null;
    /**
     * Get
     * @return 
     */
    public static OptionList getOptionList(){
        if(optionList == null){
            ArrayList<OptionItem> oi = new ArrayList<OptionItem>();
            if(firstStoryOnly){
                PrefixUtil.readOptionItemList(oi, firstStoryOIList);
            }
            else{
                PrefixUtil.readOptionItemList(oi, PrefixUtil.optionListFile);
            }
            optionList = new OptionList(oi);
        }
        return optionList;
         
    }
    
/*  add option id to the options.txt  
 * 
 */
    private static void addOptionID2Option(){
        getOptionList();
        String oldO = "par/options.txt";
        String newO = "par/options2.txt";
        String line;
        ArrayList<OptionItem> oil = optionList.getOptionListArray();
        
        try{
                BufferedReader br = new BufferedReader(new FileReader(oldO));
                BufferedWriter bw = new BufferedWriter(new FileWriter(newO));
//                int PPID = -1;
                
                while((line = br.readLine()) != null){
                    if(line.startsWith("//")){
                        bw.write(line);
                        bw.newLine();
                    }
                    else if(line.startsWith("<")){
                        bw.write(line);
                        bw.newLine();
                    }
                    else{
                        String[] t = line.split(":::");
                        int iPP = Integer.parseInt(t[0]);
                        int oid = -1;
                        for(OptionItem oi : oil){
                            if(oi.getIndicatedPP() == iPP){
                                oid = oi.getOID();
                                break;
                            }
                        }
                        if(oid == -1){
                            System.err.println("Cannot find the option: " + t[1]);
                        }
                        bw.write(""+iPP+":::"+oid+":::"+t[1]);
                        bw.newLine();
                    }
                }
                br.close();
                bw.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            
        }
        
    }
    
    /*  add option id to the quiz.txt  
 * 
 */
    private static void addOptionID2Quiz(){
        getOptionList();
        String oldO = "par/quiz.txt";
        String newO = "par/quiz2.txt";
        String line;
  
        try{
                BufferedReader br = new BufferedReader(new FileReader(oldO));
                BufferedWriter bw = new BufferedWriter(new FileWriter(newO));
//                int PPID = -1;
                
                while((line = br.readLine()) != null){
                    if(line.startsWith("//")){
                        bw.write(line);
                        bw.newLine();
                    }
                    else if(line.startsWith("<")){
                        bw.write(line);
                        bw.newLine();
                    }
                    else{
                        String[] t = line.split(":::");
                        int iPP = Integer.parseInt(t[0]);
 
                        bw.write(""+iPP+":::"+"0"+":::"+t[1]);
                        bw.newLine();
                    }
                }
                br.close();
                bw.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            
        }
        
    }
    
    
    /**
     * create option list for the first story abominable snowman
     */
    private static void createOptionListforStory1(){
        OptionList ol = OptionListOperation.getOptionList();
        ArrayList<OptionItem> noil = ol.getOptionListArray();
        ArrayList<OptionItem> newlist = new ArrayList<OptionItem>();
        for(int i = 0; i < noil.size(); i++){
            if(noil.get(i).getPPID() < 200){
                newlist.add(noil.get(i));
            }
        }
        PrefixUtil.writeOptionItemList(newlist, firstStoryOIList);
    }
    
    public static void main(String[] args){
        
        createOptionListforStory1();
//        createOptionItemList();
//        addOptionID2Option();
//        addOptionID2Quiz();
    }
    
    
    
    
}
