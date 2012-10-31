/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package optionTraining.optionPreferencePrediction;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Bunnih
 */
public class Vocabulary {
     private static ArrayList<String> stopWords = null;
     private ArrayList<String> vocabularyList = null;
     
     public Vocabulary(ArrayList<String> v){
         vocabularyList = v;
     }
     
     public ArrayList<String> getVocabuloryList(){
         return vocabularyList;
     }
     
     public int getWordIndex(String word){
         int index = -1; 
         String w = VocabularyOperation.processWord(word);
         if(w == null){
             return index;
         }
         index = Collections.binarySearch(vocabularyList, w);
         if(index < 0){
             String w2 = w+"s";
             index = Collections.binarySearch(vocabularyList, w2);
         }
         if(index < 0){
             String w2 = w+"ing";
             index = Collections.binarySearch(vocabularyList, w2);             
         }       
         
         return index;
     }
}
