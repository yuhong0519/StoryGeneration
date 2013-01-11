/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.util.ArrayList;
import optionTraining.DataProcess;
import prefix.OptionItem;
import prefix.PPOptions;
import prefix.Prefix;

/**
 *
 * @author Bunnih
 */
public class EntropyComputer {
    public static double compute(int[] data){
//        number of different ratings
        int size = 5;
        double[] stat = new double[size];
        for(int i = 0; i < data.length; i++){
            stat[data[i]-1] += 1;
        }
        double sum = data.length;
        for(int i = 0; i < stat.length; i++){
            stat[i] /= sum;
        }
        double entropy = 0;
        for(int i = 0; i < stat.length; i++){
            if(stat[i] != 0){
                entropy += -1 * stat[i] * Math.log(stat[i]);
            }
        }
        return entropy;
                
    }
    
    private static double computePlayer(ArrayList<Prefix> player){
        int len = 0;
        for(Prefix p : player){
            PPOptions ppo = p.options;
            if(ppo == null){
                continue;
            }
            len += ppo.getAllOptions().size();
        }
        int[] data = new int[len];
        int ind = 0;
         for(Prefix p : player){
            if(p.options == null){
                continue;
            }
            ArrayList<OptionItem> oil = p.options.getAllOptions();
            for(OptionItem oi : oil){
                data[ind++] = oi.getPreference();
                
            }
        }
        return compute(data);
    }
    
    public static void main(String[] args){
        ArrayList<ArrayList> allprefix = DataProcess.readAllStoryRatingsWOptions(PrefixUtil.ratingWOptionTrainingFolder, PrefixUtil.optionTrainingFolder);
        
        for(ArrayList<Prefix> player : allprefix){
            System.out.println(computePlayer(player));
        }
    }
    
}
