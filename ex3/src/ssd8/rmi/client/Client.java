package ssd8.rmi.client;

import ssd8.rmi.rface.MeetingInterface;
import ssd8.rmi.rface.UserInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import ssd8.rmi.bean.Meeting;
import ssd8.rmi.bean.User;

public class Client {
	
	static Meeting meeting;
	static int meetingID;
	static User host;
	static MeetingInterface meetingInterface;// 操作会议的接口
	static UserInterface userInterface;// 操作用户的接口
	static SimpleDateFormat format = new SimpleDateFormat("Y-M-d-k:m");// 格式化日期

	public static void main(String[] args) throws RemoteException,
			MalformedURLException, NotBoundException, ParseException {
		
		// 查询远程对象
		meetingInterface = (MeetingInterface) Naming.lookup("MeetingRemote");
		userInterface = (UserInterface) Naming.lookup("UserRemote");

		// 获取输入，根据用户名和密码创建客户端
		String name;// 用户名
		String password;// 密码
		Scanner scan = new Scanner(System.in);
		while (true) {
			System.out.print("输入用户名:");
			name = scan.nextLine();
			System.out.print("输入密码:");
			password = scan.nextLine();
			host = new User(name, password);
			boolean success = userInterface.addUser(host);// 添加用户
			
			if (success) {// 添加成功
				break;
			} else {// 用户名重复，添加失败
				System.err.println("当前用户名重复，请重新输入！");
			}
		}
		
		int count = 1;// 初始用户数为1
		
		while (true) {// 检测用户的数量，数量大于1时，跳出循环
			if (userInterface.getUserList().size() > 1) {// 用户数量大于1
				break;
			} else {// 用户数量仅有1个
				if (count == 1) {// 第一次检测到只有一个用户，给出提示
					System.out.println("当前用户只有一个，请打开第二个客户端注册用户！");
					count = 2;
				}
			}
		}
		
		System.out.println("客户端启动成功！");//启动成功，打印菜单
		printMenu();// 输出菜单
		
		while (true) {// 开始循环操作
			String commend = scan.nextLine();
			if (commend.startsWith("add")) {// 输入为add时添加会议
				addMeeting(commend);
				continue;
			} else if (commend.startsWith("delete")) {// 输入为delete时删除会议
				deleteMeeting(commend);
				continue;
			}else if (commend.equals("clear")) {// 输入为clear时清空会议记录
				meetingInterface.clearMeeting(host);
				System.out.println("Clear Finished.");
				continue;
			}  else if (commend.startsWith("query")) {// 输入为query时查询会议
				queryMeeting(commend);
				continue;
			} else if (commend.equals("help")) {// 输入为help时打印菜单
				printMenu();
				continue;
			} else if (commend.equals("quit")) {// 输入为quit时程序结束
				System.out.println("Quit Successful.");
				break;
			} else{
				System.err.println("Invalid Commend.");// 命令不存在
				continue;
			}
		}
	}

	/**
	 * 添加会议
	 * 
	 * @param commend 命令
	 * @throws RemoteException
	 */
	public static void addMeeting(String commend) throws RemoteException {
		
		String[] arg = commend.split(" ");
		int length = arg.length;
		
		if (arg.length != 5) {// 参数个数不为5时，给出错误提示
			System.err.println("The Parameter Number Is Error(The Number Should Be 5 !)");
		} else {
			
			ArrayList<User> userList = userInterface.getUserList();// 获取用户列表
			
			User participant = userInterface.getUserByName(arg[1]);// 根据命令创建会议参与者
			
			//对会议参与者进行有效性查询
			if (participant == null) {// 如果未找到该用户，给出错误提示
				System.err.println("The User Is Not Exist !");
			} else if (participant.getName().equals(host.getName())) {// 如果输入的用户名为本人，给出错误提示
				System.err.println("You Should Input Another User As The Participant !");
			} else {
				meetingID ++;// 预创建会议，meetingID++
				try {
					Date start = format.parse(arg[2]);// 会议的开始时间
					Date end = format.parse(arg[3]);// 会议的结束时间
					String title = arg[4];// 会议的标题
					ArrayList<User> users = new ArrayList<User>();// 用户列表
					users.add(host);
					users.add(participant);
					meeting = new Meeting(meetingID, host, users, start, end, title);// 实例化会议
					if (!meetingInterface.addMeeting(meeting)) {
						meetingID --;// 添加会议时出问题，会议ID恢复
						System.err.println("This Time Has Another Meeting, Please Choose Another Time !");// 远程方法会进行时间重叠检测
					} else {
						System.out.println("Add Successful !");
					}
				} catch (ParseException e) {// 时间格式错误
					System.err.println("Please Input The Correct Time Format (Time Format : 2018-12-18-16:00) !");
					meetingID --;// 添加会议时出问题，会议ID恢复
				}
			}
		}
	}
	/**
	 * 删除会议
	 * @param commend 命令
	 * @throws RemoteException
	 */
	public static void deleteMeeting(String commend) throws RemoteException {

		String[] arg = commend.split(" ");
		if (arg.length != 2) {// 参数个数不为2时，给出错误提示
			System.err.println("The Parameter Number Is Error(The Number Should Be 2 !)");
		} else {
			
			int id = Integer.parseInt(arg[1]);// 获取会议的ID
			boolean success = meetingInterface.deleteMeetingById(id, host);
			if (success) {
				System.out.println("Delete Meeting Successful !");
			} else {
				System.err.println("Delete Meeting Failed, The Host Of The Meeting Is Not You !");
			}
		}
	}

	/**
	 * 查询会议
	 * @param commend 命令
	 * @throws RemoteException
	 */
	public static void queryMeeting(String commend) throws RemoteException {
		
		String[] arg = commend.split(" ");
		ArrayList<Meeting> meetingList = new ArrayList<Meeting>();
		
		if (arg.length != 3) {// 参数个数不为3时，给出错误提示
			System.err.println("The Parameter Number Is Error(The Number Should Be 3 !)");
		} else {
			try {
				Date start = format.parse(arg[1]);// 会议开始时间
				Date end = format.parse(arg[2]);// 会议结束时间
				
				meetingList = meetingInterface.queryMeetingByTime(start, end);
				if (meetingList == null) {
					System.out.println("No Meeting !");
				} else {// 打印会议信息
					for (Meeting temp : meetingList) {
						System.out.println(temp.toString());
					}
				}
			} catch (ParseException e) {// 时间格式错误
				System.err.println("Please Input The Correct Time Format (Time Format : 2018-12-18-16:00) !");
			}
		}
	}

	/**
	 * 打印菜单
	 */
	public static void printMenu() {
		System.out.println("RMI Menu:");
		System.out.println("    1.add");
		System.out.println("        arguments: <paticipantName> <start> <end> <title> Time Format : 2018-12-18-16:00");
		System.out.println("    2.delete");
		System.out.println("        arguments: <meetingID>");
		System.out.println("    3.clear");
		System.out.println("        arguments: no args");
		System.out.println("    4.query");
		System.out.println("        arguments: <start> <end>");
		System.out.println("    5.help");
		System.out.println("        arguments: no args");
		System.out.println("    6.quit");
		System.out.println("        arguments: no args");
	}
}