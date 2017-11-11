package com.valverde.scheduler.web.dto;

import com.valverde.scheduler.algorithm.Schedule;
import com.valverde.scheduler.model.Class;
import lombok.Data;
import java.util.List;

@Data
public class ScheduleDTO {

    public ScheduleDTO(final Schedule schedule) {
        this.slots = schedule.getSlots();
        this.fitness = schedule.getFitness();
        this.roomsAmount = schedule.getConfig().getRoomsAmount();
    }

    private int roomsAmount;

    private List<Class>[] slots;

    private double fitness;
}