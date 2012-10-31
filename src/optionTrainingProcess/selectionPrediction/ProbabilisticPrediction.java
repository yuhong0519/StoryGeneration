/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package optionTrainingProcess.selectionPrediction;

/**
 *
 * @author Hong Yu
 */
import optionTrainingProcess.selectionPrediction.ProbabilityModel;
import java.util.*;
import PPCA.*;
import optionTrainingProcess.DataCreator;
import optionTrainingProcess.DataProcess;
import optionTrainingProcess.Results;
import prefix.*;
import tools.*;

public class ProbabilisticPrediction {

    
    static void probModelTest(ArrayList<ArrayList> test, ProbabilityModel pm, Results r){
        int numCorrect = 0;
        int numWrong = 0;
        int unknown = 0;
        
        for(int i = 0; i < test.size(); i++){
            ArrayList<Prefix> player = test.get(i);
           
            for(int j = 0; j < player.size() - 1; j++){
                PPOptions ppo = player.get(j).options;
                if(ppo == null || ppo.getAllOptions().size() < 2){
                    continue;
                }
                
                int numOptions = ppo.getAllOptions().size()-2;
                int numBefore = DataProcess.getExistNum(player, player.get(j), j);
                int numPosition = player.get(j).itemList.size()-1;

                int selectedPreference = player.get(j).options.getOptionItemPreferencePosition(player.get(j+1).getLast().id);
                
                int predict = pm.getPrediction(numBefore, numPosition, numOptions);
                if(predict < 0){
                    unknown++;
                }
                else if(predict == selectedPreference -1){
                    numCorrect++;
                }else{
                    numWrong++;
                }
            }
            
            
            
        }
        System.out.println("Correct: "+numCorrect+", Wrong: "+numWrong + ", Unknown: "+unknown);
        r.numCorrect += numCorrect;
        r.numWrong += numWrong;
        r.numUnknown += unknown;
    }
    
//    static void splitData(ArrayList<ArrayList> allPrefix, ArrayList<ArrayList> train, ArrayList<ArrayList> test){
//        Random r = new Random();
//        double splitProb = 0.7;
//        for(int i = 0; i < allPrefix.size(); i++){
//            if(r.nextDouble() < splitProb){
//                train.add(allPrefix.get(i));
//            }
//            else{
//                test.add(allPrefix.get(i));
//            }
//        }
//                       
//    }
    

   public static int probPredict(ArrayList<ArrayList> train, ArrayList<Prefix> player){     
                  
       ProbabilityModel pm = DataCreator.computeProbModel(train);
       int j = player.size() - 1;
        PPOptions ppo = player.get(j).options;
        if(ppo == null || ppo.getAllOptions().size() < 2){
            return 0;
        }

        int numOptions = ppo.getAllOptions().size()-2;
        int numBefore = DataProcess.getExistNum(player, player.get(j), j);
        int numPosition = player.get(j).itemList.size()-1;


        int predict = pm.getPrediction(numBefore, numPosition, numOptions);
        return predict;
   
   }
    
   static void modelOptionProbabilityModel(){
        ArrayList<ArrayList> allprefix = DataProcess.readAllStoryRatingsWOptions(PrefixUtil.ratingWOptionTrainingFolder, PrefixUtil.optionTrainingFolder);
        int numIter = 50;
        Results r = new Results();
        int[][] splitD = tools.CommonUtil.readIntData(PrefixUtil.trainDataSplitFile);
   
        for(int i = 0; i < numIter; i++){
            ArrayList<ArrayList> train = new ArrayList<ArrayList>();
            ArrayList<ArrayList> test = new ArrayList<ArrayList>();
            DataCreator.splitData(allprefix, train, test, splitD[i]);
            ProbabilityModel pm = DataCreator.computeProbModel(train);
            probModelTest(test, pm, r);
        }
        System.out.println("Correct: "+r.numCorrect+", Wrong: "+r.numWrong + ", Unknown: "+r.numUnknown);
        float p1 = (float)(r.numCorrect)/(r.numCorrect+r.numWrong+r.numUnknown);
        float p2 = (float)(r.numUnknown)/(r.numCorrect+r.numWrong+r.numUnknown);
        
        System.out.println("Correct: "+p1+", Unknown: "+p2);
        
    }
   
     public static void main(String[] args){

            modelOptionProbabilityModel();

        
    }
        
    
}
