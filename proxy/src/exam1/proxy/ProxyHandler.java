package exam1.proxy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;

/**
 * �����������Ӧ�Ĳ�������
 * @author Patchouli
 * @version 1.0
 */
public class ProxyHandler implements Runnable {

private Socket socket;
	
	/**
	 * ���������
	 */
	BufferedReader br;
	BufferedWriter bw;
	PrintWriter pw;
	BufferedInputStream istream;
	BufferedOutputStream ostream;
	
	//����Header
	private byte[] buffer;
	public String rootpath = null;
	private static int buffer_size = 8192;
	
	/**
	 * ���������������Ϊ�ͻ���ʹ��ʱ��ʵ������
	 */
	ProxyClient proxyClient = new ProxyClient();

	/**
	 * ��ʼ�����������
	 * @throws IOException
	 */
	public void initStream() throws IOException { 
		// ��ʼ��������������󷽷�
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		istream = new BufferedInputStream(socket.getInputStream());
		ostream = new BufferedOutputStream(socket.getOutputStream());
		pw = new PrintWriter(bw, true);
	}
	
	/**
	 * ����proxyHandlerʵ������
	 * @param socket �׽���
	 * @param root ��������Ŀ¼
	 */
	public ProxyHandler(Socket socket, String root) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.rootpath = root;
		/**
		 * ����������
		 */
		this.buffer = new byte[buffer_size];
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
            initStream();
            String request = null;

            /**
             * ��ȡ������Ϣ
             */
            while ((request = br.readLine()) != null) {
                if (request.startsWith("GET")) {
                	//ȡ��GET����
                	processGet(request);
                } else if (request.startsWith("From") || request.startsWith("User-Agent")) {
                	continue;
                }else {
                    badRequest();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}

	/**
     * ����������Ϣʵ��GET����
     * @param request ������Ϣ
     * @throws Exception
     */
    private void processGet(String request) throws Exception {
    	        
    	//�ָ�����
        String[] info = request.split(" ");
        if (!(info.length == 3)) {
            badRequest();
        } else {
        	//��ȡURL
            URL url = new URL(info[1]);
            
        	//��ȡ������
        	String host = url.getHost();
        	
            //��ȡ�˿ںţ�������Ĭ��Ϊ80
            int port;
        	if(url.getPort() == -1) {
        		port = 80;
        	}else {
        		port = url.getPort();
        	}
        	
        	/**
        	 * ʹ�ô�����������
        	 */
            proxyClient.connect(host,port);
            
            //��ԭ����
            String backRequest = "GET " + url.getFile() + " HTTP/1.1";
            proxyClient.processGetRequest(backRequest, host);
            String header = proxyClient.getHeader() + "\n";
            String body = proxyClient.getResponse();
            
            /**
             * ��ͻ��˷��ͱ���
             */
            buffer = header.getBytes();
            ostream.write(buffer, 0, header.length());
            ostream.write(body.getBytes());

            ostream.flush();
        }
    }

	/**
	 * ������Ч����
	 * @throws IOException
	 */
	public void badRequest() throws IOException {
		String response = "Bad Request!";
		bw.write(response);
		bw.flush();
	}
}
