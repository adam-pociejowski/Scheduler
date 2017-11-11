package com.valverde.scheduler.util;

import com.valverde.scheduler.algorithm.Schedule;
import com.valverde.scheduler.model.ScheduleConfiguration;
import com.valverde.scheduler.model.ScheduleInput;
import com.valverde.scheduler.model.Class;
import java.util.*;

@SuppressWarnings("unchecked")
public class ScheduleUtils {

    public static Schedule createRandomSchedule(final ScheduleInput input, final ScheduleConfiguration config) {
        final List<Class>[] slots = new ArrayList[config.getDayHours()*config.getRoomsAmount()*config.getDaysAmount()];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = new ArrayList<>();
        }
        for (Class clazz : input.getClasses()) {
            int index = new Random().nextInt(slots.length - ((clazz.getDuration() - 1) * config.getRoomsAmount()));
            slots[index].add(clazz);
        }
        return new Schedule(config, slots);
    }

    public static List<Class>[] createEmptySlots(final ScheduleConfiguration config) {
        final List<Class>[] slots = new ArrayList[config.getDayHours()*config.getRoomsAmount()*config.getDaysAmount()];
        for (int i = 0; i < slots.length; i++)
            slots[i] = new ArrayList<>();
        return slots;
    }

    public static void copyClassFromSchedule(final Class clazz, final List<Class>[] slots,
                                             final Schedule schedule) {
        final Integer classStartIndex = schedule.getClassMap().get(clazz);
        slots[classStartIndex].add(clazz);
    }

    public static Map<Class, Integer> prepareMap(final List<Class>[] slots) {
        final Map<Class, Integer> classAllocation = new HashMap<>();
        for (int i = 0; i < slots.length; i++) {
            for (Class clazz : slots[i]) {
                classAllocation.put(clazz, i);
            }
        }
        return classAllocation;
    }

    public static void moveClass(final Class clazz, final Schedule schedule, final int newClassStart) {
        final Integer startIndex = schedule.getClassMap().get(clazz);
        final List<Class>[] slots = schedule.getSlots();
        slots[startIndex].remove(clazz);
        slots[newClassStart].add(clazz);
    }
}
