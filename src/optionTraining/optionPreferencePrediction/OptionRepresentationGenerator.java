/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package optionTraining.optionPreferencePrediction;

/**
 *
 * @author Bunnih
 */
import java.util.*;
import optionTraining.GenerateStoryOptionRatingData;
import tools.CommonUtil;

public class OptionRepresentationGenerator {
    private static OptionRepresentationGenerator org = null;
    private OptionList ol = null;
    private Vocabulary vo = null;
    private double[][] BOWRep = null;
    private String SAGEDataFile = "theta_sage30.txt";
    private double[][] SAGEdata = null;
    
    private OptionRepresentationGenerator(){
        ol = OptionListOperation.getOptionList();
        vo = VocabularyOperation.getVocabulary();        
    }
    
    public static OptionRepresentationGenerator getInstance(){
        if(org == null){
            org = new OptionRepresentationGenerator();
//            org.init();
         }

        return org;
    }
    

//    Get original bag-of-words representations of options. 
//    Input: ArrayList<ArrayList> contains ArrayList<Prefix>, which represent a player
    public double[][] getOriginalRepresentation(){
        if(BOWRep != null){
            return BOWRep;
        }
//        return GenerateStoryOptionRatingData.generateOptionRatings(train);
        BOWRep = new double[ol.getOptionListArray().size()][vo.getVocabuloryList().size()];
        
        for(int i = 0; i < ol.getOptionListArray().size(); i++){
            String optionString = ol.getOptionListArray().get(i).getValue();
            String[] words = optionString.split(" ");
            for(int j = 0; j < words.length; j++){
                int index = vo.getWordIndex(words[j]);
                if(index >= 0){
                    BOWRep[i][index] += 1;
                }
            }
        }
        
        return BOWRep;
    }
    
//    return: option list size by reduced dimension
    public double[][] getOptionSAGERep(){
        if(SAGEdata == null){
            SAGEdata = CommonUtil.readData(SAGEDataFile, false);
        }
        return SAGEdata;
    }
    
//    Get representation of options with PCA dimension reduction
    public void getPCARepresentation(){
        
    }
    
    public static void main(String[] args){
        OptionRepresentationGenerator o = OptionRepresentationGenerator.getInstance();
        double[][] t = o.getOriginalRepresentation();
//        tools.CommonUtil.printObject(t, "BOW.txt");
    }
}
