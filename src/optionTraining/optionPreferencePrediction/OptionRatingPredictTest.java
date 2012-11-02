/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package optionTraining.optionPreferencePrediction;

import optionTraining.DataCreator;
import optionTraining.Results;
import optionTraining.GenerateStoryOptionRatingData;
import optionTraining.DataProcess;
import java.util.ArrayList;
import nmf.NMF;
import nmf.NMFModel;
import no.uib.cipr.matrix.DenseMatrix;
import optionTraining.kmean.MatrixTools;
import optionTraining.selectionPrediction.NNOptionSelPredict;
import prefix.*;
import tools.PrefixUtil;

/**
 *
 * @author Bunnih
 */
public class OptionRatingPredictTest {
    private ArrayList<ArrayList> testData = null;
    private int startPredictNum = 0;
    
    public OptionRatingPredictTest(ArrayList<ArrayList> testData){
        this.testData = testData;
    }
    
    public double[] getKMeanPredict(double[][] centers, double[] player){
        int nearest = MatrixTools.getNearest(centers, player);
        double[] ret = new double[player.length];
        
        for(int i = 0; i < ret.length; i++){
            if(player[i]  != 0){
                ret[i] = player[i];
            }
            else {
                ret[i]  = centers[nearest][i];
            }
        }
        
        return ret;
    }
    
    private Results KMeanTest(double[][] centers){
        Results all = new Results();
          for(int j = 0; j < testData.size(); j++){
                ArrayList<Prefix> player = testData.get(j);
                for(int k = startPredictNum; k < player.size()-1; k++){
                    if(player.get(k).options == null){
                        continue;
                    }
                    double[] cPlayerOption;
                    cPlayerOption = GenerateStoryOptionRatingData.generatePlayerOptionRatings(player, k);
                    Results r = getAccuracy(player.get(k), getKMeanPredict(centers, cPlayerOption));
                    all.add(r);
                }
            }
          return all;
    }
    
    public double[] getNNPredict(double[][] neibors, double[] player, int numNeibors){
        NNOptionSelPredict nn = new NNOptionSelPredict();
        double[][] n = nn.computeNeighbor(neibors, player, numNeibors);
        double[] ret = new double[player.length];
        for(int i = 0; i < ret.length; i++){
            for(int j = 0; j < n.length; j++){
                ret[i] += n[j][i];
            }
        }
        for(int i = 0; i < ret.length; i++){
            ret[i] /= n.length;
            if(player[i] != 0){
                ret[i] = player[i];
            }
        }
        return ret;
    }
    
    private Results NearestNeighborTest(double[][] data, int numNeibors){
//        int numNeibors = 11;
        Results all = new Results();
        for(int j = 0; j < testData.size(); j++){
            ArrayList<Prefix> player = testData.get(j);
            for(int k = startPredictNum; k < player.size()-1; k++){
                if(player.get(k).options == null){
                    continue;
                }
                double[] cPlayerOption;
                cPlayerOption = GenerateStoryOptionRatingData.generatePlayerOptionRatings(player, k);
                Results r = getAccuracy(player.get(k), getNNPredict(data, cPlayerOption, numNeibors));
                all.add(r);
            }
        }        
        return all;
    }
    
    public double[] getNMFPredict(NMFModel nmfm, double[] player){
        double[][] td = new double[player.length][1];
        for(int i = 0; i < td.length; i++){
            td[i][0] = player[i];
        }
        DenseMatrix test = new DenseMatrix(td);
        DenseMatrix predict = NMF.nmf_test(nmfm, test);
        double[] p = predict.getData();
        return p;
    }
    
    private Results NMFTest(NMFModel nmfm){
        Results all = new Results();
        diff = 0;
        for(int j = 0; j < testData.size(); j++){
            System.out.println("Process player: " + j);
            ArrayList<Prefix> player = testData.get(j);
            for(int k = startPredictNum; k < player.size()-1; k++){
                
                if(player.get(k).options == null){
                    continue;
                }
                double[] cPlayerOption;
                cPlayerOption = GenerateStoryOptionRatingData.generatePlayerOptionRatings(player, k);
                Results r = getAccuracy(player.get(k), getNMFPredict(nmfm, cPlayerOption));
                all.add(r);
            }
        }        
        return all;        
        
    }
    
    private Results getAccuracy(Prefix p, double[] predict){
        PPOptions ppo = p.options;
        OptionList ol = OptionListOperation.getOptionList();
        double[] playerPref = new double[ppo.getAllOptions().size()];
        double[] predictPref = new double[ppo.getAllOptions().size()];
        
        for(int i = 0; i < ppo.getAllOptions().size(); i++){
            int ind = ol.getOptionItemID(ppo.getAllOptions().get(i));
            predictPref[i] = predict[ind];
            playerPref[i] = ppo.getAllOptions().get(i).getPreference();
        }
        return computeCorrectNum(playerPref, predictPref);
    }
    

    
    
    private Results computeCorrectNum(double[] player, double[] predict){
        Results r = new Results();
        
        for(int i = 0; i < player.length; i++){
            for(int j = i+1; j < player.length; j++){
//                if(player[i]>player[j] && predict[i]>predict[j] || player[i]<player[j] && predict[i]<predict[j] || player[i]==player[j] && predict[i]==predict[j]){
                if(player[i]>player[j] && (predict[i]-predict[j])>diff || 
                        player[i]<player[j] && (predict[i]-predict[j])<-1*diff || 
                        player[i]==player[j] && Math.abs(predict[i]-predict[j])<=diff){
                    r.numCorrect++;
                }
                else{
                    r.numWrong++;
                }
                if(player[i] == player[j] && Math.abs(predict[i] - predict[j]) > diff){
                    diff += diffAdd;
                }
                else if(player[i] != player[j] && Math.abs(predict[i] - predict[j]) <= diff){
                    diff -= diffAdd;
                    if(diff < 0){
                        diff = 0;
                    }
                }
                
            }
            
            r.addMSE((player[i]-predict[i])*(player[i]-predict[i]));
        }
        return r;
    }
//     0: kmean. 1: Nearest neighbor. 2: NMF
    static int algorithm = 0;
    static int KmeanCluster = 2;
    static int NNnum = 20;
    static int NMFdim = 2;
    
    double diffAdd = 0.12;
    double diff = 0.15;
    
    
    public static void main(String[] args){

        ArrayList<ArrayList> allprefix = DataProcess.readAllStoryRatingsWOptions(PrefixUtil.ratingWOptionTrainingFolder, PrefixUtil.optionTrainingFolder);
        int[][] splitD = tools.CommonUtil.readIntData(PrefixUtil.trainDataSplitFile);
        Results all = new Results();
        for(int i = 0; i < splitD.length; i++){
            ArrayList<ArrayList> train = new ArrayList<ArrayList>();
            ArrayList<ArrayList> test = new ArrayList<ArrayList>();
            DataCreator.splitData(allprefix, train, test, splitD[i]);
            OptionRatingPredictTrain orpt = new OptionRatingPredictTrain(train);
            OptionRatingPredictTest opt = new OptionRatingPredictTest(test);
            System.out.println("***********Iteration: " + i + " **************************");
            Results r = null;
//Kmean test
            if(algorithm == 0){
                double[][] centers = orpt.KMeanClusterTrain(KmeanCluster);
                r = opt.KMeanTest(centers);
            }
// Nearest neighbor test
            else if(algorithm == 1){
                double[][] data = orpt.NearestNeighborTrain();
                r = opt.NearestNeighborTest(data, NNnum);
            }
            
// NMF test
            else if(algorithm == 2){
                NMFModel nmfm = orpt.NMFTrain(NMFdim);
                r = opt.NMFTest(nmfm);
            }
            all.add(r);            
            float accuracy = all.numCorrect / (all.numCorrect + all.numWrong);
            System.out.println("Right: " + all.numCorrect + ", Wrong: " + all.numWrong + ", Accuracy: " + accuracy);
            System.out.println("RMSE: " + all.getRMSE());
        
        }
        float accuracy = all.numCorrect / (all.numCorrect + all.numWrong);
        System.out.println("Right: " + all.numCorrect + ", Wrong: " + all.numWrong + ", Accuracy: " + accuracy);
        System.out.println("RMSE: " + all.getRMSE());
    }
    
}
