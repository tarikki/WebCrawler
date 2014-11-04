package testers;

import util.URLUtil;

/**
 * Created by extradikke on 4-11-14.
 */
public class URLUtilTesters {

    public static void main(String[] args) {
        URLUtil.stripURL("https://www.google.nl/?gfe_rd=cr&ei=8NpYVPjeEISP-waz6oGYAg&gws_rd=ssl");
        System.out.println();
        URLUtil.stripURL("http://infolab.stanford.edu/~ullman/mmds/ch9.pdf");
        URLUtil.stripURL("http://www.iltalehti.fi/iltatytto/201441_it.shtml");

    }
}
