package com.logtarget.common;

import org.junit.Test;

import java.util.UUID;
import java.util.regex.Matcher;

import static org.junit.Assert.*;

public class LogtargetConstantsTest {

    @Test
    public void testRawLogPattern() {
        final String fileId = UUID.randomUUID().toString();
        final String key = String.format(LogtargetConstants.FORMAT_LOG_RAW, fileId);
        final Matcher matcher = LogtargetConstants.LOG_RAW.matcher(key);

        assertTrue(matcher.matches());
        final String captureGroup = matcher.group(1);
        assertTrue(fileId.equals(captureGroup));
    }

}