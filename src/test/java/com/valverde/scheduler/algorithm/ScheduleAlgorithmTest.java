package com.valverde.scheduler.algorithm;

import com.valverde.scheduler.model.AlgorithmConfig;
import com.valverde.scheduler.model.ScheduleConfiguration;
import com.valverde.scheduler.model.ScheduleInput;
import com.valverde.scheduler.parser.CsvScheduleInputParser;
import org.junit.Before;
import org.junit.Test;

public class ScheduleAlgorithmTest {

    @Before
    public void setup() {
        parser = new CsvScheduleInputParser();
        scheduleAlgorithm = new ScheduleAlgorithm(prepareAlgorithmConfig());
    }

    @Test
    public void shouldGenerateSolution() throws Exception {
        final ScheduleInput scheduleInput = prepareInput();
        scheduleAlgorithm.process(scheduleInput, prepareConfiguration(scheduleInput));
    }

    private ScheduleConfiguration prepareConfiguration(final ScheduleInput scheduleInput) {
        final ScheduleConfiguration configuration = new ScheduleConfiguration();
        configuration.setClassrooms(scheduleInput.getClassrooms());
        configuration.setDaysAmount(2);
        configuration.setRoomsAmount(scheduleInput.getClassrooms().size());
        configuration.setDayHours(4);
        return configuration;
    }

    private ScheduleInput prepareInput() throws Exception {
        return parser.parse("src/main/resources/scheduleInput/testInput.csv", ",");
    }

    private AlgorithmConfig prepareAlgorithmConfig() {
        return new AlgorithmConfig(8, 3, 2, 0.1, 100);
    }

    private ScheduleAlgorithm scheduleAlgorithm;

    private CsvScheduleInputParser parser;
}