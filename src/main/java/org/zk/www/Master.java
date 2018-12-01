package org.zk.www;

import java.io.IOException;
import java.util.Random;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.ConnectionLossException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;

public class Master implements Watcher {
	private ZooKeeper zk = null;
	private String hostPort;
	private String serverId = Integer.toString((new Random()).nextInt());
	protected boolean isLeader = false;

	public Master(String hostPort) {
		this.hostPort = hostPort;
	}

	void startZk() {
		try {
			zk = new ZooKeeper(hostPort, 5000, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void stopZk() throws InterruptedException {
		zk.close();
	}

	public void process(WatchedEvent event) {
		if (event.getState() == KeeperState.SyncConnected) {
			System.out.println("Connected");
		} else {
			System.out.println("false");
		}
	}

	boolean checkMaster() throws KeeperException, InterruptedException {
		while (true) {
			Stat stat = new Stat();
			try {
				byte[] data = zk.getData("/master", false, stat);
				isLeader = new String(data).equals(serverId);
				return true;
			} catch (NodeExistsException e) {
				return false;
			} catch (ConnectionLossException e) {
				e.printStackTrace();
			}
		}
	}

	void runForMaster() throws InterruptedException, KeeperException {
		while (true) {
			try {
				zk.create("/master", serverId.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
				isLeader = true;
				break;
			} catch (NodeExistsException e) {
				isLeader = false;
				break;
			} catch (ConnectionLossException e) {
			}
			if (checkMaster())
				break;
		}
	}
}
