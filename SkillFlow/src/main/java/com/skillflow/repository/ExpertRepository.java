package com.skillflow.repository;

import com.skillflow.entity.Expert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpertRepository extends JpaRepository<Expert, Long> {
  List<Expert> findBySpecialization(String specialization);
  List<Expert> findByLastNameContainingIgnoreCase(String lastName);
}