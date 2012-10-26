/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package optionTrainingProcess.optionPreferencePrediction;

import java.util.ArrayList;
import nmf.NMF;
import nmf.NMFModel;
import no.uib.cipr.matrix.DenseMatrix;
import optionTrainingProcess.*;
import optionTrainingProcess.optionPreferencePrediction.kmean.MatrixTools;
import optionTrainingProcess.selectionPrediction.NearestNeighbor;
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
    
    private double[] getKMeanPredict(double[][] centers, double[] player){
        int nearest = MatrixTools.getNearest(centers, player);
        return centers[nearest];
    }
    
    public Results KMeanTest(double[][] centers){
        Results all = new Results();
          for(int j = 0; j < testData.size(); j++){
                ArrayList<Prefix> player = testData.get(j);
                for(int k = startPredictNum; k < player.size()-1; k++){
                    if(player.get(k).options == null){
                        continue;
                    }
                    double[] cPlayerOption;
                    cPlayerOption = GenerateStoryOptionRatingData.generateOptionRatings(player, k);
                    Results r = getAccuracy(player.get(k), getKMeanPredict(centers, cPlayerOption));
                    all.add(r);
                }
            }
          return all;
    }
    
    private double[] getNNPredict(double[][] neibors, double[] player, int numNeibors){
        NearestNeighbor nn = new NearestNeighbor();
        double[][] n = nn.computeNeighbor(neibors, player, numNeibors);
        double[] ret = new double[player.length];
        for(int i = 0; i < ret.length; i++){
            for(int j = 0; j < n.length; j++){
                ret[i] += n[j][i];
            }
        }
        for(int i = 0; i < ret.length; i++){
            ret[i] /= n.length;
        }
        return ret;
    }
    public Results NearestNeighborTest(double[][] data){
        int numNeibors = 11;
        Results all = new Results();
        for(int j = 0; j < testData.size(); j++){
            ArrayList<Prefix> player = testData.get(j);
            for(int k = startPredictNum; k < player.size()-1; k++){
                if(player.get(k).options == null){
                    continue;
                }
                double[] cPlayerOption;
                cPlayerOption = GenerateStoryOptionRatingData.generateOptionRatings(player, k);
                Results r = getAccuracy(player.get(k), getNNPredict(data, cPlayerOption, numNeibors));
                all.add(r);
            }
        }        
        return all;
    }
    
    private double[] getNMFPredict(NMFModel nmfm, double[] player){
        double[][] td = new double[player.length][1];
        for(int i = 0; i < td.length; i++){
            td[i][0] = player[i];
        }
        DenseMatrix test = new DenseMatrix(td);
        DenseMatrix predict = NMF.nmf_test(nmfm, test);
        double[] p = predict.getData();
        return p;
    }
    
    public Results NMFTest(NMFModel nmfm){
        Results all = new Results();
        for(int j = 0; j < testData.size(); j++){
            System.out.println("Process player: " + j);
            ArrayList<Prefix> player = testData.get(j);
            for(int k = startPredictNum; k < player.size()-1; k++){
                
                if(player.get(k).options == null){
                    continue;
                }
                double[] cPlayerOption;
                cPlayerOption = GenerateStoryOptionRatingData.generateOptionRatings(player, k);
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
    
    double diff = 0.5;
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
            }
        }
        return r;
    }
    
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
//Kmean test
//            double[][] centers = orpt.KMeanClusterTrain();
//            Results r = opt.KMeanTest(centers);
// Nearest neighbor test
//            double[][] data = orpt.NearestNeighborTrain();
//            Results r = opt.NearestNeighborTest(data);
//            all.add(r);
            
// NMF test
            NMFModel nmfm = orpt.NMFTrain();
            Results r = opt.NMFTest(nmfm);
            all.add(r);
        }
        float accuracy = all.numCorrect / (all.numCorrect + all.numWrong);
        System.out.println("Right: " + all.numCorrect + ", Wrong: " + all.numWrong + ", Accuracy: " + accuracy);
    }
    
}
