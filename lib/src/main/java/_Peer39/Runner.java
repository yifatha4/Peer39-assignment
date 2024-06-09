package _Peer39;
import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.*;

/**
 * Runner class for categorizing web pages based on keywords.
 */
public class Runner {

	 private static Map<String, Trie> categoryTries;
	
	 public static void main(String[] args) {
	        if (args.length < 2) {
	            System.err.println("Usage: java Runner <Category1,Category2,...> <URL1,URL2,...>");
	            System.exit(1);
	        }
	        args.toString().toLowerCase();
	        
	        // Parse categories from the first argument-- change to lowerCase
	        String[] categoryNames = args[0].toLowerCase().split(",");

	        // Parse URLs from the second argument
	        String[] urls = args[1].split(",");

	        // Predefined categories and keywords
	        List<Category> predefinedCategories = Arrays.asList(
	            new Category("Star Wars", Arrays.asList(
	                "star war", "starwars", "starwar", "r2d2", "may the force be with you"
	            )),
	            new Category("Basketball", Arrays.asList(
	                "basketball", "nba", "ncaa", "lebron james", "john stockton", "anthony davis"
	            ))
	        );

	        // Initialize the model with the specified categories
	        initializeModel(predefinedCategories);

	        // check if category that pass by user in the predefinedCategories categories
	        for(String category:categoryNames)
	        {
	        	
	        	if(categoryTries.containsKey(category))
	        	{
	        		// Process each URL
	                for (String url : urls) {
	                 searchInCategory(category,url);
	               }
	        	}	
	        }
	        
	    }

	    /**
	     * Initializes the model by creating a Trie for each category and their keywords.
	     *
	     * @param categories A list of Category objects containing category names and their respective keywords.
	     */
	 
	    public static void initializeModel(List<Category> categories) {
	        categoryTries = new HashMap<>();
	        for (Category category : categories) {
	            Trie.TrieBuilder trieBuilder = Trie.builder().ignoreCase();
	            for (String keyword : category.getKeywords()) {
	                trieBuilder.addKeyword(keyword);
	            }
	            // set all categories name in lowercase.
	            categoryTries.put(category.getName().toLowerCase(), trieBuilder.build());
	        }
	    }

	    /**
	     * Classifies the given content by finding matching categories based on their respective Tries.
	     *
	     * @param content The text content to be classified.
	     * @return A list of matching category names.
	     */
	    public static List<String> classifyContent(String content) {
	        Set<String> matchingCategories = new HashSet<>();
	        String lowerCaseContent = content.toLowerCase();
	        for (Map.Entry<String, Trie> entry : categoryTries.entrySet()) {
	            Collection<Emit> emits = entry.getValue().parseText(lowerCaseContent);
	            if (!emits.isEmpty()) {
	                matchingCategories.add(entry.getKey());
	            }
	        }
	        return new ArrayList<>(matchingCategories);
	    }

    /**
     * Fetches and cleans the content of the given URL.
     * 
     * @param url The URL of the web page to fetch content from.
     * @return The cleaned text content of the web page.
     * @throws IOException If an error occurs while fetching the web page content.
     */
    public static String fetchPageContent(String url) throws IOException {
        try {  // Connect to the URL and parse the HTML document
            Document doc = Jsoup.connect(url)
                    .timeout(10 * 1000)  // Set a timeout to handle slow responses
                    .get();

            // Remove elements that are not part of the main content
            Elements removableElements = doc.select("script, style, noscript, iframe, header, footer, nav, aside, form, button, input, textarea, select, option, label, svg, canvas, embed, object, applet, param, picture, source, track, audio, video, map, area, base, col, colgroup, frame, frameset, noframes, title, a[href]");
            removableElements.remove();

            // Extract the main content area based on common article tags
            Element mainContent = doc.select("article, main, .main-content, .article-content, [role=main]").first();
            if (mainContent == null) {
            	mainContent = doc.body();
            }

            // Get the cleaned text content of the web page
            String cleanedText = mainContent.text();

            // Remove URLs from the cleaned text
            cleanedText = removeUrls(cleanedText);

            return cleanedText;

            
        } catch (MalformedURLException e) {
        	return   url  +":"+ "Invalid URL format: " + e.getMessage();
        } catch (HttpStatusException e) {
            return url + ":"+ " HTTP error fetching URL: " + e.getStatusCode() + " " + e.getMessage();
        } catch (UnsupportedMimeTypeException e) {
            return url + ":"+"Unsupported MIME type: " + e.getMessage();
        } catch (SocketTimeoutException e) {
            return  url + ":"+  "Connection timed out: " + e.getMessage();
        } catch (IOException e) {
            return url + ":"+ "Failed to retrieve content:" + e.getMessage();
        } catch (Exception e) {
            return url + ":"+ "An unexpected error occurred:" + e.getMessage();
        }
    }
    private static String removeUrls(String text) {
        // Regular expression to match URLs
        String urlRegex = "(https?://\\S+\\s?)";
        Pattern pattern = Pattern.compile(urlRegex);
        Matcher matcher = pattern.matcher(text);

        // Remove all URLs from the text
        return matcher.replaceAll("");
    }

    public static void searchInCategory(String categoryName, String url) {
    	
    	Trie categoryTrie = categoryTries.get(categoryName);
        if (categoryTrie == null) {
        	System.err.println("Category not found: " + categoryName);
            return;
        }

        try {
            String pageContent = fetchPageContent(url);
            Collection<Emit> emits = categoryTrie.parseText(pageContent.toLowerCase());
            if (!emits.isEmpty()) {
            	System.out.println("---------------------------------------");
            	System.out.println("URL: " + url);
                System.out.println("Matches found in category '" + categoryName + "':");
                System.out.println("---------------------------------------");
               
				
				//for (Emit emit : emits) { System.out.println(" - " + emit.getKeyword()); }
				 
            } else {
                //System.out.println("URL: " + url);
                //System.out.println("No matches found in category '" + categoryName + "'.");
               
            }
        }
        catch (IOException e) {
        	
            System.err.println("Error fetching the web page content for URL " + url + ": " + e.getMessage());
          
        }
        
    }

    /**
     * The main method to run the program.
     * 
     * @param args Command-line arguments specifying the categories and the URL to process.
     */
    
}

/**
 * Category class to represent a category with a name and associated keywords.
 */
class Category {
    private final String name;
    private final List<String> keywords;

    /**
     * Constructor for the Category class.
     *
     * @param name     Name of the category.
     * @param keywords List of keywords associated with the category.
     */
    public Category(String name, List<String> keywords) {
        this.name = name;
        this.keywords = keywords;
    }

    /**
     * Gets the name of the category.
     *
     * @return Name of the category.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the list of keywords associated with the category.
     *
     * @return List of keywords.
     */
    public List<String> getKeywords() {
        return keywords;
    }
}

	
