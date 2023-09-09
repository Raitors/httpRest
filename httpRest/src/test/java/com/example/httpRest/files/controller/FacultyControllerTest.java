package com.example.httpRest.files.controller;

import com.example.httpRest.exception.DataNotFoundException;
import com.example.httpRest.model.Faculty;
import com.example.httpRest.model.Student;
import com.example.httpRest.repository.FacultyRepository;
import com.example.httpRest.repository.StudentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {

    @Autowired
    TestRestTemplate template;
    @Autowired
    FacultyRepository facultyRepository;
    @Autowired
    StudentRepository studentRepository;

    @AfterEach
    void clean() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    @Test
    void create() throws DataNotFoundException {
        String name = "math";
        String color = "black";
        ResponseEntity<Faculty> response = createFaculty(name, color);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("math");
        assertThat(response.getBody().getColor()).isEqualTo("black");

    }

    @Test
    void getById() {
        ResponseEntity<Faculty> response = createFaculty("math", "black");
        Long facultyId = response.getBody().getId();

        response = template.getForEntity("/faculty/" + facultyId, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getColor()).isEqualTo("black");
        assertThat(response.getBody().getName()).isEqualTo("math");
    }

    @Test
    void getAll() {
        createFaculty("math", "black");
        createFaculty("histor", "blue");

        ResponseEntity<Collection> response = template.
                getForEntity("/faculty/", Collection.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);
    }


    @Test
    void update() {
        ResponseEntity<Faculty> response = createFaculty("math", "black");
        Long facultyId = response.getBody().getId();

        template.put("/faculty/" + facultyId, new Faculty(null, "math", "green"));

        response = template.getForEntity("/faculty/" + facultyId, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getColor()).isEqualTo("green");
    }

    @Test
    void delete() {
        ResponseEntity<Faculty> response = createFaculty("math", "black");
        Long facultyId = response.getBody().getId();

        template.delete("/faculty/" + facultyId);

        response = template.getForEntity("/faculty/" + facultyId, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void filteredColorName() {
        String name = "geography";
        createFaculty(name, "black");
        createFaculty("history", "white");

        ResponseEntity<ArrayList> response = template
                .getForEntity("/faculty/by-color-or-name?search=" + name, ArrayList.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
        Map<String, String> next = (HashMap) response.getBody().iterator().next();
        assertThat(next.get("name")).isEqualTo(name);
    }

    @Test
    void filteredColor() {
        String color = "red";
        createFaculty("geography", color);
        createFaculty("history", "white");
        ResponseEntity<ArrayList> response = template
                .getForEntity("/faculty/filtered?color=" + color, ArrayList.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
        Map<String, String> next = (HashMap) response.getBody().iterator().next();
        assertThat(next.get("color")).isEqualTo(color);
    }

    @Test
    void byStudent() {
        ResponseEntity<Faculty> response = createFaculty("math", "black");
        Faculty faculty = response.getBody();
        Student student = new Student(null, "A", 20);
        student.setFaculty(faculty);
        ResponseEntity<Student> studentResponse = template.postForEntity("/student", student, Student.class);
        assertThat(studentResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Long studentId = studentResponse.getBody().getId();

        template.getForEntity("/faculty/by-student?studentId=" + studentId, Faculty.class);

        response = template.getForEntity("/faculty/by-student?studentId=" + studentId, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(faculty);
    }

    private ResponseEntity<Faculty> createFaculty(String name, String color) {
        ResponseEntity<Faculty> response = template.postForEntity("/faculty",
                new Faculty(null, name, color),
                Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        return response;
    }
}
