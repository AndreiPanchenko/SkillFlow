package com.skillflow.service;
import com.skillflow.entity.Course;
import com.skillflow.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
/**
 * Сервисный класс для бизнес-логики работы с курсами.
 * <p>
 * Содержит методы для управления курсами: создание, обновление, удаление,
 * поиск и валидация. Все методы выполняются в транзакционном контексте.
 * </p>
 *
 */
@Service
@Transactional
public class CourseService {
  @Autowired
  private CourseRepository courseRepository;
  /**
   * Получает список всех курсов.
   *
   * @return список всех курсов в системе
   */
  public List<Course> getAllCourses() {
    return courseRepository.findAll();
  }
  /**
   * Находит курс по идентификатору.
   *
   * @param id идентификатор курса
   * @return Optional с курсом, если найден
   */
  public Optional<Course> getCourseById(Long id) {
    return courseRepository.findById(id);
  }
  /**
   * Сохраняет или обновляет курс.
   * <p>
   * Перед сохранением выполняет валидацию данных курса.
   * </p>
   *
   * @param course объект курса для сохранения
   * @return сохраненный курс
   * @throws IllegalArgumentException если данные курса не прошли валидацию
   */
  public Course saveCourse(Course course) {
    // Валидация перед сохранением
    validateCourse(course);

    // Дополнительная валидация дат
    List<String> dateErrors = course.getValidationErrors();
    if (!dateErrors.isEmpty()) {
      throw new IllegalArgumentException(String.join("; ", dateErrors));
    }

    return courseRepository.save(course);
  }
  /**
   * Удаляет курс по идентификатору.
   *
   * @param id идентификатор курса для удаления
   */
  public void deleteCourse(Long id) {
    courseRepository.deleteById(id);
  }
  /**
   * Находит курсы по идентификатору эксперта.
   *
   * @param expertId идентификатор эксперта
   * @return список курсов, которые ведет указанный эксперт
   */
  public List<Course> getCoursesByExpert(Long expertId) {
    return courseRepository.findByExpertId(expertId);
  }
  /**
   * Находит курсы по статусу.
   *
   * @param status статус курса (АКТИВЕН, НЕ_АКТИВЕН, В_РАЗРАБОТКЕ)
   * @return список курсов с указанным статусом
   */
  public List<Course> getCoursesByStatus(String status) {
    return courseRepository.findByStatus(status);
  }
  /**
   * Находит курсы, созданные в указанном диапазоне дат.
   *
   * @param start начальная дата диапазона
   * @param end конечная дата диапазона
   * @return список курсов, созданных в указанном диапазоне
   */
  public List<Course> getCoursesByDateRange(LocalDateTime start, LocalDateTime end) {
    return courseRepository.findByCreationDateBetween(start, end);
  }
  /**
   * Получает активные курсы, на которые открыта регистрация.
   *
   * @return список активных курсов с действующей регистрацией
   */
  public List<Course> getActiveCourses() {
    return courseRepository.findByStatusAndRegistrationEndAfter("АКТИВЕН", LocalDateTime.now());
  }
  /**
   * Обновляет статус курса.
   *
   * @param courseId идентификатор курса
   * @param status новый статус (АКТИВЕН, НЕ_АКТИВЕН, В_РАЗРАБОТКЕ)
   * @throws RuntimeException если курс не найден
   */
  public void updateCourseStatus(Long courseId, String status) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new RuntimeException("Курс не найден"));
    if (List.of("АКТИВЕН", "НЕ_АКТИВЕН", "В_РАЗРАБОТКЕ").contains(status)) {
      course.setStatus(status);
      course.setUpdatedDate(LocalDateTime.now());
      courseRepository.save(course);
    }
  }
  /**
   * Проверяет, возможна ли регистрация на курс.
   * <p>
   * Регистрация возможна если:
   * 1. Курс активен
   * 2. Текущее время после даты начала регистрации (если она указана)
   * 3. Текущее время до даты окончания регистрации (если она указана)
   * </p>
   *
   * @param courseId идентификатор курса
   * @return true если регистрация на курс возможна, иначе false
   * @throws RuntimeException если курс не найден
   */
  public boolean canRegisterForCourse(Long courseId) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new RuntimeException("Курс не найден"));
    LocalDateTime now = LocalDateTime.now();
    return "АКТИВЕН".equals(course.getStatus()) &&
        (course.getRegistrationStart() == null || now.isAfter(course.getRegistrationStart())) &&
        (course.getRegistrationEnd() == null || now.isBefore(course.getRegistrationEnd()));
  }
  /**
   * Валидирует данные курса перед сохранением.
   * <p>
   * Проверяет:
   * 1. Название курса (минимум 3 символа)
   * 2. Наличие эксперта
   * 3. Корректность статуса
   * 4. Логику дат регистрации и проведения курса
   * </p>
   *
   * @param course курс для валидации
   * @throws IllegalArgumentException если данные не прошли валидацию
   */
  private void validateCourse(Course course) {
    if (course.getTitle() == null || course.getTitle().trim().length() < 3) {
      throw new IllegalArgumentException("Название курса должно содержать минимум 3 символа");
    }
    if (course.getExpert() == null) {
      throw new IllegalArgumentException("Курс должен иметь эксперта");
    }
    if (course.getStatus() == null || !List.of("АКТИВЕН", "НЕ_АКТИВЕН", "В_РАЗРАБОТКЕ").contains(course.getStatus())) {
      throw new IllegalArgumentException("Неверный статус курса");
    }
    // Валидация дат
    if (course.getRegistrationStart() != null && course.getRegistrationEnd() != null &&
        !course.getRegistrationEnd().isAfter(course.getRegistrationStart())) {
      throw new IllegalArgumentException("Дата окончания регистрации должна быть после даты начала");
    }
    if (course.getCourseStart() != null && course.getCourseEnd() != null &&
        !course.getCourseEnd().isAfter(course.getCourseStart())) {
      throw new IllegalArgumentException("Дата окончания курса должна быть после даты начала");
    }
  }
}