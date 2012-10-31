/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package optionTraining.selectionPrediction;

/**
 *
 * @author Hong Yu
 */
import optionTraining.selectionPrediction.ProbabilityModel;
import java.util.*;
import optionTraining.DataCreator;
import optionTraining.DataProcess;
import optionTraining.Results;
import prefix.*;

public class NearestNeighbor {
    int numNeighbors = 16;
    int numReadStories = 0;
    final int numPlotPoints = 6;
    
    public float computeDistance(float[] d1, float[] d2){
        float dist = 0;
        for(int i = 0; i < d1.length; i++){
            if(d1[i] != 0 && d2[i] != 0){
                dist += (d1[i]-d2[i])*(d1[i]-d2[i]);
            }
        }
//        dist = (float)(Math.sqrt(dist));
        return dist;
        
    }
    
    private void addNeibor(ArrayList<float[]> list, float[] data, float[] newNeibor){
        float dist = computeDistance(data, newNeibor);
        
        if(list.size() == 0){
            list.add(newNeibor);
            return;
        }
        int i = 0;
        for(i = 0; i < list.size(); i++){
            float td = computeDistance(list.get(i), data);
            if(dist < td){
                break;
            }
        }
        if(list.size() < numNeighbors){
//            if(i == list.size())
//                list.add(newNeibor);
//            else
                list.add(i, newNeibor);
        }
        else{
            if(i < list.size()){
                list.add(i, newNeibor);
                list.remove(list.size()-1);
            }
//            else
//                list.add(i, newNeibor);
                
        }
    }
    
    public ArrayList<float[]> computeNeighbor(ArrayList<float[]> neibors, float[] data){
        ArrayList<float[]> nearestNebors = new ArrayList<float[]>();
        
        for(int i = 0; i < neibors.size(); i++){
            addNeibor(nearestNebors, data, neibors.get(i));
        }
        
        return nearestNebors;
    }
    
    public double[][] computeNeighbor(double[][] neiborA, double[] dataA, int num){
        ArrayList<float[]> neibors = new ArrayList<float[]>();
        ArrayList<float[]> nearestNebors = new ArrayList<float[]>();
        double[][] ret = new double[num][dataA.length];
        numNeighbors = num;
        float[] data = new float[dataA.length];
        for(int i = 0; i < data.length; i++){
            data[i] = (float)(dataA[i]);
        }
        for(int i = 0; i < neiborA.length; i++){
            float[] td = new float[neiborA[i].length];
            for(int j = 0; j < td.length; j++){
                td[j] = (float)(neiborA[i][j]);
            }
            neibors.add(td);
        }
        for(int i = 0; i < neibors.size(); i++){
            addNeibor(nearestNebors, data, neibors.get(i));
        }
        for(int i = 0; i < nearestNebors.size(); i++){
            float[] tp = nearestNebors.get(i);
            for(int j = 0; j < tp.length; j++){
                ret[i][j] = tp[j];
            }
        }
        
        return ret;
    }
    
    private int predict(ArrayList<float[]> neibors, int numOptions, int numBefore, int numPositions){
        if(neibors.isEmpty()){
            return -1;
        }
        float[] pv = new float[neibors.get(0).length];
        
        for(int i = 0; i < neibors.size(); i++){
            float[] tp = neibors.get(i);
            for(int j = 0; j < pv.length; j++){
                pv[j] += tp[j];
            }
            
        }
        for(int i = 0; i < pv.length; i++){
            pv[i] /= neibors.size();
        }
        ProbabilityModel pm = new ProbabilityModel(pv);
        
        int predict = pm.getPrediction(numBefore, numPositions, numOptions);
        return predict;
    }
    
    private int predict(ArrayList<float[]> neibors, int numOptions, int numBefore, int numPositions, float[] player){
        if(neibors.isEmpty()){
            return -1;
        }
        float[] pv = new float[neibors.get(0).length];
        
        for(int i = 0; i < neibors.size(); i++){
            float[] tp = neibors.get(i);
            for(int j = 0; j < pv.length; j++){
                pv[j] += tp[j];
            }
            
        }
        for(int i = 0; i < pv.length; i++){
            pv[i] /= neibors.size();
            if(player[i] != 0){
                pv[i] = player[i];
            }
        }
        ProbabilityModel pm = new ProbabilityModel(pv);
        
        int predict = pm.getPrediction(numBefore, numPositions, numOptions);
        return predict;
    }
    
//    return the predicted position of the selected. 0: select the highest rated, 1: select the second highest rated option...
    public int NNPredict(ArrayList<ArrayList> train, ArrayList<Prefix> player){
            ProbabilityModel pm = DataCreator.computePlayerProbModel(player);
            float[] pmv = pm.modelVector();
            ArrayList<float[]> trainData = DataCreator.computeProbVectorModel(train);
            ArrayList<float[]> neibors = computeNeighbor(trainData, pmv);
            
            PPOptions ppo = player.get(player.size()-1).options;
            
            if(ppo == null || ppo.getAllOptions().size() < 2){
                return 0;
            }                
            int numOptions = ppo.getAllOptions().size()-2;
            int numBefore = DataProcess.getExistNum(player, player.get(player.size()-1), player.size()-1);
            int numPosition = player.get(player.size()-1).itemList.size()-1;
            int predict = predict(neibors, numOptions, numBefore, numPosition);
            return predict;
    }
        
    private Results playerPrediction(ArrayList<float[]> trainData, ArrayList<Prefix> player){
        Results r = new Results();
//        int unknown = 0, numCorrect = 0, numWrong = 0;
        int startnum = numPlotPoints*numReadStories-1;
        if(startnum < 0){
            startnum = 0;
        }
        for(int i = startnum; i < player.size()-1; i++){
            ProbabilityModel pm = DataCreator.computePlayerProbModel(player, i);
            float[] pmv = pm.modelVector();
            ArrayList<float[]> neibors = computeNeighbor(trainData, pmv);
            
            PPOptions ppo = player.get(i).options;
            if(ppo == null || ppo.getAllOptions().size() < 2){
                continue;
            }
                
            int numOptions = ppo.getAllOptions().size()-2;
            int numBefore = DataProcess.getExistNum(player, player.get(i), i);
            int numPosition = player.get(i).itemList.size()-1;

            int selectedPreference = player.get(i).options.getOptionItemPreferencePosition(player.get(i+1).getLast().id);

            int predict = predict(neibors, numOptions, numBefore, numPosition);
//            int predict = predict(neibors, numOptions, numBefore, numPosition, pmv);
            if(predict < 0){
                r.numUnknown++;
            }
            else if(predict == selectedPreference -1){
                r.numCorrect++;
            }else{
                r.numWrong++;
            }
        }
        
        return r;
    }
    
    
    private void startProcess(){
        ArrayList<ArrayList> alltrain = new ArrayList<ArrayList>();
        ArrayList<ArrayList> alltest = new ArrayList<ArrayList>();
        ArrayList<ArrayList> alltestData = DataCreator.createProbVectorData(alltrain, alltest);
        Results allResults = new Results();
        
        for(int iter = 0; iter < alltrain.size(); iter++){
            ArrayList<float[]> train = alltrain.get(iter);
            ArrayList<ArrayList> test = alltestData.get(iter);
            for(int i = 0; i < test.size(); i++){
                ArrayList<Prefix> player = test.get(i);
                Results r = playerPrediction(train, player);
                allResults.add(r);
            }
                
            
        }
        
        System.out.println("Correct: "+allResults.numCorrect+", Wrong: "+allResults.numWrong + ", Unknown: "+allResults.numUnknown);
        float p1 = (float)(allResults.numCorrect)/(allResults.numCorrect+allResults.numWrong+allResults.numUnknown);
        float p2 = (float)(allResults.numUnknown)/(allResults.numCorrect+allResults.numWrong+allResults.numUnknown);
        
        System.out.println("Correct: "+p1+", Unknown: "+p2);
    }
    
    public static void main(String[] args){
        NearestNeighbor nn = new NearestNeighbor();
        nn.startProcess();
    }
    
}
