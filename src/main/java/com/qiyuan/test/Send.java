package com.qiyuan.test;

import com.qiyuan.TCPv2;
import com.qiyuan.utils.Utils;

import java.util.List;
import java.util.Scanner;


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
                System.out.print("没有可用的IPV6地址,按回车重新检测(输入s强制进行):");
                String i = sc.nextLine();
                if("quit".equals(i)){
                    break;
                }
            } else {
                break;
            }
        } while (localIPv6.isEmpty());
        System.out.print("请选择要发送的文件(按下回车选择文件):");
        sc.nextLine();
        filepath = Utils.chooseFile();
        if (filepath == null) {
            System.exit(0);
        }
        System.out.println(filepath);
        System.out.print("请输入对方的IPV6地址(比如:2409:8938:2476:686:f5c3:2340:37ba:75),要是有公网IPV4也行:");
        IPv6 = sc.nextLine();
//        while (!Utils.isValidIPv6(IPv6)){
//            System.out.println("输入有误请重新输入:");
//            sc.nextLine();
//        }

        System.out.print("请输入对方的端口号:");
        port = sc.nextInt();
        new TCPv2(filepath, IPv6,port).Send();
    }

}
