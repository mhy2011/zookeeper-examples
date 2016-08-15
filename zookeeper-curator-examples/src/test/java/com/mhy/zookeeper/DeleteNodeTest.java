package com.mhy.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Before;
import org.junit.Test;

/**
 * @author mahaiyuan
 * @ClassName: DeleteNodeTest
 * @date 2016-07-23 下午5:48
 */
public class DeleteNodeTest {

  private CuratorFramework client;

  @Before
  public void before(){
    String connectStr = "10.94.100.229:2181,10.94.100.229:3181,10.94.100.229:4181";
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
    client = CuratorFrameworkFactory.builder()
            .connectString(connectStr)
            .retryPolicy(retryPolicy)
            .sessionTimeoutMs(5000)
            .build();
    client.start();
  }

  /**
   * 删除节点
   * @throws Exception
   */
  @Test
  public void testDeleteNode01() throws Exception {
    String path = "/c1";
    client.delete().forPath(path);
  }

  /**
   * 删除节点
   * @throws Exception
   */
  @Test
  public void testDeleteNode02() throws Exception {
    String path = "/lucifer";
    client.delete()
            .deletingChildrenIfNeeded() //如果非叶子节点,删除其子节点
            .forPath(path);
  }
}
