/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Bunnih
 */
public class MatrixTools {
        public static void copyArray(double[] source, double[] dest){
        if(source.length != dest.length){
            System.out.println("Error copy matrix! The lenght should be equal");
            return;
        }
        System.arraycopy(source, 0, dest, 0, source.length);
    }
    
    public static void copyArray(double[][] source, double[][] dest){
        if(source.length != dest.length){
            System.err.println("Error copy matrix! The lenght should be equal");
            return;
        }
        System.arraycopy(source, 0, dest, 0, source.length);
    }
    
    
    
    public static double innerProduct(double[] data1, double[] data2){
        double ret = 0;
        if(data1.length != data2.length){
            System.err.println("Error using inner product!!!");
            return ret;
        }
        for(int i = 0; i < data1.length; i++){
            ret += data1[i]*data2[i];
        }
        return ret;
    }
        
    public static void add(double[] data1, double[] data2){
        if(data1.length != data2.length){
            System.err.println("Error add matrix! The lenght should be equal");
            return;
        }
        for(int i = 0; i < data1.length; i++){
            data1[i] += data2[i];
        }        
    }
    
    public static void normalize(double[] data, double value){
        for(int i = 0; i < data.length; i++){
            data[i] *= value;
        }        
    }
    
    public static double getDistance(double[] d1, double[] d2){
        double dist = 0;
        for(int i = 0; i < d1.length; i++){
            dist += (d1[i] - d2[i]) * (d1[i] - d2[i]);
            
        }
        dist /= d1.length;
//        dist = Math.sqrt(dist);
        return dist;
    }
    
    public static double getDistance(double[][] d1, double[][] d2){
        double dist = 0;
        for(int i = 0; i < d1.length; i++){
            for(int j = 0; j < d2.length; j++){
                dist += (d1[i][j] - d2[i][j]) * (d1[i][j] - d2[i][j]);
            }
        }
        dist /= d1.length * d1[0].length;
        dist = Math.sqrt(dist);
        return dist;
    }
    
    public static double[][] transpose(double[][] matrix){
        double[][] ret = new double[matrix[0].length][matrix.length];
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[i].length; j++){
                ret[j][i] = matrix[i][j];
            }
        }
        return ret;
    }
    
    public static int getNearest(double[][] data, double[] sample){
        int nearest = -1;
        double nearestV = 1000000;
        for(int i = 0; i < data.length; i++){
            double tp = getDistance(data[i], sample);
            if(tp < nearestV){
                nearestV = tp;
                nearest = i;
            }
        }
        return nearest;
    }
    
//    public static double[][] getNearest(double[][] data, double[] sample, int num){
//        double[][] nearest = new double[num][data[0].length];
//        int[] index = new int[num];
//        double[] dist = new double[num];
//        
//        Arrays.fill(index, -1);
//        for(int i = 0; i < data.length; i++){
//            double tp = getDistance(data[i], sample);
//            for(int j = 0; j < index.length; j++){
//                if(index[j] == -1){
//                    index[j] = i;
//                    dist[j] = getDistance(data[i], sample);
//                    break;
//                }
//            }
//            
//
//        }
//        return nearest;
//    }
      public static  void split(double[][] data, double[][] train, double[][] validate, double splitP){
//        double splitP = 0.8;
        Random r = new Random();
        for(int i = 0; i < data.length; i++){
            for(int j = 0; j < data[0].length; j++){
                if(r.nextDouble() < splitP){
                    train[i][j] = data[i][j];
                }
                else{
                    validate[i][j] = data[i][j];
                }
            }
        }
    }
//      subvector of data: include start, exlude end
      public static double[] subVector(double[] data, int start, int end){
          double[] ret = new double[end-start];
          for(int i = start; i < end; i++){
              ret[i-start] = data[i];
          }
          return ret;
      }
    
    
}
