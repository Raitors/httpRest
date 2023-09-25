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
import java.util.stream.Collectors;

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

    public List<String> getAllStartsWithA() {
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .filter(s -> s.startsWith("A"))
                .sorted()
                .collect(Collectors.toList());
    }

    public double getAverageAge() {
        return studentRepository.findAll().stream()
                .mapToInt(Student::getAge)
                .average()
                .orElseThrow(DataNotFoundException::new);
    }

    public void printAsync() {
        List<Student> all = studentRepository.findAll();

        System.out.println(all.get(0));
        System.out.println(all.get(1));

        Thread t1 = new Thread(() -> {
            System.out.println(all.get(2));
            System.out.println(all.get(3));
        });
        Thread t2 = new Thread(() -> {
            System.out.println(all.get(4));
            System.out.println(all.get(5));
        });
        t1.start();
        t2.start();
    }

    public void printSync() {
        List<Student> all = studentRepository.findAll();

        printSync(all.get(0));
        printSync(all.get(1));

        Thread t1 = new Thread(() -> {
            printSync(all.get(2));
            printSync(all.get(3));
        });
        Thread t2 = new Thread(() -> {
            printSync(all.get(4));
            printSync(all.get(5));
        });
        t1.start();
        t2.start();
    }

    private synchronized void printSync(Student student) {
        System.out.println(student);
    }
}
