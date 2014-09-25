package com.jasonnerothin.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: 9/24/14
 * Time: 9:10 PM
 */
public class PlainTextGraphiteLayout extends LayoutBase<ILoggingEvent> {

    private static final String PLAIN_TEXT_GRAPHITE_SEPARATOR = " ";

    /**
     * {@inheritDoc}
     */
    @Override
    public String doLayout(ILoggingEvent event) {
        return event.getMessage()
                + PLAIN_TEXT_GRAPHITE_SEPARATOR
                + unixTime()
                + CoreConstants.LINE_SEPARATOR;
    }

    private String unixTime() {
        return String.format("%d", System.currentTimeMillis() / CoreConstants.MILLIS_IN_ONE_SECOND);
    }
}
