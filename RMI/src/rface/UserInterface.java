package rface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import bean.User;

public interface UserInterface extends Remote{

	/**
	 * 注册
	 * @param username 用户名
	 * @param password 密码
	 * @return 是否成功
	 * @throws RemoteException
	 */
	boolean register(String username,String password)throws RemoteException;
	
	/**
	 * 展示用户
	 * @return 所有用户的列表
	 * @throws RemoteException
	 */
	ArrayList<User>  showusers()throws RemoteException;
	
	/**
	 * 判断用户是否存在
	 * @param username 用户名
	 * @param password 密码
	 * @return 根据数据输入错误返回相应的提示符
	 * @throws RemoteException
	 */
	int checkUser(String username,String password)throws RemoteException;
	
	/**
	 * 获取用户列表
	 * @return 所有用户的列表
	 * @throws RemoteException
	 */
	ArrayList<User> getUserList()throws RemoteException;
	
}
