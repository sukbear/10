package org.zk.www;

import java.io.IOException;
import java.util.Random;


import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.KeeperException.ConnectionLossException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.AsyncCallback.DataCallback;
import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;

public class MasterAsyn implements Watcher {
	private static Logger log = LoggerFactory.getLogger(new MasterAsyn().getClass().getName());
	private static ZooKeeper zk = null;
	private String hostPort;
	private static String serverId = Integer.toString((new Random()).nextInt());
	static boolean isLeader;
	private String status;
	static StringCallback masterCallback = new StringCallback() {

		public void processResult(int rc, String path, Object ctx, String name) {
			switch (Code.get(rc)) {
			case CONNECTIONLOSS:
				checkMaster();
				return;
			case OK:
				isLeader = true;
			default:
				isLeader = false;
				break;
			}
		}
	};

	public MasterAsyn(String hostPort) {
		this.hostPort = hostPort;
	}

	public MasterAsyn() {
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

	static DataCallback masterCheckCallback = new DataCallback() {
		public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
			switch (Code.get(rc)) {
			case CONNECTIONLOSS:
				checkMaster();
				return;
			case NONODE:
				runForMaster();
				return;
			}
		}
	};

	static void checkMaster() {

		zk.getData("/master", false, masterCheckCallback, null);

	}

	static void runForMaster() {
		zk.create("/master", serverId.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, masterCallback, null);
	}

	void bootstrap() {
		createParent("/workers", new byte[0]);
		createParent("/assign", new byte[0]);
		createParent("/tasks", new byte[0]);
		createParent("/status", new byte[0]);
	}
	
	void createParent(String path, byte[] data) {
		zk.create(path, data, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, createParentCallback, null);
	}

	StringCallback createParentCallback = new StringCallback() {
		public void processResult(int rc, String path, Object ctx, String name) {
			switch (Code.get(rc)) {

			case CONNECTIONLOSS:
				createParent(path, (byte[]) ctx);
				break;
			case OK:
				log.info("Parent created");
				break;
			case NODEEXISTS:
				log.warn("Parent already registered" + path);
			default:
				log.error("Something went wrong : ", KeeperException.create(Code.get(rc)), path);
				break;
			}
		}
	};
	
	StatCallback statusUpdateCallback = new StatCallback() {
		
		public void processResult(int rc, String path, Object ctx, Stat stat) {
			switch (Code.get(rc)) {
			case CONNECTIONLOSS:
				updateStatus((String)ctx);
				return;
			}
		}
	};
	
	synchronized private void updateStatus(String status) {
		if(status == this.status) {
			zk.setData("/workers/" + serverId, status.getBytes(), -1, statusUpdateCallback, status);
		}
	}
	public void setStatus(String status) {
		this.status = status;
		updateStatus(status);
	}
}
