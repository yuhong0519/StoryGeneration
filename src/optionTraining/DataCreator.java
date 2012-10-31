/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package optionTraining;

/**
 *
 * @author Hong Yu
 */
import optionTraining.selectionPrediction.ProbabilityModel;
import java.util.*;
import prefix.*;
import tools.*;

public class DataCreator {
    
    public static ProbabilityModel computeProbModel(ArrayList<ArrayList> train){
        ProbabilityModel pm = new ProbabilityModel();
        for(int i = 0; i < train.size(); i++){
            ArrayList<Prefix> player = train.get(i);
           
            for(int j = 0; j < player.size() - 1; j++){
                PPOptions ppo = player.get(j).options;
                if(ppo == null || ppo.getAllOptions().size() < 2){
                    continue;
                }
                
                int numOptions = ppo.getAllOptions().size()-2;
                int numBefore = DataProcess.getExistNum(player, player.get(j), j);

                int numPosition = player.get(j).itemList.size()-1;
                
                int selectedPreference = player.get(j).options.getOptionItemPreferencePosition(player.get(j+1).getLast().id);
//                pm.model[numBefore][numPosition][numOptions][selectedPreference-1] += 1;
                pm.addOne(numBefore, numPosition, numOptions, selectedPreference-1);
            }
        }
        pm.normalizeProb();
        return pm;
    }
    

    
    public static ProbabilityModel computePlayerProbModel(ArrayList<Prefix> player){
       ProbabilityModel pm = new ProbabilityModel();
       for(int j = 0; j < player.size() - 1; j++){
            PPOptions ppo = player.get(j).options;
            if(ppo == null || ppo.getAllOptions().size() < 2){
                continue;
            }

            int numOptions = ppo.getAllOptions().size()-2;
            int numBefore = DataProcess.getExistNum(player, player.get(j), j);

            int numPosition = player.get(j).itemList.size()-1;

            int selectedPreference = player.get(j).options.getOptionItemPreferencePosition(player.get(j+1).getLast().id);
//                pm.model[numBefore][numPosition][numOptions][selectedPreference-1] += 1;
            pm.addOne(numBefore, numPosition, numOptions, selectedPreference-1);
        }
        pm.normalizeProb();
        return pm;
    }
    
   public static ProbabilityModel computePlayerProbModel(ArrayList<Prefix> player, int num){
       ProbabilityModel pm = new ProbabilityModel();
       for(int j = 0; j < player.size() - 1 && j < num; j++){
            PPOptions ppo = player.get(j).options;
            if(ppo == null || ppo.getAllOptions().size() < 2){
                continue;
            }

            int numOptions = ppo.getAllOptions().size()-2;
            int numBefore = DataProcess.getExistNum(player, player.get(j), j);

            int numPosition = player.get(j).itemList.size()-1;

            int selectedPreference = player.get(j).options.getOptionItemPreferencePosition(player.get(j+1).getLast().id);
//                pm.model[numBefore][numPosition][numOptions][selectedPreference-1] += 1;
            pm.addOne(numBefore, numPosition, numOptions, selectedPreference-1);
        }
        pm.normalizeProb();
        return pm;
    }
    
   public  static ArrayList<float[]> computeProbVectorModel(ArrayList<ArrayList> data){
        ArrayList<float[]> vm = new ArrayList<float[]>();
        for(int i = 0; i < data.size(); i++){
            ArrayList<Prefix> player = data.get(i);
            ProbabilityModel pm = computePlayerProbModel(player);
            float[] tp = pm.modelVector();
            vm.add(tp);
            
        }
        return vm;
        
    }    
    
    public static void splitData(ArrayList<ArrayList> allPrefix, ArrayList<ArrayList> train, ArrayList<ArrayList> test, int[] split){

        for(int i = 0; i < allPrefix.size(); i++){
            if(split[i] == 0){
                train.add(allPrefix.get(i));
            }
            else{
                test.add(allPrefix.get(i));
            }
        }
                       
    }
    
    public static int numIter = 50;
    
//    create training and testing data for option selection predicition. 
//    Output: alltrain, alltest, allTestData
    public static ArrayList<ArrayList> createProbVectorData( ArrayList<ArrayList> alltrain, ArrayList<ArrayList> alltest){
        ArrayList<ArrayList> allprefix = DataProcess.readAllStoryRatingsWOptions(PrefixUtil.ratingWOptionTrainingFolder, PrefixUtil.optionTrainingFolder);
        
        ArrayList<ArrayList> allTestData = new ArrayList<ArrayList>();
//        
        Results r = new Results();
        int[][] splitD = tools.CommonUtil.readIntData(PrefixUtil.trainDataSplitFile);
        
        for(int i = 0; i < numIter; i++){
            ArrayList<ArrayList> train = new ArrayList<ArrayList>();
            ArrayList<ArrayList> test = new ArrayList<ArrayList>();
            allTestData.add(test);
            splitData(allprefix, train, test, splitD[i]);
//            ProbabilityModel pm = DataCreator.computeProbModel(train);
            ArrayList<float[]> trainV = computeProbVectorModel(train);
            ArrayList<float[]> testV = computeProbVectorModel(test);
            alltrain.add(trainV);
            alltest.add(testV);
        }     
        return allTestData;
    }
    
//    alltrain contains ArrayList<ArrayList>s, each of which represents a training set and contains ArrayList<Prefix>, 
//    which contains all the prefixes from one player
    public static void createData( ArrayList<ArrayList> alltrain, ArrayList<ArrayList> alltest){
        ArrayList<ArrayList> allprefix = DataProcess.readAllStoryRatingsWOptions(PrefixUtil.ratingWOptionTrainingFolder, PrefixUtil.optionTrainingFolder);
        
//        ArrayList<ArrayList> allTestData = new ArrayList<ArrayList>();
//        
        Results r = new Results();
        int[][] splitD = tools.CommonUtil.readIntData(PrefixUtil.trainDataSplitFile);
        
        for(int i = 0; i < numIter; i++){
            ArrayList<ArrayList> train = new ArrayList<ArrayList>();
            ArrayList<ArrayList> test = new ArrayList<ArrayList>();
//            allTestData.add(test);
            splitData(allprefix, train, test, splitD[i]);
//            ProbabilityModel pm = DataCreator.computeProbModel(train);
//            ArrayList<float[]> trainV = computeProbVectorModel(train);
//            ArrayList<float[]> testV = computeProbVectorModel(test);
            alltrain.add(train);
            alltest.add(test);
        }     

    }
    
    public static void main(String[] args){
        ArrayList<ArrayList> alltrain = new ArrayList<ArrayList>();
        ArrayList<ArrayList> alltest = new ArrayList<ArrayList>();
        createProbVectorData(alltrain, alltest);
        
    }
    
}
