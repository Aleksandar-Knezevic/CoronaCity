package org.unibl.etf.pj2.projekat.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger
{
    private static Logger logger;

    public static void setup() throws IOException
    {
        logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        logger.setLevel(Level.INFO);
        FileHandler logFile = new FileHandler("logging.txt", true);

        SimpleFormatter formatter = new SimpleFormatter();
        logFile.setFormatter(formatter);
        logger.addHandler(logFile);
    }

    public static void log(Level level,String poruka, Exception e)
    {
        logger.log(level, poruka, e);
    }
}
