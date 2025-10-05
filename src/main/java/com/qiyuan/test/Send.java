package com.qiyuan.test;

import com.qiyuan.P2P;
import com.qiyuan.utils.Utils;

import java.util.List;
import java.util.Scanner;

@Deprecated
public class Send {
    private static String filepath;
    private static String IPv6;
    private static int port;

    public static void main(String[] args) throws Exception {
        System.out.println("=========这是一个文件传输程序=========");
        List<String> localIPv6;
        Scanner sc = new Scanner(System.in);
        do {
            localIPv6 = Utils.getLocalIPv6();
            if (localIPv6.isEmpty()) {
                System.out.print("没有可用的IPV6地址,按回车重新查找:");
                String i = sc.nextLine();
                if (":".equals(i)) {
                    break;
                }
            } else {
                break;
            }
        } while (localIPv6.isEmpty());

        // 选择文件
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
        System.out.println("你输入的文件路径是:" + filepath);

        // 输入对方IPV6:Port
        System.out.print("请输入对方的IPV6地址(比如:[2409:8938:2476:686:f5c3:2340:37ba:75]:8888)\n:");
        String[] in = sc.nextLine().split("]");
        IPv6 = in[0].substring(1).trim();
        port = Integer.parseInt(in[1].substring(1).trim());

        // 启动发送端服务
        new P2P(filepath, IPv6, port).Send();
    }

}
