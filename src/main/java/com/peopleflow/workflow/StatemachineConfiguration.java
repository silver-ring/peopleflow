package com.peopleflow.workflow;

import com.peopleflow.workflow.actions.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.data.jpa.JpaPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;

@Log
@Configuration
@EnableStateMachineFactory
public class StatemachineConfiguration extends StateMachineConfigurerAdapter<HiringStates, HiringEvents> {

    private final StateChangeListener stateChangeListener;
    private final JpaStateMachineRepository jpaStateMachineRepository;
    private final OnAdded onAdded;
    private final OnScreening onScreening;
    private final OnActivated onActivated;
    private final OnApproved onApproved;
    private final OnCanceled onCanceled;
    private final OnRejected onRejected;
    private final OnTerminated onTerminated;


    @Autowired
    public StatemachineConfiguration(StateChangeListener stateChangeListener,
                                     JpaStateMachineRepository jpaStateMachineRepository, OnAdded onAdded,
                                     OnScreening onScreening, OnActivated onActivated, OnApproved onApproved, OnCanceled onCanceled, OnRejected onRejected, OnTerminated onTerminated) {
        this.stateChangeListener = stateChangeListener;
        this.jpaStateMachineRepository = jpaStateMachineRepository;
        this.onAdded = onAdded;
        this.onScreening = onScreening;
        this.onActivated = onActivated;
        this.onApproved = onApproved;
        this.onCanceled = onCanceled;
        this.onRejected = onRejected;
        this.onTerminated = onTerminated;
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<HiringStates, HiringEvents> transitions) throws Exception {
        transitions
                .withExternal().source(HiringStates.ADDED).target(HiringStates.SCREENING)
                .event(HiringEvents.ON_SCREENING).action(this.onScreening)
                .and()
                .withExternal().source(HiringStates.SCREENING).target(HiringStates.APPROVED)
                .event(HiringEvents.ON_APPROVED).action(this.onApproved)
                .and()
                .withExternal().source(HiringStates.APPROVED).target(HiringStates.ACTIVATED)
                .event(HiringEvents.ON_ACTIVATED).action(this.onActivated)
                .and()
                .withExternal().source(HiringStates.SCREENING).target(HiringStates.REJECTED)
                .event(HiringEvents.ON_REJECTED).action(this.onRejected)
                .and()
                .withExternal().source(HiringStates.ADDED).target(HiringStates.CANCELED)
                .event(HiringEvents.ON_CANCELED).action(this.onCanceled)
                .and()
                .withExternal().source(HiringStates.SCREENING).target(HiringStates.CANCELED)
                .event(HiringEvents.ON_CANCELED).action(this.onCanceled)
                .and()
                .withExternal().source(HiringStates.APPROVED).target(HiringStates.CANCELED)
                .event(HiringEvents.ON_CANCELED).action(this.onCanceled)
                .and()
                .withExternal().source(HiringStates.ACTIVATED).target(HiringStates.TERMINATED)
                .event(HiringEvents.ON_TERMINATED).action(this.onTerminated);
    }

    @Override
    public void configure(StateMachineStateConfigurer<HiringStates, HiringEvents> states) throws Exception {
        states.withStates()
                .initial(HiringStates.ADDED, onAdded)
                .state(HiringStates.SCREENING)
                .state(HiringStates.APPROVED)
                .state(HiringStates.ACTIVATED)
                .end(HiringStates.TERMINATED)
                .end(HiringStates.REJECTED)
                .end(HiringStates.CANCELED);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<HiringStates, HiringEvents> config) throws Exception {
        config.withPersistence().runtimePersister(stateMachineRuntimePersister())
                .and()
                .withConfiguration()
                .listener(stateChangeListener);
    }

    @Bean
    public StateMachineService<HiringStates, HiringEvents> stateMachineService(StateMachineFactory<HiringStates, HiringEvents> stateMachineFactory) {
        return new DefaultStateMachineService<>(stateMachineFactory, stateMachineRuntimePersister());
    }

    @Bean
    public StateMachineRuntimePersister<HiringStates, HiringEvents, String> stateMachineRuntimePersister() {
        return new JpaPersistingStateMachineInterceptor<>(jpaStateMachineRepository);
    }

}
