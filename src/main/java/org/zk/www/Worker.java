package org.zk.www;

import java.io.IOException;
import java.util.Random;

import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Worker implements Watcher {
	private static Logger log = LoggerFactory.getLogger(Worker.class);
	ZooKeeper zk;
	String hostPort;
	int sessionTimeout;
	String serverId = Integer.toHexString(new Random().nextInt());

	public Worker(String hostPort) {
		this.hostPort = hostPort;
	}

	public void startZK() throws IOException {
		zk = new ZooKeeper(hostPort, sessionTimeout, this);
	}
	
	public void process(WatchedEvent event) {
		log.info(event.toString()+","+hostPort);
	}
	
    void register() {
    	zk.create("/workers/worker-"+serverId, "Idle".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,createWorkerCallback, null);
    }	
	
	StringCallback createWorkerCallback = new StringCallback() {
		
		public void processResult(int rc, String path, Object ctx, String name) {
			switch (Code.get(rc)) {
			case CONNECTIONLOSS:
				register();
				break;
			case OK:
				log.info("Registered successfully: "+ serverId);
				break;
			case NODEEXISTS:
				log.warn("Already registered" + serverId);
			default:
				log.error("Something went wrong : ", KeeperException.create(Code.get(rc)), path);
				break;
			}
		}
	};
}
