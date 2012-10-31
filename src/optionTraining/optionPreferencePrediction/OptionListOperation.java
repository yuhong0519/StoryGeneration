/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package optionTraining.optionPreferencePrediction;

import java.util.ArrayList;
import java.util.Collections;
import prefix.AllOptions;
import prefix.OptionItem;
import prefix.PPOptions;
import prefix.Prefix;
import tools.CommonUtil;
import tools.PrefixUtil;

/**
 *
 * @author Bunnih
 */
public class OptionListOperation {
    private static String optionListFile = "OptionItemList.txt";
//    Create a list of option items in a sequence. 
    private static ArrayList<OptionItem> createOptionItemList(){
        ArrayList<Prefix> prefixList = PrefixUtil.readPrefixList(PrefixUtil.prefixListFile, 1);
        AllOptions ao = PrefixUtil.readOptions(PrefixUtil.optionFile);
        
        ArrayList<OptionItem> optionItemList = new ArrayList<OptionItem>();
        
        PPOptions ppo;
        for(int i = 0; i < prefixList.size(); i++){
            Prefix p = prefixList.get(i);
            if((ppo = ao.getPPOptions(p.itemList.get(p.itemList.size()-1).id)) != null){
                for(int j = 0; j < ppo.getAllOptions().size(); j++){
                    OptionItem oi = new OptionItem(ppo.getAllOptions().get(j));
                    if(optionItemList.size() == 292){
                        System.out.println(oi.getValue());
                    }
                    oi.setPrefixID(i);
                    optionItemList.add(oi);
                    Collections.sort(optionItemList);
                }
            }
                          
        }
        PrefixUtil.writeOptionItemList(optionItemList, optionListFile);
        return optionItemList;
    }
    
    private static OptionList optionList = null;
    public static OptionList getOptionList(){
        if(optionList == null){
            ArrayList<OptionItem> oi = new ArrayList<OptionItem>();
            PrefixUtil.readOptionItemList(oi, optionListFile);
            optionList = new OptionList(oi);
        }
        return optionList;
         
    }
    
    public static void main(String[] args){
        createOptionItemList();
        
    }
    
    
    
    
}
