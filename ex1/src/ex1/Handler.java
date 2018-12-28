package ex1;

import java.io.*;
import java.net.*;

/**
 * implements {@link Runnable}
 */
public class Handler implements Runnable { // �����뵥���ͻ�ͨ�ŵ��߳�

	//���������
	BufferedReader br;
	BufferedWriter bw;
	PrintWriter pw;

	DatagramSocket dgsocket; // UDP���ڴ����ļ�
	SocketAddress socketAddres;
	
	private Socket socket;
	private String rootPath = null;
	private static String currentPath = null;
	private static final String HOST = "127.0.0.1"; // ���ӵ�ַ
	private static final int UDP_PORT = 2020; // UDP�˿�
	private static final int SENDSIZE = 1024; // һ�δ����ļ����ֽ���
	/**
	 * ���캯��
	 * @param socket �����׽���
	 */
	public Handler(Socket socket) {
		this.socket = socket;
	}
	/**
	 * ��ʼ������
	 * @throws IOException
	 */
	public void initStream() throws IOException { 
		// ��ʼ��������������󷽷�
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		pw = new PrintWriter(bw, true);
	}
	/**
	 * �ж��ļ��Ƿ����
	 * @param fileName �ļ���
	 * @return
	 */
	public static boolean isFileExist(String fileName) {
		boolean isExist = false;
		
		File rootFile = new File(currentPath);
		File[] fileList = rootFile.listFiles();
		//���ļ��б����Ƿ����
		for (File file : fileList) {
			if (file.getName().equals(fileName) && file.isFile()) {
				isExist = true;
			}
		}
		return isExist;
	}
	/**
	 * ���ø�Ŀ¼�뵱ǰĿ¼
	 * @param path ·��
	 */
	public void setRootPath(String path) {
		// TODO Auto-generated method stub
		this.rootPath = path + "\\";
		this.currentPath = this.rootPath;
	}
	/**
	 * ���к���
	 */
	public void run() { 
		// ִ�е�����
		try {
			// ��������Ϣ
			System.out.println(socket.getInetAddress() + ":" + socket.getPort() + ">���ӳɹ�"); 
			// ��ʼ���������������
			initStream(); 
			// ��ͻ��˷������ӳɹ���Ϣ
			pw.println(socket.getInetAddress() + ":" + socket.getPort() + ">���ӳɹ�");

			String info = null;

			while (null != (info = br.readLine())) {
				if (info.equals("bye")) {
					// �˳�
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
					// ���ڱ�ʶĿǰ��ָ��������԰�������Client�����ѭ��
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
	 * ls����г���ǰĿ¼�������ļ�
	 * @param currentPath ��ǰ·��
	 */
	private void ls(String currentPath) {
		File rootFile = new File(currentPath);
		File[] fileList = rootFile.listFiles();
		int MaxLength = 45;
		pw.println("Type" + addSpace(7) + "Name" + addSpace(MaxLength - 4) + "Size");
		for (File file : fileList) {
			if (file.isFile()) {
				// ���ļ�
				pw.println("<file>" + addSpace(5) + file.getName() + addSpace(MaxLength - file.getName().length())
						+ file.length() + " B");
			} else if (file.isDirectory()) {
				// ���ļ���
				pw.println("<dir>" + addSpace(6) + file.getName() + addSpace(MaxLength - file.getName().length())
						+ file.length() + "B");
			}
		}
	}
	/**
	 * cd���������һ���ļ���
	 * @param dir
	 */
	private void cd(String dir) {
		Boolean isExist = false;// ��ʼ�趨Ŀ¼������
		Boolean isDir = true;// ��ʼ�趨���ļ���
		File rootFile = new File(currentPath);
		File[] fileList = rootFile.listFiles();
		//�ж�dir�Ƿ�Ϊ��
		if (dir == null) {
			pw.println("Error");
		}
		//��fileList����
		for (File file : fileList) {
			if (file.getName().equals(dir)) {
				// �ҵ���ͬ�����ļ��л��ļ�
				isExist = true;
				if (file.isDirectory()) {
					// ���ֶ�Ӧ�ļ���
					isDir = true;
					break;
				} else {
					// ���ֶ�Ӧ�ļ�
					isDir = false;
					pw.println("You Can't cd A File!");
				}
			}
		}
		if (isExist && isDir) {
			// ���ļ��в��Ҵ���
			currentPath = currentPath + "\\" + dir;
			pw.println(dir + " > OK");
		} else if (isDir && (!isExist)) {
			// ���ļ��е����ڵ�ǰĿ¼
			pw.println(" Unknown Dir :" + dir);
		}

	}
	/**
	 * ������һ��Ŀ¼
	 */
	private void backDir() {
		if (currentPath.equals(rootPath)) {
			pw.println("The current path is the root path, cd.. cannot realize.");
		} else {
			for (int i = currentPath.length(); i > 0; i--) {
				if (currentPath.substring(i - 1, i).equals("\\")) {
					//ͨ�����ַ����������һ����\\������·��ȥ��
					currentPath = currentPath.substring(0, i - 1);
					pw.println((new File(currentPath)).getName() + " > OK");
					break;
				}
			}
		}
	}
	/**
	 * �����ļ�
	 * @param fileName �ļ���
	 * @throws SocketException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void sendFile(String fileName) throws SocketException, IOException, InterruptedException {
		
		// �ļ�������
		if (!isFileExist(fileName)) {
			pw.println(-1);
			return;
		}

		File file = new File(currentPath + "\\" + fileName);
		pw.println(file.length());
		// UDP����
		dgsocket = new DatagramSocket(); 
		socketAddres = new InetSocketAddress(HOST, UDP_PORT);
		DatagramPacket dp;
		
		//ÿ��UDP���ݱ�
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
	 * ��ӿո񣬽����Ű�
	 * @param count �ո����
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
