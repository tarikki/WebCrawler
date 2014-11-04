package testers;

import util.URLUtil;

/**
 * Created by extradikke on 4-11-14.
 */
public class URLUtilTesters {

    public static void main(String[] args) {
        URLUtil.stripURL("https://github.com/tarikki/WebCrawler");
        System.out.println();
        URLUtil.stripURL("http://infolab.stanford.edu/~ullman/mmds/ch9.pdf");
        URLUtil.stripURL("http://www.iltalehti.fi/iltatytto/201441_it.shtml");

    }
}
