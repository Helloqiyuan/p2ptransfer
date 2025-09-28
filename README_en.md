# P2P File Transfer Tool

A file transfer tool based on Java Socket, supporting IPv6 addresses and breakpoint resume functionality.

## Features

- Reliable file transfer implemented based on Java Socket
- Support for IPv6 address connections
- Breakpoint resume functionality
- Real-time display of file transfer progress
- MD5 checksum to ensure file integrity
- Automatic acquisition of available ports
- Automatic detection of local IPv6 addresses

## Project Structure

```
src/
└── main/
    └── java/
        └── com/qiyuan/
            ├── P2P.java        # Core file transfer implementation
            ├── pojo/           # Data transfer objects
            ├── test/           # Program entry points
            └── utils/          # Utility classes
```

## Usage

### Build the Project

```bash
mvn clean package
```

### Running the Program

The project has two main entry classes for sending and receiving files:

#### Receiving Files

```bash
java -jar p2p.jar
```

Or

```bash
java -cp p2p.jar com.qiyuan.test.Receive
```

#### Sending Files

```bash
java -cp p2p.jar com.qiyuan.test.Send
```

## Configuration

You can modify the following configurations in [P2P.java](src/main/java/com/qiyuan/P2P.java):

```java
private int cache = 1024 * 1024; // Buffer size (1MB)
```

## File Storage

- Sending files: Select any file via drag-and-drop or file selector
- Receiving files: Automatically saved in the `./Receive` directory

## Technical Highlights

1. TCP connection implemented using Java Socket programming
2. File metadata (Doc object) transferred via serialization
3. Breakpoint resume implemented by recording transferred bytes
4. Lombok used to simplify JavaBean code
5. Maven project built with shade plugin packaging
6. Support for large file transfer and progress display
7. Automatic IPv6 address detection and port allocation

## Dependencies

- Lombok 1.18.40

## Notes

1. Ensure network environment supports IPv6
2. Firewall needs to allow communication on specified ports (uses automatically assigned port by default)
3. Ensure sufficient disk space when sending large files
4. Breakpoint resume functionality is implemented on the receiving side, which creates .ucf temporary files in the Receive directory
5. The program defaults to using the receiver as the JAR package main class (specified via pom.xml configuration)