package com.skillflow.repository;

import com.skillflow.entity.CoursePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с ценами курсов в базе данных.
 * <p>
 * Предоставляет методы для выполнения операций с сущностью CoursePrice.
 * Наследует JpaRepository, что дает базовые CRUD операции.
 * </p>
 *
 */
@Repository
public interface CoursePriceRepository extends JpaRepository<CoursePrice, Long> {

  /**
   * Находит цены курса по идентификатору курса.
   *
   * @param courseId идентификатор курса
   * @return список цен указанного курса
   */
  List<CoursePrice> findByCourseId(Long courseId);
}