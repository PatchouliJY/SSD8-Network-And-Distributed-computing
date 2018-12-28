package rface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

import bean.Message;

public interface MessageInterface extends Remote{

	/**
	 * 查询消息
	 * @param username 用户名
	 * @param password 用户密码
	 * @return 查询到消息的列表
	 * @throws RemoteException
	 */
	ArrayList<Message> checkmessages(String username,String password)throws RemoteException;
	
	/**
	 * 留言
	 * @param username 用户名
	 * @param password 用户密码
	 * @param receiver_name 接收者名
	 * @param date 日期
	 * @param message_text 内容
	 * @throws RemoteException 
	 */
	void leavemessage(String username,String password,String receiver_name,Date date,String message_text)throws RemoteException;
	
	/**
	 * 获取消息列表
	 * @return 消息列表
	 * @throws RemoteException
	 */
	ArrayList<Message> getMessageList()throws RemoteException;
}
