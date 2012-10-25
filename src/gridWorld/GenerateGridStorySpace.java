package gridWorld;

import tools.PrefixUtil;
import java.util.ArrayList;
import java.util.*;

import prefix.*;

public class GenerateGridStorySpace {

//	direction: 1: up, 2: down, 3: left, 4: right
	private static int getNext(int current, int gridSize, int direction){
		if(direction == 1){
			if (current > (gridSize-1)*gridSize){
				return current;
			}
			return current+gridSize;
		}
		else if(direction == 2){
			if(current <= gridSize){
				return current;
			}
			return current-gridSize;
		}
		else if(direction == 3){
			if(current%gridSize == 1){
				return current;
			}
			return current-1;
		}
		else if(direction == 4){
			if(current%gridSize == 0){
				return current;
			}
			return current+1;
		}
		else
			return -1;
	}
	
	private static void appendNext(ArrayList<Prefix> pl, Prefix p, int gridSize){
		if(p.itemList.size() == gridSize+gridSize-1){
			pl.add(new Prefix(p));
			return;
		}
		int current = ((IntegerPlotPoint) p.itemList.get(p.itemList.size()-1)).id;
		int next = getNext(current, gridSize, 1);
		if(next != current && next > 0){
			p.itemList.add(new IntegerPlotPoint(next));
			appendNext(pl, p, gridSize);
			p.itemList.remove(p.itemList.size()-1);
		}
		
		next = getNext(current, gridSize, 4);
		if(next != current && next > 0){
			p.itemList.add(new IntegerPlotPoint(next));
			appendNext(pl, p, gridSize);
			p.itemList.remove(p.itemList.size()-1);
		}
	}
	
	private static void getFullPath(ArrayList<Prefix> pl, int gridSize){
		Prefix p = new Prefix(new int[]{1});
		appendNext(pl, p, gridSize);
		
	}

	private static void getRandomPath(ArrayList<Prefix> pl, int gridSize, int maxLen){
		int numStory = 800;
		
//		double up = 0.4;
//		double down = 0.1;
//		double left = 0.1;
//		double right = 0.4;
		double[] prob = new double[]{0.4, 0.1, 0.1, 0.4}; 
		for(int story = 0; story < numStory; story++){
			Prefix p = new Prefix();
			p.itemList.add(new IntegerPlotPoint(1));
			int current = 1;
			int previous = 0;
			int next = current;
			Random rd = new Random();
			while(p.itemList.size() < maxLen){
				while(next == current){
					double tp = rd.nextDouble();
					if(tp < prob[0]){
						next = getNext(current, gridSize, 1);
						if(next == previous){
							next = current;
						}
					}
					else if(tp < prob[1] + prob[0]){
						next = getNext(current, gridSize, 2);
						if(next == previous){
							next = current;
						}
					}
					else if(tp < prob[1] + prob[0] + prob[2]){
						next = getNext(current, gridSize, 3);
						if(next == previous){
							next = current;
						}
					}
					else{
						next = getNext(current, gridSize, 4);
						if(next == previous){
							next = current;
						}
					}
				}
				p.itemList.add(new IntegerPlotPoint(next));
				if( next == gridSize*gridSize){
					break;
				}
				previous = current;
				current = next;
								
			}
			if(Collections.binarySearch(pl, p) < 0){
				pl.add(p);
				Collections.sort(pl);
			}
			
		}
		
	}
	public static int gridSize = 5;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int maxStoryLen = 20;
		ArrayList<Prefix> storySpace = new ArrayList<Prefix>();
		
		getFullPath(storySpace, gridSize);
		Collections.sort(storySpace);
		
		getRandomPath(storySpace, gridSize, maxStoryLen);
		PrefixUtil.writeStorySpace(storySpace, null);
	}

}
