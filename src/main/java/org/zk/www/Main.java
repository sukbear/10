package org.zk.www;

import org.apache.zookeeper.KeeperException;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		Master master = new Master(args[0]);

		master.startZk();

		try {
			master.runForMaster();
		} catch (KeeperException e) {
			e.printStackTrace();
		}

		if (master.isLeader) {
			System.out.println("I'm leader");
			Thread.sleep(60000);
		} else {
			System.out.println("Someone else is leader");
		}
		master.stopZk();
	}
}
