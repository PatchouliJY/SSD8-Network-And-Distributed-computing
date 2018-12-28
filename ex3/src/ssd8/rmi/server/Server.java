package ssd8.rmi.server;

import ssd8.rmi.rface.MeetingInterface;
import ssd8.rmi.rface.UserInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Server {

	public static void main(String[] args) throws RemoteException,
			MalformedURLException {

		// 创建远程对象的一个或多个实例，
		MeetingInterface meetingInterface = new MeetingInterfaceImplement();
		UserInterface userInterface = new UserInterfaceImplement();
		
		// 启动RMI注册服务，指定端口为1099　（1099为默认端口）
        // 也可以通过命令 ＄java_home/bin/rmiregistry 1099启动
        // 这里用这种方式避免了再打开一个DOS窗口
        // 而且用命令rmi registry启动注册服务还必须事先用RMIC生成一个stub类为它所用
		LocateRegistry.createRegistry(1099);
		
		// 把User和Meeting注册到RMI注册服务器上
		Naming.rebind("UserRemote", userInterface);
		Naming.rebind("MeetingRemote", meetingInterface);
		
		//启动服务器
		System.out.println("服务器启动成功");
	}
}
