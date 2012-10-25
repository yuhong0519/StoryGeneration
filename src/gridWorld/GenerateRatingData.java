package gridWorld;
import java.util.*;

import prefix.Prefix;
import tools.PrefixUtil;
import tools.CommonUtil;

public class GenerateRatingData {



	static boolean contains(double[] data, double value){
		boolean exist = false;
		for(int i = 0; i < data.length; i++){
			if (data[i] == value){
				exist = true;
				break;
			}
			
		}
		
		return exist;
		
	}
	
	static boolean contains(Prefix p, double value){
		boolean exist = false;
		for(int i = 0; i < p.itemList.size(); i++){
			if (p.itemList.get(i).id == value){
				exist = true;
				break;
			}
			
		}
		
		return exist;
	}
	
	private static double[][] getRating1(ArrayList<Prefix> prefixList, ArrayList<Prefix> storyChosen, double[][] storySpace, int[] standard, int totalPeople, int numStorysPP){
		double[][] data = new double[prefixList.size()][totalPeople];
		
		Random rd = new Random();
		for(int i = 0; i < totalPeople; i++){
			for(int j = 0; j < numStorysPP; j++){
				int storyID = rd.nextInt(storySpace.length);
				storyChosen.add(new Prefix(storySpace[storyID]));
				
				for(int k = 0; k < storySpace[storyID].length; k++){
					Prefix pi = new Prefix(Arrays.copyOf(storySpace[storyID], k+1));
					int prefixID = Collections.binarySearch(prefixList, pi);
					if(contains(Arrays.copyOf(storySpace[storyID], k+1), standard[0])){
						data[prefixID][i] = (int)(rd.nextGaussian()/2 + 5);
						data[prefixID][i] = data[prefixID][i] > 5 ? 5 : data[prefixID][i];
						data[prefixID][i] = data[prefixID][i] < 1 ? 1 : data[prefixID][i];
					}
					else if(contains(Arrays.copyOf(storySpace[storyID], k+1), standard[1])){
						data[prefixID][i] = (int)(rd.nextGaussian()/2 + 4);
						data[prefixID][i] = data[prefixID][i] > 5 ? 5 : data[prefixID][i];
						data[prefixID][i] = data[prefixID][i] < 1 ? 1 : data[prefixID][i];
					}
					else if(contains(Arrays.copyOf(storySpace[storyID], k+1), standard[2])){
						data[prefixID][i] = (int)(rd.nextGaussian()/2 + 3);
						data[prefixID][i] = data[prefixID][i] > 5 ? 5 : data[prefixID][i];
						data[prefixID][i] = data[prefixID][i] < 1 ? 1 : data[prefixID][i];
					}
					else if(contains(Arrays.copyOf(storySpace[storyID], k+1), standard[3])){
						data[prefixID][i] = (int)(rd.nextGaussian()/2 + 2);
						data[prefixID][i] = data[prefixID][i] > 5 ? 5 : data[prefixID][i];
						data[prefixID][i] = data[prefixID][i] < 1 ? 1 : data[prefixID][i];
					}
					else if(contains(Arrays.copyOf(storySpace[storyID], k+1), standard[4])){
						data[prefixID][i] = (int)(rd.nextGaussian()/2 + 1);
						data[prefixID][i] = data[prefixID][i] > 5 ? 5 : data[prefixID][i];
						data[prefixID][i] = data[prefixID][i] < 1 ? 1 : data[prefixID][i];
					}
					else{
						data[prefixID][i] = (int)(rd.nextGaussian()/2 + 3);
						data[prefixID][i] = data[prefixID][i] > 5 ? 5 : data[prefixID][i];
						data[prefixID][i] = data[prefixID][i] < 1 ? 1 : data[prefixID][i];
					}
				}
			}
			
		}
		return data;
	}
	
//	standard: 0, prefer short story; 1: prefer long story; 2: prefer loops; 3: prefer prefer no loops 
	private static double[][] getRating2(ArrayList<Prefix> prefixList, ArrayList<Prefix> storyChosen, double[][] storySpace, int standard, int totalPeople, int numStorysPP){
		double[][] data = new double[prefixList.size()][totalPeople];
		
		Random rd = new Random();
		for(int i = 0; i < totalPeople; i++){
			for(int j = 0; j < numStorysPP; j++){
				int storyID = rd.nextInt(storySpace.length);
				storyChosen.add(new Prefix(storySpace[storyID]));
				boolean loopflag = false;
				boolean backflag = false;
				double currentRating = 3;
				double lp = 1.3;
//				System.out.println(storySpace[storyID].length);
				for(int k = 0; k < storySpace[storyID].length; k++){
					Prefix pi = new Prefix(Arrays.copyOf(storySpace[storyID], k+1));
					int prefixID = Collections.binarySearch(prefixList, pi);
					
					if(!backflag && k>0 && storySpace[storyID][k] < storySpace[storyID][k-1]){
						backflag = true;
					}
					if(!loopflag && k > 1){
						loopflag = contains(Arrays.copyOf(storySpace[storyID], k), storySpace[storyID][k]);
					}
					
					if(k == 0){
						data[prefixID][i] = (int)(rd.nextGaussian()/2 + 3);
					}
					else if(standard == 0){

						if(!backflag){
							
							currentRating = 5;
							data[prefixID][i] = (int)(rd.nextGaussian()/2 + currentRating);
						}
						else{
							currentRating = 1;
							data[prefixID][i] = (int)(rd.nextGaussian()/2 + currentRating);
						}
	
					}
					else if(standard == 1){
						if(!backflag){
							currentRating = 1;
			
							data[prefixID][i] = (int)(rd.nextGaussian()/2 + currentRating);
						}
						else{
							currentRating = 5;
							data[prefixID][i] = (int)(rd.nextGaussian()/2 + currentRating);
						}
	
					}
					else if(standard == 2){

						if(loopflag){
							
							currentRating = 5;
							data[prefixID][i] = (int)(rd.nextGaussian()/2 + currentRating);
						}
						else{
							currentRating = 1;

							data[prefixID][i] = (int)(rd.nextGaussian()/2 + currentRating);
						}

					}
					else if(standard == 3){

						if(loopflag){
							currentRating = 1;
							
							data[prefixID][i] = (int)(rd.nextGaussian()/2 + currentRating);
						}
						else{
							currentRating = 5;
					
							data[prefixID][i] = (int)(rd.nextGaussian()/2 + currentRating);
						}

					}
					data[prefixID][i] = data[prefixID][i] > 5 ? 5 : data[prefixID][i];
					data[prefixID][i] = data[prefixID][i] < 1 ? 1 : data[prefixID][i];
														
				}
			}
			
		}
		return data;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double[][] storySpace = CommonUtil.readData("GridStorySpace.txt", false);
		String datafile1 = "data1.txt";
		String datafile2 = "data2.txt";
		String datafile3 = "data3.txt";
		String datafile4 = "data4.txt";
		String datafile5 = "data5.txt";
		String datafile6 = "data6.txt";
		
		int peoplePC = 40;
		int numStorysPP = 40;
		
		ArrayList<Prefix> prefixList = PrefixUtil.readPrefixList(PrefixUtil.prefixListFile, 1);
		ArrayList<Prefix> storyChosen = new ArrayList<Prefix>();
		double data[][] = getRating1(prefixList, storyChosen, storySpace, new int[]{5, 9, 13, 17, 21}, peoplePC, numStorysPP);
		CommonUtil.printObject(data, datafile1);
		data = getRating1(prefixList, storyChosen, storySpace, new int[]{21, 17, 13, 9, 5}, peoplePC, numStorysPP);
		CommonUtil.printObject(data, datafile2);
		data = getRating2(prefixList, storyChosen, storySpace, 0, peoplePC, numStorysPP);
		CommonUtil.printObject(data, datafile3);
		data = getRating2(prefixList, storyChosen, storySpace, 1, peoplePC, numStorysPP);
		CommonUtil.printObject(data, datafile4);
		data = getRating2(prefixList, storyChosen, storySpace, 2, peoplePC, numStorysPP);
		CommonUtil.printObject(data, datafile5);
		data = getRating2(prefixList, storyChosen, storySpace, 3, peoplePC, numStorysPP);
		CommonUtil.printObject(data, datafile6);
		
		PrefixUtil.writeStorySpace(storyChosen, "StoryChosen.txt");
		
	}

}
