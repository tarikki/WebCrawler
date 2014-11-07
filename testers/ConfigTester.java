package testers;

import util.IOUtil;

/**
 * Created by extradikke on 7-11-14.
 */
public class ConfigTester {
    public static void main(String[] args) {
        IOUtil config= new IOUtil();
        System.out.println("Path: "+config.getDEFAULT_PATH());
        System.out.println("Start: "+config.getDEFAULT_START());
        System.out.println("Threads: "+config.getMAX_THREADS());
        System.out.println("Workathand: "+ config.getWORK_AT_HAND_FILENAME());
    }
}
