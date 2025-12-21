package com.skillflow.service;

import com.skillflow.entity.StudyPlan;
import com.skillflow.repository.StudyPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервисный класс для бизнес-логики работы с учебными планами.
 * <p>
 * Содержит методы для управления учебными планами: создание, обновление,
 * удаление, поиск и валидация. Все методы выполняются в транзакционном контексте.
 * </p>
 *
 */
@Service
@Transactional
public class StudyPlanService {

  @Autowired
  private StudyPlanRepository studyPlanRepository;

  @Autowired
  private StudentService studentService;

  @Autowired
  private CourseService courseService;

  /**
   * Получает список всех учебных планов.
   *
   * @return список всех учебных планов в системе
   */
  public List<StudyPlan> getAllStudyPlans() {
    return studyPlanRepository.findAll();
  }

  /**
   * Находит учебный план по идентификатору.
   *
   * @param id идентификатор учебного плана
   * @return Optional с учебным планом, если найден
   */
  public Optional<StudyPlan> getStudyPlanById(Long id) {
    return studyPlanRepository.findById(id);
  }

  /**
   * Сохраняет или обновляет учебный план.
   * <p>
   * Выполняет валидацию данных:
   * 1. Студент обязателен
   * 2. Курс обязателен
   * 3. Проверка на дублирование (студент не может быть записан дважды на один курс)
   * </p>
   *
   * @param studyPlan объект учебного плана для сохранения
   * @return сохраненный учебный план
   * @throws RuntimeException если данные не прошли валидацию
   */
  public StudyPlan saveStudyPlan(StudyPlan studyPlan) {
    try {
      // Базовая валидация
      if (studyPlan.getStudent() == null) {
        throw new RuntimeException("Студент обязателен");
      }

      if (studyPlan.getCourse() == null) {
        throw new RuntimeException("Курс обязателен");
      }

      if (studyPlan.getStartDate() == null) {
        studyPlan.setStartDate(LocalDateTime.now());
      }

      if (studyPlan.getStatus() == null || studyPlan.getStatus().trim().isEmpty()) {
        studyPlan.setStatus("активен");
      }

      // Проверка на дубликат (один студент не может быть записан дважды на один курс)
      List<StudyPlan> existingPlans = studyPlanRepository.findByStudentIdAndCourseId(
          studyPlan.getStudent().getId(), studyPlan.getCourse().getId());

      if (!existingPlans.isEmpty() &&
          (studyPlan.getId() == null || existingPlans.stream()
              .noneMatch(p -> p.getId().equals(studyPlan.getId())))) {
        throw new RuntimeException("Студент уже записан на этот курс");
      }

      return studyPlanRepository.save(studyPlan);

    } catch (Exception e) {
      throw new RuntimeException("Ошибка при сохранении учебного плана: " + e.getMessage());
    }
  }

  /**
   * Удаляет учебный план по идентификатору.
   *
   * @param id идентификатор учебного плана для удаления
   * @throws RuntimeException если произошла ошибка при удалении
   */
  public void deleteStudyPlan(Long id) {
    try {
      studyPlanRepository.deleteById(id);
    } catch (Exception e) {
      throw new RuntimeException("Ошибка при удалении учебного плана: " + e.getMessage());
    }
  }

  /**
   * Обновляет статус учебного плана.
   *
   * @param studyPlanId идентификатор учебного плана
   * @param status новый статус (активен, завершен, приостановлен, отменен)
   * @throws RuntimeException если учебный план не найден или произошла ошибка
   */
  public void updateStudyPlanStatus(Long studyPlanId, String status) {
    try {
      StudyPlan studyPlan = studyPlanRepository.findById(studyPlanId)
          .orElseThrow(() -> new RuntimeException("Учебный план не найден"));

      if (status != null && List.of("активен", "завершен", "приостановлен", "отменен").contains(status)) {
        studyPlan.setStatus(status);
        studyPlanRepository.save(studyPlan);
      }
    } catch (Exception e) {
      throw new RuntimeException("Ошибка при обновлении статуса: " + e.getMessage());
    }
  }

  /**
   * Находит учебные планы по студенту.
   *
   * @param studentId идентификатор студента
   * @return список учебных планов указанного студента
   */
  public List<StudyPlan> getStudyPlansByStudent(Long studentId) {
    return studyPlanRepository.findByStudentId(studentId);
  }

  /**
   * Находит учебные планы по курсу.
   *
   * @param courseId идентификатор курса
   * @return список учебных планов указанного курса
   */
  public List<StudyPlan> getStudyPlansByCourse(Long courseId) {
    return studyPlanRepository.findByCourseId(courseId);
  }

  /**
   * Находит учебные планы по статусу.
   *
   * @param status статус учебного плана
   * @return список учебных планов с указанным статусом
   */
  public List<StudyPlan> getStudyPlansByStatus(String status) {
    return studyPlanRepository.findByStatus(status);
  }

  /**
   * Получает активные учебные планы.
   *
   * @return список активных учебных планов
   */
  public List<StudyPlan> getActiveStudyPlans() {
    return studyPlanRepository.findByStatus("активен");
  }

  /**
   * Получает завершенные учебные планы.
   *
   * @return список завершенных учебных планов
   */
  public List<StudyPlan> getCompletedStudyPlans() {
    return studyPlanRepository.findByStatus("завершен");
  }
}