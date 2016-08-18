package com.mhy.zookeeper.cases;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * @author mahaiyuan
 * @ClassName: RecipesNoLock
 * @date 2016-08-18 下午4:16
 */
public class RecipesNoLock {
  private static SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");
  public static void main(String[] args) {
    CountDownLatch latch = new CountDownLatch(1);
    for (int i = 0; i < 10; i++) {
      new Thread(){
        @Override
        public void run() {
          try {
            latch.await();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          System.out.println(Thread.currentThread().getName() + "," + format.format(new Date()));
        }
      }.start();
    } //end for

    latch.countDown();
  }
}
