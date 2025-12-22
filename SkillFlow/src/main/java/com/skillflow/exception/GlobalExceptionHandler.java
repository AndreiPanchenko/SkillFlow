package com.skillflow.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.validation.BindException;

/**
 * Глобальный обработчик исключений для всего приложения SkillFlow.
 * <p>
 * Этот класс обрабатывает исключения, возникающие в контроллерах,
 * и возвращает соответствующие страницы ошибок. Аннотация @ControllerAdvice
 * позволяет применять обработку исключений ко всем контроллерам.
 * </p>
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Обрабатывает исключения валидации данных (BindException).
   * <p>
   * Вызывается при ошибках валидации данных формы, возвращает страницу
   * с детализированными сообщениями об ошибках.
   * </p>
   *
   * @param ex исключение валидации
   * @param model модель для передачи данных в представление
   * @return имя шаблона страницы ошибки валидации
   */
  @ExceptionHandler(BindException.class)
  public String handleValidationExceptions(BindException ex, Model model) {
    model.addAttribute("errors", ex.getBindingResult().getAllErrors());
    model.addAttribute("errorMessage", "Ошибка валидации данных");
    return "error/validation-error";
  }

  /**
   * Обрабатывает исключения недопустимых аргументов.
   * <p>
   * Вызывается при передаче некорректных аргументов в методы.
   * </p>
   *
   * @param ex исключение недопустимого аргумента
   * @param model модель для передачи данных в представление
   * @return имя шаблона страницы ошибки
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public String handleIllegalArgumentException(IllegalArgumentException ex, Model model) {
    model.addAttribute("errorMessage", ex.getMessage());
    return "error/error";
  }

  /**
   * Обрабатывает все остальные исключения.
   * <p>
   * Общий обработчик для всех непредвиденных исключений.
   * </p>
   *
   * @param ex произошедшее исключение
   * @param model модель для передачи данных в представление
   * @return имя шаблона страницы ошибки
   */
  @ExceptionHandler(Exception.class)
  public String handleGeneralException(Exception ex, Model model) {
    model.addAttribute("errorMessage", "Произошла ошибка: " + ex.getMessage());
    return "error/error";
  }
}