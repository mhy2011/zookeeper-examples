package com.mhy.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Before;

/**
 * @author mahaiyuan
 * @ClassName: BaseTest
 * @date 2016-08-01 下午2:03
 */
public class BaseTest {
  protected CuratorFramework client = null;
  private static String connectStr = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";  //连接地址
  private static RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
  private static int sessionTimeoutMs = 5000;

  @Before
  public void before(){
    client = CuratorFrameworkFactory.builder()
            .connectString(connectStr)
            .retryPolicy(retryPolicy)
            .sessionTimeoutMs(sessionTimeoutMs)
            .build();
    client.start();
  }

}
