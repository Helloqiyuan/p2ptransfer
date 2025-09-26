package com.qiyuan.test;

import com.qiyuan.P2P;
import com.qiyuan.utils.Utils;

import java.util.List;
import java.util.Scanner;

public class Receive {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        int port = Utils.getAvailablePort();
        System.out.println("==================这是一个文件传输程序==================");
        System.out.println("PS:接收文件的位置在Receive文件夹");
        List<String> localIPv6 = Utils.getLocalIPv6();
        String i;
        do{
            if(localIPv6.isEmpty()){
                System.out.print("按下回车重新查找:");
            }else{
                break;
            }
            i = sc.nextLine();
            localIPv6 = Utils.getLocalIPv6();
        }while(!":".equals(i));
        System.out.println("请将下方的地址端口发送给对方:");
        System.out.println("[" + localIPv6.get(0) + "]:" + port);
        new P2P(port).Receive();
    }
}
