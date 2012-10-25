/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package optionTrainingProcess.selectionPrediction;

/**
 *
 * @author Hong Yu
 */
import java.util.*;
import nmf.*;
import no.uib.cipr.matrix.*;
import no.uib.cipr.matrix.sparse.*;
import optionTrainingProcess.*;
import optionTrainingProcess.optionPreferencePrediction.kmean.MatrixTools;
import prefix.*;
import tools.*;

public class NMFPredict {
    private int NMFdim = 5;
//    static int numNeighbors = 16;
    static int numReadStories = 0;
    static final int numPlotPoints = 6;
    static final int maxRating = 1;
    static final int minRating = 0;
    
    boolean trainNMF = true;
    

    
    NMFModel trainNMF(ArrayList<float[]> train){
//       int numPlayers = train.size();
       double splitP = 0.8;
       double[][] allData = new double[train.get(0).length][train.size()];
       double[][] trainData = new double[allData.length][allData[0].length];
       double[][] validateData = new double[allData.length][allData[0].length];
       
       for(int i = 0; i < train.size(); i++){
           float[] p = train.get(i);
           for(int j = 0; j < p.length; j++){
                allData[j][i] = p[j];
           }
       }
       MatrixTools.split(allData, trainData, validateData, splitP);
       
       NMFModel nmfm = new NMFModel();
       nmfm.maxRating = maxRating;
       nmfm.minRating = minRating;
       nmfm.dim = NMFdim;
       
       NMF.nmf_train(new CompColMatrix(new DenseMatrix(trainData)), new CompColMatrix(new DenseMatrix(validateData)), nmfm);

       return nmfm;
    } 
    
    float[] testNMF(NMFModel nmfModel, float[] testPlayer){
        float[] ret = new float[testPlayer.length];
        double[][] data = new double[testPlayer.length][1];
        for(int i = 0; i < testPlayer.length; i++){
            data[i][0] = testPlayer[i];
        }
        DenseMatrix test = new DenseMatrix(data);

        DenseMatrix predict = NMF.nmf_test(nmfModel, test);
        double[] p = predict.getData();
        for(int i = 0; i < testPlayer.length; i++){
            ret[i] = (float)(p[i]);
//            ret[i] = 0.01f;
//            if(testPlayer[i] != 0){
//                ret[i] = testPlayer[i];
//            }
        }        
        return ret;
    }
    
    Results playerPrediction(NMFModel nmfModel, ArrayList<Prefix> player){
        Results r = new Results();
//        int unknown = 0, numCorrect = 0, numWrong = 0;
        int startnum = numPlotPoints*numReadStories-1;
        if(startnum < 0){
            startnum = 0;
        }
        for(int i = startnum; i < player.size()-1; i++){
            PPOptions ppo = player.get(i).options;
            if(ppo == null || ppo.getAllOptions().size() < 2){
                continue;
            }
            
            ProbabilityModel pm = DataCreator.computePlayerProbModel(player, i);
            float[] pmv = pm.modelVector();
            float[] predictPlayer = testNMF(nmfModel, pmv);
            ProbabilityModel newPM = new ProbabilityModel(predictPlayer);
                
            int numOptions = ppo.getAllOptions().size()-2;
            int numBefore = DataProcess.getExistNum(player, player.get(i), i);
            int numPositions = player.get(i).itemList.size()-1;

            int selectedPreference = player.get(i).options.getOptionItemPreferencePosition(player.get(i+1).getLast().id);

            int predict = newPM.getPrediction(numBefore, numPositions, numOptions);
            if(predict < 0){
                r.numUnknown++;
            }
            else if(predict == selectedPreference -1){
//                int tn = newPM.getNumSamePrediction(numBefore, numPositions, numOptions, predict);
//                float c = 1f/tn;
//                r.numCorrect += c;
//                r.numWrong += 1-c;
                r.numCorrect++;
            }else{
                r.numWrong++;
            }
        }        
        
        return r;
    }
   
    void startProcess(){
        ArrayList<ArrayList> alltrain = new ArrayList<ArrayList>();
        ArrayList<ArrayList> alltest = new ArrayList<ArrayList>();
        ArrayList<ArrayList> alltestData = DataCreator.createProbVectorData(alltrain, alltest);
        Results allResults = new Results();
        NMFModel nmfm;
        for(int iter = 0; iter < alltrain.size(); iter++){
            System.out.println("Iteration: " + iter + ", ******************************");
            ArrayList<float[]> train = alltrain.get(iter);
            ArrayList<ArrayList> test = alltestData.get(iter);
            
            if(trainNMF){
                nmfm = trainNMF(train);
                CommonUtil.printObject(nmfm.H, "optionModel/optionPrediction_H" + "_dim" + NMFdim +"_"+ iter+".txt");
                CommonUtil.printObject(nmfm.W, "optionModel/optionPrediction_W" + "_dim" + NMFdim +"_" +iter +".txt");            
            }
                    
            else{
                nmfm = new NMFModel();
                nmfm.dim = NMFdim;
                nmfm.maxRating = maxRating;
                nmfm.minRating = minRating;
                nmfm.W = new DenseMatrix(CommonUtil.readData("optionModel/optionPrediction_W"  + "_dim" + NMFdim + "_" + iter +".txt", false));
//                 nmfm.W = new DenseMatrix(CommonUtil.readData("optionModel/optionPrediction_W"  + iter +".txt", false));
                
            }
            for(int i = 0; i < test.size(); i++){
                ArrayList<Prefix> player = test.get(i);
                Results r = playerPrediction(nmfm, player);
                allResults.add(r);
                
            }
            System.out.println("Current: Correct: "+allResults.numCorrect+", Wrong: "+allResults.numWrong + ", Unknown: "+allResults.numUnknown 
                    + ", Accuracy: " + ((float)(allResults.numCorrect)/(allResults.numCorrect+allResults.numWrong+allResults.numUnknown)));    
            
        }
        
        System.out.println("All: Correct: "+allResults.numCorrect+", Wrong: "+allResults.numWrong + ", Unknown: "+allResults.numUnknown);
        float p1 = (float)(allResults.numCorrect)/(allResults.numCorrect+allResults.numWrong+allResults.numUnknown);
        float p2 = (float)(allResults.numUnknown)/(allResults.numCorrect+allResults.numWrong+allResults.numUnknown);
        
        System.out.println("Correct: "+p1+", Unknown: "+p2);
    }
    
        
    public static void main(String[] args){
        NMFPredict nmfp = new NMFPredict();
        nmfp.startProcess();
    }
    
}
