package org.zk.www;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

public class ClientOne {
	private static ZooKeeper zooKeeper = null;

	// 建立连接
	public static void createConn(String ipHost, int timeout) {
		try {
			zooKeeper = new ZooKeeper(ipHost, timeout, new Watcher() {
				// 监控事件
				public void process(WatchedEvent event) {
					if (event.getState() == KeeperState.SyncConnected) {
						System.out.println("ZooKeeper session established.");
					}
				}
			});
		} catch (IOException e) {
			System.out.println("connect fail");
			e.printStackTrace();
		}
	}

	/**
	 * 创建节点和创建子节点的区别在于nodePath 创建节点 nodePath 为 "/nodeFather" 创建其子节点时 nodePath
	 * 为"/nodeFather/nodeSon" 1.CreateMode 取值 PERSISTENT：持久化，这个目录节点存储的数据不会丢失
	 * PERSISTENT_SEQUENTIAL：顺序自动编号的目录节点，这种目录节点会根据当前已近存在的节点数自动加
	 * 1，然后返回给客户端已经成功创建的目录节点名； EPHEMERAL：临时目录节点，一旦创建这个节点的客户端与服务器端口也就是
	 * session过期超时，这种节点会被自动删除 EPHEMERAL_SEQUENTIAL：临时自动编号节点
	 */
	public static void createNode(String nodePath, String nodeData) {
		try {
			zooKeeper.create(nodePath, nodeData.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			System.out.println("创建节点" + nodePath + "成功，节点内容为：" + nodeData);
		} catch (KeeperException e) {
			System.out.println("error!!!,Node already exists");
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void readNode(String nodePath) {
		try {
			System.out.println(nodePath + "节点内容" + new String(zooKeeper.getData(nodePath, false, null)));
		} catch (KeeperException e) {
			System.out.println("读取节点" + nodePath + "不存在");
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void getCountChild(String path) {
		List<String> list = null;
		try {
			list = zooKeeper.getChildren(path, false);
			if (list.isEmpty()) {
				System.out.println(path + "中没有子节点");
			} else {
				System.out.println(path + "中有子节点");
				for (String child : list) {
					System.out.println("子节点： " + child);
				}
			}
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static Stat isExists(String nodePath) {
		Stat stat = null;
		try {
			stat = zooKeeper.exists(nodePath, true);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return stat;
	}
	
	public static void updateNode(String nodePath, String modifyNodeData) {
		try {
			zooKeeper.setData(nodePath, modifyNodeData.getBytes(), -1);
			System.out.println("更新节点"+nodePath+"的数据为"+modifyNodeData);
		} catch (KeeperException e) {
			System.out.println("更新的节点"+nodePath+"不存在");
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteNode(String nodePath) {
		try {
			zooKeeper.delete(nodePath, -1);
			System.out.println("节点"+nodePath+"删除成功");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (KeeperException e) {
			System.out.println("删除失败，删除节点不存在或没有删除子节点直接删除父节点，没有节点删除");
			e.printStackTrace();
		}
	}
	
	public static void closeConnect() {
		try {
			zooKeeper.close();
			System.out.println("关闭连接成功");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
