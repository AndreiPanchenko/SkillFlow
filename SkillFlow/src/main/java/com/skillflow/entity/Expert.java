package com.skillflow.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

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
  @Column(name = "email_эксперта")
  private String email;

  @Column(name = "биография_эксперта")
  private String biography;

  @NotBlank(message = "Специализация обязательна")
  @Column(name = "специализация_эксперта")
  private String specialization;

  @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL)
  private List<Course> courses;

  public Expert() {}

  public Expert(String firstName, String lastName, String email, String biography, String specialization) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.biography = biography;
    this.specialization = specialization;
  }

  // Геттеры и сеттеры
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getFirstName() { return firstName; }
  public void setFirstName(String firstName) { this.firstName = firstName; }

  public String getLastName() { return lastName; }
  public void setLastName(String lastName) { this.lastName = lastName; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getBiography() { return biography; }
  public void setBiography(String biography) { this.biography = biography; }

  public String getSpecialization() { return specialization; }
  public void setSpecialization(String specialization) { this.specialization = specialization; }

  public List<Course> getCourses() { return courses; }
  public void setCourses(List<Course> courses) { this.courses = courses; }
}