package com.mhy.zookeeper.cases;

import com.mhy.zookeeper.ZkClient;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Master选举示例
 * @author mahaiyuan
 * @ClassName: LeaderSelectorDemo
 * @date 2016-08-15 下午9:59
 */
public class LeaderSelectorDemo {

  private static String connectStr = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";  //连接地址
  private static RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);

  public static void main(String[] args) throws Exception {

    String path = "/mhy/app1/leader_selector";

    /*ExecutorService exec = Executors.newFixedThreadPool(5);
    for (int i = 0; i < 5; i++) {
      exec.execute(new LeaderSelectorService(CuratorFrameworkFactory.newClient(connectStr, retryPolicy), path));
    }*/
    CuratorFramework client = ZkClient.getInstance().getClient();
    LeaderSelector selector = new LeaderSelector(client, path, new LeaderSelectorListenerAdapter() {
      @Override
      public void takeLeadership(CuratorFramework client) throws Exception {
        System.out.println(Thread.currentThread().getName() + " 成为Master角色");
        Thread.sleep(3000);
        System.out.println(Thread.currentThread().getName() + " 执行完成,释放Master权利");
      }
    });
    selector.autoRequeue();
    selector.start();
    Thread.sleep(Integer.MAX_VALUE);
  }
}
