package org.zk.www;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.data.Stat;

public class AdminClient implements Watcher{
	ZooKeeper zk;
	String hostPort;
	public AdminClient(String hostPort) {
		this.hostPort = hostPort;
	}
	
	void listState() throws KeeperException {
		Stat stat = new Stat();
		try {
			byte[] masterDate =zk.getData("/master", false, stat);
		} catch (NodeExistsException e) {
			System.out.println("No Master");
			e.printStackTrace();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}
	
	public void process(WatchedEvent arg0) {
		System.out.println("argo");
	}

}
