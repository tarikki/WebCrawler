package util;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by extradikke on 7-11-14.
 */
public class IOUtil {

    public static String getWD() {
        Pattern opSysPattern = Pattern.compile("\\w+");
        String fulPath = "";
        String path = Paths.get(".").toAbsolutePath().normalize().toString();
        String opSys = System.getProperty("os.name").toLowerCase();

        Matcher matcher = opSysPattern.matcher(opSys);
        if (matcher.find()) {

            opSys = matcher.group();
            if (opSys.equals("linux")) {
                System.out.println("linux");
                fulPath = path + "/";
            }
            if (opSys.equals("windows")) {
                fulPath = path + "\\";
            }
            System.out.println(fulPath);
        }
        return fulPath;
    }

    public static boolean checkForConfig(){
        String path = getWD();
        File file = new File(path + "config.txt");
        boolean fileExists = file.exists();
        if (fileExists){
            System.out.println("yay");
        } else{
            System.out.println("nay");
        }

return fileExists;
    }

    public static void createConfig(){

    }


}
