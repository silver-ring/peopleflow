package com.peopleflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peopleflow.application.CreateApplicationRequest;
import com.peopleflow.application.IdGenerator;
import com.peopleflow.entities.Application;
import com.peopleflow.entities.Candidate;
import com.peopleflow.repos.ApplicationRepo;
import com.peopleflow.workflow.HiringStates;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationRepo applicationRepo;

    @Test
    public void createSchedulerTest_success() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String url = "/application/";
        CreateApplicationRequest createApplicationRequest = new CreateApplicationRequest();
        createApplicationRequest.setEmail("test@test.com");
        createApplicationRequest.setCoverLetter("test cover latter");
        createApplicationRequest.setJobTitle("test title");
        createApplicationRequest.setResume("test resume");
        createApplicationRequest.setFullName("test name");

        Candidate candidate = new Candidate();
        candidate.setFullName(createApplicationRequest.getFullName());
        candidate.setEmail(createApplicationRequest.getEmail());
        candidate.setCoverLetter(createApplicationRequest.getCoverLetter());
        candidate.setResume(createApplicationRequest.getResume());

        String applicationId = IdGenerator.GenerateId();
        Application application = new Application();
        application.setId(applicationId);
        application.setCandidate(candidate);
        application.setStartDate(LocalDate.now());
        application.setJobTitle(createApplicationRequest.getJobTitle());
        application.setHiringStates(HiringStates.ADDED);


        try (MockedStatic<IdGenerator> idGeneratorMock = mockStatic(IdGenerator.class)) {
            idGeneratorMock.when(IdGenerator::GenerateId).thenReturn(applicationId);
            when(applicationRepo.save(application)).thenReturn(application);
            String result = this.mockMvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createApplicationRequest)))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();
            assertEquals(result, applicationId);
            verify(applicationRepo, times(1)).save(application);
            idGeneratorMock.verify(times(1), IdGenerator::GenerateId);
        }
    }


}
