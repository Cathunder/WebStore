package com.project.WebStore.notify.entity;

import com.project.WebStore.admin.entity.AdminEntity;
import com.project.WebStore.common.entity.BaseEntity;
import com.project.WebStore.user.entity.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Entity(name = "notify")
public class NotifyEntity extends BaseEntity {

  @ManyToOne
  @JoinColumn(name = "admin_id")
  private AdminEntity adminEntity;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity userEntity;

  private String subject;
  private String contents;
  private LocalDateTime timeToSend;
}
