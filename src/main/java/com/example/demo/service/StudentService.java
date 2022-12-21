package com.example.demo.service;

import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.time.Period.between;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student addNewStudent(Student student) {
        Optional<Student> studentEmail = studentRepository.findStudentByEmail(student.getEmail());
        if (studentEmail.isPresent()) {
            throw new IllegalStateException("Email already taken");
        }
        int currentTimeStamp = (int) (System.currentTimeMillis() / 1000L);
        student.setCreatedAt(currentTimeStamp);
        int age = between(student.getDob(), LocalDate.now()).getYears();
        student.setAge(age);
        return studentRepository.save(student);
    }

    public Student getStudentByID(Integer id) {
        Optional<Student> student = studentRepository.findById(id.longValue());
        if (student.isEmpty()) {
            throw new RuntimeException("Student with given ID does not exist");
        }
        return student.get();
    }

    public Student deleteStudentByID(Integer id) throws Exception {
        Optional<Student> student = studentRepository.findById(id.longValue());
        if (student.isEmpty()) {
            throw new Exception("Student with given ID does not exist");
        }
        Student currentStudent = student.get();
        studentRepository.delete(currentStudent);
        return currentStudent;
    }

    public Page<Student> getStudentPaginated(int page, int size, String name, String email) {
        return studentRepository.studentFilter(name, email,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
    }
}
