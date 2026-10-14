package com.anseong.core.infrastructure.aop.semaphore;

import java.util.concurrent.Semaphore;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.anseong.core.domain.aop.semaphore.MasterDatasourceSemaphore;
import com.anseong.core.domain.aop.semaphore.ReplicaDatasourceSemaphore;
import com.anseong.core.domain.exception.GlobalAnseongappException;

@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DatasourceSemaphoreAspect {

     private final Semaphore masterSemaphore;
     private final Semaphore replicaSemaphore;

     public DatasourceSemaphoreAspect(
          @Value("${spring.datasource.master.maximum-pool-size}") String masterConnectionPoolSize,
          @Value("${spring.datasource.replica.maximum-pool-size}") String replicaConnectionPoolSize
     ) {
          this.masterSemaphore = new Semaphore(Integer.parseInt(masterConnectionPoolSize));
          this.replicaSemaphore = new Semaphore(Integer.parseInt(replicaConnectionPoolSize));
     }

     @Around("@annotation(masterDatasourceSemaphore")
     void controlMasterDatasourceConnection(ProceedingJoinPoint joinPoint, MasterDatasourceSemaphore masterDatasourceSemaphore) {

          boolean isAcquired = false;
          try {

               isAcquired = masterSemaphore.tryAcquire(masterDatasourceSemaphore.timeout(), masterDatasourceSemaphore.unit());

               if (isAcquired)
                    joinPoint.proceed();

          } catch (InterruptedException e) {
               Thread.currentThread().interrupt();
               throw new GlobalAnseongappException("Replica에 현재 접속량이 많아 DB 커넥션 획득 실패.");

          } catch (Throwable e) {
               throw new GlobalAnseongappException(e.getMessage(), e.getCause());

          } finally {
               if (isAcquired)
                    masterSemaphore.release();
          }
     }

     @Around("@annotation(replicaDatasourceSemaphore")
     void controlMasterDatasourceConnection(ProceedingJoinPoint joinPoint, ReplicaDatasourceSemaphore replicaDatasourceSemaphore) {

          boolean isAcquired = false;
          try {

               isAcquired = replicaSemaphore.tryAcquire(replicaDatasourceSemaphore.timeout(), replicaDatasourceSemaphore.unit());

               if (isAcquired)
                    joinPoint.proceed();

          } catch (InterruptedException e) {
               Thread.currentThread().interrupt();
               throw new GlobalAnseongappException("Master에 현재 접속량이 많아 DB 커넥션 획득 실패.");

          } catch (Throwable e) {
               throw new GlobalAnseongappException(e.getMessage(), e.getCause());

          } finally {
               if (isAcquired)
                    replicaSemaphore.release();
          }
     }
}
