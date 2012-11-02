/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prefix;

/**
 *
 * @author Hong Yu
 */
public class OptionItem implements Comparable<OptionItem>{
    private int optionID = -1;
    private int PPID = -1;
    private String option = null;
    private double preference = 0;
    private int prefixID;

    public OptionItem(int oi, int pi, String o){
        optionID = oi;
        PPID = pi;
        option = o;
    }
    
    public OptionItem(int oi, int p){
        optionID = oi;
        preference = p;
    }
    
    public OptionItem(OptionItem oi){
        optionID = oi.optionID;
        PPID = oi.PPID;
        option = oi.option;
        preference = oi.getAccuratePreference();
        prefixID = oi.prefixID;
    }
    
    public OptionItem(int oi, String o){
        optionID = oi;
        option = o;
    }

    
    public void setPrefixID(int pID){
        prefixID = pID;
    }
    
    public int getPrefixID(){
        return prefixID;
    }
    
    public void setPreference(int p){
        preference = p;
    }
    
    public void setPreference(double p){
        preference = p;
    }
    
    public int getPreference(){
        return (int)(Math.round(preference));
    }
    
    public double getAccuratePreference(){
        return preference;
    }
    
    
    public void setPPID(int id){
        PPID = id;
    }
    
    public int getPPID(){
        return PPID;
    }
    
    public int getOID(){
        return optionID;
    }
    
    public String getValue(){
        return option;
    }
    public String toString(){
        return option;
    }
    
    public int compareTo(OptionItem oi){
        if(prefixID == oi.prefixID && optionID == oi.optionID)
            return 0;
        else if(prefixID > oi.prefixID){
            return 1;
        }
        else if(prefixID < oi.prefixID){
            return -1;
        }
        else if(optionID > oi.optionID)
            return 1;
        else 
            return -1;
    }
}
