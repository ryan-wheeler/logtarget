package com.logtarget.webapp.rest;

import com.logtarget.common.NotYetProcessedException;
import com.logtarget.common.RawLogHolder;
import com.logtarget.common.parse.LogSummary;
import com.logtarget.webapp.service.LogPersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * LogTargetController contains endpoints for saving and retrieving log files
 */
@RestController
@RequestMapping(path = "/logtarget")
public class LogTargetController {
    private static final Logger log = LoggerFactory.getLogger(LogTargetController.class);
    private static final String STATUS = "status";
    private static final String PROCESSING = "processing";
    private static final ResponseEntity UPLOAD_FAILED = ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload Failed.");
    private static final ResponseEntity NOT_FOUND = ResponseEntity.notFound().build();
    private static final ResponseEntity TRY_LATER = ResponseEntity.status(HttpStatus.ACCEPTED)
            .header(STATUS, PROCESSING).build();
    private final LogPersistenceService logPersistenceService;

    public LogTargetController(final LogPersistenceService logPersistenceService) {
        this.logPersistenceService = logPersistenceService;
    }

    /**
     * putLogFile save a log file
     * @param contentType
     * @param contentLength
     * @param request
     * @return
     */
    @PutMapping(path = "/upload", produces = "application/text")
    public ResponseEntity putLogFile(
            final @RequestHeader(value = "Content-Type", defaultValue = "text/plain") String contentType,
            final @RequestHeader("Content-Length") long contentLength,
            final HttpServletRequest request
    ) {
        ResponseEntity outcome = UPLOAD_FAILED;
        try {
            final String fileId = logPersistenceService.saveLogFile(contentType, contentLength, request.getInputStream());
            outcome = ResponseEntity.ok(fileId);
        } catch (IOException e) {
            log.error("Error uploading file", e);
        }
        return outcome;
    }

    /**
     * getLogFile retrieves the log file for a fileId
     * @param fileId
     * @return
     */
    @GetMapping(path = "/getlog/{fileId}")
    public ResponseEntity getLogFile(@PathVariable("fileId") String fileId) {
        ResponseEntity outcome = NOT_FOUND;
        try {
            final RawLogHolder rawLog = logPersistenceService.retrieveLogFile(fileId);
            outcome = ResponseEntity.status(HttpStatus.OK)
                    .header(HttpHeaders.CONTENT_TYPE, rawLog.getContentType())
                    .header(HttpHeaders.CONTENT_LENGTH, Long.toString(rawLog.getContentLength()))
                    .body(new InputStreamResource(rawLog.getInputStream()));
        } catch (Exception e) {
            log.error(String.format("Error retrieving object for fileId: %s", fileId));
        }

        return outcome;
    }

    /**
     * getBatteryPercentage retrieves the battery percentage for a fileId
     * @param fileId
     * @return
     */
    @GetMapping(path = "/batteryPercentage/{fileId}", produces = "application/json")
    public ResponseEntity getBatteryPercentage(@PathVariable("fileId") String fileId) {
        ResponseEntity outcome = NOT_FOUND;
        try {
            final LogSummary summary = logPersistenceService.getBatteryPercentage(fileId);
            outcome = ResponseEntity.ok(summary);
        } catch (NotYetProcessedException e) {
            outcome = TRY_LATER;
        } catch (FileNotFoundException e) {
            log.warn(String.format("File not found for fileId: %s", fileId));
        } catch (Exception e) {
            log.error(String.format("Error retrieving: %s", fileId), e);
        }
        return outcome;
    }

}
