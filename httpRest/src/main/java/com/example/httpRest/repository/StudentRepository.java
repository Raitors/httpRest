package com.example.httpRest.repository;

import com.example.httpRest.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByAge(Integer age);

    List<Student> findAllByAgeBetween(int min, int max);

    @Query(value = "select count(*) from student", nativeQuery = true)
    long countStudent();

    @Query(value = "select avg(age) from student", nativeQuery = true)
    double averageAge();

    @Query(value = "select * from student order by id desc limit :quantity", nativeQuery = true)
    List<Student> findLastStudent(@Param("quantity") int n);


}
