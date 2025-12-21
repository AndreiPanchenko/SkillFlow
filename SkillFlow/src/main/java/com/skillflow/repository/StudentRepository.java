package com.skillflow.repository;

import com.skillflow.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Репозиторий для работы со студентами в базе данных.
 * <p>
 * Предоставляет методы для выполнения операций с сущностью Student.
 * Наследует JpaRepository, что дает базовые CRUD операции.
 * Содержит пользовательские запросы для фильтрации студентов.
 * </p>
 *
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

  /**
   * Находит студентов по части фамилии (без учета регистра).
   *
   * @param lastName часть фамилии студента
   * @return список студентов, содержащих указанную часть в фамилии
   */
  List<Student> findByLastNameContainingIgnoreCase(String lastName);

  /**
   * Находит студента по email.
   *
   * @param email email студента
   * @return студент с указанным email или null если не найден
   */
  Student findByEmail(String email);

  /**
   * Находит студентов по статусу.
   *
   * @param status статус студента
   * @return список студентов с указанным статусом
   */
  List<Student> findByStatus(String status);

  /**
   * Находит студентов, зарегистрированных в указанном диапазоне дат.
   *
   * @param startDate начальная дата диапазона
   * @param endDate конечная дата диапазона
   * @return список студентов, зарегистрированных в указанном диапазоне
   */
  @Query("SELECT s FROM Student s WHERE s.registrationDate BETWEEN :startDate AND :endDate")
  List<Student> findByRegistrationDateBetween(@Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate);

  /**
   * Находит студентов с применением нескольких фильтров одновременно.
   * <p>
   * Поддерживает фильтрацию по статусу и диапазону дат регистрации.
   * Параметры могут быть null, в этом случае соответствующий фильтр не применяется.
   * </p>
   *
   * @param status статус студента (опционально)
   * @param startDate начальная дата регистрации (опционально)
   * @param endDate конечная дата регистрации (опционально)
   * @return список студентов, соответствующих всем указанным фильтрам
   */
  @Query("SELECT s FROM Student s WHERE " +
      "(:status IS NULL OR s.status = :status) AND " +
      "(:startDate IS NULL OR s.registrationDate >= :startDate) AND " +
      "(:endDate IS NULL OR s.registrationDate <= :endDate)")
  List<Student> findWithFilters(@Param("status") String status,
      @Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate);
}