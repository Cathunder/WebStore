package com.project.WebStore.user.repository;

import com.project.WebStore.user.entity.PointHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistoryEntity, Long> {

}
