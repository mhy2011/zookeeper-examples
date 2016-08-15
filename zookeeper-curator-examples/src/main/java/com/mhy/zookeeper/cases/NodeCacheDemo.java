package com.mhy.zookeeper.cases;

import com.mhy.zookeeper.ZkClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.zookeeper.CreateMode;

/**
 * 事件监听示例
 * 可以用于数据发布/订阅场景
 * @author mahaiyuan
 * @ClassName: NodeCacheDemo
 * @date 2016-08-15 下午7:05
 */
public class NodeCacheDemo {
  public static void main(String[] args) throws Exception {
    String path = "/mhy/app1/db_config";

    //先创建该节点以供后面使用
    CuratorFramework client = ZkClient.getInstance().getClient();
    client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);

    NodeCache cache = new NodeCache(client, path);  //监听该节点
    cache.getListenable().addListener(new NodeCacheListener() { //增加监听
      @Override
      public void nodeChanged() throws Exception {
        //TODO 节点内容发生变更时会触发本方法
        System.out.println("节点内容发生变化.......");
        System.out.println("节点当前内容为:" + new String(cache.getCurrentData().getData()));
      }
    });
    cache.start();  //启动监听

    //触发节点内容变更
    String content = null;
    for (int i = 0; i < 5; i++) {
      content = "Hello " + i;
      client.setData().forPath(path, content.getBytes());
      Thread.sleep(1000);
    }

    Thread.sleep(Integer.MAX_VALUE);
  }
}
