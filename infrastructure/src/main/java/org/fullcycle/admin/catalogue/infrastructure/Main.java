package org.fullcycle.admin.catalogue.infrastructure;

import org.fullcycle.admin.catalogue.application.UseCase;
import org.fullcycle.admin.catalogue.infrastructure.config.WebServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args){
        SpringApplication.run(WebServerConfig.class, args);
    }

}
