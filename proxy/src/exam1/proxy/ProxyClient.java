package exam1.proxy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.Socket;

/**
 * 通过HttpClient修改，完成代理服务器作为客户端的GET操作
 * @author Patchouli
 * @version 1.0
 */
public class ProxyClient {
	/**
     * Allow a maximum buffer size of 8192 bytes
     */
    private static int buffer_size = 8192;

    /**
     * String to represent the Carriage Return and Line Feed character sequence.
     */
    private static String CRLF = "\r\n";

    /**
     * Response is stored in a byte array.
     */
    private byte[] buffer;
    private StringBuffer header = null;
    private StringBuffer response = null;

    /**
     * My socket to the world.
     */
    private Socket proxySocket = null;

    // 代理服务器作为客户端的输入输出流
    BufferedOutputStream ostream = null;
    BufferedInputStream istream = null;

    /**
     * ProxyClient constructor;
     */
    public ProxyClient() {
        buffer = new byte[buffer_size];
        header = new StringBuffer();
        response = new StringBuffer();
    }

    /**
     * <em>connect</em> connects to the input host on the port This function opens the socket and creates the input and output
     * streams used for communication.
     *
     * @param host 连接主机
     * @param port 连接端口
     */
    public void connect(String host, int port) throws Exception {

        /**
         * Open my socket to the specified host at the default port.
         */
        proxySocket = new Socket(host, port);

        /**
         * Create the output stream.
         */
        ostream = new BufferedOutputStream(proxySocket.getOutputStream());

        /**
         * Create the input stream.
         */
        istream = new BufferedInputStream(proxySocket.getInputStream());
    }


    /**
     *
     * <em>processGetRequest</em> process the input GET request.
     * @param request 请求信息
     * @param host 主机地址
     * @throws Exception
     */
    public void processGetRequest(String request, String host) throws Exception {
        /**
         * Send the request to the server.
         */
        request += CRLF;
        request += "Host: " + host + CRLF;
        // 长连接阻塞
        request += "Connection: Close" + CRLF + CRLF;
     		
        buffer = request.getBytes();
        ostream.write(buffer, 0, request.length());
        ostream.flush();
        /**
         * waiting for the response.
         */
        processResponse();
    }

    /**
     * <em>processResponse</em> process the server response.
     *
     * @throws Exception
     */
    public void processResponse() throws Exception {
        int last = 0, c = 0;
        /**
         * Process the header and add it to the header StringBuffer.
         */
        boolean inHeader = true; // loop control
        while (inHeader && ((c = istream.read()) != -1)) {
            switch (c) {
                case '\r':
                    break;
                case '\n':
                    if (c == last) {
                        inHeader = false;
                        break;
                    }
                    last = c;
                    header.append("\n");
                    break;
                default:
                    last = c;
                    header.append((char) c);
            }
        }

        /**
         * Read the contents and add it to the response StringBuffer.
         */
        while (istream.read(buffer) != -1) {
            response.append(new String(buffer, "iso-8859-1"));
        }
    }

    /**
     * Get the response header.
     */
    public String getHeader() {
        return header.toString();
    }

    /**
     * Get the server's response.
     */
    public String getResponse() {
        return response.toString();
    }

    /**
     * Close all open connections -- sockets and streams.
     */
    public void close() throws Exception {
        proxySocket.close();
        istream.close();
        ostream.close();
    }
}
