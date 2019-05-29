package com.logtarget.webapp.service;

import com.logtarget.common.NotYetProcessedException;
import com.logtarget.common.RawLogHolder;
import com.logtarget.common.parse.LogSummary;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * LogPersistenceService is an interface to abstract the details of the backing service
 * from the container
 */
public interface LogPersistenceService {

    /**
     * saveLogFile saves a log file and returns a unique id
     * @param contentType
     * @param contentLength
     * @param inputStream
     * @return
     */
    String saveLogFile(
            String contentType,
            long contentLength,
            InputStream inputStream
    );

    /**
     * retrieveLogFile returns a RawLogHolder which contains the attributes required to
     * return a log file
     * @param fileId
     * @return
     */
    RawLogHolder retrieveLogFile(String fileId);

    /**
     * getBatteryPercentage return a LogSummary which contains the battery percentage values
     * @param fileId
     * @return
     * @throws NotYetProcessedException
     * @throws FileNotFoundException
     */
    LogSummary getBatteryPercentage(String fileId) throws NotYetProcessedException, FileNotFoundException;
}
