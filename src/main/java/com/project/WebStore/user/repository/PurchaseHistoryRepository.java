package com.project.WebStore.user.repository;

import com.project.WebStore.user.entity.PurchaseHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistoryEntity, Long> {

}
