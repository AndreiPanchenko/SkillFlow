package com.skillflow.service;

import com.skillflow.entity.Expert;
import com.skillflow.repository.ExpertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpertService {

  @Autowired
  private ExpertRepository expertRepository;

  public List<Expert> getAllExperts() {
    return expertRepository.findAll();
  }

  public Optional<Expert> getExpertById(Long id) {
    return expertRepository.findById(id);
  }

  public Expert saveExpert(Expert expert) {
    return expertRepository.save(expert);
  }

  public void deleteExpert(Long id) {
    expertRepository.deleteById(id);
  }

  public List<Expert> getExpertsBySpecialization(String specialization) {
    return expertRepository.findBySpecialization(specialization);
  }
}