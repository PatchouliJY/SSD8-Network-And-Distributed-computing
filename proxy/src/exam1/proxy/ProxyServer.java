package exam1.proxy;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 代理服务器
 * @author Patchouli
 * @version 1.0
 */
public class ProxyServer {
	
	ServerSocket serverSocket;
	/**
	 * 线程池，处理多线程操作
	 */
	ExecutorService executorService;
	/**
	 * 根目录
	 */
	static File rootPath;

	/**
	 * 监听的TCP端口
	 */
	private static final int PORT = 8000;
	/**
	 * 单个处理器线程池同时工作线程数目
	 */
	final static int POOLSIZE = 10;
	
	/**
	 * ProxyServer启动服务器
	 * @throws IOException
	 */
	public ProxyServer() throws IOException {
		// 创建服务器端套接字
		serverSocket = new ServerSocket(PORT, 2);
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * POOLSIZE);
		System.out.println("服务器代理启动，开始监听8000端口!");
	}
	
	/**
	 * 启动代理服务器
	 * @param args 默认参数
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		/**
		 * 服务器启动时需通过Run Configurations传递目录参数
		 */
		if (args.length != 1) {
			System.err.println("Please Input Your Root Path!");
			return;
		}

		try {
			rootPath = new File(args[0]);
			// 判断root是否为有效文件夹
			if (rootPath.isDirectory() == false) {
				System.err.println(rootPath.getAbsolutePath() + " Is Not A Directory, Link Failure!");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		System.out.println("ProxyServer Root Path Is: " + rootPath.getAbsolutePath()); // 启动服务器
		new ProxyServer().service(args[0]);
	}
	
	/**
	 * 连接设置根目录
	 * @param root 根路径/服务器路径
	 */
	//根据path连接root
	public void service(String root) {
		Socket socket = null;
		while (true) {
			try {
				 /**
				  * 等待用户连接
				  */
				socket = serverSocket.accept();
				 // 把执行交给线程池来维护
				ProxyHandler handler = new ProxyHandler(socket,root);
				 // 开启多线程
				executorService.execute(handler);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
