package com.example.httpRest.service;

import com.example.httpRest.exception.DataNotFoundException;
import com.example.httpRest.model.Faculty;
import com.example.httpRest.repository.FacultyRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty getById(long id) {
        return facultyRepository.findById(id).orElseThrow(DataNotFoundException::new);
    }

    public Collection<Faculty> getAll() {
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getByColor(String color) {
        return facultyRepository.findAllByColor(color);
    }

    public Collection<Faculty> getByColorOrName(String color, String name) {
        return facultyRepository.findAllByColorIgnoreCaseOrNameIgnoreCase(color, name);
    }

    public Faculty create(Faculty faculty) {
        return facultyRepository.save(faculty);

    }

    public Faculty update(Long id, Faculty faculty) {
        Faculty existingFaculty = facultyRepository.findById(id).
                orElseThrow(DataNotFoundException::new);
        if (faculty.getName() != null) {
            existingFaculty.setName(faculty.getName());
        }
        if (faculty.getColor() != null) {
            existingFaculty.setColor(faculty.getColor());
        }
        return facultyRepository.save(existingFaculty);

    }

    public Faculty remove(Long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(DataNotFoundException::new);
        facultyRepository.delete(faculty);
        return faculty;
    }


    public Faculty getByStudentId(Long studentId) {
        return facultyRepository.findByStudent_Id(studentId).orElseThrow(DataNotFoundException::new);
    }
}
