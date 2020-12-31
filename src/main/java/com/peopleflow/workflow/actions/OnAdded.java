package com.peopleflow.workflow.actions;

import com.peopleflow.application.CreateApplicationRequest;
import com.peopleflow.entities.Application;
import com.peopleflow.entities.Candidate;
import com.peopleflow.repos.ApplicationRepo;
import com.peopleflow.workflow.HiringEvents;
import com.peopleflow.workflow.HiringStates;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
@Log
public class OnAdded implements Action<HiringStates, HiringEvents> {

    private final ApplicationRepo applicationRepo;

    @Autowired
    public OnAdded(ApplicationRepo applicationRepo) {
        this.applicationRepo = applicationRepo;
    }

    @Override
    public void execute(StateContext<HiringStates, HiringEvents> context) {
        ExtendedState extendedState = context.getExtendedState();
        CreateApplicationRequest createApplicationRequest = extendedState.get("application", CreateApplicationRequest.class);

        String applicationId = context.getStateMachine().getId();
        Candidate candidate = new Candidate();
        candidate.setEmail(createApplicationRequest.getEmail());
        candidate.setCoverLetter(createApplicationRequest.getCoverLetter());
        candidate.setResume(createApplicationRequest.getResume());
        candidate.setLinks(createApplicationRequest.getLinks());

        Application application = new Application();
        application.setId(applicationId);
//        application.setCandidate(candidate);
//        application.setDate(LocalDate.now());
//        application.setPosition(createApplicationRequest.getPosition());
//        application.setStatus(HiringStates.ADDED);

        applicationRepo.save(application);
    }

}
