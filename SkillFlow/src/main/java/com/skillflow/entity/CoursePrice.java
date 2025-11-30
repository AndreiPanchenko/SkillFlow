package com.skillflow.entity;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "course_prices")
public class CoursePrice {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "id_курса")
  @NotNull(message = "Курс обязателен")
  private Course course;

  @Column(name = "дата_начала_действия_цены")
  @NotNull(message = "Дата начала действия обязательна")
  private LocalDateTime startDate;

  @Column(name = "цена_курса")
  @DecimalMin(value = "0.0", message = "Цена должна быть положительной")
  private BigDecimal price;

  @Column(name = "дата_окончания_действия_цены")
  private LocalDateTime endDate;

  public CoursePrice() {}

  public CoursePrice(Course course, LocalDateTime startDate, BigDecimal price) {
    this.course = course;
    this.startDate = startDate;
    this.price = price;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public Course getCourse() { return course; }
  public void setCourse(Course course) { this.course = course; }

  public LocalDateTime getStartDate() { return startDate; }
  public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

  public BigDecimal getPrice() { return price; }
  public void setPrice(BigDecimal price) { this.price = price; }

  public LocalDateTime getEndDate() { return endDate; }
  public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
}