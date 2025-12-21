package com.skillflow.repository;

import com.skillflow.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Репозиторий для работы с курсами в базе данных.
 * <p>
 * Предоставляет методы для выполнения операций с сущностью Course.
 * Наследует JpaRepository, что дает базовые CRUD операции.
 * Содержит пользовательские запросы для фильтрации курсов.
 * </p>
 *
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

  /**
   * Находит курсы по части названия (без учета регистра).
   *
   * @param title часть названия курса
   * @return список курсов, содержащих указанную часть в названии
   */
  List<Course> findByTitleContainingIgnoreCase(String title);

  /**
   * Находит курсы по идентификатору эксперта.
   *
   * @param expertId идентификатор эксперта
   * @return список курсов, которые ведет указанный эксперт
   */
  List<Course> findByExpertId(Long expertId);

  /**
   * Находит курсы по статусу.
   *
   * @param status статус курса
   * @return список курсов с указанным статусом
   */
  List<Course> findByStatus(String status);

  /**
   * Находит курсы, созданные в указанном диапазоне дат.
   *
   * @param start начальная дата диапазона
   * @param end конечная дата диапазона
   * @return список курсов, созданных в указанном диапазоне
   */
  List<Course> findByCreationDateBetween(LocalDateTime start, LocalDateTime end);

  /**
   * Находит активные курсы с открытой регистрацией.
   *
   * @param status статус курса (должен быть "АКТИВЕН")
   * @param date текущая дата для проверки окончания регистрации
   * @return список активных курсов, на которые еще открыта регистрация
   */
  List<Course> findByStatusAndRegistrationEndAfter(String status, LocalDateTime date);

  /**
   * Находит курсы с применением нескольких фильтров одновременно.
   * <p>
   * Поддерживает фильтрацию по статусу, эксперту и диапазону дат создания.
   * Параметры могут быть null, в этом случае соответствующий фильтр не применяется.
   * </p>
   *
   * @param status статус курса (опционально)
   * @param expertId идентификатор эксперта (опционально)
   * @param startDate начальная дата создания (опционально)
   * @param endDate конечная дата создания (опционально)
   * @return список курсов, соответствующих всем указанным фильтрам
   */
  @Query("SELECT c FROM Course c WHERE " +
      "(:status IS NULL OR c.status = :status) AND " +
      "(:expertId IS NULL OR c.expert.id = :expertId) AND " +
      "(:startDate IS NULL OR c.creationDate >= :startDate) AND " +
      "(:endDate IS NULL OR c.creationDate <= :endDate)")
  List<Course> findWithFilters(@Param("status") String status,
      @Param("expertId") Long expertId,
      @Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate);
}