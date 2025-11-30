package com.skillflow.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "students")
public class Student {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_студента")
  private Long id;

  @NotBlank(message = "Имя обязательно")
  @Column(name = "имя_студента")
  private String firstName;

  @NotBlank(message = "Фамилия обязательна")
  @Column(name = "фамилия_студента")
  private String lastName;

  @Email(message = "Некорректный email")
  @NotBlank(message = "Email обязателен")
  @Column(name = "email_студента")
  private String email;

  @Column(name = "дата_регистрации_студента")
  private LocalDateTime registrationDate;

  @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
  private List<StudyPlan> studyPlans;

  public Student() {}

  public Student(String firstName, String lastName, String email) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.registrationDate = LocalDateTime.now();
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getFirstName() { return firstName; }
  public void setFirstName(String firstName) { this.firstName = firstName; }

  public String getLastName() { return lastName; }
  public void setLastName(String lastName) { this.lastName = lastName; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public LocalDateTime getRegistrationDate() { return registrationDate; }
  public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }

  public List<StudyPlan> getStudyPlans() { return studyPlans; }
  public void setStudyPlans(List<StudyPlan> studyPlans) { this.studyPlans = studyPlans; }
}