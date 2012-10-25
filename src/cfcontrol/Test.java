package cfcontrol;


import tools.CommonUtil;
import tools.PrefixUtil;
import java.io.*;
import java.util.*;

import gridWorld.GenerateGridStorySpace;
import PPCA.*;
import prefix.*;
import nmf.*;
import simulatedStoryWorld.*;

import no.uib.cipr.matrix.*;

public class Test {
    	public static ArrayList<Prefix> prefixList = null;
        public static DenseMatrix cov = null;
        public static DenseMatrix mean = null;
        public static DenseMatrix currentRatings = null;
        public static double[][] mask = null;
        public static DenseMatrix W = null;
        public static NMFModel nmfm = null;
        private static ArrayList<Integer> shown = new ArrayList<Integer>();
        private static boolean reverseFlag = false;
        private static double[] thueModel = new double[]{0,0,0,0,0};
        private static ArrayList<Prefix> tags = PrefixUtil.readPrefixList("Tag.txt", 1);
        
	static int getNext(Matrix predict, double[][] mask, ArrayList<Prefix> prefixList, int currentID){
		double max = -10;
//                double min = 10;
//                double minID = 0;
                
		int maxID = 0;
//                boolean inverseTag = true;
                
                for(int i = 0; i < shown.size(); i++){
                    predict.set(shown.get(i).intValue(), 0, -1);
                }
//              Find next story
                if(prefixList.get(currentID).itemList.size() < prefixList.get(prefixList.size()-1).itemList.size()){
//                    while(true){
                            for(int i = 0; i < predict.numRows(); i++){
                                    if(mask[currentID][i] != 0 && predict.get(i, 0) > max && prefixList.get(i).itemList.size() == prefixList.get(prefixList.size()-1).itemList.size()){
                                            max = predict.get(i, 0);
                                            maxID = i;
                                    }
//                                    if(predict.get(i, 0) < min && prefixList.get(i).itemList.size() == prefixList.get(prefixList.size()-1).itemList.size()){
//                                            min = predict.get(i, 0);
//                                            minID = i;
//                                    }
                            }

//                            if(mask[currentID][maxID] != 0 && prefixList.get(maxID).itemList.size() > prefixList.get(currentID).itemList.size()){
//                                    break;
//                            }
//                            predict.set(maxID, 0, -100);
//                            max = -10;
//                    }


                    return maxID;
                }
//              Start a new story
                else{
                    
                    for(int i = 0; i < predict.numRows(); i++){
                            if(prefixList.get(i).itemList.size() == prefixList.get(prefixList.size()-1).itemList.size() &&
                                predict.get(i, 0) > max){
                                    max = predict.get(i, 0);
                                    maxID = i;
                            }

                    }
                    

                    return maxID;
                }
	}
        
        static int getNextMin(Matrix predict, double[][] mask, ArrayList<Prefix> prefixList, int currentID){
//		double max = -10;
                double min = 10;
                int minID = 0;
                
//		int maxID = 0;
//                boolean inverseTag = true;
                
                for(int i = 0; i < shown.size(); i++){
                    predict.set(shown.get(i).intValue(), 0, 10);
                }
//              Find next story
                if(prefixList.get(currentID).itemList.size() < prefixList.get(prefixList.size()-1).itemList.size()){
                    
                            for(int i = 0; i < predict.numRows(); i++){
                                if(mask[currentID][i] != 0 && prefixList.get(i).itemList.size() == prefixList.get(prefixList.size()-1).itemList.size()){
                                    if(predict.get(i, 0) < min ){
                                            min = predict.get(i, 0);
                                            minID = i;
                                    }
                                }

                            }
//
//                            if(mask[currentID][minID] != 0 && prefixList.get(minID).itemList.size() > prefixList.get(currentID).itemList.size()){
//                                    break;
//                            }
//                            predict.set(minID, 0, 100);
//                            min = 10;
                    


                    return minID;
                }
//              Start a new story
                else{
                    
                    for(int i = 0; i < predict.numRows(); i++){
                            if(prefixList.get(i).itemList.size() == prefixList.get(prefixList.size()-1).itemList.size() &&
                                predict.get(i, 0) < min){
                                    min = predict.get(i, 0);
                                    minID = i;
                            }

                    }
                    

                    return minID;
                }
	}
                
        public static void restart(){
            if(prefixList == null){
                prefixList = PrefixUtil.readPrefixList(PrefixUtil.prefixListFile, 1);
                currentRatings = new DenseMatrix(prefixList.size(), 1);
                
            }
            else
                currentRatings.zero();
            
            shown.clear();
            for(int i = 0; i < thueModel.length; i++){
                thueModel[i] = 0;
            }
        }
        
        
        static int computeThueNext(int currentID, double[][] mask){
            int id = -1;
            double value = -1;
            if(reverseFlag){
                value = 100;
            }
            for(int i = 0; i < tags.size(); i++){
                if(mask[currentID][i] != 0 && prefixList.get(i).itemList.size() == prefixList.get(prefixList.size()-1).itemList.size()){
                    double tp = GenerateRatings.calculateValue(tags.get(i), thueModel);
                    if(!reverseFlag){
                        if(tp > value){
                            value = tp;
                            id = i;
                        }
                    }
                    else{
                        if(tp < value){
                            value = tp;
                            id = i;
                        }
                    }
                }
            }
            
            return id;
            
        }
        
        
//        Inputs: 
//          currentStory should be in the format: 2: 1 2 3
//          algorith 1: PPCA, 2: NMF, 3: thue
//        It keeps records of all the previous rating in the currentRatings variable. 
//        Return:
//          Integer list containing the next possible story
        public static int[] getNextRecommendation(String currentStory, int algorithm, boolean addShown){
            if(prefixList == null){
                prefixList = PrefixUtil.readPrefixList(PrefixUtil.prefixListFile, 1);
                currentRatings = new DenseMatrix(prefixList.size(), 1);
                
            }
            int id = -1;
            if(currentStory != ""){
                currentStory = currentStory.trim();
                String[] vs = currentStory.split(" ");
                double[] data = new double[vs.length-1];
                int label = 0;
                try{
                        for(int i = 1; i < vs.length; i++){
                                data[i-1] = Integer.parseInt(vs[i]);
                        }

                        label = Integer.parseInt(vs[0].substring(0, vs[0].length()-1));
                }
                catch(Exception e){
                        System.out.println("Format exception: Wrong Input! Try again:");
                        return null;
                }
                id = Collections.binarySearch(prefixList, new Prefix(data));
                if(id < 0){
                        System.out.println("Cannot find the prefix! Try again:");
                        return null;
                }
                currentRatings.set(id, 0, label);
                if(addShown && prefixList.get(id).itemList.size() == prefixList.get(prefixList.size()-1).itemList.size())
                    shown.add(new Integer(id));
            
//          This is the a full story, no more recommendation on this one
                if( data.length == prefixList.get(prefixList.size()-1).itemList.size()){
//                        System.out.println("Finished!");
//                        return new int[]{-1};
                }
//              David Thue algorithm
                if(algorithm == 3){
                    int tlabel = label - 3;
                    for(int i = 0; i < thueModel.length; i++){
                        thueModel[i] += tlabel * (tags.get(id).itemList.get(i).id - 1) / 10.0;
                        if(thueModel[i] < 0){
                            thueModel[i] = 0;
                        }
                    }                 
                }
                
            }
            if(mask == null){
                mask = CommonUtil.readData("Mask.txt", false);
            }
            
            if (algorithm == 1){
                if(cov == null){
                    cov = new DenseMatrix(CommonUtil.readData("cov1.txt", false));
                    mean = new DenseMatrix(CommonUtil.readData("mean1.txt", false));
                    
                }

                Matrix predict =  PPCA.ppca_test(cov, mean, currentRatings);
                int recommend;
                if(reverseFlag){
                    recommend = getNextMin(predict, mask, prefixList, id);
                }
                else
                    recommend = getNext(predict, mask, prefixList, id);
                
                ArrayList<PlotPoint> tp = prefixList.get(recommend).itemList;
                int[] rt = new int[tp.size()];
                for(int i = 0; i < rt.length; i++){
                    rt[i] = tp.get(i).id;
                }
                return rt;
            }
            else if( algorithm == 2){
                if(W == null){
                    W = new DenseMatrix(CommonUtil.readData("W1.txt", false));
                    nmfm.W = W;
                }
                
                Matrix predict = nmf.NMF.nmf_test(nmfm, currentRatings);
//                int recommend = getNext(predict, mask, prefixList, id);
                int recommend;
                if(reverseFlag){
                    recommend = getNextMin(predict, mask, prefixList, id);
                }
                else
                    recommend = getNext(predict, mask, prefixList, id);
                
                ArrayList<PlotPoint> tp = prefixList.get(recommend).itemList;
                int[] rt = new int[tp.size()];
                for(int i = 0; i < rt.length; i++){
                    rt[i] = tp.get(i).id;
                }
                return rt;
                
            }
            else if(algorithm == 3){
                int recommend = computeThueNext(id, mask);
                ArrayList<PlotPoint> tp = prefixList.get(recommend).itemList;
                int[] rt = new int[tp.size()];
                for(int i = 0; i < rt.length; i++){
                    rt[i] = tp.get(i).id;
                }
                return rt;
            }
            else
                return null;
        }

        
	public static void main(String[] args) {
		String line = null;
                System.out.println("Loading data...");
		prefixList = PrefixUtil.readPrefixList(PrefixUtil.prefixListFile, 1);
		
		cov = new DenseMatrix(CommonUtil.readData("cov1.txt", false));
		mean = new DenseMatrix(CommonUtil.readData("mean1.txt", false));
		mask = CommonUtil.readData("Mask.txt", false);
                W = new DenseMatrix(CommonUtil.readData("W1.txt", false));
                nmfm.W = W;
                currentRatings = new DenseMatrix(prefixList.size(), 1);
                int algorithm = 2;
                //MyUtil.printRemoteObject(cov, "ttt.txt");
		System.out.println("Finished!\nInput:\n");
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			line = br.readLine();
			
			while(line != ""){
				line = line.trim();
				String[] vs = line.split(" ");
				double[] data = new double[vs.length-1];
				int label = 0;
				try{
					for(int i = 1; i < vs.length; i++){
						data[i-1] = Integer.parseInt(vs[i]);
					}
				
					label = Integer.parseInt(vs[0].substring(0, vs[0].length()-1));
				}
				catch(Exception e){
					System.out.println("Format exception: Wrong Input! Try again:");
					line = br.readLine();
					continue;
				}
				int id = Collections.binarySearch(prefixList, new Prefix(data));
				if(id < 0){
					System.out.println("Cannot find the prefix! Try again:");
					line = br.readLine();
					continue;
				}
				currentRatings.set(id, 0, label);
				if(data[data.length-1] == GenerateGridStorySpace.gridSize*GenerateGridStorySpace.gridSize || data.length == prefixList.get(prefixList.size()-1).itemList.size()){
					System.out.println("Finished!");
					line = br.readLine();
					continue;
				}
                                Matrix predict = null;
                                if(algorithm == 1){
                                    predict =  PPCA.ppca_test(cov, mean, currentRatings);
                                }
                                else if(algorithm == 2){
                                    predict = nmf.NMF.nmf_test(nmfm, currentRatings);
                                }
				int recommend = getNext(predict, mask, prefixList, id);
				System.out.println(prefixList.get(recommend).itemList);
				line = br.readLine();
			
		
                        }
                }
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
