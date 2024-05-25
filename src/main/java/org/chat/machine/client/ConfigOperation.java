package org.chat.machine.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.util.properties.ReadPro;
import org.util.properties.SetPro;

public class ConfigOperation {
    protected static final Logger log = LogManager.getLogger(ConfigOperation.class);

    public static String ReadConfig(String item) {
        String value = ReadPro.ReadPro(".\\config.properties", item);
        log.info("ReadConfig({}): {}", item, value);
        return value;
    }

    public static String ReadSet(String item) {
        String value = ReadPro.ReadPro(".\\set.properties", item);
        log.info("ReadSet({}): {}", item, value);
        return value;
    }

    public static void SetSet(String item, String text) {
        log.info("SetSet({}): {}", item, text);
        SetPro.SetPro(".\\set.properties", item, text, "CMC");
    }

    public static String ReadRecord(String item) {
        String value = ReadPro.ReadPro(".\\record.properties", item);
        log.info("ReadRecord({}): {}", item, value);
        return value;
    }

    public static void SetRecord(String item, String text) {
        log.info("SetRecord({}): {}", item, text);
        SetPro.SetPro(".\\record.properties", item, text, "CMC");
    }
}
