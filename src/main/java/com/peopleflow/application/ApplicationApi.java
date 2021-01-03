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
        StateMachine<HiringStates, HiringEvents> stateMachine = stateMachineService.acquireStateMachine(id, false);
        stateMachine.getExtendedState().getVariables().put("application", createApplicationRequest);
        stateMachine.start();
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

    @PatchMapping("/screening/{applicationId}")
    public void screenApplication(@PathVariable String applicationId, @RequestBody ApplicationScreeningRequest applicationScreeningRequest) {
        StateMachine<HiringStates, HiringEvents> stateMachine = getStateMachine(applicationId);
        if (stateMachine.getState() == null) {
            throw new ApplicationNotExist();
        }
        stateMachine.getExtendedState().getVariables().put("application", applicationScreeningRequest);
        stateMachine.sendEvent(HiringEvents.ON_SCREENING);
    }

    @PatchMapping("/approve/{applicationId}")
    public void approveApplicationStatus(@PathVariable String applicationId, @RequestBody ApproveApplicationRequest approveApplicationRequest) {
        StateMachine<HiringStates, HiringEvents> stateMachine = getStateMachine(applicationId);
        stateMachine.getExtendedState().getVariables().put("application", approveApplicationRequest);
        stateMachine.sendEvent(HiringEvents.ON_APPROVED);
    }

    @PatchMapping("/activate/{applicationId}")
    public void activateApplicationStatus(@PathVariable String applicationId, @RequestBody ActivateApplicationRequest activateApplicationRequest) {
        StateMachine<HiringStates, HiringEvents> stateMachine = getStateMachine(applicationId);
        stateMachine.getExtendedState().getVariables().put("application", activateApplicationRequest);
        stateMachine.sendEvent(HiringEvents.ON_ACTIVATED);
    }

    @PatchMapping("/treminate/{applicationId}")
    public void terminateApplicationStatus(@PathVariable String applicationId, @RequestBody TerminateApplicationRequest terminateApplicationRequest) {
        StateMachine<HiringStates, HiringEvents> stateMachine = getStateMachine(applicationId);
        stateMachine.getExtendedState().getVariables().put("application", terminateApplicationRequest);
        stateMachine.sendEvent(HiringEvents.ON_TERMINATED);
        stateMachineService.releaseStateMachine(applicationId, true);
    }

    @PatchMapping("/reject/{applicationId}")
    public void rejectApplicationStatus(@PathVariable String applicationId, @RequestBody RejectApplicationRequest rejectApplicationRequest) {
        StateMachine<HiringStates, HiringEvents> stateMachine = getStateMachine(applicationId);
        stateMachine.getExtendedState().getVariables().put("application", rejectApplicationRequest);
        stateMachine.sendEvent(HiringEvents.ON_REJECTED);
        stateMachineService.releaseStateMachine(applicationId, true);
    }

    @PatchMapping("/cancel/{applicationId}")
    public void cancelApplicationStatus(@PathVariable String applicationId, CancelApplicationRequest cancelApplicationRequest) {
        StateMachine<HiringStates, HiringEvents> stateMachine = getStateMachine(applicationId);
        stateMachine.getExtendedState().getVariables().put("application", cancelApplicationRequest);
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
