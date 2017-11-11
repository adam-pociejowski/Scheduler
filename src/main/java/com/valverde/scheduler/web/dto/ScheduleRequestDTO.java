package com.valverde.scheduler.web.dto;

import lombok.Data;

@Data
public class ScheduleRequestDTO {

    private String inputFileName;

    private int populationSize;

    private int eliteChromosomesNumber;

    private int tournamentSelectionNumber;

    private double mutationRate;

    private int maxIterations;

    private int daysAmount;

    private int hoursAmount;
}
