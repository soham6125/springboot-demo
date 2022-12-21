package com.example.demo.controller;

import com.example.demo.model.Student;
import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping(path = "/api/v1")
public class StudentController {

    private final StudentService studentService; // taking reference of service

    @Autowired
    public StudentController(StudentService studentService) { // creating constructor for studentService
        this.studentService = studentService;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.healthCheck());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable("id") Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getStudentByID(id));
    }

    @GetMapping("/paginated")
    public ResponseEntity<Map<String, Object>> findStudentPaginated(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "5") int size,
                                                                    @RequestParam(defaultValue = "") String name,
                                                                    @RequestParam(defaultValue = "") String email) {
        Map<String, Object> response = new HashMap<>();
        Page<Student> studentPaginated = studentService.getStudentPaginated(page, size, name, email);
        response.put("data", studentPaginated.getContent());
        response.put("currentPage", studentPaginated.getNumber());
        response.put("totalItems", studentPaginated.getTotalElements());
        response.put("totalPages", studentPaginated.getTotalPages());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/")
    public ResponseEntity<Student> registerStudent(@Valid @RequestBody Student student) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.addNewStudent(student));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable("id") Integer id) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.deleteStudentByID(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable("id") Long id, @Valid @RequestBody Student student) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.editStudent(id, student));
    }
}
