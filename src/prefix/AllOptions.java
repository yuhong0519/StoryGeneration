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
public class AllOptions {
    private ArrayList<PPOptions> ao;
    public AllOptions(){
        ao = new ArrayList<PPOptions>();
    }
    
    public void addPPOptions(PPOptions o){
        ao.add(o);
    }
    
    public ArrayList<PPOptions> getAllPPOptions(){
        return ao;
    }
    
    public PPOptions getPPOptions(int index){
//        PPOptions p = null;
        for(int i = 0; i < ao.size(); i++){
            if(ao.get(i).getPPID() == index){
                return ao.get(i);
            }
        }
        
        return null;
        
    }
    
    public ArrayList<PPOptions> getPPOptionList(int index){
        ArrayList<PPOptions> ppoList = new ArrayList<PPOptions>();
        for(int i = 0; i < ao.size(); i++){
            if(ao.get(i).getPPID() == index){
                ppoList.add(ao.get(i));
            }
        }
        
        return ppoList;
    }
    
}
