package com.valverde.scheduler.model;

import lombok.Getter;
import java.util.List;

@Getter
public class ScheduleInput {

    private List<Class> classes;

    private List<Classroom> classrooms;

    public ScheduleInput(final List<Class> classes, final List<Classroom> classrooms) {
        this.classes = classes;
        this.classrooms = classrooms;
    }
}
