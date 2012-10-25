/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package optionTrainingProcess.optionPreferencePrediction.kmean;

/**
 *
 * @author Bunnih
 */
import java.util.*;

public class KMean {
    private static int maxIter = 100;
    

    
    static int K = 0;
    
    private static double[][] getNewCenters(double[][] data, double[] membership){
        double[][] centers = new double[K][data[0].length];
        int[] count = new int[centers.length];
        Arrays.fill(count, 0);
        for(int i = 0; i < data.length; i++){
            MatrixTools.add(centers[(int)(membership[i])], data[i]);
            count[(int)(membership[i])]++;
        }
        for(int i = 0; i < count.length; i++){
            if(count[i] != 0){
                MatrixTools.normalize(centers[i], 1.0/count[i]);
            }
            else{
                Random r = new Random();
                int nr = r.nextInt(data.length);
                MatrixTools.copyArray(data[nr], centers[i]);
            }
        }
        return centers;
    }
    
//   data: number of dimension by number of data points
    public static double[][] kmean(double[][] traindata, int numCluster){
        K = numCluster;
        double[][] data = MatrixTools.transpose(traindata);
        double[] membership = new double[data.length];
        Random r = new Random();
        
        double[][] centers = new double[K][data[0].length];
//        Initialization
        for(int i = 0; i < K; i++){
            int nr = r.nextInt(data.length);
            MatrixTools.copyArray(data[nr], centers[i]);
        }
        System.out.println("Start KMean iteration.......");
        
        for(int it = 0; it < maxIter; it++){
//            update membership
            System.out.println("Iteration number: " +it);
            for(int i = 0; i < data.length; i++){
                membership[i] = MatrixTools.getNearest(centers, data[i]);
                               
            }
            
//          update centers;
            
            double[][] newC = getNewCenters(data, membership);
            if(MatrixTools.getDistance(newC, centers) < 0.0001){
                break;
            }
            MatrixTools.copyArray(newC, centers);
            
            
        }
        return centers;
        
        
    }
    
    
}
