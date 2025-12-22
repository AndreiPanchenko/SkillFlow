package com.skillflow.entity;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Сущность, представляющая цену курса в системе SkillFlow.
 * <p>
 * Содержит информацию о цене курса на определенный период времени.
 * Позволяет устанавливать разные цены для курса в разные периоды.
 * Отображается на таблицу "course_prices" в базе данных.
 * </p>
 *
 */
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

  /**
   * Конструктор по умолчанию, необходимый для JPA.
   */
  public CoursePrice() {}

  /**
   * Конструктор для создания новой цены курса.
   *
   * @param course курс
   * @param startDate дата начала действия цены
   * @param price цена курса
   */
  public CoursePrice(Course course, LocalDateTime startDate, BigDecimal price) {
    this.course = course;
    this.startDate = startDate;
    this.price = price;
  }

  /**
   * Возвращает идентификатор цены курса.
   *
   * @return идентификатор цены курса
   */
  public Long getId() { return id; }

  /**
   * Устанавливает идентификатор цены курса.
   *
   * @param id идентификатор цены курса
   */
  public void setId(Long id) { this.id = id; }

  /**
   * Возвращает курс, к которому относится цена.
   *
   * @return курс
   */
  public Course getCourse() { return course; }

  /**
   * Устанавливает курс для цены.
   *
   * @param course курс
   */
  public void setCourse(Course course) { this.course = course; }

  /**
   * Возвращает дату начала действия цены.
   *
   * @return дата начала действия цены
   */
  public LocalDateTime getStartDate() { return startDate; }

  /**
   * Устанавливает дату начала действия цены.
   *
   * @param startDate дата начала действия цены
   */
  public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

  /**
   * Возвращает цену курса.
   *
   * @return цена курса
   */
  public BigDecimal getPrice() { return price; }

  /**
   * Устанавливает цену курса.
   *
   * @param price цена курса
   */
  public void setPrice(BigDecimal price) { this.price = price; }

  /**
   * Возвращает дату окончания действия цены.
   *
   * @return дата окончания действия цены
   */
  public LocalDateTime getEndDate() { return endDate; }

  /**
   * Устанавливает дату окончания действия цены.
   *
   * @param endDate дата окончания действия цены
   */
  public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
}