package com.peopleflow.entities;

import com.peopleflow.workflow.HiringStates;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class Application {

    @Id
    private String id;
    private String jobTitle;
    private HiringStates hiringStates;

    @OneToOne(cascade = CascadeType.ALL)
    private Candidate candidate;

    @OneToOne(cascade = CascadeType.ALL)
    private EmployeeContact screeningEmployeeContact;

    private LocalDate startDate;
    private LocalDate screeningDate;
    private LocalDate approvalDate;
    private LocalDate rejectionDate;
    private LocalDate terminationDate;
    private LocalDate cancelDate;

    private String approvalReason;
    private String rejectionReason;
    private String terminationReason;
    private String cancelReason;

}
