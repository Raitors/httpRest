package com.example.httpRest.controller;

import com.example.httpRest.model.Student;
import com.example.httpRest.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {


    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public Collection<Student> getAll() {
        return studentService.getAll();
    }

    @GetMapping("/{id}")
    public Student getById(@PathVariable("id") Long id) {
        return studentService.getById(id);
    }

    @PostMapping
    public Student create(@RequestBody Student student) {
        return studentService.create(student);
    }

    @GetMapping("/filtered")
    public Collection<Student> getByAge(@RequestParam("age") int age) {
        return studentService.getByAge(age);
    }

    @GetMapping("/by-age")
    public Collection<Student> getByAge(@RequestParam int min, @RequestParam int max) {
        return studentService.getByAgeBetween(min, max);
    }

    @GetMapping("/by-faculty")
    public Collection<Student> getByAge(@RequestParam Long facultyId) {
        return studentService.getByFacultyId(facultyId);
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable("id") Long id, @RequestBody Student student) {
        return studentService.update(id, student);
    }

    @DeleteMapping("/{id}")
    public Student remove(@PathVariable("id") Long id) {
        return studentService.remove(id);
    }

    @GetMapping("/count")
    public long count() {
        return studentService.count();
    }

    @GetMapping("/average")
    public double average() {
        return studentService.average();
    }

    @GetMapping("/last-five")
    public List<Student> getLastFive() {
        return studentService.getLastStudents(5);
    }

    @GetMapping("/stream/start-with-a")
    public List<String> getAllStartsWithA() {
        return studentService.getAllStartsWithA();
    }

    @GetMapping("/stream/average-age")
    public double getAverageAge() {
        return studentService.getAverageAge();
    }

    @GetMapping("/threads/async")
    public void printAsync() {
        studentService.printAsync();
    }

    @GetMapping("/threads/sync")
    public void printSync() {
        studentService.printSync();
    }
}
