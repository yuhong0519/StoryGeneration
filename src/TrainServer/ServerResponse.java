/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TrainServer;

/**
 *
 * @author Bunnih
 */
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServerResponse {
       public boolean ratePlot = true;
       public boolean rateOptions = true;
       public int maxPreference = 5;
       public int minPreference = 1;
       public int userId = 0;
       public String plot = null;
       public String[] options = new String[0];
       public int oldPlotPref = 0;
       public int[] oldOptionsPref = new int[0];
//       last plot point of the entire user session
       public boolean last = false;
//       clear screen
       public boolean clear = false;
       public boolean timeoutEnabled = false;
       public int timeout = 0;
       
       public ServerResponse(){
           
       }
       public ServerResponse(JSONObject jso){
            try{
                ratePlot = jso.getBoolean("ratePlot");
            }
            catch(JSONException e){
                System.err.println("Cannot interprete ratePlot");
                e.printStackTrace();
            }
            try{
                rateOptions = jso.getBoolean("rateOptions");
            }
            catch(JSONException e){
                System.err.println("Cannot interprete rateOptions");
                e.printStackTrace();
            }
                        
            try{
                maxPreference = jso.getInt("maxPreference");
            }
            catch(JSONException e){
                System.err.println("Cannot interprete player maxPreference");
                e.printStackTrace();
            }
            try{
                minPreference = jso.getInt("minPreference");
            }
            catch(JSONException e){
                System.err.println("Cannot interprete minPreference");
                e.printStackTrace();
            }
            try{
                userId = jso.getInt("userId");
            }
            catch(JSONException e){
                System.err.println("Cannot interprete userId");
                e.printStackTrace();
            } 
            try{
                oldPlotPref = jso.getInt("oldPlotPref");
            }
            catch(JSONException e){
                System.err.println("Cannot interprete oldPlotPref");
                e.printStackTrace();
            }            
            try{
                plot = jso.getString("plot");
            }
            catch(JSONException e){
                System.err.println("Cannot interprete plot");
                e.printStackTrace();
            }      
            try{
                JSONArray ja = jso.getJSONArray("options");
                options = new String[ja.length()];
                for(int i = 0; i < ja.length(); i++){
                    options[i] = ja.getString(i);
                }                
            }
            catch(JSONException e){
                System.err.println("Cannot interprete options");
                e.printStackTrace();
            }
            
            try{
                JSONArray ja = jso.getJSONArray("oldOptionsPref");
                oldOptionsPref = new int[ja.length()];
                for(int i = 0; i < ja.length(); i++){
                    oldOptionsPref[i] = ja.getInt(i);
                }                
            }
            catch(JSONException e){
                System.err.println("Cannot interprete oldOptionsPref");
                e.printStackTrace();
            }
            
            try{
                last = jso.getBoolean("last");
            }
            catch(JSONException e){
                System.err.println("Cannot interprete last");
                e.printStackTrace();
            }
            try{
                clear = jso.getBoolean("clear");
            }
            catch(JSONException e){
                System.err.println("Cannot interprete clear");
                e.printStackTrace();
            }
            try{
                timeoutEnabled = jso.getBoolean("timeoutEnabled");
            }
            catch(JSONException e){
                System.err.println("Cannot interprete timeoutEnabled");
                e.printStackTrace();
            }
            try{
                timeout = jso.getInt("timeout");
            }
            catch(JSONException e){
                System.err.println("Cannot interprete timeout");
                e.printStackTrace();
            }
       }
       
       
       public JSONObject toJason(){
           JSONObject jo = new JSONObject();
           try{
                jo.put("ratePlot", ratePlot);
                jo.put("rateOptions", rateOptions);
                jo.put("maxPreference", maxPreference);
                jo.put("minPreference", minPreference);
                jo.put("userId", userId);
                jo.put("plot", plot);

                if(options != null){
                    JSONArray ja = new JSONArray();
                    for(String s : options){
                        ja.put(s);
                    }
                    jo.put("options", ja);
               }
                jo.put("oldPlotPref", oldPlotPref);
                if(oldOptionsPref != null){
                    JSONArray ja = new JSONArray();
                    for(int s : oldOptionsPref){
                        ja.put(s);
                    }
                    jo.put("oldOptionsPref", ja);
                }
                jo.put("last", last);
                jo.put("clear", clear);
                jo.put("timeoutEnabled", timeoutEnabled);
                jo.put("timeout", timeout);

           }
           catch(JSONException e){
               e.printStackTrace();
           }
           return jo;
       }
}
