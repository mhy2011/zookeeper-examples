package com.mhy.zookeeper.cache;

import com.mhy.zookeeper.BaseTest;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.zookeeper.CreateMode;
import org.junit.Before;
import org.junit.Test;

/**
 * @author mahaiyuan
 * @ClassName: NodeCacheTest
 * @date 2016-08-01 下午2:02
 */
public class NodeCacheTest extends BaseTest {

  @Test
  public void test01() throws Exception {
    String path = "/nodecache";

    //先创建一个节点供下面使用
    client.create().withMode(CreateMode.EPHEMERAL).forPath(path);

    final NodeCache cache = new NodeCache(client, path, false);
    cache.start(true);
    cache.getListenable().addListener(new NodeCacheListener() { //添加一个监听
      @Override
      public void nodeChanged() throws Exception {
        System.out.println("节点新内容为:" + new String(cache.getCurrentData().getData()));
      }
    });
    
    //修改节点数据,看是否会监听到
    String content = null;
    for (int i = 0; i < 5; i++) {
      content = "新内容" + i;
      client.setData().forPath(path, content.getBytes("UTF-8"));  //更新节点数据
      Thread.sleep(1000);
    }

    //删除节点,看是否会监听
    client.delete().deletingChildrenIfNeeded().forPath(path);

    Thread.sleep(Integer.MAX_VALUE);

  }
}
