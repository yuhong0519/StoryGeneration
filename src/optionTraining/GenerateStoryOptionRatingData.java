/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package optionTraining;
import optionTraining.optionPreferencePrediction.OptionList;
import optionTraining.optionPreferencePrediction.OptionListOperation;
import java.io.*;
import java.util.*;
import prefix.*;
import tools.CommonUtil;
import tools.PrefixUtil;
/**
 *
 * @author Hong Yu
 */
public class GenerateStoryOptionRatingData {
    
    
//    create option rating file, which contains the matrix: number of options * number of players 
    static void generateAllOptionRatings(){
        ArrayList<OptionItem> optionItemList = OptionListOperation.getOptionList().getOptionListArray();
        ArrayList<ArrayList> storyWOptions = DataProcess.readAllStoryRatingsWOptions(PrefixUtil.ratingWOptionTrainingFolder, PrefixUtil.optionTrainingFolder );
        double data[][] = generateOptionRatings(storyWOptions);
//                new double[optionItemList.size()][storyWOptions.size()];
        
//        for(int i = 0; i < storyWOptions.size(); i++){
//            ArrayList<Prefix> people = storyWOptions.get(i);
//            for(int j = 0; j < people.size(); j++){
//                Prefix p = people.get(j);
//                PPOptions ppo = p.options;
//                if(ppo == null){
//                    continue;
//                }
//                for(int k = 0; k < ppo.getAllOptions().size(); k++){
//                    OptionItem oi = ppo.getAllOptions().get(k);
//                    int index = Collections.binarySearch(optionItemList, oi);
//                    data[index][i] = oi.getPreference();
//                }
//            }
//            
//        }
        CommonUtil.printObject(data, PrefixUtil.optionRatingFile);
    }
    
//    returns number of options by number of players (size of trainData)
    public static double[][]  generateOptionRatings(ArrayList<ArrayList> trainData){
//        ArrayList<OptionItem> optionItemList = OptionListOperation.getOptionList().getOptionListArray();
        OptionList ol = OptionListOperation.getOptionList();
        double data[][] = new double[ol.getOptionListArray().size()][trainData.size()];
        
        for(int i = 0; i < trainData.size(); i++){
            ArrayList<Prefix> people = trainData.get(i);
            for(int j = 0; j < people.size(); j++){
                Prefix p = people.get(j);
                PPOptions ppo = p.options;
                if(ppo == null){
                    continue;
                }
                for(int k = 0; k < ppo.getAllOptions().size(); k++){
                    OptionItem oi = ppo.getAllOptions().get(k);
//                    int index = Collections.binarySearch(optionItemList, oi);
                    int index = ol.getOptionItemID(oi);
                    data[index][i] = oi.getPreference();
                }
            }
            
        }
        return data;
    }
    
    public static double[]  generateOptionRatings(ArrayList<Prefix> trainData, int num){
        ArrayList<OptionItem> optionItemList = OptionListOperation.getOptionList().getOptionListArray();
        double data[] = new double[optionItemList.size()];
                   
        for(int j = 0; j < trainData.size() && j < num; j++){
            Prefix p = trainData.get(j);
            PPOptions ppo = p.options;
            if(ppo == null){
                continue;
            }
            for(int k = 0; k < ppo.getAllOptions().size(); k++){
                OptionItem oi = ppo.getAllOptions().get(k);
                int index = Collections.binarySearch(optionItemList, oi);
                data[index] = oi.getPreference();
            }
        }
            
        
        return data;
    }

    
//    Generate rating file, number of prefixes * number of players
    static void generatePrefixRatingData(String path){
        ArrayList<Prefix> prefixList = PrefixUtil.readPrefixList(PrefixUtil.prefixListFile, 1);
//        String path = "ratings_wOptionTraining";
        File folder = new File(path);
        String[] files = folder.list();
        try{
            String line;

            double data[][] = new double[prefixList.size()][files.length];
            for (int i = 0; i < files.length; i++) {
                BufferedReader br = new BufferedReader(
                      new FileReader(path + "\\" + files[i]));
                while((line = br.readLine()) != null){
                      String[] t = line.split("\t");
                      int[] list = new int[t.length-1];
                      for(int j = 0; j < list.length; j++){
                            list[j] = Integer.parseInt(t[j+1]);
                      }
                      Prefix pi = new Prefix(list);
                      int id = Collections.binarySearch(prefixList, pi);
                      if(id >= 0){
                            data[id][i] = Integer.parseInt(t[0].substring(0, t[0].length()-1));
                      }
                      else{
                          System.out.println("Error!!!!!!!!!");
                      }
                }
                br.close();
            }
            CommonUtil.printObject(data, PrefixUtil.allRatingFile);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public static void main(String[] args){
//        generateRatingData("ratings_wOptionTraining");
        
        
        generateAllOptionRatings();
    }
}
