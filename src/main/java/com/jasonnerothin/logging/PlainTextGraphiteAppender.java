package com.jasonnerothin.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Implements the client side of Carbon's plaintext protocol:
 * <p/>
 * {@see http://graphite.readthedocs.org/en/latest/feeding-carbon.html}
 */
public class PlainTextGraphiteAppender extends AppenderBase<ILoggingEvent> {

    public static final String GRAPHITE_HOST_PROPERTY_NAME = "graphite.host";
    private static final String GRAPHITE_HOST_PROPERTY_DESCRIPTION = "graphite host";
    public static final String GRAPHITE_PORT_PROPERTY_NAME = "graphite.port";
    private static final String MISSING_PROPERTY_ERROR
            = "%s was not provided to the runtime. Please ensure that %s is provided (e.g. -D%s=aValue).";

    private static Integer graphitePort;
    private static String graphiteHost;

    public PlainTextGraphiteAppender() {
    }

    private void init() {
        graphiteHost = System.getProperty(GRAPHITE_HOST_PROPERTY_NAME);
        ensure(GRAPHITE_HOST_PROPERTY_DESCRIPTION, GRAPHITE_HOST_PROPERTY_NAME, graphiteHost);

        String graphitePortValue = System.getProperty(GRAPHITE_PORT_PROPERTY_NAME);
        ensure("graphite port", GRAPHITE_PORT_PROPERTY_NAME, graphitePortValue);
        try {
            graphitePort = Integer.valueOf(graphitePortValue);
        } catch (NumberFormatException e) {
            throw new ExceptionInInitializerError(String.format("Illegal value for %s: [%s]. Port number should be an integer.", GRAPHITE_PORT_PROPERTY_NAME, graphitePortValue));
        }
    }

    private void ensure(String propertyDescription, String propertyName, String value) {
        if (value == null || value.trim().length() == 0)
            throw new IllegalStateException(
                    String.format(MISSING_PROPERTY_ERROR, propertyDescription, propertyName, propertyName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void append(ILoggingEvent eventObject) {
        OutputStream outputStream = null;
        PrintWriter writer = null;
        Socket graphiteSocket = getSocket();
        try {
            outputStream = graphiteSocket.getOutputStream();
            writer = new PrintWriter(outputStream, true);
            writer.println(eventObject.getMessage() + " " + unixTime());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if( writer != null ) writer.flush();
            try {
                if (outputStream != null) outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (writer != null) writer.close();
        }
    }

    private String unixTime() {
        return String.format("%d", System.currentTimeMillis() / 1000 );
    }

    private Socket getSocket() {
        if (graphiteHost == null) init();
        try {
            return new Socket(graphiteHost, graphitePort);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

}