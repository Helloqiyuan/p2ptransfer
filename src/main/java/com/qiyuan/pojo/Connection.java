package com.qiyuan.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 连接信息
 */
@Data
public class Connection implements Serializable {
    // 角色信息 发送者还是接收者
    private Role role;
    // 角色的IP信息
    private String IP;
    // 角色的端口信息
    private Integer port;
    // 角色的房间号信息
    private Integer RoomNumber;
    public Connection(Role role){
        this.role = role;
    }
    public Connection(Role role, String IP, Integer port){
        this.role = role;
        this.IP = IP;
        this.port = port;
    }
}
