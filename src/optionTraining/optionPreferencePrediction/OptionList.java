/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package optionTraining.optionPreferencePrediction;

/**
 *
 * @author Bunnih
 */
import java.util.*;
import prefix.*;

public class OptionList {
    private ArrayList<OptionItem> optionItemList = null;
    
    public OptionList(ArrayList<OptionItem> oil){
        optionItemList = oil;
    }
    
    public ArrayList<OptionItem> getOptionListArray(){
        return optionItemList;
    }
    
    /**
     * Get the position of oi in the option list
     * @param oi
     * @return the position of oi
     */
    public int getOptionItemPosition(OptionItem oi){
        int id = -1;
        for(int i = 0; i < optionItemList.size(); i++){
            if(optionItemList.get(i).compareTo(oi) == 0){
                id = i;
                return id;
            }
        }
        return id;
    }
    
    
    
}
