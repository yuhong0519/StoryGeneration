/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TrainServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

/**
 *
 * @author Bunnih
 */
public class TCPRequestProcessor  implements Runnable{
    Socket skt;
    InteractionProcessor itp = InteractionProcessor.getInstance();
    public TCPRequestProcessor(Socket skt){
        this.skt = skt;
    }
    
    public void run(){
        try{
                ObjectOutputStream out = new ObjectOutputStream(skt.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(skt.getInputStream());
                System.out.println("Connected!");
                String line = (String)in.readObject();
                System.out.println("Server receive: " + line);
                ServerResponse sr = itp.processMessage(line);
                out.writeObject(sr.toJason().toString()); 
                System.out.println("Server send: " + sr.toJason().toString());
                
                out.close();
                in.close();
                skt.close();
        }
        catch(Exception e){
           e.printStackTrace();
        }
    }
}
