package com.example.demo.repository;

import com.example.demo.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("select s from Student s where s.email = ?1")
    Optional<Student> findStudentByEmail(String email);

    String FILTER_STUDENT = "select b from Student b where UPPER(b.name) like CONCAT('%',UPPER(?1),'%')" +
            "and UPPER(b.email) like CONCAT('%',UPPER(?2),'%') ";
    @Query(FILTER_STUDENT)
    Page<Student> studentFilter(String name, String email, PageRequest createdAt);

}
