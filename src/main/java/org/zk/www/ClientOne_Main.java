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
		clientOne.createNode(nodePath, "���ǵ�һ���ڵ�");
		clientOne.readNode(nodePath);
	      if(ClientOne.isExists(nodePath)!=null)
          {
              System.out.println(nodePath+"�ڵ����");
          }else{
              System.out.println(nodePath+"�ڵ㲻����");
          }
	    clientOne.updateNode(nodePath, "��һ���ڵ㱻�޸���");
	    clientOne.createNode(sonNodePath, "�����ӽڵ�");
	    clientOne.readNode(sonNodePath);
	    clientOne.deleteNode(sonNodePath);
	    clientOne.closeConnect();
	}
}
