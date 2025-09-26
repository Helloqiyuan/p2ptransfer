package com.qiyuan.test;

import com.qiyuan.TCP;
import com.qiyuan.utils.NetUtils;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Deprecated
public class Main {
    public static void main(String[] args) throws Exception{
        Scanner sc = new Scanner(System.in);
        System.out.println("1.接收文件");
        System.out.println("2.发送文件");
        System.out.println("3.退出");
        System.out.print("请输入一个数字：");
        List<String> localIPv6 = NetUtils
                .getLocalIPv6()
                .stream()
                .filter(ip -> !ip.startsWith("fe80"))
                .collect(Collectors.toList());
        do {
            int i = sc.nextInt();
            switch (i) {
                case 1:
                    if(localIPv6.isEmpty()){
                        System.out.println("没有可用的IPV6地址!!!");
                        System.out.println("可能是程序没有扫描出,请手动到设置中找到本地IPV6;也有可能是你没有IPV6地址");
//                        System.exit(0);
                    }
                    System.out.println("请从下面的IPV6地址中挑选一个给要给你发文件的朋友:");
                    localIPv6.forEach(System.out::println);
                    new TCP().receive();
                    break;
                case 2:
                    sc.nextLine();
                    System.out.print("请输入对方的IPV6地址:");
                    String ip = sc.nextLine();
                    System.out.print("输入需要发送的文件的路径(比如:C:\\Windows\\explorer.exe):");
                    String path = sc.nextLine();
                    new TCP(ip,path).send();
                    break;
                default:
                    System.out.println("输入错误");
            }
        }while(true);
    }
}
