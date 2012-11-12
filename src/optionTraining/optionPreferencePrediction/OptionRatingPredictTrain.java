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
import nmf.*;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.sparse.CompColMatrix;
import optionTraining.GenerateStoryOptionRatingData;
import optionTraining.kmean.KMean;
import prefix.OptionItem;
import prefix.PPOptions;
import prefix.Prefix;
import tools.MatrixTools;

public class OptionRatingPredictTrain {
    private ArrayList<ArrayList> trainData = null;
    private int KmeanClass = 1;
    static double max = 0, min = 10;
    static int maxRating = 5;
    
    public OptionRatingPredictTrain(ArrayList<ArrayList> trainData){
        this.trainData = trainData;
    }
    

//    trainsform training data to option rating matrix: number of option items * number of players
    public double[][] getOptionRatings(){
        double[][] optionRatings = GenerateStoryOptionRatingData.generateOptionRatings(trainData);
        return optionRatings;
    }
    
    public double[][] KMeanClusterTrain(){
        double[][] data = getOptionRatings();
        return KMean.kmean(data, KmeanClass);
        
    }
    
    public double[][] KMeanClusterTrain(int numC){
        KmeanClass = numC;
        double[][] data = getOptionRatings();
        return KMean.kmean(data, KmeanClass);
        
    }
    
//    return number of players * number of option items
    public double[][] NearestNeighborTrain(){
        return MatrixTools.transpose(getOptionRatings());
    }
    
    public NMFModel NMFTrain(int nmfDim){
        double[][] data = getOptionRatings();
//        percent of training vs. validation data
        double splitP = 0.9;
//        int nmfDim = 5;
        NMFModel nmfm = new NMFModel();
        nmfm.dim = nmfDim;
        double[][] NMFtrainData = new double[data.length][data[0].length];
        double[][] NMFvalidateData = new double[data.length][data[0].length];
        MatrixTools.split(data, NMFtrainData, NMFvalidateData, splitP);
        NMF.nmf_train(new CompColMatrix(new DenseMatrix(NMFtrainData)), new CompColMatrix(new DenseMatrix(NMFvalidateData)), nmfm);
       return nmfm;
        
        
    }
    
    public static double[] getRedPlayerTrainData(ArrayList<Prefix> player, int num){
        OptionRepresentationGenerator o = OptionRepresentationGenerator.getInstance();
        double[][] sageOptionRep = o.getOptionSAGERep();
        double[] train = new double[sageOptionRep[0].length];
        double[] count = new double[train.length];
        OptionList ol = OptionListOperation.getOptionList();
        
        for(int j = 0; j < player.size() && j < num; j++){
            Prefix p = player.get(j);
            PPOptions ppo = p.options;
            if(ppo == null){
                continue;
            }
            ArrayList<OptionItem> oil = ppo.getAllOptions();
            for(int k = 0; k < oil.size() ; k++){
                OptionItem oi = oil.get(k);
                int pos = ol.getOptionItemID(oi);
                if(pos >= 0 && pos < sageOptionRep.length){
                    double[] sageClass = sageOptionRep[pos];
                    for(int l = 0; l < sageClass.length; l++){
                        if(sageClass[l] != 0){
                            train[l] += sageClass[l] * oi.getAccuratePreference();
                            count[l] += 1;
                        }
                    }
                }
                else{
                    System.err.println("Error: Cannot find the option item");
                }
            }
        }
        for(int i = 0; i < train.length; i++){
           
                if(count[i] != 0){
                    train[i] /= count[i];
                }
            
        }
        for(int i = 0; i < train.length; i++){
            if(train[i] > max){
                max = train[i];
            }
            if(train[i] < min){
                min = train[i];
            }            
        }
        if(max == min){
            max = min + 0.001;
        }
        double scale = (maxRating-1) / (max - min);
        for(int i = 0; i < train.length; i++){
            train[i] = (train[i]-min) * scale + 1;
         }
        return train;
    }
    


    /**
     *
     * @param trainA Contains all players: ArrayList<Prefix>s
     * @return word feature: number of word feature * number of players
     */
    public static double[][] getRedTrainData(ArrayList<ArrayList> trainA){
        OptionRepresentationGenerator o = OptionRepresentationGenerator.getInstance();
        double[][] sageOptionRep = o.getOptionSAGERep();
        double[][] train = new double[trainA.size()][sageOptionRep[0].length];
        int[][] count = new int[trainA.size()][sageOptionRep[0].length];
        OptionList ol = OptionListOperation.getOptionList();
        
        for(int i = 0; i < trainA.size(); i++){
            ArrayList<Prefix> player = trainA.get(i);
            for(int j = 0; j < player.size(); j++){
                Prefix p = player.get(j);
                PPOptions ppo = p.options;
                if(ppo == null){
                    continue;
                }
                ArrayList<OptionItem> oil = ppo.getAllOptions();
                for(int k = 0; k < oil.size(); k++){
                    OptionItem oi = oil.get(k);
                    int pos = ol.getOptionItemID(oi);
                    if(pos >= 0 && pos < sageOptionRep.length){
                        double[] sageClass = sageOptionRep[pos];
                        for(int l = 0; l < sageClass.length; l++){
                            if(sageClass[l] != 0){
                                train[i][l] += sageClass[l] * oi.getAccuratePreference();
                                count[i][l] += 1;
                            }
                        }
                    }
                    else{
                        System.err.println("Error: Cannot find the option item");
                    }
                }
            }
        }
        
        for(int i = 0; i < train.length; i++){
            for(int j = 0; j < train[i].length; j++){
                if(count[i][j] != 0){
                    train[i][j] /= count[i][j];
                }
            }
        }

        for(int i = 0; i < train.length; i++){
            for(int j = 0; j < train[i].length; j++){
                if(train[i][j] > max){
                    max = train[i][j];
                }
                if(train[i][j] < min){
                    min = train[i][j];
                }
            }
        }
        if(max == min){
            max = min + 0.001;
        }
        double scale = (maxRating-1) / (max - min);
        for(int i = 0; i < train.length; i++){
            for(int j = 0; j < train[i].length; j++){
                train[i][j] = (train[i][j]-min) * scale + 1;
            }
        }
                
        return MatrixTools.transpose(train);
    }
        
//    Train NMF using word feature
    public NMFModel redNMFTrain(int nmfDim){
//        if(nmfm != null){
//            return nmfm;
//        }
        double splitP = 0.9;
        double[][] data = getRedTrainData(trainData);
        NMFModel nmfm = new NMFModel();
        nmfm.dim = nmfDim;
        double[][] NMFtrainData = new double[data.length][data[0].length];
        double[][] NMFvalidateData = new double[data.length][data[0].length];
        MatrixTools.split(data, NMFtrainData, NMFvalidateData, splitP);
        NMF.nmf_train(new CompColMatrix(new DenseMatrix(NMFtrainData)), new CompColMatrix(new DenseMatrix(NMFvalidateData)), nmfm);
       return nmfm;
               
    }
    
    
    public static double[] getAllPlayerTrainData(ArrayList<Prefix> player, int num){
        double[] wordData = getRedPlayerTrainData(player, num);
        double[] ratingData =  GenerateStoryOptionRatingData.generatePlayerOptionRatings(player, num);
        double[] ret = new double[ratingData.length + wordData.length];
        System.arraycopy(ratingData, 0, ret, 0, ratingData.length);
        System.arraycopy(wordData, 0, ret, ratingData.length, wordData.length);
        return ret;
        
    }
    
    /**
     * @return all training feature for NMF, including option rating feature and word feature
     */
    public double[][] getAllTrainData(){
        double[][] ratingF = getOptionRatings();
        double[][] wordF = getRedTrainData(trainData);
        if(ratingF[0].length != wordF[0].length){
            System.err.println("Cannot get all feature: the number of players in rating feature and word feature does not match!");
            return null;
        }
        double[][] all = new double[ratingF.length+wordF.length][ratingF[0].length];
        for(int i = 0; i < ratingF[0].length; i++){
            for(int j = 0; j < ratingF.length; j++){
                all[j][i] = ratingF[j][i];
            }
            for(int j = 0; j < wordF.length; j++){
                all[j+ratingF.length][i] = wordF[j][i];
            }
            
        }
        
        return all;
        
    }
    
//    train NMF using both rating feature and word feature
    public NMFModel allNMFTrain(int nmfDim){
        double splitP = 0.9;
        double[][] data = getAllTrainData();
        NMFModel nmfm = new NMFModel();
        nmfm.dim = nmfDim;
        double[][] NMFtrainData = new double[data.length][data[0].length];
        double[][] NMFvalidateData = new double[data.length][data[0].length];
        MatrixTools.split(data, NMFtrainData, NMFvalidateData, splitP);
        NMF.nmf_train(new CompColMatrix(new DenseMatrix(NMFtrainData)), new CompColMatrix(new DenseMatrix(NMFvalidateData)), nmfm);
        return nmfm;      
    }
    
    public static void main(String[] args){

        
    }
     
    
}
