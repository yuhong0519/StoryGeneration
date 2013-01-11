/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TrainServer;

/**
 *
 * @author Bunnih
 */
import org.json.*;


public class PlayerResponse {
      public int userId;
      public int choice; // the index of the options array
      public int plotRating;
      public int[] optionRatings;
      public PlayerResponse(JSONObject jso){
            try{
                userId = jso.getInt("userId");
                choice = jso.getInt("choice");
                plotRating = jso.getInt("plotRating");
                JSONArray ja = jso.getJSONArray("optionRatings");
                optionRatings = new int[ja.length()];
                for(int i = 0; i < ja.length(); i++){
                    optionRatings[i] = ja.getInt(i);
                }
                
            }
            catch(JSONException e){
                System.err.println("Cannot interprete JSON message of player response");
                e.printStackTrace();
            }
      }
      
    
}
