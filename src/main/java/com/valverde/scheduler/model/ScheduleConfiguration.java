package com.valverde.scheduler.model;

import lombok.Data;

import java.util.List;

@Data
public class ScheduleConfiguration {

    private int daysAmount;

    private int dayHours;

    private int roomsAmount;

    private List<Classroom> classrooms;
}