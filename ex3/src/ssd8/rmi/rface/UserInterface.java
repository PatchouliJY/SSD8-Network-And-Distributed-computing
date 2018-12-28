package ssd8.rmi.rface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import ssd8.rmi.bean.User;

public interface UserInterface extends Remote{

	/**
	 * 根据用户姓名查询用户
	 * @param username 用户姓名
	 * @return 返回查询被用户的信息
	 * @throws RemoteException
	 */
	public User getUserByName(String username)throws RemoteException;

	/**
	 * 添加用户，并返回是否添加成功
	 * @param user 待添加用户
	 * @return 返回用户是否添加成功
	 * @throws RemoteException
	 */
	public boolean addUser(User user)throws RemoteException;

	/**
	 * 返回用户列表
	 * @return 返回用户列表
	 * @throws RemoteException
	 */
	public ArrayList<User> getUserList()throws RemoteException;
	
}

