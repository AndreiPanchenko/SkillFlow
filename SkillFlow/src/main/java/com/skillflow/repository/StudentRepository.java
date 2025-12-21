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

}