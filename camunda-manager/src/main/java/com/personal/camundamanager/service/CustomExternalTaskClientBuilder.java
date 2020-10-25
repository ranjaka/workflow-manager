package com.personal.camundamanager.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.impl.ExternalTaskClientBuilderImpl;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.client.task.impl.ExternalTaskServiceImpl;


@Slf4j
public class CustomExternalTaskClientBuilder extends ExternalTaskClientBuilderImpl {

    public static CustomExternalTaskClientBuilder create() {

        return new CustomExternalTaskClientBuilder();
    }

    public ExternalTaskClientBase buildExternalTaskClientBase() {

        final ExternalTaskClient client = super.build();

        final ExternalTaskService service = new ExternalTaskServiceImpl(engineClient);

        return new ExternalTaskClientBase(client, service);
    }

}
