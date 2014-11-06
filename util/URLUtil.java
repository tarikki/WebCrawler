package util;

import org.jsoup.Connection;
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
        String host;
        String path;
        URL anURL = null;
        String result = null;
        if (!protocolPattern.matcher(name).find()) {
            name = "http://" + name;
            System.out.println("No Protocol!");
        }
        try {
//            System.out.println(name.length());
            anURL = new URL(name);
            protocol = anURL.getProtocol();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        if (!protocol.equals("mailto") && !protocol.equals("ftp") && !protocol.equals("")) { //if not mailto, return address. Otherwise keep the result as null

            host = anURL.getHost();
            path = anURL.getPath();

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(protocol);
            stringBuilder.append("://");
            stringBuilder.append(host);
            stringBuilder.append(path);
//        System.out.println(stringBuilder);
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
     * @param anURL anURL the String representation of the URL to check for
     * @return boolean true if a connection could be established, false otherwise
     */


    public static boolean isReachableURL(String anURL) throws IOException {
        //System.out.println(anURL);

        if (anURL == null) return false; // if no url, don't bother checking

        int response = 400;
        try {
//            anURL = anURL.replace("https", "http");
            HttpURLConnection connection = (HttpURLConnection) new URL(anURL).openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000); //// Set timeout to 5s (may be too long?)
            response = connection.getResponseCode();
//            System.out.println(response);
        } catch (IOException | ClassCastException e) {
//            e.printStackTrace();
//            System.out.println(e.getClass().toString());
            System.out.println("This did not work: " + anURL);
            if (e.getClass().toString().equals("class java.net.SocketException")) {
                System.out.println("BitchServer on: " + anURL);
            }


        }
        if (200 <= response && response <= 399) /// If response code is other than 200 <= response <= 399, website is unreachable
        {
            return true;
        } else {
            System.out.println(anURL + " IS LE FUCKED! HTTP response " + response);
            return false;
        }
    }

    public static boolean isReachableJSoup(Connection connection, String url) throws IOException {
        int response = connection.execute().statusCode();
        if (200 <= response && response <= 399) /// If response code is other than 200 <= response <= 399, website is unreachable
        {
            return true;
        } else {
            System.out.println(url + " IS LE FUCKED! HTTP response " + response);
            return false;
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
//        int responseCode = con.getResponseCode();
//        System.out.println("Response " + responseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
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

    public static ArrayList<String> getAnchors(String anURL, StatisticsCallback callback) throws IOException {
        //TODO put the statistic callback back in the this method
        ArrayList<String> result = new ArrayList<>();
        long bytes = 0;
        try {
//        System.out.println(anURL);
            Connection connection = Jsoup.connect(anURL).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
                    .timeout(10000).referrer("http://www.google.com");

            if (isReachableJSoup(connection, anURL)) {
                Document document = connection.get();
                bytes += document.toString().getBytes().length;
//        Document document = Jsoup.parse(content, "http://www.regexplanet.com/");
                Elements elements = document.select("a[href]");
                System.out.println("Links on page: " + elements.size());
                for (Element element : elements) {
                    if (element.attr("abs:href") != null) result.add(element.attr("abs:href"));
//            System.out.println(element.attr("abs:href"));

                }
            }
        } catch (UnsupportedMimeTypeException e) {
            System.out.println("Can't parse " + e.getMimeType());
        }


        // What if the stream itself could not be read. In the future, keep track of how fast this occurs.
        // It it happens too many times in a row, end the thread pool
        // For now, do nothing
        callback.amountUsed(bytes);
        return result;
    }
}
