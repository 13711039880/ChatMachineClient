package org.chat.machine.client;

public class timing {
    private long StartTime;

    public timing() {
        StartTime = System.currentTimeMillis();
    }

    public long stop() {
        return System.currentTimeMillis() - StartTime;
    }
}
