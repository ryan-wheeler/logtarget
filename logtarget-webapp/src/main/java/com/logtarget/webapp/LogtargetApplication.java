package com.logtarget.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * LogtargetApplication contains the 'main' method for the LogtargetApplication,
 * and the ServletInitializer.configure method
 */
@SpringBootApplication
public class LogtargetApplication extends SpringBootServletInitializer {

    /**
     * entrypoint of application when started manually or using gradle:bootrun
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(LogtargetApplication.class, args);
    }

    /**
     * entrypoint of application when started in a container such as Tomcat
     * @param application
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(LogtargetApplication.class);
    }

}
