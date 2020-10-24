package com.personal.camundamanager.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class NodeExternalService {

    @Autowired
    ProcessEngine processEngine;

    public ExternalTaskClient initClient(){
        ExternalTaskClient client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/engine-rest")
                .build();

        client.subscribe("external")
                .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {
                    log.info("Reached external task:");
                    Map<String,Object> vars = new HashMap<>();
                    vars.put("TestMessage","Hello");

                    try{
                        String response = this.getExternalServiceResponse();
                        vars.put("Response",response);

                    }catch(IOException | InterruptedException e){
                        log.error("Exception: {}",e.getMessage());

                    }


                    externalTaskService.complete(externalTask,vars);


                }).open();

        return client;

    }

    private String getExternalServiceResponse() throws IOException, InterruptedException {
        // New HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Create a request
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("http://localhost:3000/home")).header("accept","application/json").build();


        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
        log.info("Message received: {}",response.body());

        return response.body();


    }









}
