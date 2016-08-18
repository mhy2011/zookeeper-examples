package com.mhy.zookeeper.cases;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.utils.CloseableUtils;

/**
 * @author mahaiyuan
 * @ClassName: LeaderSelectorService
 * @date 2016-08-15 下午10:08
 */
public class LeaderSelectorService implements Runnable {

  private CuratorFramework client;
  private String path;

  public LeaderSelectorService(CuratorFramework client, String path){
    this.client = client;
    this.path = path;
  }

  @Override
  public void run() {
    client.start();
    LeaderSelector selector = null;
    try {
      selector = new LeaderSelector(client, path, new LeaderSelectorListenerAdapter() {
        @Override
        public void takeLeadership(CuratorFramework client) throws Exception {
          String name = Thread.currentThread().getName();
          System.out.println(name + " 成为Master角色");
          Thread.sleep(3000);
          System.out.println(name + " 执行完成,释放Master权利");

          CloseableUtils.closeQuietly(client);

        }
      });

    } finally {
      CloseableUtils.closeQuietly(selector);
    }
    String name = Thread.currentThread().getName();
    System.out.println(name + "竞争MasterMaster角色..............");

  }
}
