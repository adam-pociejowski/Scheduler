package com.valverde.scheduler.model;

import lombok.Data;

@Data
public class Class {

    private Long id;

    private Professor professor;

    private StudentGroup studentGroup;

    private Course course;

    private int duration;
}