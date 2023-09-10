package com.example.httpRest.files.controller;

import com.example.httpRest.controller.FacultyController;
import com.example.httpRest.model.Faculty;
import com.example.httpRest.model.Student;
import com.example.httpRest.repository.FacultyRepository;
import com.example.httpRest.repository.StudentRepository;
import com.example.httpRest.service.FacultyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(FacultyController.class)
public class FacultyControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    StudentRepository studentRepository;

    @MockBean
    FacultyRepository facultyRepository;

    @SpyBean
    FacultyService facultyService;


    @Test
    void create() throws Exception {
        Faculty faculty = new Faculty(1L, "math", "black");
        when(facultyRepository.save(ArgumentMatchers.any(Faculty.class))).thenReturn(faculty);
        mockMvc.perform(MockMvcRequestBuilders.post("/faculty")
                        .content(objectMapper.writeValueAsString(faculty))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("math"))
                .andExpect(jsonPath("$.color").value("black"));
    }

    @Test
    void update() throws Exception {
        Faculty faculty = new Faculty(1L, "math", "black");
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        when(facultyRepository.save(ArgumentMatchers.any(Faculty.class))).thenReturn(faculty);
        mockMvc.perform(MockMvcRequestBuilders.put("/faculty/1")
                        .content(objectMapper.writeValueAsString(faculty))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("math"))
                .andExpect(jsonPath("$.color").value("black"));
    }

    @Test
    void getById() throws Exception {
        Faculty faculty = new Faculty(1L, "math", "black");
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("math"))
                .andExpect(jsonPath("$.color").value("black"));
    }

    @Test
    void delete() throws Exception {
        Faculty faculty = new Faculty(1L, "math", "black");
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
//        when(studentRepository.delete(ArgumentMatchers.any(Student.class));

        mockMvc.perform(MockMvcRequestBuilders.delete("/faculty/1")
                        .content(objectMapper.writeValueAsString(faculty))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("math"))
                .andExpect(jsonPath("$.color").value("black"))
        ;
    }

    @Test
    void filteredByColor() throws Exception {
        Faculty faculty = new Faculty(1L, "math", "black");
        when(facultyRepository.findAllByColor("red"))
                .thenReturn(Arrays.asList(
                        new Faculty(1L, "math", "black"),
                        new Faculty(2L, "meh", "blue")));

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/filtered?color=red")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].color").value("black"));
    }


    @Test
    void findByStudent() throws Exception {
        Faculty faculty = new Faculty(1L, "math", "black");
        Student student = new Student(1L, "A", 23);
        student.setFaculty((Faculty) faculty);

        when(facultyRepository.findByStudent_Id(1L)).thenReturn(Optional.of(faculty));
        ;

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/by-student?studentId=1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.color").value("black"));
    }
}
