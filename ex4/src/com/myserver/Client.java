package com.myserver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class Client {

	private static int projectID = 0;

	static ServiceService service = new ServiceService();
	static MyServer myServer = service.getMyServerPort();

	static SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");

	private static Scanner scan;

	public static void main(String[] args) throws Exception {
		System.out.println("客户端启动成功！");//启动成功，打印菜单
		printMenu();

		scan = new Scanner(System.in);

		/**
		 * 开始操作
		 */
		while (true) {// 开始循环操作
			String commend = scan.nextLine();
			if (commend.startsWith("register")) {// 命令为register 注册用户
				register(commend);
				continue;
			} else if (commend.startsWith("add")) {// 命令为add 添加项目
				add(commend);
				continue;
			} else if (commend.startsWith("delete")) {// 命令为delete 删除项目
				delete(commend);
				continue;
			} else if (commend.startsWith("clear")) {// 命令为clear 清除指定用户的项目
				clear(commend);
				continue;
			} else if (commend.startsWith("query")) {// 命令为query 查询项目
				query(commend);
				continue;
			} else if (commend.startsWith("help")) {// 命令为help 打印菜单
				printMenu();
				continue;
			} else if (commend.startsWith("quit")) {// 命令为quit 退出系统
				System.err.println("Quit Successful.");
				break;
			} else {// 命令不存在
				System.err.println("Invalid Commend.");
			}
		}

	}

	/**
	 *  注册用户
	 * @param commend 命令
	 */
	public static void register(String commend) {
		String username;
		String password;
		User user = new User();

		String[] tokens = commend.split(" ");
		if (tokens.length != 3) {// 参数数不正确，打印错误信息
			System.err.println("The Parameter Number Is Error(The Number Should Be 3 !)");
		} else {
			username = tokens[1];
			password = tokens[2];

			user.setUsername(username);// 无法使用构造函数，手动设置 userName
			user.setPassword(password);// 无法使用构造函数，手动设置 password

			boolean isSuccess = myServer.addUser(user);

			if (isSuccess == true) {// userName未被注册，则注册成功
				System.out.println("Register User " + username + " Successful !");
			} else {// userName已被注册，则注册失败
				System.err.println("The User Name Has Already Exist !");
			}
		}
	}

	/**
	 * 添加Project
	 * @param commend 命令
	 * @throws Exception
	 */
	public static void add(String commend) throws Exception {
		String creator;
		Date startTime;
		Date endTime;
		String title;

		String[] tokens = commend.split(" ");
		
		if (tokens.length != 5) {// 参数数不正确，打印错误信息
			System.err.println("The Parameter Number Is Error(The Number Should Be 5 !)");
		} else {
			creator = tokens[1];
			startTime = time.parse(tokens[2]);
			endTime = time.parse(tokens[3]);
			title = tokens[4];
			projectID++;

			Project project = new Project();

			//无法调用构造函数，进行手动设置
			project.setCreator(creator);
			project.setStartTime(transfer(startTime));
			project.setEndTime(transfer(endTime));
			project.setTitle(title);
			project.setProjectID(projectID);

			if (myServer.queryUser(creator) != null) {// 查询到Creator时
				if (startTime.before(endTime)) {// 判断时间合法性
					int isSuccess = myServer.addProject(project);
					if (isSuccess == 0) {// 添加失败
						System.err.println("Add Project Failed!");
						projectID--;
					} else if (isSuccess == 1) {// 添加成功
						System.out.println("Add Project " + projectID + " successful !");
					} else {// 时间冲突，给出错误信息
						System.err.println("This Time Has Another Project, Please Choose Another Time !");
						projectID--;
					}
				} else {
					System.err.println("Start Time Should Before Than End Time !");
					projectID--;
				}
			} else {// Creator不存在，给出错误信息
				System.err.println("The Creator Is Not Exist !");
				projectID--;
			}

		}
	}

	/**
	 * 删除项目
	 * @param commend 命令
	 */
	public static void delete(String commend) {
		String username;

		String[] tokens = commend.split(" ");
		
		if (tokens.length != 3) {// 参数数不正确，打印错误信息
			System.err.println("The Parameter Number Is Error(The Number Should Be 3 !)");
		} else {
			username = tokens[1];
			int id = Integer.valueOf(tokens[2]);
			boolean isSuccess = myServer.deleteProject(id, username);
			
			if (isSuccess) {// 删除成功
				System.out.println("Delete Successful !");
			} else {// 删除失败，给出错误信息
				System.err.println("Delete Failed ! Please Check The Creator And The ProjectID !");
			}
		}
	}

	/**
	 * 清除用户所有项目
	 * @param commend 命令
	 */
	public static void clear(String commend) {
		String username;

		String[] tokens = commend.split(" ");
		
		if (tokens.length != 2) {// 参数数不正确，打印错误信息
			System.err.println("The Parameter Number Is Error(The Number Should Be 2 !)");
		} else {
			username = tokens[1];
			
			if (myServer.queryUser(username) != null) {// 用户存在
				boolean isSuccess = myServer.clearProject(username);
				if (isSuccess) {// 成功清除该用户项目
					System.out.println("Clear Successful !");
				} else {// 清除失败，给出错误信息
					System.err.println("Clear Project Failed ! Please Check The Creator !");
				}
			} else {// Creator不存在，给出错误信息
				System.err.println("The Creator Is Not Exist !");
			}
		}
	}

	/**
	 * 查询用户所有项目
	 * @param commend 命令
	 * @throws Exception
	 */
	public static void query(String commend) throws Exception {
		Date startTime;
		Date endTime;
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");// 设置时间格式

		String[] tokens = commend.split(" ");

		if (tokens.length != 3) {// 参数数不正确，打印错误信息
			System.err.println("The Parameter Number Is Error(The Number Should Be 3 !)");
		} else {
			startTime = time.parse(tokens[1]);
			endTime = time.parse(tokens[2]);

			if (startTime.before(endTime)) {
				ArrayList<Project> queryList = (ArrayList<Project>) myServer.queryProject(transfer(startTime), transfer(endTime));

				if (queryList == null || queryList.size() == 0) {// 未查询到
					System.err.println("No Project In This Time !");
				} else {// 查询到项目存在，逐条打印
					for (Project temp : queryList) {// 无法调用toString函数，手动输出
						System.out.println("Project [creator=" + temp.getCreator() + ", startTime="
								+ temp.getStartTime().toString() + ", endTime=" + temp.getEndTime().toString()
								+ ", title=" + temp.getTitle() + ", ID=" + temp.getProjectID() + "]");
					}
				}
			} else {
				System.err.println("Start Time Should Before Than End Time !");
			}
		}
	}

	/**
	 * 打印菜单 
	 * 1.register 
	 * 		arguments: <userName> <password> 
	 * 2.add 
	 * 		arguments: <userName> <startTime> <endTime> <title> Time Format : 2018-12-18-16:00:00 
	 * 3.delete 
	 * 		arguments: <userName> <projectID> 
	 * 4.clear 
	 * 		arguments: <userName> 
	 * 5.query
	 * 		arguments: <startTime> <endTime> 
	 * 6.help 
	 * 		no args 
	 * 7.quit 
	 * 		no args
	 */
	public static void printMenu() {
		System.out.println("Menu:");
		System.out.println("    1.register");
		System.out.println("        arguments: <userName> <password>");
		System.out.println("    2.add");
		System.out.println("        arguments: <userName> <startTime> <endTime> <title> Time Format : 2018-12-18-16:00:00");
		System.out.println("    3.delete");
		System.out.println("        arguments: <userName> <projectID>");
		System.out.println("    4.clear");
		System.out.println("        arguments: <userName>");
		System.out.println("    5.query");
		System.out.println("        arguments: <startTime> <endTime> Time Format : 2018-12-18-16:00");
		System.out.println("    6.help");
		System.out.println("        arguments: no args");
		System.out.println("    7.quit");
		System.out.println("        arguments: no args");
	}

	/**
	 * web service中使用XMLGregorianCalendar 将Date类型转化为XMLGregorianCalendar
	 * 
	 * @param date Date
	 * @return XMLGregorianCalendar
	 */
	public static XMLGregorianCalendar transfer(Date date) throws Exception {
		XMLGregorianCalendar xmlGregorianCalendar = null;
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
		xmlGregorianCalendar = datatypeFactory.newXMLGregorianCalendar(calendar);
		return xmlGregorianCalendar;
	}

}
