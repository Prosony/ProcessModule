package debug;

import services.config.GetConfig;

import java.io.BufferedReader;
import java.io.IOException;

public class LocalLog {

    private static LocalLog instance = new LocalLog();
    public static LocalLog getInstance(){
        return instance;
    }
    public LocalLog(){}

    private final String ANSI_RESET = "\u001B[0m";
    private final String ANSI_BLACK = "\u001B[30m";
    private final String ANSI_RED = "\u001B[31m";
    private final String ANSI_GREEN = "\u001B[32m";
    private final String ANSI_YELLOW = "\u001B[33m";
    private final String ANSI_BLUE = "\u001B[34m";
    private final String ANSI_PURPLE = "\u001B[35m";
    private final String ANSI_CYAN = "\u001B[36m";
    private final String ANSI_WHITE = "\u001B[37m";

    private GetConfig config = GetConfig.getInstance();

    public void info(String message){
        if (config.isDEBUG()){
            System.out.println(ANSI_YELLOW+"[INFO]"+ ANSI_RESET +" "+message);
        }

    }
    public void success(String message){
        if (config.isDEBUG()){
            System.out.println(ANSI_GREEN+"[SUCCESS]"+ ANSI_RESET +" "+message);
        }
    }
    public void error(String error){
        if (config.isDEBUG()){
            System.out.println(ANSI_RED+"[ERROR]"+ ANSI_RESET +" "+error);
        }
    }
    public void console(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            info(line);
        }

    }
}
