package com.skillflow.controller;

import com.skillflow.entity.Course;
import com.skillflow.entity.Expert;
import com.skillflow.service.CourseService;
import com.skillflow.service.ExpertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/courses")
public class CourseController {

  @Autowired
  private CourseService courseService;

  @Autowired
  private ExpertService expertService;

  @GetMapping
  public String getAllCourses(Model model) {
    model.addAttribute("courses", courseService.getAllCourses());
    return "courses/list";
  }

  @GetMapping("/{id}")
  public String getCourseById(@PathVariable Long id, Model model) {
    Optional<Course> course = courseService.getCourseById(id);
    if (course.isPresent()) {
      model.addAttribute("course", course.get());
      return "courses/view";
    }
    return "redirect:/courses";
  }

  @GetMapping("/new")
  public String showCourseForm(Model model) {
    model.addAttribute("course", new Course());
    List<Expert> experts = expertService.getAllExperts();
    model.addAttribute("experts", experts);
    return "courses/form";
  }

  @PostMapping
  public String saveCourse(@Valid @ModelAttribute Course course,
      BindingResult result,
      @RequestParam Long expertId,
      Model model) {
    if (result.hasErrors()) {
      model.addAttribute("experts", expertService.getAllExperts());
      return "courses/form";
    }

    Optional<Expert> expert = expertService.getExpertById(expertId);
    if (expert.isPresent()) {
      course.setExpert(expert.get());
      courseService.saveCourse(course);
      return "redirect:/courses";
    } else {
      model.addAttribute("error", "Эксперт не найден");
      model.addAttribute("experts", expertService.getAllExperts());
      return "courses/form";
    }
  }

  @GetMapping("/{id}/edit")
  public String editCourse(@PathVariable Long id, Model model) {
    Optional<Course> course = courseService.getCourseById(id);
    if (course.isPresent()) {
      model.addAttribute("course", course.get());
      model.addAttribute("experts", expertService.getAllExperts());
      return "courses/form";
    }
    return "redirect:/courses";
  }

  @GetMapping("/{id}/delete")
  public String deleteCourse(@PathVariable Long id) {
    courseService.deleteCourse(id);
    return "redirect:/courses";
  }
}