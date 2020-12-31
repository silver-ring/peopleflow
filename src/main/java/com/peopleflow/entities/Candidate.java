package com.peopleflow.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Getter
@Setter
public class Candidate {

    @Id
    @GeneratedValue
    private Long id;

    private String fullName;
    private String email;
    private String coverLetter;
    private String resume;
    private List<String> links;

}
