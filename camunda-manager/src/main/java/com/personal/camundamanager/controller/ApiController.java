package com.personal.camundamanager.controller;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ApiController {

    @Autowired
    RuntimeService runtimeService;

    @GetMapping("/start")
    public ResponseEntity<Object> startProcessWorkflow() {
        log.info("Reached /start endpoint");
        try {
            runtimeService.startProcessInstanceByKey("SimpleWorkflowProcess");
            return ResponseEntity.ok("Process started successfully");

        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().build();
        }


    }
}
