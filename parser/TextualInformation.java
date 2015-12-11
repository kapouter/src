/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;
import model.DocumentMot;
import model.Mots;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author MC
 */
public class TextualInformation {

    // espaces enlevés aussi
    private static final Pattern PUNCTUATION = Pattern.compile("[\\]\\[(){} ,.;\\-:!?&@<>|'%€–/[0-9]©]");

    private ArrayList<ArrayList<String>> CorpusWords;

    public TextualInformation() {
        this.CorpusWords = new ArrayList<>();
    }

    public ArrayList<ArrayList<String>> generateCorpusWords(ArrayList<Document> corpus) {
        for (Document document : corpus) {
            this.CorpusWords.add(generateWords(document));
        }
        return this.CorpusWords;
    }
    
    public void insertCorpusWordsInDB(Mots motsDB, ArrayList<ArrayList<String>> corpusWords) {     
        for (int i = 0; i < corpusWords.size(); i++) {
            ArrayList<String> documentWords = corpusWords.get(i);
            for (String words : documentWords) {
                motsDB.insertIfNotPresent(words);
            }
        }
    }

    public ArrayList<ArrayList<String>> cleanCorpusWords(HashMap<String, String> emptyWords, ArrayList<ArrayList<String>> corpusWords) {
        for (ArrayList<String> documentWords : corpusWords) {
            minimize(documentWords);
            removeEmptyWords(emptyWords, documentWords);
            truncate7(documentWords);
        }
        return corpusWords;
    }

    public ArrayList<String> splitWords(Element e) {
        ArrayList<String> wordsList = new ArrayList();
        String aux = "";
        if (e.nodeName().equals("meta")) {
            aux = e.attr("content");
            String auxTab[] = aux.split(PUNCTUATION.toString());
            for (String w : auxTab) {
                if (w.length() > 0) {
                    wordsList.add(w);
                }
            }
        }
        else {
            aux = e.text();
            String auxTab[] = aux.split(PUNCTUATION.toString());
            for (String w : auxTab) {
                if (w.length() > 0) {
                    wordsList.add(w);
                }
            }
        }
        return wordsList;
    }

    public ArrayList<String> generateWords(Document doc) {
        ArrayList<String> wordsList = new ArrayList();

        Element head = doc.head();
        Elements elementsHead = head.getAllElements();
        for (Element e : elementsHead) {
            wordsList.addAll(splitWords(e)); 
        }
        Element body = doc.body();
        Elements elementsBody = body.getAllElements();
        for (Element e : elementsBody) {
            wordsList.addAll(splitWords(e));
        }
        return wordsList;
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
