package org.chat.machine.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class close {
    private static final Logger log = LogManager.getLogger(close.class);

    public static void main() {
        log.info("close...");
        RequestServer.send("close:");
        System.exit(0);
    }
}
