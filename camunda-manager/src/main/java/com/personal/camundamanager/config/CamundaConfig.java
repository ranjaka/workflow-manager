package com.personal.camundamanager.config;

import com.personal.camundamanager.service.NodeExternalService;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class CamundaConfig {

    @Autowired
    RepositoryService repositoryService;

    // Initiate external task client once process engine is running
    @EventListener
    public void notify(final PostDeployEvent unused){
        ExternalTaskClient initTask = new NodeExternalService().initClient();

    }

}
