/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

/**
 *
 * @author Peng
 */
public class StoryLearnMain extends javax.swing.JFrame {
    public StoryLearnMain() {
        StoryLearnPanel storyPanel = new StoryLearnPanel();
        setContentPane(storyPanel);
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        pack();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
//                new StoryLearnMain().setVisible(true);
                StoryLearnMain stm = new StoryLearnMain();
                stm.setVisible(true);
                stm.setSize(800,600);
                stm.setExtendedState( stm.MAXIMIZED_BOTH );
            }
        });
    }
}
