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

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {
    @Autowired
    TestRestTemplate template;

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    FacultyRepository facultyRepository;

    @AfterEach
    void clean() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    @Test
    void create() throws DataNotFoundException {
        String name = "A";
        int age = 20;
        ResponseEntity<Student> response = createStudent(name, age);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("A");
        assertThat(response.getBody().getAge()).isEqualTo(20);
    }

    @Test
    void getAll() {
        createStudent("A", 20);
        createStudent("B", 30);

        ResponseEntity<Collection> response = template.
                getForEntity("/student/", Collection.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);
    }

    @Test
    void getById() {
        ResponseEntity<Student> response = createStudent("A", 20);
        Long stundetId = response.getBody().getId();

        response = template.getForEntity("/student/" + stundetId, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("A");
        assertThat(response.getBody().getAge()).isEqualTo(20);
    }

    @Test
    void update() {
        ResponseEntity<Student> response = createStudent("A", 20);
        Long studentId = response.getBody().getId();

        template.put("/student/" + studentId, new Student(null, "A", 25));

        response = template.getForEntity("/student/" + studentId, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAge()).isEqualTo(25);
    }

    @Test
    void delete() {
        ResponseEntity<Student> response = createStudent("A", 20);
        Long studentId = response.getBody().getId();

        template.delete("/student/" + studentId);

        response = template.getForEntity("/student/" + studentId, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void getByAge() {
        int age = 20;
        createStudent("A", age);
        createStudent("B", 30);
        ResponseEntity<ArrayList> response = template
                .getForEntity("/student/filtered?age=" + age, ArrayList.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
        Map<String, Integer> next = (HashMap) response.getBody().iterator().next();

    }

    @Test
    void getByAgeBetween() {
        createStudent("A", 20);
        createStudent("B", 30);
        createStudent("C", 40);

        ResponseEntity<ArrayList> response = template
                .getForEntity("/student/by-age?min=10&max=35", ArrayList.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);
        Map<String, Integer> next = (HashMap) response.getBody().iterator().next();
    }

    @Test
    void byFaculty() {

        ResponseEntity<Student> response = createStudent("A", 20);
        Student student = response.getBody();

        Faculty faculty = new Faculty(null, "math", "black");
        faculty.setStudent(List.of(student));

        ResponseEntity<Faculty> facultyResponseEntity = template.
                postForEntity("/faculty", faculty, Faculty.class);
        assertThat(facultyResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Long facultyId = facultyResponseEntity.getBody().getId();

        ResponseEntity<Student[]> responses = template.
                getForEntity("/student/by-faculty?facultyId=" + facultyId, Student[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(student);

    }

    private ResponseEntity<Student> createStudent(String name, int age) {
        ResponseEntity<Student> response = template.postForEntity("/student",
                new Student(null, name, age),
                Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        return response;
    }

}
