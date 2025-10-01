package com.qiyuan.cs;

import com.qiyuan.P2P;
import com.qiyuan.pojo.Connection;
import com.qiyuan.pojo.Role;
import com.qiyuan.utils.Utils;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
    private static final String ServerIP = "112.124.44.107";
    private static final Scanner sc = new Scanner(System.in);
    private static final String LocalIPv6;

    static {
        try {
            LocalIPv6 = Utils.handleIPv6();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws  Exception{
        String operation = operation();
        if("1".equals(operation)){
            // 发送
            SendOp();
        }else{
            // 接收
            ReceiveOp();
        }
    }
    private static String operation(){
        System.out.println("[1].发送文件");
        System.out.println("[2].接收文件");
        System.out.println("[3].退出");
        System.out.print("请选择操作:");
        List<String> oper = new ArrayList<>();
        oper.add("1");
        oper.add("2");
        do{
            String input = sc.nextLine();
            if(oper.contains(input)){
                return input;
            }else if("3".equals(input)) {
                System.exit(0);
            }else{
                System.out.print("输入错误,请重新输入:");
            }
        }while ( true);
    }
    private static String ChooseFile(){
        String filepath;
        System.out.print("请选择要发送的文件(拖入文件或仅按下回车以选择文件)\n:");
        String filePath = sc.nextLine();
        if (filePath.isEmpty()) {
            filepath = Utils.chooseFile();
            if (filepath == null) {
                System.exit(0);
            }

        } else {
            if (filePath.startsWith("\"")) {
                filepath = filePath.substring(1, filePath.length() - 1);
            } else {
                filepath = filePath;
            }
        }
        return filepath;
    }
    private static void SendOp() throws Exception{
        Socket socket = new Socket(ServerIP, 1024);
        // 发送连接信息 只包含身份信息
        // 输入 房间号
        System.out.print("请输入房间号:");
        int roomNumber = sc.nextInt();
        sc.nextLine();
        Connection connection1 = new Connection(Role.SENDER);
        connection1.setRoomNumber(roomNumber);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(connection1);
        oos.flush();
        // 选择文件
        String filepath = ChooseFile();

        System.out.println("等待接收者连接...");
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        // 读取服务器传来的接收者的连接信息 包括身份信息 本地IPv6和端口 房间号信息
        Connection connection = (Connection) ois.readObject();
//        System.out.println(connection);
        if(connection == null){
            System.out.println("房间号不可用!!!");
            return;
        }
        new P2P(filepath, connection.getIP(), connection.getPort()).Send();
        oos.close();
    }
    private static void ReceiveOp() throws Exception{
        Socket socket = new Socket(ServerIP, 1024);
        // 发送连接信息 包括身份信息 本地IPv6和端口
        int port = Utils.getAvailablePort();
        Connection connection = new Connection(Role.RECEIVER, LocalIPv6,port);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(connection);
        oos.flush();

        //接收房间号
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Connection connection1 = (Connection) ois.readObject();
        System.out.println("你的房间号是:" + connection1.getRoomNumber());

        // 启动接收服务在port监听
        new P2P(port).Receive();
        oos.close();
    }
}
