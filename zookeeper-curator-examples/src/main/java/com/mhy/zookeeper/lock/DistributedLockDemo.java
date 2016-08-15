package com.mhy.zookeeper.lock;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author mahaiyuan
 * @ClassName: DistributedLockDemo
 * @date 2016-08-01 下午6:45
 */
public class DistributedLockDemo {

  private static String connectStr = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";  //连接地址
  private static RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
  private static int sessionTimeoutMs = 5000;
  protected static CuratorFramework client = CuratorFrameworkFactory.builder()
          .connectString(connectStr)
          .retryPolicy(retryPolicy)
          .sessionTimeoutMs(sessionTimeoutMs)
          .build();

  public static void main(String[] args) {
    client.start();
    String lockPath = "/lockPath";
    final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");
    final CountDownLatch countDownLatch = new CountDownLatch(1);
    InterProcessMutex lock = new InterProcessMutex(client, lockPath);
    for (int i = 0; i < 50; i++) {
      new Thread(){
        @Override
        public void run() {
          try {
            countDownLatch.await();
            lock.acquire();
          } catch(Exception e) {
            e.printStackTrace();
          }
          String orderNo = format.format(new Date());
          System.out.println(Thread.currentThread().getName() + "\t订单号:" + orderNo);

          try {
            lock.release(); //释放锁
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }.start();

    }

    countDownLatch.countDown();
  }
}
