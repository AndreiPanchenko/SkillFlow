package com.skillflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения SkillFlow - точка входа в Spring Boot приложение.
 * <p>
 * Этот класс запускает Spring Boot приложение с автоматической конфигурацией.
 * Аннотация @SpringBootApplication включает:
 * - @Configuration: определяет класс как источник конфигурации бинов
 * - @EnableAutoConfiguration: включает автоматическую конфигурацию Spring Boot
 * - @ComponentScan: сканирует компоненты в пакете com.skillflow и его подпакетах
 * </p>
 *
 * @author Панченко Андрей ИТ-13
 * @version 1.0
 * @since 2025
 */
@SpringBootApplication
public class SkillFlowApplication {

  /**
   * Точка входа в приложение Spring Boot.
   * <p>
   * Метод запускает Spring Boot приложение, инициализируя все компоненты,
   * настраивая контекст приложения и запуская встроенный сервер.
   * </p>
   *
   * @param args аргументы командной строки, переданные при запуске приложения
   */
  public static void main(String[] args) {
    SpringApplication.run(SkillFlowApplication.class, args);
  }
}