package com.jasonnerothin.logging;

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
public class PlainTextGraphiteAppender extends AppenderBase {

    static final String GRAPHITE_HOST_PROPERTY_NAME = "graphite.host";
    private static final String GRAPHITE_HOST_PROPERTY_DESCRIPTION = "graphite host";
    static final String GRAPHITE_PORT_PROPERTY_NAME = "graphite.port";
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
    protected void append(Object eventObject) {
        OutputStream outputStream = null;
        PrintWriter writer = null;
        Socket graphiteSocket = getSocket();
        try {
            outputStream = graphiteSocket.getOutputStream();
            writer = new PrintWriter(outputStream);
            writer.println(eventObject);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (writer != null) writer.close();
        }
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