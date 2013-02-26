/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package optionTraining;
import java.io.*;
import java.util.*;
import nmf.*;
import no.uib.cipr.matrix.*;
import no.uib.cipr.matrix.sparse.*;
import prefix.*;
import tools.CommonUtil;
import x.na.SparseMatrixBuilder;
/**
 *
 * @author Hong Yu
 * 
 * 
 */
public class DataProcess {
    
//    public static ArrayList<Prefix> readPrefixList(String prefixFile, String ratingFile){
//        ArrayList<Prefix> prefixList = PrefixUtil.readPrefixWOption(prefixFile, ratingFile);
//        
//        
//        return prefixList;
//    }
    
    
    
    public static void computeMeanRatings(ArrayList<ArrayList> allprefix){
        int num = 0;
        double total = 0;
        for(int i = 0; i < allprefix.size(); i++){
            ArrayList<Prefix> pl = allprefix.get(i);
            for(int j = 0; j < pl.size(); j++){
                Prefix p = pl.get(j);
                if(p.numOfPlotPoints() == 6){
                    num += 1;
                    total += p.rating;
                }
            }
        }
        double mean = total / num;
        System.out.println(mean);
    }
    
    
//    Get the number of the prefixes equals to p in prefixList 
    public static int getExistNum(ArrayList<Prefix> prefixList, Prefix p, int position){
        int num = 0;
        for(int i = 0; i < position; i++){
            if(prefixList.get(i).compareTo(p) == 0)
                num++;
        }
        
        return num;
    }
    
    
    public static void computeBehavior(ArrayList<ArrayList> allprefix){
//        ArrayList<Prefix> visited = new ArrayList<Prefix>();
//        All the number of prefixes, firstly meet
        int total1 = 0;
        int select1 = 0;
        int total2 = 0;
        int select2 = 0;
        int total3 = 0;
        int select3 = 0;
        
        for(int i = 0; i < allprefix.size(); i++){
            ArrayList<Prefix> pList = allprefix.get(i);
//            visited.clear();
            for(int j = 0; j < pList.size()-1; j++){
                Prefix p1 = pList.get(j);
                Prefix p2 = pList.get(j+1);
                if(p1.numOfPlotPoints() > p2.numOfPlotPoints())
                    continue;
                if(p1.options.getAllOptions().size() == 1)
                    continue;
                
                int numMetBefore = getExistNum(pList, p1, j);
                if(numMetBefore == 4){
                    total1++;
                    if(p1.options.getOptionItemPreferencePosition(p2.getLast().id)==3){
                        select1++;
                    }
                }
                else if(numMetBefore == 5){
                    total2++;
                    if(p1.options.getOptionItemPreferencePosition(p2.getLast().id)==3){
                        select2++;
                    }                    
                }
                else if(numMetBefore == 6){
                    total3++;
                    if(p1.options.getOptionItemPreferencePosition(p2.getLast().id)==3){
                        select3++;
                    }                        
                }
                
            }
        }
        double percent1 = (double)select1/total1;
        double percent2 = (double)select2/total2;
        double percent3 = (double)select3/total3;
        System.out.println(percent1);
        System.out.println(percent2);
        System.out.println(percent3);
    }
    
    
    public static void getCorrelationData(ArrayList<ArrayList> allprefix){
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter("CorrelationData.txt"));
            for(int i = 0; i < allprefix.size(); i++){
                ArrayList<Prefix> pList = allprefix.get(i);

                for(int j = 0; j < pList.size()-1; j++){
                    Prefix p1 = pList.get(j);
                    Prefix p2 = pList.get(j+1);
                    if(p1.numOfPlotPoints() > p2.numOfPlotPoints())
                        continue;
                    if(p1.options.getAllOptions().size() == 1){
                        continue;
                    }
                    
                    int prefixRating = p2.rating;
                    int optionRating = p1.options.getItemByIndicatedPP(p2.getLast().id).getPreference();
                    bw.write("" + prefixRating + "\t" + optionRating + "\n");
                }
            }
            bw.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        
    }
    
    static int numIter = 10;
    static int numDim = 10;
    static void trainNMF(){
        try{
            CompColMatrix trainWOO = new CompColMatrix(SparseMatrixBuilder.readCoordMatrix(new FileReader("trainWOOption.txt")));
            CompColMatrix validateWOO = new CompColMatrix(SparseMatrixBuilder.readCoordMatrix(new FileReader("validateWOOption.txt")));
            CompColMatrix trainWO = new CompColMatrix(SparseMatrixBuilder.readCoordMatrix(new FileReader("trainWOption.txt")));
            CompColMatrix validateWO = new CompColMatrix(SparseMatrixBuilder.readCoordMatrix(new FileReader("validateWOption.txt")));
            
            NMFModel nmfModel = new NMFModel();
            nmfModel.dim = numDim;
            for(int i = 0; i < numIter; i++){
                System.out.println("Train NMF model without options. Iteration: " + i + "****************************************");
                NMF.nmf_train(trainWOO, validateWOO, nmfModel);
                CommonUtil.printObject(nmfModel.H, "optionModel/HwoOptions" + i + ".txt");
                CommonUtil.printObject(nmfModel.W, "optionModel/WwoOPtions" + i + ".txt");
                nmfModel = new NMFModel();
                nmfModel.dim = numDim;
            }
            
            nmfModel = new NMFModel();
            nmfModel.dim = numDim;
            for(int i = 0; i < numIter; i++){
                System.out.println("Train NMF model with options. Iteration: " + i + "****************************************");
                NMF.nmf_train(trainWO, validateWO, nmfModel);
                CommonUtil.printObject(nmfModel.H, "optionModel/HwOptions" + i + ".txt");
                CommonUtil.printObject(nmfModel.W, "optionModel/WwOPtions" + i + ".txt");
                nmfModel = new NMFModel();
                nmfModel.dim = numDim;
            }
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    static void testNMF(){
        try{
            CompColMatrix testWOO = new CompColMatrix(SparseMatrixBuilder.readCoordMatrix(new FileReader("testWOOption.txt")));
            CompColMatrix testWO = new CompColMatrix(SparseMatrixBuilder.readCoordMatrix(new FileReader("testWOption.txt")));
            CompColMatrix trainWOO = new CompColMatrix(SparseMatrixBuilder.readCoordMatrix(new FileReader("trainWOOption.txt")));
            CompColMatrix trainWO = new CompColMatrix(SparseMatrixBuilder.readCoordMatrix(new FileReader("trainWOption.txt")));
            
//            W = new DenseMatrix(CommonUtil.readData("W1.txt", false));
            double[][] rmse = new double[2][numIter];
            for(int i = 0; i < numIter; i++){
                System.out.println("Train NMF model without options. Iteration: " + i + "****************************************");
                DenseMatrix w = new DenseMatrix(CommonUtil.readData("optionModel/WwoOPtions" + i + ".txt", false));
                NMFModel nmfm = new NMFModel();
                nmfm.W = w;
                DenseMatrix predict = NMF.nmf_test(nmfm, trainWOO);
                rmse[0][i] = NMF.computeRMSE(testWOO, predict);
            }
            
            for(int i = 0; i < numIter; i++){
                System.out.println("Train NMF model with options. Iteration: " + i + "****************************************");
                DenseMatrix w = new DenseMatrix(CommonUtil.readData("optionModel/WwOPtions" + i + ".txt", false));
                NMFModel nmfm = new NMFModel();
                nmfm.W = w;
                DenseMatrix predict = NMF.nmf_test(nmfm, trainWO);
                rmse[1][i] = NMF.computeRMSE(testWOO, predict);
            }
            CommonUtil.printObject(rmse, "rmse.txt");
        }
        catch(Exception e){
            e.printStackTrace();
        }        
    }
    
    

    
    public static void main(String[] args){
//        ArrayList<ArrayList> allprefix = PrefixUtil.readAllStoryRatingsWOptions("ratings_wOptionTraining", "Options_training");
//        computeMeanRatings(allprefix);
//        computeBehavior(allprefix);
//        getCorrelationData(allprefix);
//        trainNMF();
//        testNMF();
//        modelOptionProbabilityModel();

        
    }
    
}
