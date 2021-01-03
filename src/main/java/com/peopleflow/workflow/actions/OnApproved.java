package com.peopleflow.workflow.actions;

import com.peopleflow.application.ApproveApplicationRequest;
import com.peopleflow.entities.Application;
import com.peopleflow.exceptions.ApplicationNotExist;
import com.peopleflow.repos.ApplicationRepo;
import com.peopleflow.workflow.HiringEvents;
import com.peopleflow.workflow.HiringStates;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Log
public class OnApproved implements Action<HiringStates, HiringEvents> {

    private final ApplicationRepo applicationRepo;

    @Autowired
    public OnApproved(ApplicationRepo applicationRepo) {
        this.applicationRepo = applicationRepo;
    }

    @Override
    public void execute(StateContext<HiringStates, HiringEvents> context) {
        ExtendedState extendedState = context.getExtendedState();
        ApproveApplicationRequest approveApplicationRequest = extendedState.get("application", ApproveApplicationRequest.class);
        Application application = applicationRepo.findById(context.getStateMachine().getId()).orElseThrow(ApplicationNotExist::new);
        application.setApprovalDate(LocalDate.now());
        application.setApprovalReason(approveApplicationRequest.getReason());
        application.setHiringStates(HiringStates.APPROVED);
        applicationRepo.save(application);
    }

}
