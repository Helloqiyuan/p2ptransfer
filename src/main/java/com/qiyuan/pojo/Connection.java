package com.qiyuan.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 连接信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Connection implements Serializable {
    // 角色信息 发送者还是接收者
    private Role role;
    // 角色的IP信息
    private String IP;
    // 角色的端口信息
    private Integer port;
    // 角色的房间号信息
    private Integer RoomNumber;
}
