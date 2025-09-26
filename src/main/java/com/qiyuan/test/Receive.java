package com.qiyuan.test;

import com.qiyuan.TCPv2;
import com.qiyuan.utils.Utils;

import java.util.List;
import java.util.Scanner;

public class Receive {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("=========这是一个文件传输程序=========");
        System.out.println("接收文件的位置默认是你的cmd运行时的地址(比如:C:\\Users\\username>)");
        System.out.println("请把你的IPV6地址告诉对方,一般第一个就行,下面展示的是你的IPv6");
        List<String> localIPv6 = Utils.getLocalIPv6();
        if(localIPv6.isEmpty()){
            System.out.println("未找到该设备的IPv6地址");
            String i;
            do {
                System.out.print("按下回车重新查找(输入s强制进行):");
                i = sc.nextLine();
            }while (!"quit".equals(i));
        }
        localIPv6.forEach(System.out::println);
        new TCPv2().Receive();
    }
}
