package com.project.WebStore.admin.entity;

import com.project.WebStore.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity(name = "admin")
public class AdminEntity extends BaseEntity {

  @Column(unique = true)
  private String email;
  private String password;
}
