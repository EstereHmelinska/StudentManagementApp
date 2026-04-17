package com.spring.StudentManagement.repository;

import com.spring.StudentManagement.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    // For search/filter by course name
    java.util.List<Course> findByNameContainingIgnoreCase(String name);
}

