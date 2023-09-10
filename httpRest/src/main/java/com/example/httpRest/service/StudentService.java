package com.example.httpRest.service;

import com.example.httpRest.exception.DataNotFoundException;
import com.example.httpRest.model.Faculty;
import com.example.httpRest.model.Student;
import com.example.httpRest.repository.FacultyRepository;
import com.example.httpRest.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student getById(long id) {
        return studentRepository.findById(id).orElseThrow(DataNotFoundException::new);
    }

    public Collection<Student> getAll() {
        return studentRepository.findAll();
    }

    public Collection<Student> getByAge(int age) {
        return studentRepository.findAllByAge(age);
    }

    public Student create(Student student) {
        return studentRepository.save(student);
    }

    public Student update(Long id, Student student) {
        Student exsitingStudent = studentRepository.findById(id).orElseThrow(DataNotFoundException::new);
        Optional.ofNullable(student.getName()).ifPresent(exsitingStudent::setName);
        Optional.ofNullable(student.getAge()).ifPresent(exsitingStudent::setAge);
        return studentRepository.save(exsitingStudent);
    }

    public Student remove(Long id) {
        Student existingStudent = studentRepository.findById(id).orElseThrow(DataNotFoundException::new);
        studentRepository.delete(existingStudent);
        return existingStudent;
    }

    public Collection<Student> getByAgeBetween(int min, int max) {
        return studentRepository.findAllByAgeBetween(min, max);
    }

    public Collection<Student> getByFacultyId(Long facultyId) {
        return facultyRepository.findById(facultyId)
                .map(Faculty::getStudent)
                .orElseThrow(DataNotFoundException::new);

    }

}
