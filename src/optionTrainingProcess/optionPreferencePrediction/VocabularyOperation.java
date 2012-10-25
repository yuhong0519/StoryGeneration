/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package optionTrainingProcess.optionPreferencePrediction;
import java.util.*;
import prefix.*;
import tools.*;

/**
 *
 * @author Bunnih
 */
public class VocabularyOperation {
    private static ArrayList<String> stopWords = null;
    private static String stopWordFile = "StopWords.txt";
    private static String vocabularyFile = "vocabulary.txt";
    
    public static String processWord(String iword){
        StringBuilder ret = new StringBuilder();
        String word = iword.toLowerCase();
        if(stopWords == null){
            stopWords = CommonUtil.readStringList(stopWordFile);
        }
        for(int i = 0; i < word.length(); i++){
            char c = word.charAt(i);
            if(c=='\"' || c==';'){
                continue;
            }
            if(c=='.' || c=='\'' || c==',' || c=='(' || c==')'){
                if(i != word.length()-1){
//                    System.out.println("Error process: " + word);
                    continue;
                }
                i++;
                continue;
            }
            ret.append(c);
                        
        }
        String rets = ret.toString();
        for(int i = 0; i < stopWords.size(); i++){
            if(rets.compareTo(stopWords.get(i)) == 0){
                return null;
            }
        }
        return rets;
    }
    
    private ArrayList<String> processLine(String optionString){
        String option = optionString.trim().toLowerCase();
        String[] words = option.split(" ");
        ArrayList<String> ret = new ArrayList<String>();
        
        for(int i = 0; i < words.length; i++){
            String tw = processWord(words[i]);
            if(tw != null){
                ret.add(tw);
            }
        }
        return ret;
        
    }
    
    void processVocabularyList(ArrayList<String> vocabulary){
        for(int i = 0; i < vocabulary.size()-1; i++){
            String word1 = vocabulary.get(i);
            for(int j = i+1; j < vocabulary.size(); j++){
                String word2 = vocabulary.get(j);
                if(word2.indexOf(word1) == 0 && word1.length()==word2.length()-1 && word2.charAt(word2.length()-1)=='s'){
                    vocabulary.remove(j);
                }
                if(word2.indexOf(word1) == 0 && word1.length()==word2.length()-3 && word2.substring(word2.length()-3).compareTo("ing") == 0){
                    vocabulary.remove(j);
                }
            }
            
        }
        
    }
    
    void startBuildVocabulary(String fileName){
        ArrayList<String> vocabulary = new ArrayList<String>();
        AllOptions ao = PrefixUtil.readOptions(fileName);
        ArrayList<PPOptions> allOptions = ao.getAllPPOptions();
        for(int i = 0; i < allOptions.size(); i++){
            PPOptions ppo = allOptions.get(i);
            ArrayList<OptionItem> ois = ppo.getAllOptions();
            for(int j = 0; j < ois.size(); j++){
                OptionItem oi = ois.get(j);
                String optionString = oi.getValue();
                ArrayList<String> linewords = processLine(optionString);
                for(int k = 0; k < linewords.size(); k++){
                    String word = linewords.get(k);
                    if(Collections.binarySearch(vocabulary, word) < 0){
                        vocabulary.add(word);
                        Collections.sort(vocabulary);
                    }
                }
                
            }
        }
        processVocabularyList(vocabulary);
        CommonUtil.printStringList(vocabulary, vocabularyFile);
    }
    static Vocabulary v = null;
    public static Vocabulary getVocabulary(){
        if(v == null){
            ArrayList<String> vl = CommonUtil.readStringList(vocabularyFile);
            v = new Vocabulary(vl);
        }
        return v;
    }
    
    public static void main(String[] args){
        VocabularyOperation bv = new VocabularyOperation();
        bv.startBuildVocabulary(PrefixUtil.optionFile);
    }
}
