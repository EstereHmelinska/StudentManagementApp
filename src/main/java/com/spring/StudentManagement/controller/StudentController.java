package com.spring.StudentManagement.controller;

import com.spring.StudentManagement.entity.Student;
import com.spring.StudentManagement.entity.User;
import com.spring.StudentManagement.service.StudentService;
import com.spring.StudentManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/students")
public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String listStudents(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        return "students";
    }

    @GetMapping("/new")
    public String showAddStudentForm(Model model) {
        Student student = new Student();
        student.setUser(new User()); // Important for nested binding!
        model.addAttribute("student", student);
        model.addAttribute("isNew", true);
        return "student-form";
    }

    @PostMapping("/save")
    public String saveStudent(@ModelAttribute("student") Student student,
                             @RequestParam("isNew") boolean isNew) {
        if (isNew) {
            // For new user, password is required
            if (student.getUser().getPassword() == null || student.getUser().getPassword().isBlank()) {
                throw new RuntimeException("Password is required");
            }
            if (student.getUser().getRole() == null) {
                student.getUser().setRole(User.Role.STUDENT);
            }
            userService.save(student.getUser());
        } else {
            // Edit existing user
            User existingUser = userService.findById(student.getUser().getId());
            if (existingUser == null) throw new RuntimeException("User not found");
            existingUser.setUsername(student.getUser().getUsername());
            // Only set password if it's non-blank!
            if (student.getUser().getPassword() != null && !student.getUser().getPassword().isBlank()) {
                existingUser.setPassword(student.getUser().getPassword());
            }
            // Optionally set other user fields here
            userService.save(existingUser);
            student.setUser(existingUser);
        }
        studentService.saveStudent(student);
        return "redirect:/admin/students";
    }

    @GetMapping("/edit/{id}")
    public String showEditStudentForm(@PathVariable int id, Model model) {
        Optional<Student> studentOpt = studentService.getStudentById(id);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            if (student.getUser() == null) student.setUser(new User());
            model.addAttribute("student", student);
            model.addAttribute("isNew", false);
            return "student-form";
        }
        return "redirect:/admin/students";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable int id) {
        studentService.deleteStudent(id);
        return "redirect:/admin/students";
    }
}
