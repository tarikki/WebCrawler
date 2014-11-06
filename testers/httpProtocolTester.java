package testers;

import util.URLUtil;

import java.io.IOException;

/**
 * Created by extradikke on 6-11-14.
 */
public class httpProtocolTester {

    public static void main(String[] args) {
        try {
            System.out.println(URLUtil.isReachableURL(URLUtil.stripURL("stackexchange.com/users/180005/danny-miller")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
