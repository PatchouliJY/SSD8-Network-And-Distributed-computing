package ssd8.socket.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
/**
 * @author Patchouli
 * @version 1.0
 */
public class Handler implements Runnable {

	private Socket socket;
	
	//输入输出流
	BufferedReader br;
	BufferedWriter bw;
	PrintWriter pw;
	BufferedInputStream istream;
	BufferedOutputStream ostream;
	
	//保存Header
	private byte[] buffer;
	private String backHeader = null;
	public String rootpath = null;
	static private String CRLF = "\r\n";
	private static int buffer_size = 8192;
	
	/**
	 * 初始化函数
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
	
	public Handler(Socket socket , String rootpath) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.rootpath = rootpath;
		//创建缓冲区
		this.buffer = new byte[buffer_size];
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			//初始化Server输入输出流对象
			initStream();
			
			String func = null;
			func = br.readLine();
			
			if(func.startsWith("GET")) {
				processGET(func);
			}else if(func.startsWith("PUT")) {
				processPUT(func);
			}else {
				badRequest(func);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
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
	 * 处理GET操作
	 * @param request 请求报文
	 * @throws IOException 
	 */
	private void processGET(String request) throws IOException {
		// TODO Auto-generated method stub
		//确认操作
		System.out.println(request);
		if(request.split(" ").length != 3) {
			String header = "Bad Request !";
			bw.write(header);
			bw.flush();
			System.exit(0);
		}
		String filename = rootpath + request.split(" ")[1];
		File file = new File(filename);
		//文件不存在，返回404 Not Found
		if(!file.exists()) {
			System.out.println("No Exist File !");
			String header = "";
			header = "HTTP/1.0 404 Not Found" + CRLF;
			header += "Server:Patchouli HttpServer/1.0" + CRLF+ CRLF;
			
			ostream.write(header.getBytes(), 0, header.length());
			ostream.flush();
			
			String page= "<html>" + "<head>" + "<title>Error</title>" + "</head>"
					+ "<body><h1>404 Not Found</h1>" + "</body>" + "</html>";
			
			ostream.write(page.getBytes(), 0, page.length());
			ostream.flush();
			return;
		}else {
			//文件存在，返回文件
			FileInputStream fis = new FileInputStream(file);
			String content_type = null;
			if (filename.endsWith(".jpg")) {
				content_type = "image/jpg";
			} else if (filename.endsWith(".html")) {
				content_type = "text/html";
			} else {
				content_type = "other";
			}
			int content_length = fis.available(),buffer_length = 0;
			
			//构造Header
			backHeader = "HTTP/1.0 200 OK";
			backHeader += CRLF;
			backHeader += "Server:Patchouli HttpServer/1.0" + CRLF;
			backHeader += "Content-length:" + content_length + CRLF;
			backHeader += "Content-type:" + content_type + CRLF;
			backHeader += CRLF;
			
			buffer = backHeader.getBytes();
			//向客户端写入Header
			ostream.write(buffer, 0, backHeader.length());
			ostream.flush();
			//下面开始传输文件
			while ((buffer_length = fis.read(buffer)) != -1) {
				ostream.write(buffer, 0, buffer_length);
				//测试buffer_length
				System.out.println(buffer_length);
				ostream.flush();
			}
			System.out.println("Success Transform File !");
			fis.close();
		}
	}

	/**
	 * 处理PUT操作
	 * @param request 请求报文
	 * @throws IOException 
	 */
	private void processPUT(String request) throws IOException {
		// TODO Auto-generated method stub
		System.out.println(request);
		String fileName = request.split(" ")[1];
		String absolutePath = rootpath + "\\" + fileName;
		File file = new File(absolutePath);
		String header = "";
		if (file.exists()) {
			header += "HTTP/1.1 200 OK" + CRLF;
		} else {
			header += "HTTP/1.1 201 Created" + CRLF;
		}

		FileOutputStream fos = new FileOutputStream(absolutePath);
		System.out.println("Header:" + CRLF);
		char[] chars = new char[100];
		String headerLine = null;
		int lineLength = 0;
		int contentLength = 0;
		int i = 0, last = 0, c = 0;
		String contentType = "";
		boolean inHeader = true;
		while (inHeader && (c = istream.read()) != -1) {
			switch (c) {
			case '\r':
				// 解析字节流
				lineLength = i;
				headerLine = new String(chars, 0, lineLength);
				if (headerLine.split(": ")[0].equals("Content-length")) {
					contentLength = Integer.valueOf(headerLine.split(": ")[1]);
				}
				if (headerLine.split(": ")[0].equals("Content-type")) {
					contentType = headerLine.split(": ")[1];
				}
				i = 0;
				lineLength = 0;
				break;
			case '\n':
				if (c == last) {
					inHeader = false;
					break;
				}
				last = c;
				System.out.print((char) c);
				break;
			default:
				chars[i] = (char) c;
				i++;
				last = c;
				System.out.print((char) c);
			}
		}

		// 接收文件
		int number = 0;
		while (number < contentLength && (c = istream.read()) != -1) {
			fos.write(c);
			fos.flush();
			number++;
		}
		System.out.println("File PUT Successful !");
		fos.close();

		// 构造header
		header += "Server:Patchouli HttpServer/1.1" + CRLF;
		header += "Content-length: " + contentLength + CRLF;
		header += "Content-type: " + contentType + CRLF;

		ostream.write(header.getBytes(), 0, header.length());
		ostream.flush();
		socket.close();
	}

	/**
	 * 处理无效请求
	 * @param request 请求报文
	 * @throws IOException
	 */
	public void badRequest(String request) throws IOException {
		String response = "Bad Request!";
		bw.write(response);
		bw.flush();
	}

}
