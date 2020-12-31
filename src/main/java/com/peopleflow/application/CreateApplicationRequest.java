package com.peopleflow.application;

import lombok.Data;

import java.util.List;

@Data
public class CreateApplicationRequest {
    private String email;
    private String fullName;
    private String resume;
    private String coverLetter;
    private String position;
    private List<String> links;
}
