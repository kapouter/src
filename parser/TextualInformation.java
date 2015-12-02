/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author MC
 */
public class TextualInformation {
    
    // espaces enlevés aussi
    private static final Pattern PUNCTUATION = Pattern.compile("[\\]\\[(){} ,.;:\\-!?<>%]");

    public ArrayList<String> generateWords(Document doc) {
        ArrayList<String> textList = new ArrayList();
        String aux = "";
        Element head = doc.head();
        Elements elements = head.getAllElements();
        for (Element e : elements) {
            aux = e.text();
            String auxTab[] = aux.split(" ");
            for (String w : auxTab) {
                if (w.length() > 0) {
                    textList.add(w);
                }  
            }      
        }
        return textList;
    }

    public void removeEmptyWords(HashMap<String, String> emptyWords, ArrayList<String> wordsList) {
        for (int i = 0; i < wordsList.size(); i++) {
            if (emptyWords.get(wordsList.get(i)) != null) {
                wordsList.remove(i);
            }
        }
    }

    public void truncate7(ArrayList<String> wordsList) {
        for (int i = 0; i < wordsList.size(); i++) {
            if (wordsList.get(i).length() > 7) {
                String truncated = wordsList.get(i).substring(0, 7);
                wordsList.set(i, truncated);
            }
        }
    }

    public void minimize(ArrayList<String> wordsList) {
        for (int i = 0; i < wordsList.size(); i++) {
            String minimized = wordsList.get(i).toLowerCase();
            wordsList.set(i, minimized);
        }
    }
    
    public void removePunctuation(ArrayList<String> wordsList) {
        for (int i = 0; i < wordsList.size(); i++) {
            String punctuationless = PUNCTUATION.matcher(wordsList.get(i)).replaceAll("");
            if (punctuationless.length() > 0) {
                wordsList.set(i, punctuationless);
            } else {
                wordsList.remove(i);
            }
        }
    }

}
