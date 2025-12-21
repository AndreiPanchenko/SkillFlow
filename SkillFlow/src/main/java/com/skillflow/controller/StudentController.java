package com.skillflow.controller;

import com.skillflow.entity.Student;
import com.skillflow.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

/**
 * Контроллер для управления студентами в системе SkillFlow.
 * <p>
 * Обрабатывает все операции со студентами: создание, редактирование,
 * удаление, просмотр и управление статусами.
 * </p>
 *
 */
@Controller
@RequestMapping("/students")
public class StudentController {

  @Autowired
  private StudentService studentService;

  /**
   * Отображает список всех студентов.
   *
   * @param model модель для передачи данных в представление
   * @return имя шаблона страницы со списком студентов
   */
  @GetMapping
  public String getAllStudents(Model model) {
    try {
      model.addAttribute("students", studentService.getAllStudents());
      model.addAttribute("statuses", List.of("АКТИВЕН", "ЗАВЕРШИЛ", "ОТЧИСЛЕН", "В_ОЖИДАНИИ"));
      return "students/list";
    } catch (Exception e) {
      model.addAttribute("errorMessage", "Ошибка при загрузке студентов: " + e.getMessage());
      return "error/error";
    }
  }

  /**
   * Отображает детальную информацию о конкретном студенте.
   *
   * @param id идентификатор студента
   * @param model модель для передачи данных в представление
   * @return имя шаблона страницы просмотра студента или страницу ошибки
   */
  @GetMapping("/{id}")
  public String getStudentById(@PathVariable Long id, Model model) {
    try {
      Optional<Student> student = studentService.getStudentById(id);
      if (student.isPresent()) {
        model.addAttribute("student", student.get());
        return "students/view";
      }
      model.addAttribute("errorMessage", "Студент не найден");
      return "error/error";
    } catch (Exception e) {
      model.addAttribute("errorMessage", "Ошибка при загрузке студента: " + e.getMessage());
      return "error/error";
    }
  }

  /**
   * Отображает форму для создания нового студента.
   *
   * @param model модель для передачи данных в представление
   * @return имя шаблона формы создания студента
   */
  @GetMapping("/new")
  public String showStudentForm(Model model) {
    try {
      Student student = new Student();
      student.setStatus("АКТИВЕН");
      student.setRegistrationDate(LocalDateTime.now());
      model.addAttribute("student", student);
      model.addAttribute("statuses", List.of("АКТИВЕН", "ЗАВЕРШИЛ", "ОТЧИСЛЕН", "В_ОЖИДАНИИ"));
      return "students/form";
    } catch (Exception e) {
      model.addAttribute("errorMessage", "Ошибка при открытии формы: " + e.getMessage());
      return "error/error";
    }
  }

  /**
   * Сохраняет или обновляет студента.
   * <p>
   * Обрабатывает данные из формы. Если передан id, обновляет существующего студента,
   * иначе создает нового.
   * </p>
   *
   * @param id идентификатор студента (опционально, для редактирования)
   * @param firstName имя студента
   * @param lastName фамилия студента
   * @param email email студента
   * @param status статус студента
   * @param registrationDate дата регистрации
   * @param model модель для передачи данных в представление
   * @return редирект на список студентов или возврат к форме при ошибке
   */
  @PostMapping
  public String saveStudent(
      @RequestParam(required = false) Long id,
      @RequestParam String firstName,
      @RequestParam String lastName,
      @RequestParam String email,
      @RequestParam String status,
      @RequestParam(required = false) String registrationDate,
      Model model) {

    try {
      Student student;

      if (id != null) {
        // Редактирование существующего студента
        Optional<Student> existingStudent = studentService.getStudentById(id);
        if (existingStudent.isPresent()) {
          student = existingStudent.get();
          student.setFirstName(firstName);
          student.setLastName(lastName);
          student.setEmail(email);
          student.setStatus(status);
        } else {
          model.addAttribute("error", "Студент не найден");
          model.addAttribute("statuses", List.of("АКТИВЕН", "ЗАВЕРШИЛ", "ОТЧИСЛЕН", "В_ОЖИДАНИИ"));
          return "students/form";
        }
      } else {
        // Создание нового студента
        student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setEmail(email);
        student.setStatus(status);

        // Установка даты регистрации
        if (registrationDate != null && !registrationDate.isEmpty()) {
          try {
            student.setRegistrationDate(LocalDateTime.parse(registrationDate + "T00:00:00"));
          } catch (DateTimeParseException e) {
            student.setRegistrationDate(LocalDateTime.now());
          }
        } else {
          student.setRegistrationDate(LocalDateTime.now());
        }
      }

      studentService.saveStudent(student);
      return "redirect:/students";

    } catch (Exception e) {
      model.addAttribute("error", "Ошибка при сохранении: " + e.getMessage());
      model.addAttribute("statuses", List.of("АКТИВЕН", "ЗАВЕРШИЛ", "ОТЧИСЛЕН", "В_ОЖИДАНИИ"));
      // Создаем временный объект студента для повторного заполнения формы
      Student tempStudent = new Student();
      tempStudent.setFirstName(firstName);
      tempStudent.setLastName(lastName);
      tempStudent.setEmail(email);
      tempStudent.setStatus(status);
      if (id != null) tempStudent.setId(id);
      model.addAttribute("student", tempStudent);
      return "students/form";
    }
  }

  /**
   * Отображает форму для редактирования существующего студента.
   *
   * @param id идентификатор студента для редактирования
   * @param model модель для передачи данных в представление
   * @return имя шаблона формы редактирования студента или страницу ошибки
   */
  @GetMapping("/{id}/edit")
  public String editStudent(@PathVariable Long id, Model model) {
    try {
      Optional<Student> student = studentService.getStudentById(id);
      if (student.isPresent()) {
        model.addAttribute("student", student.get());
        model.addAttribute("statuses", List.of("АКТИВЕН", "ЗАВЕРШИЛ", "ОТЧИСЛЕН", "В_ОЖИДАНИИ"));
        return "students/form";
      }
      model.addAttribute("errorMessage", "Студент не найден");
      return "error/error";
    } catch (Exception e) {
      model.addAttribute("errorMessage", "Ошибка при загрузке студента: " + e.getMessage());
      return "error/error";
    }
  }

  /**
   * Удаляет студента по идентификатору.
   *
   * @param id идентификатор студента для удаления
   * @return редирект на список студентов
   */
  @GetMapping("/{id}/delete")
  public String deleteStudent(@PathVariable Long id) {
    try {
      studentService.deleteStudent(id);
      return "redirect:/students";
    } catch (Exception e) {
      return "redirect:/students";
    }
  }

  /**
   * Обновляет статус студента.
   *
   * @param id идентификатор студента
   * @param status новый статус
   * @return редирект на страницу студента
   */
  @PostMapping("/{id}/status")
  public String updateStudentStatus(@PathVariable Long id,
      @RequestParam String status) {
    try {
      studentService.updateStudentStatus(id, status);
      return "redirect:/students/" + id;
    } catch (Exception e) {
      return "redirect:/students";
    }
  }
}