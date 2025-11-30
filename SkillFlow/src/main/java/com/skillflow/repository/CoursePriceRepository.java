package com.skillflow.repository;

import com.skillflow.entity.CoursePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoursePriceRepository extends JpaRepository<CoursePrice, Long> {
  List<CoursePrice> findByCourseId(Long courseId);
}