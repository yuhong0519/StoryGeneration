/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package optionTraining;

/**
 *
 * @author Hong Yu
 */
public class Results {
    public float numCorrect = 0;
    public float numWrong = 0;
    public float numUnknown = 0;
    
    private double mse = 0;
    private int numMSE = 0;
    
    public void add(Results r){
        numCorrect += r.numCorrect;
        numWrong += r.numWrong;
        numUnknown += r.numUnknown;
        mse = (r.mse * r.numMSE + mse * numMSE) / (r.numMSE + numMSE);
        numMSE += r.numMSE;
    }
    
    public void addMSE(double error){
        mse = (mse*numMSE + error) / (numMSE + 1);
        numMSE++;
    }
    public double getMSE(){
        return mse;
    }
    public double getRMSE(){
        return Math.sqrt(mse);
    }
}
