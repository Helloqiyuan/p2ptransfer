package com.qiyuan;

import com.qiyuan.pojo.BreakpointContinue;
import com.qiyuan.pojo.Doc;
import com.qiyuan.utils.HashUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class TCPv2 {
    /**
     * 接收端监听的端口号
     */
    private int port = 8888;

    /**、
     * 缓冲区大小
     * 每次读取或写入的字节数
     */
    private int cache = 4 * 1024; //64KB

    /**
     * 发送端发送的文件的路径
     */
    private String path = "./send/xx.txt";

    /**
     * 接收端的IPv6地址
     */
    private String TargetIPv6 = "127.0.0.1";

    /**
     * 接收端服务
     */
    public void Receive(){
        try {
            // 1.创建接收端套接字
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("接收端已启动，等待发送端连接...");
            Socket socket = serverSocket.accept();
            System.out.println("检测到发送方连接");


            // 2.接收文件基本信息
//            System.out.println("等待发送方发送文件基本信息...");
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Doc doc = (Doc) ois.readObject();
            System.out.println("已接收文件基本信息" + doc);
            // 指向接收文件夹
            File fileDirectory = new File("./Receive");
            if(!fileDirectory.exists()){
                fileDirectory.mkdir();
            }
            // 用来断点续传
            BreakpointContinue breakpointContinue = new BreakpointContinue();
            File[] files = fileDirectory.listFiles();
            int i;
            //不包含后缀的文件名
            String docName = doc.getName().split( "\\.")[0];


            /**
             * 假设需要传输的是abc.txt 10MB
             * 本地存在文件abc.ucf 5MB 文件则需要续传
             * 若不存在，则创建abc.txt
             */
            for(i = 0;i < files.length;i++){
                //本地文件
                String name = files[i].getName().split("\\.")[0]; //文件名不包含后缀:abc
                String format = files[i].getName().split("\\.")[1]; //文件后缀:txt
                //Doc信息
                if(format.equals("ucf") && name.equals(docName)){
                    breakpointContinue.setBreakPointByte(files[i].length());
                    break;
                }
            }
            // 待接收的文件
            File file = new File("./Receive/" + docName + ".ucf");
            if(i >= files.length){
                file.createNewFile();
            }

            // 3.返回本地已存在文件的断点信息
//            System.out.println("已完成文件初始化" +(breakpointContinue.getBreakPointByte() == 0L ? "" :"并返回断点信息") + "...");
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(breakpointContinue);
            oos.flush();
//            System.out.print(breakpointContinue.getBreakPointByte() == 0L ? "" : "已返回断点信息\n");


            // 4.接收文件
            System.out.println(breakpointContinue.getBreakPointByte() == 0L ?  "准备接收文件..." : "准备续传文件...");
            FileOutputStream fos = new FileOutputStream(file,true);
            InputStream ips = socket.getInputStream();
            byte[] buffer = new byte[cache];
            int len;
            Long totalReceived = breakpointContinue.getBreakPointByte();
            while((len = ips.read(buffer)) != -1){
                fos.write(buffer,0,len);
                totalReceived += len;
                System.out.print("\r接收进度" + (int)(totalReceived * 1.0 / doc.getSize() * 100 * 10000) / 10000.0 + "%");
                System.out.flush();
            }
            System.out.println("\n本次共接收" + (totalReceived - breakpointContinue.getBreakPointByte()) + "字节,合计" + ((totalReceived - breakpointContinue.getBreakPointByte()) * 1.0 / 1024 / 1024) + "MB");
            System.out.println("接收完成");

            // 5.更正文件名
            fos.close(); //关闭文件输出流
            Path source = Paths.get("./Receive/" + file.getName());
            Path target = Paths.get("./Receive/" + doc.getName());

            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);


            // 6.校验文件
            // TODO



            // 7.接收完成关闭资源
            ips.close(); //关闭输入流
            oos.close(); //关闭对象输出流
            ois.close(); //关闭对象输入流
            socket.close(); //关闭Socket

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 发送端服务
     */
    public void Send(){
        try{
            // 1.Socket连接
            System.out.println("正在连接" + TargetIPv6);
            Socket socket = new Socket(TargetIPv6,port);
            System.out.println("连接成功");

            // 2.发送文件基本信息
            File file = new File(path);
            if(!file.exists()){
                throw new Exception("文件不存在");
            }
            System.out.println("正在生成文件(" + path + ")的哈希值,请稍后...");
            // 简化过程先不做哈希运算
//            Doc doc = new Doc(file.getName(),file.length(), HashUtils.getFileChecksum(file));
            Doc doc = new Doc(file.getName(),file.length(), "123456");
//            System.out.println("文件哈希值计算完成");
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(doc);
            oos.flush();
            System.out.println("发送文件基本信息" + doc);

            // 3.等待接收方确认
//            System.out.println("等待接收方确认...");
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            // 接收对方发来的断点信息
            // 阻塞方法
            BreakpointContinue response = (BreakpointContinue) ois.readObject();
//            System.out.println("接收方已确认文件基本信息" + (response.getBreakPointByte() == 0L ? "" : "并返回断点信息"));

            // 4.发送文件
            FileInputStream fis = new FileInputStream(file);
            // 跳过断点字节
            fis.skip(response.getBreakPointByte());
            System.out.println(response.getBreakPointByte() == 0L ? "开始发送文件" : "开始断点续传");
            OutputStream ops = socket.getOutputStream();

            byte[] buffer = new byte[cache];
            int len;
            Long totalSent = response.getBreakPointByte();
            while((len = fis.read(buffer)) != -1){
                ops.write(buffer,0,len);
                totalSent += len;
                System.out.print("\r发送进度" + (int)(totalSent * 1.0 / doc.getSize() * 100 * 10000) / 10000.0 + "%");
                System.out.flush();
            }
            System.out.println("\n本次共发送" + (totalSent - response.getBreakPointByte()) + "字节,合计" + ((totalSent - response.getBreakPointByte()) * 1.0 / 1024 / 1024) + "MB");
            System.out.println("发送完成");
            // 5.发送完成关闭资源
            fis.close(); // 关闭文件输入流
            ops.close(); // 关闭字节输出流
            oos.close(); // 关闭对象输出流
            ois.close(); // 关闭对象输入流
            socket.close(); // 关闭Socket
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
