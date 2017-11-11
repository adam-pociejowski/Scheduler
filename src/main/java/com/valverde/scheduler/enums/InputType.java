package com.valverde.scheduler.enums;

import lombok.Getter;

@Getter
public enum InputType {
    PROFESSOR("P"),
    COURSE("C"),
    CLASSROOM("R"),
    STUDENT_GROUP("G"),
    CLASS("CL");

    private String label;

    InputType(final String label) {
        this.label = label;
    }
}