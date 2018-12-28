package ex1;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
/**
 * 
 * @author Patchouli
 * @version 1.0
 */
public class FileClient {
	private static final int TCP_PORT = 2021; // TCP连接端口
	private static final String HOST = "127.0.0.1"; // 连接地址
	private static final int UDP_PORT = 2020; // UDP端口
	private static final int SENDSIZE = 1024; // 一次传送文件的字节数
	Socket socket = new Socket();
	DatagramSocket dgsocket;
	/**
	 * 构造函数
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public FileClient() throws UnknownHostException, IOException {
		socket = new Socket(HOST, TCP_PORT); // 创建客户端套接字

	}
	/**
	 * 主函数
	 * @param args 默认参数
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		new FileClient().send();
	}
	/**
	 * 接收文件
	 */
	// 文件传输
	public void send() {
		try {

			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));// 客户端输出流，向服务器发消息
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 客户端输入流，接收服务器消息
			PrintWriter pw = new PrintWriter(bw, true);// 装饰输出流，及时刷新

			// 输出服务器返回连接成功的消息
			System.out.println(br.readLine());

			// 接受用户信息
			Scanner in = new Scanner(System.in);
			String cmd = null;
			while ((cmd = in.next()) != null) {
				// 发送给服务器端
				pw.println(cmd); 
				// 命令含有空格
				if (cmd.equals("cd") || cmd.equals("get")) {
					// 获取文件名或者地址
					String dir = in.next();
					pw.println(dir);
					if (cmd.equals("get")) {
						// 下载文件
						long fileLength = Long.parseLong(br.readLine());
						if (fileLength != -1) {
							System.out.println("文件大小为：" + fileLength + " B");
							System.out.println("开始接收文件：" + dir);
							getFile(dir, fileLength);
							System.out.println("文件接收完毕");
						} else {
							System.out.println("Unknown File!");
						}
					}
				}
				String msg = null;
				while (null != (msg = br.readLine())) {
					if (msg.equals("CmdEnd")) {
						break;
					}
					// 输出服务器返回的消息
					System.out.println(msg);
				}

				if (cmd.equals("bye")) {
					break;
				}
			}
			in.close();
			br.close();
			bw.close();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != socket) {
				try {
					// 断开连接
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 获得文件
	 * @param fileName 文件名
	 * @param fileLength 文件长度
	 * @throws IOException
	 */
	private void getFile(String fileName, long fileLength) throws IOException {
		DatagramPacket dp = new DatagramPacket(new byte[SENDSIZE], SENDSIZE);
		dgsocket = new DatagramSocket(UDP_PORT);
		byte[] recInfo = new byte[SENDSIZE];
		// 写文件的地址
		FileOutputStream fos = new FileOutputStream(new File(("E:\\Test\\Copy") + fileName));

		// UDP报文个数
		int count = (int) (fileLength / SENDSIZE) + ((fileLength % SENDSIZE) == 0 ? 0 : 1);

		while ((count--) > 0) {
			// 接收文件信息
			dgsocket.receive(dp);
			recInfo = dp.getData();
			fos.write(recInfo, 0, dp.getLength());
			fos.flush();
		}

		dgsocket.close();
		fos.close();
	}
}
