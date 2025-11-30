package com.skillflow.controller;

import com.skillflow.entity.Student;
import com.skillflow.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/students")
public class StudentController {

  @Autowired
  private StudentService studentService;

  @GetMapping
  public String getAllStudents(Model model) {
    model.addAttribute("students", studentService.getAllStudents());
    return "students/list";
  }

  @GetMapping("/{id}")
  public String getStudentById(@PathVariable Long id, Model model) {
    Optional<Student> student = studentService.getStudentById(id);
    if (student.isPresent()) {
      model.addAttribute("student", student.get());
      return "students/view";
    }
    return "redirect:/students";
  }

  @GetMapping("/new")
  public String showStudentForm(Model model) {
    model.addAttribute("student", new Student());
    return "students/form";
  }

  @PostMapping
  public String saveStudent(@Valid @ModelAttribute Student student, BindingResult result) {
    if (result.hasErrors()) {
      return "students/form";
    }
    studentService.saveStudent(student);
    return "redirect:/students";
  }

  @GetMapping("/{id}/edit")
  public String editStudent(@PathVariable Long id, Model model) {
    Optional<Student> student = studentService.getStudentById(id);
    if (student.isPresent()) {
      model.addAttribute("student", student.get());
      return "students/form";
    }
    return "redirect:/students";
  }

  @GetMapping("/{id}/delete")
  public String deleteStudent(@PathVariable Long id) {
    studentService.deleteStudent(id);
    return "redirect:/students";
  }
}