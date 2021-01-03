package com.peopleflow.application;

import lombok.Data;

@Data
public class CreateApplicationRequest {
    private String email;
    private String fullName;
    private String resume;
    private String coverLetter;
    private String jobTitle;
}
