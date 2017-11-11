package com.valverde.scheduler.algorithm;

import com.valverde.scheduler.model.ScheduleConfiguration;
import com.valverde.scheduler.model.ScheduleInput;
import com.valverde.scheduler.util.ScheduleUtils;
import lombok.Getter;
import java.text.DecimalFormat;
import java.util.Arrays;

public class Population {

    Population init(final ScheduleInput input) {
        for (int i = 0; i < schedules.length; i++) {
            schedules[i] = ScheduleUtils.createRandomSchedule(input, scheduleConfig);
            schedules[i].calculateFitness();
        }
        try {
            sort();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    void toString(final int generation) {
        System.out.println("------- Generation "+generation+" -------");
        final DecimalFormat df = new DecimalFormat("#.###");
        for (int i = 0; i < schedules.length; i++) {
            System.out.println("Schedule # "+i+" | Fitness: "+df.format(schedules[i].getFitness()));
        }
        System.out.println();
    }

    void sort() {
        Arrays.sort(schedules, (ch1, ch2) -> {
            if (ch1.getFitness() > ch2.getFitness()) {
                return -1;
            } else if (ch1.getFitness() < ch2.getFitness()) {
                return 1;
            }
            return 0;
        });
    }

    Population(int length, final ScheduleConfiguration scheduleConfig) {
        this.scheduleConfig = scheduleConfig;
        this.schedules = new Schedule[length];
        for (int i = 0; i < schedules.length; i++) {
            schedules[i] = new Schedule(scheduleConfig, ScheduleUtils.createEmptySlots(scheduleConfig));
            schedules[i].calculateFitness();
        }
    }

    private final ScheduleConfiguration scheduleConfig;

    @Getter
    private Schedule[] schedules;
}