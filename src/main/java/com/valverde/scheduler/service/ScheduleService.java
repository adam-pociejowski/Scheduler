package com.valverde.scheduler.service;

import com.valverde.scheduler.algorithm.Schedule;
import com.valverde.scheduler.algorithm.ScheduleAlgorithm;
import com.valverde.scheduler.model.AlgorithmConfig;
import com.valverde.scheduler.model.ScheduleConfiguration;
import com.valverde.scheduler.model.ScheduleInput;
import com.valverde.scheduler.parser.CsvScheduleInputParser;
import com.valverde.scheduler.web.dto.ScheduleDTO;
import com.valverde.scheduler.web.dto.ScheduleRequestDTO;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

@Service
@CommonsLog
public class ScheduleService {

    public ScheduleDTO generateSchedule(final ScheduleRequestDTO request) throws Exception {
        final ScheduleInput input = new CsvScheduleInputParser()
                .parse(DEFAULT_INPUT_PATH + request.getInputFileName(), DEFAULT_SEPARATOR);
        final AlgorithmConfig config = prepareAlgorithmConfig(request);
        final ScheduleConfiguration scheduleConfig = prepareScheduleConfig(request, input);
        log.info("Generating schedule...");
        final long startTime = System.currentTimeMillis();
        final ScheduleAlgorithm scheduleAlgorithm = new ScheduleAlgorithm(config);
        final Schedule schedule = scheduleAlgorithm.process(input, scheduleConfig);
        log.info("Schedule generated in "+(System.currentTimeMillis() - startTime)+
                " millis with fitness: "+schedule.getFitness());
        return new ScheduleDTO(schedule);
    }

    private ScheduleConfiguration prepareScheduleConfig(final ScheduleRequestDTO request, final ScheduleInput scheduleInput) {
        final ScheduleConfiguration configuration = new ScheduleConfiguration();
        configuration.setClassrooms(scheduleInput.getClassrooms());
        configuration.setDaysAmount(request.getDaysAmount());
        configuration.setRoomsAmount(scheduleInput.getClassrooms().size());
        configuration.setDayHours(request.getHoursAmount());
        return configuration;
    }

    private AlgorithmConfig prepareAlgorithmConfig(final ScheduleRequestDTO request) {
        return new AlgorithmConfig(
                request.getPopulationSize(),
                request.getEliteChromosomesNumber(),
                request.getTournamentSelectionNumber(),
                request.getMutationRate(),
                request.getMaxIterations());
    }

    private static final String DEFAULT_INPUT_PATH = "src/main/resources/scheduleInput/";

    private static final String DEFAULT_SEPARATOR = ",";
}