package com.skillflow.controller;

import com.skillflow.entity.Expert;
import com.skillflow.service.ExpertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Контроллер для управления экспертами (преподавателями) в системе SkillFlow.
 * <p>
 * Обрабатывает операции с экспертами: создание, редактирование,
 * удаление и просмотр. Использует валидацию данных через аннотации Bean Validation.
 * </p>
 *
 */
@Controller
@RequestMapping("/experts")
public class ExpertController {

  @Autowired
  private ExpertService expertService;

  /**
   * Отображает список всех экспертов.
   *
   * @param model модель для передачи данных в представление
   * @return имя шаблона страницы со списком экспертов
   */
  @GetMapping
  public String getAllExperts(Model model) {
    model.addAttribute("experts", expertService.getAllExperts());
    return "experts/list";
  }

  /**
   * Отображает детальную информацию о конкретном эксперте.
   *
   * @param id идентификатор эксперта
   * @param model модель для передачи данных в представление
   * @return имя шаблона страницы просмотра эксперта или редирект при ошибке
   */
  @GetMapping("/{id}")
  public String getExpertById(@PathVariable Long id, Model model) {
    Optional<Expert> expert = expertService.getExpertById(id);
    if (expert.isPresent()) {
      model.addAttribute("expert", expert.get());
      return "experts/view";
    }
    return "redirect:/experts";
  }

  /**
   * Отображает форму для создания нового эксперта.
   *
   * @param model модель для передачи данных в представление
   * @return имя шаблона формы создания эксперта
   */
  @GetMapping("/new")
  public String showExpertForm(Model model) {
    model.addAttribute("expert", new Expert());
    return "experts/form";
  }

  /**
   * Создает нового эксперта с валидацией данных.
   *
   * @param expert объект эксперта с данными из формы
   * @param result объект с результатами валидации
   * @return редирект на список экспертов или возврат к форме при ошибке
   */
  @PostMapping("/create")
  public String createExpert(@Valid @ModelAttribute Expert expert, BindingResult result) {
    if (result.hasErrors()) {
      return "experts/form";
    }
    try {
      expertService.saveExpert(expert);
      return "redirect:/experts";
    } catch (RuntimeException e) {
      result.rejectValue("email", "error.expert", e.getMessage());
      return "experts/form";
    }
  }

  /**
   * Отображает форму для редактирования существующего эксперта.
   *
   * @param id идентификатор эксперта для редактирования
   * @param model модель для передачи данных в представление
   * @return имя шаблона формы редактирования эксперта или редирект при ошибке
   */
  @GetMapping("/{id}/edit")
  public String editExpert(@PathVariable Long id, Model model) {
    Optional<Expert> expert = expertService.getExpertById(id);
    if (expert.isPresent()) {
      model.addAttribute("expert", expert.get());
      return "experts/form";
    }
    return "redirect:/experts";
  }

  /**
   * Обновляет данные существующего эксперта с валидацией.
   *
   * @param id идентификатор эксперта
   * @param expert объект эксперта с обновленными данными
   * @param result объект с результатами валидации
   * @return редирект на список экспертов или возврат к форме при ошибке
   */
  @PostMapping("/{id}/update")
  public String updateExpert(@PathVariable Long id,
      @Valid @ModelAttribute Expert expert,
      BindingResult result) {
    if (result.hasErrors()) {
      return "experts/form";
    }

    try {
      Optional<Expert> existingExpert = expertService.getExpertById(id);
      if (existingExpert.isPresent()) {
        Expert toUpdate = existingExpert.get();
        toUpdate.setFirstName(expert.getFirstName());
        toUpdate.setLastName(expert.getLastName());
        toUpdate.setEmail(expert.getEmail());
        toUpdate.setBiography(expert.getBiography());
        toUpdate.setSpecialization(expert.getSpecialization());
        expertService.saveExpert(toUpdate);
      }
      return "redirect:/experts";
    } catch (RuntimeException e) {
      result.rejectValue("email", "error.expert", e.getMessage());
      return "experts/form";
    }
  }

  /**
   * Удаляет эксперта по идентификатору.
   *
   * @param id идентификатор эксперта для удаления
   * @return редирект на список экспертов
   */
  @GetMapping("/{id}/delete")
  public String deleteExpert(@PathVariable Long id) {
    expertService.deleteExpert(id);
    return "redirect:/experts";
  }
}