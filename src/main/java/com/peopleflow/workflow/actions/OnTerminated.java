package com.peopleflow.workflow.actions;

import com.peopleflow.repos.ApplicationRepo;
import com.peopleflow.workflow.HiringEvents;
import com.peopleflow.workflow.HiringStates;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
@Log
public class OnTerminated implements Action<HiringStates, HiringEvents> {

    private final ApplicationRepo applicationRepo;

    @Autowired
    public OnTerminated(ApplicationRepo applicationRepo) {
        this.applicationRepo = applicationRepo;
    }

    @Override
    public void execute(StateContext<HiringStates, HiringEvents> context) {
        System.out.println(">>>>>>>>>>>>>>>>>>");
        System.out.println("terminated");
        System.out.println(">>>>>>>>>>>>>>>>>>");
    }

}
