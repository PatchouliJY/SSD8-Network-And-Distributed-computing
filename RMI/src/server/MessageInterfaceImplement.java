package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;

import rface.MessageInterface;
import bean.Message;

public class MessageInterfaceImplement extends UnicastRemoteObject implements MessageInterface {

	private static final long serialVersionUID = 1L;
	ArrayList<Message> messageList = new ArrayList<Message>();

	protected MessageInterfaceImplement() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArrayList<Message> checkmessages(String username,String password) throws RemoteException {
		ArrayList<Message> receive = new ArrayList<Message>();
			for (Message temp : messageList) {
				if (temp.getReceiver_name().equals(username))
					receive.add(temp);// 将收取人是username的消息放入receive列表
			}
		return receive;
	}

	@Override
	public void leavemessage(String username, String password, String receiver_name, Date date, String message_text)
			throws RemoteException {
		Message message = new Message(username, receiver_name, date, message_text);
		messageList.add(message);

	}

	@Override
	public ArrayList<Message> getMessageList() throws RemoteException {
		return this.messageList;
	}

}
