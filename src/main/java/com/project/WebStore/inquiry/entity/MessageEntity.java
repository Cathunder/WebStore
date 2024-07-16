package com.project.WebStore.inquiry.entity;

import com.project.WebStore.common.entity.BaseEntity;
import com.project.WebStore.common.type.SenderType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Entity(name = "message")
public class MessageEntity extends BaseEntity {

  @ManyToOne
  @JoinColumn(name = "inquiry_room_id")
  private InquiryRoomEntity inquiryRoomEntity;

  private Long senderId;

  @Enumerated(EnumType.STRING)
  private SenderType senderType;

  private String contents;
  private LocalDateTime sendAt;
}
