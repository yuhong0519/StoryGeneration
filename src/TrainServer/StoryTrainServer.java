/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TrainServer;

import java.util.ArrayList;
import java.io.*;
import java.net.*;

/**
 *
 * @author Bunnih
 */
public class StoryTrainServer implements Runnable{
    InteractionProcessor itp = InteractionProcessor.getInstance();
    private boolean serverFlag = true;
    private int port;
    private ServerSocket srvr = null;
    public StoryTrainServer(int port){
        this.port = port;
    }
    public void run(){
        while(serverFlag){
            System.out.print("Story Server has started!\n");
            PrintWriter out = null;
            BufferedReader in = null;
            Socket skt = null;
             try {
                
                srvr = new ServerSocket(port);
//                srvr.setSoTimeout(10000);
                skt = srvr.accept();
                
                in = new BufferedReader(new InputStreamReader((skt.getInputStream())));
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = in.readLine())!=null){
                    sb.append(line);
                }
                itp.processMessage(sb.toString());
                in.close();
                
                
                out = new PrintWriter(skt.getOutputStream(), true);
                out.close();
                
                skt.close();
             }
             catch(Exception e) {
                e.printStackTrace();
                try{
                    in.close();
                    skt.close();
                    srvr.close();
                 }
                 catch(IOException ee){
                     ee.printStackTrace();
                 }
             }
             finally{
             }
        }
        System.out.println("Story Server has stopped!");
    }
    
    public void stopServer(){
        serverFlag = false;
        if(srvr != null){
            try{
                srvr.close();
            }            
            catch(IOException e){
                e.printStackTrace();
            }
            
        }
    }
    
    public static void main(String[] args){
        int port = 10000;
        StoryTrainServer sts = new StoryTrainServer(port);
        Thread story = new Thread(sts);
        story.start();
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String line = br.readLine();
            while(true){
                if(line.compareTo("stop") == 0){
                   sts.stopServer();
                   break;
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    
                
    }
    
}
