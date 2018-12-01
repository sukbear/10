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

	// ��������
	public static void createConn(String ipHost, int timeout) {
		try {
			zooKeeper = new ZooKeeper(ipHost, timeout, new Watcher() {
				// ����¼�
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
	 * �����ڵ�ʹ����ӽڵ����������nodePath �����ڵ� nodePath Ϊ "/nodeFather" �������ӽڵ�ʱ nodePath
	 * Ϊ"/nodeFather/nodeSon" 1.CreateMode ȡֵ PERSISTENT���־û������Ŀ¼�ڵ�洢�����ݲ��ᶪʧ
	 * PERSISTENT_SEQUENTIAL��˳���Զ���ŵ�Ŀ¼�ڵ㣬����Ŀ¼�ڵ����ݵ�ǰ�ѽ����ڵĽڵ����Զ���
	 * 1��Ȼ�󷵻ظ��ͻ����Ѿ��ɹ�������Ŀ¼�ڵ����� EPHEMERAL����ʱĿ¼�ڵ㣬һ����������ڵ�Ŀͻ�����������˿�Ҳ����
	 * session���ڳ�ʱ�����ֽڵ�ᱻ�Զ�ɾ�� EPHEMERAL_SEQUENTIAL����ʱ�Զ���Žڵ�
	 */
	public static void createNode(String nodePath, String nodeData) {
		try {
			zooKeeper.create(nodePath, nodeData.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			System.out.println("�����ڵ�" + nodePath + "�ɹ����ڵ�����Ϊ��" + nodeData);
		} catch (KeeperException e) {
			System.out.println("error!!!,Node already exists");
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void readNode(String nodePath) {
		try {
			System.out.println(nodePath + "�ڵ�����" + new String(zooKeeper.getData(nodePath, false, null)));
		} catch (KeeperException e) {
			System.out.println("��ȡ�ڵ�" + nodePath + "������");
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
				System.out.println(path + "��û���ӽڵ�");
			} else {
				System.out.println(path + "�����ӽڵ�");
				for (String child : list) {
					System.out.println("�ӽڵ㣺 " + child);
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
			System.out.println("���½ڵ�"+nodePath+"������Ϊ"+modifyNodeData);
		} catch (KeeperException e) {
			System.out.println("���µĽڵ�"+nodePath+"������");
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteNode(String nodePath) {
		try {
			zooKeeper.delete(nodePath, -1);
			System.out.println("�ڵ�"+nodePath+"ɾ���ɹ�");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (KeeperException e) {
			System.out.println("ɾ��ʧ�ܣ�ɾ���ڵ㲻���ڻ�û��ɾ���ӽڵ�ֱ��ɾ�����ڵ㣬û�нڵ�ɾ��");
			e.printStackTrace();
		}
	}
	
	public static void closeConnect() {
		try {
			zooKeeper.close();
			System.out.println("�ر����ӳɹ�");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
