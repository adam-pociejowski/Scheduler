package com.valverde.scheduler.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ScheduleController {

    @RequestMapping("/")
    public String showMainPage() {
        return "index";
    }
}
