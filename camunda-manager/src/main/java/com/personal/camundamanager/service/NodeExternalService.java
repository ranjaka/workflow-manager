package com.personal.camundamanager.service;

import com.personal.camundamanager.config.Constants;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.ExternalTaskClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class NodeExternalService {

    public ExternalTaskClient initClient() {
        ExternalTaskClient client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/engine-rest")
                .build();

        client.subscribe("external")
                .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {

                    Map<String, Object> vars = new HashMap<>();

                    try {
                        log.info("Reached external task:");

                        vars.put("TestMessage", "Hello");

                        String response = this.getExternalServiceResponse();
                        vars.put("Response", response);

                    } catch (IOException e) {
                        log.error("Exception: {}", e.getMessage());

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();

                    } finally {
                        externalTaskService.complete(externalTask, vars);
                    }


                }).open();

        return client;

    }

    private String getExternalServiceResponse() throws IOException, InterruptedException {
        // New HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Create a request
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(Constants.EXTERNAL_TASK_URI+"/home")).header("accept", "application/json").build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("Message received: {}", response.body());

        return response.body();

    }

}
