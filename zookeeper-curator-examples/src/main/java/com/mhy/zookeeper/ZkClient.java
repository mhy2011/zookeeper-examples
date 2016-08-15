package com.mhy.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author mahaiyuan
 * @ClassName: BaseClient
 * @date 2016-08-15 下午5:11
 */
public class ZkClient {
  private CuratorFramework client = null;
  private static String connectStr = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";  //连接地址
  private static RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);

  private static ZkClient INSTANCE = new ZkClient();

  private ZkClient(){
    client = CuratorFrameworkFactory.newClient(connectStr, retryPolicy);
    client.start();
  }

  public static ZkClient getInstance(){
    if(null == INSTANCE){
      synchronized (ZkClient.class){
        if(null == INSTANCE){
          INSTANCE = new ZkClient();
        }
      }
    }
    return INSTANCE;
  }

  public CuratorFramework getClient(){
    return client;
  }

}
