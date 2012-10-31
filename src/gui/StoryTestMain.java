/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

/**
 *
 * @author Peng
 */
public class StoryTestMain extends javax.swing.JFrame {
    public StoryTestMain() {
        StoryTestPanel storyPanel = new StoryTestPanel();
        setContentPane(storyPanel);
        setSize(800, 600);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        pack();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                StoryTestMain stm = new StoryTestMain();
                stm.setVisible(true);
                stm.setSize(800,600);
                stm.setExtendedState( stm.MAXIMIZED_BOTH );
            }
        });
    }
}
