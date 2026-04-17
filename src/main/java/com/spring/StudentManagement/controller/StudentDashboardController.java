package com.spring.StudentManagement.controller;

import com.spring.StudentManagement.entity.Course;
import com.spring.StudentManagement.entity.Student;
import com.spring.StudentManagement.entity.User;
import com.spring.StudentManagement.service.CourseService;
import com.spring.StudentManagement.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentDashboardController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    // 1. Student Dashboard
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || user.getRole() != User.Role.STUDENT) {
            return "redirect:/login";
        }
        Student student = studentService.findByUserId(user.getId());
        if (student == null) {
            // Redirect to a friendly error page or a profile completion form
            model.addAttribute("errorMessage", "Student profile not found! Please contact admin.");
            return "student-dashboard-error"; // or redirect to /logout, or show a helpful message
        }
        model.addAttribute("studentName", student.getName() != null ? student.getName() : user.getUsername());
        return "student-dashboard";
    }


    // 2. View all available courses (with search/filter)
    @GetMapping("/courses")
    public String listCourses(@RequestParam(required = false) String keyword,
                             HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || user.getRole() != User.Role.STUDENT) {
            return "redirect:/login";
        }
        Student student = studentService.findByUserId(user.getId());
        List<Course> courses = (keyword != null && !keyword.isEmpty())
                ? courseService.searchCourses(keyword)
                : courseService.getAllCourses();
        List<Integer> enrolledCourseIds = studentService.getEnrolledCourseIds(student.getId());
        model.addAttribute("courses", courses);
        model.addAttribute("keyword", keyword);
        model.addAttribute("enrolledCourseIds", enrolledCourseIds);
        return "student-courses";
    }

    // 3. Enroll in a course
    @PostMapping("/enroll")
    public String enrollCourse(@RequestParam int courseId, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || user.getRole() != User.Role.STUDENT) {
            return "redirect:/login";
        }
        Student student = studentService.findByUserId(user.getId());
        studentService.enrollInCourse(student.getId(), courseId);
        return "redirect:/student/courses";
    }

    // 4. View enrolled courses
    @GetMapping("/my-courses")
    public String myCourses(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || user.getRole() != User.Role.STUDENT) {
            return "redirect:/login";
        }
        Student student = studentService.findByUserId(user.getId());
        List<Course> enrolledCourses = studentService.getEnrolledCourses(student.getId());
        model.addAttribute("courses", enrolledCourses);
        return "student-my-courses";
    }

    // 5. Unenroll from a course
    @PostMapping("/unenroll")
    public String unenrollCourse(@RequestParam int courseId, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || user.getRole() != User.Role.STUDENT) {
            return "redirect:/login";
        }
        Student student = studentService.findByUserId(user.getId());
        studentService.unenrollFromCourse(student.getId(), courseId);
        return "redirect:/student/my-courses";
    }

    // 6. Edit profile (show form)
    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || user.getRole() != User.Role.STUDENT) {
            return "redirect:/login";
        }
        Student student = studentService.findByUserId(user.getId());
        model.addAttribute("student", student);
        return "student-profile";
    }

    // 7. Edit profile (handle submission)
    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute Student student, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || user.getRole() != User.Role.STUDENT) {
            return "redirect:/login";
        }
        Student existingStudent = studentService.findByUserId(user.getId());
        existingStudent.setName(student.getName());
        existingStudent.setAge(student.getAge());
        existingStudent.setEmail(student.getEmail());
        studentService.saveStudent(existingStudent);
        return "redirect:/student/dashboard";
    }
}
