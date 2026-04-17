package com.spring.StudentManagement.service;

import com.spring.StudentManagement.entity.Course;
import com.spring.StudentManagement.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    public Optional<Course> getCourseById(int id) {
        return courseRepository.findById(id);
    }

    public void deleteCourse(int id) {
        courseRepository.deleteById(id);
    }

    public List<Course> searchCourses(String keyword) {
        return courseRepository.findByNameContainingIgnoreCase(keyword);
    }
}

