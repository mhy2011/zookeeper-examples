package com.mhy.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

/**
 * @author mahaiyuan
 * @ClassName: GetDataTest
 * @date 2016-07-23 下午5:48
 */
public class GetDataTest {

  private CuratorFramework client;

  @Before
  public void before(){
    String connectStr = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
    client = CuratorFrameworkFactory.builder()
            .connectString(connectStr)
            .retryPolicy(retryPolicy)
            .sessionTimeoutMs(5000)
            .build();
    client.start();
  }

  /**
   * 获取节点数据
   * @throws Exception
   */
  @Test
  public void testGetData01() throws Exception {
    String path = "/c1";
    byte[] bytes = client.getData().forPath(path);
    System.out.println("结果:" + new String(bytes));
  }

  /**
   * 获取节点数据
   * @throws Exception
   */
  @Test
  public void testGetData02() throws Exception {
    String path = "/c1";
    Stat stat = new Stat();
    byte[] bytes = client.getData()
            .storingStatIn(stat)
            .forPath(path);
    System.out.println("结果:" + new String(bytes));
    System.out.println(stat);
  }

}
