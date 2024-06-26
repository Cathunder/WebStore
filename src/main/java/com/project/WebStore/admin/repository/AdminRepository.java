package com.project.WebStore.admin.repository;

import com.project.WebStore.admin.entity.AdminEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Long> {

  boolean existsByEmail(String email);
  Optional<AdminEntity> findByEmail(String email);
}
