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
public class PlotPointLibrary {
    private ArrayList<IntegerPlotPoint> pplist;
    public PlotPointLibrary(){
        pplist = new ArrayList<IntegerPlotPoint>();
        
    }
    public void add(IntegerPlotPoint ipp){
        pplist.add(ipp);
    }
    public ArrayList<IntegerPlotPoint> getLibrary(){
        return pplist;
    }
    public IntegerPlotPoint getPP(int index){
        IntegerPlotPoint ipp = null;
        for(int i = 0; i < pplist.size(); i++){
            if(pplist.get(i).id == index){
                ipp = pplist.get(i);
                break;
            }
        }
        return ipp;
    }
    
}
