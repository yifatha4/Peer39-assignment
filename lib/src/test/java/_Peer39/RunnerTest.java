package _Peer39;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Unit tests for the Runner class.
 */
public class RunnerTest {

    /**
     * Set up the test environment by initializing the model with predefined categories.
     */
    @BeforeAll
    public static void setUp() {
        // Predefined categories and keywords
        List<Category> predefinedCategories = Arrays.asList(
            new Category("Star Wars", Arrays.asList("star war", "starwars", "starwar", "r2d2", "may the force be with you")),
            new Category("Basketball", Arrays.asList("basketball", "nba", "ncaa", "lebron james", "john stockton", "anthony davis"))
        );

        // Initialize the model with predefined categories
        Runner.initializeModel(predefinedCategories);
    }

    /**
     * Test if a Star Wars-related URL is correctly classified.
     * 
     * @throws IOException If an error occurs while fetching the web page content.
     */
	/*
	 * @Test public void testClassifyStarWarsPage() throws IOException { String url
	 * = "https://www.starwars.com/news/everything-we-know-about-the-mandalorian";
	 * String content = Runner.fetchPageContent(url); List<String>
	 * matchingCategories = Runner.classifyContent(content);
	 * 
	 * assertTrue(matchingCategories.contains("Star Wars")); }
	 */

    /**
     * Test if a basketball-related URL is correctly classified.
     * 
     * @throws IOException If an error occurs while fetching the web page content.
     */
	/*
	 * @Test public void testClassifyBasketballPage() throws IOException { String
	 * url = "https://www.espn.com/nba/"; String content =
	 * Runner.fetchPageContent(url); List<String> matchingCategories =
	 * Runner.classifyContent(content);
	 * 
	 * assertTrue(matchingCategories.contains("Basketball")); }
	 */

    /**
     * Test if a URL with no matching categories is correctly classified as empty.
     * 
     * @throws IOException If an error occurs while fetching the web page content.
     */
    @Test
    public void testNoMatchingCategory() throws IOException {
        String url = "https://www.wikipedia.org/";
        String content = Runner.fetchPageContent(url);
        List<String> matchingCategories = Runner.classifyContent(content);
        
        assertTrue(matchingCategories.isEmpty());
    }
}

