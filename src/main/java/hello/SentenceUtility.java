package hello;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import ca.rmen.rhymer.RhymeResult;
import ca.rmen.rhymer.Rhymer;
import ca.rmen.rhymer.cmu.CmuDictionary;

public class SentenceUtility
{
	
	public SentenceUtility() {
		
	}
	
    public String getARandomSentence(Book book) {
    	String bookText = book.getText();
    	String sentence = "";
    	
    	try {
			while (!checkIfSentenceMatchesCriteria(sentence, true)) {
				sentence = getASentence(bookText);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return sentence;
    }
    
    public List<String> getARhymingSentence(BookUtility bookUtility, ArrayList<String> wordsThatRhymeWithSentence) {
    	String sentence = "";
    	Book book = bookUtility.getRandomBook();
    	List<String> result = new ArrayList<>();    
    	
    	try {
			while (!checkIfSentenceMatchesCriteria(sentence, false) || !wordsThatRhymeWithSentence.contains(getLastWordOfSentence(sentence))) {
		    	book = bookUtility.getRandomBook();
		    	String bookText = book.getText();
				sentence = getASentence(bookText);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	result.add(sentence);
    	result.add(book.getTitle());
    	result.add(book.getAuthor());
    	
    	return result;
    }
    
    public String getASentence(String bookText) {
    	String sentence = "";
		int startingPoint = randomizeStartingPoint(bookText);
		int firstCharIndex = bookText.indexOf(".", startingPoint);
		
		int a = bookText.indexOf(".", firstCharIndex + 1);
		int b = bookText.indexOf(",", firstCharIndex + 1);
		int c = bookText.indexOf("!", firstCharIndex + 1);
		int d = bookText.indexOf("?", firstCharIndex + 1);
		
		// Trim the sentence to the next . or , or ! or ?
		if (a > 0 && b > 0 && c > 0 && d > 0) {
			sentence = bookText.substring(firstCharIndex + 1, Math.min(Math.min(a,b), Math.min(c, d)));
		}
		else {
			sentence = bookText.substring(firstCharIndex + 1, a);
		}
		return sentence;
    }
	
    public ArrayList<String> getRhymingWordsForSentence(String sentence) throws IOException {
    	String lastWordFromSentence = getLastWordOfSentence(sentence);
    	
    	Rhymer rhymer = CmuDictionary.loadRhymer();
		List<RhymeResult> results = rhymer.getRhymingWords(lastWordFromSentence);
		
		ArrayList<String> rhymingWords = new ArrayList<String>();
		for (RhymeResult result : results) {
			rhymingWords.addAll(Arrays.asList(result.strictRhymes));
			rhymingWords.addAll(Arrays.asList(result.oneSyllableRhymes));
			rhymingWords.addAll(Arrays.asList(result.twoSyllableRhymes));
			rhymingWords.addAll(Arrays.asList(result.threeSyllableRhymes));
		}
		
        return rhymingWords;
    }
    
    public String getLastWordOfSentence(String sentence) {
    	String lastWord = "";
    	if (sentence.contains(" ")) {
    		lastWord = sentence.substring(sentence.lastIndexOf(" ")+1);
    	}

    	return lastWord;
    }
    
    public boolean checkIfSentenceMatchesCriteria(String sentence, boolean isSentenceToBeRhymedOffOf) throws IOException {
    	if ((sentence.length() < 25 || sentence.length() > 50) ||
		sentence.contains("CHAPTER") || sentence.matches(".*\\d+.*")) {
    		return false;
    	} else if (!sentence.contains(" ") || sentence.matches(".*\\d+.*")) {
    		return false;
    	} else if (getLastWordOfSentence(sentence).equals("Mr") || getLastWordOfSentence(sentence).equals("Mrs")) {
    		return false;
    	}
    	
    	// For only the first sentence
    	if (isSentenceToBeRhymedOffOf) {
        	ArrayList<String> rhymingWords = getRhymingWordsForSentence(sentence);
        	if (rhymingWords.size() < 70) {
        		return false;
        	}
    	}
    	
    	return true;
    }
    
    public String cleanString(String sentence) {
    	sentence = sentence.replace("\n", "");
    	sentence = sentence.replace("\"", "");
    	sentence = sentence.replace("“", "");
    	sentence = sentence.replace("”", "");
    	sentence = sentence.replace("(", "");
    	sentence = sentence.replace(")", "");
    	sentence = sentence.replace("_", "");
    	sentence = sentence.replace("“", "\"");
    	sentence = sentence.replace("”", "\"");
    	sentence = sentence.replace("�", "");
    	
		if (!Character.isLetter(sentence.charAt(0))) {
			sentence = sentence.substring(1, sentence.length());
		}
    	return sentence;
    }
    
    private int randomizeStartingPoint(String book) {
    	Random rn = new Random();
    	int bookLength = book.length() - 500;
    	int range = bookLength;
    	int randomNum =  rn.nextInt(range) + 0;
    	return randomNum;
    }

}
