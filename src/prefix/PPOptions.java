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
        PPID = -1; // no plot point id for prefix
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
        OptionItem oi = getOptionItem(id);
        
        for(int i = 0; i < options.size(); i++){
            if(options.get(i).getAccuratePreference() > oi.getAccuratePreference()){
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
            if(options.get(i).getIndicatedPP() == index){
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
     
//    get the option items by their perference ranking pos
    public ArrayList<OptionItem> getOptionItembyPosition(int pos){
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
