package com.peopleflow.workflow.actions;

import com.peopleflow.workflow.HiringEvents;
import com.peopleflow.workflow.HiringStates;
import lombok.extern.java.Log;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
@Log
public class OnRejected implements Action<HiringStates, HiringEvents> {

    @Override
    public void execute(StateContext<HiringStates, HiringEvents> context) {
        System.out.println(">>>>>>>>>>>>>>>>>>");
        System.out.println("rejected");
        System.out.println(">>>>>>>>>>>>>>>>>>");
    }

}
