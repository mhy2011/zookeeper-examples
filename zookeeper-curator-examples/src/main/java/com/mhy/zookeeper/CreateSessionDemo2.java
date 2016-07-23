package com.mhy.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author mahaiyuan
 * @ClassName: CreateSessionDemo2
 * @date 2016-07-22 下午1:41
 */
public class CreateSessionDemo2 {
  public static void main(String[] args) throws InterruptedException {
    //基于fluent风格的session创建
    String connStr = "127.0.0.1:2181,127.0.0.1:2183,127.0.0.1:2183";
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
    CuratorFramework client = CuratorFrameworkFactory.builder().connectString(connStr)
            .sessionTimeoutMs(5000).retryPolicy(retryPolicy).build();
    client.start();
    Thread.sleep(Integer.MAX_VALUE);
  }
}
