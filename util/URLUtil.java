package util;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * An utility class used to process URL's.
 *
 * @author harald.drillenburg
 */

public class URLUtil {

//    private static Pattern pattern = Pattern.compile("(?<=\\.)\\w*$");
//    private static Pattern hrefPattern = Pattern.compile("(?<=<a href=\").*(?=\")");
//    private static Pattern hrefPattern = Pattern.compile("a href=\"(.*?)\"");
//    private static Pattern mailtoPattern = Pattern.compile("^mailto:");
    private static Pattern protocolPattern = Pattern.compile("://");


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
        String protocol = "";
        String host = "";
        String path = "";

        URL anURL = null;
        String result = null;

        // remove spaces
        name = name.replaceAll(" ", "");

        // if the name is empty, don't even bother checking the rest and just return null
        if (name.length() == 0) {
            return result;
        }


        // if there is no pattern to indicate the presence of a protocol, give it one
        if (!protocolPattern.matcher(name).find()) {
            name = "http://" + name;

        }

        //get the necessary info
        try {
            anURL = new URL(name);
            protocol = anURL.getProtocol();
            path = anURL.getPath();
            host = anURL.getHost();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        // let's ditch some protocols straight away and build the url from basics without all the unnecessary stuff
        if (!protocol.equals("mailto") && !protocol.equals("ftp") && !protocol.equals("")) {

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(protocol);
            stringBuilder.append("://");
            stringBuilder.append(host);
            stringBuilder.append(path);

            result = stringBuilder.toString();
        }
        return result;
    }

    /**
     * Checks whether a given URL is actually reachable (i.e. whether a connection can be established)
     * If an URL could not be reached, just return false, do not throw any Exception here
     *
     * @param anURL anURL the String representation of the URL to check for
     * @return boolean true if a connection could be established, false otherwise
     */


    /**
     * Checks whether a given URL is actually reachable (i.e. whether a connection can be established)
     * If an URL could not be reached, just return false, do not throw any Exception here
     *
     *  anURL anURL the String representation of the URL to check for
     * @return boolean true if a connection could be established, false otherwise
     */




    private static boolean isReachableJSoup(Connection connection, String url) {

        // assume all links are bad, then improve on that assumption
        int response = 400;
        // try connection and get possible status code
        try {
            response = connection.execute().statusCode();

            //handle exceptions
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println(url + " IS LE FUCKED");
            if (e.getClass().toString().equals("class java.net.SocketException")) {
                System.out.println("BitchServer on: " + url);
            }
            if (e.getClass().toString().equals("class org.jsoup.HttpStatusException")) {
                HttpStatusException exception = (HttpStatusException) e;

                System.out.println("Interestin!: " + exception.getStatusCode());
            }
            return false;
        }
        // anything between 200 and 399 is ok, else return false
        if (200 <= response && response <= 399)
        {
            return true;
        } else {
            System.out.println(url + " IS LE FUCKED! HTTP response " + response);
            return false;
        }


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

    public static ArrayList<String> getAnchors(String anURL, StatisticsCallback callback) throws IOException {

        ArrayList<String> result = new ArrayList<>();
        long bytes = 0;
        try {

            // connect to anURL
            Connection connection = Jsoup.connect(anURL)
                    // tell the page what kind of encoding we're using
                    .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
                    // give it a timeout
                    .timeout(10000)
                    // lie that we arrived here by googling, so the website might not find out that we're a bot
                    .referrer("http://www.google.com");

            // if reachable, scrape
            // we're able to use the same connection object for availability checks and scraping
            // very handy!
            if (isReachableJSoup(connection, anURL)) {
                Document document = connection.get();

                // memory usage is only an approximation, as this depends on the character encoding
                // and doesn't account for overhead
                // should sit straight on the stream on OS level to get an accurate reading
                // but writing that for this app would be overkill! :D
                bytes += document.toString().getBytes().length;

                // get all regular links
                // and also search inside frames (otherwise, for example, http://docs.oracle.com/javase/7/docs/api/ returns 0 links!)

                Elements elements = document.select("a[href]");
                elements.addAll(document.select("frame[src]"));

                // loop through elements and add absolute path (another thing that JSoup has a method for)
                for (Element element : elements) {
                    if (element.attr("abs:href") != null) result.add(element.attr("abs:href"));
                    if (element.attr("abs:src") != null) result.add(element.attr("abs:src"));
                }
            }
            // catch errors generated by attempts to pass videos, images, etc. ass parseable links
        } catch (UnsupportedMimeTypeException e) {
            System.out.println("Can't parse " + e.getMimeType());
        }

        // report memory usage and return all pages parsed
        callback.amountUsed(bytes);
        return result;
    }
}
