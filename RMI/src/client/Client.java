package client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import rface.MessageInterface;
import rface.UserInterface;
import bean.Message;
import bean.User;

public class Client {

	static UserInterface userInterface;// 操作用户的接口
	static MessageInterface messageInterface;// 操作消息的接口
	private static Scanner scan;

	public static void main(String[] args)
			throws RemoteException, MalformedURLException, NotBoundException, ParseException {

		userInterface = (UserInterface) Naming.lookup("UserRemote");
		messageInterface = (MessageInterface) Naming.lookup("MessageRemote");

		scan = new Scanner(System.in);

		System.out.println("客户端启动成功！");
		printMenu();// 输出菜单

		while (true) {// 开始循环操作
			String commend = scan.nextLine();
			if (commend.startsWith("register")) {// 命令为register注册用户
				register(commend);
				continue;
			} else if (commend.equals("showusers")) {// 命令为showusers打印用户信息
				showUser();
				continue;
			} else if (commend.startsWith("checkmessages")) {// 命令为checkmessages时查看留言
				checkmessages(commend);
				continue;
			} else if (commend.startsWith("leavemessage")) {// 命令为leavemessages时留言
				leavemessage(commend);
				continue;
			} else if (commend.equals("quit")) {// 命令为quit时程序结束
				System.out.println("Quit Successful.");
				break;
			} else if (commend.equals("help")) {// 命令为help时打印菜单
				printMenu();// 输出菜单
				continue;
			} else {
				System.err.println("Invalid Commend !");// 对于不存在的命令给出错误提示
				continue;
			}
		}

	}

	private static void showUser() throws RemoteException {
		ArrayList<User> userList = userInterface.showusers();// 获取所有用户列表
		String userInfo = "";
		
		if (userList.isEmpty()) {// 判断列表是否为空
			userInfo = "No User !";
		} else {
			for (User temp : userList)
				userInfo += temp.toString() + "\n";// 打印用户信息
		}
		System.out.println(userInfo);
	}

	public static void register(String commend) throws RemoteException {
		String[] args = commend.split(" ");

		if (args.length != 3) {// 参数数不正确，打印错误信息
			System.err.println("The Parameter Number Is Error(The Number Should Be 2 !)");
		} else {// 进行注册
			String username = args[1];
			String password = args[2];
			int isExist = 0;
			for (User temp : userInterface.getUserList()) {// 判断用户名是否存在
				if (temp.getName().equals(username))
					isExist = 1;
			}
			if (isExist == 1) {
				System.err.println("The User Name Has Already Exist !");
			} else {// 用户名存在，调用远程接口进行注册
				userInterface.register(username, password);
				System.out.println("Register User " + username + " Successful !");
			}
		}
	}

	public static void checkmessages(String commend) throws RemoteException {
		String[] args = commend.split(" ");

		if (args.length != 3) {// 参数数不正确，打印错误信息
			System.err.println("The Parameter Number Is Error(The Number Should Be 3 !)");
		} else {
			String username = args[1];
			String password = args[2];
			if (userInterface.checkUser(username, password) == 1) {
				ArrayList<Message> receive = new ArrayList<Message>();
				receive = messageInterface.checkmessages(username, password);
				if (receive.isEmpty()) {
					System.err.println("You Don't Have Any Message !");
				} else {
					String messageInfo = "";
					for (Message temp : receive) {
						if (temp.getReceiver_name().equals(username))
							messageInfo += temp.toString() + "\n";
					}
					if (messageInfo.equals("")) {
						System.out.println("You Don't Have Any Message !");
					} else
						System.out.println(messageInfo);
				}
			} else if (userInterface.checkUser(username, password) == 0) {
				System.err.println("Your User Name Is Not Exist !");
			} else if (userInterface.checkUser(username, password) == 2) {
				System.err.println("Wrong Password !");
			}
		}
	}

	public static void leavemessage(String commend) throws RemoteException {
		String[] args = commend.split(" ");
		Date date = new Date();

		if (args.length < 5) {// 参数数不正确，打印错误信息
			System.err.println("The Parameter Number Is Error(The Number Should Be Bigger Than 5 !)");
		} else {
			String username = args[1];
			String password = args[2];
			if (userInterface.checkUser(username, password) == 1) {// 判断发送方用户是否存在
				String receiver_name = args[3];
				String message_text = "";

				for (int i = 4; i < args.length; i++) {// 将message中的·空格进行还原
					message_text += args[i] + " ";
				}

				if (receiver_name.equals(username)) {
					System.err.println("Please Input Another People !");
				}

				int isExist = 0;// 标记，判断接受方是否存在
				for (User temp : userInterface.getUserList()) {
					if (temp.getName().equals(receiver_name)) {// 接受方存在，进行留言
						messageInterface.leavemessage(username, password, receiver_name, date, message_text);
						isExist = 1;
						System.out.println("Leaving Message Successful !");
					}
				}
				if (isExist == 0) {// 接收方用户不存在
					System.err.println("No Such User! Please Check The Receiver Again !");
				}
			} else if (userInterface.checkUser(username, password) == 0) {
				System.err.println("Your User Name Is Not Exist !");// 发送方用户名不存在，打印错误信息
			} else if (userInterface.checkUser(username, password) == 2) {// 发送方密码输入错误，打印错误信息
				System.err.println("Wrong Password !");
			}
		}
	}

	public static void printMenu() {
		System.out.println("RMI Menu:");
		System.out.println("    1.register");
		System.out.println("         arguments: <username> <password>");
		System.out.println("    2.showusers");
		System.out.println("         arguments: no args");
		System.out.println("    3.checkmessages");
		System.out.println("         arguments: <username> <password>");
		System.out.println("    4.leavemessage");
		System.out.println("         arguments: <username> <password> <receiver_name> <message_text>");
		System.out.println("    5.help");
		System.out.println("         arguments: no args");
		System.out.println("    6.quit");
		System.out.println("         arguments: no args");
	}
}