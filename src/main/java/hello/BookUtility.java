package hello;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class BookUtility {
	
	private static final int BOOK_COUNT = 142;
	
	private ArrayList<Book> books;
	
	public BookUtility() {
		try {
			books = createBooks();
		} catch (IOException e) {
			System.err.println("Failed to capture books on start up.");
			e.printStackTrace();
		}
	}
	
	public ArrayList<Book> createBooks() throws IOException {
		
		ArrayList<Book> books = new ArrayList<>();
		Class<GreetingController> clazz = GreetingController.class;
		InputStream inputStream;
		
		for (int i = 1; i < BOOK_COUNT + 1; i++)
		{
			inputStream = clazz.getResourceAsStream(i + ".txt");
			HashMap<String, String> bookInfo = readFromInputStream(inputStream, i);
			Book book = new Book(bookInfo.get("title"), bookInfo.get("author"), bookInfo.get("text"));
			books.add(book);
		}
		System.out.println("!!!!!! --    Books Captured Count    -- !!!!!!   " + books.size());
		return books;
	}
	
    private HashMap<String, String> readFromInputStream(InputStream inputStream, int bookNumber) throws IOException {
    	
	    StringBuilder title = new StringBuilder();
	    StringBuilder author = new StringBuilder();
	    StringBuilder text = new StringBuilder();
	    
	    HashMap<String, String> bookData = new HashMap<>();
	    try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
	        String line;
	        int i = 0;
	        while ((line = br.readLine()) != null) {
	        	if (i < 50) {
		        	if (line.contains("Title:")) {
		        		line = line.replace("�", "");
		        		title.append(line).append(" \n");
		        	}
		        	else if (line.contains("Author:")) {
		        		line = line.replace("�", "");
		        		author.append(line).append(" \n");
		        	}
	        	}
	        	else {
	        		text.append(line).append(" \n");	
	        	}
	        	i++;
	        }
	        
	        bookData.put("title", title.substring(title.indexOf(":") + 2));
	        bookData.put("text", text.toString());
	        bookData.put("author", author.substring(author.indexOf(":") + 2));
	        
	    } catch (Exception e) {
	    	System.err.println("!!!!!!!!!!!!!! ---------- bufferedreader failed on book " + bookNumber + ".txt");
	    }
	    return bookData;
    }
    
    public Book getRandomBook() {
    	Random rn = new Random();
    	int randomBook = rn.nextInt(books.size()-1);
    	return books.get(randomBook);
    }
    
    public ArrayList<Book> getBooks() {
    	return books;
    }

}
