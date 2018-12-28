配置需求
1.ProxyServer.java需配置代理服务器地址，可使用本地文件夹
2.Client.java可配置Host为127.0.0.1
运行
1.eclipse打开proxy，运行配置完成的ProxyServer.java
2.运行配置完成的Client.java
3.在显示
    127.0.0.1 is listening to your request:
  后，输入命令
    GET http://henry.somesite.com:80/index.html HTTP/1.0
4.返回报文后，输入保存的文件名
    Enter the name of the file to save: 
    e.g. SAVE.html
浏览器运行proxy（建议使用IE）
1.对IE进行网络配置
   Internet option选项->连接->局域网设置->代理服务器（地址：127.0.0.1 端口：8000）
2.输入http://henry.somesite.com:80/index.html
   完成通过代理访问