package com.spring.StudentManagement.repository;

import com.spring.StudentManagement.entity.Student;
import com.spring.StudentManagement.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    // Find student by linked user id
    Student findByUserId(int userId);

    // Get list of enrolled course IDs for a student
    @Query(value = "SELECT course_id FROM student_courses WHERE student_id = :studentId", nativeQuery = true)
    List<Integer> findEnrolledCourseIdsByStudentId(@Param("studentId") int studentId);

    // Get list of Course objects a student is enrolled in
    @Query(value = "SELECT c.* FROM course c " +
            "INNER JOIN student_courses sc ON c.id = sc.course_id " +
            "WHERE sc.student_id = :studentId", nativeQuery = true)
    List<Course> findEnrolledCoursesByStudentId(@Param("studentId") int studentId);
}
