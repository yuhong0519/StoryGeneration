package TrainServer;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Bunnih
 */
public class InteractionList {
    ArrayList<Interaction> userList = new ArrayList<Interaction>();
    
    /**
     * Add new Interaction to the list
     * @param ni the new Interaction to add
     */
    public void add(Interaction ni){
        userList.add(ni);
        Collections.sort(userList);
        
    }
    
    public void remove(Interaction ni){
        userList.remove(ni);
    }
    
    /**
     * Get the index of the Interaction with the same ID as ni
     * @param ni The Interaction to look for
     * @return the index of the Interaction if found, -1 otherwise
     */
    public int getIndex(Interaction ni){
        return Collections.binarySearch(userList, ni);
    }
    
    /**
     * Get the index of the Interaction with UserID id
     * @param id The userID to look for
     * @return the index of the Interaction if found, -1 otherwise
     */
    public int getIndex(int id){
        int ind = -1;
        for(int i = 0; i < userList.size(); i++){
            if(userList.get(i).getUserID() == id){
                ind = i;
                break;
            }
        }
        return ind;
    }
    
    /**
     * Get the Interaction with UserID id
     * @param id 
     * @return the Interaction with UserID id. null if not found
     */
    public Interaction get(int id){
        int ind = getIndex(id);
        if(ind < 0){
            return null;
        }
        return userList.get(ind);
    }
    
}
