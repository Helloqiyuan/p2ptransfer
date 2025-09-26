# TCP文件传输工具

一个基于TCP协议的文件传输工具，支持IPv6地址和断点续传功能。

## 功能特点

- 基于TCP协议实现可靠文件传输
- 支持IPv6地址连接
- 具备断点续传功能
- 文件传输进度实时显示
- MD5校验保证文件完整性

## 项目结构

```
src/
└── main/
    └── java/
        └── com/qiyuan/
            ├── pojo/           # 数据传输对象
            ├── test/           # 程序入口
            ├── utils/          # 工具类
            └── TCPv2.java      # 支持断点续传的文件传输核心实现
```

## 使用方法

### 编译项目

```bash
mvn clean package
```

### 运行程序

项目有两个主要的入口类，用于发送和接收文件：
#### 编译

```bash
mvn package
java -jar tcptransform-2.0.jar
```
#### 接收文件

```bash
java -cp tcptransform-2.0.jar com.qiyuan.test.Receive
```

#### 发送文件


```bash
java -cp tcptransform-2.0.jar com.qiyuan.test.Send
```

## 配置说明

### TCPv2版本配置

在[TCPv2.java](src/main/java/com/qiyuan/TCPv2.java)中可以修改以下配置：

```java
private int port = 8888;        // 监听端口
private int cache = 4 * 1024;   // 缓冲区大小(4KB)
```

## 文件存储

- 发送文件: 通过文件选择器选择任意文件
- 接收文件: 自动保存在 `./Receive` 目录下

## 技术要点

1. 使用Java Socket编程实现TCP连接
2. 通过序列化传输文件元数据(Doc对象)
3. 断点续传通过记录已传输字节数实现
4. 使用Lombok简化JavaBean代码
5. Maven构建项目，使用shade插件打包

## 依赖项

- Lombok 1.18.40

## 注意事项

1. 确保网络环境支持IPv6
2. 防火墙需要允许指定端口通信(默认8888)
3. 发送大文件时请确保磁盘空间充足
4. 断点续传功能在接收方实现，会在Receive目录下创建.ucf临时文件