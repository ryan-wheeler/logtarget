package com.logtarget.common;

import java.util.regex.Pattern;

/**
 * LogtargetConstants holds constants for use accross the different modules of logtarget
 */
public class LogtargetConstants {
    public static final String PATTERN_LOG_RAW = "(.*)\\/log-raw"; // pattern to match key ending in "/log-raw"
    public static final Pattern LOG_RAW = Pattern.compile(PATTERN_LOG_RAW);
    public static final String FORMAT_LOG_RAW = "%s/log-raw"; // format for raw log files - "/{log-file-id}/log-raw"
    public static final String FORMAT_LOG_SUMMARY =
            "%s/log-summary"; // format for log summary - "/{log-file-id}/log-summary"
}
