/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulatedStoryWorld;

/**
 *
 * @author Hong Yu
 */
import java.util.*;
import prefix.Prefix;
import tools.PrefixUtil;
import tools.CommonUtil;


public class GenerateRatings {
    
    static double[][] people;
    static double[][] peopleStyle = new double[][]{{1,0,0,0,0},{0,1,0,0,0},{0,0,1,0,0},{0,0,0,1,0},{0,0,0,0,1}};
    
    static void initialPeople(){
        people = new double[][]{
//            {1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},
//            {0,1,0,0,0}, {0,1,0,0,0},{0,1,0,0,0}, {0,1,0,0,0},
//            {0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},
//            {0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},
//            {0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},
            
            {1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},
            {0,1,0,0,0}, {0,1,0,0,0},{0,1,0,0,0},{0,1,0,0,0},{0,1,0,0,0},{0,1,0,0,0},
            {0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},
            {0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},
            {0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},
//            
            {1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},
            {0,1,0,0,0}, {0,1,0,0,0},{0,1,0,0,0},{0,1,0,0,0},{0,1,0,0,0},{0,1,0,0,0},
            {0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},
            {0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},
            {0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},
            
                        {1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},
            {0,1,0,0,0}, {0,1,0,0,0},{0,1,0,0,0},{0,1,0,0,0},{0,1,0,0,0},{0,1,0,0,0},
            {0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},
            {0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},
            {0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},
//            
            {1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},
            {0,1,0,0,0}, {0,1,0,0,0},{0,1,0,0,0},{0,1,0,0,0},{0,1,0,0,0},{0,1,0,0,0},
            {0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},
            {0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},
            {0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},
            
                        {1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},
            {0,1,0,0,0}, {0,1,0,0,0},{0,1,0,0,0},{0,1,0,0,0},{0,1,0,0,0},{0,1,0,0,0},
            {0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},
            {0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},
            {0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},
//            
            {1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},
            {0,1,0,0,0}, {0,1,0,0,0},{0,1,0,0,0},{0,1,0,0,0},{0,1,0,0,0},{0,1,0,0,0},
            {0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},
            {0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},
            {0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},
//            
//                        {1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},
//            {0,1,0,0,0}, {0,1,0,0,0},{0,1,0,0,0},{0,1,0,0,0},{0,1,0,0,0},{0,1,0,0,0},
//            {0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},
//            {0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},
//            {0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},{0,0,0,0,1},
        };
    }
    public static double calculateValue(Prefix story, double[] people){
        Random rd = new Random();
        double  sumP = 0, cos = 0;
        for(int i = 0; i < people.length; i++){
            double tp = story.itemList.get(i).id;
            if(tp == 1)
                tp--;
            tp /= 10;
//            sumS += tp*tp;
            sumP += people[i]*people[i];
            cos += tp*people[i];
        }
        if(sumP == 0){
            return 1;
        }
        cos = cos / Math.sqrt(sumP);
        cos = cos * 5;
        cos += rd.nextGaussian();
        cos = cos > 5 ? 5 : cos;
        cos = cos < 1 ? 1 : cos;
        return cos ;
    }
    
    static int[][] getRatings(ArrayList<Prefix> prefixList, ArrayList<Prefix> tags, ArrayList<Prefix> storyChosen, double[][] storySpace, int numStoryPP){
        int numPeople = people.length;
        int[][] data = new int[prefixList.size()][numPeople];

        Random rd = new Random();
            for(int i = 0; i < numPeople; i++){
                    for(int j = 0; j < numStoryPP; j++){
                            int storyID = rd.nextInt(storySpace.length);
                            storyChosen.add(new Prefix(storySpace[storyID]));

                            for(int k = 0; k < storySpace[storyID].length; k++){
                                    Prefix pi = new Prefix(Arrays.copyOf(storySpace[storyID], k+1));
                                    int prefixID = Collections.binarySearch(prefixList, pi);
                                    data[prefixID][i] = Math.round((float)(calculateValue(tags.get(prefixID), people[i])));
//                                    if(contains(Arrays.copyOf(storySpace[storyID], k+1), standard[0])){
//                                            data[prefixID][i] = (int)(rd.nextGaussian()/2 + 5);
//                                            data[prefixID][i] = data[prefixID][i] > 5 ? 5 : data[prefixID][i];
//                                            data[prefixID][i] = data[prefixID][i] < 1 ? 1 : data[prefixID][i];
//                                    }

                            }
                    }
			
		}
        
        
        return data;
    }
    
    static int[][] getPrior(ArrayList<Prefix> tags){
        int[][] prior = new int[tags.size()][peopleStyle.length];
        for(int i = 0; i < peopleStyle.length; i++){
            for(int j = 0; j < tags.size(); j++){
                prior[j][i] = Math.round((float)(calculateValue(tags.get(j), peopleStyle[i])));
                
            }
        }
        return prior;
    }

    public static void main(String[] args) {
            // TODO Auto-generated method stub
            double[][] storySpace = CommonUtil.readData(PrefixUtil.storySpaceFile, false);
            String datafile = "simulatedRatings.txt";

//            int peoplePC = 10;
            int numStorysPP = 10;
            initialPeople();
            ArrayList<Prefix> prefixList = PrefixUtil.readPrefixList(PrefixUtil.prefixListFile, 1);
            ArrayList<Prefix> tags = PrefixUtil.readPrefixList("Tag.txt", 1);
            
            ArrayList<Prefix> storyChosen = new ArrayList<Prefix>();
            int data[][] = getRatings(prefixList, tags, storyChosen, storySpace, numStorysPP);
            CommonUtil.printObject(data, datafile);
            PrefixUtil.writeStorySpace(storyChosen, "StoryChosen.txt");
            
//            int[][] prior = getPrior(tags);
//            MyUtil.printObject(prior, "Prior.txt");
    }

}
