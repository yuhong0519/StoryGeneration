/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

/**
 *
 * @author Peng
 */
public class PlotPoint {
    private String text;
    private int id;
    
    public PlotPoint() {
        text = "";
    }
    
    public PlotPoint(String s, int i) {
        text = s;
        id = i;
    }
    
    public String toString() {
        return text;
    }
    
    public int getId() {
        return id;
    }
}
