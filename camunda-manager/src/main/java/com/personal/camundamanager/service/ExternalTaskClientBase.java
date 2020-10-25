package com.personal.camundamanager.service;

import lombok.Getter;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTaskService;

public class ExternalTaskClientBase {

    @Getter
    private final ExternalTaskClient externalTaskClient;
    @Getter
    private final ExternalTaskService externalTaskService;


    public ExternalTaskClientBase(ExternalTaskClient externalTaskClient, ExternalTaskService externalTaskService) {
        this.externalTaskClient = externalTaskClient;
        this.externalTaskService = externalTaskService;
    }
}
