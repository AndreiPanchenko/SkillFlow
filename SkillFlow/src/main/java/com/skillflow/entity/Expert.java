package com.skillflow.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Сущность, представляющая эксперта (преподавателя) в системе SkillFlow.
 * <p>
 * Содержит информацию об эксперте: имя, фамилия, email, биография,
 * специализация и список курсов, которые он ведет.
 * Отображается на таблицу "experts" в базе данных.
 * </p>
 *
 */
@Entity
@Table(name = "experts")
public class Expert {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_эксперта")
  private Long id;

  @NotBlank(message = "Имя обязательно")
  @Column(name = "имя_эксперта")
  private String firstName;

  @NotBlank(message = "Фамилия обязательна")
  @Column(name = "фамилия_эксперта")
  private String lastName;

  @Email(message = "Некорректный email")
  @NotBlank(message = "Email обязателен")
  @Column(name = "email_эксперта", unique = true)
  private String email;

  @Column(name = "биография_эксперта")
  private String biography;

  @NotBlank(message = "Специализация обязательна")
  @Column(name = "специализация_эксперта")
  private String specialization;

  @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL)
  private List<Course> courses;

  /**
   * Конструктор по умолчанию, необходимый для JPA.
   */
  public Expert() {}

  /**
   * Конструктор для создания нового эксперта.
   *
   * @param firstName имя эксперта
   * @param lastName фамилия эксперта
   * @param email email эксперта
   * @param biography биография эксперта
   * @param specialization специализация эксперта
   */
  public Expert(String firstName, String lastName, String email, String biography, String specialization) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.biography = biography;
    this.specialization = specialization;
  }

  /**
   * Возвращает идентификатор эксперта.
   *
   * @return идентификатор эксперта
   */
  public Long getId() { return id; }

  /**
   * Устанавливает идентификатор эксперта.
   *
   * @param id идентификатор эксперта
   */
  public void setId(Long id) { this.id = id; }

  /**
   * Возвращает имя эксперта.
   *
   * @return имя эксперта
   */
  public String getFirstName() { return firstName; }

  /**
   * Устанавливает имя эксперта.
   *
   * @param firstName имя эксперта
   */
  public void setFirstName(String firstName) { this.firstName = firstName; }

  /**
   * Возвращает фамилию эксперта.
   *
   * @return фамилия эксперта
   */
  public String getLastName() { return lastName; }

  /**
   * Устанавливает фамилию эксперта.
   *
   * @param lastName фамилия эксперта
   */
  public void setLastName(String lastName) { this.lastName = lastName; }

  /**
   * Возвращает email эксперта.
   *
   * @return email эксперта
   */
  public String getEmail() { return email; }

  /**
   * Устанавливает email эксперта.
   *
   * @param email email эксперта
   */
  public void setEmail(String email) { this.email = email; }

  /**
   * Возвращает биографию эксперта.
   *
   * @return биография эксперта
   */
  public String getBiography() { return biography; }

  /**
   * Устанавливает биографию эксперта.
   *
   * @param biography биография эксперта
   */
  public void setBiography(String biography) { this.biography = biography; }

  /**
   * Возвращает специализацию эксперта.
   *
   * @return специализация эксперта
   */
  public String getSpecialization() { return specialization; }

  /**
   * Устанавливает специализацию эксперта.
   *
   * @param specialization специализация эксперта
   */
  public void setSpecialization(String specialization) { this.specialization = specialization; }

  /**
   * Возвращает список курсов, которые ведет эксперт.
   *
   * @return список курсов эксперта
   */
  public List<Course> getCourses() { return courses; }

  /**
   * Устанавливает список курсов эксперта.
   *
   * @param courses список курсов эксперта
   */
  public void setCourses(List<Course> courses) { this.courses = courses; }
}