package com.skillflow.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Сущность, представляющая студента в системе SkillFlow.
 * <p>
 * Содержит основную информацию о студенте: имя, фамилия, email,
 * дата регистрации, статус и список учебных планов.
 * Отображается на таблицу "students" в базе данных.
 * </p>
 *
 */
@Entity
@Table(name = "students")
public class Student {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_студента")
  private Long id;

  @Column(name = "имя_студента")
  private String firstName;

  @Column(name = "фамилия_студента")
  private String lastName;

  @Column(name = "email_студента", unique = true)
  private String email;

  @Column(name = "дата_регистрации_студента")
  private LocalDateTime registrationDate;

  @Column(name = "статус_студента")
  private String status; // "АКТИВЕН", "ЗАВЕРШИЛ", "ОТЧИСЛЕН", "В_ОЖИДАНИИ"

  @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
  private List<StudyPlan> studyPlans;

  /**
   * Конструктор по умолчанию, необходимый для JPA.
   */
  public Student() {}

  /**
   * Конструктор для создания нового студента.
   *
   * @param firstName имя студента
   * @param lastName фамилия студента
   * @param email email студента
   */
  public Student(String firstName, String lastName, String email) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.registrationDate = LocalDateTime.now();
    this.status = "АКТИВЕН";
  }

  /**
   * Возвращает идентификатор студента.
   *
   * @return идентификатор студента
   */
  public Long getId() { return id; }

  /**
   * Устанавливает идентификатор студента.
   *
   * @param id идентификатор студента
   */
  public void setId(Long id) { this.id = id; }

  /**
   * Возвращает имя студента.
   *
   * @return имя студента
   */
  public String getFirstName() { return firstName; }

  /**
   * Устанавливает имя студента.
   *
   * @param firstName имя студента
   */
  public void setFirstName(String firstName) { this.firstName = firstName; }

  /**
   * Возвращает фамилию студента.
   *
   * @return фамилия студента
   */
  public String getLastName() { return lastName; }

  /**
   * Устанавливает фамилию студента.
   *
   * @param lastName фамилия студента
   */
  public void setLastName(String lastName) { this.lastName = lastName; }

  /**
   * Возвращает email студента.
   *
   * @return email студента
   */
  public String getEmail() { return email; }

  /**
   * Устанавливает email студента.
   *
   * @param email email студента
   */
  public void setEmail(String email) { this.email = email; }

  /**
   * Возвращает дату регистрации студента.
   *
   * @return дата регистрации студента
   */
  public LocalDateTime getRegistrationDate() { return registrationDate; }

  /**
   * Устанавливает дату регистрации студента.
   *
   * @param registrationDate дата регистрации студента
   */
  public void setRegistrationDate(LocalDateTime registrationDate) {
    this.registrationDate = registrationDate;
  }

  /**
   * Возвращает статус студента.
   *
   * @return статус студента (АКТИВЕН, ЗАВЕРШИЛ, ОТЧИСЛЕН, В_ОЖИДАНИИ)
   */
  public String getStatus() { return status; }

  /**
   * Устанавливает статус студента с валидацией.
   * <p>
   * Допустимые значения: АКТИВЕН, ЗАВЕРШИЛ, ОТЧИСЛЕН, В_ОЖИДАНИИ.
   * </p>
   *
   * @param status статус студента
   */
  public void setStatus(String status) {
    if (status != null && java.util.List.of("АКТИВЕН", "ЗАВЕРШИЛ", "ОТЧИСЛЕН", "В_ОЖИДАНИИ").contains(status)) {
      this.status = status;
    }
  }

  /**
   * Возвращает список учебных планов студента.
   *
   * @return список учебных планов студента
   */
  public List<StudyPlan> getStudyPlans() { return studyPlans; }

  /**
   * Устанавливает список учебных планов студента.
   *
   * @param studyPlans список учебных планов студента
   */
  public void setStudyPlans(List<StudyPlan> studyPlans) { this.studyPlans = studyPlans; }
}