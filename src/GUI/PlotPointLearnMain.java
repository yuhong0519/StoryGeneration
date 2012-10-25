/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

/**
 *
 * @author Hong Yu
 */
public class PlotPointLearnMain extends javax.swing.JFrame{
    public PlotPointLearnMain() {
        PlotPointLearnPanel pplp = new PlotPointLearnPanel();
        setContentPane(pplp);
        setSize(800, 600);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        pack();
    }
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                PlotPointLearnMain pplm = new PlotPointLearnMain();
                pplm.setVisible(true);
                pplm.setSize(800,600);
                pplm.setResizable(false);
                //pplm.setExtendedState( pplm.MAXIMIZED_BOTH );
            }
        });
    }
    
}
