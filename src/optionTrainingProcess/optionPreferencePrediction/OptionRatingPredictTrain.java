/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package optionTrainingProcess.optionPreferencePrediction;

/**
 *
 * @author Bunnih
 */

import java.io.FileReader;
import java.util.*;
import nmf.*;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.sparse.CompColMatrix;
import optionTrainingProcess.*;
import optionTrainingProcess.optionPreferencePrediction.kmean.KMean;
import optionTrainingProcess.optionPreferencePrediction.kmean.MatrixTools;
import x.na.SparseMatrixBuilder;

public class OptionRatingPredictTrain {
    private ArrayList<ArrayList> trainData = null;
    private int KmeanClass = 1;
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
    
//    return number of players * number of option items
    public double[][] NearestNeighborTrain(){
        return MatrixTools.transpose(getOptionRatings());
    }
    
    public NMFModel NMFTrain(){
        double[][] data = getOptionRatings();
//        percent of training vs. validation data
        double splitP = 0.8;
        int nmfDim = 5;
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
