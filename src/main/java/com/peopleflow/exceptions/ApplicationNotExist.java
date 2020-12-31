package com.peopleflow.exceptions;

public class ApplicationNotExist extends RuntimeException {

    public ApplicationNotExist() {
        super("Application not exist");
    }

}
