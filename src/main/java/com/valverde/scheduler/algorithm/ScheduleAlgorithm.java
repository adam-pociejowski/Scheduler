package com.valverde.scheduler.algorithm;

import com.valverde.scheduler.model.AlgorithmConfig;
import com.valverde.scheduler.model.ScheduleConfiguration;
import com.valverde.scheduler.model.ScheduleInput;
import com.valverde.scheduler.util.ScheduleUtils;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.util.Set;
import com.valverde.scheduler.model.Class;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
public class ScheduleAlgorithm {

    public Schedule process(final ScheduleInput scheduleInput, final ScheduleConfiguration scheduleConfig) {
        this.scheduleInput = scheduleInput;
        this.scheduleConfig = scheduleConfig;
        Population population = new Population(config.getPopulationSize(), scheduleConfig)
                .init(scheduleInput);
        int generation = 0;
        final DecimalFormat df = new DecimalFormat("#.###");
        log.info("Generation: "+generation+" | Fitness: "+
                df.format(population.getSchedules()[0].getFitness())+"/5.0");
        while (generation < config.getMaxIterations() && population.getSchedules()[0].getFitness() < 5.0) {
            generation++;
            population = evolve(population);
            if (generation % 500 == 0) {
                log.info("Generation: "+generation+" | Fitness: "+
                        df.format(population.getSchedules()[0].getFitness())+"/5.0");
            }
        }
        log.info("Finished with generation: "+generation);
        final Schedule schedule = population.getSchedules()[0];
        lookForAnomalies(schedule);
        return schedule;
    }

    private Population evolve(final Population population) {
        return mutatePopulation(crossoverPopulation(population));
    }

    private Population crossoverPopulation(final Population population) {
        final Population crossoverPopulation = new Population(config.getPopulationSize(), scheduleConfig);
        for (int i = 0; i < config.getPopulationSize(); i++) {
            final Schedule schedule = population.getSchedules()[i];
            if (i < config.getNumberOfEliteChromosomes()) {
                crossoverPopulation.getSchedules()[i] = schedule;
            } else {
                final Schedule parent1 = selectParentSchedule(population);
                final Schedule parent2 = selectParentSchedule(population);
                final Schedule crossoverSchedule = crossoverSchedule(parent1, parent2);
                crossoverSchedule.calculateFitness();
                crossoverPopulation.getSchedules()[i] = crossoverSchedule;
            }
        }
        crossoverPopulation.sort();
        return crossoverPopulation;
    }

    private Population mutatePopulation(final Population population) {
        final Population mutatedPopulation = new Population(config.getPopulationSize(), scheduleConfig);
        for (int i = 0; i < config.getPopulationSize(); i++) {
            final Schedule schedule = population.getSchedules()[i];
            if (i < config.getNumberOfEliteChromosomes()) {
                mutatedPopulation.getSchedules()[i] = schedule;
            } else {
                mutatedPopulation.getSchedules()[i] = mutateSchedule(schedule);
            }
            mutatedPopulation.getSchedules()[i].calculateFitness();
        }
        mutatedPopulation.sort();
        return mutatedPopulation;
    }

    private Schedule crossoverSchedule(final Schedule parent1, final Schedule parent2) {
        final List<Class> classes = scheduleInput.getClasses();
        final List<Class>[] slots = ScheduleUtils.createEmptySlots(scheduleConfig);
        for (Class clazz : classes) {
            if (Math.random() < 0.5) {
                ScheduleUtils.copyClassFromSchedule(clazz, slots, parent1);
            } else {
                ScheduleUtils.copyClassFromSchedule(clazz, slots, parent2);
            }
        }
        return new Schedule(scheduleConfig, slots);
    }

    private Schedule mutateSchedule(final Schedule schedule) {
        final int slotsAmount = schedule.getSlots().length;
        final List<Class> classes = scheduleInput.getClasses();
        for (Class clazz : classes) {
            if (Math.random() < config.getMutationRate()) {
                final int classStart = new Random().nextInt(
                        slotsAmount - ((clazz.getDuration() - 1) * scheduleInput.getClassrooms().size()));
                ScheduleUtils.moveClass(clazz, schedule, classStart);
            }
        }
        return schedule;
    }

    private Schedule selectParentSchedule(final Population population) {
        final Population tournamentPopulation = new Population(config.getTournamentSelectionSize(), scheduleConfig);
        for (int i = 0; i < config.getTournamentSelectionSize(); i++) {
            int randomIndex = new Random().nextInt(config.getPopulationSize());
            tournamentPopulation.getSchedules()[i] = population.getSchedules()[randomIndex];
        }
        tournamentPopulation.sort();
        return tournamentPopulation.getSchedules()[0];
    }

    private void lookForAnomalies(final Schedule schedule) {
        final Set<Class> classes = schedule.getClassMap().keySet();
        for (Class cl : classes) {
            Integer classStartTime = schedule.getClassMap().get(cl);
            final int classEndTime = schedule.getClassEndIndex(cl, classStartTime);
            for (int time = classStartTime; time <= classEndTime; time += 3) {
                if (schedule.isAnotherClassInSlot(time, cl)) {
                    log.error("Another class overlapping "+cl.getId());
                }
            }
        }
    }

    public ScheduleAlgorithm(final AlgorithmConfig config) {
        this.config = config;
    }

    private final AlgorithmConfig config;

    private ScheduleConfiguration scheduleConfig;

    private ScheduleInput scheduleInput;
}