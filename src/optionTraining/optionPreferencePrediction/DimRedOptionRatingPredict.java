/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package optionTraining.optionPreferencePrediction;

import java.util.ArrayList;
import nmf.NMF;
import nmf.NMFModel;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.sparse.CompColMatrix;
import optionTraining.DataCreator;
import optionTraining.DataProcess;
import optionTraining.GenerateStoryOptionRatingData;
import optionTraining.Results;
import prefix.OptionItem;
import prefix.PPOptions;
import prefix.Prefix;
import tools.MatrixTools;
import tools.PrefixUtil;

/**
 *
 * @author Bunnih
 */
public class DimRedOptionRatingPredict {
    private ArrayList<ArrayList> trainData = null;
    private NMFModel nmfm= null;
    int startPredictNum = 0;
    
    public DimRedOptionRatingPredict(ArrayList<ArrayList> trainData){
        this.trainData = trainData;
    }
    
    
    public double[][] getRedTrainData(ArrayList<ArrayList> trainA){
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
        double max = -1, min = 10;
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
        double scale = 4 / (max - min);
        for(int i = 0; i < train.length; i++){
            for(int j = 0; j < train[i].length; j++){
                train[i][j] = (train[i][j]-min) * scale + 1;
            }
        }
                
        return MatrixTools.transpose(train);
    }
    

    
    private NMFModel redNMFTrain(int dim){
//        if(nmfm != null){
//            return nmfm;
//        }
        double splitP = 0.9;
        double[][] data = getRedTrainData(trainData);
        nmfm = new NMFModel();
        nmfm.dim = dim;
        double[][] NMFtrainData = new double[data.length][data[0].length];
        double[][] NMFvalidateData = new double[data.length][data[0].length];
        MatrixTools.split(data, NMFtrainData, NMFvalidateData, splitP);
        NMF.nmf_train(new CompColMatrix(new DenseMatrix(NMFtrainData)), new CompColMatrix(new DenseMatrix(NMFvalidateData)), nmfm);
       return nmfm;
               
    }
    

    
    public static void main(String[] args){
        ArrayList<ArrayList> allprefix = DataProcess.readAllStoryRatingsWOptions(PrefixUtil.ratingWOptionTrainingFolder, PrefixUtil.optionTrainingFolder);
        int[][] splitD = tools.CommonUtil.readIntData(PrefixUtil.trainDataSplitFile);
        Results all = new Results();
        int dim = 5;
        for(int i = 0; i < splitD.length; i++){
            ArrayList<ArrayList> train = new ArrayList<ArrayList>();
            ArrayList<ArrayList> test = new ArrayList<ArrayList>();
            DataCreator.splitData(allprefix, train, test, splitD[i]);
            DimRedOptionRatingPredict dorp = new DimRedOptionRatingPredict(train);
            System.out.println("***********Iteration: " + i + " **************************");
            Results r = null;
            dorp.redNMFTrain(5);
            
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
