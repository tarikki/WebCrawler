package util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.*;

/**
 * An utility class used to process URL's.
 * 
 * @author harald.drillenburg
 *
 */

public class URLUtil {

//    private static Pattern pattern = Pattern.compile("(?<=\\.)\\w*$");

	/**
	 * This method is used to normalize a URL. In order to do this, the following steps are taken:
	 * - Strip all parameters from the URL
	 * - Strip signs like ; and # at the end of the URL
	 * - Strip all file extensions
	 * - Strip any remaing / sign at the end of the URL
	 * - Make sure the remaining URL does not start or end with a space
	 * 
	 * @param name the String representation of the URL
	 * @return String the stripped URL
	 */
	public static String stripURL(String name) {


//        Matcher match = pattern.matcher(name);
//        if (match.find()){
//            System.out.println("taddaaa "+match.group());
//            name = name.replace(("."+match.group()), "");
//        }
        URL aURL = null;
        try {
            aURL = new URL(name);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String protocol = aURL.getProtocol();

        String host = aURL.getHost();
        String path = aURL.getPath();


        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(protocol);
        stringBuilder.append("://");
        stringBuilder.append(host);
        stringBuilder.append(path);
        System.out.println(stringBuilder.toString());

        return null;
    }




	
	/**
	 * Checks whether a given URL is actually reachable (i.e. whether a connection can be established)
	 * If an URL could not be reached, just return false, do not throw any Exception here
	 * 
	 * @param anURL anURL the String representation of the URL to check for
	 * @return boolean true if a connection could be established, false otherwise
	 */
	
	public static boolean isReachableURL(String anURL) {
		return true;
	}
	
	/**
	 * This method connects to a given URL and retrieves the webpage behind it. It assumes an URL is 
	 * reachable; if not, it throws an Exception
	 * The method also keeps track of the amount of data retrieved using the StatisticsCallback interface
	 * 
	 * @param anURL anURL the String representation of the URL to be retrieved
	 * @param callback callback the object to tell how many bytes were retrieved
	 * @return String the retrieved webpage as one String
	 * @throws IOException if anything goes wrong retrieving the page
	 */
	
	public static String getURLContent(String anURL, StatisticsCallback callback) throws IOException {
		return null;
	}
	
	/**
	 * This method gets a webpage in the form of a String and retrieves all anchors in it.
	 * Remember, an anchor is of the form <a href= , and what counts is the URL in the quote 
	 * following. 
	 * If anything goes wrong reading the URL itself: see comments in the source code given
	 * 
	 * @param anURL anURL the String representation of the URL to be retrieved
	 * @param callback callback the object to tell how many bytes were retrieved
	 * @return A List containing the anchors found
	 */
	
	public static ArrayList<String> getAnchors(String anURL, StatisticsCallback callback) {
		// What if the stream itself could not be read. In the future, keep track of how fast this occurs.
		// It it happens too many times in a row, end the thread pool
		// For now, do nothing

		return null;
	}
}
