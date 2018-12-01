package org.zk.www;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class AdminClient implements Watcher{
	ZooKeeper zk;
	String hostPort;
	public AdminClient(String hostPort) {
		this.hostPort = hostPort;
	}
	public void process(WatchedEvent arg0) {
		System.out.println("argo");
	}

}
