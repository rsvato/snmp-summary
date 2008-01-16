package net.paguo.trafshow.backend.snmp.summary.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Reyentenko
 */
public class TestWithFixture {
    private List<TrafficRecord> records = new ArrayList<TrafficRecord>();
    @Before
    public final void prepare() throws IOException {
        final InputStream stream = getClass().getClassLoader().getResourceAsStream("fixture.txt");
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        String line = "";
        while(line != null){
            line = r.readLine();
            if (line != null && line.trim().length() > 0) {
                final String[] strings = line.split("\t");
                records.add(new TrafficRecord(Long.valueOf(strings[0].trim()),
                        Long.valueOf(strings[1].trim()), Long.valueOf(strings[2].trim())));
            } else {
            }
        }
    }

    @Test
    public void testFixtureProcessing(){
        RouterSummaryTraffic traffic = new RouterSummaryTraffic();
        int i = 0;
        for (TrafficRecord record : records) {
            i++;
            long before = traffic.getTotalOutput();
            traffic.processRecord(record);
            long difference = traffic.getTotalOutput() - before;
            Assert.assertTrue("No more than 100000000: " + difference + " at step " + i, difference < 100000000L);
        }
    }

}
