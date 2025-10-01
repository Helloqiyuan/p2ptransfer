package com.qiyuan.cs;

import com.qiyuan.pojo.Connection;
import com.qiyuan.pojo.Role;
import com.qiyuan.utils.Utils;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    // 一个房间号对应一个接收者的Connection
    private static Map<Integer, Connection> map = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(1024);
        System.out.println("服务器已启动...");
        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(() -> {
                try {
                    // 等待客户端连接
//                    Socket socket = serverSocket.accept();
//                    System.out.println("一个客户端已连接...");
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    Connection connection = (Connection) ois.readObject();
                    if (connection.getRole() == Role.RECEIVER) {
                        System.out.println("一个接收者连接...");
                        // 如果是接收者 把它的Connection信息存储到哈希表中
                        Integer roomNumber;
                        // 获取到一个可用的房间号
                        do {
                            roomNumber = Utils.generateRoomNumber();
                        } while (map.containsKey(roomNumber));
                        map.put(roomNumber, connection);
                        connection.setRoomNumber(roomNumber);
//                        System.out.println("接收者已连接,房间号为:" + roomNumber);
                        // 返回给接收者的主要是 房间 号
                        oos.writeObject(connection);
                    } else {
                        System.out.println("一个发送者连接...");
                        // 如果是发送者 找到它对应的接收者并把接收者的Connection信息发送给它
                        oos.writeObject(map.get(connection.getRoomNumber()));
                    }
                    oos.flush();
                    oos.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }).start();
        }
    }
}
