package ssd8.rmi.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import ssd8.rmi.bean.User;
import ssd8.rmi.rface.UserInterface;

public class UserInterfaceImplement extends UnicastRemoteObject implements UserInterface{

	private ArrayList<User> userList=new ArrayList<User>();//用户列表
	
	/**
	 * 无参构造函数，必须存在，且抛出异常RemoteException
	 * @throws RemoteException
	 */
	public UserInterfaceImplement() throws RemoteException{
	}

	@Override
	public ArrayList<User> getUserList()throws RemoteException {
		return this.userList;// 返回userList
	}
	
	@Override
	public User getUserByName(String name) throws RemoteException {
		for(User user:userList){
			if(user.getName().equals(name)){// 查找名字一样的User
				return user;// 返回符合条件的User实例
			}
		}
		return null;// 未找到相应用户
	}
	
	@Override
	public boolean addUser(User user) throws RemoteException {
		int isExist = 0;// 用来判断是否重名
		
		for(User temp:userList){
			if(temp.getName().equals(user.getName())){// 存在相同用户名的用户
				isExist = 1;// 标记置为存在
			}
		}
		if(isExist == 0){// 不存在重复用户
			userList.add(user);// 添加用户
			System.out.println("新添加的用户：" + user.toString());
			return true;// 添加成功
		}else return false;// 添加失败
	}
}
