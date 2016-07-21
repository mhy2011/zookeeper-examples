package com.mhy.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author mahaiyuan
 * @ClassName: CreateSessionDemo
 * @date 2016-07-21 下午11:58
 */
public class CreateSessionDemo {
  public static void main(String[] args) {
    String connStr = "127.0.0.1:2181,127.0.0.1:2183,127.0.0.1:2183";
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
    CuratorFramework client = CuratorFrameworkFactory.newClient(connStr, 10000, 3000, retryPolicy);
    client.start();
    try {
      Thread.sleep(Integer.MAX_VALUE);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
