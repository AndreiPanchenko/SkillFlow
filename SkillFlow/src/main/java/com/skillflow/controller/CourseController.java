package com.skillflow.controller;
import com.skillflow.entity.Course;
import com.skillflow.entity.Expert;
import com.skillflow.service.CourseService;
import com.skillflow.service.ExpertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 * Контроллер для управления курсами в системе SkillFlow.
 * <p>
 * Обрабатывает HTTP-запросы, связанные с курсами: создание, редактирование,
 * удаление, просмотр и управление статусами. Использует Thymeleaf для рендеринга
 * HTML-страниц.
 * </p>
 *
 */
@Controller
@RequestMapping("/courses")
public class CourseController {
  private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
  /** Форматтер для преобразования дат из строк в формате yyyy-MM-dd */
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  @Autowired
  private CourseService courseService;
  @Autowired
  private ExpertService expertService;
  /**
   * Отображает список всех курсов с возможностью фильтрации.
   * <p>
   * Поддерживает фильтрацию по статусу и диапазону дат.
   * Если параметры фильтрации не указаны, возвращает все курсы.
   * </p>
   *
   * @param model модель для передачи данных в представление
   * @param status статус для фильтрации (опционально)
   * @param startDate начальная дата для фильтрации (опционально)
   * @param endDate конечная дата для фильтрации (опционально)
   * @return имя шаблона страницы со списком курсов
   */
  @GetMapping
  public String getAllCourses(Model model,
      @RequestParam(value = "status", required = false) String status,
      @RequestParam(value = "startDate", required = false) String startDate,
      @RequestParam(value = "endDate", required = false) String endDate) {
    List<Course> courses;
    if (status != null && !status.isEmpty()) {
      // Фильтрация по статусу
      courses = courseService.getCoursesByStatus(status);
    } else if (startDate != null && endDate != null) {
      // Фильтрация по диапазону дат
      try {
        LocalDateTime start = LocalDate.parse(startDate, DATE_FORMATTER).atStartOfDay();
        LocalDateTime end = LocalDate.parse(endDate, DATE_FORMATTER).atTime(23, 59, 59);
        courses = courseService.getCoursesByDateRange(start, end);
      } catch (DateTimeParseException e) {
        model.addAttribute("error", "Неверный формат даты");
        courses = courseService.getAllCourses();
      }
    } else {
      // Без фильтрации - все курсы
      courses = courseService.getAllCourses();
    }
    model.addAttribute("courses", courses);
    model.addAttribute("statuses", List.of("АКТИВЕН", "НЕ_АКТИВЕН", "В_РАЗРАБОТКЕ"));
    model.addAttribute("experts", expertService.getAllExperts());
    return "courses/list";
  }
  /**
   * Отображает детальную информацию о конкретном курсе.
   *
   * @param id идентификатор курса
   * @param model модель для передачи данных в представление
   * @return имя шаблона страницы просмотра курса или редирект при ошибке
   */
  @GetMapping("/{id}")
  public String getCourseById(@PathVariable Long id, Model model) {
    Optional<Course> course = courseService.getCourseById(id);
    if (course.isPresent()) {
      model.addAttribute("course", course.get());
      model.addAttribute("isActive", course.get().getStatus().equals("АКТИВЕН"));
      model.addAttribute("canRegister", courseService.canRegisterForCourse(id));
      return "courses/view";
    }
    model.addAttribute("error", "Курс не найден");
    return "redirect:/courses";
  }
  /**
   * Отображает форму для создания нового курса.
   *
   * @param model модель для передачи данных в представление
   * @return имя шаблона формы создания курса
   */
  @GetMapping("/new")
  public String showCourseForm(Model model) {
    Course course = new Course();
    course.setStatus("В_РАЗРАБОТКЕ");
    model.addAttribute("course", course);
    model.addAttribute("experts", expertService.getAllExperts());
    model.addAttribute("statuses", List.of("АКТИВЕН", "НЕ_АКТИВЕН", "В_РАЗРАБОТКЕ"));
    return "courses/form";
  }
  /**
   * Сохраняет или обновляет курс.
   * <p>
   * Обрабатывает данные из формы. Если передан id, обновляет существующий курс,
   * иначе создает новый. Проводит валидацию дат и логики бизнес-правил.
   * </p>
   *
   * @param id идентификатор курса (опционально, для редактирования)
   * @param title название курса
   * @param description описание курса
   * @param expertId идентификатор эксперта (преподавателя)
   * @param status статус курса
   * @param registrationStart дата начала регистрации
   * @param registrationEnd дата окончания регистрации
   * @param courseStart дата начала курса
   * @param courseEnd дата окончания курса
   * @param model модель для передачи данных в представление
   * @return редирект на список курсов или возврат к форме при ошибке
   */
  @PostMapping
  public String saveCourse(
      @RequestParam(required = false) Long id,
      @RequestParam String title,
      @RequestParam(required = false) String description,
      @RequestParam Long expertId,
      @RequestParam String status,
      @RequestParam(required = false) String registrationStart,
      @RequestParam(required = false) String registrationEnd,
      @RequestParam(required = false) String courseStart,
      @RequestParam(required = false) String courseEnd,
      Model model) {
    logger.info("Сохранение курса: название={}, expertId={}", title, expertId);
    Course course;
    if (id != null) {
      // Редактирование существующего курса
      Optional<Course> existingCourse = courseService.getCourseById(id);
      if (existingCourse.isPresent()) {
        course = existingCourse.get();
        course.setTitle(title);
        course.setDescription(description);
        course.setStatus(status);
        course.setUpdatedDate(LocalDateTime.now());
      } else {
        model.addAttribute("error", "Курс не найден");
        model.addAttribute("experts", expertService.getAllExperts());
        model.addAttribute("statuses", List.of("АКТИВЕН", "НЕ_АКТИВЕН", "В_РАЗРАБОТКЕ"));
        return "courses/form";
      }
    } else {
      // Создание нового курса
      course = new Course();
      course.setTitle(title);
      course.setDescription(description);
      course.setStatus(status);
      course.setCreationDate(LocalDateTime.now());
      course.setUpdatedDate(LocalDateTime.now());
    }
    // Установка дат с валидацией формата
    try {
      if (registrationStart != null && !registrationStart.isEmpty()) {
        course.setRegistrationStart(LocalDate.parse(registrationStart, DATE_FORMATTER).atStartOfDay());
      } else {
        course.setRegistrationStart(null);
      }
      if (registrationEnd != null && !registrationEnd.isEmpty()) {
        course.setRegistrationEnd(LocalDate.parse(registrationEnd, DATE_FORMATTER).atTime(23, 59, 59));
      } else {
        course.setRegistrationEnd(null);
      }
      if (courseStart != null && !courseStart.isEmpty()) {
        course.setCourseStart(LocalDate.parse(courseStart, DATE_FORMATTER).atStartOfDay());
      } else {
        course.setCourseStart(null);
      }
      if (courseEnd != null && !courseEnd.isEmpty()) {
        course.setCourseEnd(LocalDate.parse(courseEnd, DATE_FORMATTER).atTime(23, 59, 59));
      } else {
        course.setCourseEnd(null);
      }
    } catch (DateTimeParseException e) {
      model.addAttribute("error", "Неверный формат даты. Используйте формат ГГГГ-ММ-ДД");
      model.addAttribute("course", course);
      model.addAttribute("experts", expertService.getAllExperts());
      model.addAttribute("statuses", List.of("АКТИВЕН", "НЕ_АКТИВЕН", "В_РАЗРАБОТКЕ"));
      return "courses/form";
    }
    // === ДОБАВЛЯЕМ РАСШИРЕННУЮ ВАЛИДАЦИЮ ДАТ ===
    List<String> dateErrors = course.getValidationErrors();

    if (!dateErrors.isEmpty()) {
      // Добавляем все ошибки в модель для отображения
      model.addAttribute("dateErrors", dateErrors);
      model.addAttribute("course", course);
      model.addAttribute("experts", expertService.getAllExperts());
      model.addAttribute("statuses", List.of("АКТИВЕН", "НЕ_АКТИВЕН", "В_РАЗРАБОТКЕ"));

      // Формируем общее сообщение об ошибке
      String errorMessage = "Обнаружены ошибки в датах:\n" +
          String.join("\n", dateErrors);
      model.addAttribute("error", errorMessage);

      return "courses/form";
    }

    // Установка эксперта
    Optional<Expert> expert = expertService.getExpertById(expertId);
    if (expert.isPresent()) {
      course.setExpert(expert.get());
    } else {
      model.addAttribute("error", "Эксперт не найден");
      model.addAttribute("course", course);
      model.addAttribute("experts", expertService.getAllExperts());
      model.addAttribute("statuses", List.of("АКТИВЕН", "НЕ_АКТИВЕН", "В_РАЗРАБОТКЕ"));
      return "courses/form";
    }
    try {
      Course savedCourse = courseService.saveCourse(course);
      logger.info("Курс успешно сохранен: id={}, название={}", savedCourse.getId(), savedCourse.getTitle());
      return "redirect:/courses";
    } catch (Exception e) {
      logger.error("Ошибка при сохранении курса", e);
      model.addAttribute("error", "Ошибка при сохранении курса: " + e.getMessage());
      model.addAttribute("course", course);
      model.addAttribute("experts", expertService.getAllExperts());
      model.addAttribute("statuses", List.of("АКТИВЕН", "НЕ_АКТИВЕН", "В_РАЗРАБОТКЕ"));
      return "courses/form";
    }
  }
  /**
   * Отображает форму для редактирования существующего kursа.
   *
   * @param id идентификатор курса для редактирования
   * @param model модель для передачи данных в представление
   * @return имя шаблона формы редактирования курса или редирект при ошибке
   */
  @GetMapping("/{id}/edit")
  public String editCourse(@PathVariable Long id, Model model) {
    Optional<Course> course = courseService.getCourseById(id);
    if (course.isPresent()) {
      model.addAttribute("course", course.get());
      model.addAttribute("experts", expertService.getAllExperts());
      model.addAttribute("statuses", List.of("АКТИВЕН", "НЕ_АКТИВЕН", "В_РАЗРАБОТКЕ"));
      return "courses/form";
    }
    return "redirect:/courses";
  }
  /**
   * Удаляет курс по идентификатору.
   *
   * @param id идентификатор курса для удаления
   * @return редирект на список курсов
   */
  @GetMapping("/{id}/delete")
  public String deleteCourse(@PathVariable Long id) {
    courseService.deleteCourse(id);
    return "redirect:/courses";
  }
  /**
   * Обновляет статус курса.
   *
   * @param id идентификатор курса
   * @param status новый статус
   * @return редирект на страницу курса
   */
  @PostMapping("/{id}/status")
  public String updateCourseStatus(@PathVariable Long id,
      @RequestParam String status) {
    courseService.updateCourseStatus(id, status);
    return "redirect:/courses/" + id;
  }
  /**
   * Активирует курс (устанавливает статус "АКТИВЕН").
   *
   * @param id идентификатор курса
   * @return редирект на страницу kursа
   */
  @GetMapping("/{id}/activate")
  public String activateCourse(@PathVariable Long id) {
    courseService.updateCourseStatus(id, "АКТИВЕН");
    return "redirect:/courses/" + id;
  }
  /**
   * Деактивирует курс (устанавливает статус "НЕ_АКТИВЕН").
   *
   * @param id идентификатор курса
   * @return редирект на страницу курса
   */
  @GetMapping("/{id}/deactivate")
  public String deactivateCourse(@PathVariable Long id) {
    courseService.updateCourseStatus(id, "НЕ_АКТИВЕН");
    return "redirect:/courses/" + id;
  }
}