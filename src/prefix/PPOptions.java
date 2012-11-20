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
    //    Create Prefix options, deep copy
    public PPOptions(PPOptions ppo){
        PPID = ppo.PPID; 
        prefixID = ppo.prefixID;
        options = new ArrayList<OptionItem>();
        if(ppo != null){
            ArrayList<OptionItem> o = ppo.getAllOptions();
            for(int i = 0; i < o.size(); i++){
                options.add(new OptionItem(o.get(i)));
            }
        }
    }
    
    public void resetPreference(){
        for(OptionItem oi : options){
            oi.setPreference(0);
        }
        
    }
    
    public void setPrefixID(int pID){
        prefixID = pID;
    }
    
    public int getPrefixID(){
        return prefixID;
    }
    
    public int getOptionItemPreferencePosition(int id){
        int position = 1;
        OptionItem oi = getItemByIndicatedPP(id);
        
        for(int i = 0; i < options.size(); i++){
            if(options.get(i).getAccuratePreference() > oi.getAccuratePreference()){
                position++;
                
            }
        }
        
        return position;
    }
    
    public void add(OptionItem oi){
        oi.setPPID(PPID);
        oi.setPrefixID(prefixID);
        options.add(oi);
    }
    
    public ArrayList<OptionItem> getAllOptions(){
        return options;
    }
    
    /**
     * Get option item with indicated plot point indicatedPP 
     */
    public OptionItem getItemByIndicatedPP(int indicatedPP){
        OptionItem oi = null;
        for(int i = 0; i < options.size(); i++){
            if(options.get(i).getIndicatedPP() == indicatedPP){
                oi = options.get(i);
            }
        }
        return oi;
    }
    
     /**
     * Get all the option items with the indicated plot points indicatedPP 
     */
    public ArrayList<OptionItem> getItemListByIndicatedPP(int indicatedPP){
        ArrayList<OptionItem> ret = new ArrayList<OptionItem>();
        for(int i = 0; i < options.size(); i++){
            if(options.get(i).getIndicatedPP() == indicatedPP){
                ret.add(options.get(i));
            }
        }
        return ret;
    }
    
    /**
     * Get the Option Item with optionID id
     */
    public OptionItem getItemByID(int id){
        OptionItem oi = null;
        for(int i = 0; i < options.size(); i++){
            if(options.get(i).getOID() == id){
                oi = options.get(i);
            }
        }
        return oi;
    }
    
    /**
     * Find the first occurrence of Option Item oi
     * @param oi: the Option Item to find
     * @return the position of the option item, -1 if not found
     */
    public int getOptionItemPos(OptionItem oi){
        int ret = -1;        
        for(int i = 0; i < options.size(); i++){
            if(options.get(i).getOID() == oi.getOID()){
                ret = i;
            }
        }
        return ret;
    }
    
    /**
     * Get all the indicated plot point IDs
     * @return Integer ArrayList contains all the distinct plot point IDs
     */
    public ArrayList<Integer> getAllIndicatedPP(){
        ArrayList<Integer> ret = new ArrayList<Integer>();
        for(OptionItem oi : options){
            int ipp = oi.getIndicatedPP();
            if(Collections.binarySearch(ret, ipp) < 0){
                ret.add(ipp);
                Collections.sort(ret);
            }
        }
        return ret;
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
     
//    get the option items by their perference ranking pos
    public ArrayList<OptionItem> getItembyPreference(int pos){
        if(pos > options.size()-1){
            System.err.println("Preference position out of range!");
            return null;
        }
        ArrayList<OptionItem> ret = new ArrayList<OptionItem>();
        ArrayList<OptionItem> list = new ArrayList<OptionItem>(options);
        sortPreference(list);
        OptionItem oi = list.get(pos);
        for(OptionItem t : options){
            if(t.getAccuratePreference() == oi.getAccuratePreference()){
                ret.add(t);
            }
        }
        return ret;
    }
    
    private void sortPreference(ArrayList<OptionItem> list){
        for(int i = 0; i < list.size(); i++){
            OptionItem cm = list.get(i);
            for(int j = i + 1; j < list.size(); j++){
                if(list.get(j).getAccuratePreference() >= cm.getAccuratePreference()){
                    cm = list.get(j);
                }
            }
            list.remove(cm);
            list.add(i, cm);
        }        
    }
    

}
