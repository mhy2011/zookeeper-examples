package com.mhy.zookeeper.cases;

import com.mhy.zookeeper.ZkClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;

/**
 * @author mahaiyuan
 * @ClassName: PathChildrenCacheDemo
 * @date 2016-08-18 下午3:09
 */
public class PathChildrenCacheDemo {

  public static void main(String[] args) throws Exception {
    CuratorFramework client = ZkClient.getInstance().getClient();
    String path = "/mhy";
    if(null == client.checkExists().forPath(path)){ //节点不存在
      client.create().creatingParentsIfNeeded().forPath(path);  //创建节点
    }

    PathChildrenCache cache = new PathChildrenCache(client, path, true);
    cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);

    cache.getListenable().addListener(new PathChildrenCacheListener() {
      @Override
      public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
        System.out.println("事件类型:" + event.getType() + ",数据内容:" + event.getData());
      }
    });

    //下面触发事件
    String chlildPath = "/mhy/test01";
    client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(chlildPath, "Hello".getBytes("UTF-8"));  //创建事件
    client.setData().forPath(chlildPath, "Hello2".getBytes("UTF-8"));  //更新事件
    client.delete().forPath(chlildPath);  //删除事件

    Thread.sleep(5000);

    //Curator只监听第一级叶子节点下相关事件,不监听二级or更多级节点下相关事件
    String chlildPath2 = "/mhy/test01/test001";
    client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(chlildPath2);

    Thread.sleep(Integer.MAX_VALUE);
  }
}
