package com.skillflow.service;

import com.skillflow.entity.Student;
import com.skillflow.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервисный класс для бизнес-логики работы со студентами.
 * <p>
 * Содержит методы для управления студентами: создание, обновление, удаление,
 * поиск и валидация. Все методы выполняются в транзакционном контексте.
 * </p>
 *
 */
@Service
@Transactional
public class StudentService {

  @Autowired
  private StudentRepository studentRepository;

  /**
   * Получает список всех студентов.
   *
   * @return список всех студентов в системе
   */
  public List<Student> getAllStudents() {
    return studentRepository.findAll();
  }

  /**
   * Находит студента по идентификатору.
   *
   * @param id идентификатор студента
   * @return Optional со студентом, если найден
   */
  public Optional<Student> getStudentById(Long id) {
    return studentRepository.findById(id);
  }

  /**
   * Сохраняет или обновляет студента.
   * <p>
   * Выполняет валидацию данных:
   * 1. Имя и фамилия не должны быть пустыми
   * 2. Email должен иметь корректный формат
   * 3. Email должен быть уникальным
   * </p>
   *
   * @param student объект студента для сохранения
   * @return сохраненный студент
   * @throws RuntimeException если данные не прошли валидацию
   */
  public Student saveStudent(Student student) {
    try {
      // Базовая валидация
      if (student.getFirstName() == null || student.getFirstName().trim().isEmpty()) {
        throw new RuntimeException("Имя не может быть пустым");
      }

      if (student.getLastName() == null || student.getLastName().trim().isEmpty()) {
        throw new RuntimeException("Фамилия не может быть пустой");
      }

      if (student.getEmail() == null || student.getEmail().trim().isEmpty() ||
          !student.getEmail().contains("@")) {
        throw new RuntimeException("Неверный формат email");
      }

      if (student.getStatus() == null || student.getStatus().trim().isEmpty()) {
        student.setStatus("АКТИВЕН");
      }

      if (student.getRegistrationDate() == null) {
        student.setRegistrationDate(LocalDateTime.now());
      }

      // Валидация уникальности email
      Student existingStudent = studentRepository.findByEmail(student.getEmail());
      if (existingStudent != null &&
          (student.getId() == null || !existingStudent.getId().equals(student.getId()))) {
        throw new RuntimeException("Студент с таким email уже существует");
      }

      return studentRepository.save(student);

    } catch (Exception e) {
      throw new RuntimeException("Ошибка при сохранении студента: " + e.getMessage());
    }
  }

  /**
   * Удаляет студента по идентификатору.
   *
   * @param id идентификатор студента для удаления
   * @throws RuntimeException если произошла ошибка при удалении
   */
  public void deleteStudent(Long id) {
    try {
      studentRepository.deleteById(id);
    } catch (Exception e) {
      throw new RuntimeException("Ошибка при удалении студента: " + e.getMessage());
    }
  }

  /**
   * Обновляет статус студента.
   *
   * @param studentId идентификатор студента
   * @param status новый статус (АКТИВЕН, ЗАВЕРШИЛ, ОТЧИСЛЕН, В_ОЖИДАНИИ)
   * @throws RuntimeException если студент не найден или произошла ошибка
   */
  public void updateStudentStatus(Long studentId, String status) {
    try {
      Student student = studentRepository.findById(studentId)
          .orElseThrow(() -> new RuntimeException("Студент не найден"));

      if (status != null && List.of("АКТИВЕН", "ЗАВЕРШИЛ", "ОТЧИСЛЕН", "В_ОЖИДАНИИ").contains(status)) {
        student.setStatus(status);
        studentRepository.save(student);
      }
    } catch (Exception e) {
      throw new RuntimeException("Ошибка при обновлении статуса: " + e.getMessage());
    }
  }

  /**
   * Находит студента по email.
   *
   * @param email email студента
   * @return студент с указанным email или null если не найден
   */
  public Student findByEmail(String email) {
    return studentRepository.findByEmail(email);
  }
}