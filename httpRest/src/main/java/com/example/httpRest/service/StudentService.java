package com.example.httpRest.service;

import com.example.httpRest.exception.DataNotFoundException;
import com.example.httpRest.model.Faculty;
import com.example.httpRest.model.Student;
import com.example.httpRest.repository.FacultyRepository;
import com.example.httpRest.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student getById(long id) {
        logger.info("invoked method getById");
        return studentRepository.findById(id).orElseThrow(DataNotFoundException::new);
    }

    public Collection<Student> getAll() {
        logger.info("invoked method getAll");
        return studentRepository.findAll();
    }

    public Collection<Student> getByAge(int age) {
        logger.info("invoked method getByAge");
        return studentRepository.findAllByAge(age);
    }

    public Student create(Student student) {
        logger.info("invoked method create");
        return studentRepository.save(student);
    }

    public Student update(Long id, Student student) {
        logger.info("invoked method update");
        Student exsitingStudent = studentRepository.findById(id).orElseThrow(DataNotFoundException::new);
        Optional.ofNullable(student.getName()).ifPresent(exsitingStudent::setName);
        Optional.ofNullable(student.getAge()).ifPresent(exsitingStudent::setAge);
        return studentRepository.save(exsitingStudent);
    }

    public Student remove(Long id) {
        logger.info("invoked method remove");
        Student existingStudent = studentRepository.findById(id).orElseThrow(DataNotFoundException::new);
        studentRepository.delete(existingStudent);
        return existingStudent;
    }

    public Collection<Student> getByAgeBetween(int min, int max) {
        logger.info("invoked method getByAgeBetween");
        return studentRepository.findAllByAgeBetween(min, max);
    }

    public Collection<Student> getByFacultyId(Long facultyId) {
        logger.info("invoked method getByFacultyId");
        return facultyRepository.findById(facultyId)
                .map(Faculty::getStudent)
                .orElseThrow(DataNotFoundException::new);

    }

    public long count() {
        logger.info("invoked method count");
        return studentRepository.countStudent();
    }

    public double average() {
        logger.info("invoked method average");
        return studentRepository.averageAge();
    }

    public List<Student> getLastStudents(int quantity) {
        logger.info("invoked method getLastStudents");
        return studentRepository.findLastStudent(quantity);
    }
}
