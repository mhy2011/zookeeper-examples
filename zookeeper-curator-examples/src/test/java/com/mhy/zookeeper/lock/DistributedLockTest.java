package com.mhy.zookeeper.lock;

import com.mhy.zookeeper.BaseTest;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author mahaiyuan
 * @ClassName: DistributedLockTest
 * @date 2016-08-01 下午5:33
 */
public class DistributedLockTest extends BaseTest {

  @Test
  public void testNoLock(){

    final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");
    final CountDownLatch countDownLatch = new CountDownLatch(1);
    for (int i = 0; i < 10; i++) {
      new Thread(){
        @Override
        public void run() {
          try {
            countDownLatch.await();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          String orderNo = format.format(new Date());
          System.out.println("订单号:" + orderNo);
        }
      }.start();
    }

    countDownLatch.countDown();
  }

  @Test
  public void testLock(){
    String lockPath = "/lockPath";
    final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");
    final CountDownLatch countDownLatch = new CountDownLatch(1);
    for (int i = 0; i < 10; i++) {
      new Thread(){
        @Override
        public void run() {
          InterProcessMutex lock = new InterProcessMutex(client, lockPath);
          try {
            if (lock.acquire(60, TimeUnit.SECONDS)){
              countDownLatch.await();
              String orderNo = format.format(new Date());
              System.out.println("订单号:" + orderNo);
              try {
              } catch (Exception e) {
                e.printStackTrace();
              } finally {
                try {
                lock.release(); //释放锁
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }.start();

    }

    countDownLatch.countDown();
  }

  @Test
  public void test03(){
    String servers = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
    CuratorFramework curator = CuratorFrameworkFactory.builder().retryPolicy(new ExponentialBackoffRetry(10000, 3)).connectString(servers).build();
    curator.start();
    final InterProcessMutex lock = new InterProcessMutex(curator, "/global_lock");

    Executor pool = Executors.newFixedThreadPool(10);
    for (int i = 0; i < 10; i ++) {
      pool.execute(new Runnable() {
        public void run() {
          try {
            lock.acquire();
            System.out.println(Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(5);
          } catch (Exception e) {
            e.printStackTrace();
          }finally{
            try {
              lock.release();
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
      });
    }

  }
}
