package com.qiyuan.test;

import com.qiyuan.P2P;
import com.qiyuan.utils.Utils;

import java.util.List;
import java.util.Scanner;
@Deprecated
public class Receive {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        int port = Utils.getAvailablePort();
        System.out.println("==================这是一个文件传输程序==================");
        System.out.println("PS:接收文件的位置在Receive文件夹");
        List<String> localIPv6s = Utils.getLocalIPv6();
        String input;
        do{
            if(localIPv6s.isEmpty()){
                System.out.print("没有可用的IPV6地址,按回车重新查找:");
            }else{
                break;
            }
            input = sc.nextLine();
            localIPv6s = Utils.getLocalIPv6();
        }while(!":".equals(input));
        if(!localIPv6s.isEmpty()){
            System.out.println("请将下方的地址端口发送给对方:");
            System.out.println("[" + localIPv6s.get(0) + "]:" + port);
        }
        new P2P(port).Receive();
    }
}
