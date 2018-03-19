package com.agico.smk.carinspectionapp;

import com.agico.smk.carinspectionapp.soap.utils.SOAPUtils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void additionIsCorrect() throws Exception {
        assertEquals("07/02/2018", SOAPUtils.getDateFromSoap("1517943600000"));
    }
}