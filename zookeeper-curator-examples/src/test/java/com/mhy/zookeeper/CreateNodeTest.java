package com.mhy.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.Before;
import org.junit.Test;

/**
 * @author mahaiyuan
 * @ClassName: CreateNodeTest
 * @date 2016-07-22 下午1:58
 */
public class CreateNodeTest {

  private CuratorFramework client;

  @Before
  public void before(){
    String connectionStr = "127.0.0.1:2181,127.0.0.1:2183,127.0.0.1:2183";
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
    client = CuratorFrameworkFactory.builder()
            .connectString(connectionStr)
            .retryPolicy(retryPolicy)
            .sessionTimeoutMs(5000)
            .build();
    client.start();
  }

  @Test
  public void testCreateNode01() throws Exception {
    String path = "/c1";  //节点路径
    String result = client.create().forPath(path);  //创建一个永久节点,节点内容为空
    System.out.println("result = " + result);
  }

  /**
   * 创建一个有内容的节点
   * @throws Exception
   */
  @Test
  public void testCreateNode02() throws Exception {
    String path = "/c2";  //节点路径
    String content = "你好Curator";
    String result = client.create().forPath(path, content.getBytes("UTF-8"));  //创建一个带数据的永久节点
    System.out.println("result = " + result);
  }

  /**
   * 创建一个临时节点
   * @throws Exception
   */
  @Test
  public void testCreateNode03() throws Exception {
    String path = "/c3";  //节点路径
    String content = "你好Curator";
    String result = client.create().withMode(CreateMode.EPHEMERAL).forPath(path, content.getBytes("UTF-8"));  //创建临时节点
    System.out.println("result = " + result);
    Thread.sleep(Integer.MAX_VALUE);
  }

  /**
   * 创建一个节点,如果父节点不存在则自动创建
   * @throws Exception
   */
  @Test
  public void testCreateNode04() throws Exception {
    String path = "/study/c4";  //节点路径,父节点/study不存在
    String content = "你好Curator";
    String result = client.create()
            .creatingParentsIfNeeded()  //如果需要则创建父节点
            .withMode(CreateMode.EPHEMERAL)
            .forPath(path, content.getBytes("UTF-8"));  //创建临时节点
    System.out.println("result = " + result);
    Thread.sleep(Integer.MAX_VALUE);
  }

}
