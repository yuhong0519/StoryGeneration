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
    private int indicatedPP = -1;
    private int PPID = -1;
    private String option = null;
    private double preference = 0;
    private int prefixID;
    private int optionID = -1;

//    public OptionItem(int iPP, int pi, String o){
//        indicatedPP = iPP;
//        PPID = pi;
//        option = o;
//        
//    }
    
    public OptionItem(int iPP, int p){
        indicatedPP = iPP;
        preference = p;
    }
    
    public OptionItem(OptionItem oi){
        indicatedPP = oi.indicatedPP;
        PPID = oi.PPID;
        option = oi.option;
        preference = oi.getAccuratePreference();
        prefixID = oi.prefixID;
        optionID = oi.optionID;
    }
    
    public OptionItem(int indicatePP, int optionID, String o){
        this.indicatedPP = indicatePP;  
        this.optionID = optionID;
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
    
    public int getIndicatedPP(){
        return indicatedPP;
    }
    
    public String getValue(){
        return option;
    }
    public String toString(){
        return option;
    }
    public void setOID(int id){
        optionID = id;
    }
    public int getOID(){
        return optionID;
    }
    
    public int compareTo(OptionItem oi){
//        if(prefixID == oi.prefixID && indicatedPP == oi.indicatedPP && optionID == optionID) {
//            return 0;
//        }
//        else if(prefixID > oi.prefixID){
//            return 1;
//        }
//        else if(prefixID < oi.prefixID){
//            return -1;
//        }
//        else if(indicatedPP > oi.indicatedPP) {
//            return 1;
//        }
//        else if(indicatedPP < oi.indicatedPP){
//            return -1;
//        }
        if(optionID > oi.optionID){
            return 1;
        }
        else if(optionID < oi.optionID){
            return -1;
        }
        else{
            return 0;
        }
    }
}
