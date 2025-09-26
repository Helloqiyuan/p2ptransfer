package com.qiyuan.utils;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Utils {
    public static List<String> getLocalIPv6() throws SocketException {
        List<String> ipv6List = new ArrayList<>();
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {
            NetworkInterface ni = interfaces.nextElement();
            if (!ni.isUp() || ni.isLoopback() || ni.isVirtual()) continue;

            Enumeration<InetAddress> addresses = ni.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                if (addr instanceof Inet6Address && !addr.isLoopbackAddress()) {
                    ipv6List.add(addr.getHostAddress());
                }
            }
        }
        return ipv6List
                .stream()
                .filter(ip -> !ip.startsWith("fe80"))
                .collect(Collectors.toList());
    }


    public static String getFileChecksum(File file) throws Exception {
        long start = System.currentTimeMillis();
        String md5 = getFileChecksum(file, "MD5");
        long end = System.currentTimeMillis();
        System.out.println("计算完成,耗时:" + (end - start) / 1000.0 + "s");
        return md5;
    }

    /**
     * 计算文件的哈希值
     *
     * @param file      文件对象
     * @param algorithm 算法名 ("MD5", "SHA-1", "SHA-256" 等)
     * @return 十六进制的哈希字符串
     * @throws Exception
     */
    public static String getFileChecksum(File file, String algorithm) throws Exception {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        InputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[128 * 1024 * 1024]; // 128MB缓冲区
        int n;
        long done = 0L;

        while ((n = fis.read(buffer)) != -1) {
            digest.update(buffer, 0, n);
            done += n;
            Utils.loading(done, file.length());
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

    public static String chooseFile() {
        Frame frame = new Frame();
        frame.setAlwaysOnTop(true); // 设置窗口始终在顶部
        FileDialog dialog = new FileDialog(frame, "选择文件", FileDialog.LOAD);
        dialog.setAlwaysOnTop(true); // 设置对话框始终在顶部
        dialog.setVisible(true);

        String dir = dialog.getDirectory();
        String file = dialog.getFile();
        if (file != null) {
            return new File(dir, file).getAbsolutePath();
        }
        return null; // 用户取消时返回 null
    }

    // IPv6 正则表达式
    private static final String IPV6_REGEX =
            "(([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|" +               // 完整形式
                    "([0-9a-fA-F]{1,4}:){1,7}:|" +                            // :: 结尾
                    "([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|" +            // 单个省略
                    "([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|" +
                    "([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|" +
                    "([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|" +
                    "([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|" +
                    "[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|" +
                    ":((:[0-9a-fA-F]{1,4}){1,7}|:)|" +                        // :: 开头
                    "fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|" +        // 链路本地
                    "::(ffff(:0{1,4}){0,1}:){0,1}" +
                    "((25[0-5]|(2[0-4][0-9])|(1[0-9]{2})|([1-9]?[0-9]))\\.){3,3}" +
                    "(25[0-5]|(2[0-4][0-9])|(1[0-9]{2})|([1-9]?[0-9]))|" +    // IPv4 映射
                    "([0-9a-fA-F]{1,4}:){1,4}:" +
                    "((25[0-5]|(2[0-4][0-9])|(1[0-9]{2})|([1-9]?[0-9]))\\.){3,3}" +
                    "(25[0-5]|(2[0-4][0-9])|(1[0-9]{2})|([1-9]?[0-9])))";

    private static final Pattern IPV6_PATTERN = Pattern.compile(IPV6_REGEX);

    // 检测 IPv6 合法性
    public static boolean isValidIPv6(String ip) {
        return ip != null && IPV6_PATTERN.matcher(ip).matches();
    }

    /**
     * 获取一个可用的端口
     *
     * @return 端口号
     */
    public static int getAvailablePort() {
        ServerSocket socket;
        int begin = 1024;
        int end = 65535;
        for (int i = begin; i <= end; i++) {
            try {
                socket = new ServerSocket(i);
                socket.close();
                return i;
            } catch (Exception e) {
            }
        }
        throw new RuntimeException("无可用端口");
    }

    /**
     * 显示进度和速度
     * @param done 已完成字节数 byte
     * @param total 总字节数 byte
     */
    public static void loading(Long done, Long total) {
        double percent = done * 100.0 / total;
        if (done.equals(total)) {
            System.out.print("\r进度: 100%");
            return;
        }
        System.out.printf("\r进度: %.3f%%", percent);
        System.out.flush();
    }
}
