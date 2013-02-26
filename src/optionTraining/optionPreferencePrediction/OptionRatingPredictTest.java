/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package optionTraining.optionPreferencePrediction;

import PPCA.PPCA;
import PPCA.PPCAModel;
import optionTraining.DataCreator;
import optionTraining.Results;
import optionTraining.GetStoryOptionRatingData;
import optionTraining.DataProcess;
import java.util.ArrayList;
import nmf.NMF;
import nmf.NMFModel;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.sparse.CompColMatrix;
import tools.MatrixTools;
import optionTraining.selectionPrediction.NNOptionSelPredict;
import prefix.*;
import tools.CommonUtil;
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
        
    public double[] getPPCAPredict(PPCAModel ppcam, double[] player){
        double[][] td = new double[player.length][1];
        for(int i = 0; i < td.length; i++){
            td[i][0] = player[i];
        }
        CompColMatrix test = CommonUtil.getCompMatrix(new DenseMatrix(td));
        DenseMatrix ret = new DenseMatrix(PPCA.ppca_test(ppcam.cov, ppcam.mean, test));
        return ret.getData();
        
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
                    cPlayerOption = GetStoryOptionRatingData.generatePlayerOptionRatings(player, k);
                    Results r = getAccuracy(player.get(k), getKMeanPredict(centers, cPlayerOption));
                    all.add(r);
                }
            }
          return all;
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
                cPlayerOption = GetStoryOptionRatingData.generatePlayerOptionRatings(player, k);
                Results r = getAccuracy(player.get(k), getNNPredict(data, cPlayerOption, numNeibors));
                all.add(r);
            }
        }        
        return all;
    }
    
   
    /**
     * NMF on word features
     * @param nmfm
     * @param player
     * @param num
     * @return 
     */
    public double[] getRedNMFPredict(NMFModel nmfm, ArrayList<Prefix> player, int num){
        double[] cPlayerOption = OptionRatingPredictTrain.getRedPlayerTrainData(player, num);
        double[] prePlayer = GetStoryOptionRatingData.generatePlayerOptionRatings(player, num);
        double[] playerVector = getNMFPredict(nmfm, cPlayerOption);
        OptionList ol = OptionListOperation.getOptionList();
        double[] predict = new double[ol.getOptionListArray().size()];
        OptionRepresentationGenerator o = OptionRepresentationGenerator.getInstance();
        double[][] sageOptionRep = o.getOptionSAGERep();
        for(int i = 0; i < predict.length; i++){
            predict[i] = MatrixTools.innerProduct(playerVector, sageOptionRep[i]);
            if(prePlayer[i] != 0){
                predict[i] = prePlayer[i];
            }
        }        
        return predict;               
    }
    
    /**
     * NMF on both rating features and word features
     * @param nmfm
     * @param player
     * @param num
     * @return 
     */
    public double[] getAllNMFPredict(NMFModel nmfm, ArrayList<Prefix> player, int num){
        double[] cPlayerOption = OptionRatingPredictTrain.getAllPlayerTrainData(player, num);
        double[] prePlayer = GetStoryOptionRatingData.generatePlayerOptionRatings(player, num);
        double[] playerVector = getNMFPredict(nmfm, cPlayerOption);
        OptionList ol = OptionListOperation.getOptionList();
        
        double[] predict = new double[ol.getOptionListArray().size()];
        OptionRepresentationGenerator o = OptionRepresentationGenerator.getInstance();
        double[][] sageOptionRep = o.getOptionSAGERep();
        
        double[] wordPredict = MatrixTools.subVector(playerVector, predict.length, playerVector.length);
        for(int i = 0; i < predict.length; i++){
            predict[i] = RatingFeatureWeight * playerVector[i] + (1-RatingFeatureWeight) * MatrixTools.innerProduct(wordPredict, sageOptionRep[i]);
            if(prePlayer[i] != 0){
                predict[i] = prePlayer[i];
            }
        }        
        return predict;               
    }
        
    
    private Results redNMFTest(NMFModel nmfm){
        Results all = new Results();
//        diff = 0;
        for(int j = 0; j < testData.size(); j++){
            System.out.println("Process player: " + j);
            ArrayList<Prefix> player = testData.get(j);
            for(int k = startPredictNum; k < player.size()-1; k++){
                
                if(player.get(k).options == null){
                    continue;
                }
//                double[] cPlayerOption = OptionRatingPredictTrain.getRedPlayerTrainData(player, k);
//                double[] playerVector = getNMFPredict(nmfm, cPlayerOption);
                Results r = getAccuracy(player.get(k), getRedNMFPredict(nmfm, player, k));
                all.add(r);
            }
        }        
        return all;        
              
    }
    
    private Results allNMFTest(NMFModel nmfm){
        Results all = new Results();
//        diff = 0;
        for(int j = 0; j < testData.size(); j++){
            System.out.println("Process player: " + j);
            ArrayList<Prefix> player = testData.get(j);
            for(int k = startPredictNum; k < player.size()-1; k++){
                
                if(player.get(k).options == null){
                    continue;
                }
                Results r = getAccuracy(player.get(k), getAllNMFPredict(nmfm, player, k));
                all.add(r);
            }
        }        
        return all;        
              
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
                cPlayerOption = GetStoryOptionRatingData.generatePlayerOptionRatings(player, k);
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
            int ind = ol.getOptionItemPosition(ppo.getAllOptions().get(i));
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
//     0: kmean. 1: Nearest neighbor. 2: rating feature NMF 3:word feature  NMF 4: all NMF
    static int algorithm = 4;
    static int KmeanCluster = 2;
    static int NNnum = 20;
    static int NMFdim = 1;
    
    private double RatingFeatureWeight = 0.7;
    double diffAdd = 0.12;
    double diff = 0.15;
    
    
    public static void main(String[] args){

        ArrayList<ArrayList> allprefix = PrefixUtil.readAllStoryRatingsWOptions(PrefixUtil.storyRatingTrainingFolder, PrefixUtil.optionRatingTrainingFolder);
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
            
// NMF rating test
            else if(algorithm == 2){
                NMFModel nmfm = orpt.NMFTrain(NMFdim);
                r = opt.NMFTest(nmfm);
            }
// NMF word feature test
            else if(algorithm == 3){
                NMFModel nmfm = orpt.redNMFTrain(NMFdim);
                r = opt.redNMFTest(nmfm);
            }
// NMF all feature test
            else if(algorithm == 4){
                NMFModel nmfm = orpt.allNMFTrain(NMFdim);
                r = opt.allNMFTest(nmfm);
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
