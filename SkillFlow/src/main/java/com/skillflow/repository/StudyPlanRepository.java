package com.skillflow.repository;

import com.skillflow.entity.StudyPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyPlanRepository extends JpaRepository<StudyPlan, Long> {
  List<StudyPlan> findByStudentId(Long studentId);
  List<StudyPlan> findByCourseId(Long courseId);
  List<StudyPlan> findByStatus(String status);
}