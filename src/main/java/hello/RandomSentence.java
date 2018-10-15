package hello;

import java.io.IOException;
import java.util.Random;

public class RandomSentence {
	
	private BookUtility bookUtility;
	
	private SentenceUtility sentenceUtility;
	
	private String sentence;
	
	private String titleOfBookSentenceIsFrom;
	
	private String authorOfBookSentenceIsFrom;
	
	public RandomSentence(BookUtility bookUtility) throws IOException {
		
		this.bookUtility = bookUtility;
		sentenceUtility = new SentenceUtility();
		
		findRandomSentence(bookUtility.getRandomBook());
	}
	
    public void findRandomSentence(Book book) throws IOException {
    	String sentence = sentenceUtility.getARandomSentence(book);
    	this.sentence = sentenceUtility.cleanString(sentence);
    	this.authorOfBookSentenceIsFrom = book.getAuthor();
    	this.titleOfBookSentenceIsFrom = book.getTitle();
    }
    
    public String getSentence() {
    	return sentence;
    }

	public BookUtility getBookUtility() {
		return bookUtility;
	}

	public SentenceUtility getSentenceUtility() {
		return sentenceUtility;
	}

	public String getTitleOfBookSentenceIsFrom() {
		return titleOfBookSentenceIsFrom;
	}
	
	public String getAuthorOfBookSentenceIsFrom() {
		return authorOfBookSentenceIsFrom;
	}

}
