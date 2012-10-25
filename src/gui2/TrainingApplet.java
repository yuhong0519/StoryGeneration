/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui2;

import javax.swing.JApplet;
import java.awt.Container;
import java.rmi.*;
import java.net.*;
/**
 *
 * @author Hong Yu
 */
public class TrainingApplet extends JApplet {

    /**
     * Initialization method that will be called after the applet is loaded
     * into the browser.
     */
    TrainingPanel tp;
    private String ip;
    public void init() {
        // TODO start asynchronous download of heavy resources
//        System.setSecurityManager (new RMISecurityManager() {
//            public void checkConnect (String host, int port) {}
//            public void checkConnect (String host, int port, Object context) {}
//          });
        tp = new TrainingPanel(this);
        try{
            int port;

            if(getDocumentBase().getPort()!=-1){
                    port = getDocumentBase().getPort();
            }else{
                    port = 80;
            }

            Socket socket = new Socket(getDocumentBase().getHost(), port);
            ip = socket.getLocalAddress().getHostAddress();

        }catch(Exception io){
            System.out.println(io.getMessage());
        }
        System.out.println(ip);
        tp.tc.ip = ip;
        
        try{
         javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
             public void run() {
                 createGUI();
              }
         });
        }catch(Exception e){
            e.printStackTrace();
        }
        


    }
    
    public void createGUI() {
        this.setSize(1100, 1000);
        this.setContentPane(tp);
        
    }
    // TODO overwrite start(), stop() and destroy() methods
}
