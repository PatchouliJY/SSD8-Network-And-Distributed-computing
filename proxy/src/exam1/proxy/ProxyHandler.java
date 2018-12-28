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
 * 代理服务器相应的操作方法
 * @author Patchouli
 * @version 1.0
 */
public class ProxyHandler implements Runnable {

private Socket socket;
	
	/**
	 * 输入输出流
	 */
	BufferedReader br;
	BufferedWriter bw;
	PrintWriter pw;
	BufferedInputStream istream;
	BufferedOutputStream ostream;
	
	//保存Header
	private byte[] buffer;
	public String rootpath = null;
	private static int buffer_size = 8192;
	
	/**
	 * 创建代理服务器作为客户端使用时的实例对象
	 */
	ProxyClient proxyClient = new ProxyClient();

	/**
	 * 初始化输入输出流
	 * @throws IOException
	 */
	public void initStream() throws IOException { 
		// 初始化输入输出流对象方法
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		istream = new BufferedInputStream(socket.getInputStream());
		ostream = new BufferedOutputStream(socket.getOutputStream());
		pw = new PrintWriter(bw, true);
	}
	
	/**
	 * 创建proxyHandler实例对象
	 * @param socket 套接字
	 * @param root 服务器根目录
	 */
	public ProxyHandler(Socket socket, String root) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.rootpath = root;
		/**
		 * 创建缓冲区
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
             * 获取请求信息
             */
            while ((request = br.readLine()) != null) {
                if (request.startsWith("GET")) {
                	//取得GET请求
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
     * 根据请求信息实现GET方法
     * @param request 请求信息
     * @throws Exception
     */
    private void processGet(String request) throws Exception {
    	        
    	//分割请求
        String[] info = request.split(" ");
        if (!(info.length == 3)) {
            badRequest();
        } else {
        	//提取URL
            URL url = new URL(info[1]);
            
        	//获取主机号
        	String host = url.getHost();
        	
            //获取端口号，不存在默认为80
            int port;
        	if(url.getPort() == -1) {
        		port = 80;
        	}else {
        		port = url.getPort();
        	}
        	
        	/**
        	 * 使用代理连接请求
        	 */
            proxyClient.connect(host,port);
            
            //复原报文
            String backRequest = "GET " + url.getFile() + " HTTP/1.1";
            proxyClient.processGetRequest(backRequest, host);
            String header = proxyClient.getHeader() + "\n";
            String body = proxyClient.getResponse();
            
            /**
             * 向客户端发送报文
             */
            buffer = header.getBytes();
            ostream.write(buffer, 0, header.length());
            ostream.write(body.getBytes());

            ostream.flush();
        }
    }

	/**
	 * 处理无效请求
	 * @throws IOException
	 */
	public void badRequest() throws IOException {
		String response = "Bad Request!";
		bw.write(response);
		bw.flush();
	}
}
