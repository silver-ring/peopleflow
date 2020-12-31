package com.peopleflow.application;

import com.peopleflow.exceptions.ApplicationNotExist;
import com.peopleflow.workflow.HiringEvents;
import com.peopleflow.workflow.HiringStates;
import lombok.extern.java.Log;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Log
@RestController
@RequestMapping("/employee")
public class ApplicationApi {

    private final StateMachineService<HiringStates, HiringEvents> stateMachineService;

    public ApplicationApi(StateMachineService<HiringStates, HiringEvents> stateMachineService) {
        this.stateMachineService = stateMachineService;
    }

    @PostMapping("/")
    @ResponseBody
    public String createApplication(@RequestBody CreateApplicationRequest createApplicationRequest) {
        String id = UUID.randomUUID().toString().replace("-", "");
        StateMachine<HiringStates, HiringEvents> stateMachine = stateMachineService.acquireStateMachine(id, true);
        stateMachine.getExtendedState().getVariables().put("application", createApplicationRequest);
        return stateMachine.getId();
    }

    @GetMapping("/{applicationId}")
    @ResponseBody
    public HiringStates getApplicationStatus(@PathVariable String applicationId) {
        StateMachine<HiringStates, HiringEvents> stateMachine = getStateMachine(applicationId);
        if (stateMachine.getState() == null) {
            throw new ApplicationNotExist();
        }
        String fullName = (String) stateMachine.getExtendedState().getVariables().getOrDefault("fullName", "Not found");
        return stateMachine.getState().getId();
    }

    @PatchMapping("/check/{applicationId}")
    public void checkApplication(@PathVariable String applicationId) {
        StateMachine<HiringStates, HiringEvents> stateMachine = getStateMachine(applicationId);
        if (stateMachine.getState() == null) {
            throw new ApplicationNotExist();
        }
        stateMachine.sendEvent(HiringEvents.ON_IN_CHECK);
    }

    @PatchMapping("/approve/{applicationId}")
    public void approveApplicationStatus(@PathVariable String applicationId) {
        StateMachine<HiringStates, HiringEvents> stateMachine = getStateMachine(applicationId);
        stateMachine.sendEvent(HiringEvents.ON_APPROVED);
    }

    @PatchMapping("/activate/{applicationId}")
    public void activateApplicationStatus(@PathVariable String applicationId) {
        StateMachine<HiringStates, HiringEvents> stateMachine = getStateMachine(applicationId);
        stateMachine.sendEvent(HiringEvents.ON_ACTIVE);
    }

    @PatchMapping("/treminate/{applicationId}")
    public void treminateApplicationStatus(@PathVariable String applicationId) {
        StateMachine<HiringStates, HiringEvents> stateMachine = getStateMachine(applicationId);
        stateMachine.sendEvent(HiringEvents.ON_TERMINATED);
        stateMachineService.releaseStateMachine(applicationId, true);
    }

    @PatchMapping("/reject/{applicationId}")
    public void rejectApplicationStatus(@PathVariable String applicationId) {
        StateMachine<HiringStates, HiringEvents> stateMachine = getStateMachine(applicationId);
        stateMachine.sendEvent(HiringEvents.ON_REJECTED);
        stateMachineService.releaseStateMachine(applicationId, true);
    }

    @PatchMapping("/cancel/{applicationId}")
    public void cancelApplicationStatus(@PathVariable String applicationId) {
        StateMachine<HiringStates, HiringEvents> stateMachine = getStateMachine(applicationId);
        stateMachine.sendEvent(HiringEvents.ON_CANCELED);
        stateMachineService.releaseStateMachine(applicationId, true);
    }

    private StateMachine<HiringStates, HiringEvents> getStateMachine(String applicationId) {
        StateMachine<HiringStates, HiringEvents> stateMachine = stateMachineService.acquireStateMachine(applicationId, false);
        if (stateMachine.getState() == null) {
            throw new ApplicationNotExist();
        }
        return stateMachine;
    }

}
