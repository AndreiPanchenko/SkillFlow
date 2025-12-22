package com.skillflow.repository;

import com.skillflow.entity.Expert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с экспертами в базе данных.
 * <p>
 * Предоставляет методы для выполнения операций с сущностью Expert.
 * Наследует JpaRepository, что дает базовые CRUD операции.
 * </p>
 *
 */
@Repository
public interface ExpertRepository extends JpaRepository<Expert, Long> {

  /**
   * Находит экспертов по специализации.
   *
   * @param specialization специализация для поиска
   * @return список экспертов с указанной специализацией
   */
  List<Expert> findBySpecialization(String specialization);

  /**
   * Находит эксперта по email.
   *
   * @param email email эксперта
   * @return эксперт с указанным email или null если не найден
   */
  Expert findByEmail(String email);
}