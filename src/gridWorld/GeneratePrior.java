package gridWorld;
import java.util.*;
import no.uib.cipr.matrix.*;
import prefix.Prefix;
import tools.PrefixUtil;
import tools.CommonUtil;

public class GeneratePrior {

	/**
	 * @param args
	 */
	
	static boolean checkBack(Prefix p){
		boolean flag = false;
		for(int i = 0; i < p.itemList.size() - 1; i++){
			if(p.itemList.get(i+1).id < p.itemList.get(i).id){
				flag = true;
				break;
			}
		}
		return flag;
	}
	static boolean checkLoop(Prefix p){
		boolean flag = false;
		
BreakFlag:
	for(int i = 0; i < p.itemList.size() - 1; i++){
			for(int j = i+1; j < p.itemList.size(); j++){
				if(p.itemList.get(i).id == p.itemList.get(j).id){
					flag = true;
					break BreakFlag;
				}
			}
		}
		return flag;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Prefix> prefixList = PrefixUtil.readPrefixList(PrefixUtil.prefixListFile, 1);
		int numPrior = 6;
		DenseMatrix prior = new DenseMatrix(prefixList.size(), numPrior);
		int[] label1 = new int[]{5, 9, 13, 17, 21};
		int[] label2 = new int[]{21, 17, 13, 9, 5};
		
		for(int i = 0; i < prefixList.size(); i++){
			if(GenerateRatingData.contains(prefixList.get(i), label1[0])){
				prior.set(i, 0, 5);
			}
			else if (GenerateRatingData.contains(prefixList.get(i), label1[1])){
				prior.set(i, 0, 4);
			}
			else if (GenerateRatingData.contains(prefixList.get(i), label1[2])){
				prior.set(i, 0, 3);			
			}
			else if (GenerateRatingData.contains(prefixList.get(i), label1[3])){
				prior.set(i, 0, 2);
			}
			else if (GenerateRatingData.contains(prefixList.get(i), label1[4])){
				prior.set(i, 0, 1);
			}
			else
				prior.set(i, 0, 3);
		}
		
		
		for(int i = 0; i < prefixList.size(); i++){
			if(GenerateRatingData.contains(prefixList.get(i), label2[0])){
				prior.set(i, 1, 5);
			}
			else if (GenerateRatingData.contains(prefixList.get(i), label2[1])){
				prior.set(i, 1, 4);
			}
			else if (GenerateRatingData.contains(prefixList.get(i), label2[2])){
				prior.set(i, 1, 3);			
			}
			else if (GenerateRatingData.contains(prefixList.get(i), label2[3])){
				prior.set(i, 1, 2);
			}
			else if (GenerateRatingData.contains(prefixList.get(i), label2[4])){
				prior.set(i, 1, 1);
			}
			else
				prior.set(i, 1, 3);
		}
		
		for(int i = 0; i < prefixList.size(); i++){
			if(checkBack(prefixList.get(i))){
				prior.set(i, 2, 5);
				prior.set(i, 3, 1);
				
			}
			else{
				prior.set(i, 2, 1);
				prior.set(i, 3, 5);
			}
			
			if(checkLoop(prefixList.get(i))){
				prior.set(i, 4, 5);
				prior.set(i, 5, 1);
			}
			else{
				prior.set(i, 4, 1);
				prior.set(i, 5, 5);
			}
		}
		
		CommonUtil.printObject(prior, "prior.txt");
		
	}

}
