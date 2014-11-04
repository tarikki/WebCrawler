package util;

import com.sun.xml.internal.bind.v2.TODO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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
//    private static Pattern hrefPattern = Pattern.compile("(?<=<a href=\").*(?=\")");
    private static Pattern hrefPattern = Pattern.compile("a href=\"(.*?)\"");

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
        System.out.println(stringBuilder);

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
     * @return String the retrieved webpage as one String
     * @throws IOException if anything goes wrong retrieving the page
     */

	
	/**
	 * Checks whether a given URL is actually reachable (i.e. whether a connection can be established)
	 * If an URL could not be reached, just return false, do not throw any Exception here
	 * 
	 * @param anURL anURL the String representation of the URL to check for
	 * @return boolean true if a connection could be established, false otherwise
	 */


	



    public static boolean isReachableURL(String anURL) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) new URL(anURL).openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(3000); //// Set timeout to 3s (may be too long?)
        if (connection.getResponseCode() != 200) /// If response code is other than 200 -> website is unreachable
        {

            System.out.println("IT IS LE FUCKED");
            return false;
        } else {
            System.out.println("IT WORKS!!!!");
            return true;

        }
    }


    /**
     * This method connects to a given URL and retrieves the webpage behind it. It assumes an URL is
     * reachable; if not, it throws an Exception
     * The method also keeps track of the amount of data retrieved using the StatisticsCallback interface
     *
     * @param anURL anURL the String representation of the URL to be retrieved
     * @return String the retrieved webpage as one String
     * @throws IOException if anything goes wrong retrieving the page
     */

    public static String getURLContent(String anURL) throws IOException {
        //TODO put the statistic callback back in the this method
        URL urlObj = new URL(anURL);
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        System.out.println("Response " + responseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
            response.append("\n");
        }
        in.close();
//        System.out.println(response.toString());


        return response.toString();
    }

    /**
     * This method gets a webpage in the form of a String and retrieves all anchors in it.
     * Remember, an anchor is of the form <a href= , and what counts is the URL in the quote
     * following.
     * If anything goes wrong reading the URL itself: see comments in the source code given
     *
     * @param anURL anURL the String representation of the URL to be retrieved
     *              //	 * @param callback callback the object to tell how many bytes were retrieved
     * @return A List containing the anchors found
     */

    public static ArrayList<String> getAnchors(String anURL) throws IOException {
        //TODO put the statistic callback back in the this method
        ArrayList<String> result = new ArrayList<String>();


        Document document = Jsoup.connect(anURL).get();
//        Document document = Jsoup.parse(content, "http://www.regexplanet.com/");
        Elements elements = document.select("a[href]");
        for (Element element : elements) {
            result.add(element.attr("abs:href"));
            System.out.println(element.attr("abs:href"));

        }


        // What if the stream itself could not be read. In the future, keep track of how fast this occurs.
        // It it happens too many times in a row, end the thread pool
        // For now, do nothing

        return result;
}
