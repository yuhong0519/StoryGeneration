/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clienttest;

import TrainServer.PlayerResponse;
import TrainServer.ServerResponse;
import java.io.*;
import java.util.Arrays;

/**
 *
 * @author Bunnih
 */
public class UndumPlayer {
    private int playerID;
    private PlayerResponse pr = null;
    public UndumPlayer(int playerID){
        this.playerID = playerID;
        startProcess();
    }
    private void startProcess(){
        pr = new PlayerResponse(playerID);
//        return pr;
    }
    
    public PlayerResponse getResponse(){
        return pr;
    }
    
    public void next(ServerResponse sr){
        System.out.println("UserID: " + sr.userId);
        System.out.println(sr.plot);
        for(int i = 0; i < sr.options.length; i++){
            System.out.println("Options: " + i + ": " + sr.options[i]);
        
        }
        System.out.println("Clear: " + sr.clear);
        System.out.println("Last: " + sr.last);
        int sel = -1;
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String tp = br.readLine();
            sel = Integer.parseInt(tp);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        pr = new PlayerResponse(playerID);
        pr.choice = sel;
        pr.userId = playerID;
        pr.plotRating = 3;
        pr.optionRatings = new int[sr.options.length];
        Arrays.fill(pr.optionRatings, 3);
        
    }
}
