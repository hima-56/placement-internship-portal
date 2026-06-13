// service/StudentService.java
package com.placement.portal.service;

import com.placement.portal.dto.*;
import com.placement.portal.exception.*;
import com.placement.portal.model.Student;
import com.placement.portal.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentResponse register(StudentRequest request) {
        if (studentRepository.existsByEmail(request.getEmail()))
            throw new DuplicateResourceException("Student already registered: " + request.getEmail());

        Student student = Student.builder()
                .name(request.getName())
                .email(request.getEmail())
                .branch(request.getBranch())
                .cgpa(request.getCgpa())
                .skills(request.getSkills())
                .resumeUrl(request.getResumeUrl())
                .build();

        return toResponse(studentRepository.save(student));
    }

    public List<StudentResponse> getAll() {
        return studentRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public StudentResponse getById(Long id) {
        return toResponse(findById(id));
    }

    public StudentResponse update(Long id, StudentRequest request) {
        Student student = findById(id);
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setBranch(request.getBranch());
        student.setCgpa(request.getCgpa());
        student.setSkills(request.getSkills());
        student.setResumeUrl(request.getResumeUrl());
        return toResponse(studentRepository.save(student));
    }

    public void delete(Long id) {
        studentRepository.delete(findById(id));
    }

    public Student findById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    private StudentResponse toResponse(Student s) {
        return StudentResponse.builder()
                .id(s.getId())
                .name(s.getName())
                .email(s.getEmail())
                .branch(s.getBranch())
                .cgpa(s.getCgpa())
                .skills(s.getSkills())
                .resumeUrl(s.getResumeUrl())
                .createdAt(s.getCreatedAt())
                .build();
    }
}