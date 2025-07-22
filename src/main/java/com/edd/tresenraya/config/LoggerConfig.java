package com.edd.tresenraya.config;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LoggerConfig {

    public static void configure() {
        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.INFO);

        for (var handler : logger.getHandlers()) {
            handler.setLevel(Level.INFO);
            handler.setFormatter(new CustomFormatter());
        }
    }

    static class CustomFormatter extends Formatter {
        private static final String BLUE = "\u001B[34m";
        private static final String RED = "\u001B[31m";
        private static final String RESET = "\u001B[0m";

        @Override
        public String format(LogRecord record) {
            String color = record.getLevel() == Level.SEVERE || record.getLevel() == Level.WARNING ? RED : BLUE;
            String level = record.getLevel().getName();
            String timestamp = new java.text.SimpleDateFormat("HH:mm:ss")
                    .format(new java.util.Date(record.getMillis()));
            String origin = record.getLoggerName();
            String method = record.getSourceMethodName();
            String message = formatMessage(record);
            return String.format("%s[%s] %s: %s.%s: %s%s%n", color, level, timestamp, origin, method, message, RESET);
        }
    }
}