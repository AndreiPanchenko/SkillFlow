package com.skillflow.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "courses")
public class Course {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_курса")
  private Long id;

  @NotBlank(message = "Название курса обязательно")
  @Column(name = "название_курса")
  private String title;

  @Column(name = "описание_курса")
  private String description;

  @Column(name = "дата_создания_курса")
  private LocalDateTime creationDate;

  @ManyToOne
  @JoinColumn(name = "id_эксперта")
  private Expert expert;

  @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
  private List<CoursePrice> prices;

  @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
  private List<StudyPlan> studyPlans;

  public Course() {}

  public Course(String title, String description, Expert expert) {
    this.title = title;
    this.description = description;
    this.expert = expert;
    this.creationDate = LocalDateTime.now();
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }

  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }

  public LocalDateTime getCreationDate() { return creationDate; }
  public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

  public Expert getExpert() { return expert; }
  public void setExpert(Expert expert) { this.expert = expert; }

  public List<CoursePrice> getPrices() { return prices; }
  public void setPrices(List<CoursePrice> prices) { this.prices = prices; }

  public List<StudyPlan> getStudyPlans() { return studyPlans; }
  public void setStudyPlans(List<StudyPlan> studyPlans) { this.studyPlans = studyPlans; }
}