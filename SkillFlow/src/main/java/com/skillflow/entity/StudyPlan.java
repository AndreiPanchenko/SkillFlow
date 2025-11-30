package com.skillflow.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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

  public StudyPlan() {}

  public StudyPlan(Student student, Course course) {
    this.student = student;
    this.course = course;
    this.startDate = LocalDateTime.now();
    this.status = "активен";
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public Student getStudent() { return student; }
  public void setStudent(Student student) { this.student = student; }

  public Course getCourse() { return course; }
  public void setCourse(Course course) { this.course = course; }

  public LocalDateTime getStartDate() { return startDate; }
  public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

  public LocalDateTime getPlannedEndDate() { return plannedEndDate; }
  public void setPlannedEndDate(LocalDateTime plannedEndDate) { this.plannedEndDate = plannedEndDate; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }
}