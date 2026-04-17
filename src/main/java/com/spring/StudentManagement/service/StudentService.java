package com.spring.StudentManagement.service;

import com.spring.StudentManagement.entity.Student;
import com.spring.StudentManagement.entity.Course;
import com.spring.StudentManagement.repository.StudentRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EntityManager entityManager;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public Optional<Student> getStudentById(int id) {
        return studentRepository.findById(id);
    }

    public void deleteStudent(int id) {
        studentRepository.deleteById(id);
    }

    public Student findByUserId(int userId) {
        return studentRepository.findByUserId(userId);
    }

    public List<Integer> getEnrolledCourseIds(int studentId) {
        return studentRepository.findEnrolledCourseIdsByStudentId(studentId);
    }

    public List<Course> getEnrolledCourses(int studentId) {
        return studentRepository.findEnrolledCoursesByStudentId(studentId);
    }

    @Transactional
    public void enrollInCourse(int studentId, int courseId) {
        entityManager.createNativeQuery(
                "INSERT IGNORE INTO student_courses (student_id, course_id) VALUES (?, ?)")
                .setParameter(1, studentId)
                .setParameter(2, courseId)
                .executeUpdate();
    }

    @Transactional
    public void unenrollFromCourse(int studentId, int courseId) {
        entityManager.createNativeQuery(
                "DELETE FROM student_courses WHERE student_id = ? AND course_id = ?")
                .setParameter(1, studentId)
                .setParameter(2, courseId)
                .executeUpdate();
    }
}
