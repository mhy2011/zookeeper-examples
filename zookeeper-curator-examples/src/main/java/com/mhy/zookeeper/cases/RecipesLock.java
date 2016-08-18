package com.mhy.zookeeper.cases;

import com.mhy.zookeeper.ZkClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * @author mahaiyuan
 * @ClassName: RecipesNoLock
 * @date 2016-08-18 下午4:16
 */
public class RecipesLock {
  private static SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");
  public static void main(String[] args) {
    CuratorFramework client = ZkClient.getInstance().getClient();
    String path = "/mhy/lock";
    InterProcessMutex lock = new InterProcessMutex(client, path);
    CountDownLatch latch = new CountDownLatch(1);
    for (int i = 0; i < 10; i++) {
      new Thread(){
        @Override
        public void run() {
          try {
            latch.await();
            lock.acquire(); //加锁操作
          } catch (Exception e) {
            e.printStackTrace();
          }
          System.out.println(Thread.currentThread().getName() + "," + format.format(new Date()));

          try {
            System.out.println(Thread.currentThread().getName() + "释放锁");
            lock.release(); //释放锁
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }.start();
    } //end for

    latch.countDown();
  }
}
