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

@Controller
@RequestMapping("/experts")
public class ExpertController {

  @Autowired
  private ExpertService expertService;

  @GetMapping
  public String getAllExperts(Model model) {
    model.addAttribute("experts", expertService.getAllExperts());
    return "experts/list";
  }

  @GetMapping("/{id}")
  public String getExpertById(@PathVariable Long id, Model model) {
    Optional<Expert> expert = expertService.getExpertById(id);
    if (expert.isPresent()) {
      model.addAttribute("expert", expert.get());
      return "experts/view";
    }
    return "redirect:/experts";
  }

  @GetMapping("/new")
  public String showExpertForm(Model model) {
    model.addAttribute("expert", new Expert());
    return "experts/form";
  }

  @PostMapping
  public String saveExpert(@Valid @ModelAttribute Expert expert, BindingResult result) {
    if (result.hasErrors()) {
      return "experts/form";
    }
    expertService.saveExpert(expert);
    return "redirect:/experts";
  }

  @GetMapping("/{id}/edit")
  public String editExpert(@PathVariable Long id, Model model) {
    Optional<Expert> expert = expertService.getExpertById(id);
    if (expert.isPresent()) {
      model.addAttribute("expert", expert.get());
      return "experts/form";
    }
    return "redirect:/experts";
  }

  @GetMapping("/{id}/delete")
  public String deleteExpert(@PathVariable Long id) {
    expertService.deleteExpert(id);
    return "redirect:/experts";
  }
}