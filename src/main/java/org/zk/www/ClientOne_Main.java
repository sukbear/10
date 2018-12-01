package org.zk.www;
import org.zk.www.ClientOne;
public class ClientOne_Main {
	public static void main(String[] args) {
		String nodePath = "/able";
		String sonNodePath = "/able/son";
		String ipHost = "127.0.0.1:2181";
		int timeOut = 10000;
		ClientOne clientOne = new ClientOne();
		clientOne.createConn(ipHost, timeOut);
		clientOne.createNode(nodePath, "我是第一个节点");
		clientOne.readNode(nodePath);
	      if(ClientOne.isExists(nodePath)!=null)
          {
              System.out.println(nodePath+"节点存在");
          }else{
              System.out.println(nodePath+"节点不存在");
          }
	    clientOne.updateNode(nodePath, "第一个节点被修改了");
	    clientOne.createNode(sonNodePath, "我是子节点");
	    clientOne.readNode(sonNodePath);
	    clientOne.deleteNode(sonNodePath);
	    clientOne.closeConnect();
	}
}
