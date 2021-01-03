package com.peopleflow;

import com.peopleflow.entities.EntitiesPackage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication()
@EntityScan(basePackageClasses = EntitiesPackage.class)
public class PeopleFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(PeopleFlowApplication.class, args);
    }

}
