package net.wouterb.blunthornapi.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.wouterb.blunthornapi.BlunthornAPI.MOD_ID;

public class ClientServerLogger {
    public static final Logger LOGGER_CLIENT = LoggerFactory.getLogger(MOD_ID + "_CLIENT");
    public static final Logger LOGGER_SERVER = LoggerFactory.getLogger(MOD_ID + "_SERVER");

    public static void info(String msg, boolean isClient) {
        if (isClient)
            LOGGER_CLIENT.info(msg);
        else
            LOGGER_SERVER.info(msg);
    }

    public static void warn(String msg, boolean isClient) {
        if (isClient)
            LOGGER_CLIENT.warn(msg);
        else
            LOGGER_SERVER.warn(msg);
    }

    public static void error(String msg, boolean isClient) {
        if (isClient)
            LOGGER_CLIENT.error(msg);
        else
            LOGGER_SERVER.error(msg);
    }
}
