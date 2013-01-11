package TrainServer;

import java.util.ArrayList;
import java.util.Collections;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Bunnih
 */
public class InteractionProcessor {
    private InteractionList intl = new InteractionList();
    private static InteractionProcessor itp = null;
    private InteractionProcessor(){
        
    }
    
    public static InteractionProcessor getInstance(){
        if(itp == null){
            itp = new InteractionProcessor();
        }
        return itp;
    }
    
    private void processPlayerResponse(PlayerResponse pr){
        Interaction itn = intl.get(pr.userId);
        if(itn == null){
            itn = new Interaction(pr.userId);
            intl.add(itn);
        }
        itn.playerAction(intl, pr);
        
    }
    
    
    public void processMessage(String msg){
        try{
            JSONObject jso = new JSONObject(msg);
            PlayerResponse pr = new PlayerResponse(jso);
            processPlayerResponse(pr);
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }
}
