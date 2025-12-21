package com.skillflow.controller;

import com.skillflow.entity.StudyPlan;
import com.skillflow.service.StudyPlanService;
import com.skillflow.service.StudentService;
import com.skillflow.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

/**
 * Контроллер для управления учебными планами в системе SkillFlow.
 * <p>
 * Учебный план связывает студента с курсом и содержит информацию
 * о датах обучения и статусе. Этот контроллер обрабатывает создание,
 * редактирование, удаление и просмотр учебных планов.
 * </p>
 *
 */
@Controller
@RequestMapping("/studyplans")
public class StudyPlanController {

  @Autowired
  private StudyPlanService studyPlanService;

  @Autowired
  private StudentService studentService;

  @Autowired
  private CourseService courseService;

  /**
   * Отображает список всех учебных планов.
   *
   * @param model модель для передачи данных в представление
   * @return имя шаблона страницы со списком учебных планов
   */
  @GetMapping
  public String getAllStudyPlans(Model model) {
    try {
      model.addAttribute("studyPlans", studyPlanService.getAllStudyPlans());
      model.addAttribute("statuses", List.of("активен", "завершен", "приостановлен", "отменен"));
      return "studyplans/list";
    } catch (Exception e) {
      model.addAttribute("errorMessage", "Ошибка при загрузке учебных планов: " + e.getMessage());
      return "error/error";
    }
  }

  /**
   * Отображает детальную информацию о конкретном учебном плане.
   *
   * @param id идентификатор учебного плана
   * @param model модель для передачи данных в представление
   * @return имя шаблона страницы просмотра учебного плана или страницу ошибки
   */
  @GetMapping("/{id}")
  public String getStudyPlanById(@PathVariable Long id, Model model) {
    try {
      Optional<StudyPlan> studyPlan = studyPlanService.getStudyPlanById(id);
      if (studyPlan.isPresent()) {
        model.addAttribute("studyPlan", studyPlan.get());
        return "studyplans/view";
      }
      model.addAttribute("errorMessage", "Учебный план не найден");
      return "error/error";
    } catch (Exception e) {
      model.addAttribute("errorMessage", "Ошибка при загрузке учебного плана: " + e.getMessage());
      return "error/error";
    }
  }

  /**
   * Отображает форму для создания нового учебного плана.
   *
   * @param model модель для передачи данных в представление
   * @return имя шаблона формы создания учебного плана
   */
  @GetMapping("/new")
  public String showStudyPlanForm(Model model) {
    try {
      StudyPlan studyPlan = new StudyPlan();
      studyPlan.setStatus("активен");
      studyPlan.setStartDate(LocalDateTime.now());

      model.addAttribute("studyPlan", studyPlan);
      model.addAttribute("students", studentService.getAllStudents());
      model.addAttribute("courses", courseService.getAllCourses());
      model.addAttribute("statuses", List.of("активен", "завершен", "приостановлен", "отменен"));
      return "studyplans/form";
    } catch (Exception e) {
      model.addAttribute("errorMessage", "Ошибка при открытии формы: " + e.getMessage());
      return "error/error";
    }
  }

  /**
   * Сохраняет или обновляет учебный план.
   * <p>
   * Обрабатывает данные из формы. Если передан id, обновляет существующий учебный план,
   * иначе создает новый. Проверяет, что студент не записан дважды на один курс.
   * </p>
   *
   * @param id идентификатор учебного плана (опционально, для редактирования)
   * @param studentId идентификатор студента
   * @param courseId идентификатор курса
   * @param status статус учебного плана
   * @param startDate дата начала обучения
   * @param plannedEndDate плановая дата окончания обучения
   * @param model модель для передачи данных в представление
   * @return редирект на список учебных планов или возврат к форме при ошибке
   */
  @PostMapping
  public String saveStudyPlan(
      @RequestParam(required = false) Long id,
      @RequestParam Long studentId,
      @RequestParam Long courseId,
      @RequestParam String status,
      @RequestParam(required = false) String startDate,
      @RequestParam(required = false) String plannedEndDate,
      Model model) {

    try {
      StudyPlan studyPlan;

      if (id != null) {
        // Редактирование существующего учебного плана
        Optional<StudyPlan> existingStudyPlan = studyPlanService.getStudyPlanById(id);
        if (existingStudyPlan.isPresent()) {
          studyPlan = existingStudyPlan.get();
        } else {
          model.addAttribute("error", "Учебный план не найден");
          model.addAttribute("students", studentService.getAllStudents());
          model.addAttribute("courses", courseService.getAllCourses());
          model.addAttribute("statuses", List.of("активен", "завершен", "приостановлен", "отменен"));
          return "studyplans/form";
        }
      } else {
        // Создание нового учебного плана
        studyPlan = new StudyPlan();
      }

      // Установка студента
      studentService.getStudentById(studentId).ifPresent(studyPlan::setStudent);

      // Установка курса
      courseService.getCourseById(courseId).ifPresent(studyPlan::setCourse);

      studyPlan.setStatus(status);

      // Установка дат
      try {
        if (startDate != null && !startDate.isEmpty()) {
          studyPlan.setStartDate(LocalDateTime.parse(startDate + "T00:00:00"));
        } else if (id == null) {
          studyPlan.setStartDate(LocalDateTime.now());
        }

        if (plannedEndDate != null && !plannedEndDate.isEmpty()) {
          studyPlan.setPlannedEndDate(LocalDateTime.parse(plannedEndDate + "T00:00:00"));
        }
      } catch (DateTimeParseException e) {
        // Если дата некорректная, оставляем текущие значения
      }

      studyPlanService.saveStudyPlan(studyPlan);
      return "redirect:/studyplans";

    } catch (Exception e) {
      model.addAttribute("error", "Ошибка при сохранении: " + e.getMessage());
      model.addAttribute("students", studentService.getAllStudents());
      model.addAttribute("courses", courseService.getAllCourses());
      model.addAttribute("statuses", List.of("активен", "завершен", "приостановлен", "отменен"));

      // Восстанавливаем данные формы
      StudyPlan tempStudyPlan = new StudyPlan();
      tempStudyPlan.setStatus(status);
      if (id != null) tempStudyPlan.setId(id);
      model.addAttribute("studyPlan", tempStudyPlan);

      return "studyplans/form";
    }
  }

  /**
   * Отображает форму для редактирования существующего учебного плана.
   *
   * @param id идентификатор учебного плана для редактирования
   * @param model модель для передачи данных в представление
   * @return имя шаблона формы редактирования учебного плана или страницу ошибки
   */
  @GetMapping("/{id}/edit")
  public String editStudyPlan(@PathVariable Long id, Model model) {
    try {
      Optional<StudyPlan> studyPlan = studyPlanService.getStudyPlanById(id);
      if (studyPlan.isPresent()) {
        model.addAttribute("studyPlan", studyPlan.get());
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("statuses", List.of("активен", "завершен", "приостановлен", "отменен"));
        return "studyplans/form";
      }
      model.addAttribute("errorMessage", "Учебный план не найден");
      return "error/error";
    } catch (Exception e) {
      model.addAttribute("errorMessage", "Ошибка при загрузке учебного плана: " + e.getMessage());
      return "error/error";
    }
  }

  /**
   * Удаляет учебный план по идентификатору.
   *
   * @param id идентификатор учебного плана для удаления
   * @return редирект на список учебных планов
   */
  @GetMapping("/{id}/delete")
  public String deleteStudyPlan(@PathVariable Long id) {
    try {
      studyPlanService.deleteStudyPlan(id);
      return "redirect:/studyplans";
    } catch (Exception e) {
      return "redirect:/studyplans";
    }
  }

  /**
   * Обновляет статус учебного плана.
   *
   * @param id идентификатор учебного плана
   * @param status новый статус
   * @return редирект на страницу учебного плана
   */
  @PostMapping("/{id}/status")
  public String updateStudyPlanStatus(@PathVariable Long id,
      @RequestParam String status) {
    try {
      studyPlanService.updateStudyPlanStatus(id, status);
      return "redirect:/studyplans/" + id;
    } catch (Exception e) {
      return "redirect:/studyplans";
    }
  }

  /**
   * Отображает учебные планы конкретного студента.
   *
   * @param studentId идентификатор студента
   * @param model модель для передачи данных в представление
   * @return имя шаблона страницы со списком учебных планов студента
   */
  @GetMapping("/student/{studentId}")
  public String getStudyPlansByStudent(@PathVariable Long studentId, Model model) {
    try {
      model.addAttribute("studyPlans", studyPlanService.getStudyPlansByStudent(studentId));
      model.addAttribute("student", studentService.getStudentById(studentId).orElse(null));
      return "studyplans/list-by-student";
    } catch (Exception e) {
      model.addAttribute("errorMessage", "Ошибка при загрузке учебных планов: " + e.getMessage());
      return "error/error";
    }
  }

  /**
   * Отображает учебные планы конкретного курса.
   *
   * @param courseId идентификатор курса
   * @param model модель для передачи данных в представление
   * @return имя шаблона страницы со списком учебных планов курса
   */
  @GetMapping("/course/{courseId}")
  public String getStudyPlansByCourse(@PathVariable Long courseId, Model model) {
    try {
      model.addAttribute("studyPlans", studyPlanService.getStudyPlansByCourse(courseId));
      model.addAttribute("course", courseService.getCourseById(courseId).orElse(null));
      return "studyplans/list-by-course";
    } catch (Exception e) {
      model.addAttribute("errorMessage", "Ошибка при загрузке учебных планов: " + e.getMessage());
      return "error/error";
    }
  }
}