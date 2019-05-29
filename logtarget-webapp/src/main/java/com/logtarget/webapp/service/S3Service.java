package com.logtarget.webapp.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.logtarget.common.LogtargetConstants;
import com.logtarget.common.NotYetProcessedException;
import com.logtarget.common.RawLogHolder;
import com.logtarget.common.parse.LogSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * S3Service implements LogPersistenceService, saving files to S3
 */
public class S3Service implements LogPersistenceService {
    private static final Logger log = LoggerFactory.getLogger(S3Service.class);
    private static final String AWS_ERROR_CODE_NO_SUCH_KEY = "NoSuchKey";
    private final AmazonS3 amazonS3;
    private final ObjectMapper objectMapper;
    private final String bucketName;

    public S3Service(
            final AmazonS3 amazonS3,
            final ObjectMapper objectMapper,
            final String bucketName
    ) {
        this.amazonS3 = amazonS3;
        this.objectMapper = objectMapper;
        this.bucketName = bucketName;
    }

    /**
     * saveLogFile persists a log file to S3
     * @param contentType
     * @param contentLength
     * @param inputStream
     * @return
     */
    @Override
    public String saveLogFile(
            final String contentType,
            final long contentLength,
            final InputStream inputStream
    ) {
        final String fileId = UUID.randomUUID().toString();
        final ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(contentLength);
        objectMetadata.setHeader(HttpHeaders.CONTENT_TYPE, contentType);

        final PutObjectResult result = amazonS3.putObject(
                bucketName,
                String.format(LogtargetConstants.FORMAT_LOG_RAW, fileId),
                inputStream,
                objectMetadata
        );
        return fileId;
    }

    /**
     * retrieve the raw log from S3
     * @param fileId
     * @return RawLogHolder
     */
    @Override
    public RawLogHolder retrieveLogFile(final String fileId) {
        final S3Object s3Object = amazonS3.getObject(bucketName, fileId);
        final ObjectMetadata metadata = s3Object.getObjectMetadata();
        return new RawLogHolder(
                metadata::getContentType,
                metadata::getContentLength,
                s3Object::getObjectContent
        );
    }

    /**
     * getBatteryPercentage retrieves the log summary for the provided fileId, if available
     *
     * @param fileId
     * @return
     * @throws NotYetProcessedException - if the log summary can't be found, but the raw log is found
     * @throws FileNotFoundException - if neither the log summary or the raw log can be found
     */
    @Override
    public LogSummary getBatteryPercentage(String fileId) throws NotYetProcessedException, FileNotFoundException {
        if (StringUtils.isEmpty(fileId)) {
            throw new RuntimeException("Invalid paramater");
        }

        try {
            final String summary = amazonS3.getObjectAsString(
                    bucketName,
                    String.format(LogtargetConstants.FORMAT_LOG_SUMMARY, fileId)
            );

            return objectMapper.readValue(summary, LogSummary.class);

        } catch (AmazonS3Exception e) {
            if (404 == e.getStatusCode() && AWS_ERROR_CODE_NO_SUCH_KEY.equals(e.getErrorCode())) {
                if (log.isDebugEnabled()) {
                    log.debug(String.format("'log-summary' not found for fileId %s - checking for " +
                            "the existence of 'log-raw'", fileId));
                }
            } else {
                throw e;
            }
        } catch (IOException e) {
            log.error(String.format("LogSummary for fileId %s was found, but could not be deserialized", fileId), e);
            throw new RuntimeException(e);
        }

        try {
            final AccessControlList logFileAcl = amazonS3.getObjectAcl(
                    bucketName,
                    String.format(LogtargetConstants.FORMAT_LOG_SUMMARY, fileId)
            );
            // if the acl exists - the log file exists

            throw new NotYetProcessedException(String.format("'log-summary' has not been generated for fileId %s, " +
                    "but 'log-raw' is available for processing", fileId));

        } catch (NotYetProcessedException e) {
            throw e;
        } catch (Exception e) {
            final String message = String.format("Unable to locate log file for fileId %s", fileId);
            log.warn(message, e);
            throw new FileNotFoundException(message);
        }
    }
}
