/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package optionTraining;

/**
 *
 * @author Bunnih
 * To predict which option the player is going to choose
 * Including two steps: option preference prediction & option selection prediction
 */
import java.util.*;
import nmf.NMFModel;
import optionTraining.optionPreferencePrediction.OptionList;
import optionTraining.optionPreferencePrediction.OptionListOperation;
import optionTraining.optionPreferencePrediction.OptionRatingPredictTest;
import optionTraining.optionPreferencePrediction.OptionRatingPredictTrain;
import optionTraining.selectionPrediction.NMFOptionSelPredict;
import optionTraining.selectionPrediction.NNOptionSelPredict;
import optionTraining.selectionPrediction.ProbabilisticOptionSelPredict;
import prefix.*;
import tools.PrefixUtil;

public class OptionPredictor {
    private ArrayList<ArrayList> trainData = null;
    
//    Option preference prediction models
    public static final int NMFPref = 0, NNPref = 1, KMeanPref = 2;
//    Option selection prediction models
    public static final int NMFSel = 0, NNSel = 1, ProbSel = 2;
    
    private int numNNPref = 10, numNNSel = 10;
    private int numNMFPref = 2, numNMFSel = 2;
    private int numKMPref = 2;
    
    
    private NMFModel nmfmPref = null;
    private double[][] NNmPref = null;
    private double[][] KMmPref = null;
    private NMFOptionSelPredict nmfOSP = null;
    private NNOptionSelPredict nnOSP = null;
    
    
    private double diffAdd = 0.12;
    private double diff = 0.15;
    private double[] previousPredict = null;
    private boolean updateDiff = true;
    
    public OptionPredictor(ArrayList<ArrayList> trainData){
        this.trainData = trainData;
    }
    
//  Default algorithm: NNPref, and NNSel  
    public ArrayList<OptionItem> predict(ArrayList<Prefix> player, int PrefAlgorithm, int SelAlgorithm){
        
        if(updateDiff){
            updateThreshold(player);
            previousPredict = null;
        }
        
        PPOptions ppo = player.get(player.size()-1).options;
        if(ppo == null ){
            return null;
        }
        if(ppo.getAllOptions().size() < 2){
            return ppo.getAllOptions();
        }
        
//        change the last item in player
        ArrayList<Prefix> newPlayer = new ArrayList<Prefix>(player);
        Prefix lastP = newPlayer.remove(newPlayer.size()-1);
        Prefix newlastP = new Prefix(lastP);
        ppo = newlastP.options;
        
        OptionRatingPredictTrain orpt = new OptionRatingPredictTrain(trainData);
        OptionRatingPredictTest opt = new OptionRatingPredictTest(null);
        
//      Predict ratings for ppo  
        double[] predictArray = null;
        double[] cPlayerOption;
        cPlayerOption = GenerateStoryOptionRatingData.generatePlayerOptionRatings(newPlayer);
        if(PrefAlgorithm == NNPref){
            if(NNmPref == null){
                NNmPref = orpt.NearestNeighborTrain();
            }
            predictArray = opt.getNNPredict(NNmPref, cPlayerOption, numNNPref);
        }
        else if(PrefAlgorithm == NMFPref){
            if(nmfmPref == null){
                nmfmPref = orpt.NMFTrain(numNMFPref);
            }
            predictArray = opt.getNMFPredict(nmfmPref, cPlayerOption);
        }
        else if(PrefAlgorithm == KMeanPref){
            if(KMmPref == null){
                KMmPref = orpt.KMeanClusterTrain(numKMPref);
            }
            predictArray = opt.getKMeanPredict(KMmPref, cPlayerOption);
        }
        else{
            System.err.println("No option preference prediction algorithm found!");
            return null;
        }
        
        previousPredict = predictArray;
        OptionList ol = OptionListOperation.getOptionList();
        for(int i = 0; i < ppo.getAllOptions().size(); i++){
            int ind = ol.getOptionItemID(ppo.getAllOptions().get(i));
            ppo.getAllOptions().get(i).setPreference(predictArray[ind]);
        }
//        update using diff
        for(int i = 0; i < ppo.getAllOptions().size(); i++){
            for(int j = i+1; j < ppo.getAllOptions().size(); j++){
                if(Math.abs(ppo.getAllOptions().get(i).getAccuratePreference() - ppo.getAllOptions().get(j).getAccuratePreference()) < diff){
                    ppo.getAllOptions().get(j).setPreference(ppo.getAllOptions().get(i).getAccuratePreference());
                }                
            }
        }
        newPlayer.add(newlastP);
        
//      Option Selection
        int predictSel = 0;
        if(SelAlgorithm == NNSel){
            if(nnOSP == null){
                nnOSP = new NNOptionSelPredict();
            }
            predictSel = nnOSP.NNPredict(trainData, newPlayer, numNNSel);
        }
        else if(SelAlgorithm == NMFSel){
            if(nmfOSP == null){
                nmfOSP = new NMFOptionSelPredict(trainData, numNMFSel);
            }
            predictSel = nmfOSP.NMFPredict(newPlayer);
        }
        else if(SelAlgorithm == ProbSel){
            predictSel = ProbabilisticOptionSelPredict.probPredict(trainData, newPlayer);
        }
        else{
            System.err.println("No option selection prediction algorithm found!");
            return null;
        }
        if(predictSel < 0){
            predictSel = 0;
        }
        ArrayList<OptionItem> ret = ppo.getOptionItembyPosition(predictSel);
        return ret;
        
    }
    
//    update diff if needed
    private void updateThreshold(ArrayList<Prefix> player){
        if(previousPredict != null && player.size()>1){
             PPOptions ppo = player.get(player.size()-2).options;
             if(ppo == null || ppo.getAllOptions().size() < 2){
                 return;
             }
             OptionList ol = OptionListOperation.getOptionList();
             
             for(int i = 0; i < ppo.getAllOptions().size(); i++){
                for(int j = i+1; j < ppo.getAllOptions().size(); j++){
                    int ind1 = ol.getOptionItemID(ppo.getAllOptions().get(i));
                    int ind2 = ol.getOptionItemID(ppo.getAllOptions().get(j));
                    if(Math.abs(previousPredict[ind1]-previousPredict[ind2]) > diff && 
                            ppo.getAllOptions().get(i).getPreference() == ppo.getAllOptions().get(j).getPreference()){
                        diff += diffAdd;
                    }
                    else if(Math.abs(previousPredict[ind1]-previousPredict[ind2]) > diff &&
                            ppo.getAllOptions().get(i).getPreference() != ppo.getAllOptions().get(j).getPreference()){
                        diff -= diffAdd;
                        if(diff < 0){
                            diff = 0;
                        }
                    }                    
                }
             }
        }
    }
    
    private static Results getResults(Prefix nextP, ArrayList<OptionItem> predict){
        Results r = new Results();
        int ppID = nextP.getLast().id;
        boolean flag = false;
        for(OptionItem oi : predict){
            if(oi.getOID() == ppID){
                flag = true;
                break;
            }
        }
        if(flag){
//            r.numCorrect += 1.0f / predict.size();
//            r.numWrong += 1 - 1.0f / predict.size();
            r.numCorrect += 1;
        }
        else{
            r.numWrong += 1;
        }
        return r;
    }
    
    public static  void main(String[] args){
        
        ArrayList<ArrayList> allprefix = DataProcess.readAllStoryRatingsWOptions(PrefixUtil.ratingWOptionTrainingFolder, PrefixUtil.optionTrainingFolder);
        int[][] splitD = tools.CommonUtil.readIntData(PrefixUtil.trainDataSplitFile);
        Results all = new Results();
        for(int it = 0; it < splitD.length; it++){
            ArrayList<ArrayList> train = new ArrayList<ArrayList>();
            ArrayList<ArrayList> test = new ArrayList<ArrayList>();
            DataCreator.splitData(allprefix, train, test, splitD[it]);
            OptionRatingPredictTrain orpt = new OptionRatingPredictTrain(train);
            OptionRatingPredictTest opt = new OptionRatingPredictTest(test);
            System.out.println("***********Iteration: " + it + " **************************");
            Results r = null;

            OptionPredictor op = new OptionPredictor(train);
//            Start test process
            for(int i = 0; i < test.size(); i++){
                ArrayList<Prefix> player = test.get(i);
                System.out.println("Process player: " + i);
                for(int k = 0; k < player.size()-1; k++){
                    if(player.get(k).options == null){
                        continue;
                    }
                    ArrayList<Prefix> tplayer = new ArrayList<Prefix>(player.subList(0, k+1));
                    Prefix tprefix = tplayer.get(k);
                    PPOptions tppo = tprefix.options;
                    PPOptions nppo = new PPOptions(tppo);
                    nppo.resetPreference();
                    tprefix.options = nppo;
                    ArrayList<OptionItem> predictSel = op.predict(tplayer, NMFPref, NMFSel);
                    tprefix.options = tppo;
                    r = getResults(player.get(k+1), predictSel);
                    all.add(r);
                }
                
                
            }
            
            all.add(r);            
            float accuracy = all.numCorrect / (all.numCorrect + all.numWrong);
            System.out.println("Right: " + all.numCorrect + ", Wrong: " + all.numWrong + ", Accuracy: " + accuracy);
            System.out.println("RMSE: " + all.getRMSE());
        
        }

        
    }
    
}
