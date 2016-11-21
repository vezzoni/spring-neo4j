package com.github.vezzoni.spring.neo4j;

import com.github.vezzoni.spring.neo4j.helper.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@SpringBootApplication
/*
https://github.com/spring-projects/spring-boot/issues/6709
 */
@EntityScan({"com.github.vezzoni.spring.neo4j.model", "BOOT-INF.classes.com.github.vezzoni.spring.neo4j.model"})
public class Application implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(Application.class);

    @Autowired
    private DataLoader loader;

    private String fileName;

    public Application() {

    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        String action = args[0];
        switch (action) {
            case "start": {
                fileName = args[1];
                this.loadData();
            } break;
            case "stop": {
                System.exit(0);
            } break;
            default: {
                System.out.println("the action argument must be either start or stop.");
            }
        }

    }

    private void loadData() {

        try (FileReader fr = new FileReader(fileName); BufferedReader reader = new BufferedReader(fr)) {
            loader.load(reader);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

}