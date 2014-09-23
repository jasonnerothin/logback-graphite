package com.jasonnerothin.logging;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: 9/6/14
 * Time: 9:39 AM
 * Provides an integration test for the appender, writing to a real graphite service.
 */
public class PlainTextGraphiteAppenderI10nTest {

    private static final int NUM_ITERATIONS = 120;

    private Logger logger = LoggerFactory.getLogger(getClass());
    private Random random = new Random(System.currentTimeMillis());
    private TestMetric[] kids;

    @Before
    public void setup() {

        System.setProperty(PlainTextGraphiteAppender.GRAPHITE_HOST_PROPERTY_NAME, "192.168.33.10");
        System.setProperty(PlainTextGraphiteAppender.GRAPHITE_PORT_PROPERTY_NAME, "2003");

        kids = new TestMetric[3];
        kids[0] = elida();
        kids[1] = ruby();
        kids[2] = peter();

    }

    @Test
    public void testWriteOnce() throws InterruptedException {
        logKids();
    }

    private void logKids() {
        for (TestMetric kid : kids)
            logger.info(kid.toString());
    }

    @Test
    public void testWriteForAWhile() throws InterruptedException {
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            adjustKids();
            logKids();
            Thread.sleep(1000);
        }
    }

    private void adjustKids() {
        for (TestMetric kid : kids) {
            adjust(kid);
        }
    }

    public void adjust(TestMetric kid) {
        Number number = kid.data;
        kid.data = number.floatValue() + randomNum();
    }

    /**
     * @return between (-1, 1)
     */
    private float randomNum() {
        float pct = random.nextInt(100) / 100f;
        if (random.nextBoolean()) pct = pct * -1;
        return pct;
    }

    private TestMetric peter() {
        TestMetric peter = new TestMetric();
        peter.name = "nerothin.age.peter";
        peter.data = 3.3d;
        return peter;
    }

    private TestMetric ruby() {
        TestMetric ruby = new TestMetric();
        ruby.name = "nerothin.age.ruby";
        ruby.data = 6l;
        return ruby;
    }

    private TestMetric elida() {
        TestMetric elida = new TestMetric();
        elida.name = "nerothin.age.elida";
        elida.data = 8.99;
        return elida;
    }

}

class TestMetric {

    private static final String DELIMITER = " ";

    public String name;
    public Number data;

    @Override
    public String toString() {
        return name + DELIMITER + data + DELIMITER + unixTime();
    }

    private long unixTime() {
        return System.currentTimeMillis() / 1000;
    }

}