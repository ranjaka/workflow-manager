package com.personal.camundamanager.service;

import com.personal.camundamanager.config.CamundaConfig;
import com.personal.camundamanager.config.Constants;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@NoArgsConstructor
public class ExternalTaskHandler implements CustomExternalTaskInterface {

    @Autowired
    ExternalTaskClientBase nodeClient;

    @Autowired
    CamundaConfig.CamundaExternalTaskWorkerConfig camundaExternalTaskWorkerConfig;


    @Override
    @PostConstruct
    @Transactional
    public void initExternalClientService() {


        nodeClient.getExternalTaskClient()
                .subscribe("external")
                .lockDuration(camundaExternalTaskWorkerConfig.getLockTimeout())
                .handler((externalTask, externalTaskService) -> {
                    Map<String, Object> vars = new HashMap<>();
                    try {
                        log.info("Reached external task:");
                        vars.put("TestMessage", "Hello");
                        String response = this.getExternalServiceResponse();
                        vars.put("Response", response);
                    } catch (IOException e) {

                        handleFailure(externalTaskService, externalTask, e.getMessage());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        externalTaskService.complete(externalTask, vars);
                    }

                }).open();

    }

    @Override
    public void handleFailure(ExternalTaskService externalTaskService, ExternalTask externalTask, String errorMessage) {
        log.error("Exception in external task with Id: {}", externalTask.getId());

    }

    private String getExternalServiceResponse() throws IOException, InterruptedException {
        // New HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Create a request
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(Constants.EXTERNAL_TASK_URI + "/home")).header("accept", "application/json").build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("Message received: {}", response.body());

        return response.body();

    }
}
