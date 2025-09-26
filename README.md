# TCP文件传输工具

一个基于TCP协议的文件传输工具，支持IPv6地址和断点续传功能。

## 功能特点

- 基于TCP协议实现可靠文件传输
- 支持IPv6地址连接
- 具备断点续传功能
- 文件传输进度实时显示
- MD5校验保证文件完整性（v1版本）(默认关闭)

## 项目结构

```
src/
└── main/
    └── java/
        └── com/qiyuan/
            ├── pojo/           # 数据传输对象
            ├── test/           # 测试主程序
            ├── utils/          # 工具类
            ├── TCP.java        # 基础版本文件传输(已弃用)
            └── TCPv2.java      # 支持断点续传的版本
```

> **已弃用标识说明**
> - `TCP.java`: 已弃用的旧版本文件传输实现
> - `test/Main.java`: 已弃用的主程序入口
> 
> 推荐使用 `TCPv2.java` 及其对应的测试类 `test/Send.java` 和 `test/Receive.java`

## 版本说明

### TCP.java (v1版本) - [!已弃用]
- 基础的文件发送和接收功能
- 支持MD5校验确保文件完整性
- 实时显示传输进度

### TCPv2.java (v2版本)
- 支持断点续传功能
- 自动检测已接收部分并继续传输
- 更好的用户体验

## 使用方法

### 编译项目

```bash
mvn clean package
```

### 运行程序

#### 方式一：使用主程序（已弃用）

```bash
java -jar target/tcptransform-1.0-SNAPSHOT.jar
# 选择选项1: 接收文件
# 程序会显示可用的IPv6地址，将其中一个提供给发送方
```

```bash
java -jar target/tcptransform-1.0-SNAPSHOT.jar
# 选择选项2: 发送文件
# 输入接收方的IPv6地址和要发送的文件路径
```

#### 方式二：使用独立的发送/接收程序（推荐）

```bash
# 接收文件
java -cp target/tcptransform-1.0-SNAPSHOT.jar com.qiyuan.test.Receive
```

```bash
# 发送文件
java -cp target/tcptransform-1.0-SNAPSHOT.jar com.qiyuan.test.Send
```

> 注：推荐使用方式二，方式一中的主程序(test/Main.java)已被标记为弃用

## 配置说明

### TCPv2版本配置

在[TCPv2.java](src/main/java/com/qiyuan/TCPv2.java)中可以修改以下配置：

```java
private int port = 8888;                // 监听端口
private Integer cache = 4 * 1024;       // 缓冲区大小(4KB)
private String path = "./send/yy.iso";  // 发送文件路径
private String TargetIPv6 = "xxxx:xxxx:xxxx:xxxx:xxxx:xxxx:xxxx:xxxx"; // 目标IPv6地址
```

## 文件存储

- 发送文件: 放在 `send` 目录下
- 接收文件: 自动保存在 `Receive` 目录下

## 技术要点

1. 使用Java Socket编程实现TCP连接
2. 通过序列化传输文件元数据(Doc对象)
3. 断点续传通过记录已传输字节数实现
4. 使用Lombok简化JavaBean代码
5. Maven构建项目，使用shade插件打包

## 依赖项

- Lombok 1.18.40
- JUnit 3.8.1 (测试)
- JUnit Jupiter 5.13.4 (测试)

## 注意事项

1. 确保网络环境支持IPv6
2. 防火墙需要允许指定端口通信(默认8888)
3. 发送大文件时请确保磁盘空间充足
4. 断点续传功能在v2版本中实现

## 未来改进

- [ ] 添加图形用户界面
- [ ] 支持多文件同时传输
- [ ] 增加传输速度显示
- [ ] 支持压缩传输
- [ ] 添加文件加密功能