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
       boolean ratePlot = true;
       boolean rateOptions = true;
       int maxPreference = 5;
       int minPreference = 1;
       int userId = 0;
       String plot = null;
       String[] options = null;
       int oldPlotPref = 0;
       int[] oldOptionsPref = null;
//       last plot point of the entire user session
       boolean last = false;
//       clear screen
       boolean clear = false;
       boolean timeoutEnabled = false;
       int timeout = 0;
       
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
