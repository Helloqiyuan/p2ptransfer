package com.qiyuan.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class HashUtils {
    public static String getFileChecksum(File file) throws Exception {
        return getFileChecksum(file, "MD5");
    }
    /**
     * 计算文件的哈希值
     * @param file 文件对象
     * @param algorithm 算法名 ("MD5", "SHA-1", "SHA-256" 等)
     * @return 十六进制的哈希字符串
     * @throws Exception
     */
    public static String getFileChecksum(File file, String algorithm) throws Exception {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        InputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[512 * 1024 * 1024]; // 512MB 缓冲区
        int n;
        while ((n = fis.read(buffer)) != -1) {
            digest.update(buffer, 0, n);
        }
        fis.close();

        byte[] hashBytes = digest.digest();
        // 转换为16进制字符串
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
