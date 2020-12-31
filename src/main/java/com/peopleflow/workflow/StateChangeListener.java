package com.peopleflow.workflow;

import lombok.extern.java.Log;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;

@Component
@Log
public class StateChangeListener extends StateMachineListenerAdapter<HiringStates, HiringEvents> {

    @Override
    public void stateChanged(State<HiringStates, HiringEvents> from, State<HiringStates, HiringEvents> to) {
        log.info(String.format("stateChanged(from: %s, to: %s)", from + "", to + ""));
    }

}
