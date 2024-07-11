package com.project.WebStore.user.repository;

import com.project.WebStore.user.entity.CashHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashHistoryRepository extends JpaRepository<CashHistoryEntity, Long> {

}
