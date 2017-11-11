package com.valverde.scheduler.web.rest;

import com.valverde.scheduler.service.ScheduleService;
import com.valverde.scheduler.web.dto.ScheduleDTO;
import com.valverde.scheduler.web.dto.ScheduleRequestDTO;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CommonsLog
@RestController
@RequestMapping("/rest")
public class ScheduleRestController {

    @PostMapping("schedule")
    public ResponseEntity<ScheduleDTO> getSchedule(@RequestBody ScheduleRequestDTO request) {
        try {
            final ScheduleDTO scheduleDTO = scheduleService.generateSchedule(request);
            return new ResponseEntity<>(scheduleDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception during generation of schedule.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ScheduleRestController(final ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    private final ScheduleService scheduleService;
}