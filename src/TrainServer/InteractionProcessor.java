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
    
    private ServerResponse processPlayerResponse(PlayerResponse pr){
        Interaction itn = intl.get(pr.userId);
        if(itn == null){
            itn = new Interaction(pr.userId);
            intl.add(itn);
        }
        ServerResponse sr = itn.playerAction(intl, pr);
        return sr;
    }
    
    
    public ServerResponse processMessage(String msg){
        try{
            JSONObject jso = new JSONObject(msg);
            PlayerResponse pr = new PlayerResponse(jso);
            return processPlayerResponse(pr);
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        return null;
    }
}
