package com.project.WebStore.common.redis;

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

  private static final String REDISSON_LOCK_PREFIX = "lock name: ";

  private final RedissonClient redissonClient;
  private final AopForTransaction aopForTransaction;

  @Around("@annotation(com.project.WebStore.common.redis.DistributedLock)")
  public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

    String lockKey = REDISSON_LOCK_PREFIX
        + CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key());
    RLock lock = redissonClient.getLock(lockKey);

    long waitTime = distributedLock.waitTime();
    long leaseTime = distributedLock.leaseTime();
    TimeUnit timeUnit = distributedLock.timeUnit();

    try {
      if (lock.tryLock(waitTime, leaseTime, timeUnit)) {
        log.info("락 획득 성공 / {}", lockKey);
        return aopForTransaction.proceed(joinPoint);
      } else {
        log.info("락 획득 실패 / {}", lockKey);
        return false;
      }
    } catch (InterruptedException e) {
      log.info("InterruptedException");
      throw new InterruptedException();
    } finally {
      try {
        log.info("락 해제 / {}", lock.getName());
        lock.unlock();
      } catch (IllegalMonitorStateException e) {
        log.info("이미 해제된 락 / {}", lock.getName());
      }
    }
  }
}