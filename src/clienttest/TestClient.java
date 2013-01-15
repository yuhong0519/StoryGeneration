/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clienttest;

import TrainServer.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.Random;
import org.json.JSONObject;

/**
 *
 * @author Bunnih
 */
public class TestClient implements Runnable{
    UndumPlayer up;
    private boolean notEnd = true;
    private int port;
//    private InetAddress ia ;
    private Socket skt;
    
    public TestClient(int port){
        this.port = port;
        Random r = new Random();
        up = new UndumPlayer(r.nextInt(10000));

    }
    public void run(){
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

                    
        while(notEnd){
            try {
                skt = new Socket("localhost", port);
                in = new ObjectInputStream(skt.getInputStream());
                out = new ObjectOutputStream(skt.getOutputStream());
                System.out.println("Player Send: " + up.getResponse().toJason().toString());
                out.writeObject(up.getResponse().toJason().toString());

                String line = (String)in.readObject();
                System.out.println("player receive: " + line);
                ServerResponse sr = new ServerResponse(new JSONObject(line));
                if(sr.last){
                    notEnd = false;
                }
//                skt = new Socket("localhost", port);
                up.next(sr);
//                PlayerResponse pr = up.getResponse();
//                out = new ObjectOutputStream(skt.getOutputStream());
//                out.writeObject(pr.toJason().toString());
                out.close();
                in.close();
                skt.close();
             }
             catch(Exception e) {
                e.printStackTrace();
             }

            
        }
        System.out.println("Player has stopped!");
    }
    
    public void stopServer(){
        notEnd = false;
        if(skt != null){
            try{
                skt.close();
            }            
            catch(IOException e){
                e.printStackTrace();
            }
            
        }
    }
    
    public static void main(String[] args){
        int port = 10000;
        TestClient sts = new TestClient(port);
//        Thread story = new Thread(sts);
//        story.start();
//        try{
//            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//            String line = br.readLine();
//            while(true){
//                if(line.compareTo("stop") == 0){
//                   sts.stopServer();
//                   break;
//                }
//            }
//        }
//        catch(IOException e){
//            e.printStackTrace();
//        }
        sts.run();
                
    }
        
}
