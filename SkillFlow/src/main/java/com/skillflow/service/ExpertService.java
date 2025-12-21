package com.skillflow.service;

import com.skillflow.entity.Expert;
import com.skillflow.repository.ExpertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Сервисный класс для бизнес-логики работы с экспертами.
 * <p>
 * Содержит методы для управления экспертами: создание, обновление, удаление,
 * поиск и валидация. Все методы выполняются в транзакционном контексте.
 * </p>
 *
 */
@Service
@Transactional
public class ExpertService {

  @Autowired
  private ExpertRepository expertRepository;

  /**
   * Получает список всех экспертов.
   *
   * @return список всех экспертов в системе
   */
  public List<Expert> getAllExperts() {
    return expertRepository.findAll();
  }

  /**
   * Находит эксперта по идентификатору.
   *
   * @param id идентификатор эксперта
   * @return Optional с экспертом, если найден
   */
  public Optional<Expert> getExpertById(Long id) {
    return expertRepository.findById(id);
  }

  /**
   * Сохраняет или обновляет эксперта.
   * <p>
   * Выполняет валидацию данных:
   * 1. Имя и фамилия не должны быть пустыми
   * 2. Email должен иметь корректный формат
   * 3. Специализация обязательна
   * 4. Email должен быть уникальным
   * </p>
   *
   * @param expert объект эксперта для сохранения
   * @return сохраненный эксперт
   * @throws RuntimeException если данные не прошли валидацию
   */
  public Expert saveExpert(Expert expert) {
    try {
      // Базовая валидация
      if (expert.getFirstName() == null || expert.getFirstName().trim().isEmpty()) {
        throw new RuntimeException("Имя не может быть пустым");
      }

      if (expert.getLastName() == null || expert.getLastName().trim().isEmpty()) {
        throw new RuntimeException("Фамилия не может быть пустой");
      }

      if (expert.getEmail() == null || expert.getEmail().trim().isEmpty() ||
          !expert.getEmail().contains("@")) {
        throw new RuntimeException("Неверный формат email");
      }

      if (expert.getSpecialization() == null || expert.getSpecialization().trim().isEmpty()) {
        throw new RuntimeException("Специализация обязательна");
      }

      // Валидация уникальности email
      Expert existingExpert = expertRepository.findByEmail(expert.getEmail());
      if (existingExpert != null &&
          (expert.getId() == null || !existingExpert.getId().equals(expert.getId()))) {
        throw new RuntimeException("Эксперт с таким email уже существует");
      }

      return expertRepository.save(expert);

    } catch (Exception e) {
      throw new RuntimeException("Ошибка при сохранении эксперта: " + e.getMessage());
    }
  }

  /**
   * Удаляет эксперта по идентификатору.
   *
   * @param id идентификатор эксперта для удаления
   * @throws RuntimeException если произошла ошибка при удалении
   */
  public void deleteExpert(Long id) {
    try {
      expertRepository.deleteById(id);
    } catch (Exception e) {
      throw new RuntimeException("Ошибка при удалении эксперта: " + e.getMessage());
    }
  }

  /**
   * Находит экспертов по специализации.
   *
   * @param specialization специализация для поиска
   * @return список экспертов с указанной специализацией
   */
  public List<Expert> getExpertsBySpecialization(String specialization) {
    return expertRepository.findBySpecialization(specialization);
  }

  /**
   * Находит эксперта по email.
   *
   * @param email email эксперта
   * @return эксперт с указанным email или null если не найден
   */
  public Expert findByEmail(String email) {
    return expertRepository.findByEmail(email);
  }
}