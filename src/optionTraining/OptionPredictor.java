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
import PPCA.PPCAModel;
import java.util.*;
import nmf.NMFModel;
import no.uib.cipr.matrix.DenseMatrix;
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
    public static final int NMFPref = 0, NNPref = 1, KMeanPref = 2, PPCAPref = 3;
//    Option selection prediction models
    public static final int NMFSel = 0, NNSel = 1, ProbSel = 2;
//    number of clusters for NN, NMF, KMean
    private int numNNPref = 6, numNNSel = 10;
    private int numNMFPref = 2, numNMFSel = 1;
    private int numKMPref = 3;
    
    
    private static NMFModel nmfmPref = null;
    private static PPCAModel ppcam = null;
    private static double[][] NNmPref = null;
    private static double[][] KMmPref = null;
    private static NMFOptionSelPredict nmfOSP = null;
    private static NNOptionSelPredict nnOSP = null;
    private static OptionRatingPredictTrain orpt = null;
    private static  OptionRatingPredictTest opt = null;
    
    private double diffAdd = 0.12;
    private double diff = 0.15;
    private double[] previousPredict = null;
    private boolean updateDiff = false;
    
    public OptionPredictor(ArrayList<ArrayList> trainData){
        this.trainData = trainData;
        orpt = new OptionRatingPredictTrain(trainData);
        opt = new OptionRatingPredictTest(null);
        nmfmPref = null;
        ppcam = null;
        NNmPref = null;
        KMmPref = null;
        nmfOSP = null;
        nnOSP = null;
    }
    
    /**
     * Predict the option preferences for all the option item for newPlayer
     * @param newPlayer
     * @param PrefAlgorithm
     * @return a vector containing option preferences, including predicted and original
     */
    public double[] predictPref(ArrayList<Prefix> newPlayer, int PrefAlgorithm){
        //      Predict ratings for ppo  

        double[] predictArray = null;
        double[] cPlayerOption = GetStoryOptionRatingData.generatePlayerOptionRatings(newPlayer);
        if(PrefAlgorithm == NNPref){
            if(NNmPref == null){
                NNmPref = orpt.NearestNeighborTrain();
            }
            predictArray = opt.getNNPredict(NNmPref, cPlayerOption, numNNPref);
        }
        else if(PrefAlgorithm == NMFPref){
            if(nmfmPref == null){
                nmfmPref = orpt.NMFTrain(numNMFPref);
//                tools.CommonUtil.printObject(nmfmPref.W, "playerData/model/NMFmodel"+modelNum);
//                modelNum++;
            }
            predictArray = opt.getNMFPredict(nmfmPref, cPlayerOption);
        }
        else if(PrefAlgorithm == KMeanPref){
            if(KMmPref == null){
                KMmPref = orpt.KMeanClusterTrain(numKMPref);
            }
            predictArray = opt.getKMeanPredict(KMmPref, cPlayerOption);
        }
        else if(PrefAlgorithm == PPCAPref){
            if(ppcam == null){
//                ppcam = orpt.PPCATrain();
//                tools.CommonUtil.printObject(ppcam.cov, PrefixUtil.PPCAModelFolder+"cov"+modelNum);
//                tools.CommonUtil.printObject(ppcam.mean, PrefixUtil.PPCAModelFolder+"mean"+modelNum);
//                modelNum++;
                loadModel();
            }
            predictArray = opt.getPPCAPredict(ppcam, cPlayerOption);
        }
        else{
            System.err.println("No option preference prediction algorithm found!");
            return null;
        }
        return predictArray;
    }
    
    private void loadModel(){
        ppcam = new PPCAModel();
        ppcam.mean = new DenseMatrix(tools.CommonUtil.readData(PrefixUtil.PPCAModelFolder+"mean"+modelNum, false));
        ppcam.cov = new DenseMatrix(tools.CommonUtil.readData(PrefixUtil.PPCAModelFolder+"cov"+modelNum, false));
                
            
    }
    
    /**
     * Predict the preference rank the player will select
     * @param newPlayer
     * @param SelAlgorithm
     * @return the predicted preference rank selected by the player
     */
    public int predictSelection(ArrayList<Prefix> newPlayer, int SelAlgorithm){
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
            return -1;
        }
        if(predictSel < 0){
            predictSel = 0;
        }
        return predictSel;
    }
    
    private static int modelNum = 1; 
//  
    /**
     * Get predicted player selected options
     * Default algorithm: NNPref, and NNSel 
     * @param player the player to test, will predict on the last Prefix in player.
     * @param PrefAlgorithm
     * @param SelAlgorithm
     * @return the option items which the algorithm predicts the player will choose
     */
    public ArrayList<OptionItem> getPredictedSelectedOL(ArrayList<Prefix> player, int PrefAlgorithm, int SelAlgorithm){
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
//        predict option preference for all the option items
        double[] predictArray = predictPref(newPlayer, PrefAlgorithm);
        previousPredict = predictArray;
        OptionList ol = OptionListOperation.getOptionList();
        for(int i = 0; i < ppo.getAllOptions().size(); i++){
            int ind = ol.getOptionItemPosition(ppo.getAllOptions().get(i));
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
        
//      Option Selection prediction
//        int predictSel = predictSelection(newPlayer, SelAlgorithm);
        int predictSel = 0;
        ArrayList<OptionItem> ret = ppo.getItembyPreference(predictSel);
//        ArrayList<OptionItem> ret = ppo.getItemListByIndicatedPP(41);
        
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
                    int ind1 = ol.getOptionItemPosition(ppo.getAllOptions().get(i));
                    int ind2 = ol.getOptionItemPosition(ppo.getAllOptions().get(j));
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
            if(oi.getIndicatedPP() == ppID){
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
    
    /**
     * For testing option selection purpose. Simulate a test player by moving all the other option ratings forward, let plot point 2 the last. 
     * @param player 
     */
    
    private ArrayList<Prefix> simulateTestPlayer(ArrayList<Prefix> player){
        int indicator = 2;
        ArrayList<Prefix> newPlayer = new ArrayList<Prefix>();
        int testInd = -1;
        for(int i = 0; i < player.size(); i++){
            Prefix p = player.get(i);
            if(p.getLast().id != indicator){
                newPlayer.add(p);
            }
            else{
                if(testInd < 0){
                    testInd = i;
                }
                i+= 5;
            }
        }
        if(testInd < 0){
            return null;
        }
        newPlayer.add(player.get(testInd));
        newPlayer.add(player.get(testInd+1));
        return newPlayer;
    }
    
    public static  void main(String[] args){
        ArrayList<ArrayList> allprefix = PrefixUtil.readAllStoryRatingsWOptions(PrefixUtil.storyRatingTrainingFolder, PrefixUtil.optionRatingTrainingFolder);
        int[][] splitD = tools.CommonUtil.readIntData(PrefixUtil.trainDataSplitFile);
        Results all = new Results();
        for(int it = 0; it < splitD.length; it++){
            ArrayList<ArrayList> train = new ArrayList<ArrayList>();
            ArrayList<ArrayList> test = new ArrayList<ArrayList>();
            DataCreator.splitData(allprefix, train, test, splitD[it]);
//            OptionRatingPredictTrain orpt = new OptionRatingPredictTrain(train);
//            OptionRatingPredictTest opt = new OptionRatingPredictTest(test);
            System.out.println("***********Iteration: " + it + " **************************");
            Results r = null;

            OptionPredictor op = new OptionPredictor(train);
//            OptionPredictor op = new OptionPredictor(allprefix);
//            Start test process
            for(int i = 0; i < test.size(); i++){
                ArrayList<Prefix> player = test.get(i);
                System.out.println("Process player: " + i);
/////////////////Traditional test
//                for(int k = 0; k < player.size()-1; k++){
//                    if(player.get(k).options == null){
//                        continue;
//                    }
//                    ArrayList<Prefix> tplayer = new ArrayList<Prefix>(player.subList(0, k+1));
//                    Prefix tprefix = tplayer.get(k);
//                    PPOptions tppo = tprefix.options;
//                    PPOptions nppo = new PPOptions(tppo);
//                    nppo.resetPreference();
//                    tprefix.options = nppo;
//                    ArrayList<OptionItem> predictSel = op.predict(tplayer, NMFPref, NMFSel);
//                    tprefix.options = tppo;
//                    r = getResults(player.get(k+1), predictSel);
//                    all.add(r);
//                }
////////////////Perform test on simulated players who read plot point 2 last
                ArrayList<Prefix> newPlayer = op.simulateTestPlayer(player);
                if(newPlayer == null){
                    continue;
                }
                Prefix nextP = newPlayer.remove(newPlayer.size()-1);
                PPOptions tppo = newPlayer.get(newPlayer.size()-1).options;
                PPOptions nppo = new PPOptions(tppo);
                nppo.resetPreference();
                newPlayer.get(newPlayer.size()-1).options = nppo;
                ArrayList<OptionItem> predictSel = op.getPredictedSelectedOL(newPlayer, KMeanPref, NNSel);
                newPlayer.get(newPlayer.size()-1).options = tppo;
                r = getResults(nextP, predictSel);
                all.add(r);
            }    
            if(r != null){
                all.add(r);            

            }
            float accuracy = all.numCorrect / (all.numCorrect + all.numWrong);
            System.out.println("Right: " + all.numCorrect + ", Wrong: " + all.numWrong + ", Accuracy: " + accuracy);
            System.out.println("RMSE: " + all.getRMSE());
        
        }        
    }
    
}
