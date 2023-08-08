package com.example.httpRest.service;

import com.example.httpRest.exception.DataNotFoundException;
import com.example.httpRest.model.Student;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final HashMap<Long, Student> map = new HashMap<>();
    private Long COUNTER = 1L;

    public Student getById(long id) {
        return map.get(id);
    }

    public Collection<Student> getAll() {
        return map.values();
    }

    public Collection<Student> getByAge(int age) {
        return map.values().stream()
                .filter(s -> s.getAge() == age)
                .collect(Collectors.toList());
    }

    public Student create(Student student) {
        Long nextId = COUNTER++;
        student.setId(nextId);
        map.put(student.getId(), student);
        return student;
    }

    public Student update(Long id, Student student) {
        if (!map.containsKey(id)) {
            throw new DataNotFoundException();
        }
        Student exsitingStudent = map.get(id);
        exsitingStudent.setName(student.getName());
        exsitingStudent.setAge(student.getAge());
        return exsitingStudent;
    }

    public void delete(Long id) {
        if (map.remove(id) == null) {
            throw new DataNotFoundException();
        }
    }
}
