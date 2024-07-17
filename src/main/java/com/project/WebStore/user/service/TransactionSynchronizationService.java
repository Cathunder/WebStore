package com.project.WebStore.user.service;

import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
public class TransactionSynchronizationService {

  private final ApplicationEventPublisher eventPublisher;
  private final ThreadLocal<Set<RLock>> locks = ThreadLocal.withInitial(HashSet::new);

  public TransactionSynchronizationService(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  public void registerLock(RLock lock) {
    locks.get().add(lock);
    log.info("락 등록: {}", lock.getName());
    eventPublisher.publishEvent(new LockEvent(this));
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void afterTransactionCommit(LockEvent event) {
    Set<RLock> locksToRelease = locks.get();
    for (RLock lock : locksToRelease) {
      log.info("커밋됨 락 해제: {}", lock.getName());
      lock.unlock();
    }
    locks.remove();
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
  public void afterTransactionRollback(LockEvent event) {
    Set<RLock> locksToRelease = locks.get();
    for (RLock lock : locksToRelease) {
      log.info("롤백됨 락 해제: {}", lock.getName());
      lock.unlock();
    }
    locks.remove();
  }

  private static class LockEvent {

    public LockEvent(Object source) {
    }
  }
}
