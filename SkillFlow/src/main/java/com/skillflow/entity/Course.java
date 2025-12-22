package com.skillflow.entity;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * Сущность, представляющая курс в системе SkillFlow.
 * <p>
 * Содержит информацию о курсе: название, описание, даты проведения,
 * эксперта (преподавателя), статус и связанные объекты (цены, учебные планы).
 * Отображается на таблицу "courses" в базе данных.
 * </p>
 *
 */
@Entity
@Table(name = "courses")
public class Course {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_курса")
  private Long id;

  @NotBlank(message = "Название курса обязательно")
  @Size(min = 3, max = 100, message = "Название курса должно быть от 3 до 100 символов")
  @Column(name = "название_курса")
  private String title;

  @Size(max = 1000, message = "Описание не должно превышать 1000 символов")
  @Column(name = "описание_курса")
  private String description;

  @Column(name = "дата_создания_курса")
  private LocalDateTime creationDate;

  @Column(name = "дата_обновления_курса")
  private LocalDateTime updatedDate;

  @ManyToOne
  @JoinColumn(name = "id_эксперта")
  private Expert expert;
  @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
  private List<CoursePrice> prices;
  @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
  private List<StudyPlan> studyPlans;

  @Column(name = "статус_курса")
  @NotBlank(message = "Статус обязателен")
  private String status; // "АКТИВЕН", "НЕ_АКТИВЕН", "В_РАЗРАБОТКЕ"

  @Column(name = "начало_регистрации")
  private LocalDateTime registrationStart;

  @Column(name = "окончание_регистрации")
  private LocalDateTime registrationEnd;

  @Column(name = "начало_курса")
  private LocalDateTime courseStart;

  @Column(name = "окончание_курса")
  private LocalDateTime courseEnd;
  /**
   * Конструктор по умолчанию, необходимый для JPA.
   */
  public Course() {}
  /**
   * Конструктор для создания нового курса.
   *
   * @param title название курса
   * @param description описание курса
   * @param expert эксперт (преподаватель) курса
   */
  public Course(String title, String description, Expert expert) {
    this.title = title;
    this.description = description;
    this.expert = expert;
    this.creationDate = LocalDateTime.now();
    this.updatedDate = LocalDateTime.now();
    this.status = "АКТИВЕН";
  }

  /**
   * Возвращает идентификатор курса.
   *
   * @return идентификатор курса
   */
  public Long getId() { return id; }
  /**
   * Устанавливает идентификатор курса.
   *
   * @param id идентификатор курса
   */
  public void setId(Long id) { this.id = id; }
  /**
   * Возвращает название курса.
   *
   * @return название курса
   */
  public String getTitle() { return title; }
  /**
   * Устанавливает название курса с валидацией.
   * <p>
   * Название должно содержать минимум 3 символа.
   * </p>
   *
   * @param title название курса
   */
  public void setTitle(String title) {
    if (title != null && title.trim().length() >= 3) {
      this.title = title;
    }
  }
  /**
   * Возвращает описание курса.
   *
   * @return описание курса
   */
  public String getDescription() { return description; }
  /**
   * Устанавливает описание курса с валидацией.
   * <p>
   * Описание не должно превышать 1000 символов.
   * </p>
   *
   * @param description описание курса
   */
  public void setDescription(String description) {
    if (description != null && description.length() <= 1000) {
      this.description = description;
    }
  }
  /**
   * Возвращает дату создания курса.
   *
   * @return дата создания курса
   */
  public LocalDateTime getCreationDate() { return creationDate; }
  /**
   * Устанавливает дату создания курса.
   *
   * @param creationDate дата создания курса
   */
  public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }
  /**
   * Возвращает дату обновления курса.
   *
   * @return дата обновления курса
   */
  public LocalDateTime getUpdatedDate() { return updatedDate; }
  /**
   * Устанавливает дату обновления курса.
   *
   * @param updatedDate дата обновления курса
   */
  public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }
  /**
   * Возвращает эксперта (преподавателя) курса.
   *
   * @return эксперт курса
   */
  public Expert getExpert() { return expert; }
  /**
   * Устанавливает эксперта (преподавателя) курса.
   *
   * @param expert эксперт курса
   */
  public void setExpert(Expert expert) { this.expert = expert; }
  /**
   * Возвращает список цен курса.
   *
   * @return список цен курса
   */
  public List<CoursePrice> getPrices() { return prices; }
  /**
   * Устанавливает список цен курса.
   *
   * @param prices список цен курса
   */
  public void setPrices(List<CoursePrice> prices) { this.prices = prices; }
  /**
   * Возвращает список учебных планов курса.
   *
   * @return список учебных планов курса
   */
  public List<StudyPlan> getStudyPlans() { return studyPlans; }
  /**
   * Устанавливает список учебных планов курса.
   *
   * @param studyPlans список учебных планов курса
   */
  public void setStudyPlans(List<StudyPlan> studyPlans) { this.studyPlans = studyPlans; }
  /**
   * Возвращает статус курса.
   *
   * @return статус курса (АКТИВЕН, НЕ_АКТИВЕН, В_РАЗРАБОТКЕ)
   */
  public String getStatus() { return status; }
  /**
   * Устанавливает статус курса с валидацией.
   * <p>
   * Допустимые значения: АКТИВЕН, НЕ_АКТИВЕН, В_РАЗРАБОТКЕ.
   * </p>
   *
   * @param status статус курса
   */
  public void setStatus(String status) {
    if (status != null && List.of("АКТИВЕН", "НЕ_АКТИВЕН", "В_РАЗРАБОТКЕ").contains(status)) {
      this.status = status;
    }
  }
  /**
   * Возвращает дату начала регистрации на курс.
   *
   * @return дата начала регистрации
   */
  public LocalDateTime getRegistrationStart() { return registrationStart; }
  /**
   * Устанавливает дату начала регистрации на курс.
   *
   * @param registrationStart дата начала регистрации
   */
  public void setRegistrationStart(LocalDateTime registrationStart) {
    this.registrationStart = registrationStart;
  }
  /**
   * Возвращает дату окончания регистрации на курс.
   *
   * @return дата окончания регистрации
   */
  public LocalDateTime getRegistrationEnd() {
    return registrationEnd;
  }
  /**
   * Устанавливает дату окончания регистрации с валидацией.
   * <p>
   * Дата окончания должна быть после даты начала (если обе даты указаны).
   * </p>
   *
   * @param registrationEnd дата окончания регистрации
   */
  public void setRegistrationEnd(LocalDateTime registrationEnd) {
    if (registrationEnd == null || registrationStart == null ||
        registrationEnd.isAfter(registrationStart)) {
      this.registrationEnd = registrationEnd;
    }
  }
  /**
   * Возвращает дату начала курса.
   *
   * @return дата начала курса
   */
  public LocalDateTime getCourseStart() { return courseStart; }
  /**
   * Устанавливает дату начала курса.
   *
   * @param courseStart дата начала курса
   */
  public void setCourseStart(LocalDateTime courseStart) {
    this.courseStart = courseStart;
  }
  /**
   * Возвращает дату окончания курса.
   *
   * @return дата окончания курса
   */
  public LocalDateTime getCourseEnd() { return courseEnd; }
  /**
   * Устанавливает дату окончания курса с валидацией.
   * <p>
   * Дата окончания должна быть после даты начала (если обе даты указаны).
   * </p>
   *
   * @param courseEnd дата окончания курса
   */
  public void setCourseEnd(LocalDateTime courseEnd) {
    if (courseEnd == null || courseStart == null ||
        courseEnd.isAfter(courseStart)) {
      this.courseEnd = courseEnd;
    }
  }
  /**
   * Проверяет корректность дат регистрации.
   * <p>
   * Дата окончания регистрации должна быть после даты начала.
   * Если какая-либо дата не указана, возвращает true.
   * </p>
   *
   * @return true если даты регистрации корректны, иначе false
   */
  public boolean isRegistrationDatesValid() {
    if (registrationStart == null || registrationEnd == null) {
      return true;
    }
    return registrationEnd.isAfter(registrationStart);
  }
  /**
   * Проверяет корректность дат проведения курса.
   * <p>
   * Дата окончания курса должна быть после даты начала.
   * Если какая-либо дата не указана, возвращает true.
   * </p>
   *
   * @return true если даты проведения курса корректны, иначе false
   */
  public boolean isCourseDatesValid() {
    if (courseStart == null || courseEnd == null) {
      return true;
    }
    return courseEnd.isAfter(courseStart);
  }

  /**
   * Проверяет корректность всех дат курса.
   * <p>
   * Проверяет:
   * 1. Даты регистрации (начало должно быть до окончания)
   * 2. Даты проведения курса (начало должно быть до окончания)
   * 3. Логическую последовательность: регистрация → начало курса → окончание курса
   * </p>
   *
   * @return true если все даты корректны, иначе false
   */
  public boolean areAllDatesValid() {
    return isRegistrationDatesValid() &&
        isCourseDatesValid() &&
        isCourseAfterRegistration();
  }

  /**
   * Проверяет, что дата начала курса после даты окончания регистрации.
   * <p>
   * Если дата окончания регистрации не указана, считается что регистрация
   * открыта постоянно и проверка пройдена.
   * </p>
   *
   * @return true если дата начала курса после даты окончания регистрации или дата окончания регистрации не указана
   */
  public boolean isCourseAfterRegistration() {
    if (registrationEnd == null || courseStart == null) {
      return true; // Если одна из дат не указана, считаем валидным
    }
    return courseStart.isAfter(registrationEnd);
  }

  /**
   * Проверяет, что дата начала курса после текущей даты (если курс активен).
   * <p>
   * Для активных курсов дата начала должна быть в будущем или настоящем.
   * </p>
   *
   * @return true если дата начала курса корректна для активного курса
   */
  public boolean isCourseStartDateValidForActiveStatus() {
    if (!"АКТИВЕН".equals(status) || courseStart == null) {
      return true; // Проверка только для активных курсов с указанной датой начала
    }
    return !courseStart.isBefore(LocalDateTime.now());
  }

  /**
   * Возвращает все ошибки валидации дат в виде списка сообщений.
   * <p>
   * Используется для отображения пользователю всех проблем с датами сразу.
   * </p>
   *
   * @return список сообщений об ошибках валидации
   */
  public List<String> getValidationErrors() {
    List<String> errors = new ArrayList<>();

    // Валидация дат регистрации
    if (registrationStart != null && registrationEnd != null &&
        !registrationEnd.isAfter(registrationStart)) {
      errors.add("Дата окончания регистрации должна быть после даты начала регистрации");
    }

    // Валидация дат курса
    if (courseStart != null && courseEnd != null &&
        !courseEnd.isAfter(courseStart)) {
      errors.add("Дата окончания курса должна быть после даты начала курса");
    }

    // Валидация последовательности: регистрация → курс
    if (registrationEnd != null && courseStart != null &&
        !courseStart.isAfter(registrationEnd)) {
      errors.add("Дата начала курса должна быть после даты окончания регистрации");
    }

    // Валидация для активного статуса
    if ("АКТИВЕН".equals(status) && courseStart != null &&
        courseStart.isBefore(LocalDateTime.now())) {
      errors.add("Для активного курса дата начала не может быть в прошлом");
    }

    return errors;
  }
}