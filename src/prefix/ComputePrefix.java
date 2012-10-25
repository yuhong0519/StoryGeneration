package prefix;

import tools.PrefixUtil;
import java.util.*;
import java.io.*;
import no.uib.cipr.matrix.*;
import tools.CommonUtil;
// Compute prefix from story space
public class ComputePrefix {


//	Generate mask matrix: 1 represents dependent, 0 represents independent
	public static DenseMatrix computeMask(String storyFile, String prefixFile){
		
		ArrayList<Prefix> pl = PrefixUtil.readPrefixList(prefixFile, 1);
		DenseMatrix mask = new DenseMatrix(pl.size(), pl.size());
		for(int i = 0; i < pl.size(); i++){
			mask.set(i, i, 1);
		}
		for(int i = 0; i < pl.size() - 1; i++){
			for(int j = i + 1; j < pl.size(); j++){
				if(pl.get(i).itemList.size() == pl.get(j).itemList.size()){
					mask.set(i, j, 1);
					mask.set(j, i, 1);
					
				}
				else{
					break;
				}
			}
		}
		try{
			BufferedReader br = new BufferedReader(new FileReader(storyFile));
			String line;
			while((line = br.readLine()) != null){
				String[] t = line.split("\t");
				int[] list = new int[t.length];
				for(int i = 0; i < list.length; i++){
					list[i] = Integer.parseInt(t[i]);
				}
				Prefix[] items = new Prefix[list.length];
				for(int i = 0; i < list.length; i++){
					items[i] = new Prefix(Arrays.copyOf(list, i+1));
				}
				for(int i = 0; i < items.length; i++){
					for(int j = i+1; j < items.length; j++){
						int t1 = Collections.binarySearch(pl, items[i]);
						int t2 = Collections.binarySearch(pl, items[j]);
						mask.set(t1, t2, 1);
						mask.set(t2, t1, 1);
					}
				}			
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		return mask;
		
	}
	
	public static void main(String[] args){
                
		String storySpace = new String(PrefixUtil.storySpaceFile);
		ArrayList<Prefix> pl = PrefixUtil.readStorySpace(storySpace);
		PrefixUtil.writePrefixList(pl);
		DenseMatrix mask = computeMask(storySpace, PrefixUtil.prefixListFile);
		CommonUtil.printObject(mask, "Mask.txt");
	}
}
