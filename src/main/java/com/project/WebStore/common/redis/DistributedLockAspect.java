package com.project.WebStore.common.redis;

import static com.project.WebStore.error.ErrorCode.INTERRUPTED_LOCK;
import static com.project.WebStore.error.ErrorCode.NOT_ACQUIRE_LOCK;

import com.project.WebStore.error.exception.WebStoreException;
import com.project.WebStore.user.service.TransactionSynchronizationService;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {

  private static final String REDISSON_LOCK_PREFIX = "LOCK: ";

  private final RedissonClient redissonClient;
  private final TransactionSynchronizationService transactionSynchronizationService;

  @Around("@annotation(com.project.WebStore.common.redis.DistributedLock)")
  public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

    String lockKey = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key());
    RLock lock = redissonClient.getLock(lockKey);

    long waitTime = distributedLock.waitTime();
    long leaseTime = distributedLock.leaseTime();
    TimeUnit timeUnit = distributedLock.timeUnit();

    try {
      if (lock.tryLock(waitTime, leaseTime, timeUnit)) {
        log.info("락 획득: {}", lockKey);
        try {
          transactionSynchronizationService.registerLock(lock);
          log.info("락 등록 완료: {}", lockKey);
          return joinPoint.proceed();
        } catch (Exception e) {
          log.info("락 획득 중 에러발생: {}", lockKey);
          lock.unlock(); // 예외 발생 시 락 해제
          throw e;
        }
      } else {
        log.info("락 획득 실패");
        throw new WebStoreException(NOT_ACQUIRE_LOCK);
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.info("InterruptedException");
      throw new WebStoreException(INTERRUPTED_LOCK);
    }
  }
}