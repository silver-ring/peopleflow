package com.peopleflow.application;

import java.util.UUID;

final public class IdGenerator {
    public static String GenerateId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
