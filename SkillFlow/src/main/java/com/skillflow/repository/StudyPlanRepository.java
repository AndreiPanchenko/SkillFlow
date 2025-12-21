package com.skillflow.repository;

import com.skillflow.entity.StudyPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с учебными планами в базе данных.
 * <p>
 * Предоставляет методы для выполнения операций с сущностью StudyPlan.
 * Наследует JpaRepository, что дает базовые CRUD операции.
 * </p>
 *
 */
@Repository
public interface StudyPlanRepository extends JpaRepository<StudyPlan, Long> {

  /**
   * Находит учебные планы по идентификатору студента.
   *
   * @param studentId идентификатор студента
   * @return список учебных планов указанного студента
   */
  List<StudyPlan> findByStudentId(Long studentId);

  /**
   * Находит учебные планы по идентификатору курса.
   *
   * @param courseId идентификатор курса
   * @return список учебных планов указанного курса
   */
  List<StudyPlan> findByCourseId(Long courseId);

  /**
   * Находит учебные планы по статусу.
   *
   * @param status статус учебного плана
   * @return список учебных планов с указанным статусом
   */
  List<StudyPlan> findByStatus(String status);

  /**
   * Находит учебные планы по студенту и курсу.
   * <p>
   * Используется для проверки дублирования (чтобы студент не был записан
   * дважды на один курс).
   * </p>
   *
   * @param studentId идентификатор студента
   * @param courseId идентификатор курса
   * @return список учебных планов для указанного студента и курса
   */
  List<StudyPlan> findByStudentIdAndCourseId(Long studentId, Long courseId);
}