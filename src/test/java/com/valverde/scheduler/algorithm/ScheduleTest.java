package com.valverde.scheduler.algorithm;

import com.valverde.scheduler.algorithm.Schedule;
import com.valverde.scheduler.model.ScheduleConfiguration;
import com.valverde.scheduler.model.ScheduleInput;
import com.valverde.scheduler.parser.CsvScheduleInputParser;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import com.valverde.scheduler.model.Class;
import static org.junit.Assert.assertEquals;

public class ScheduleTest {

    @Before
    public void setup() {
        parser = new CsvScheduleInputParser();
    }

    @Test
    public void shouldReturnMaxFitnessForGoodSchedule() throws Exception {
        final Schedule schedule = prepareFitSchedule();
        double fitness = schedule.calculateFitness();
        assertEquals(5.0, fitness, DELTA);
    }

    @Test
    public void shouldReturnZeroFitnessForFatalSchedule() throws Exception {
        final Schedule schedule = prepareFatalSchedule();
        double fitness = schedule.calculateFitness();
        assertEquals(0.0, fitness, DELTA);
    }

    private Schedule prepareFatalSchedule() throws Exception {
        final ScheduleInput scheduleInput = prepareInput();
        scheduleInput.getClassrooms().forEach(cl -> cl.setCapacity(0));
        scheduleInput.getClasses().forEach(cl -> cl.setDuration(2));
        final ScheduleConfiguration config = prepareConfiguration(scheduleInput);
        final List<Class>[] slots = prepareFatalSlots(config, scheduleInput);
        return new Schedule(config, slots);
    }

    private Schedule prepareFitSchedule() throws Exception {
        final ScheduleInput scheduleInput = prepareInput();
        final ScheduleConfiguration config =prepareConfiguration (scheduleInput);
        final List<Class>[] slots = prepareFitSlots(config, scheduleInput);
        return new Schedule(config, slots);
    }

    @SuppressWarnings("unchecked")
    private List<Class>[] prepareFatalSlots(final ScheduleConfiguration config, final ScheduleInput scheduleInput) {
        final List<Class>[] slots = new ArrayList[config.getDayHours()*config.getRoomsAmount()*config.getDaysAmount()];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = new ArrayList<>();
        }
        slots[10].add(scheduleInput.getClasses().get(0));
        slots[10].add(scheduleInput.getClasses().get(3));
        slots[10].add(scheduleInput.getClasses().get(5));
        slots[10].add(scheduleInput.getClasses().get(2));
        slots[10].add(scheduleInput.getClasses().get(4));
        slots[10].add(scheduleInput.getClasses().get(1));
        return slots;
    }

    @SuppressWarnings("unchecked")
    private List<Class>[] prepareFitSlots(final ScheduleConfiguration config, final ScheduleInput scheduleInput) {
        final List<Class>[] slots = new ArrayList[config.getDayHours()*config.getRoomsAmount()*config.getDaysAmount()];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = new ArrayList<>();
        }
        slots[0].add(scheduleInput.getClasses().get(0));
        slots[1].add(scheduleInput.getClasses().get(5));
        slots[4].add(scheduleInput.getClasses().get(2));
        slots[10].add(scheduleInput.getClasses().get(1));
        slots[12].add(scheduleInput.getClasses().get(3));
        slots[13].add(scheduleInput.getClasses().get(4));
        return slots;
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

    private CsvScheduleInputParser parser;

    private static final Double DELTA = 0.001;
}