package com.core_engine.console;

import freemarker.log.Logger;

public class Logging {

    static Logger logger = Logger.getLogger("e2e-api-test");

    public static void info(String console) {
        logger.info(console);
    }

    public static void debug(String console) {
        logger.debug(console);
    }

    public static void error(String console) {
        logger.error(console);
    }
}
