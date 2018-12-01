package org.zk.www;

import java.io.IOException;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

public class ZookeeperConn implements Watcher {
	private static ZooKeeper zooKeeper;

	public void process(WatchedEvent event) {
		System.out.println("Receive watched event :" + event);
	    if(event.getState() == KeeperState.SyncConnected){
            System.out.println("ZooKeeper session established.");  
            doSomething();
        }
	}

	private void doSomething() {
		System.out.println("do something");
	}
	public static void main(String[] args) throws IOException, InterruptedException {
		zooKeeper = new ZooKeeper("0.0.0.0:2181", 5000, new ZookeeperConn());
        System.out.println(zooKeeper.getState());
        Thread.sleep(60000);
	}
}
