package com.valverde.scheduler.parser;

import org.junit.Before;
import org.junit.Test;

public class CsvScheduleInputParserTest {

    @Before
    public void setup() {
        parser = new CsvScheduleInputParser();
    }

    @Test
    public void shouldParseInputFile() throws Exception {
        parser.parse("src/main/resources/scheduleInput/testInput.csv", ",");
    }

    private CsvScheduleInputParser parser;
}