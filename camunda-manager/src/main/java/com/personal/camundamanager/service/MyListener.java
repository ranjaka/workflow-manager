package com.personal.camundamanager.service;


import org.camunda.bpm.spring.boot.starter.event.ProcessApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MyListener extends ExternalTaskHandler implements ApplicationListener<ProcessApplicationEvent>{

    @Transactional
    @Override
    public void onApplicationEvent(ProcessApplicationEvent applicationEvent) {

        String requiredState = applicationEvent.getSource().toString();

        if (requiredState.contains("org.springframework.boot.context.event.ApplicationReadyEvent")) {
            System.out.println("GOT here");

            initExternalClientService();

        }
        System.out.println("Received spring custom event - " + applicationEvent.getSource().toString());

    }

}

