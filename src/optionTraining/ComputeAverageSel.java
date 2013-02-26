/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package optionTraining;

import java.util.ArrayList;
import prefix.Prefix;
import tools.PrefixUtil;

/**
 *
 * @author Bunnih
 * Compute the probability the players select branch 41 over 42
 */
public class ComputeAverageSel {
    public static void main(String[] args){
        int num2 = 0;
        int num1 = 0;
        int node1 = 41;
        int node2 = 42;
        ArrayList<ArrayList> allPlayers = PrefixUtil.readAllStoryRatingsWOptions(PrefixUtil.storyRatingTrainingFolder, PrefixUtil.optionRatingTrainingFolder);
        for(ArrayList<Prefix> player : allPlayers){
            for(Prefix p : player){
                if(p.getLast().id == node1){
                    num1++;
                    break;
                }
                else if(p.getLast().id == node2){
                    num2++;
                    break;
                }
            }
        }
        double accuracy = (double)num1/(num1+num2);
        System.out.println(""+node1+": "+num1+", "+node2+": "+num2+", Accuracy: " + accuracy);
    }
}
