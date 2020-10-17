package com.personal.camundamanager;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@EnableProcessApplication
public class CamundaManagerApplication {

    @Autowired
    RepositoryService repositoryService;

    public static void main(String[] args) {
        SpringApplication.run(CamundaManagerApplication.class, args);
    }

    @EventListener
    public void notify(final PostDeployEvent usused) {
        final ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("SimpleWorkflowProcess").singleResult();
    }

}
