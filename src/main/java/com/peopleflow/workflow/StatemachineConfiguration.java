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

    @Autowired
    public StatemachineConfiguration(StateChangeListener stateChangeListener,
                                     JpaStateMachineRepository jpaStateMachineRepository, OnAdded onAdded) {
        this.stateChangeListener = stateChangeListener;
        this.jpaStateMachineRepository = jpaStateMachineRepository;
        this.onAdded = onAdded;
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<HiringStates, HiringEvents> transitions) throws Exception {
        transitions
                .withExternal().source(HiringStates.ADDED).target(HiringStates.IN_CHECK)
                .event(HiringEvents.ON_IN_CHECK).action(new OnInCheck())
                .and()
                .withExternal().source(HiringStates.IN_CHECK).target(HiringStates.APPROVED)
                .event(HiringEvents.ON_APPROVED).action(new OnApproved())
                .and()
                .withExternal().source(HiringStates.APPROVED).target(HiringStates.ACTIVE)
                .event(HiringEvents.ON_ACTIVE).action(new OnActive())
                .and()
                .withExternal().source(HiringStates.IN_CHECK).target(HiringStates.REJECTED)
                .event(HiringEvents.ON_REJECTED).action(new OnRejected())
                .and()
                .withExternal().source(HiringStates.ADDED).target(HiringStates.CANCELED)
                .event(HiringEvents.ON_CANCELED).action(new OnCanceled())
                .and()
                .withExternal().source(HiringStates.IN_CHECK).target(HiringStates.CANCELED)
                .event(HiringEvents.ON_CANCELED).action(new OnCanceled())
                .and()
                .withExternal().source(HiringStates.APPROVED).target(HiringStates.CANCELED)
                .event(HiringEvents.ON_CANCELED).action(new OnCanceled())
                .and()
                .withExternal().source(HiringStates.ACTIVE).target(HiringStates.TERMINATED)
                .event(HiringEvents.ON_TERMINATED).action(new OnTerminated());
    }

    @Override
    public void configure(StateMachineStateConfigurer<HiringStates, HiringEvents> states) throws Exception {
        states.withStates()
                .initial(HiringStates.ADDED, onAdded)
                .state(HiringStates.IN_CHECK)
                .state(HiringStates.APPROVED)
                .state(HiringStates.ACTIVE)
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
