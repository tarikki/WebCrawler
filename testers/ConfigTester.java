package testers;

import util.ConfigUtil;

/**
 * Created by extradikke on 7-11-14.
 */
public class ConfigTester {
    public static void main(String[] args) {
        ConfigUtil config= new ConfigUtil();
        System.out.println("Path: "+config.getDEFAULT_PATH());
        System.out.println("Start: "+config.getDEFAULT_START());
        System.out.println("Threads: "+config.getMAX_THREADS());
        System.out.println("Workathand: "+ config.getWORK_AT_HAND_FILENAME());
        config.setDEFAULT_START("http://stackoverflow.com/questions/1046809/string-contains-in-java");
        System.out.println(config.getDEFAULT_START());
    }
}
