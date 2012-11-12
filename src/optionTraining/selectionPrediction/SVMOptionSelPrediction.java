/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package optionTraining.selectionPrediction;

/**
 *
 * @author Hong Yu
 */
import java.util.*;
import prefix.*;
import java.io.*;
import optionTraining.DataCreator;
import optionTraining.DataProcess;
import optionTraining.Results;
import tools.*;

public class SVMOptionSelPrediction {
    int qid = 1;
    int numFeature = 9;
    ArrayList<float[]> computeSVMData(ArrayList<ArrayList> data){
        ArrayList<float[]> SVMData = new ArrayList<float[]>();
        
        
        for(int i = 0; i < data.size(); i++){
            ArrayList<Prefix> player = data.get(i);
            for(int j = 0; j < player.size() - 1; j++){
                PPOptions ppo = player.get(j).options;
                if(ppo == null || ppo.getAllOptions().size() < 2){
                    continue;
                }                
                int numOptions = ppo.getAllOptions().size();
                int numBefore = DataProcess.getExistNum(player, player.get(j), j);

                int numPosition = player.get(j).itemList.size()-1;
                
//                int preferencePosition = player.get(j).options.getOptionItemPreferencePosition(player.get(j+1).getLast().id);
                for(int k = 0; k < ppo.getAllOptions().size(); k++){
                    float[] svmd = new float[numFeature + 2];
//                    String line = "qid:" + qid;
                    if(player.get(j+1).getLast().id == ppo.getAllOptions().get(k).getIndicatedPP()){
//                        line = "" + "5 " + line;
                        svmd[0] = 5;
                    }
                    else{
//                        line = "" + "1 " + line;
                        svmd[0] = 1;
                    }
                    svmd[1] = qid;
                    svmd[2] = numBefore;
                    svmd[3] = numPosition;

                    svmd[4] = ppo.getOptionItemPreferencePosition(ppo.getAllOptions().get(k).getIndicatedPP());
                    svmd[5] = numOptions;
                    svmd[6] = ppo.getAllOptions().get(k).getPreference();
                    svmd[7] = (float)(ppo.getAllOptions().get(k).getPreference()) / ppo.getHighestItemRating();
                    svmd[8] = (float)(ppo.getAllOptions().get(k).getPreference()) / ppo.getLowestItemRating();
                    svmd[9] = ((ppo.getAllOptions().get(k).getPreference()) - ppo.getHighestItemRating());
                    svmd[10] = ((ppo.getAllOptions().get(k).getPreference()) - ppo.getLowestItemRating());
                    
                    SVMData.add(svmd);
                    
                    
                }
                qid++;
                
            }
            
        }
        return SVMData;
        
        
    }
    
    ArrayList<String> transformSVMArray2String(ArrayList<float[]> data){
        ArrayList<String> ret = new ArrayList<String>();
        for(int i = 0; i < data.size(); i++){
            float[] tf = data.get(i);
            String line = "";
            if(algorithm == 1){
                line = "" + (int)(tf[0]) + " qid:" + (int)(tf[1]);
            }
            else if(algorithm == 0){
                if(tf[0] == 5)
                    line = "1";
                else
                    line = "-1";
            }
            for(int j = 2; j < tf.length; j++){
                line = line + " " + (j-1) + ":" + tf[j];
            }
            line = line + " #";
            
            ret.add(line);
            
        }
        return ret;
        
    }
    
    static int numIter = 50;
    static double splitProb = 0.7;
//    1: rank svm, 0: svm
    static int algorithm = 0;
    
    void createSVMData(){
        ArrayList<ArrayList> allprefix = DataProcess.readAllStoryRatingsWOptions(PrefixUtil.ratingWOptionTrainingFolder, PrefixUtil.optionTrainingFolder);
        
        int[][] split = new int[numIter][allprefix.size()];
        Random r = new Random();
        
        for(int i = 0; i < numIter; i++){
            for(int j = 0; j < allprefix.size(); j++){
                if(r.nextDouble() < splitProb){
                    split[i][j] = 0;
                }
                else{
                    split[i][j] = 1;
                }
            }
        }
        tools.CommonUtil.printObject(split, PrefixUtil.trainDataSplitFile);
    }
    
    void process(){
        ArrayList<ArrayList> allprefix = DataProcess.readAllStoryRatingsWOptions(PrefixUtil.ratingWOptionTrainingFolder, PrefixUtil.optionTrainingFolder);
        int[][] splitData = tools.CommonUtil.readIntData(PrefixUtil.trainDataSplitFile);
        
        double right = 0, wrong = 0;
        
        for(int i = 0; i < numIter; i++){
//            System.out.println();
            ArrayList<ArrayList> train = new ArrayList<ArrayList>();
            ArrayList<ArrayList> test = new ArrayList<ArrayList>();
            DataCreator.splitData(allprefix, train, test, splitData[i]);
            
            ArrayList<float[]> trainA = computeSVMData(train);
            ArrayList<float[]> testA = computeSVMData(test);
            normalizeData(trainA, testA);
            
            ArrayList<String> trainSVM = transformSVMArray2String(trainA);
            ArrayList<String> testSVM = transformSVMArray2String(testA);
            writeData2File(trainSVM, "svm/train"+i+".txt");
            writeData2File(testSVM, "svm/test"+i+".txt");
            trainSVM("svm/train"+i+".txt", "svm/model"+i+".txt");
            testSVM("svm/test"+i+".txt", "svm/model"+i+".txt", "svm/predict"+i+".txt");
            float[] svmResults = readSVMResults("svm/predict"+i+".txt", testA.size());
            Results r = compareResults(testA, svmResults);
            right += r.numCorrect;
            wrong += r.numWrong;
        }
        System.out.println("Right: " + right + ", Wrong: " + wrong + ", percent: " + (right/(right+wrong)));
    }
    
    void normalizeData(ArrayList<float[]> train, ArrayList<float[]> test){
        float[] max = new float[train.get(0).length];
        for(int i = 0; i < max.length; i++){
            max[i] = -1;
        }
        for(float[] tp : train){
            for(int i = 2; i < tp.length; i++){
                if(Math.abs(tp[i]) > max[i]){
                    max[i] = Math.abs(tp[i]);
                }
            }
        }
        for(float[] tp : test){
            for(int i = 2; i < tp.length; i++){
                if(Math.abs(tp[i]) > max[i]){
                    max[i] = Math.abs(tp[i]);
                }
            }
        }
        for(float[] tp : train){
            for(int i = 2; i < tp.length; i++){
                tp[i] = tp[i] / max[i];
            }
        }
        for(float[] tp : test){
            for(int i = 2; i < tp.length; i++){
                tp[i] = tp[i] / max[i];
            }
        }
    }
    
    Results compareResults(ArrayList<float[]> test, float[] predict){
        float cid = test.get(0)[1];
        int maxTestID = 0, maxPredictID = 0;
        Results r = new Results();
//        float correct = 0, incorrect = 0;
        float maxTestV = -100, maxPredictV = -100;
        for(int i = 0; i < test.size(); i++){
            if(cid == test.get(i)[1]){
                if(test.get(i)[0] > maxTestV){
                    maxTestID = i;
                    maxTestV = test.get(i)[0];
                }
                
                if(predict[i] > maxPredictV){
                    maxPredictV = predict[i];
                    maxPredictID = i;
                }
                
            }
            else{
                if(maxPredictID == maxTestID){
                    r.numCorrect += 1;
                }
                else if(predict[maxTestID] == predict[maxPredictID]){
                    r.numCorrect += 1;
                   
                }
                else{
                    r.numWrong += 1;
                }
                cid  = test.get(i)[1];
                maxTestID = i;
                maxTestV = test.get(i)[0];
                maxPredictV = predict[i];
                maxPredictID = i;
                
            }
            
        }
        
        return r;
        
    }
    
    float[] readSVMResults(String filename, int numItems){
        float[] results = new float[numItems];
        try{
            BufferedReader bw = new BufferedReader(new FileReader(filename));
            String line;
            int i= 0;
            while((line = bw.readLine()) != null){
                results[i++] = Float.parseFloat(line);               
                
            }
            bw.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return results;
    }
    
    
    void trainSVM(String trainFileName, String modelFile){
         try {
              String line;
              String command = "";
              if(algorithm == 1){
                  command = "svm_learn -z p -t 1 -d 8 " + trainFileName + " " + modelFile;
              }
              else if(algorithm == 0){
                  command = "svm_learn  " + trainFileName + " " + modelFile;
              }
              Process p = Runtime.getRuntime().exec("svm_learn -z p -t 1 -d 8 " + trainFileName + " " + modelFile );
              BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
              BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
              while ((line = bri.readLine()) != null) {
                System.out.println(line);
              }
              bri.close();
              while ((line = bre.readLine()) != null) {
                System.out.println(line);
              }
              bre.close();
              p.waitFor();
              System.out.println("Training Done: " + trainFileName);
        }
        catch (Exception err) {
          err.printStackTrace();
        }
    }
    
    void testSVM(String testFileName, String modelFile, String predictFile){
         try {
              String line;
              Process p = Runtime.getRuntime().exec("svm_classify " + testFileName + " " +  modelFile + " " + predictFile);
              BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
              BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
              while ((line = bri.readLine()) != null) {
                System.out.println(line);
              }
              bri.close();
              while ((line = bre.readLine()) != null) {
                System.out.println(line);
              }
              bre.close();
              p.waitFor();
              System.out.println("Testing Done: " + testFileName);
        }
        catch (Exception err) {
          err.printStackTrace();
        }        
    }
    
    void writeData2File(ArrayList<String> data, String filename){
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            for(String line : data){
                bw.write(line+"\r\n");
                
            }
            bw.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        SVMOptionSelPrediction svmp = new SVMOptionSelPrediction();
//        svmp.createSVMData();
        svmp.process();
    }
            
    
}
