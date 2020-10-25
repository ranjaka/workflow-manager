package com.personal.camundamanager.service;

import org.atteo.classindex.IndexSubclasses;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;

@IndexSubclasses
public interface CustomExternalTaskInterface {
    // Bootstrap client function
    void initExternalClientService();

    // Error handler
    void handleFailure(ExternalTaskService externalTaskService, ExternalTask externalTask, String errorMessage);
}
