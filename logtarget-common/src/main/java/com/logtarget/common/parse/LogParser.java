package com.logtarget.common.parse;

import java.io.InputStream;

/**
 * dummy class for simulating log parsing
 */
public class LogParser {

    public LogParser(InputStream inputStream) {
    }

    /**
     * dummy method for producing a LogSummary
     * @return
     */
    public LogSummary getSummary() {
        return new LogSummary(93, 40);
    }
}
