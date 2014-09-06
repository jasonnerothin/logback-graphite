package com.jasonnerothin.logging;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: 9/6/14
 * Time: 9:39 AM
 * Provides an integration test for the appender, writing to a real graphite service.
 */
public class PlainTextGraphiteAppenderI10nTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final long KEEP_ALIVE_MS = Long.MAX_VALUE;
//    private static final long KEEP_ALIVE_MS = 2;

    @Before
    public void setup(){
        System.setProperty(PlainTextGraphiteAppender.GRAPHITE_HOST_PROPERTY_NAME, "0.0.0.0");
        System.setProperty(PlainTextGraphiteAppender.GRAPHITE_PORT_PROPERTY_NAME, "2003");
    }

    @Test
    public void testWrite() throws InterruptedException {

        TestMetric elida = new TestMetric();
        elida.name = "nerothin.elida.age";
        elida.data = 8.999999999;
        elida.timestamp = new Date();

        Thread.sleep(1000);

        TestMetric ruby = new TestMetric();
        ruby.name = "nerothin.ruby.age";
        ruby.data = 6l;
        ruby.timestamp = new Date();

        Thread.sleep(1000);

        TestMetric peter = new TestMetric();
        peter.name = "nerothin.peter.age";
        peter.data = 3.3f;
        peter.timestamp = new Date();

        logger.info(elida.toString());
        logger.info(ruby.toString());
        logger.info(peter.toString());

//        doNotReturn();
    }



    private void doNotReturn() {
        try{
            Thread.currentThread();
            Thread.sleep(KEEP_ALIVE_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}

class TestMetric {

    private static final String DELIMITER = " ";

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("%s");

    public String name;
    public Number data;
    public Date timestamp;

    @Override
    public String toString() {
        return name + DELIMITER + data + DELIMITER + dateFormat.format(timestamp) + DELIMITER;
    }

}