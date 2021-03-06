package com.peopleflow.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Candidate {

    @Id
    @GeneratedValue
    private Long id;

    private String fullName;
    private String email;
    private String coverLetter;
    private String resume;

}
