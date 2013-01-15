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
      public int[] optionRatings = new int[0];
      public PlayerResponse(JSONObject jso){
            try{
                userId = jso.getInt("userId");
            }
            catch(JSONException e){
                System.err.println("Cannot interprete userId");
                e.printStackTrace();
            }
            try{
                choice = jso.getInt("choice");
            }
            catch(JSONException e){
                System.err.println("Cannot interprete player choice");
                e.printStackTrace();
            }
            try{
                plotRating = jso.getInt("plotRating");
            }
            catch(JSONException e){
                System.err.println("Cannot interprete plotRating");
                e.printStackTrace();
            }
            try{
                JSONArray ja = jso.getJSONArray("optionRatings");
                optionRatings = new int[ja.length()];
                for(int i = 0; i < ja.length(); i++){
                    optionRatings[i] = ja.getInt(i);
                }
                
            }
            catch(JSONException e){
                System.err.println("Cannot interprete option ratings");
                e.printStackTrace();
            }
      }
      public PlayerResponse(int id){
          userId = id;
          
      }
        
      public JSONObject toJason(){
           JSONObject jo = new JSONObject();
           try{
                jo.put("userId", userId);
                jo.put("choice", choice);
                jo.put("plotRating", plotRating);
                
                if(optionRatings == null){
                    optionRatings = new int[0];
                }
                JSONArray ja = new JSONArray();
                for(int s : optionRatings){
                    ja.put(s);
                }
                jo.put("optionRatings", optionRatings);               
           }
           catch(JSONException e){
               e.printStackTrace();
           }
           return jo;
       }
    
}
