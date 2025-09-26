package com.qiyuan.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BreakpointContinue implements Serializable {
    /**
     * 断点字节
     */
    private Long BreakPointByte = 0L;
}
