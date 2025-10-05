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
//    private static final String ServerIP = "localhost";
    private static final Scanner sc = new Scanner(System.in);
    private static final String LocalIPv6;

    static {
        try {
            LocalIPv6 = Utils.handleIPv6();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        String operation = operation();
        if ("1".equals(operation)) {
            // 发送
            SendOp();
        } else {
            // 接收
            ReceiveOp();
        }
    }

    private static String operation() {
        System.out.println("[1].发送文件");
        System.out.println("[2].接收文件");
        System.out.println("[3].退出");
        System.out.print("请选择操作(输入一个数字):");
        do {
            String input = sc.nextLine();
            if ("1".equals( input) || "2".equals(input)) {
                return input;
            } else if ("3".equals(input)) {
                System.exit(0);
            } else {
                System.out.print("输入错误,请重新输入:");
            }
        } while (true);
    }

    private static String ChooseFile() {
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

    private static void SendOp() throws Exception {
        Socket socket;
        // 发送连接信息 只包含身份信息
        Connection MessageToServer;
        // 接收服务器返回的数据
        Connection MessageFromServer;
        ObjectOutputStream oos;
        ObjectInputStream ois;
        while (true) {
            socket = new Socket(ServerIP, 1024);
            // 输入 房间号
            System.out.print("请输入房间号:");
            int roomNumber = Integer.parseInt(sc.nextLine());
            MessageToServer = Connection.builder()
                    .role(Role.SENDER)
                    .RoomNumber(roomNumber)
                    .build();

            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(MessageToServer);
            oos.flush();

            // 读取服务器传来的接收者的连接信息 包括身份信息 本地IPv6和端口 房间号信息
            ois = new ObjectInputStream(socket.getInputStream());
            MessageFromServer = (Connection) ois.readObject();
            oos.close();
            ois.close();
            socket.close();
            if (MessageFromServer == null) {
                System.out.println("房间号不存在,请检查房间号是否正确,然后重新输入!");
                continue;
            }
            break;
        }
        // 选择文件
        String filepath = ChooseFile();
        // 发送文件
        new P2P(filepath, MessageFromServer.getIP(), MessageFromServer.getPort()).Send();
    }

    private static void ReceiveOp() throws Exception {
        Socket socket = new Socket(ServerIP, 1024);
        // 发送连接信息 包括身份信息 本地IPv6和端口
        int port = Utils.getAvailablePort();
        Connection connection = Connection.builder()
                .role(Role.RECEIVER)
                .IP(LocalIPv6)
                .port(port)
                .build();

        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(connection);
        oos.flush();

        //接收房间号
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Connection connection1 = (Connection) ois.readObject();
        System.out.println("你的房间号是:" + connection1.getRoomNumber());

        oos.close();
        socket.close();
        // 启动接收服务在port监听
        new P2P(port).Receive();
    }
}
