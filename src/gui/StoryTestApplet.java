/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Container;
import javax.swing.JApplet;

/**
 *
 * @author Peng
 */
public class StoryTestApplet extends JApplet {
    StoryTestPanel storyPanel;
        
    public void init() {
    //Execute a job on the event-dispatching thread:
    //creating this applet's GUI.
     storyPanel = new StoryTestPanel();
     try {
         javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
             public void run() {
                  createGUI();
              }
         });
     } catch (Exception e) {
          System.err.println("createGUI didn't successfully complete");
        }
    }
    
    public void createGUI() {
        this.setSize(1000, 800);
        this.setContentPane(storyPanel);
    }
}
