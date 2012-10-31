/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package optionTraining.selectionPrediction;

/**
 *
 * @author Hong Yu
 */
import java.util.*;

public class ProbabilityModel {
//    dim1: num of times meet before: 0, 1, 2, 3, 4+;
//    dim2: position 0,1,2,3,4
//    dim3: num of options: 2,3,4,5,6
//    dim4: 
    public float[][][][] model = new float[3][5][5][];
    private float epsilon = 0.0000001f;
    public void addOne(int dim1, int dim2, int dim3, int dim4){
        if(dim1 > model.length-1){
            dim1 = model.length - 1;
        }
        model[dim1][dim2][dim3][dim4] += 1;
    }
    
    public ProbabilityModel(){
        for(int i = 0; i < model.length; i++){
            for(int j = 0; j < model[i].length; j++){
                for(int k = 0; k < model[i][j].length; k++){
                    model[i][j][k] = new float[k+2];
                    for(int l = 0; l < model[i][j][k].length; l++){
                        model[i][j][k][l] = 0;
                    }
                    
                }
            }
        }
    }
    
    public ProbabilityModel(float[] ve){
        int ind = 0;
        for(int i = 0; i < model.length; i++){
            for(int j = 0; j < model[i].length; j++){
                for(int k = 0; k < model[i][j].length; k++){
                    model[i][j][k] = new float[k+2];
                    for(int l = 0; l < model[i][j][k].length; l++){
                        model[i][j][k][l] = ve[ind];
                        ind++;
                    }
                    
                }
            }
        }        
        
    }
    
    public float[] modelVector(){
        int veLength = 0;
        for(int i = 0; i < model.length; i++){
            for(int j = 0; j < model[i].length; j++){
                for(int k = 0; k < model[i][j].length; k++){
                    
                    
                        veLength += model[i][j][k].length;
                                     
                }
            }
        }        
        float[] ve = new float[veLength];
        int ind = 0;
        for(int i = 0; i < model.length; i++){
            for(int j = 0; j < model[i].length; j++){
                for(int k = 0; k < model[i][j].length; k++){
//                    boolean flag = false;
//                    for(int l = 0; l < model[i][j][k].length; l++){
////                        ve[ind++] = model[i][j][k][l];
//                        if(model[i][j][k][l] != 0){
//                            flag = true;
//                            break;
//                        }
//                    }
                    for(int l = 0; l < model[i][j][k].length; l++){
//                        if(flag){
                            ve[ind++] = model[i][j][k][l];
//                        }
//                        else
//                            ve[ind++] = -1;
                    }
                                     
                }
            }
        }
        return ve;
    }
    
    
    
    
    public void normalizeProb(){
        for(int i = 0; i < model.length; i++){
            for(int j = 0; j < model[i].length; j++){
                for(int k = 0; k < model[i][j].length; k++){
                    int sum = 0;
                    for(int l = 0; l < model[i][j][k].length; l++){
                        sum += model[i][j][k][l];
                    }
                    if(sum != 0){
                        for(int l = 0; l < model[i][j][k].length; l++){
                            model[i][j][k][l] /= sum;
                            if(model[i][j][k][l] == 0){
                                model[i][j][k][l] = epsilon;
                            }
                        } 
                    }
                    
                }
            }
        }        
        
    }
//    predict the most probable one
    public int getPrediction(int dim1, int dim2, int dim3){
        float max = -1;
        int maxID = -1;
        if(dim1 > model.length-1){
            dim1 = model.length-1;
        }
        for(int i = 0; i < model[dim1][dim2][dim3].length; i++){
            if(model[dim1][dim2][dim3][i] > max){
                max = model[dim1][dim2][dim3][i];
                maxID = i;
            }
        }
        
        if(max == 0){
            return -1;
        }
        return maxID;
    }
    
    public int getNumSamePrediction(int dim1, int dim2, int dim3, int pos){
        float max = -1;
        int maxID = -1;
        if(dim1 > model.length-1){
            dim1 = model.length-1;
        }
        float[] tp = model[dim1][dim2][dim3];
        Arrays.sort(tp);
        float selected = tp[tp.length-pos-1];
        int num = 0;
        for(int i = 0; i < model[dim1][dim2][dim3].length; i++){
            if(model[dim1][dim2][dim3][i] == selected){
                num++;
            }
        }
        

        return num;
    }
    
//    probabilitic prediction
   public int getPrediction2(int dim1, int dim2, int dim3){
//        float max = -1;
//        int maxID = -1;
        if(dim1 > model.length-1){
            dim1 = model.length-1;
        }
        float sum = 0;
        for(int i = 0; i < model[dim1][dim2][dim3].length; i++){
            sum += model[dim1][dim2][dim3][i];
        }
        Random r = new Random();
        float tp = r.nextFloat() * sum;
        sum = 0;
        int id = -1;
        for(int i = 0; i < model[dim1][dim2][dim3].length; i++){
            sum += model[dim1][dim2][dim3][i];
            if(sum >= tp){
                id = i;
                break;
            }
        }
        return id;

    }
    
}
