package com.project.WebStore.inquiry.entity;

import com.project.WebStore.admin.entity.AdminEntity;
import com.project.WebStore.common.entity.BaseEntity;
import com.project.WebStore.user.entity.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Entity(name = "inquiry_room")
public class InquiryRoomEntity extends BaseEntity {

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity userEntity;

  @ManyToOne
  @JoinColumn(name = "admin_id")
  private AdminEntity adminEntity;
}
