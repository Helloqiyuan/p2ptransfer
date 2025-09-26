package com.qiyuan;


import com.qiyuan.pojo.Doc;
import com.qiyuan.utils.HashUtils;
import lombok.NoArgsConstructor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@Deprecated
@NoArgsConstructor
public class TCP {
    private int PORT = 8888;
    private Integer SIZE = 64 * 1024;// 64KB
    private String PATH;

    private String TARGETIP;

    public TCP(String ip,String path){
        TARGETIP = ip;
        PATH = path;
    }


    public void receive() throws Exception {
        ServerSocket socket = new ServerSocket(PORT);
        System.out.println("服务器监听...");
//        监听连接
        Socket accept = socket.accept();
        System.out.println("接收文件参数...");
        ObjectInputStream ois = new ObjectInputStream(accept.getInputStream());
        Doc doc = (Doc) ois.readObject();
        File file = new File("./R" + doc.getName());
        if (file.exists()) {
            file.delete();
        } else {
            file.createNewFile();
        }
        System.out.println("文件参数已接收...\n" + doc);


        System.out.println("准备接收文件内容...");
        FileOutputStream fos = new FileOutputStream(file);
        InputStream in = accept.getInputStream();
        byte[] buffer = new byte[SIZE];
        int len;
        Long received = 0L;
        while ((len = in.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
            received += len;
            System.out.print("\r接收进度" + (int) (received * 1.0 / doc.getSize() * 100 * 10000) / 10000.0 + "%");
            System.out.flush();
        }
        System.out.println("\n文件传输完毕,校验中...");
        String md5 = HashUtils.getFileChecksum(file);
        if (md5.equals(doc.getMd5())) {
            System.out.println("MD5校验成功");
            System.out.println("文件传输结束");
        } else {
            System.out.println("\nMD5校验失败");
            System.out.println("文件损坏请重传");
        }
        fos.flush();
        fos.close();
        accept.close();
    }

    public void send() throws Exception {
        File file = new File(PATH);
        if (file == null || !file.exists()) {
            throw new IOException("File not found");
        }
        System.out.println("正在计算文件的哈希值,请稍后...");
        String md5 = HashUtils.getFileChecksum(file);
        System.out.println("文件哈希值计算完成...");
        System.out.println("启动发送...");
        Socket socket = new Socket(TARGETIP, PORT);
        Doc doc = new Doc(file.getName(), file.length(), md5);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(doc);
        oos.flush();
        System.out.println("已完成文件参数传输...\n" + doc);


        System.out.println("开始发送文件内容...");
        FileInputStream fis = new FileInputStream(file);
        OutputStream out = socket.getOutputStream();

        byte[] buffer = new byte[SIZE];
        int len;
        Long sent = 0L;
        while ((len = fis.read(buffer)) != -1) {
            out.write(buffer, 0, len);
            sent += len;
            System.out.print("\r发送进度" + (int) (sent * 1.0 / doc.getSize() * 100 * 10000) / 10000.0 + "%");
            System.out.flush();
        }
        System.out.println("\n发送完成");
        out.flush();
        fis.close();
        socket.close();
    }

}
