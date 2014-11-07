package util;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by extradikke on 7-11-14.
 */
public class IOUtil {

    private String WORK_AT_HAND_FILENAME;
    private String VERTICES_FILENAME;
    private String EDGES_FILENAME;
    private String DEFAULT_START;
    private int MAX_THREADS;
    private String DEFAULT_PATH;
    private String WORKING_DIRECTORY;


    public IOUtil() {
        this.WORKING_DIRECTORY = getWD();
        try {
            createConfig();
            readConfig();

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.WORK_AT_HAND_FILENAME = DEFAULT_PATH + "/WorkAtHand.txt";
        this.VERTICES_FILENAME = DEFAULT_PATH+"Vertices.txt";
        this.EDGES_FILENAME = DEFAULT_PATH + "Edges.txt";


    }

    public String getWD() {
        Pattern opSysPattern = Pattern.compile("\\w+");
        String fulPath = "";
        String path = Paths.get(".").toAbsolutePath().normalize().toString();
        String opSys = System.getProperty("os.name").toLowerCase();

        Matcher matcher = opSysPattern.matcher(opSys);
        if (matcher.find()) {

            opSys = matcher.group();
            if (opSys.equals("linux")) {
//                System.out.println("linux");
                fulPath = path + "/";
            }
            if (opSys.equals("windows")) {
                fulPath = path + "\\";
            }
//            System.out.println(fulPath);
        }
        return fulPath;
    }

    public String osPathCorrection() {
        Pattern opSysPattern = Pattern.compile("\\w+");
        String opSys = System.getProperty("os.name").toLowerCase();
        Matcher matcher = opSysPattern.matcher(opSys);
        String result = "";
        if (matcher.find()) {

            opSys = matcher.group();
            if (opSys.equals("linux")) {
//                System.out.println("linux");
                result = "/";
            }
            if (opSys.equals("windows")) {
                result = "\\";
            }

        }
        return result;


    }

    public boolean checkForConfig() {
        String path = getWD();
        File file = new File(path + "config.txt");
        boolean fileExists = file.exists();
        if (fileExists) {
//            System.out.println("yay");
        } else {
//            System.out.println("nay");
        }

        return fileExists;
    }

    public void createConfig() throws FileNotFoundException, UnsupportedEncodingException {
        if (!checkForConfig()) {
            String path = getWD();
            File file = new File(path + "config.txt");
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            writer.println("MAX_THREADS: 50");
            writer.println("DEFAULT_START: http://www.reddit.com/");
            writer.println("DEFAULT_PATH: " + path + "data" + osPathCorrection());
            System.out.println("DEFAULT_PATH: " + path + "data" + osPathCorrection());
            writer.close();
        }

    }

    public void readConfig() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(WORKING_DIRECTORY+ "config.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("MAX_THREADS: ")) {
                MAX_THREADS = Integer.parseInt(line.replace("MAX_THREADS: ", ""));
            } else if (line.contains("DEFAULT_START: ")) {
                DEFAULT_START = line.replace("DEFAULT_START: ", "");

            } else if (line.contains("DEFAULT_PATH: ")) {
                DEFAULT_PATH = line.replace("DEFAULT_PATH: ", "");
            }


        }
        reader.close();

    }

    public String getWORK_AT_HAND_FILENAME() {
        return WORK_AT_HAND_FILENAME;
    }

    public String getVERTICES_FILENAME() {
        return VERTICES_FILENAME;
    }

    public String getEDGES_FILENAME() {
        return EDGES_FILENAME;
    }

    public String getDEFAULT_START() {
        return DEFAULT_START;
    }

    public int getMAX_THREADS() {
        return MAX_THREADS;
    }

    public String getDEFAULT_PATH() {
        return DEFAULT_PATH;
    }
}
