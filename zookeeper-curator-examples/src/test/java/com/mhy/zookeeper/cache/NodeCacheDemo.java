package com.mhy.zookeeper.cache;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @author mahaiyuan
 * @ClassName: NodeCacheDemo
 * @date 2016-07-25 上午9:35
 */
public class NodeCacheDemo {
  public static void main(String[] args) throws Exception {
    CuratorFramework client = null;
    String connectStr = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
    client = CuratorFrameworkFactory.builder()
            .connectString(connectStr)
            .retryPolicy(retryPolicy)
            .sessionTimeoutMs(5000)
            .build();
    client.start();

    String path = "/nodecache";
    String content = "测试nodecache";
    client.create()
            .withMode(CreateMode.EPHEMERAL)
            .forPath(path, content.getBytes("UTF-8"));


    final NodeCache cache = new NodeCache(client, path, false);
    cache.start();
    cache.getListenable().addListener(new NodeCacheListener() {
      @Override
      public void nodeChanged() throws Exception {
        System.out.println("node data update. new data is:" + new String(cache.getCurrentData().getData()));
      }
    });
    Thread.sleep(1000 * 60);
  }
}
