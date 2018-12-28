package ssd8.rmi.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * User对象
 * @author Patchouli
 * @version 1.1
 */
public class User implements Serializable{
	private String name;
	private String password;
	
	/**
	 * 带参的构造函数
	 * @param name 用户姓名
	 * @param password 用户密码
	 */
	public User(String name, String password) {
		super();
		this.name = name;
		this.password = password;
	}

	/**
	 * 
	 * @return 返回用户姓名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置用户姓名
	 * @param name 用户姓名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return 返回用户密码
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 设置用户密码
	 * @param password 用户密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", password=" + password + "]";
	}
	
}
