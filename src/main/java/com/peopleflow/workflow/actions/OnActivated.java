package com.peopleflow.workflow.actions;

import com.peopleflow.entities.Application;
import com.peopleflow.exceptions.ApplicationNotExist;
import com.peopleflow.repos.ApplicationRepo;
import com.peopleflow.workflow.HiringEvents;
import com.peopleflow.workflow.HiringStates;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Log
public class OnActivated implements Action<HiringStates, HiringEvents> {

    private final ApplicationRepo applicationRepo;

    @Autowired
    public OnActivated(ApplicationRepo applicationRepo) {
        this.applicationRepo = applicationRepo;
    }

    @Override
    public void execute(StateContext<HiringStates, HiringEvents> context) {
        Application application = applicationRepo.findById(context.getStateMachine().getId()).orElseThrow(ApplicationNotExist::new);
        application.setActivationDate(LocalDate.now());
        application.setHiringStates(HiringStates.ACTIVATED);
        applicationRepo.save(application);
    }

}
