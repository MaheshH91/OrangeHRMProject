package com.orangehrm.utilities;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class LoggerManager {

    // Standard way: Pass the class explicitly
    public static Logger getLogger(Class<?> cls) {
        return LogManager.getLogger(cls);
    }

    // "Lazy" way: Automatically gets the class name of the caller
    public static Logger getLogger() {
        String callingClassName = Thread.currentThread().getStackTrace()[2].getClassName();
        return LogManager.getLogger(callingClassName);
    }
}