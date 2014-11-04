package testers;

import util.URLUtil;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by extradikke on 4-11-14.
 */
public class ContentTester {

    public static void main(String[] args) {
        try {
            ArrayList<String> result = URLUtil.getAnchors("http://hs.fi");
            for (String url : result) {
                URLUtil.stripURL(url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
