package com.qiyuan.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Doc implements Serializable {
    private String name;
    private Long size;
    private String md5;

    @Override
    public String toString() {

        Long MB = size / 1024 / 1024;
        return "Doc{" +
                "文件名='" + name + "'" +
                ", 文件大小=" + size + "字节" +
                "(共" + MB + "MB)" +
                ",MD5='" + md5 + "'" +
                '}';
    }
}
