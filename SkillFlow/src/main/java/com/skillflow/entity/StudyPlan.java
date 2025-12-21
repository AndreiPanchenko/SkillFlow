package com.skillflow.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Сущность, представляющая учебный план в системе SkillFlow.
 * <p>
 * Связывает студента с курсом и содержит информацию о датах обучения
 * и статусе прохождения. Отображается на таблицу "study_plans" в базе данных.
 * </p>
 *
 */
@Entity
@Table(name = "study_plans")
public class StudyPlan {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_плана")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "id_студента")
  @NotNull(message = "Студент обязателен")
  private Student student;

  @ManyToOne
  @JoinColumn(name = "id_курса")
  @NotNull(message = "Курс обязателен")
  private Course course;

  @Column(name = "дата_начала")
  private LocalDateTime startDate;

  @Column(name = "плановая_дата_окончания")
  private LocalDateTime plannedEndDate;

  @Column(name = "статус_плана")
  private String status;

  /**
   * Конструктор по умолчанию, необходимый для JPA.
   */
  public StudyPlan() {}

  /**
   * Конструктор для создания нового учебного плана.
   *
   * @param student студент
   * @param course курс
   */
  public StudyPlan(Student student, Course course) {
    this.student = student;
    this.course = course;
    this.startDate = LocalDateTime.now();
    this.status = "активен";
  }

  /**
   * Возвращает идентификатор учебного плана.
   *
   * @return идентификатор учебного плана
   */
  public Long getId() { return id; }

  /**
   * Устанавливает идентификатор учебного плана.
   *
   * @param id идентификатор учебного плана
   */
  public void setId(Long id) { this.id = id; }

  /**
   * Возвращает студента, связанного с учебным планом.
   *
   * @return студент
   */
  public Student getStudent() { return student; }

  /**
   * Устанавливает студента для учебного плана.
   *
   * @param student студент
   */
  public void setStudent(Student student) { this.student = student; }

  /**
   * Возвращает курс, связанный с учебным планом.
   *
   * @return курс
   */
  public Course getCourse() { return course; }

  /**
   * Устанавливает курс для учебного плана.
   *
   * @param course курс
   */
  public void setCourse(Course course) { this.course = course; }

  /**
   * Возвращает дату начала обучения по плану.
   *
   * @return дата начала обучения
   */
  public LocalDateTime getStartDate() { return startDate; }

  /**
   * Устанавливает дату начала обучения по плану.
   *
   * @param startDate дата начала обучения
   */
  public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

  /**
   * Возвращает плановую дату окончания обучения.
   *
   * @return плановая дата окончания обучения
   */
  public LocalDateTime getPlannedEndDate() { return plannedEndDate; }

  /**
   * Устанавливает плановую дату окончания обучения.
   *
   * @param plannedEndDate плановая дата окончания обучения
   */
  public void setPlannedEndDate(LocalDateTime plannedEndDate) { this.plannedEndDate = plannedEndDate; }

  /**
   * Возвращает статус учебного плана.
   *
   * @return статус учебного плана (активен, завершен, приостановлен, отменен)
   */
  public String getStatus() { return status; }

  /**
   * Устанавливает статус учебного плана.
   *
   * @param status статус учебного плана
   */
  public void setStatus(String status) { this.status = status; }
}