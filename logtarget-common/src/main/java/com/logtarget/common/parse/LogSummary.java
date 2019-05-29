package com.logtarget.common.parse;

import java.io.Serializable;

/**
 * LogSummary is a pojo for commonly accessed flight attributes
 */
public class LogSummary implements Serializable {
    private int batteryPercentageStart;
    private int batteryPercentageEnd;

    public LogSummary() {
    }

    public LogSummary(
            final int batteryPercentageStart,
            final int batteryPercentageEnd
    ) {
        this.batteryPercentageStart = batteryPercentageStart;
        this.batteryPercentageEnd = batteryPercentageEnd;
    }

    /**
     * getter for batteryPercentageStart
     *
     * @return int batteryPercentageStart
     */
    public int getBatteryPercentageStart() {
        return batteryPercentageStart;
    }

    /**
     * setter for batteryPercentageStart
     *
     * @param batteryPercentageStart
     */
    public void setBatteryPercentageStart(int batteryPercentageStart) {
        this.batteryPercentageStart = batteryPercentageStart;
    }

    /**
     * getter for batteryPercentageEnd
     *
     * @return int batteryPercentageEnd
     */
    public int getBatteryPercentageEnd() {
        return batteryPercentageEnd;
    }

    /**
     * setter for batteryPercentageEnd
     *
     * @param batteryPercentageEnd
     */
    public void setBatteryPercentageEnd(int batteryPercentageEnd) {
        this.batteryPercentageEnd = batteryPercentageEnd;
    }
}
