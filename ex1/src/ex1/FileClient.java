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
	private static final int TCP_PORT = 2021; // TCP���Ӷ˿�
	private static final String HOST = "127.0.0.1"; // ���ӵ�ַ
	private static final int UDP_PORT = 2020; // UDP�˿�
	private static final int SENDSIZE = 1024; // һ�δ����ļ����ֽ���
	Socket socket = new Socket();
	DatagramSocket dgsocket;
	/**
	 * ���캯��
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public FileClient() throws UnknownHostException, IOException {
		socket = new Socket(HOST, TCP_PORT); // �����ͻ����׽���

	}
	/**
	 * ������
	 * @param args Ĭ�ϲ���
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		new FileClient().send();
	}
	/**
	 * �����ļ�
	 */
	// �ļ�����
	public void send() {
		try {

			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));// �ͻ���������������������Ϣ
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));// �ͻ��������������շ�������Ϣ
			PrintWriter pw = new PrintWriter(bw, true);// װ�����������ʱˢ��

			// ����������������ӳɹ�����Ϣ
			System.out.println(br.readLine());

			// �����û���Ϣ
			Scanner in = new Scanner(System.in);
			String cmd = null;
			while ((cmd = in.next()) != null) {
				// ���͸���������
				pw.println(cmd); 
				// ����пո�
				if (cmd.equals("cd") || cmd.equals("get")) {
					// ��ȡ�ļ������ߵ�ַ
					String dir = in.next();
					pw.println(dir);
					if (cmd.equals("get")) {
						// �����ļ�
						long fileLength = Long.parseLong(br.readLine());
						if (fileLength != -1) {
							System.out.println("�ļ���СΪ��" + fileLength + " B");
							System.out.println("��ʼ�����ļ���" + dir);
							getFile(dir, fileLength);
							System.out.println("�ļ��������");
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
					// ������������ص���Ϣ
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
					// �Ͽ�����
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * ����ļ�
	 * @param fileName �ļ���
	 * @param fileLength �ļ�����
	 * @throws IOException
	 */
	private void getFile(String fileName, long fileLength) throws IOException {
		DatagramPacket dp = new DatagramPacket(new byte[SENDSIZE], SENDSIZE);
		dgsocket = new DatagramSocket(UDP_PORT);
		byte[] recInfo = new byte[SENDSIZE];
		// д�ļ��ĵ�ַ
		FileOutputStream fos = new FileOutputStream(new File(("E:\\Test\\Copy") + fileName));

		// UDP���ĸ���
		int count = (int) (fileLength / SENDSIZE) + ((fileLength % SENDSIZE) == 0 ? 0 : 1);

		while ((count--) > 0) {
			// �����ļ���Ϣ
			dgsocket.receive(dp);
			recInfo = dp.getData();
			fos.write(recInfo, 0, dp.getLength());
			fos.flush();
		}

		dgsocket.close();
		fos.close();
	}
}
