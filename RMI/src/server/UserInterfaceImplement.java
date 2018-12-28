package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import bean.User;
import rface.UserInterface;

public class UserInterfaceImplement extends UnicastRemoteObject implements UserInterface{


	private static final long serialVersionUID = 1L;
	ArrayList<User> userList=new ArrayList<User>();
	
	protected UserInterfaceImplement() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean register(String username, String password)
			throws RemoteException {
		for(User temp : userList){
			if(temp.getName().equals(username)){
				return false;
			}
		}
		User user=new User(username, password);
		userList.add(user);
		return true;
	}

	@Override
	public ArrayList<User> showusers() throws RemoteException {
		return this.userList;
	}

	@Override
	public ArrayList<User> getUserList() throws RemoteException {
		return this.userList;
	}

	@Override
	public int checkUser(String username, String password) throws RemoteException {
		
		int userExist = 0;
		
		int isExistName = 0;
		int isExist = 0;
		for(User temp : getUserList()) {
			if(temp.getName().equals(username)) {
				isExistName = 1;
				if(temp.getPassword().equals(password))
					isExist = 1;
			}	
		}
		if(isExistName == 0) {
			userExist = 0;// 用户名不存在设0
		}else {
			if(isExist == 0) {
				userExist = 2;// 用户名存在，密码错误设2
			}else {
				userExist = 1;// 用户存在设1
			}
		}
		return userExist;
	}

}
