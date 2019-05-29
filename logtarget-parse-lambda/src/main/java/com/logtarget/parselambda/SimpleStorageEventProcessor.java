package com.logtarget.parselambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.logtarget.common.LogtargetConstants;
import com.logtarget.common.parse.LogParser;
import com.logtarget.common.parse.LogSummary;

import java.util.regex.Matcher;

/**
 * SimpleStorageEventProcessor handles events triggered by files being written to S3
 * any file written to S3 with a suffix matching "log-raw" will trigger this event
 */
public class SimpleStorageEventProcessor implements RequestHandler<S3Event, String> {
    private static final AmazonS3 AMAZON_S3 = AmazonS3ClientBuilder.defaultClient();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String output = "Success!";

    /**
     * handleRequest checks that the file written to S3 is a log file, and parses the file
     * @param input
     * @param context
     * @return
     */
    @Override
    public String handleRequest(final S3Event input, final Context context) {

        try {
            final S3EventNotification.S3EventNotificationRecord record = input.getRecords().get(0);
            final String bucket = record.getS3().getBucket().getName();
            final String key = record.getS3().getObject().getKey();

            final Matcher rawLogMatcher = LogtargetConstants.LOG_RAW.matcher(key);

            // check that the key does end in "log-raw'
            if (!rawLogMatcher.matches()) {
                return null; // this event is not a raw log file being added - exit
            }

            final String fileId = rawLogMatcher.group(1);

            final S3Object logToParse = AMAZON_S3.getObject(bucket, key);
            final ObjectMetadata logMetadata = logToParse.getObjectMetadata();

            // retrieve useful information about the log from the metadata

            final LogParser logParser = new LogParser(logToParse.getObjectContent());
            final LogSummary summary = logParser.getSummary();
            final String summaryJson = OBJECT_MAPPER.writeValueAsString(summary);

            AMAZON_S3.putObject(
                    bucket,
                    String.format(LogtargetConstants.FORMAT_LOG_SUMMARY, fileId),
                    summaryJson
            );

            return null;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
