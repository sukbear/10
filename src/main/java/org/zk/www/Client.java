package org.zk.www;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.KeeperException.NodeExistsException;

public class Client implements Watcher {
	ZooKeeper zk;
	String hostPort;

	public Client(String hostPort) {
		this.hostPort = hostPort;
	}

	void startZK() throws IOException {
		zk = new ZooKeeper(hostPort, 15000, this);
	}

	String queueCommand(String command) throws KeeperException {
		while (true) {
			String name;
			try {
				name = zk.create("/tasks/task-", command.getBytes(), Ids.OPEN_ACL_UNSAFE,
						CreateMode.EPHEMERAL_SEQUENTIAL);
				return name;
			} catch (NodeExistsException e) {
				System.out.println();
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void process(WatchedEvent event) {
		System.out.println(event);
	}
	public static void main(String[] args) throws IOException, KeeperException {
		Client client = new Client(args[0]);
		client.startZK();
		String nameString = client.queueCommand(args[1]);
		System.out.println("created"+ nameString);
	}
}
