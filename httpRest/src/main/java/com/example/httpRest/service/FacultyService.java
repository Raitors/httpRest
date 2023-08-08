package com.example.httpRest.service;

import com.example.httpRest.exception.DataNotFoundException;
import com.example.httpRest.model.Faculty;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final HashMap<Long, Faculty> map = new HashMap<>();
    private Long COUNTER = 1L;

    public Faculty getById(long id) {
        return map.get(id);
    }

    public Collection<Faculty> getAll() {
        return map.values();
    }

    public Collection<Faculty> getByColor(String color) {
        return map.values().stream()
                .filter(c -> c.getColor().equalsIgnoreCase(color))
                .collect(Collectors.toList());
    }

    public Faculty create(Faculty faculty) {
        Long nextId = COUNTER++;
        faculty.setId(nextId);
        map.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty update(Long id, Faculty faculty) {
        if (!map.containsKey(id)) {
            throw new DataNotFoundException();
        }
        Faculty exsitingFaculty = map.get(id);
        exsitingFaculty.setName(faculty.getName());
        exsitingFaculty.setColor(faculty.getColor());
        return exsitingFaculty;
    }

    public void delete(Long id) {
        if (map.remove(id) == null) {
            throw new DataNotFoundException();
        }
    }
}
