/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package optionTrainingProcess;

/**
 *
 * @author Hong Yu
 */
public class Results {
    public float numCorrect = 0;
    public float numWrong = 0;
    public float numUnknown = 0;
    
    public double rmse = 0;
    public int numRMSE = 0;
    
    public void add(Results r){
        numCorrect += r.numCorrect;
        numWrong += r.numWrong;
        numUnknown += r.numUnknown;
    }
}
