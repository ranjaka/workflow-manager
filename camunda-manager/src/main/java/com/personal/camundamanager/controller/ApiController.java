package com.personal.camundamanager.controller;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ApiController {

    @Autowired
    RuntimeService runtimeService;

    @GetMapping("/start")
    public void startProcessWorkflow(){
        log.info("Reached /start endpoint");
        runtimeService.startProcessInstanceByKey("SimpleWorkflowProcess");

    }
}
