package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.RoleDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.enums.Gender;
import com.cydeo.enums.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProjectControllerTest {

    @Autowired
    private MockMvc mvc;

    static UserDTO userDTO;
    static ProjectDTO projectDTO;

    static String token;

    @BeforeAll
    static void setUp(){

        token = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJDRjVDS2JtNl9JaEdGbmc1LXIyTjczVHJ2cFo1azB1TXZsazZVTEk3dlVBIn0.eyJleHAiOjE2ODg2OTM0NzIsImlhdCI6MTY4ODY5MzE3MiwianRpIjoiN2MwYmQ0NzUtNjk3ZS00NWFhLTkwM2MtM2Q5MzAyYzYxM2Y5IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2F1dGgvcmVhbG1zL2N5ZGVvLWRldiIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI4NzM5MTE1My04OGI3LTRlMDMtODRjNy0yNDM1M2YwNTBjNDciLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJ0aWNrZXRpbmctYXBwIiwic2Vzc2lvbl9zdGF0ZSI6IjQ0MmVkNWVhLTA1N2ItNDRjMC1iN2U1LTcyZGMwNjFhYzc3MiIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cDovL2xvY2FsaG9zdDo4MDgxIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsImRlZmF1bHQtcm9sZXMtY3lkZW8tZGV2IiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJ0aWNrZXRpbmctYXBwIjp7InJvbGVzIjpbIk1hbmFnZXIiXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgZW1haWwiLCJzaWQiOiI0NDJlZDVlYS0wNTdiLTQ0YzAtYjdlNS03MmRjMDYxYWM3NzIiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwicHJlZmVycmVkX3VzZXJuYW1lIjoibWlrZSJ9.BTO8wx8J1kKYzCAMwXeJRf_Of4SzQlGT90aRQDS-MbTbFbu-eLiA5rCYNrQG32LZ6vJpgaTZvz4XHcbeZipkkcMxn1UEYa57gqRJaZxtrasmXNMDYTCOLJfAiOGJP-aY2DVhzoL7QSlJoLIwuJFQoNsR0SHZ4xPhMaIcKLvNECVeVFm9yZ82XTbDWmJm8x3oyHRyDqYoTsOp1joQ5uLveDOAo7Q4FtOVgYklHs7VDid-uqMio_m2afibMI-HkryTTktGH3dt-ZBSm-JFlU2GyJbPkYoEG9bgU-RLLPy2GR3NTbe5gxjwJB-p6CvF2CmUIH6YU3F1cPgbZHbGS8AexA";


        userDTO = UserDTO.builder()
                .id(2L)
                .firstName("mike")
                .lastName("mike")
                .userName("mike")
                .passWord("adm1")
                .confirmPassWord("adm1")
                .role(new RoleDTO(2L, "Manager"))
                .gender(Gender.MALE)
                .build();

        projectDTO = ProjectDTO.builder()
                .projectCode("Api1")
                .projectName("Api-ozzy")
                .assignedManager(userDTO)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5))
                .projectDetail("Api Test")
                .projectStatus(Status.OPEN)
                .build();

    }


    @Test
    public void givenNoToken_whenGetRequest() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/project"))
                .andExpect(status().is4xxClientError());

    }
    @Test
    public void givenToken_whenGetRequest() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/project")
                .header("Authorization", token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].projectCode").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].assignedManager.userName").isNotEmpty());

    }

    @Test
    public void givenToken_createProject() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/project")
                .header("Authorization", token)
                .content(toJsonString(projectDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

    }


    @Test
    public void givenToken_updateProject() throws Exception {

        projectDTO.setProjectName("Api-cydeo");

        mvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/project")
                        .header("Authorization", token)
                        .content(toJsonString(projectDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Project is successfully updated"));

    }

    @Test
    public void givenToken_deleteProject() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/project/" + projectDTO.getProjectCode())
                        .header("Authorization", token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }







    private static String toJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }





}