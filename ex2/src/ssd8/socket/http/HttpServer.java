package ssd8.socket.http;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * 
 * @author Patchouli
 * @version 1.0
 */
public class HttpServer {

	ServerSocket serverSocket;
	ExecutorService executorService;
	static File rootPath;

	// TCP�˿�
	private static final int PORT = 80;
	// �����������̳߳�ͬʱ�����߳���Ŀ
	final static int POOLSIZE = 10;
	/**
	 * FileServer����������
	 * @throws IOException
	 */
	public HttpServer() throws IOException {
		// �������������׽���
		serverSocket = new ServerSocket(PORT, 2);
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * POOLSIZE);
		System.out.println("��������������ʼ����80�˿�!");
	}
	/**
	 * 
	 * @param args Ĭ�ϲ���
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// ����������ʱ��ͨ��Run Configurations����Ŀ¼����
		if (args.length != 1) {
			System.err.println("Please Input Your Root Path!");
			return;
		}

		try {
			rootPath = new File(args[0]);
			// �ж�root�Ƿ�Ϊ��Ч�ļ���
			if (rootPath.isDirectory() == false) {
				System.err.println(rootPath.getAbsolutePath() + " Is Not A Directory, Link Failure!");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		System.out.println("Host Root Path Is: " + rootPath.getAbsolutePath()); // ����������
		new HttpServer().service(args[0]);
	}
	/**
	 * �������ø�Ŀ¼
	 * @param root ��·��/������·��
	 */
	//����path����root
	public void service(String root) {
		Socket socket = null;
		while (true) {
			try {
				 // �ȴ��û�����
				socket = serverSocket.accept();
				 // ��ִ�н����̳߳���ά��
				Handler handler = new Handler(socket,root);
				 // �������߳�
				executorService.execute(handler);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
