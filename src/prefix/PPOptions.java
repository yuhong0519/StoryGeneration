/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prefix;
import java.util.*;

/**
 *
 * @author Hong Yu
 */
public class PPOptions {
    private ArrayList<OptionItem> options;
    private int PPID;
    private int prefixID;
    
    public PPOptions(int id){
        options = new ArrayList<OptionItem>();
        PPID = id;
    }
    
    public void setPrefixID(int pID){
        prefixID = pID;
    }
    
    public int getPrefixID(){
        return prefixID;
    }
    
//    Create Prefix options, deep copy
    public PPOptions(PPOptions ppo){
        PPID = -1; // no plot point id for prefix
        options = new ArrayList<OptionItem>();
        if(ppo != null){
            ArrayList<OptionItem> o = ppo.getAllOptions();
            for(int i = 0; i < o.size(); i++){
                options.add(new OptionItem(o.get(i)));
            }
        }
    }
    
    public int getOptionItemPreferencePosition(int id){
        int position = 1;
        OptionItem oi = getOptionItem(id);
        
        for(int i = 0; i < options.size(); i++){
            if(options.get(i).getPreference() > oi.getPreference()){
                position++;
                
            }
        }
        
        return position;
    }
    
    public void add(OptionItem oi){
        oi.setPPID(PPID);
        options.add(oi);
    }
    public ArrayList<OptionItem> getAllOptions(){
        return options;
    }
    public OptionItem getOptionItem(int index){
        OptionItem oi = null;
        for(int i = 0; i < options.size(); i++){
            if(options.get(i).getOID() == index){
                oi = options.get(i);
            }
        }
        return oi;
    }
    public int getPPID(){
        return PPID;
    }
    
    public int getHighestItemRating(){
        int max = -1;
        for(int i = 0; i < options.size(); i++){
            if(options.get(i).getPreference() > max){
                max = options.get(i).getPreference();
            }
        }        
        
        return max;
    }
    
    public int getLowestItemRating(){
        int min = 10;
        for(int i = 0; i < options.size(); i++){
            if(options.get(i).getPreference() < min){
                min = options.get(i).getPreference();
            }
        }        
        
        return min;
    }
        
}
