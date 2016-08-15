package com.mhy.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.junit.Test;

/**
 * Master选举实例
 * @author mahaiyuan
 * @ClassName: MasterSelectTest
 * @date 2016-08-01 下午5:19
 */
public class MasterSelectTest extends BaseTest {

  @Test
    public void test01() throws InterruptedException {
    String masterPath = "/masterPath";
    for (int i = 0; i < 10; i++) {
      final int index = i;
      LeaderSelector selector = new LeaderSelector(client, masterPath, new LeaderSelectorListenerAdapter() {
        @Override
        public void takeLeadership(CuratorFramework client) throws Exception {
          System.out.println(index + "成为master......");
          Thread.sleep(3000);
          System.out.println(index + "完成操作,释放master权利......");
        }
      });
      selector.autoRequeue();
      selector.start();
    }


    Thread.sleep(Integer.MAX_VALUE);
  }

}
