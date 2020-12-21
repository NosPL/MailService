package com.noscompany.mailing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class MailServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MailServiceApplication.class, args);
    }
}