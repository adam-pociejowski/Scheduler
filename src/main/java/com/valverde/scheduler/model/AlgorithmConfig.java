package com.valverde.scheduler.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlgorithmConfig {

    private final int populationSize;

    private final int numberOfEliteChromosomes;

    private final int tournamentSelectionSize;

    private final double mutationRate;

    private final int maxIterations;
}
