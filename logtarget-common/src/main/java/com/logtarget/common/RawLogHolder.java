package com.logtarget.common;

import java.io.InputStream;
import java.util.function.Supplier;

/**
 * pojo for returning a log stream in a generic fashion
 */
public class RawLogHolder {
    private final Supplier<String> contentType;
    private final Supplier<Long> contentLength;
    private final Supplier<InputStream> inputStream;

    public RawLogHolder(
            Supplier<String> contentType,
            Supplier<Long> contentLength,
            Supplier<InputStream> inputStream
    ) {
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.inputStream = inputStream;
    }

    /**
     * getter for contentType
     *
     * @return java.lang.String contentType
     */
    public String getContentType() {
        return contentType.get();
    }

    /**
     * getter for contentLength
     *
     * @return long contentLength
     */
    public long getContentLength() {
        return contentLength.get();
    }

    /**
     * getter for inputStream
     *
     * @return java.io.InputStream inputStream
     */
    public InputStream getInputStream() {
        return inputStream.get();
    }
}
