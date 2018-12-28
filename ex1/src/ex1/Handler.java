package ex1;

import java.io.*;
import java.net.*;

/**
 * implements {@link Runnable}
 */
public class Handler implements Runnable { // 负责与单个客户通信的线程

	//输入输出流
	BufferedReader br;
	BufferedWriter bw;
	PrintWriter pw;

	DatagramSocket dgsocket; // UDP用于传送文件
	SocketAddress socketAddres;
	
	private Socket socket;
	private String rootPath = null;
	private static String currentPath = null;
	private static final String HOST = "127.0.0.1"; // 连接地址
	private static final int UDP_PORT = 2020; // UDP端口
	private static final int SENDSIZE = 1024; // 一次传送文件的字节数
	/**
	 * 构造函数
	 * @param socket 连接套接字
	 */
	public Handler(Socket socket) {
		this.socket = socket;
	}
	/**
	 * 初始化函数
	 * @throws IOException
	 */
	public void initStream() throws IOException { 
		// 初始化输入输出流对象方法
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		pw = new PrintWriter(bw, true);
	}
	/**
	 * 判断文件是否存在
	 * @param fileName 文件名
	 * @return
	 */
	public static boolean isFileExist(String fileName) {
		boolean isExist = false;
		
		File rootFile = new File(currentPath);
		File[] fileList = rootFile.listFiles();
		//从文件夹遍历是否存在
		for (File file : fileList) {
			if (file.getName().equals(fileName) && file.isFile()) {
				isExist = true;
			}
		}
		return isExist;
	}
	/**
	 * 设置根目录与当前目录
	 * @param path 路径
	 */
	public void setRootPath(String path) {
		// TODO Auto-generated method stub
		this.rootPath = path + "\\";
		this.currentPath = this.rootPath;
	}
	/**
	 * 运行函数
	 */
	public void run() { 
		// 执行的内容
		try {
			// 服务器信息
			System.out.println(socket.getInetAddress() + ":" + socket.getPort() + ">连接成功"); 
			// 初始化输入输出流对象
			initStream(); 
			// 向客户端发送连接成功信息
			pw.println(socket.getInetAddress() + ":" + socket.getPort() + ">连接成功");

			String info = null;

			while (null != (info = br.readLine())) {
				if (info.equals("bye")) {
					// 退出
					break;
				} else {
					switch (info) {
					case "ls":
						ls(currentPath);
						break;
					case "cd":
						String dir = null;
						dir = br.readLine();
						System.out.println(dir);
						cd(dir);
						break;
					case "cd..":
						backDir();
						break;
					case "get":
						String fileName = br.readLine();
						sendFile(fileName);
						break;
					default:
						pw.println("unknown cmd");
					}
					// 用于标识目前的指令结束，以帮助跳出Client的输出循环
					pw.println("CmdEnd");
				}
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (null != socket) {
				try {
					br.close();
					bw.close();
					pw.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * ls命令：列出当前目录下所有文件
	 * @param currentPath 当前路径
	 */
	private void ls(String currentPath) {
		File rootFile = new File(currentPath);
		File[] fileList = rootFile.listFiles();
		int MaxLength = 45;
		pw.println("Type" + addSpace(7) + "Name" + addSpace(MaxLength - 4) + "Size");
		for (File file : fileList) {
			if (file.isFile()) {
				// 是文件
				pw.println("<file>" + addSpace(5) + file.getName() + addSpace(MaxLength - file.getName().length())
						+ file.length() + " B");
			} else if (file.isDirectory()) {
				// 是文件夹
				pw.println("<dir>" + addSpace(6) + file.getName() + addSpace(MaxLength - file.getName().length())
						+ file.length() + "B");
			}
		}
	}
	/**
	 * cd命令：进入下一级文件夹
	 * @param dir
	 */
	private void cd(String dir) {
		Boolean isExist = false;// 初始设定目录不存在
		Boolean isDir = true;// 初始设定是文件夹
		File rootFile = new File(currentPath);
		File[] fileList = rootFile.listFiles();
		//判断dir是否为空
		if (dir == null) {
			pw.println("Error");
		}
		//从fileList遍历
		for (File file : fileList) {
			if (file.getName().equals(dir)) {
				// 找到了同名的文件夹或文件
				isExist = true;
				if (file.isDirectory()) {
					// 名字对应文件夹
					isDir = true;
					break;
				} else {
					// 名字对应文件
					isDir = false;
					pw.println("You Can't cd A File!");
				}
			}
		}
		if (isExist && isDir) {
			// 是文件夹并且存在
			currentPath = currentPath + "\\" + dir;
			pw.println(dir + " > OK");
		} else if (isDir && (!isExist)) {
			// 是文件夹但不在当前目录
			pw.println(" Unknown Dir :" + dir);
		}

	}
	/**
	 * 返回上一级目录
	 */
	private void backDir() {
		if (currentPath.equals(rootPath)) {
			pw.println("The current path is the root path, cd.. cannot realize.");
		} else {
			for (int i = currentPath.length(); i > 0; i--) {
				if (currentPath.substring(i - 1, i).equals("\\")) {
					//通过子字符串，将最后一个“\\”向后的路径去除
					currentPath = currentPath.substring(0, i - 1);
					pw.println((new File(currentPath)).getName() + " > OK");
					break;
				}
			}
		}
	}
	/**
	 * 发送文件
	 * @param fileName 文件名
	 * @throws SocketException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void sendFile(String fileName) throws SocketException, IOException, InterruptedException {
		
		// 文件不存在
		if (!isFileExist(fileName)) {
			pw.println(-1);
			return;
		}

		File file = new File(currentPath + "\\" + fileName);
		pw.println(file.length());
		// UDP连接
		dgsocket = new DatagramSocket(); 
		socketAddres = new InetSocketAddress(HOST, UDP_PORT);
		DatagramPacket dp;
		
		//每份UDP数据报
		byte[] sendInfo = new byte[SENDSIZE];
		int size = 0;
		dp = new DatagramPacket(sendInfo, sendInfo.length, socketAddres);
		BufferedInputStream bfdIS = new BufferedInputStream(new FileInputStream(file));
		
		while ((size = bfdIS.read(sendInfo)) > 0) {
			dp.setData(sendInfo);
			dgsocket.send(dp);
			sendInfo = new byte[SENDSIZE];
		}
		bfdIS.close();
		dgsocket.close();
	}
	/**
	 * 添加空格，进行排版
	 * @param count 空格个数
	 * @return
	 */
	public static String addSpace(int count) {
		String str = "";
		for (int i = 0; i < count; i++) {
			str += " ";
		}
		return str;
	}

}
