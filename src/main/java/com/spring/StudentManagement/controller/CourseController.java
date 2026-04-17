package com.spring.StudentManagement.controller;

import com.spring.StudentManagement.entity.Course;
import com.spring.StudentManagement.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public String listCourses(@RequestParam(required = false) String keyword, Model model) {
        List<Course> courses;
        if (keyword != null && !keyword.isEmpty()) {
            courses = courseService.searchCourses(keyword);
        } else {
            courses = courseService.getAllCourses();
        }
        model.addAttribute("courses", courses);
        model.addAttribute("keyword", keyword);
        return "courses";
    }

    @GetMapping("/new")
    public String showAddCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "course-form";
    }

    @PostMapping("/save")
    public String saveCourse(@ModelAttribute Course course) {
        courseService.saveCourse(course);
        return "redirect:/admin/courses";
    }

    @GetMapping("/edit/{id}")
    public String showEditCourseForm(@PathVariable int id, Model model) {
        Optional<Course> courseOpt = courseService.getCourseById(id);
        if (courseOpt.isPresent()) {
            model.addAttribute("course", courseOpt.get());
            return "course-form";
        }
        return "redirect:/admin/courses";
    }

    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable int id) {
        courseService.deleteCourse(id);
        return "redirect:/admin/courses";
    }
}

