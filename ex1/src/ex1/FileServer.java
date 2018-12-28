package ex1;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * 
 * @author Patchouli Ji Yi
 * @version 1.0
 */
public class FileServer {

	ServerSocket serverSocket;
	ExecutorService executorService;
	static File rootPath;

	// TCP�˿�
	private static final int TCP_PORT = 2021;
	// �����������̳߳�ͬʱ�����߳���Ŀ
	final static int POOLSIZE = 10;
	/**
	 * FileServer����������
	 * @throws IOException
	 */
	public FileServer() throws IOException {
		// �������������׽���
		serverSocket = new ServerSocket(TCP_PORT, 2);
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * POOLSIZE);
		System.out.println("����������!");
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
		System.out.println("Root Path Is: " + rootPath.getAbsolutePath()); // ����������
		new FileServer().service(args[0]);
	}
	/**
	 * �������ø�Ŀ¼
	 * @param root ��·��
	 */
	//����path����root
	public void service(String root) {
		Socket socket = null;
		while (true) {
			try {
				 // �ȴ��û�����
				socket = serverSocket.accept();
				 // ��ִ�н����̳߳���ά��
				Handler handler = new Handler(socket);
				System.out.println(root);
				handler.setRootPath(root);
				 // �������߳�
				executorService.execute(handler);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
