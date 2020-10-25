package com.personal.camundamanager.config;


import com.personal.camundamanager.service.CustomExternalTaskClientBuilder;
import com.personal.camundamanager.service.CustomExternalTaskInterface;
import com.personal.camundamanager.service.ExternalTaskClientBase;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.atteo.classindex.ClassIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Configuration
@Slf4j
@EnableAsync
public class CamundaConfig {

    @Autowired
    Environment env;

    @Autowired
    CamundaExternalTaskWorkerConfig externalTaskWorkerConfig;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Instantiates all external task clients during camunda engine runtime
     * This makes all external clients start up during runtime and available to accept jobs for external task
     * services from the start
     *
     * @return List of external task clients
     */
    @Bean
    public List<CustomExternalTaskInterface> initCustomExternalTaskClients() {
        List<CustomExternalTaskInterface> customExternalTaskClients = new ArrayList<>();

        Iterable<Class<? extends CustomExternalTaskInterface>> iterable =
                ClassIndex.getSubclasses(CustomExternalTaskInterface.class);

        AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
        IntStream.range(0, externalTaskWorkerConfig.getCount()).forEach(
                externalTaskClient -> {
                    iterable.forEach(item -> {
                        try {
                            Object obj = factory.createBean(item.newInstance().getClass(),
                                    AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, true);
                            factory.autowireBean(obj);
                            customExternalTaskClients.add((CustomExternalTaskInterface) obj);
                        } catch (InstantiationException e) {
                            log.error("Cannot instantiate {} ", e.getMessage());

                        } catch (IllegalAccessException e) {
                            log.error("Illegal access {}", e.getMessage());
                        }
                    });
                }
        );

        return customExternalTaskClients;

    }

    // Set common configs for all external task clients which use the CustomExternalTaskInterface
    @Bean
    @Lazy(false)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ExternalTaskClientBase configureExternalTaskClientBase() {
        log.debug("Configuring external task base parameters");
        CustomExternalTaskClientBuilder builder = CustomExternalTaskClientBuilder.create();
        builder.baseUrl(env.getProperty("camunda.bpm.endpoint"))
                .asyncResponseTimeout(10000);

        return builder.buildExternalTaskClientBase();
    }

    // General config parameters for camunda external task clients
    @Configuration
    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode
    public class CamundaExternalTaskWorkerConfig {
        @Value("${camunda.bpm.external-task-worker.count}")
        private int count;
        @Value("${camunda.bpm.external-task-worker.retries}")
        private int retries;
        @Value("${camunda.bpm.external-task-worker.retry-timeout}")
        private int retryTimeout;
        @Value("${camunda.bpm.external-task-worker.lock-timeout}")
        private int lockTimeout;


    }


}
